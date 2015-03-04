package com.moolu.plugins.baidu;

import android.content.Context;
import android.os.Handler;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.moolu.framework.NananLog;

import org.slf4j.Logger;

/**
 * Created by Nanan on 3/4/2015.
 */
public class BaiduLocationUtil {
    private final static Logger Log = new NananLog(BaiduLocationUtil.class);
    private final static String TAG = "BaiduLocationUtil";
    private Context context = null;
    private Handler handler = null;
    public LocationClient locationClient = null;
    public MainLocationListener listener = null;

    public BaiduLocationUtil(Context context,Handler handler){
        this.context = context;
        this.handler = handler;
        init();
    }

    private void init(){
        locationClient = new LocationClient(context);
        listener = new MainLocationListener(context,handler);
        locationClient.registerLocationListener(listener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setProdName("caware");
        locationClient.setLocOption(option);
    }

    //start the location server
    public void start(){
        if(locationClient != null){
            locationClient.start();
        }
    }

    //stop the location server
    public void stop(){
        if(locationClient != null){
            locationClient.stop();
        }
    }

    public void requestLocation(){
        if (locationClient != null) {
            int res = locationClient.requestLocation();
            Log.debug(TAG, "requestLocation res:" + res);
        }
    }
}
