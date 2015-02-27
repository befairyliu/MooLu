package com.moolu.http.pack;

import android.content.Context;

import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.util.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nanan on 2/27/2015.
 */
public class ProxyJsonTask implements AsyncTaskWithCallback<String,Void,ProxyResponse>{

    private final Context context;
    private final static Logger Log = new NananLog(ProxyJsonTask.class);

    public ProxyJsonTask(final Context context, final ActivityCallback callback, final int ref) {
        //TODO...
        /*super(callback, ref);
        if (context == null) {
            throw new IllegalArgumentException("owner must not be null");
        }*/
        this.context=context;
    }
    @Override
    protected ProxyResponse doInBackground(String... params) {
        // TODO....
        ProxyResponse proxyRsp=new ProxyResponse();
        JSONObject jsonHeaders = null;

        /*try{
            if(params!=null&&params.length==5){
                String url=params[0];//url, params, method, callbackJs, headers
                String urlPara=params[1];
                String method=params[2];
                String callbackJs=params[3];
                String headers=params[4];
                if(!StringUtil.IsNullOrEmpty(headers)){
                    jsonHeaders = new JSONObject(headers);
                }
                proxyRsp.setCallbackJs(callbackJs);
                StringBuffer sf=null;
                TitanHttpClient httpClient = TitanHttpClient.getInstance(this.context);
                Log.debug("send request:"+url);
                Log.debug("send request parameters:"+urlPara);
                if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_POST)){
                    List<NameValuePair> paraList=null;
                    paraList = getPostParaFromJson(urlPara);
                    sf= httpClient.connectURLByPost(url, null, paraList, jsonHeaders);
                    proxyRsp.setResponseStr(sf);
                    return proxyRsp;
                }else if(method!=null&&method.toUpperCase().equals(Constants.REQUEST_GET)){
                    urlPara=StringUtil.getGetParaFromJson(urlPara);
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
            }else{
                this.setError(FAILED,"Proxy parameters are not enought.");
            }
        }catch(Exception e){
            Log.error("Proxy error!",e);
            this.setError(FAILED,e.getMessage());
        }
        */
        return proxyRsp;
    }

    public List<NameValuePair> getPostParaFromJson(String str){
        try{
            if(str==null){
                return null;
            }
            List<NameValuePair> parmList=new ArrayList<NameValuePair>();
            JSONObject jobject = new JSONObject(str);
            if(jobject!=null){
                Iterator<String> e =jobject.keys();
                while(e.hasNext()){
                    String name=(String)e.next();
                    String value="";
                    if(name!=null){
                        value = (String)jobject.get(name);
                        NameValuePair np=new BasicNameValuePair(name,value);
                        parmList.add(np);
                    }
                }
                return parmList;
            }
        }catch(Exception e){
            Log.error("Get post para from json error",e);
        }
        return null;
    }
}
