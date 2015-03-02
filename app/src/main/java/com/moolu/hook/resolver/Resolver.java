package com.moolu.hook.resolver;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.actions.MooLuAction;
import com.moolu.util.ReflectUtil;

import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanan on 3/2/2015.
 */
public class Resolver {
    private final static Logger Log = new NananLog(Resolver.class);
    public static String[] resolve(String url, Map<String,String[]> map, Hook mHook){
        if (!url.startsWith(HookConstants.ML_URL_PREFIX)) {
            throw new IllegalArgumentException(String.format("%s is not a valid URL", url));
        }

        // Parse params  function=GetAppVersion&callbackjs=iMobile.callBackStockHandler
        String paramsString = url.substring(HookConstants.ML_URL_PREFIX.length());
        Map<String, String> params = processParams(paramsString);
        if (params != null) {
            String function = params.get(HookConstants.FUNCTION);
            String[] action = map.get(function);
            if (action == null) {
                Resolver.Log.error("URL Action has no function");
                return null;
            }
            MooLuAction mAction = ReflectUtil.getAction(action);
            try{
                mHook.setMooLuAction(mAction);
                mAction.setParams(params);
            }catch (Exception e){
                Resolver.Log.error("action get error");
            }
            return action;
        }
        else {
            Resolver.Log.error("URL Action has no params");
            return null;
        }
    }



    public static Map<String, String> processParams(final String paramsString) {
        String[] paramsKVPairs = paramsString.split("&");
        Map<String, String> params = new HashMap<String, String>(paramsKVPairs.length);
        for (String keyValuePair : paramsKVPairs) {
            if(keyValuePair!=null&&keyValuePair.indexOf("=")!=-1){
                try {
                    String[] kv = keyValuePair.split("=");
                    String key=null;
                    String value="";

                    if(kv.length>1){
                        value=kv[1];
                    }
                    if(kv.length>0){
                        key=kv[0];
                        params.put(key, URLDecoder.decode(value, "UTF-8"));
                    }
                }catch (UnsupportedEncodingException e) {
                    // Unable to parse the params, possibly incorrectly encoded, set params to null.
                    params = null;
                }
            }
        }
        if (params!=null&&!params.containsKey(HookConstants.FUNCTION)) {
            // This means the URL is invalid, return null;
            params = null;
        }
        return params;
    }
}
