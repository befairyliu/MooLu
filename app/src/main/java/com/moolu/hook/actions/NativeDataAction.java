package com.moolu.hook.actions;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.storage.prefs.NameValueStore;
import com.moolu.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Nanan on 3/3/2015.
 */
public class NativeDataAction extends MooLuJsDataAction {
    private final static Logger Log = new NananLog(NativeDataAction.class);
    private void getter(Context context,Hook hook,WebView webView){
        try {
            Map<String,String> map = this.getParams();
            if(map == null){
                throw new HookException();
            }
            String key = map.get(HookConstants.KEY);
            String callbackJs = map.get(HookConstants.CALLBACK_JS);
            if(StringUtil.IsNullOrEmpty(key)||StringUtil.IsNullOrEmpty(callbackJs)){
                throw new HookException("request parameter missing.");
            }
            NameValueStore store = new NameValueStore(context);
            String value = store.getAttribute(key);
            loadUrlInMainThread(webView,getCallbackJs(callbackJs,value));
        } catch (Exception e){
            executeHookAPIFailCallJs(webView);
            Log.error("Fail to execute the hook:{}",e.getMessage());
        }
    }

    private void setter(Context context,Hook hook,WebView webView,JSONObject jo,String dataValue){
        try {
            String callbackJs = jo.getString(HookConstants.CALLBACK_JS);
            String key = jo.getString(HookConstants.KEY);
            if (key != null) {
                NameValueStore store = new NameValueStore(context);
                store.setAttribute(key, dataValue);
                if (!StringUtil.IsNullOrEmpty(callbackJs)) {
                    loadUrlInMainThread(webView,MooLuAction.getCallbackJs(callbackJs, dataValue));
                }
            }
        } catch (JSONException e) {
            this.executeHookAPIFailCallJs(webView);
            Log.error("Setter action data process error",e);
        }
    }
}
