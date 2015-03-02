package com.moolu.http.proxy;

import android.content.Context;

import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.http.pack.MooLuHttpClient;
import com.moolu.util.StringUtil;

import org.json.JSONObject;
import org.slf4j.Logger;

/**
 * This class is mainly for handling GSP proxy which request content type is "application/json"
 * Created by Nanan on 3/2/2015.
 */
public class GspProxyTask extends AsyncTaskWithCallback<String, Void, GSPProxyResponse> {
    private final Context context;
    private final static Logger Log = new NananLog(GspProxyTask.class);

    public GspProxyTask(final Context context, final ActivityCallback callback, final int ref) {
        super(callback, ref);
        if (context == null) {
            throw new IllegalArgumentException("owner must not be null");
        }
        this.context=context;
    }

    @Override
    protected GSPProxyResponse doInBackground(String... params) {
        JSONObject jsonHeaders = null;
        JSONObject jsonObject = null;
        try {
            if(params!=null&&params.length==5){
                String url=params[0];
                String urlPara=params[1];
                String headers = params[2];//headers={"xxx-http-head":"GSP"}
                String method=params[3];
                String callbackJs=params[4];
                Log.debug("======request datajs parameters:"+urlPara);
                Log.debug("======send request headers parameters:"+headers);

                if(!StringUtil.IsNullOrEmpty(urlPara)){
                    jsonObject = new JSONObject(urlPara);
                }

                if(!StringUtil.IsNullOrEmpty(headers)){
                    jsonHeaders = new JSONObject(headers);
                }
                MooLuHttpClient httpClient = MooLuHttpClient.getInstance(this.context);

                Log.debug("send request:"+url);
                Log.debug("send request parameters:"+urlPara);
                if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_POST)){
                    GSPProxyResponse proxyRsp = httpClient.GSPConnectURLByPostOrPut(url, method,null, jsonObject,jsonHeaders);
                    setCallBackJsToResponse(proxyRsp, callbackJs);
                    Log.debug("POST response result:"+proxyRsp.getResponseStr()+";"+proxyRsp.getHttpStatusCode());
                    return proxyRsp;
                }else if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_GET)){
                    urlPara = StringUtil.getParaFromJson(urlPara);
                    url = getRequestURL(url, urlPara);
                    GSPProxyResponse proxyRsp= httpClient.GSPConnectURLByGetOrDelete(url, method,null,jsonHeaders);
                    setCallBackJsToResponse(proxyRsp, callbackJs);
                    Log.debug("GET response result:"+proxyRsp.getResponseStr()+";"+proxyRsp.getHttpStatusCode());
                    return proxyRsp;
                }else if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_PUT)){
                    GSPProxyResponse proxyRsp = httpClient.GSPConnectURLByPostOrPut(url, method,null, jsonObject,jsonHeaders);
                    setCallBackJsToResponse(proxyRsp, callbackJs);
                    return proxyRsp;
                }else if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_DELETE)){
                    urlPara = StringUtil.getParaFromJson(urlPara);
                    url = getRequestURL(url, urlPara);
                    GSPProxyResponse proxyRsp = httpClient.GSPConnectURLByGetOrDelete(url, method ,null, jsonHeaders);
                    setCallBackJsToResponse(proxyRsp, callbackJs);
                    return proxyRsp;
                }else {
                    Log.debug("parameter error");
                }
            }else{
                this.setError(FAILED,"Gsp Proxy parameters are not enought.");
            }
        } catch (Exception e) {
            Log.error("Gsp Proxy error!",e);
            this.setError(FAILED,e.getMessage());
        }
        return null;
    }

    /**
     * To reduce the duplicated code and make clean code
     */
    private String getRequestURL(String url,String urlPara){
        if(!StringUtil.IsNullOrEmpty(urlPara)){
            if(!StringUtil.IsNullOrEmpty(url)&&url.indexOf("?")!=-1){
                url = url+"&"+urlPara;
            }else{
                url = url+"?"+urlPara;
            }
        }else{
            url = url;
        }
        if(url.indexOf(" ")!=-1){
            url=url.replaceAll(" ", "%20");
        }
        return url;
    }

    private void setCallBackJsToResponse(GSPProxyResponse proxyRsp , String callbackJs){
        if(proxyRsp!=null && !StringUtil.IsNullOrEmpty(callbackJs)){
            proxyRsp.setCallbackJs(callbackJs);
        }
    }
}
