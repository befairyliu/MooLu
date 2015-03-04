package com.moolu.plugins.eps;

/**
 * the data class for better extensibility
 * Created by Nanan on 3/4/2015.
 */
public class DeviceStatus {
    public final static String ROOTED = "rooted";
    protected boolean rooted = false;

    //put into JSON format

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"")
                .append(ROOTED)
                .append("\":\"")
                .append(rooted)
                .append("\"}");
        return sb.toString();
    }

    public void setRooted(boolean rooted){
        this.rooted = rooted;
    }
}
