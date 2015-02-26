package com.moolu.framework.entity;

import android.content.Context;

import com.moolu.framework.ActivityCallback;
import com.moolu.framework.AsyncTaskWithCallback;
import com.moolu.framework.NananLog;
import com.moolu.framework.entity.model.Entity;

/**
 * Created by Nanan on 2/26/2015.
 */
public class EntityTask extends AsyncTaskWithCallback<Void,Void,Entity>{

    private final static NananLog Log = new NananLog(EntityTask.class);
    private final Context context;

    public EntityTask(final Context context,final ActivityCallback callback, final int ref){
        super(callback,ref);
        if(context == null){
            throw new IllegalArgumentException("owner must not be null");
        }
        this.context = context;
    }
    @Override
    protected Entity doInBackground(Void... params) {
        EntityUtil entityUtil = new EntityUtil(this.context);
        Entity entity = entityUtil.initEntity();
        return entity;
    }
}
