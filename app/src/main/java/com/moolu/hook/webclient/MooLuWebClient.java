package com.moolu.hook.webclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.moolu.application.MApplication;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.actions.MooLuAction;
import com.moolu.hook.resolver.ResolverUI15;
import com.moolu.util.ActivityUtil;
import com.moolu.util.ReflectUtil;

import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Nanan on 3/3/2015.
 */
public class MooLuWebClient extends WebViewClient {

    private final static Logger Log = new NananLog(MooLuWebClient.class);
    private Activity owningActivity;
    protected final int progressDialogRef;
    protected final int noConnectionDialogRef;
    protected final Hook mHook;
    private boolean allowAllSSL = false;
    private LinearLayout linearLayout;

    public MooLuWebClient(final Activity owningActivity,final Hook hook,final int progressDialogRef,
                          final int noConnectionDialogRef,final int invalidVersionDialogRef,
                          LinearLayout linearLayout){
        this.owningActivity = owningActivity;
        this.mHook = hook;
        this.progressDialogRef = progressDialogRef;
        this.noConnectionDialogRef = noConnectionDialogRef;
        this.allowAllSSL = ((MApplication)owningActivity.getApplication()).isAllowAllSSL();
        this.linearLayout = linearLayout;
    }

    public boolean commomShouldOverrideUrlLoading(final WebView webView,final String url){
        if(hookHandle(webView,url)){
            return true;
        }
        loadurl(webView, url);
        return true;
    }

    public void loadurl(final WebView view, final String url) {
        view.loadUrl(url);
    }

    public boolean hookHandle(WebView webview, String url) {
        //app prefix handling.
        try {
            if (url.startsWith(HookConstants.ML_URL_PREFIX)) {
                Log.debug("===========================hook url:{}", url);
                String[] action = ResolverUI15.resolver(url, mHook);
                if (action != null) {
                    synchronized (this) {
                        if (url.indexOf(Constants.PAGE_TRANSATION) == -1) {
                            // WebView secWebview;
                            /*
                            if (currentWebView == wv1) {
                                // LOG.debug("currentWebView:webview1");
                                secWebview = wv2;
                            } else {
                                // LOG.debug("currentWebView:webview2");
                                secWebview = wv1;
                            }
                            */
                            webview.clearHistory();

                            MooLuAction dataAction = ReflectUtil.getAction(action);
                            dataAction.execute(owningActivity, webview, mHook);
                            //action.execute(this, mWebview, hook);
                            // this.overridePendingTransition(R.anim.page_in_rightleft,
                            // R.anim.page_out_rightleft);
                            // this.slideRightToLeft();
                        } else {
                            // action.execute(this, mWebview, hook);
                            // if (action instanceof BackToAppAction) {
                            // this.overridePendingTransition(R.anim.page_in_leftright,
                            // R.anim.page_out_leftright);
                            //  this.slideLeftToRight();
                            // } else {
                            // this.overridePendingTransition(R.anim.page_in_rightleft,
                            // R.anim.page_out_rightleft);
                            // this.slideRightToLeft();
                        }
                    }
                }
                return true;
            }
        } catch (Exception e){
            Log.error("hook handle error.");
        }
        return  false;
    }

    /**
     * Indicates that the {@link android.app.Activity} been used to show dialogs has been destroyed.
     * This is added as its possible for the web client to be informed of events even if the
     * {@link android.app.Activity} owning the {@link android.webkit.WebView} this is attached to
     * has been destroy. Trying to show/hide dialogs at this point results in an error been thrown
     * even though the app has been closed.
     **/
    public void ownerDestroyed() {
        // Synchronized around this to ensure we don't cause owning activity
        // to be null while it is been used
        synchronized (this) {
            this.owningActivity = null;
        }
    }

    /**
     * Calls {@link android.app.Activity#showDialog(int)} if the owning activity hasn't been destroyed.
     *
     * @param dialogId
     */
    public void showDialog(final int dialogId) {
        // Synchronized around this to ensure we call show Dialog only on a valid activity.
        synchronized (this) {
            if (this.owningActivity != null) {
                this.owningActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        try{
                           MooLuWebClient.this.owningActivity.showDialog(dialogId);
                        }catch(Exception e){
                            Log.error("showDialog error");
                        }
                    }
                });
            }
        }
    }

    /**
     * Calls {@link android.app.Activity#dismissDialog(int)} if the owning activity hasn't been destroyed.
     *
     * @param dialogId
     */
    public void dismissDialog(final int dialogId) {
        // Synchronized around this to ensure we call dismiss Dialog only on a valid activity.
        synchronized (this) {
            if (this.owningActivity != null) {
                this.owningActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        try{
                            MooLuWebClient.this.owningActivity.dismissDialog(dialogId);
                        }catch(Exception e){
                            Log.error("dismissDialog error");
                        }
                    }
                });
            }
        }
    }

    /*
     * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String,
     * android.graphics.Bitmap)
     */
    @Override
    public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
        showDialog(this.progressDialogRef);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
     */
    @Override
    public void onPageFinished(final WebView view, final String url) {
        dismissDialog(this.progressDialogRef);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int,
	 * java.lang.String, java.lang.String)
	 */
    @Override
    public void onReceivedError(final WebView view, final int errorCode, final String description,
                                final String failingUrl) {
        // showing to the user. Need to find a way to work around this.
        switch (errorCode) {
            case ERROR_CONNECT:
                showDialog(this.noConnectionDialogRef);
                break;
            case ERROR_HOST_LOOKUP:
                showDialog(this.noConnectionDialogRef);
                break;
            default:
                break;
        }
    }
    /*
     * @see com.hsbc.util.LoggedWebViewClient#onReceivedSslError(android.webkit.WebView,
     * android.webkit.SslErrorHandler, android.net.http.SslError)
     */
    @Override
    public void onReceivedSslError(final WebView view, final SslErrorHandler handler,
                                   final SslError error) {
        // live web server SHOULD have a correct SSL Cert.
        if(allowAllSSL){
            handler.proceed();
        }else{
            Log.info("The SSL Cert is not valid!");
            handler.cancel();
        }
    }


    /*
     * Just use those code in version 1.3
     *
     * @see android.webkit.WebViewClient#shouldOverrideUrlLoading(android.webkit.WebView,
     * java.lang.String)
     */
    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        // As we only want to do something with the url if the owning activity is still running
        // (i.e this view this client is attached to is still displayed), if it's not we can do nothing.
        if (this.owningActivity == null) {
            return false;
        }
        if (url.startsWith(HookConstants.TEL)) {
            return ActivityUtil.startCall(owningActivity, url);
        }else if (url.startsWith(HookConstants.MAIL_TO)) {
            return true;
        }else {
            return commomShouldOverrideUrlLoading(view,url);
        }
    }

    public static void setCookie(String url, Bundle cookieBundle,Context context) {
        if (cookieBundle == null) {
            return;
        }
        try {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            // set httpclient cookie
            // HSBCHttpClient client=HSBCHttpClient.getInstance(this);

            String cookiesdomain = cookieBundle.getString(Constants.COOKIES_DOMAIN);
            Bundle unitBundle = cookieBundle.getBundle(Constants.COOKIES);
            if (unitBundle == null) {
                return;
            }
            Set<String> keys = unitBundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                StringBuffer sb = new StringBuffer();
                String key = it.next();
                String value = unitBundle.getString(key);
                sb.append(key)
                        .append("=")
                        .append(value)
                        .append(";")
                        .append(Constants.DOMAIN)
                        .append("=")
                        .append(cookiesdomain)
                        .append(";path=/");
                cookieManager.setCookie(url, sb.toString());
                // set httpclient cookie
                // client.setCookie(cookiesdomain, key, value);
            }
            cookieSyncManager.sync();
        } catch (Exception e) {
            Log.error("set cookie error!", e);
        }
    }
}
