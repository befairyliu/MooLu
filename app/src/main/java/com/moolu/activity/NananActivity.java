package com.moolu.activity;


import android.app.Activity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.moolu.R;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class NananActivity extends Activity {

    protected RequestQueue requestQueue = null;
    private final List<AsyncTaskWithCallback<?,?,?>> mRunningTasks = new ArrayList<AsyncTaskWithCallback<?,?,?>>();
    private static LoadingDialog loadingDialog;
    public void showDialog(){
        loadingDialog = new LoadingDialog(this,"load");
        loadingDialog.show();
    }

    protected void dismissDialog(){
        loadingDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_nanan);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
    }

    protected void onDestory(){
        super.onDestroy();
        requestQueue.stop();
        requestQueue = null;
        for (AsyncTaskWithCallback<?, ?, ?> task : this.mRunningTasks) {
            task.cancel(true);
        }
    }

    public void addTask(final AsyncTaskWithCallback<?, ?, ?> task) {
        this.mRunningTasks.add(task);
    }

    public void removeTask(final AsyncTaskWithCallback<?, ?, ?> task) {
        this.mRunningTasks.remove(task);
    }
    @SuppressWarnings("rawtypes")
    public void handleCallback(final AsyncTaskWithCallback task, final int ref) {
        this.removeTask(task);
    }

    /*public void slideBottomToTop() {
        overridePendingTransition(R.anim.in_bottomtop, 0);
    }

    public void slideTopToBottom() {
        overridePendingTransition(0, R.anim.out_topbottom);
    }

    public void slideLeftToRight() {
        overridePendingTransition(R.anim.page_in_leftright, R.anim.page_out_leftright);
    }

    public void slideRightToLeft() {
        overridePendingTransition(R.anim.page_in_rightleft, R.anim.page_out_rightleft);
    }
    *//**
     * check whether support orientation change.
     * @return
     *//*
    public boolean isSupportOrientationChange() {
        return getResources().getBoolean(R.bool.isSupportOrientationChange);
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
    }*/

}
