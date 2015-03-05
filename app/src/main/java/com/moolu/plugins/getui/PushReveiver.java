package com.moolu.plugins.getui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.igexin.sdk.PushConsts;
import com.moolu.application.MApplication;
import com.moolu.framework.NananLog;
import com.moolu.http.volley.ApiConstDef;
import com.moolu.http.volley.StringRequestUtil;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanan on 3/4/2015.
 */
public class PushReveiver extends BroadcastReceiver{
    private final static Logger Log = new NananLog(PushReveiver.class);
    public Context context = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getExtras();
        Log.debug("GetuiSDKDemo","onReceive() action="+bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:
                //get client id
                String cid = bundle.getString("clientid");
                Log.debug("GetuiSDKDemo","Get ClientID:"+cid);
                bindClientID(context,cid);
                //// to do something...
                /* 第三方应用需要将ClientID上传到第三方服务器，并且将当前用户帐号和ClientID进行关联，
                以便以后通过用户帐号查找ClientID进行消息推送。有些情况下ClientID可能会发生变化，为保证获取最新的ClientID，
                请应用程序在每次获取ClientID广播后，都能进行一次关联绑定 */
                break;
            case PushConsts.GET_MSG_DATA:
                //get payload data
                byte[] payload = bundle.getByteArray("payload");
                if(payload != null){
                    String data = new String(payload);
                    Log.debug("GetuiSDKDemo","Get playload:"+payload);
                    //to handle the payload data
                    //..........
                }
                break;
            default:
                break;
        }
    }

    /**
     * when receive clientID, update it to third-party server.
     * If user have login, also update the userID.
     */
    private void bindClientID(Context context, String cid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ApiConstDef.CALLBACK_MARK, String.valueOf(ApiConstDef.getuiRegisterType));
        map.put("cid", cid);

        //map.put("deviceToken", devideToken);
        RequestQueue queue = ((MApplication)context.getApplicationContext()).mQueue;
        queue.add(StringRequestUtil.getStringRequest(ApiConstDef.getuiRegisterType, map, listener, errorListener));
    }

    /**
     * Listener and errorListener callback for HTTP request with volley.
     */
    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            String callbackMark = StringRequestUtil.getCallbackMark(response);
            if (callbackMark.equals(String.valueOf(ApiConstDef.loginType))) {
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            }

        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
