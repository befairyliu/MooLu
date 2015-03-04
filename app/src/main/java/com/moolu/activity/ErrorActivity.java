package com.moolu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.moolu.R;
import com.moolu.application.MApplication;
import com.moolu.framework.Constants;
import com.moolu.framework.entity.AppProcessor;
import com.moolu.framework.entity.EntityUtil;
import com.moolu.storage.prefs.PrefConstants;
import com.moolu.util.FileUtil;

public class ErrorActivity extends NananActivity implements View.OnClickListener {

    private Button button;
    private TextView textView;
    Intent intent;
    private MApplication application;
    private AppProcessor resourceProcessor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        application = (MApplication)getApplication();
        button = (Button)findViewById(R.id.retry_button);
        textView = (TextView)findViewById(R.id.tv_message);
        intent = getIntent();
        String msg = intent.getStringExtra("errorMsg");
        textView.setText(msg);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (intent.getIntExtra("flag",0)){
            case Constants.ERRORENTITY:
                SharedPreferences prefs = getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
                String currentEntityPath = prefs.getString(PrefConstants.ENTITY_PATH,null)+"entitylist.xml";
                FileUtil.deleteFile(currentEntityPath);
                Intent intent=new Intent(this,LoadingActivity.class);
                startActivity(intent);
                finish();
                break;
            case Constants.ERRORZIP:
                resourceProcessor = AppProcessor.getInstance();
                resourceProcessor.prepareWebResourceForApp(this, new Handler(),
                        EntityUtil.getApp(EntityUtil.getCurrentAppId(this), application.getEntity()));
                break;
            case Constants.ERRORAPK:
                /*
                resourceProcessor = AppProcessor.getInstance();
                resourceProcessor.prepareWebResourceForApp(this, new Handler(),
                EntityUtil.getApp(EntityUtil.getCurrentAppId(this), application.getEntity()));
                */
                break;
        }
    }

}
