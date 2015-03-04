package com.moolu.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.igexin.sdk.PushManager;
import com.moolu.R;
import com.moolu.application.MApplication;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;
import com.moolu.hook.actions.MooLuAction;
import com.moolu.hook.resolver.ResolverUI15;
import com.moolu.hook.webclient.MooLuWebClient;
import com.moolu.http.volley.ApiConstDef;
import com.moolu.http.volley.StringRequestUtil;
import com.moolu.plugins.baidu.BaiduLocationUtil;
import com.moolu.plugins.baidu.MainLocationListener;
import com.moolu.util.ReflectUtil;

import org.slf4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainBrowserActivity extends NananActivity {

    private final static Logger Log = new NananLog(MainBrowserActivity.class);
    private WebView mWebview = null;
    private BaiduLocationUtil mBaiduLocationUtil = null;
    private LocationClient mLocationClient = null;
    private MooLuWebClient webClient;
    private Hook mHook;
    private long currentTime=0;
    boolean flag=true;
    private LinearLayout linearLayout;
    private boolean isExit=false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case MainLocationListener.RECEIVE_BAIDU_LOCATION:
                        BDLocation location = (BDLocation) msg.obj;
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_browser);
        linearLayout = (LinearLayout)findViewById(R.id.loading_main);
        init();
    }

    //-----------------------------Init start------------------------------
    private void init() {
        mWebview = (WebView) findViewById(R.id.webivew);
        Intent intent= getIntent();
        String page=intent.getStringExtra("homepage");
        String path=intent.getStringExtra("path");
        //dismissDialog();
        if(page==null){
            loadUrl("http://10.61.213.156:8080/MooLu/config.txt");
        }else{
            loadUrl(Constants.PREVENT_BEFORE+path+ File.separator+page);
        }

        // initPush();
        //initLocation();
    }

    private void initPush() {
        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    private void initWebview() {
        mHook = new Hook(this, mHandler);
        mWebview.addJavascriptInterface(mHook, "hook");
        //mWebview.setBackgroundColor(0);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            //The default value is true for API level ICE_CREAM_SANDWICH_MR1 and below, and false for API level JELLY_BEAN and above.
            mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        mWebview.getSettings().setAllowFileAccess(true);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
            //Chrome remote debug is only enable for KITKAT and above.
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebview.setWebChromeClient(new WebChromeClient());
        webClient =  new MooLuWebClient(this, mHook,Constants.PROGRESS_DIALOG, Constants.NO_CONNECTION_RETRY_DIALOG, Constants.INVALID_VER_DIALOG,linearLayout);
        mWebview.setWebViewClient(webClient);
    }
    public boolean commomOverrideUrlLoading(final WebView view, final String url) {
        Log.debug("shouldOverrideUrlLoading.." + url);
        if (hookHandle(view, url)) {
            return true;
        }
        // loadurl(view, url);
        return true;
    }

    public boolean hookHandle(WebView webview, String url) {
        try {
            if (url.startsWith(HookConstants.ML_URL_PREFIX)) {
                Log.debug("===========================hook url:{}", url);
                String[] action = ResolverUI15.resolver(url, mHook);
                if (action != null) {
                    synchronized (this) {
                        if (url.indexOf(Constants.PAGE_TRANSATION) != -1) {
                            webview.clearHistory();

                            MooLuAction dataAction= ReflectUtil.getAction(action);
                            dataAction.execute(this,webview,mHook);
                            this.slideRightToLeft();
                        } else {

                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            Log.error("hook handle error!", e);
        }
        return false;
    }

    private void initLocation() {
        MainLocationListener listener = new MainLocationListener(MainBrowserActivity.this, mHandler);
        ((MApplication)getApplication()).setLocationListener(listener);
        mLocationClient = ((MApplication)getApplication()).mLocationClient;
        mLocationClient.start();
        mLocationClient.requestLocation();
    }
    //--------------------Init end-----------------------

    private void loadUrl(String pageName) {
        //mWebview.loadUrl("http://www.baidu.com");
        //mWebview.loadUrl("file:///android_asset/web/index.html");
        //mWebview.loadUrl("http://192.168.43.71/MooLu/web/index.html");
        initWebview();
        mWebview.loadUrl(pageName);
        //  linearLayout.setVisibility(View.INVISIBLE);
        // dismissDialog();
    }

    /**
     * Listener and errorListener callback for HTTP request with volley.
     */
    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            String callbackMark = StringRequestUtil.getCallbackMark(response);
            if (callbackMark.equals(String.valueOf(ApiConstDef.loginType))) {
                Toast.makeText(MainBrowserActivity.this, response, Toast.LENGTH_LONG).show();
            }

        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainBrowserActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void doLogin() {
        final String username = "username";
        final String password = "password";
        Map<String, String> map = new HashMap<String, String>();
        map.put(ApiConstDef.CALLBACK_MARK, String.valueOf(ApiConstDef.loginType));
        map.put("username", username);
        map.put("password", password);
        requestQueue.add(StringRequestUtil.getStringRequest(ApiConstDef.loginType, map, listener, errorListener));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //mBaiduLocationUtil.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack(); // goBack
            return true;
        }else{
            Timer tExit = null;
            if (isExit == false) {
                isExit = true;
                Toast.makeText(MainBrowserActivity.this, "Press Again", Toast.LENGTH_LONG).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);

            } else {
                finish();
                System.exit(0);

            }
            return false;
        }

    }
}
