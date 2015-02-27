package com.moolu.hook.actions;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.hook.MooURLAction;
import com.moolu.util.StringUtil;

import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 */
public class GetLocaleAction extends MooURLAction{

    public String getLocale(Context context){
        return "en_US";
    }

    @Override
    public void execute(Context context, WebView webview, Hook hook) {
        try {
            Map<String, String> map = this.getParams();
            if(map == null){
                throw new HookException();
            }
            hook.setMap(map);
            String callbackJs = map.get(HookConstants.CALLBACK_JS);
            String locale = getLocale(context);
            if(StringUtil.isNotNullAndEmpty(callbackJs) || StringUtil.isNotNullAndEmpty(locale)){
                throw new HookException("request parameter missing");
            }
            loadUrlInMainThread(webview, getCallbackJs(callbackJs,locale));
        } catch (Exception e){
            executeHookAPIFailCallJs(webview);
        }
    }
}
