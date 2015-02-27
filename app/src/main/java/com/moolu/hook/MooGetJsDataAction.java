package com.moolu.hook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.WebView;

import com.moolu.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 */
public abstract class MooGetJsDataAction extends MooURLAction implements IMooUrlAction{

    protected Context context = null;
    protected WebView webView = null;

    public void validate(Context context,WebView webView,Hook hook) throws HookException{
        Map<String, String> map = this.getParams();
        if(map == null){
            throw new HookException();
        }

        String datajs = map.get(HookConstants.DATA_JS);
        String callbackjs = map.get(HookConstants.CALLBACK_JS);
        if(StringUtil.IsNullOrEmpty(datajs)||StringUtil.IsNullOrEmpty(callbackjs)){
            throw new HookException("request parameter missing");
        }
    }

    public abstract void dataProcess(Context context, WebView webview, Handler mHandler,
                                     String dataValue, JSONObject jo)throws JSONException,HookException;

    @Override
    public void dataProcess(Context context, WebView webView, Handler handler, String dataValue,
                            JSONObject jo, Hook hook) throws JSONException, HookException {
        dataProcess(context,webView,handler,dataValue,jo);
    }

    @Override
    public void onActionResult(Context context, Handler handler, int resultCode, Intent data) {

    }

    @Override
    public void execute(Context context, WebView webview, Hook hook) {
        try{
            validate(context,webView,hook);
            //save the execute context and webview to prevent them override
            this.context = context;
            this.webView = webview;
            Map<String,String> map = this.getParams();
            hook.setMap(map);
            String js = executeDataJs(map);
            loadUrlInMainThread(webView,js);
            hook.setWebview(webview);
        } catch (Exception e){
            executeHookAPIFailCallJs(webview);
        }

    }
}
