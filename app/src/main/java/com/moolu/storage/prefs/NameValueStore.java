package com.moolu.storage.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.moolu.framework.NananLog;
import com.moolu.framework.entity.model.EntityUtil;

import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

/**
 * Created by Nanan on 2/4/2015.
 */
public class NameValueStore {
    private final static Logger Log = new NananLog(NameValueStore.class);
    private final Context context;
    private final String NAME_VALUE_STORE = "nameValueStore";
    public SharedPreferences prefs;

    public NameValueStore(Context context) {
        this.context = context;
        String appId = EntityUtil.getCurrentAppId(this.context);
        this.prefs = this.context.getSharedPreferences(NAME_VALUE_STORE+"_"+appId, Context.MODE_PRIVATE);
    }

    //TODO....
}
