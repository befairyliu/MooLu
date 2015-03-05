package com.moolu.framework.entity;

import android.os.Handler;

import com.moolu.activity.NananActivity;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.util.DownloadUtil;
import com.moolu.util.ProcessUtil;
import com.moolu.util.UnzipUtil;

import org.apache.http.client.ClientProtocolException;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by banliu on 2/26/2015.
 */
public class UnzipResourceTask extends AsyncTaskWithCallback<TaskParam, Void, TaskParam> {

    private final NananActivity context;
    private Handler handler;

    public UnzipResourceTask(final NananActivity context, final ActivityCallback callback,
                             final int ref, Handler handler){
        super(callback,ref);
        if(context == null){
            throw new IllegalArgumentException("owner must not be null");
        }
        this.context = context;
        this.handler = handler;
    }
    @Override
    protected TaskParam doInBackground(TaskParam... params) {
        TaskParam param = null;
        try {
            Date begin = new Date();
            param = params[0];
            if(!EntityUtil.deviceOnline(context)){
                throw new Exception("device not on line");
            }
            String eid = param.getEid();
            String webVersion = param.getWebVersion();
            String zipDownloadPath = param.getZipDownloadPath();
            String unzipedWebResourcePath = param.getUnzipedWebResourcePath();
            String processType = param.getType();

            //check whether exist the zip file
            if( zipDownloadPath != null && param.getZipFileName() != null){
                String zipFileName = param.getZipFileName();
                boolean needUnzip = true;
                String zipPathName = ProcessUtil.getPathName(zipDownloadPath,zipFileName);
                if(ProcessUtil.isExists(unzipedWebResourcePath,zipFileName)){
                    needUnzip = false;
                }

                if(needUnzip){
                    try{
                        UnzipUtil util = new UnzipUtil();
                        File file = new File(zipPathName);
                        util.unZipFolder(zipPathName,unzipedWebResourcePath,handler);
                    } catch (Exception e){
                        //Below Code is remove the zip file which thorwn exception from resource
                        // path: "data/data/com.moolu/files/resource/zip/******.zip"
                        File file = new File(zipPathName);
                        if(file.exists()){
                            file.delete();
                        }
                        this.setError(FAILED);
                    }
                }

                DownloadUtil downloadUtil = new DownloadUtil(context);
            }
        } catch (ClientProtocolException e) {
            this.setError(FAILED);
            // Log.error("UnzipResourceTask doInBackground",e);
        } catch (IOException e) {
            this.setError(FAILED);
            // Log.error("UnzipResourceTask doInBackground",e);
        } catch (Exception e) {
            this.setError(FAILED);
            // Log.error("UnzipResourceTask doInBackground",e);
        }
        return param;
    }
}
