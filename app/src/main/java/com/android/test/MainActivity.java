package com.android.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.moolu.R;

/*
*          『未曲』
*
*        冬已没，马尾远，
*        烟火入空，花千树。
*
*        春檐下，木窗边，
*        时光折叠，梦潮湿。
*
*        故事春风，早已入词，
*        食指轻轻，乙曲未满。
*
*        不懂，不爱，
*        不惜，不痛。
*
*        你,
*        为谁等？
*
*                    -- 南安
*                   除夕 杭州
*
*/
public class MainActivity extends Activity {

    private String url = "http://shouji.baidu.com";
    private Button back;
    private Button refresh;
    private TextView titleView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view,String title){
                titleView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        webView.setDownloadListener(new MyDownload());
        refresh.setOnClickListener(new MyListener());
        back.setOnClickListener(new MyListener());

    }

    public void init(){
        back = (Button)findViewById(R.id.back);
        refresh = (Button)findViewById(R.id.refresh);
        titleView = (TextView)findViewById(R.id.title);
        webView = (WebView)findViewById(R.id.webView);
    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.refresh:
                    webView.reload();
                    break;
                case R.id.back:
                    finish();
                    break;
                default:
                    break;
            }

        }
    }

    private class MyDownload implements DownloadListener{
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            //new HttpThread(url).start();

            if(url.endsWith(".apk")){
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        }
    }

}
