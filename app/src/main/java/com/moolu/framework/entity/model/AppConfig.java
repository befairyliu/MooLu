package com.moolu.framework.entity.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Nanan on 2/3/2015.
 */
public class AppConfig {
    @Expose
    private boolean supportRotation;
    @Expose
    private int refreshTime;
    @Expose
    private AppUpdateResource appUpdate;

    public AppUpdateResource getAppUpdate() {
        return appUpdate;
    }

    public void setAppUpdate(AppUpdateResource appUpdate) {
        this.appUpdate = appUpdate;
    }

    public boolean isSupportRotation() {
        return supportRotation;
    }

    public void setSupportRotation(boolean supportRotation) {
        this.supportRotation = supportRotation;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }
}
