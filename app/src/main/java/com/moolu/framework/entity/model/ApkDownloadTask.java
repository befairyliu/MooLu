package com.moolu.framework.entity.model;

import android.os.Handler;

import com.moolu.activity.NananActivity;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

/**
 * Created by Nanan on 2/3/2015.
 */
public class ApkDownloadTask extends AsyncTaskWithCallback<TaskParam,Void,TaskParam>{
    private NananActivity context;
    private Handler handler;

    public ApkDownloadTask(NananActivity context,ActivityCallback callback,Handler handler,int ref){
        super(callback,ref);
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected TaskParam doInBackground(TaskParam... params) {
        TaskParam param = params[0];
        String url = param.getAppUpdateResource().getAppUrl();
        String downloadPath = param.getZipDownloadPath();
        String apkName = param.getZipFileName();
        try{
            if(url == null){
                throw new Exception("Download apk failure !");
            } else{
                //TODO...
            }
        } catch(ClientProtocolException e){
            this.setError(FAILED);
        } catch(IOException e){
            this.setError(FAILED);
        } catch(Exception e){
            this.setError(FAILED);
        }

        return param;
    }
}
