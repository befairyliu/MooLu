package com.moolu.framework.entity.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nanan on 2/3/2015.
 */
public class Entity {
    @Expose
    private List<Center> center = new ArrayList<Center>();
    @Expose
    private AppConfig appConfig;
    @Expose
    private String version;

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Center> getCenter() {
        return center;
    }

    public void setCenter(List<Center> center) {
        this.center = center;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
}

