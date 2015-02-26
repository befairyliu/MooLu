package com.moolu.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.moolu.framework.NananLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nanan on 2/26/2015.
 */
public class JsonUtil {
    public final static String DEFAULT_LOCALTE="en";
    private final static Logger Log = new NananLog(JsonUtil.class);
    private static Gson gson = null;
    public static Gson getGsonInstance() {
        synchronized (JsonUtil.class) {
            if (gson == null) {
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                gson = builder.create();
            }
            return gson;
        }
    }

    public static String getContentByLocale(HashMap<String,String> map,String locale){
        try {
            if(map!=null){
                String content=map.get(locale);
                if(content==null){
                    content=map.get(DEFAULT_LOCALTE);
                    if(content==null){
                        content="";
                    }
                }
                return content;
            }
        } catch (Exception e) {
            Log.error("getContentByLocale:{} error",locale);
        }
        return "";
    }
    public static String getFirstNodeContentByLocale(List<HashMap<String, String>> list,String locale){
        if(list!=null&&list.size()>0){
            return getContentByLocale(list.get(0),locale);
        }
        return null;
    }
    public static String getFirstJsonNodeByKey(JSONArray list,String key){
        try {
            if(list!=null&&list.length()>0){
                JSONObject object = list.getJSONObject(0);
                if(object.has(key)){
                    String str= object.getString(key);
                    if(str!=null){
                        return str;
                    }
                }
            }
        } catch (JSONException e) {
            Log.error("getFirstJsonNodeByLocale:{} error",key);
        }
        return "";
    }
    public static JSONObject getFirstJsonObjectByKey(JSONObject obj,String key){
        try {
            JSONArray list = obj.getJSONArray(key);
            if(list!=null&&list.length()>0){
                JSONObject object = list.getJSONObject(0);
                return object;
            }
        } catch (JSONException e) {
            Log.error("getFirstJsonObjectByKey:{} error",key);
        }
        return null;
    }
    /**
     * Conversion JSON to Java objects
     */
    public static <T> T getObjectFromJson(String mStr,Class<T> toJsonClass){

        try {
            return getGsonInstance().fromJson(mStr, toJsonClass);
        } catch (Exception e) {
            Log.error("Get newmessage from Json error", e);
        }

        return null;
    }

    public static <T> T getObjectFromJson(InputStream is, Class<T> toJsonClass) {
        try {
            Reader reader = new InputStreamReader(is);
            return getGsonInstance().fromJson(reader,toJsonClass);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Conversion JSON to Java objects
     */
    public static <T> T getObjectFromJsonThrowError(String mStr,Class<T> toJsonClass) throws JsonSyntaxException {
        return getGsonInstance().fromJson(mStr, toJsonClass);
    }

    /**
     * Conversion Java object to JSON
     */
    public static <T> String getJsonFromJavaObject(T src){

        try {

            return getGsonInstance().toJson(src);

        } catch (Exception e) {
            Log.error("Get Json from Java object", e);
        }

        return null;
    }

    /**
     * Create a response json
     */
    public static String getRspCallbackJson(String statusCode,HashMap<String,?> body){
        HashMap<String,HashMap<String,?>> rspMap=new HashMap<String,HashMap<String,?>>();
        HashMap<String,String> header=new HashMap<String,String>();
        header.put(JSONConstants.RESPONCE_STATUS_CODE, statusCode);
        rspMap.put(JSONConstants.RESPONCE_HEADER, header);
        if(body!=null){
            rspMap.put(JSONConstants.RESPONCE_BODY, body);
        }
        return getJsonFromJavaObject(rspMap);
    }
}
