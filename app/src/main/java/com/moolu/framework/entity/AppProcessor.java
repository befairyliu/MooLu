package com.moolu.framework.entity;

import android.app.Dialog;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moolu.R;
import com.moolu.activity.NananActivity;
import com.moolu.framework.Constants;
import com.moolu.framework.entity.model.Center;
import com.moolu.framework.entity.model.WebResource;
import com.moolu.util.ActivityUtil;
import com.moolu.util.DeviceUtil;
import com.moolu.util.ProcessUtil;
import com.moolu.util.SharePrefenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nanan on 2/26/2015.
 */
public class AppProcessor {
    //TODO....

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
                int type=DeviceUtil.getNetworkType(context);
                if(type==DeviceUtil.NETTYPE_WIFI){
                    downloadZip(context, app);
                    SharePrefenceUtil.putValue(context,Constants.ISFIRSTTIMEDOWNLOADZIP,Constants.FIRSTTIMEDOWNLOADZIP);
                }else{
                    //TODO....
                    /*showDownloadConfirmDialog(context, null, app, context.getResources().getString(R.string.zipdownloadtitle),
                            context.getResources().getString(R.string.zipdownloadMsg), Constants.DIAGLOGFIRSTTIME);*/
                }
            }
        }
    }

    private void downloadZip(NananActivity context, Center app) {
        //TODO....
        /*showProgressBar();
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
        SharePrefenceUtil.saveHashCode(map, context);*/
    }
}
