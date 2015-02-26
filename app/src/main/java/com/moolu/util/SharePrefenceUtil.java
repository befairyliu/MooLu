package com.moolu.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.moolu.storage.prefs.PrefConstants;

import java.util.Map;

/**
 * Created by Nanan on 2/26/2015.
 */
public class SharePrefenceUtil {

    public static boolean putValue(Context context,String key, int value) {
        try {
            SharedPreferences.Editor sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
            sp.putInt(key, value);
            sp.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean putValue(Context context,String key, boolean value) {
        try {
            SharedPreferences.Editor sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
            sp.putBoolean(key, value);
            sp.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean putValue(Context context,String key, String value) {
        try {
            SharedPreferences.Editor sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
            sp.putString(key, value);
            sp.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int getValue(Context context,String key, int defValue) {
        SharedPreferences sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }
    public static boolean getValue(Context context,String key, boolean defValue) {
        SharedPreferences sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }
    public static String getValue(Context context,String key, String defValue) {
        SharedPreferences sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
    public static void putValues(Context context,Map<String,String> map){
        SharedPreferences.Editor sp =  context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        for(Map.Entry entry:map.entrySet()){
            sp.putString((String)entry.getKey(),(String)entry.getValue());
        }
        sp.apply();

    }
    public static void saveHashCode(Map map,Context context) {
        // Map<String,String> map=new HashMap<String,String>();
        SharePrefenceUtil.putValues(context, map);
    }
}
