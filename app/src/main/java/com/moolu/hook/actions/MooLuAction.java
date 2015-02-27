package com.moolu.hook.actions;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.moolu.framework.Constants;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.json.gson.JsonUtil;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 */
public abstract class MooLuAction {

    private Map<String, String> params = Collections.emptyMap();
    public final void setParams(final Map<String, String> params) {
        if (params == null) {
            throw new IllegalArgumentException("Action parameters may not be null");
        }
        this.params = Collections.unmodifiableMap(params);
    }
    public final Map<String, String> getParams() {
        return this.params;
    }

    public static String getJavascriptWithArray(String callback,String[] parameter){
        StringBuffer parameterJs = new StringBuffer();
        StringBuffer js = new StringBuffer();
        for(int i=0;i<parameter.length;i++){
            if(i!=0){
                parameterJs.append(",");
            }
            parameterJs.append(JsonUtil.getJsonFromJavaObject(parameter[i]));
        }

        js.append(HookConstants.JAVASCRIPT)
                .append(callback)
                .append("([")
                .append(parameterJs)
                .append("])");

        String data=js.toString();
        data = data.replaceAll("\r", "");
        data = data.replaceAll("\n", "");
        return data;
    }
    public static void loadUrlInMainThread(final WebView view,final String url){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                view.loadUrl(url);
            }
        });
    }

    public void executeHookAPIFailCallJs(WebView webview){
        loadUrlInMainThread(webview,gethookAPIFailCallScript());
    }

    public static String gethookAPIFailCallScript(){
        return getCallbackJs(HookConstants.API_FAIL_CALL_FUNC,
                getErrorResponseJson(HookConstants.API_EXCUTE_ERROR_CODE));
    }

    public static String getErrorResponseJson(String code) {
        return JsonUtil.getRspCallbackJson(code, null);
    }

    public static String getGspCallbackJs(String callback, String data,String statusCode) {
        StringBuffer str = new StringBuffer();
        try {
            data = data.replaceAll("\r", "");
            data = data.replaceAll("\n", "");
            data = JsonUtil.getJsonFromJavaObject(data);
        } catch (Exception e) {
            data = "";
        }
        str.append(HookConstants.JAVASCRIPT)
                .append(callback)
                .append("(")
                .append(data)
                .append(",")
                .append("\"")
                .append(statusCode)
                .append("\"")
                .append(")");
        return str.toString();
    }

    public static String getCallbackJsWithTaskId(String taskId, String callback, String data) {
        StringBuffer str = new StringBuffer();
        try {
            data = data.replaceAll("\r", "");
            data = data.replaceAll("\n", "");
            data = JsonUtil.getJsonFromJavaObject(data);
        } catch (Exception e) {
            data = "";
        }
        str.append(HookConstants.JAVASCRIPT)
                .append(callback)
                .append("('")
                .append(taskId)
                .append("', ")
                .append(data)
                .append(")");
        return str.toString();
    }

    public static String getGspCallbackJsWithTaskId(String taskId, String callback,
                                                    String data,String statusCode){
        StringBuffer str = new StringBuffer();
        try {
            data = data.replaceAll("\r", "");
            data = data.replaceAll("\n", "");
            data = JsonUtil.getJsonFromJavaObject(data);
        } catch (Exception e) {
            data = "";
        }
        str.append(HookConstants.JAVASCRIPT)
                .append(callback)
                .append("('")
                .append(taskId)
                .append("', ")
                .append(data)
                .append(",")
                .append("\"")
                .append(statusCode)
                .append("\"")
                .append(")");
        return str.toString();
    }
    public static String getCallbackJs(String callback, String data) {
        StringBuffer str = new StringBuffer();
        try {
            data = data.replaceAll("\r", "");
            data = data.replaceAll("\n", "");
            data = JsonUtil.getJsonFromJavaObject(data);
        } catch (Exception e) {
            data = "";
        }
        str.append(HookConstants.JAVASCRIPT)
                .append(callback)
                .append("(")
                .append(data)
                .append(")");
        return str.toString();
    }
    public String getProxyDataJs(String url,String datajs,String method,
                                 String callbackjs,String dataMethod){
        if(method==null||method.length()==0){
            method= Constants.REQUEST_GET;
        }
        StringBuffer sb=getDataJs(dataMethod);
        sb.append("(");
        sb.append("\"").append(url).append("\"");
        sb.append(",");
        sb.append(datajs).append("()");
        sb.append(",");
        sb.append("\"").append(method).append("\"");
        sb.append(",");
        sb.append("\"").append(callbackjs).append("\"");
        sb.append(")");
        return sb.toString();
    }
    public StringBuffer getDataJs(String methodName){
        StringBuffer sb=new StringBuffer();
        sb.append("javascript:window.")
                .append(HookConstants.HOOK_OBJECT)
                .append(".")
                .append(methodName);
        return sb;
    }
    public String executeDataJs(Map<String,String> map){
        String datajs=map.get(HookConstants.DATA_JS);
        String paras=mapToJson(map);
        StringBuffer sb=getDataJs("setHookData");
        sb.append("(");
        sb.append(datajs).append("()");
        sb.append(",");
        sb.append("'").append(paras).append("'");
        sb.append(")");
        return sb.toString();
    }
    public String mapToJson(Map<String,String> map){
        String json= JsonUtil.getJsonFromJavaObject(map);
        return json;
    }
    public abstract void execute(final Context context,WebView webview,Hook hook);
}
