package com.moolu.framework.entity.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Nanan on 2/3/2015.
 */
public class AppUpdateResource {
    @Expose
    private String appUrl;
    @Expose
    private String appVersion;
    @Expose
    private String appSize;

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
