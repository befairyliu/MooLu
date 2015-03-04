package com.moolu.http.fileupload.action;

import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.framework.util.SupportedFeature;
import com.moolu.hook.HookException;
import com.moolu.hook.MooGetJsDataAction;
import com.moolu.http.fileupload.FileUploadConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

/**
 * Created by Nanan on 3/4/2015.
 */
public class UploadFileAction extends MooGetJsDataAction {
    private final static Logger Log = new NananLog(UploadFileAction.class);
    //WebView webView;

    @Override
    public void dataProcess(Context context, WebView webview, Handler mHandler, String dataValue, JSONObject jo) throws JSONException, HookException {
        boolean isSupported = SupportedFeature.checkIsSupportedFeature(FileUploadConstants.FEATURE_NAME,context);
        if(!isSupported){
            return;
        }

        /*
        String callbackJs = null;
        String function = null;
        this.webView = webview;
        try {
            callbackJs = jo.getString(HookConstants.CALLBACK_JS);
            function = jo.getString(HookConstants.FUNCTION);
            Activity activity = (Activity) context;
            String eid = EntityUtil.getSavedEntityId(activity);
            NApplication application = (NApplication) activity.getApplication();
            String webVersion = application.getCurrentWebVersion();
            String webResourcePath = ProcessUtil.getWebResourcePath(activity, eid, webVersion);
            Intent intent = new Intent(context, FileUploadActivity.class);
            intent.putExtra(HookConstants.FUNCTION, function);
            intent.putExtra(HookConstants.CALLBACK_JS, callbackJs);
            intent.putExtra(FileUploadConstants.JSONDATA, dataValue);
            intent.putExtra(FileUploadConstants.WEB_RESOURCE_PATH, webResourcePath);
            activity.startActivityForResult(intent, HookConstants.ACTION_RESULT);
        } catch (JSONException e) {
            Log.error("Get upload file info error");
        }
        */
    }

    /*
    @Override
    public void onActionResult(Context context, Handler mHandler, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                String callbackJs = bundle.get(HookConstants.CALLBACK_JS).toString();
                String jsonData = bundle.get(FileUploadConstants.JSONDATA).toString();
                String script = MooURLAction.getCallbackJs(callbackJs, jsonData);
                //LOG.debug("File Upload callback js:{}", script);
                Hook.sendStringMsg(mHandler,script);
            }
        } else {
            Log.error("Call back to file upload ack page error");
        }
    }
    */

}
