package com.moolu.http.proxy;

/**
 * Created by Nanan on 2/27/2015.
 */
public class GSPProxyResponse {
    private String responseStr;
    private int httpStatusCode;
    private String callbackJs;
    public String getResponseStr(){
        return responseStr;
    }
    public void setResponseStr(String responseStr){
        this.responseStr=responseStr;
    }
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
    public String getCallbackJs() {
        return callbackJs;
    }
    public void setCallbackJs(String callbackJs) {
        this.callbackJs = callbackJs;
    }

}
