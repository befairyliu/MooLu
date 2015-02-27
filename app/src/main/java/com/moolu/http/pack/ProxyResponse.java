package com.moolu.http.pack;

/**
 * Created by Nanan on 2/27/2015.
 */
public class ProxyResponse {
    StringBuffer responseStr;
    String callbackJs;
    String statusCode;

    public StringBuffer getResponseStr() {
        return responseStr;
    }
    public void setResponseStr(StringBuffer responseStr) {
        this.responseStr = responseStr;
    }
    public String getCallbackJs() {
        return callbackJs;
    }
    public String getStatusCode() {
        return statusCode;
    }

    public void setCallbackJs(String callbackJs) {
        this.callbackJs = callbackJs;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
