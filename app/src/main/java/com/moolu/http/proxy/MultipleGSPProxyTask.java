package com.moolu.http.proxy;

import android.content.Context;

import com.moolu.framework.ActivityCallback;

/**
 * Created by Nanan on 3/2/2015.
 */
public class MultipleGSPProxyTask extends GspProxyTask{
    private String taskId;

    public MultipleGSPProxyTask(final String taskId, final Context context, final ActivityCallback callback, final int ref) {
        super(context, callback, ref);
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
