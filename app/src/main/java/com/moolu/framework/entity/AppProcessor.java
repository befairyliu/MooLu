package com.moolu.framework.entity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moolu.R;
import com.moolu.activity.MainBrowserActivity;
import com.moolu.activity.NananActivity;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.framework.entity.model.AppUpdateResource;
import com.moolu.framework.entity.model.Center;
import com.moolu.framework.entity.model.Entity;
import com.moolu.framework.entity.model.WebResource;
import com.moolu.storage.prefs.PrefConstants;
import com.moolu.util.ActivityUtil;
import com.moolu.util.DeviceUtil;
import com.moolu.util.FileUtil;
import com.moolu.util.ProcessUtil;
import com.moolu.util.SharePrefenceUtil;
import com.moolu.util.StringUtil;

import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nanan on 2/26/2015.
 */
public class AppProcessor implements ActivityCallback{

    private final static Logger Log = new NananLog(AppProcessor.class);

    private static AppProcessor processor;
    private String newWebResourcePath;
    private String globalResourcePath;
    private NananActivity context;
    Button btnCancel;
    private Dialog downloadPd = null;
    private ProgressBar progresBar = null;
    private TextView progressText = null;
    private TextView progressConentText = null;
    private int completedResourceTaskCount = 0;
    private int completedExtreactTaskCount = 0;
    private String downloadDialogTitleWithoutCancelOption = null;
    private String downloadProgressDialogMessagewithCancelOption = null;
    public String downloadDialogCancel = null;
    public String cancel = null;
    public String downloadUpdateComplete = null;
    private ArrayList<TaskParam> resourceMap = new ArrayList<TaskParam>();
    private String homePage;
    private long totalSize = 0;
    private long downloadedSize = 0;
    private String msg;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PrefConstants.DOWNLOAD_PROGRESS_UPDATE_MSG:
                    updateDownloadProgressRate(msg.arg1);
                    break;
            }
        }
    };

    public static AppProcessor getInstance() {
        synchronized (AppProcessor.class) {
            if (processor == null) {
                processor = new AppProcessor();
            }
            return processor;
        }
    }

    public void prepareWebResourceForApp(NananActivity context, Handler handler, Center app) {
        this.context = context;
        downloadedSize = 0;
        if(SharePrefenceUtil.getValue(context, Constants.ISFIRSTTIMEDOWNLOADZIP, "").equals(Constants.FIRSTTIMEDOWNLOADZIP)){
            downloadZip(context, app);
        }else{
            if(!DeviceUtil.isNetworkConnected(context)){
                ActivityUtil.createDialog(context, context.getResources().getString(R.string.dialog_fire_missiles),
                        context.getResources().getString(R.string.buildtitle), Constants.SYSTEMEXIT).show();
            }else{
                int type = DeviceUtil.getNetworkType(context);
                if(type == DeviceUtil.NETTYPE_WIFI){
                    downloadZip(context, app);
                    SharePrefenceUtil.putValue(context,Constants.ISFIRSTTIMEDOWNLOADZIP,Constants.FIRSTTIMEDOWNLOADZIP);
                }else{
                    showDownloadConfirmDialog(context, null, app, context.getResources().getString(R.string.zipdownloadtitle),
                            context.getResources().getString(R.string.zipdownloadMsg), Constants.DIAGLOGFIRSTTIME);
                }
            }
        }
    }

    private void downloadZip(NananActivity context, Center app) {
        showProgressBar();
        updateSharedPreference(app);
        msg = context.getResources().getString(R.string.completely);
        List<WebResource> webResources = app.getWebResource();
        newWebResourcePath = ProcessUtil.getNewWebResourcePath(context, app.getRegionId(), DeviceUtil.getReleaseVersion());
        globalResourcePath = ProcessUtil.getGlobalResourcePath(context, app.getRegionId());
        doDownloadZipTask(app, this.handler, webResources, DeviceUtil.getReleaseVersion());
        Map map=new HashMap();
        for(int i=0;i<app.getWebResource().size();i++){
            WebResource obj=app.getWebResource().get(i);
            map.put(app.getRegionId()+"_"+ProcessUtil.getFileNameByURL(obj.getZipUrl()),obj.getHashCode());
        }
        SharePrefenceUtil.saveHashCode(map, context);
    }

    protected void updateDownloadProgressRate(int updatedSize) {
        try {
            downloadedSize += updatedSize;
            int percent = (int) (downloadedSize * 100 / totalSize);
            percent = percent > 100 ? 100 : percent;
            progresBar.setProgress(percent);
            progressText.setText(percent + "% " + msg);

        } catch (Exception e) {
            // show genarate error box
            Log.error("updateDownloadProgressRate error", e);
        }
    }

    @Override
    public void handleCallback(AsyncTaskWithCallback task, int ref) {
        try {
            switch (ref) {
                case PrefConstants.WEBRESOURCE_REF:
                    handleResourcePrepareCallback((WebResourceTask) task);
                    break;
                case PrefConstants.PREPATE_UNZIP_RESOURCE_TASK_REF:
                    handleUnzipCallback((UnzipResourceTask) task);
                    break;
                case PrefConstants.PREPATE_DOWNLOAD_APK_TASK_REF:
                    handleApkDownloadCallback((ApkDownloadTask) task);
                    break;
            }
        } catch (ResourcePrepareException e) {
            handleException(e);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleApkDownloadCallback(final ApkDownloadTask task) throws ResourcePrepareException {
        dismissProgressBar();
        String fileName = task.getResult().getZipDownloadPath() + task.getResult().getZipFileName();
        if (AsyncTaskWithCallback.SUCCESS == task.getError()) {
            File file = new File(fileName);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            Log.debug("appprocess", "apk install failed");
            ActivityUtil.showErrorMsg(context,"download apk error",Constants.ERRORAPK);
            DeviceUtil.deleteFile(fileName);
        }
    }

    private void handleUnzipCallback(final UnzipResourceTask task) throws ResourcePrepareException {
        completedExtreactTaskCount++;
        int errorCode = task.getError();
        if (AsyncTaskWithCallback.SUCCESS == errorCode) {
            unzipTaskSuccess(task.getResult());
        } else if (AsyncTaskWithCallback.FAILED == errorCode) {
            ActivityUtil.showErrorMsg(context,"download zip error",Constants.ERRORZIP);
        }
    }

    private void unzipTaskSuccess(TaskParam param) throws ResourcePrepareException {
        this.lastExtractTaskHandle(param);
    }

    private void lastExtractTaskHandle(TaskParam param) throws ResourcePrepareException {
        if (completedExtreactTaskCount == resourceMap.size()) {
            // all resource task completed
            for (int i = 0; i < resourceMap.size(); i++) {
                DeviceUtil.deleteFile(resourceMap.get(i).getZipDownloadPath()
                        + File.separator + resourceMap.get(i).getZipFileName());
            }
            resourceMap.clear();
            resourceMap = null;
            Intent intent = new Intent(context, MainBrowserActivity.class);
            intent.putExtra("path", param.getUnzipedWebResourcePath());
            intent.putExtra("homepage", this.homePage);
            context.startActivity(intent);
            dismissProgressBar();
            context.finish();
        }
    }

    private void handleException(Exception e) {
        //to do something
    }

    public void updateSharedPreference(Center app) {
        int length = app.getWebResource().size();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < length; i++) {
            String key = app.getRegionId() + "_" + ProcessUtil.getFileNameByURL(app.getWebResource().get(i).getZipUrl());
            map.put(key, app.getWebResource().get(i).getHashCode());
        }
        SharePrefenceUtil.putValues(context, map);
    }

    private void handleResourcePrepareCallback(final WebResourceTask task) throws ResourcePrepareException {
        completedResourceTaskCount++;
        int errorCode = task.getError();
        task.getResult().getWebResource().getHashCode();
        if (AsyncTaskWithCallback.SUCCESS == errorCode) {
            downloadTaskSuccess(task.getResult());
        } else if (AsyncTaskWithCallback.FAILED == errorCode) {
            // resourceFailCallback(task.getResult(),true);
            ActivityUtil.showErrorMsg(context,"download downZip error",Constants.ERRORZIP);
        }
    }

    private void downloadTaskSuccess(TaskParam param) throws ResourcePrepareException {
        //Mark the function table to enable.
        this.lastDownloadTaskHandle(param);
    }

    public void lastDownloadTaskHandle(TaskParam param) throws ResourcePrepareException {
        //TaskCount is max size and all task not success then throw exception.
        if (completedResourceTaskCount == resourceMap.size()) {
            executeUnzipTasks(param);
        }
    }

    private void executeUnzipTasks(TaskParam param) throws ResourcePrepareException {
        downloadedSize = 0;
        progressConentText.setText(R.string.unzip);
        /**remove the download fail optional task**/
        // resourceMap.clear();
        for (TaskParam arg : resourceMap) {
            if (ProcessConstants.BUNDLED_TYPE.equals(arg.getType())) {
                //if the task is prepare bundled resource then do nothing
            } else if (arg.getUnzipedWebResourcePath() != null && arg.getZipFileName() != null) {
                UnzipResourceTask task = new UnzipResourceTask(context, this,
                        PrefConstants.PREPATE_UNZIP_RESOURCE_TASK_REF, handler);

                // context.addTask(task);
                task.execute(arg);
            } else {
                //throw exception if config error
                throw new ResourcePrepareException("resource config error");
            }
        }
    }

    private void doDownloadZipTask(Center app, Handler handler, List<WebResource> webResource, String webVersion) {
        this.homePage = app.getHomePage();
        this.loopZipConfig(app, webVersion, newWebResourcePath, globalResourcePath);
        for (TaskParam param : resourceMap) {
            WebResourceTask webResourceTask = new WebResourceTask(context, this, handler, PrefConstants.WEBRESOURCE_REF);
            webResourceTask.execute(param);
        }
    }

    private void loopZipConfig(Center localZipList, String webVersion, String unzipedWebResourcePath,
                               String zipDownloadPath) {
        if (localZipList != null) {
            List<WebResource> urls = localZipList.getWebResource();
            WebResource webResources = null;
            for (int i = 0; i < urls.size(); i++) {
                String fileNameByURL = ProcessUtil.getFileNameByURL(urls.get(i).getZipUrl());
                TaskParam param = new TaskParam();
                webResources = new WebResource();
                webResources.setZipSize(urls.get(i).getZipSize());
                totalSize += StringUtil.string2Long(urls.get(i).getZipSize());
                webResources.setZipUrl(urls.get(i).getZipUrl());
                param.setEid(localZipList.getRegionId());
                param.setWebVersion(webVersion);
                param.setUnzipedWebResourcePath(unzipedWebResourcePath);
                // param.setType(processType);
                param.setZipDownloadPath(zipDownloadPath);
                param.setZipFileName(fileNameByURL);
                param.setWebResource(webResources);
                //param.setZipInfo(zipInfo);
                /**Handle always unzip,if is aways unzip then no need check record**/
                resourceMap.add(param);
            }
        }
    }

    protected void showProgressBar() {
        /** This is logon progress bar logic **/
        dismissProgressBar();
        RelativeLayout layoutLoading = (RelativeLayout) context.findViewById(R.id.layout_loading);
        if (layoutLoading != null) {
            View splashLoading = layoutLoading.findViewById(R.id.splashloadingSpinner);
            splashLoading.setVisibility(View.INVISIBLE);
        }
        if (downloadPd != null) {
            if (downloadPd.isShowing()) {
                downloadPd.show();
            }
        } else {
            initDownloadProgress();
            // downloadPd.show();
        }
    }

    private void dismissProgressBar() {
        try {
            if (downloadPd != null) {
                downloadPd.dismiss();
                downloadPd = null;
            }
        } catch (Exception e) {
             Log.debug("Dismiss Progress Bar Error",e);
        }
    }

    protected void initDownloadProgress() {
        RelativeLayout layoutLoading = (RelativeLayout) context.findViewById(R.id.layout_loading);
        if (layoutLoading != null && layoutLoading.getVisibility() != View.INVISIBLE) {
            downloadPd = new Dialog(context, R.style.dialog_not_dim);
            downloadPd.setContentView(R.layout.splash_download_progress_dialog);
        } else {
            downloadPd = new Dialog(context, R.style.FullHeightDialog);
            downloadPd.setContentView(R.layout.download_progress_dialog);
        }

        downloadPd.setCanceledOnTouchOutside(false);
        downloadPd.setCancelable(false);//comment for test

        progressConentText = (TextView) downloadPd.findViewById(R.id.progressBarTitle);
        progresBar = (ProgressBar) downloadPd.findViewById(R.id.progressBar);
        progressText = (TextView) downloadPd.findViewById(R.id.progressPercent);
        btnCancel = (Button) downloadPd.findViewById(R.id.btnCancel);
        downloadPd.show();

        boolean cancelBtnShow = false;

        if (cancelBtnShow) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
            if (true){
                // for 1.5.3
                if (!StringUtil.IsNullOrEmpty(cancel)) {
                    btnCancel.setText(cancel);
                }
            } else{
                // 1.5.4
                if (!StringUtil.IsNullOrEmpty(downloadDialogCancel)) {
                    btnCancel.setText(downloadDialogCancel);
                }
            }

            if (!StringUtil.IsNullOrEmpty(downloadProgressDialogMessagewithCancelOption)) {
                progressConentText.setText(downloadProgressDialogMessagewithCancelOption);
            }
        } else {
            btnCancel.setVisibility(View.GONE);
            if (!StringUtil.IsNullOrEmpty(downloadDialogTitleWithoutCancelOption)) {
                progressConentText.setText("nihao");
            }
        }
        if (downloadUpdateComplete == null) {
            progressText.setText(0 + "%");
        } else {
            progressText.setText(0 + "% " + downloadUpdateComplete);
        }
        progresBar.setMax(ProcessConstants.MAX_PROGRESS);
    }

    public void prepareToAppDownload(NananActivity context, Entity entity) {
        showDownloadConfirmDialog(context, entity, null, null, null, Constants.DIAGLOGAPPUPDATE);
    }

    public void showDownloadConfirmDialog(final NananActivity context, final Entity entity,
                                          final Center center, String title, String message, final int type) {
        this.context = context;
        RelativeLayout layoutLoading = (RelativeLayout) context.findViewById(R.id.layout_loading);
        int dialogStyle = R.style.FullHeightDialog;
        if (layoutLoading != null && layoutLoading.getVisibility() != View.VISIBLE) {
            dialogStyle = R.style.dialog_not_dim;
        }
        final Dialog dialog = new Dialog(context, dialogStyle);
        /*
        if(downloadDialogMessage==null || resourceSize<1000){
            executeAllDownloadTask();
            return;
        }
        */

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.download_confirm_dialog);
        TextView titles = null;
        TextView messages = null;
        switch (type) {
            case Constants.DIAGLOGAPPUPDATE:
                titles = (TextView) dialog.findViewById(R.id.tv_title);
                messages = (TextView) dialog.findViewById(R.id.tv_message);
                messages.setText(context.getResources().getString(R.string.downloadAppMessage));
                break;
            case Constants.DIAGLOGZIPUPDATE:
                titles = (TextView) dialog.findViewById(R.id.tv_title);
                titles.setText(title);
                messages = (TextView) dialog.findViewById(R.id.tv_message);
                messages.setText(message);
                break;
            case Constants.DIAGLOGFIRSTTIME:
                titles = (TextView) dialog.findViewById(R.id.tv_title);
                titles.setText(title);
                messages = (TextView) dialog.findViewById(R.id.tv_message);
                messages.setText(message);
                break;
            case Constants.CONFIRMDIALOG:
                titles = (TextView) dialog.findViewById(R.id.tv_title);
                titles.setText(title);
                messages = (TextView) dialog.findViewById(R.id.tv_message);
                messages.setText(message);
                break;
        }
        Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_button_cancel);

        boolean cancelBtnShow = true;// this.isCancelBtnShowOnDownloadConfirmDialog();
        if (!cancelBtnShow) {
            cancelBtn.setVisibility(View.GONE);
            if (!StringUtil.IsNullOrEmpty(downloadDialogTitleWithoutCancelOption)) {
                titles.setText(downloadDialogTitleWithoutCancelOption);
            }
        } else {
            cancelBtn.setVisibility(View.VISIBLE);
            if (!StringUtil.IsNullOrEmpty(downloadDialogCancel)) {
                cancelBtn.setText(downloadDialogCancel);
            }

        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case Constants.DIAGLOGAPPUPDATE:
                        startSelectActivity(dialog, context, entity);
                        break;
                    case Constants.CONFIRMDIALOG:
                        startSelectActivity(dialog, context, entity);
                        break;
                    case Constants.DIAGLOGZIPUPDATE:
                        Intent intents = new Intent(context, MainBrowserActivity.class);
                        intents.putExtra("path", ProcessUtil.getNewWebResourcePath(context, center.getRegionId(), DeviceUtil.getReleaseVersion()));
                        intents.putExtra("homepage", center.getHomePage());
                        context.startActivity(intents);
                        context.finish();
                        break;
                    case Constants.DIAGLOGFIRSTTIME:
                        SharePrefenceUtil.putValue(context,Constants.ISFIRSTTIMELOGON,"");
                        System.exit(0);
                }
            }
        });

        Button okBtn = (Button) dialog.findViewById(R.id.dialog_button_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case Constants.DIAGLOGAPPUPDATE:
                        dialog.dismiss();
                        checkInternet(context, entity);
                        break;
                    case Constants.CONFIRMDIALOG:
                        dialog.dismiss();
                        executePrepareDownloadApp(context, entity);
                        break;
                    case Constants.DIAGLOGZIPUPDATE:
                        dialog.dismiss();
                        FileUtil.deleteDirectory(ProcessUtil.getNewWebResourcePath(context, center.getRegionId(), DeviceUtil.getReleaseVersion()));
                        AppProcessor.getInstance().prepareWebResourceForApp(context, handler, center);
                        break;
                    case Constants.DIAGLOGFIRSTTIME:
                        dialog.dismiss();
                        downloadZip(context, center);
                        SharePrefenceUtil.putValue(context,Constants.ISFIRSTTIMEDOWNLOADZIP,Constants.FIRSTTIMEDOWNLOADZIP);
                        break;
                }

            }
        });
    }

    private void startSelectActivity(Dialog dialog, NananActivity context, Entity entity) {
        dialog.dismiss();
        if (!SharePrefenceUtil.getValue(context, Constants.ISFIRSTTIMELOGON, "").equals(Constants.FIRSTTIMELOGON)) {
            ActivityUtil.startAppSelectActivity(context);
            context.finish();
        } else {
            compareToDownloadZip(entity, context);
        }
    }

    public void compareToDownloadZip(Entity result, NananActivity context) {
        this.context = context;
        int length = result.getCenter().size();
        //checkIsFirstLogon(this.context, result);
        int total = 0;
        // List<WebResource> webResource=null;
        // List<Center> centers= result.getCenter();
        for (int i = 0; i < length; i++) {
            Center center = result.getCenter().get(i);
            String id = center.getRegionId();
            int webLength = center.getWebResource().size();
            for (int j = 0; j < webLength; j++) {
                String fileName = ProcessUtil.getFileNameByURL(center.getWebResource().get(j).getZipUrl());
                String key = id + "_" + fileName;
                String hashCode = SharePrefenceUtil.getValue(context, key, "");
                if (!hashCode.equals("")) {
                    if (!center.getWebResource().get(j).getHashCode().equals(hashCode)) {//need update
                        if (!DeviceUtil.isNetworkConnected(context)) {
                            ActivityUtil.createDialog(context, context.getResources().getString(R.string.dialog_fire_missiles),
                                    context.getResources().getString(R.string.buildtitle), Constants.DIALOGDISMISS).show();
                        } else {
                            if (DeviceUtil.getNetworkType(context) == DeviceUtil.NETTYPE_WIFI) {
                                FileUtil.deleteDirectory(ProcessUtil.getNewWebResourcePath(context, id, DeviceUtil.getReleaseVersion()));
                                AppProcessor.getInstance().prepareWebResourceForApp(context, handler, center);
                                break;
                            } else {
                                AppProcessor.getInstance().showDownloadConfirmDialog(context, result, center, context.getResources().getString(R.string.zipupdatetitle),
                                        context.getResources().getString(R.string.zipupdateMsg), Constants.DIAGLOGZIPUPDATE);
                            }
                        }
                    } else {
                        if (total == center.getWebResource().size() - 1) {
                            Intent intent = new Intent(context, MainBrowserActivity.class);
                            intent.putExtra("path", ProcessUtil.getNewWebResourcePath(context, id, DeviceUtil.getReleaseVersion()));
                            intent.putExtra("homepage", center.getHomePage());
                            context.startActivity(intent);
                            // loadingDialog.dismiss();
                            context.finish();
                        }
                        total++;
                    }
                }

            }

        }
    }

    private void checkInternet(Context context, Entity entity) {
        if (!DeviceUtil.isNetworkConnected(context)) {
            ActivityUtil.createDialog(context, context.getResources()
                    .getString(R.string.dialog_fire_missiles), context.getResources()
                    .getString(R.string.buildtitle), Constants.SYSTEMEXIT).show();
        } else {
            int type= DeviceUtil.getNetworkType(context);
            if(type==DeviceUtil.NETTYPE_WIFI){
                executePrepareDownloadApp(context, entity);
            }else{
                showDownloadConfirmDialog(this.context, entity, null, context.getResources().getString(R.string.confirmdialogtitle),
                        context.getResources().getString(R.string.confirmdialogmsg), Constants.CONFIRMDIALOG);
            }
        }
    }

    private void executePrepareDownloadApp(Context context, Entity entity) {
        globalResourcePath = null;
        TaskParam taskParam = new TaskParam();
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            globalResourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Update/";
            File file = new File(globalResourcePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            ActivityUtil.createDialog(context, context.getResources().getString(R.string.sdcarderrormsg),
                    context.getResources().getString(R.string.sdcarderrortitle), Constants.SYSTEMEXIT);
        }
        showProgressBar();
        executeDownloadApk(context, entity, taskParam);
    }

    private void executeDownloadApk(Context context, Entity entity, TaskParam taskParam) {
        ApkDownloadTask apkDownloadTask = new ApkDownloadTask(this.context, this, handler, PrefConstants.PREPATE_DOWNLOAD_APK_TASK_REF);
        progressConentText.setText(R.string.downloadApp);
        AppUpdateResource appUpdateResource = new AppUpdateResource();
        totalSize = StringUtil.string2Long(entity.getAppConfig().getAppUpdate().getAppSize());
        msg = context.getResources().getString(R.string.completely);
        String appUrl = entity.getAppConfig().getAppUpdate().getAppUrl();
        String fileName = ProcessUtil.getFileNameByURL(appUrl);
        appUpdateResource.setAppUrl(appUrl);
        taskParam.setAppUpdateResource(appUpdateResource);
        taskParam.setZipDownloadPath(globalResourcePath);
        taskParam.setZipFileName(fileName);
        File file = new File(globalResourcePath + File.separator + fileName);
        if (file.exists()) {
            DeviceUtil.deleteFile(globalResourcePath + File.separator + fileName);
        }
        apkDownloadTask.execute(taskParam);
    }
}
