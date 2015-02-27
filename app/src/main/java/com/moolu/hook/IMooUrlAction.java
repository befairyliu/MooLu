package com.moolu.hook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nanan on 2/27/2015.
 */
public interface IMooUrlAction {
    public void dataProcess(Context context, WebView webView, Handler handler,String dataValue,
                            JSONObject jo, Hook hook) throws JSONException,HookException;

    public void onActionResult(Context context,Handler handler,int resultCode,Intent data);
}
