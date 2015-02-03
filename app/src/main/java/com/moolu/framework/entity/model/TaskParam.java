package com.moolu.framework.entity.model;

/**
 * Created by Nanan on 2/3/2015.
 */
public class TaskParam {
    private int id;
    private String eid;
    private String webVersion;
    private String unzipedWebResourcePath;
    private String zipDownloadPath;
    private String zipFileName;
    private WebResource webResource;
    private AppUpdateResource appUpdateResource;
    /**
     * ProcessConstants.LOCAL_TYPE,ProcessConstants.GLOBAL_TYPE,ProcessConstants.BUNDLED_TYPE
     */
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppUpdateResource getAppUpdateResource() {
        return appUpdateResource;
    }

    public void setAppUpdateResource(AppUpdateResource appUpdateResource) {
        this.appUpdateResource = appUpdateResource;
    }

    public WebResource getWebResource() {
        return webResource;
    }

    public void setWebResource(WebResource webResource) {
        this.webResource = webResource;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    public String getZipDownloadPath() {
        return zipDownloadPath;
    }

    public void setZipDownloadPath(String zipDownloadPath) {
        this.zipDownloadPath = zipDownloadPath;
    }

    public String getUnzipedWebResourcePath() {
        return unzipedWebResourcePath;
    }

    public void setUnzipedWebResourcePath(String unzipedWebResourcePath) {
        this.unzipedWebResourcePath = unzipedWebResourcePath;
    }

    public String getWebVersion() {
        return webVersion;
    }

    public void setWebVersion(String webVersion) {
        this.webVersion = webVersion;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
}
