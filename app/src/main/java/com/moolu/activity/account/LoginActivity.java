package com.moolu.activity.account;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moolu.R;
import com.moolu.activity.NananActivity;
import com.moolu.http.volley.ApiConstDef;
import com.moolu.http.volley.StringRequestUtil;
import com.moolu.json.jackson.JsonUtil;
import com.moolu.json.jackson.ResponseDo;
import com.moolu.model.User;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends NananActivity implements View.OnClickListener {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!Thread.currentThread().isInterrupted()){
                switch (msg.what){
                    //to do something.....
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    /**
     * Listener and errorListener callback for HTTP request with volley.
     */
    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            String callbackMark = StringRequestUtil.getCallbackMark(response);
            String json = response.toString();
            try {
                ResponseDo<User> responseDo = JsonUtil.getResult(json, User.class);
                int i = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void doLogin() {
        final String username = ((EditText)findViewById(R.id.et_email)).getText().toString();
        final String password = ((EditText)findViewById(R.id.et_password)).getText().toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("Php-Auth-User", username);
        map.put("Php-Auth-Pw", password);
        requestQueue.add(StringRequestUtil.getStringRequest(ApiConstDef.loginType,map,listener,errorListener));
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
    protected void onDestory() {
        super.onDestory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                doLogin();
                break;
        }
    }
}
