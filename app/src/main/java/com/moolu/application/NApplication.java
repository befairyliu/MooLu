package com.moolu.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.moolu.R;
import com.moolu.framework.entity.model.Entity;
import com.moolu.storage.prefs.PrefConstants;

import java.io.File;

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
        init();
    }

    private void init(){
        initBaiduMapSdk();
        InitLocation();
        initVolley();
        initEnv();
    }

    private void initEnv(){
        SharedPreferences prefs = this.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        String currentEntityPath = prefs.getString(PrefConstants.ENTITY_PATH,null);
        if(currentEntityPath == null){
            currentEntityPath = this.getFilesDir()+ File.separator + ENTITY_DIY;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PrefConstants.ENTITY_PATH,currentEntityPath);
            editor.commit();
        }
    }

    private void initVolley(){
        mQueue = Volley.newRequestQueue(this);
        mQueue.start();
    }

    private void initBaiduMapSdk(){
        SDKInitializer.initialize(getApplicationContext());
    }

    private void setLocationListener(BDLocationListener listener){
        mLocationClient.registerLocationListener(listener);
    }

    private void InitLocation(){
        mLocationClient = new LocationClient(this.getApplicationContext());
        mGeofenceClient = new GeofenceClient(getApplicationContext());

        LocationMode tempMode = LocationMode.Hight_Accuracy;
        String tempcoor = "gcj02";
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);
        option.setCoorType(tempcoor);
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    //-----------------setter and getter---------------------
    public String getCookiePolicy() {
        return cookiePolicy;
    }

    public void setCookiePolicy(String cookiePolicy) {
        this.cookiePolicy = cookiePolicy;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isAllowAllSSL() {
        return allowAllSSL;
    }

    public void initAllowAllSSL() {
        String allowSSLStr = getString(R.string.allow_ssl);
        allowAllSSL = "1".equals(allowSSLStr) ? true : false;
    }

    String str = new String();
}
