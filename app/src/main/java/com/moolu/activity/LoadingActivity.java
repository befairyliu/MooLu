package com.moolu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.moolu.R;
import com.moolu.framework.ActivityCallback;
import com.moolu.framework.NananLog;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class LoadingActivity extends NananActivity implements ActivityCallback{

    private final static Logger Log = new NananLog(LoadingActivity.class);
    public static final int ENTITY_TASK_REF = 1;
    public static final int APPUPDATE_TASK_REF = 2;
    private static final int ACTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
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

    //TODO...
}
