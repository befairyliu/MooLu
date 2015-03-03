package com.moolu.hook.actions;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.util.StringUtil;

import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Nanan on 3/3/2015.
 */
public class ProxyAction extends MooLuAction{
    private final static Logger Log = new NananLog(ProxyAction.class);

    @Override
    public void execute(Context context, WebView webview, Hook hook) {
        try{
            Map<String,String> map = this.getParams();
            if(map == null){
                throw new HookException();
            }
            String datajs = map.get(HookConstants.DATA_JS);
            String callbackJs = map.get(HookConstants.CALLBACK_JS);
            String url = map.get(HookConstants.URL);
            String method = map.get(HookConstants.METHOD);
            if(StringUtil.IsNullOrEmpty(datajs)||StringUtil.IsNullOrEmpty(callbackJs)
                    ||StringUtil.IsNullOrEmpty(url)){
                throw new HookException("request parameter missing");
            }
            hook.setMap(map);
            String js = getProxyDataJs(url,datajs,method,callbackJs,"setProxyDataJson");
            loadUrlInMainThread(webview,js);
            hook.setWebview(webview);
        } catch (Exception e){
            executeHookAPIFailCallJs(webview);
            Log.error("Fail to execute the hook:{}",e.getMessage());
        }
    }
}
