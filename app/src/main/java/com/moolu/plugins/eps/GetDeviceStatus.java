package com.moolu.plugins.eps;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.hook.MooURLAction;

import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Nanan on 3/4/2015.
 */
public class GetDeviceStatus extends MooURLAction{

    private final static Logger Log = new NananLog(GetDeviceStatus.class);

    @Override
    public void execute(Context context, WebView webview, Hook hook) {
        try{
            Map<String,String> map = this.getParams();
            if(map == null){
                throw new HookException();
            }
            String callbackJs = map.get(HookConstants.CALLBACK_JS);
            if (callbackJs == null || "".equals(callbackJs.trim())){
                throw new HookException("request parameter missing");
            }

            DeviceStatusEPSProvider provider = DeviceStatusProviderFactory
                    .getInstance().getProvider(context);
            String value = "Get Device Status Failed.";
            if(provider  != null){
                DeviceStatusEPS status = provider.calculateDeviceStatus();
                if(status != null){
                    value = status.toString();
                }
            }

            webview.loadUrl(getCallbackJs(callbackJs,value));
        } catch (Exception e){
            executeHookAPIFailCallJs(webview);
            Log.error("Get Device Status Error",e);
            Log.error("Fail to execute the hook:{}",e.getMessage());
        }
    }
}
