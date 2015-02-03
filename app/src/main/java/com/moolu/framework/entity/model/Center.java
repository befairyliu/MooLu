package com.moolu.framework.entity.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nanan on 2/3/2015.
 */
public class Center {
    @Expose String regionId;
    @Expose
    private String regionName;
    @Expose
    private List<WebResource> webResource = new ArrayList<WebResource>();
    @Expose
    private String homePage;

    /**
     * Local parameters.
     */
    private int app_status = status_default;

    public static final int status_default = 0;
    public static final int status_not_download = 1;
    public static final int status_invalid = 2;
    public static final int status_latest = 3;
    public static final int status_need_update = 4;


    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<WebResource> getWebResource() {
        return webResource;
    }

    public void setWebResource(List<WebResource> webResource) {
        this.webResource = webResource;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homepage) {
        this.homePage = homePage;
    }

    public int getApp_status() {
        return app_status;
    }

    public void setApp_status(int app_status) {
        this.app_status = app_status;
    }
}
