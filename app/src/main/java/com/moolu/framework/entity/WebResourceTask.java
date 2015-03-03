package com.moolu.framework.entity;

import android.os.Handler;

import com.moolu.activity.NananActivity;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;

import com.moolu.util.DownloadUtil;
import com.moolu.util.ProcessUtil;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

import java.util.Date;


/**
 * Created by Nanan on 3/3/2015.
 */
public class WebResourceTask extends AsyncTaskWithCallback<TaskParam, Void, TaskParam> {
    private final NananActivity context;
    private final Handler handler;


    public WebResourceTask(NananActivity context,ActivityCallback callback,Handler handler, int ref){
        super(callback, ref);
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected TaskParam doInBackground(TaskParam... params) {
        TaskParam param = null;
        String zipFileName = null;
        try{
            Date begin = new Date();
            param = params[0];
            if(!EntityUtil.deviceOnline(context)){
                throw new Exception("device not on line");
            }
            String eid = param.getEid();
            String webVersion = param.getWebVersion();
            String zipDownloadPath = param.getZipDownloadPath();
            zipFileName = param.getZipFileName();

            boolean needDownload = true;
            String zipPathName = ProcessUtil.getPathName(zipDownloadPath,zipFileName);
            /*
            String unzipDownloadPath = param.getUnzipedWebResourcePath();
            String processType = param.getType();
            ZipInfo zipInfo = param.getZipInfo();
            String zipUrl = zipInfo.getZipurl();

            String configChecksum=zipInfo.getHashcode();
            NameValueStore store = new NameValueStore(context);
            */

            //check whether exist that zip file
            if(ProcessUtil.isExists(zipDownloadPath,zipFileName)){
                needDownload = false;
                //check whether exist the unzip log. if no then need unzip again as well,
                // and check whether need download again.

                /*
                if(ResourceProcessor.hasNoValidUnzipRecord(store,eid,webVersion, processType, zipFileName, configChecksum)){
                    //Log.debug("==no unzip record");
                    //Produce checksum of the particular local ZIP
                    String localChecksum = EntityUtil.getLocalCopyCurrentChecksum(zipPathName, Constants.ALGORITHM);
                    if(EntityUtil.checkLocalCopyChecksum(configChecksum, localChecksum)){
                        //equlas then no need download.
                        needDownload=false;
                    }else{
                        needDownload=true;
                    }
                }else{
                    needDownload=false;
                }
                */
            }

            //need download case
            DownloadUtil downloadUtil = new DownloadUtil(context,this);
            if(needDownload){
                //Save the new ZIP to download zip folder
                boolean isSaveSuccess = downloadUtil.downloadResourceAndSave(
                        param.getWebResource().getZipUrl(), zipDownloadPath, zipFileName,handler,0);
                // Added immediately following if condition to check if the download is complete.
                // Once it is complete, we disable the Cancel button on Client Pack download page
                if (!isSaveSuccess) {
                    throw new Exception("download zip file fail");
                }else{
                    //check checksum again
                    //String localChecksum = EntityUtil.getLocalCopyCurrentChecksum(zipPathName, Constants.ALGORITHM);
                    /*
                    if(EntityUtil.checkLocalCopyChecksum(configChecksum, localChecksum)){
                        //success
                    }else{
                        throw new Exception("zip file checksum varify fail");
                    }*/
                }
            }
        } catch (ClientProtocolException e) {
            this.setError(FAILED);
        } catch (IOException e) {
            this.setError(FAILED);
        } catch (Exception e){
            //objCallback.handleCallback(this, ResourceProcessor.PREPARE_RESOURCE_TASK_REF);
            this.setError(FAILED);
        }

        // This is to supplement the callback if the task had canceled.
        if(this.isCancelled()){
            //objCallback.handleCallback(this, ResourceProcessor.PREPARE_RESOURCE_TASK_REF);
        }
        return param;
    }


}
