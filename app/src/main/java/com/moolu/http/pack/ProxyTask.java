package com.moolu.http.pack;

import android.content.Context;

import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.util.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Nanan on 3/2/2015.
 */
public class ProxyTask extends AsyncTaskWithCallback<String,Void,ProxyResponse> {
    private final Context context;
    private final static Logger Log = new NananLog(ProxyTask.class);

    public ProxyTask(final Context context, final ActivityCallback callback, final int ref){
        super(callback,ref);
        if(context == null){
            throw new IllegalArgumentException("owner must not be null");
        }
        this.context = context;
    }
    @Override
    protected ProxyResponse doInBackground(String... params) {
        ProxyResponse proxyRsp = new ProxyResponse();
        JSONObject jsonHeaders = null;
        try{
            if(params != null && params.length == 5){
                String url = params[0];
                String urlPara = params[1];
                String method = params[2];
                String callbackJs = params[3];
                String headers = params[4];
                if( !StringUtil.isNotNullAndEmpty(headers)){
                    jsonHeaders = new JSONObject(headers);
                }
                proxyRsp.setCallbackJs(callbackJs);
                StringBuffer sf = null;
                MooLuHttpClient httpClient = MooLuHttpClient.getInstance(this.context);

                if(method != null && method.toUpperCase().equals(Constants.REQUEST_POST)){
                    ////will not return null;
                    List<NameValuePair> paraList = MooLuHttpClient.parseURL(urlPara);
                    sf = httpClient.connectURLByPost(url, null, paraList, jsonHeaders);
                    proxyRsp.setResponseStr(sf);
                    return proxyRsp;
                }else if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_GET)){
                    //urlPara=getURLPara(urlPara);
                    if(urlPara!=null){
                        if(url!=null&&url.indexOf("?")!=-1){
                            url=url+"&"+urlPara;
                        }else{
                            url=url+"?"+urlPara;
                        }
                    }
                    if(url.indexOf(" ")!=-1){
                        url=url.replaceAll(" ", "%20");
                    }
                    sf= httpClient.connectURL(url, null, jsonHeaders);
                    proxyRsp.setResponseStr(sf);
                    return proxyRsp;
                }else{
                    Log.debug("parameter error");
                }
            } else {
                this.setError(FAILED,"Proxy parameters are not enought.");
            }
        } catch (Exception e){
            Log.error("Proxy error !",e);
            this.setError(FAILED,e.getMessage());
        }
        return proxyRsp;
    }

    public String getURLPara(String urlPara){
        try{
            List<NameValuePair> paraList = MooLuHttpClient.parseURL(urlPara);
            StringBuffer para=new StringBuffer();
            for(int i=0;i<paraList.size();i++){
                NameValuePair nvp = paraList.get(i);
                if( i>0 ){
                    para.append("&");
                }
                para.append(URLEncoder.encode(nvp.getName(), HTTP.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(nvp.getValue(),HTTP.UTF_8));
            }
            return para.toString();
        }catch(Exception e){
            Log.error("URL parameter parse error",e);
        }
        return null;
    }
}
