package com.moolu.hook.actions;

import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.hook.MooGetJsDataAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 */
public class MooLu extends MooGetJsDataAction{

    private final static Logger Log = new NananLog(MooLu.class);
    private Context context = null;

    @Override
    public void validate(Context context, WebView webView, Hook hook) throws HookException {
        super.validate(context, webView, hook);
        Map<String,String> map = this.getParams();
    }

    @Override
    public void dataProcess(Context context, WebView webview, Handler mHandler, String dataValue, JSONObject jo) throws JSONException, HookException {
        try {
            this.context = context;
            String callbackJs = jo.getString(HookConstants.CALLBACK_JS);
            String key = jo.getString(HookConstants.KEY);

        } catch (JSONException e){
            this.executeHookAPIFailCallJs(webview);
            Log.error("Setter action data process error");
        }
    }
}
