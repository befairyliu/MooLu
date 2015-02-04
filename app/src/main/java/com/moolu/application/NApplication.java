package com.moolu.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.moolu.framework.entity.model.Entity;

/**
 * Created by Nanan on 2/4/2015.
 */
public class NApplication extends Application{
    //TODO for LocationClient, GeofenceClient and RequestQueue;

    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public RequestQueue mQueue;
    private boolean allowAllSSL = false;
    private String cookiePolicy;
    private final String ENTITY_DIY = "entity";
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static final int NETTYPE_WIFI = 0x01;

    private Entity entity = null;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void init(){

    }

    private void initEnv(){
        //SharedPreferences prefs = this.getSharedPreferences();
    }
}
