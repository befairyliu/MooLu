package com.moolu.hook.actions;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.hook.resolver.ResolverUI15;
import com.moolu.util.ReflectUtil;
import com.moolu.util.StringUtil;

import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Nanan on 3/3/2015.
 */
public class MooLuJsDataAction extends MooLuAction{
    private final static Logger Log = new NananLog(MooLuJsDataAction.class);
    private Context context;
    private WebView webView;

    public void validate(Context context,WebView webView,Hook hook) throws HookException{
        Map<String,String> map = this.getParams();
        if(map == null){
            throw new HookException();
        }
        String datajs = map.get(HookConstants.DATA_JS);
        String callbackjs = map.get(HookConstants.CALLBACK_JS);
        if(StringUtil.IsNullOrEmpty(callbackjs)){
            throw new HookException("request parameter missing");
        }
    }

    @Override
    public void execute(Context context, WebView webview, Hook hook) {
        try{
            validate(context,webView,hook);
            this.context = context;
            this.webView = webview;
            Map<String,String> map = this.getParams();
            hook.setMap(map);
            if(map.get(HookConstants.DATA_JS) != null){
                String js = executeDataJs(map);
                loadUrlInMainThread(webView,js);
                hook.setWebview(webview);
            } else {
                String[] action = null;
                if(map.get(HookConstants.FUNCTION) != null){
                    action = ResolverUI15.resolveByFunction(map.get(HookConstants.FUNCTION));
                } else {
                    throw new Exception("function name not found !");
                }
                if(action == null){
                    throw new Exception("Hook API not support !");
                }
                ReflectUtil.invoke(action,context,hook,webview);
            }
        } catch (Exception e){
            executeHookAPIFailCallJs(webview);
            Log.error("Fail to execute the hook:{}",e.getMessage());
        }
    }
}
