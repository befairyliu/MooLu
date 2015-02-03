package com.moolu.framework.util;

/**
 * Created by Nanan on 2/3/2015.
 */
public class AppSupportFeature {
    // App Support feature name
    private String name;
    // whether support
    private boolean isSupported;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSupported() {
        return isSupported;
    }

    public void setSupported(boolean isSupported) {
        this.isSupported = isSupported;
    }

    public String toString() {
        return new StringBuffer().append("{name:").append(name).append("; isSupported:")
                .append(isSupported).append("}").toString();

    }
}
