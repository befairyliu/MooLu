package com.moolu.plugins.eps;

import android.app.AlertDialog;
import android.content.Context;

import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.NananLog;

import org.slf4j.Logger;

/**
 * Created by Nanan on 3/4/2015.
 */
public class InitDeviceStatusProviderTask extends AsyncTaskWithCallback<Void,Void,Boolean>{
    private final static Logger Log = new NananLog(InitDeviceStatusProviderTask.class);
    private final Context context;
    public AlertDialog.Builder dlg;

    public InitDeviceStatusProviderTask(final Context context,final ActivityCallback callback,
                                        final int ref){
        super(callback,ref);
        if(context == null){
            throw new IllegalArgumentException("owner must not be null");
        }
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        boolean results = false;
        DeviceStatusEPSProvider provider = DeviceStatusProviderFactory
                .getInstance().getProvider(context);
        if(provider != null){
            boolean initResults = provider.init(context);
            if(initResults){
                DeviceStatusEPS status = provider.calculateDeviceStatus();
                if(status != null){
                    results = true;
                }
            }
        }
        return results;
    }
}
