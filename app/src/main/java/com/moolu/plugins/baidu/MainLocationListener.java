package com.moolu.plugins.baidu;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * Created by banliu on 3/4/2015.
 */
public class MainLocationListener implements BDLocationListener{
    private Context context = null;
    private Handler handler = null;
    public final static int RECEIVE_BAIDU_LOCATION = 1109;
    public MainLocationListener(Context context,Handler handler){
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if(bdLocation == null){
            return;
        }
        Message message = new Message();
        message.what = RECEIVE_BAIDU_LOCATION;
        message.obj = bdLocation;
        handler.sendMessage(message);
    }
}
