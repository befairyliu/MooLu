package com.moolu.storage.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.moolu.framework.NananLog;
import com.moolu.framework.entity.EntityUtil;

import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public void setAttribute(String key, String value){
        Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getAttribute(String key){
        return prefs.getString(key,"");
    }

    public void cleanUnzipRecord(String keyPrefix){
        Editor editor = prefs.edit();
        Map<String,?> map = prefs.getAll();
        Set<String> keys = map.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()){
            String key = it.next();
            if(key.startsWith(keyPrefix)){
                Log.debug("==delete old regional config:{}",key);
                editor.remove(key);
            }
        }
        editor.commit();
    }

    public void removeUnzipRecodeByKey(String key){
        Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void saveKeyValueInAppLevel(Context context,String key, String value){
        SharedPreferences prefs = context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getSavedValueInAppLevel(Context context,String key){
        SharedPreferences prefs = context.getSharedPreferences(PrefConstants.PREFS_NAME,Context.MODE_PRIVATE);
        return prefs.getString(key,null);
    }
}
