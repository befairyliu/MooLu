package com.moolu.hook.actions;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.HookException;
import com.moolu.hook.MooURLAction;
import com.moolu.util.StringUtil;

import org.slf4j.Logger;

import java.util.Map;

/**
 * moolu://function=GSPProxy&url=<url>&datajs=<function>&callbackjs=<function>&method=<GET|POST|PUT|DELETE>
 * Created by Nanan on 3/3/2015.
 */
public class GspProxyAction extends MooURLAction{
    private final static Logger Log = new NananLog(GspProxyAction.class);
    private String headers;

    @Override
    public void execute(Context context, WebView webview, Hook hook) {
        try{
            Map<String,String> map = this.getParams();
            if(map == null){
                throw new HookException();
            }
            String datajs = map.get(HookConstants.DATA_JS);
            String callbackjs = map.get(HookConstants.CALLBACK_JS);
            String url = map.get(HookConstants.URL);
            String method = map.get(HookConstants.METHOD);
            headers = map.get(HookConstants.HEADERS);
            if(StringUtil.IsNullOrEmpty(datajs)||StringUtil.IsNullOrEmpty(callbackjs)
                    || StringUtil.IsNullOrEmpty(url)){
                throw new HookException("request parameter missing.");
            }

            hook.setMap(map);
            String js = getProxyDataJs(url,datajs,method,callbackjs,"setGspProxyData");
            Log.debug("excute data js: "+js);
            loadUrlInMainThread(webview,js);
            hook.setWebview(webview);
            Log.debug("Gsp Json Proxy:{}",datajs);

        } catch (Exception e){
            executeHookAPIFailCallJs(webview);
            Log.error("Fail to execute the hook:{}",e.getMessage());
        }
    }

    @Override
    public String getProxyDataJs(String url, String datajs, String method, String callbackjs, String dataMethod) {
        if(method == null || method.length() == 0){
            method = Constants.REQUEST_GET;
        }
        StringBuffer sb = getDataJs(dataMethod);
        sb.append("(");
        sb.append("\"").append(url).append("\"");
        sb.append(",");
        sb.append(datajs).append("()");
        if(!StringUtil.IsNullOrEmpty(headers)){
            sb.append(",");
            sb.append(headers).append("()");
        } else {
            sb.append(",");
            sb.append(headers).append("");
        }
        sb.append(",");
        sb.append("\"").append(method).append("\"");
        sb.append(",");
        sb.append("\"").append(callbackjs).append("\"");
        sb.append(")");

        return sb.toString();
    }
}
