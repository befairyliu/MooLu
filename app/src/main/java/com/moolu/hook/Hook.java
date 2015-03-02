package com.moolu.hook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.moolu.activity.NananActivity;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.hook.actions.MooLuAction;
import com.moolu.hook.resolver.ResolverUI15;
import com.moolu.http.pack.MultipleProxyTask;
import com.moolu.http.pack.ProxyJsonTask;
import com.moolu.http.pack.ProxyResponse;
import com.moolu.http.pack.ProxyTask;
import com.moolu.http.proxy.GSPProxyResponse;
import com.moolu.http.proxy.GspProxyTask;
import com.moolu.http.proxy.MultipleGSPProxyTask;
import com.moolu.util.ActivityUtil;
import com.moolu.util.DeviceUtil;
import com.moolu.util.ReflectUtil;
import com.moolu.util.StringUtil;

import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 */
public class Hook implements ActivityCallback{

    protected final static Logger Log = new NananLog(Hook.class);
    protected final Activity context;
    protected final static int PROXY_TASK_REF = 3;
    protected final static int PROXY_JSON_TASK_REF = 4;
    protected final static int MULTIPLE_PROXY_TASK_REF = 5;//RDC Project : Proxy Connections Scholes 18Jun2013
    protected final static int GSP_PROXY_JSON_TASK_REF = 6;
    protected final static int MULTIPLE_GSP_PROXY_TASK_REF = 7;
    protected WebView webview;
    protected Handler handler;
    private Map<String, String> map;
    private String dataValue;
    private MooLuAction mooLuAction;
    /* for sotp add by June 2012-12-07 */
    public String logOffFlag = "";
    public String logOffFUrl = "";
    public String tokenType = "";

    // JW [Aug-2014] Promote extendability and Mobile SaaS integration
    //private static final String PLATFORM_KEY  = "platform";
    protected static final String PLATFORM_KEY  = "platform";
    //private static final String PLATFORM = "A";
    protected static final String PLATFORM = "A";
    //private static final String DEVICE_TYPE_KEY = "devtype";
    protected static final String DEVICE_TYPE_KEY = "devtype";
    //private static final String DEVICE_TYPE_MOBILE = "M";
    protected static final String DEVICE_TYPE_MOBILE = "M";
    //private static final String DEVICE_TYPE_TABLET = "T";
    protected static final String DEVICE_TYPE_TABLET = "T";

    //RDC Project : Proxy Connections Scholes 18Jun2013 start
    public enum RequestType {
        JSON,
        PLAIN,
        MULTIPLE;
    }

    public Hook(Activity context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }


    @Override
    public void handleCallback(final AsyncTaskWithCallback task, final int ref) {
        //Promote extendability and Mobile SaaS integration
        if (!(context instanceof NananActivity)) {
            // add fail-safe handling to ensure caller invoking this method
            // knows the context instance must be HSBCActivity
            throw new RuntimeException("context is not instance of HSBCActivity");
        }

        try {
            // JW [Aug-2014] Promote extendability and Mobile SaaS integration
            ((NananActivity)context).removeTask(task);
            if (PROXY_TASK_REF == ref) {
                handleProxyTask((ProxyTask) task);
            } else if (PROXY_JSON_TASK_REF == ref) {
                handleJsonProxyTask((ProxyJsonTask) task);
            } else if (MULTIPLE_PROXY_TASK_REF == ref) {
                MultipleProxyTask multipleProxyTask = (MultipleProxyTask) task;
                handleMultipleProxyTask(multipleProxyTask.getTaskId(),
                        multipleProxyTask.getError(), multipleProxyTask.getResult());
            } else if (GSP_PROXY_JSON_TASK_REF == ref){
                handleGspProxyTask((GspProxyTask) task);
            } else if (MULTIPLE_GSP_PROXY_TASK_REF == ref) {
                MultipleGSPProxyTask multipleGSPProxyTask = (MultipleGSPProxyTask) task;
                handleMultipleGSPProxyTask(multipleGSPProxyTask.getTaskId(),
                        multipleGSPProxyTask.getError(),
                        multipleGSPProxyTask.getResult());
            }
        } catch (Exception e) {
            Log.error("handle callback in hook error.", e);
            executeHookAPIFailCallJs(webview);
        }
    }

    public String getLogOffFlag() {
        return logOffFlag;
    }

    public void setLogOffFlag(String logOffFlag) {
        this.logOffFlag = logOffFlag;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getLogOffFUrl() {
        return logOffFUrl;
    }

    public void setLogOffFUrl(String logOffFUrl) {
        this.logOffFUrl = logOffFUrl;
    }

    public MooLuAction getMooLuAction() {
        return mooLuAction;
    }

    public void setMooLuAction(MooLuAction mooLuAction) {
        this.mooLuAction = mooLuAction;
    }

    public WebView getWebview() {
        return webview;
    }

    public void setWebview(WebView webview) {
        this.webview = webview;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setProxyData(String url, String dataValue, String method, String callbackjs) {
        proxyAPI(url, dataValue, method, callbackjs, RequestType.PLAIN);
    }

    public void setMultipleProxyData(String taskId, String url, String params, String method, String callbackJs) {
        proxyAPI(taskId, url, params, method, callbackJs, RequestType.MULTIPLE);
    }

    @JavascriptInterface
    public void setProxyDataJson(String url, String dataValue, String method, String callbackjs) {
        proxyAPI(url, dataValue, method, callbackjs, RequestType.JSON);
    }
    public void setGspProxyData(String url, String dataValue, String headers,String method, String callbackjs) {
        proxyGspAPI(url, dataValue, headers,method, callbackjs);
    }
    public void setGspMultipleProxyData(String taskId,String url, String dataValue, String headers,String method, String callbackjs) {
        proxyGspAPI(taskId, url, dataValue, headers,method, callbackjs);
    }

    @JavascriptInterface
    public void setHookData(String dataValue, String json) {
        this.dataValue = dataValue;
        try {
            JSONObject jo = new JSONObject(json);
            // String function = jo.getString(HookConstants.FUNCTION);
            Map<String, String> map = mooLuAction.getParams();
            if(map==null){
                throw new HookException();
            }
            String[] action=null;
            if(map.get(HookConstants.FUNCTION)!=null){
                action = ResolverUI15.resolveByFunction(map.get(HookConstants.FUNCTION));
            }else{
                throw new Exception("function name not found!");
            }

            if (action == null) {
                throw new Exception("Hook API not support!");
            }
            ReflectUtil.invoke(action, context, this, webview, jo, dataValue);
        } catch (Exception e) {
            //Log.error("Hook API parameter error for:{}/{}", dataValue, json);
            //Log.error("Set hook data error",e);
            executeHookAPIFailCallJs(webview);
        }
    }

    public void proxyAPI(String url, String params, String method, String callbackJs, RequestType type) {
        proxyAPI(null, url, params, method, callbackJs, type);
    }

    public void proxyAPI(String taskId, String url, String params, String method, String callbackJs, RequestType type) {
        // Promote extendability and Mobile SaaS integration
        if (!(context instanceof NananActivity)) {
            // add fail-safe handling to ensure caller invoking this method knows the context instance must be HSBCActivity
            throw new RuntimeException("context is not instance of BaseActivity");
        }
        try {

            if (!DeviceUtil.deviceOnline(context)) {
                String script = mooLuAction.getCallbackJs(callbackJs, mooLuAction.getErrorResponseJson(HookConstants.NETWORK_ERROR_CODE));
                Log.debug("device network unvailable:" + script);
                this.webview.loadUrl(script);
                return;
            }
            if (StringUtil.IsNullOrEmpty(url) || StringUtil.IsNullOrEmpty(callbackJs)) { // those two parameters
                // are mandatory
                return;
            }

            url = java.net.URLDecoder.decode(url);
            if (url.indexOf(" ") != -1) {
                url = url.replaceAll(" ", "%20");
            }

            if (method == null) {
                method = "GET";
            } else {
                method = method.toUpperCase();
            }
            if (!method.equals(Constants.REQUEST_POST) && !method.equals(Constants.REQUEST_GET)) {
                method = "GET";
            }

            //add the headers for all proxy hook.
            String headers = "{'device-model':'" + ActivityUtil.getDeviceName()
                    + "','accept-language':'en-, en-US','accept-charset':'utf-8, iso-8859-1, utf-16, *;q=0.7','accept-encoding':'*'}";
            //remove the tablet support checking, for this feature should apply to all country
            final String mi_log = String.format("%s=%s&%s=%s", PLATFORM_KEY, PLATFORM, DEVICE_TYPE_KEY,
                    //Promote extendability and Mobile SaaS integration
                    ((NananActivity) context).isSupportOrientationChange()?DEVICE_TYPE_TABLET:DEVICE_TYPE_MOBILE);
            if(!StringUtil.IsNullOrEmpty(params)){
                params += "&" + mi_log;
            } else {
                params = mi_log;
            }

            switch (type) {
                case JSON:
                    ProxyJsonTask task =  new ProxyJsonTask(this.context, this, PROXY_JSON_TASK_REF);
                    // JW [Aug-2014] Promote extendability and Mobile SaaS integration
                    ((NananActivity) context).addTask(task);
                    task.execute(url, params, method, callbackJs, headers);
                    break;
                case PLAIN:
                    ProxyTask task2 = new ProxyTask(this.context, this, PROXY_TASK_REF);
                    // JW [Aug-2014] Promote extendability and Mobile SaaS integration
                    ((NananActivity) context).addTask(task2);
                    task2.execute(url, params, method, callbackJs, headers);
                    break;
                case MULTIPLE:
                    AsyncTaskWithCallback<String, Void, ProxyResponse> task3 = new MultipleProxyTask(taskId, this.context, this, MULTIPLE_PROXY_TASK_REF);
                    // JW [Aug-2014] Promote extendability and Mobile SaaS integration
                    ((NananActivity) context).addTask(task3);
                    task3.execute(url, params, method, callbackJs, headers);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            Log.error("proxyAPI error", e);
            executeHookAPIFailCallJs(webview);
        }
    }

    public void proxyGspAPI(String url, String params, String headers,String method, String callbackJs){
        proxyGspAPI(null, url, params, headers, method, callbackJs);
    }

    //used for handle gsp proxy
    public void proxyGspAPI(String taskId, String url, String params, String headers,String method, String callbackJs) {
        // JW [Aug-2014] Promote extendability and Mobile SaaS integration
        if (!(context instanceof NananActivity)) {
            // add fail-safe handling to ensure caller invoking this method knows the context instance must be HSBCActivity
            throw new RuntimeException("context is not instance of HSBCActivity");
        }
        try {
            if (!DeviceUtil.deviceOnline(context)) {
                String script = mooLuAction.getCallbackJs(callbackJs, mooLuAction.getErrorResponseJson(HookConstants.NETWORK_ERROR_CODE));
                Log.debug("device network unvailable:" + script);
                this.webview.loadUrl(script);
                return;
            }
            if (StringUtil.IsNullOrEmpty(url) || StringUtil.IsNullOrEmpty(callbackJs)) { // those two parameters
                // are mandatory
                return;
            }
            url = java.net.URLDecoder.decode(url);
            if (url.indexOf(" ") != -1) {
                url = url.replaceAll(" ", "%20");
            }

            if (method == null) {
                method = "GET";
            } else {
                method = method.toUpperCase();
            }
            if(!StringUtil.IsNullOrEmpty(headers)){
                JSONObject object = new JSONObject(headers);
                object.put("device-model", ActivityUtil.getDeviceName());
                object.put("accept-language", "en-, en-US");
                object.put("accept-charset", "utf-8, iso-8859-1, utf-16, *;q=0.7");
                object.put("accept-encoding", "*");
                headers = object.toString();
            }else{
                headers += "{'device-model':'" + ActivityUtil.getDeviceName() + "','accept-language':'en-, en-US','accept-charset':'utf-8, iso-8859-1, utf-16, *;q=0.7','accept-encoding':'*'}";
            }
            if(taskId == null){
                GspProxyTask task = new GspProxyTask(this.context, this, GSP_PROXY_JSON_TASK_REF);
                // JW [Aug-2014] Promote extendability and Mobile SaaS integration
                ((NananActivity)context).addTask(task);
                task.execute(url, params, headers, method, callbackJs);
            }else{
                AsyncTaskWithCallback<String, Void, GSPProxyResponse> task2 = new MultipleGSPProxyTask(taskId, this.context, this, MULTIPLE_GSP_PROXY_TASK_REF);
                ((NananActivity)context).addTask(task2);
                task2.execute(url, params, headers, method, callbackJs);
            }
        } catch (Exception e) {
            Log.error("GSP proxyAPI error", e);
            executeHookAPIFailCallJs(webview);
        }
    }

    //Promote extendability and Mobile SaaS integration
    protected void handleProxyTask(final ProxyTask task) {
        handleProxyTask(task.getError(), task.getResult());
    }

    //Promote extendability and Mobile SaaS integration
    protected void handleGspProxyTask(final GspProxyTask task){
        handleGspProxyTask(task.getError(), task.getResult());

    }

    //handle the GSPProxy hook call back
    private void handleGspProxyTask(int flag, GSPProxyResponse responseResult) {
        String rs = "";
        String callbackJs = responseResult.getCallbackJs();
        String results = responseResult.getResponseStr();
        int stateCode = responseResult.getHttpStatusCode();
        String statusCode  = String.valueOf(stateCode);
        switch (flag) {
            case AsyncTaskWithCallback.SUCCESS:
                if (results != null) {
                    try {
                        rs = results;
                        if(!StringUtil.IsNullOrEmpty(rs)){
                            if (Log.isDebugEnabled()) {
                                int count = (int) (Math.ceil((double) rs.length() / 3000));
                                for (int i = 0; i < count; i++) {
                                    if (i == count - 1) {
                                        Log.debug("GSP proxy response" + i + ":{}", rs.substring(i * 3000, rs.length()));
                                    } else
                                        Log.debug("GSP proxy response" + i + ":{}", rs.substring(i * 3000, (i + 1) * 3000));
                                }
                            }
                            String script = mooLuAction.getGspCallbackJs(callbackJs, rs, statusCode);
                            Log.debug("excute callback:"+script);
                            this.webview.loadUrl(script);
                        }else {
                            String errorCode = HookConstants.OTHER_ERROR_CODE;
                            triggerGspCallbackFunction(statusCode,rs,callbackJs,errorCode);
                        }

                    } catch (IllegalStateException e1) {
                        Log.error("GspProxyTask error",e1);
                    }
                } else {
                    String errorCode = HookConstants.OTHER_ERROR_CODE;
                    triggerGspCallbackFunction(statusCode,rs,callbackJs,errorCode);
                }
                break;
            case AsyncTaskWithCallback.FAILED:
                if (StringUtil.IsNullOrEmpty(callbackJs)) {
                    //callback js not exist and then call the hook api error
                    String script = mooLuAction.gethookAPIFailCallScript();
                    this.webview.loadUrl(script);
                } else {
                    String errorCode = HookConstants.TIMEOUT_ERROR_CODE;
                    triggerGspCallbackFunction(statusCode,rs,callbackJs,errorCode);
                }
                break;
        }
    }

    /***
     * copy http status code into json response and trigger the call back
     */
    private void triggerGspCallbackFunction(String statusCode , String rs, String callbackJs ,String errorCode){
        String errorStatusCode="";
        if(!StringUtil.IsNullOrEmpty(statusCode)){
            errorStatusCode = statusCode;
        }else{
            errorStatusCode = errorCode;
        }
        String script = mooLuAction.getGspCallbackJs(callbackJs, rs, errorStatusCode);
        Log.debug("excute callback:"+script);
        this.webview.loadUrl(script);
    }
    private void triggerGspCallbackFunctionWithTaskId(String taskId, String statusCode, String rs,
                                                      String callbackJs, String errorCode){
        String errorStatusCode = "";
        if (!StringUtil.IsNullOrEmpty(statusCode)) {
            errorStatusCode = statusCode;
        } else {
            errorStatusCode = errorCode;
        }
        String script = mooLuAction.getGspCallbackJsWithTaskId(taskId, callbackJs, rs,
                errorStatusCode);
        Log.debug("excute callback:" + script);
        this.webview.loadUrl(script);
    }

    //Promote extendability and Mobile SaaS integration
    protected void handleJsonProxyTask(final ProxyJsonTask task) {
        handleProxyTask(task.getError(), task.getResult());
    }

    protected void handleMultipleGSPProxyTask(String taskId, int flag,GSPProxyResponse result) {
        String rs = "";
        String callbackJs = result.getCallbackJs();
        String results = result.getResponseStr();
        int stateCode = result.getHttpStatusCode();
        String statusCode = String.valueOf(stateCode);
        switch (flag) {
            case AsyncTaskWithCallback.SUCCESS:
                if (results != null) {
                    try {
                        rs = results;
                        if (!StringUtil.IsNullOrEmpty(rs)) {
                            if (Log.isDebugEnabled()) {
                                int count = (int) (Math
                                        .ceil((double) rs.length() / 3000));
                                for (int i = 0; i < count; i++) {
                                    if (i == count - 1) {
                                        Log.debug("GSP proxy response" + i + ":{}",
                                                rs.substring(i * 3000, rs.length()));
                                    } else
                                        Log.debug("GSP proxy response" + i + ":{}",
                                                rs.substring(i * 3000,
                                                        (i + 1) * 3000));
                                }
                            }
                            String script = mooLuAction.getGspCallbackJsWithTaskId(taskId,
                                    callbackJs, rs, statusCode);
                            Log.debug("excute callback:" + script);
                            this.webview.loadUrl(script);
                        } else {
                            String errorCode = HookConstants.OTHER_ERROR_CODE;
                            triggerGspCallbackFunctionWithTaskId(taskId, statusCode, rs, callbackJs,
                                    errorCode);
                        }

                    } catch (IllegalStateException e1) {
                        Log.error("GspProxyTask error", e1);
                    }
                } else {
                    String errorCode = HookConstants.OTHER_ERROR_CODE;
                    triggerGspCallbackFunctionWithTaskId(taskId, statusCode, rs, callbackJs,
                            errorCode);
                }
                break;
            case AsyncTaskWithCallback.FAILED:
                // handler.sendEmptyMessage(HookConstants.HIDE_PROGRESS_MSG);
                if (StringUtil.IsNullOrEmpty(callbackJs)) {
                    //callback js not exist and then call the hook api error
                    String script = mooLuAction.gethookAPIFailCallScript();
                    this.webview.loadUrl(script);
                } else {
                    String errorCode = HookConstants.TIMEOUT_ERROR_CODE;
                    triggerGspCallbackFunctionWithTaskId(taskId, statusCode, rs, callbackJs,
                            errorCode);
                }
                break;
        }
    }

    //RDC Project : Proxy Connections Scholes 18Jun2013 start
    // JW [Aug-2014] Promote extendability and Mobile SaaS integration
    protected void handleMultipleProxyTask(String taskId, int flag, ProxyResponse result) {
        String callbackJs = result.getCallbackJs();
        switch (flag) {
            case AsyncTaskWithCallback.SUCCESS:
                StringBuffer results = result.getResponseStr();
                // handler.sendEmptyMessage(HookConstants.HIDE_PROGRESS_MSG);
                if (results != null) {
                    String rs = results.toString();
                    if (Log.isDebugEnabled()) {
                        int count = (int) (Math.ceil((double) rs.length() / 3000));
                        for (int i = 0; i < count; i++) {
                            if (i == count - 1) {
                                Log.debug("proxy response" + i + ":{}", rs.substring(i * 3000, rs.length()));
                            } else {
                                Log.debug("proxy response" + i + ":{}", rs.substring(i * 3000, (i + 1) * 3000));
                            }
                        }
                    }
                    String script = mooLuAction.getCallbackJsWithTaskId(taskId, callbackJs, rs);
                    Log.debug("excute callback:"+script);
                    this.webview.loadUrl(script);
                } else {
                    String script = mooLuAction.getCallbackJsWithTaskId(taskId, callbackJs, mooLuAction.getErrorResponseJson(HookConstants.OTHER_ERROR_CODE));
                    Log.debug("excute callback:"+script);
                    this.webview.loadUrl(script);
                }
                break;
            case AsyncTaskWithCallback.FAILED:
                // handler.sendEmptyMessage(HookConstants.HIDE_PROGRESS_MSG);
                if (StringUtil.IsNullOrEmpty(callbackJs)) {
                    //callback js not exist and then call the hook api error
                    String script = mooLuAction.gethookAPIFailCallScript();
                    this.webview.loadUrl(script);
                } else {
                    String script = mooLuAction.getCallbackJsWithTaskId(taskId, callbackJs, mooLuAction.getErrorResponseJson(HookConstants.TIMEOUT_ERROR_CODE));
                    Log.debug("excute callback:"+script);
                    this.webview.loadUrl(script);
                }
                break;
        }
    }
    //RDC Project : Proxy Connections Scholes 18Jun2013 end
    private void handleProxyTask(int flag, ProxyResponse result) {
        String callbackJs = result.getCallbackJs();
        switch (flag) {
            case AsyncTaskWithCallback.SUCCESS:
                StringBuffer results = result.getResponseStr();
                // handler.sendEmptyMessage(HookConstants.HIDE_PROGRESS_MSG);
                if (results != null) {
                    String rs = results.toString();
                    if (Log.isDebugEnabled()) {
                        int count = (int) (Math.ceil((double) rs.length() / 3000));
                        for (int i = 0; i < count; i++) {
                            if (i == count - 1) {
                                Log.debug("proxy response" + i + ":{}", rs.substring(i * 3000, rs.length()));
                            } else {
                                Log.debug("proxy response" + i + ":{}", rs.substring(i * 3000, (i + 1) * 3000));
                            }
                        }
                    }
                    String script = mooLuAction.getCallbackJs(callbackJs, rs);
                    Log.debug("excute callback:"+script);
                    this.webview.loadUrl(script);
                } else {
                    String script = mooLuAction.getCallbackJs(callbackJs, mooLuAction.getErrorResponseJson(HookConstants.OTHER_ERROR_CODE));
                    Log.debug("excute callback:"+script);
                    this.webview.loadUrl(script);
                }
                break;
            case AsyncTaskWithCallback.FAILED:
                // handler.sendEmptyMessage(HookConstants.HIDE_PROGRESS_MSG);
                if (StringUtil.IsNullOrEmpty(callbackJs)) {
                    //callback js not exist and then call the hook api error
                    String script = mooLuAction.gethookAPIFailCallScript();
                    this.webview.loadUrl(script);
                } else {
                    String script = mooLuAction.getCallbackJs(callbackJs, mooLuAction.getErrorResponseJson(HookConstants.TIMEOUT_ERROR_CODE));
                    Log.debug("excute callback:"+script);
                    this.webview.loadUrl(script);
                }
                break;
        }
    }

    public void sendMsg(int MsgId) {
        if (handler != null) {
            handler.sendEmptyMessage(MsgId);
        } else {
            Log.error("handler should not be null");
        }
    }

    public void sendMsgObj(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            Log.error("handler should not be null!");
        }
    }

    public void sendStringMsg(String script) {
        sendStringMsg(this.handler,script,false);
    }
    public void loadUrlInCurrentWebview(String script,boolean setHeader) {
        sendStringMsg(this.handler,script,setHeader);
    }
    public static void sendStringMsg(Handler handler,String script) {
        sendStringMsg(handler,script,false);
    }

    /**
     * Send back the javascript to the MainBrowser's webview to execute
     */
    public static void sendStringMsg(Handler handler,String script,boolean setHeader){
        if (handler != null && !StringUtil.IsNullOrEmpty(script)) {
            Message message = Message.obtain();
            message.what = HookConstants.EXECUTE_JAVASCRIPT;
            Bundle mBundle = new Bundle();
            mBundle.putString(HookConstants.MESSAGE_DATA, script);
            if(setHeader){
                mBundle.putBoolean(HookConstants.SET_HEADER, true);
            }
            message.setData(mBundle);
            handler.sendMessage(message);
        } else {
            Log.error("handler should not be null!");
        }
    }

    public void executeHookAPIFailCallJs(WebView webview) {
        if(webview!=null){
            webview.loadUrl(MooLuAction.gethookAPIFailCallScript());
        }
    }
    public void executeHookAPIFailCallJs() {
        sendStringMsg(MooLuAction.gethookAPIFailCallScript());
    }
}
