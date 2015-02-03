package com.moolu.framework.entity.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Nanan on 2/3/2015.
 */
public class WebResource {
    @Expose
    private String zipUrl;
    @Expose
    private String zipSize;
    @Expose
    private String hashCode;

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getZipSize() {
        return zipSize;
    }

    public void setZipSize(String zipSize) {
        this.zipSize = zipSize;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }
}
