package com.moolu.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.webkit.WebView;

import com.moolu.R;
import com.moolu.activity.AppSelectActivity;
import com.moolu.activity.ErrorActivity;
import com.moolu.framework.Constants;
import com.moolu.framework.entity.model.Center;
import com.moolu.hook.HookConstants;

/**
 * Created by Nanan on 2/26/2015.
 */
public class ActivityUtil {
    /**
     * This method is for Hook action
     */
    public static void executeJsInWebview(final WebView webview,Bundle mBundle){
        if (mBundle != null && mBundle.getString(HookConstants.MESSAGE_DATA) != null) {
            String script = mBundle.getString(HookConstants.MESSAGE_DATA);
            if(!StringUtil.IsNullOrEmpty(script)){
                webview.loadUrl(script);
            }
        }
    }


    /**
     * According to the direction of mobile phone , get the rotation Angle of camera preview screen
     */
    public static int getPreviewDegree(Activity activity){
        int rotation = activity.getWindowManager().getDefaultDisplay().getOrientation();
        int degree = 0;
        switch(rotation){
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    /**
     * For making phone call
     */
    public static boolean startCall(Context context,String url){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
        return true;
    }

    /**
     * For making device model
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public static AlertDialog createDialog(final Context context,String msg,String title, final int action){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (action){
                            case Constants.SYSTEMEXIT:
                                System.exit(0);
                                break;
                            case Constants.DIALOGDISMISS:
                                dialog.dismiss();
                        }
                    }
                });
        return builder.create();
    }
//    public static void startMainBrowserActivity(Context context) {
//        Intent intent = new Intent();
//        intent.setClass(context, LaunchTestActivity.class);
//        context.startActivity(intent);
//        //context.
//    }

    public static void startAppSelectActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AppSelectActivity.class);
        context.startActivity(intent);
    }
    public static void showErrorMsg(Context context,String msg,int flag) {
        Intent intent = new Intent();
        intent.putExtra("errorMsg",msg);
        intent.putExtra("flag",flag);
        intent.setClass(context, ErrorActivity.class);
        context.startActivity(intent);
    }
    public static void showErrorMsg(Context context,String msg,Handler handler,Center app) {
        Intent intent = new Intent();
        Bundle bundle =new Bundle();
        intent.putExtra("errorMsg",msg);
        intent.setClass(context, ErrorActivity.class);
        context.startActivity(intent);
    }
}
