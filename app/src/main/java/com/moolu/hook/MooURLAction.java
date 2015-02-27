package com.moolu.hook;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.json.gson.JSONConstants;
import com.moolu.json.gson.JsonUtil;

import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 * Implementations of this class should be used to handled custom MooLu URLs received in the {@link android.webkit.WebView}.
 */
public abstract class MooURLAction {

    private final static Logger Log = new NananLog(MooURLAction.class);
    /**
     * Params for this action, this will either be empty if no params are supplied
     * or a unmodifiable map of params.
     */
    private Map<String, String> params = Collections.emptyMap();

    public static String getJavascript(String callback,String[] parameter){
        StringBuffer parameterJs = new StringBuffer();
        for(int i=0;i<parameter.length;i++){
            if(i != 0){
                parameterJs.append(",");
            }
            try{
                parameter[i] = parameter[i].replaceAll("\r","");
                parameter[i] = parameter[i].replaceAll("\n","");
            } catch (Exception e){
                Log.error(e.getMessage(),e);
            }
            parameterJs.append(HookConstants.QUOTA)
                    .append(parameter[i])
                    .append(HookConstants.QUOTA);
        }
        StringBuffer str = new StringBuffer();
        str.append(HookConstants.JAVASCRIPT)
                .append(callback)
                .append("(")
                .append(parameterJs.toString())
                .append(")");

        return str.toString();
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
                .append(callback).append("(")
                .append(parameterJs)
                .append(")");

        String data=js.toString();
        data = data.replaceAll("\r", "");
        data = data.replaceAll("\n", "");
        return data;
    }

    /**
     * User gson to transfer string to json object
     */
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

    /**
     * used for handling gsp proxy response
     */
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

    public void executeHookAPIFailCallJs(WebView webview){
        loadUrlInMainThread(webview,gethookAPIFailCallScript());
    }

    public static String gethookAPIFailCallScript(){
        return getCallbackJs(HookConstants.API_FAIL_CALL_FUNC,
                getErrorResponseJson(HookConstants.API_EXCUTE_ERROR_CODE));
    }

    /**
     * when execute general Hook API error ,call the function
     */
    public void hookAPIGeneralErrorCallback(Handler handler){
        Hook.sendStringMsg(handler, MooURLAction.gethookAPIFailCallScript());
    }

    private static boolean isObject(String str){
        if(str==null){
            return false;
        }
        try{
            JSONObject json= new JSONObject(str);
            json=null;
            return true;
        } catch(Exception e){
            return false;
        }

    }

    /**
     * Used by the {@link } to set the parameters for this action.
     * @param params A non null map of parameters for this action.
     */
    public final void setParams(final Map<String, String> params) {
        if (params == null) {
            throw new IllegalArgumentException("Action parameters may not be null");
        }
        this.params = Collections.unmodifiableMap(params);
    }

    /**
     * @return A unmodifiable map of parameters for given to this action.
     */
    protected final Map<String, String> getParams() {
        return this.params;
    }

    /*
        * Subclasses should implement this method to execute the action this class is responsible for.
        * @param context
        */
    public abstract void execute(final Context context,WebView webview,Hook hook);

    public String getProxyDataJs(String url,String datajs,String method,
                                 String callbackjs,String dataMethod){
        if(method==null||method.length()==0){
            method= Constants.REQUEST_GET;
        }
        StringBuffer sb = getDataJs(dataMethod);
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

    public String executeDataJs(Map<String,String> map){
        String datajs=map.get(HookConstants.DATA_JS);
        String paras = mapToJson(map);
        StringBuffer sb = getDataJs("setHookData");
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
    public StringBuffer getDataJs(String methodName){
        StringBuffer sb=new StringBuffer();
        sb.append("javascript:window.").append(HookConstants.HOOK_OBJECT).append(".").append(methodName);
        return sb;
    }

    public static String getErrorResponseJson(String code) {
        //Call JsonUtil function by converting HashMap<String,String>() to Json
        return JsonUtil.getRspCallbackJson(code, null);
    }

    /**
     * When toggle Language successfully callback and provide return parameters
     * call JsonUtil function by converting HashMap<String,String>() to Json
     */
    public static String getSuccessfulResponseJson(String code, String locale) {
        HashMap<String,String> body=new HashMap<String,String>();
        body.put(HookConstants.APPLOCALE, locale);
        return JsonUtil.getRspCallbackJson(code, body);
    }

    /**
     * When IsSupportedFeatureInAppAction successfully callback and provide
     * return parameters call JsonUtil function by converting HashMap<String,String>() to Json
     */
    public static String getSuccessfulForSupportFeatureResponseJson(String code, String name,
                                                                    boolean isSupported) {
        HashMap<String,String> body=new HashMap<String,String>();
        body.put(HookConstants.FEATURE_NAME, name);
        body.put(JSONConstants.IS_SUPPORT, String.valueOf(isSupported));
        return JsonUtil.getRspCallbackJson(code, body);

    }

    /**
     * Make the webview.loadUrl() which in Hook action switch to execute
     * in main thread to avoid the warning.
     */
    public static void loadUrlInMainThread(final WebView view,final String url){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                view.loadUrl(url);
            }
        });
    }
    /**
     * Make the webview.loadUrl(String url, Map<String, String> additionalHttpHeaders)
     * which in Hook action switch to execute in main thread to avoid the warning.
     */
    public static void loadUrlInMainThread(final WebView view,final String url,
                                           final Map<String, String> addHeaders){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                view.loadUrl(url,addHeaders);
            }
        });
    }

    /**
     * Make the webview.postUrl() which in Hook action switch to execute in
     * main thread to avoid the warning.
     */
    public static void postUrlInMainThread(final WebView view, final String url, final byte[] postData){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                view.postUrl(url, postData);
            }
        });
    }

}
