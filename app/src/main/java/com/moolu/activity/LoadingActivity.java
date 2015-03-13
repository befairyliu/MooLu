package com.moolu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import com.moolu.R;
import com.moolu.application.MApplication;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.framework.entity.AppProcessor;
import com.moolu.framework.entity.EntityListParserException;
import com.moolu.framework.entity.EntityTask;
import com.moolu.framework.entity.EntityUtil;
import com.moolu.framework.entity.model.Center;
import com.moolu.framework.entity.model.Entity;
import com.moolu.util.ActivityUtil;
import com.moolu.util.DeviceUtil;
import com.moolu.util.SharePrefenceUtil;
import com.moolu.util.StringUtil;

import org.slf4j.Logger;

public class LoadingActivity extends NananActivity implements ActivityCallback{

    private final static Logger Log = new NananLog(LoadingActivity.class);
    public static final int ENTITY_TASK_REF = 1;
    public static final int APPUPDATE_TASK_REF = 2;
    private static final int ACTION = 1;
    private MApplication application;
    private java.util.Date begin = new java.util.Date();
    AppProcessor resourceProcessor;

    public final static int WEB_RESOURCE_FINISH = 10;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case WEB_RESOURCE_FINISH:
                        ActivityUtil.startAppSelectActivity(LoadingActivity.this);
                        finish();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

         /*for test html*/
        if("true".equals(getResources().getString(R.string.openInbrowner))){
            Intent intent = new Intent(this, MainBrowserActivity.class);
            intent.putExtra("path", "");
            this.startActivity(intent);
        }else{

            application = (MApplication) getApplication();
            if (!DeviceUtil.isNetworkConnected(this)) {
                ActivityUtil.createDialog(this, getResources().getString(R.string.dialog_fire_missiles),
                        getResources().getString(R.string.buildtitle), Constants.SYSTEMEXIT).show();

            } else {
                if (application.getEntity() == null) {
                    excuteEntityTask();
                } else {
                    startAppFlow();
                }
            }
        }
        /*for test html*/
    }

    private void startAppFlow() {

        String currentAppId = EntityUtil.getCurrentAppId(this);
        if (!StringUtil.IsNullOrEmpty(currentAppId)) {
            startAppInitFlow(currentAppId);

        } else {
            Intent intent = new Intent(this, AppSelectActivity.class);
            startActivity(intent);
            this.slideBottomToTop();
            this.finish();
        }
    }

    private void startAppInitFlow(String savedApp) {
        Entity entity=application.getEntity();
        Center app = EntityUtil.getApp(savedApp, entity);
        if (app != null) {
            try {
                resourceProcessor = AppProcessor.getInstance();
                resourceProcessor.prepareWebResourceForApp(this, mHandler, app);
                //finish();
            } catch (Exception e) {
                e.printStackTrace();
                ActivityUtil.showErrorMsg(this,"download Zip error",Constants.ERRORZIP);
            }
        }
    }

    private void excuteEntityTask() {
        try {
            EntityTask task = new EntityTask(this, this, ENTITY_TASK_REF);
            addTask(task);
            task.execute();
        } catch (Exception e) {
            Log.error("excuteEntityTask error");
        }
    }

    @SuppressWarnings("rawtypes")
    public void handleCallback(final AsyncTaskWithCallback task, final int ref) {
        super.handleCallback(task, ref);
        try {
            switch (ref) {
                case ENTITY_TASK_REF:
                    handleEntityCallback((EntityTask) task);
                    break;

            }
        } catch (Exception e) {
            this.showDialog(Constants.NETWORK_ERROR_SELECT_COUNTRY_DIALOG);
            Log.error("task error", e);
        }
    }

    public void handleEntityCallback(final EntityTask task) throws EntityListParserException {
        try {
            int flag = task.getError();
            if (AsyncTaskWithCallback.SUCCESS == flag) {
                Entity result = task.getResult();

                if (result != null) {
                    ((MApplication) getApplication()).setEntity(result);
                    String serverVersion = result.getAppConfig().getAppUpdate().getAppVersion();
                    String version = DeviceUtil.getAppVersion(this);
                    resourceProcessor = AppProcessor.getInstance();
                    if (serverVersion.equals(version)||Float.parseFloat(serverVersion)<Float.parseFloat(version)) {
                        if(!SharePrefenceUtil.getValue(this, Constants.ISFIRSTTIMELOGON, "").equals(Constants.FIRSTTIMELOGON)){
                            ActivityUtil.startAppSelectActivity(this);
                            finish();
                        }else{
                            resourceProcessor.compareToDownloadZip(result,this);
                            //finish();
                        }

                    } else {
                        resourceProcessor.prepareToAppDownload(this,result);
                        // finish();
                    }
                } else {
                    //throw new EntityListParserException("result is null");
                    ActivityUtil.showErrorMsg(this,"download entity error",Constants.ERRORENTITY);
                }
            } else {
                ActivityUtil.showErrorMsg(this,"download entity error",Constants.ERRORENTITY);
                // throw new EntityListParserException("entity task execute fail");
            }
            Log.debug("====after entity finish root:{}===== ", new java.util.Date().getTime() - begin.getTime());
        } catch (EntityListParserException e) {
            ActivityUtil.showErrorMsg(this,"download entity error",Constants.ERRORENTITY);
            throw e;
        } catch (Exception e) {
            ActivityUtil.showErrorMsg(this,"download entity error",Constants.ERRORENTITY);
            Log.error("handleEntityCallback error", e);
        }
    }
}
