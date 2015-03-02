package com.moolu.http.pack;

import android.content.Context;

import com.moolu.framework.ActivityCallback;

/**
 * Created by Nanan on 3/2/2015.
 */
public class MultipleProxyTask extends ProxyTask{
    private String taskId;
    public MultipleProxyTask(final String taskId, final Context context,
                             final ActivityCallback callback, final int ref){
        super(context,callback,ref);
        this.taskId = taskId;
    }

    public String getTaskId(){
        return taskId;
    }
}
