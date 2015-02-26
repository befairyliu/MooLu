package com.moolu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.moolu.R;
import com.moolu.adapter.AppSelectAdapter;
import com.moolu.application.NApplication;
import com.moolu.framework.Constants;
import com.moolu.framework.entity.EntityUtil;
import com.moolu.framework.entity.model.Center;
import com.moolu.util.DeviceUtil;
import com.moolu.util.SharePrefenceUtil;

public class AppSelectActivity extends ActionBarActivity {

    private ListView mAppLv;
    private AppSelectAdapter mAppSelectAdapter;
    private NApplication nApplication;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        init();
    }

    private void init() {
        initLv();
    }

    private void initLv() {
        mAppLv = (ListView) findViewById(R.id.lv_main);
        nApplication = (NApplication) getApplication();
        mAppSelectAdapter = new AppSelectAdapter(AppSelectActivity.this,nApplication.getEntity().getCenter());
        mAppLv.setAdapter(mAppSelectAdapter);
        mAppSelectAdapter.notifyDataSetChanged();
        mAppLv.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mAppSelectAdapter.setSelectedPosition(position);
            mAppSelectAdapter.notifyDataSetInvalidated();
            if(!DeviceUtil.isNetworkConnected(AppSelectActivity.this)){
                dialog = new Dialog(AppSelectActivity.this, R.style.FullHeightDialog);
                dialog.setContentView(R.layout.download_progress_dialog);
            }else{
                Center app = nApplication.getEntity().getCenter().get(position);
                //SharePrefenceUtil.saveHashCode(app,AppSelectActivity.this);
                SharePrefenceUtil.putValue(AppSelectActivity.this, Constants.ISFIRSTTIMELOGON, Constants.FIRSTTIMELOGON);
                EntityUtil.setCurrentApp(AppSelectActivity.this, app);
                Intent activity=new Intent(AppSelectActivity.this,LoadingActivity.class);
                startActivity(activity);
                finish();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_select, menu);
        return true;
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
}
