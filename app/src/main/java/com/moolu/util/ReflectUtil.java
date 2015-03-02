package com.moolu.util;

import android.content.Context;
import android.webkit.WebView;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.actions.MooLuAction;

import org.json.JSONObject;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanan on 3/2/2015.
 */
public class ReflectUtil {
    private final static Logger Log = new NananLog(ReflectUtil.class);
    private final static Map<String, Object> instanceMap = new HashMap<String, Object>();
    public static  void invoke(String[] action,Context context,Hook hook,
                               WebView webview,JSONObject jo,String dataValue){
        Class<?> clazz = null;
        try {
            clazz = Class.forName(action[0]);
            Method method = clazz.getDeclaredMethod(action[1], Context.class, Hook.class,
                    WebView.class,JSONObject.class,String.class);
            method.setAccessible(true);
            Object instance = getInstance(clazz, action[0]);
            instanceMap.put(action[0],instance);
            method.invoke(instance,context,hook,webview,jo,dataValue);
        }catch (Exception e){
            Log.debug("ReflectUtil", "invoke error");
        }
    }
    public static  void invoke(String[] action,Context context,Hook hook,WebView webview){
        Class<?> clazz=null;
        try {
            clazz = Class.forName(action[0]);
            Method method = clazz.getDeclaredMethod(action[1], Context.class, Hook.class, WebView.class);
            method.setAccessible(true);
            Object instance = getInstance(clazz, action[0]);
            instanceMap.put(action[0],instance);
            method.invoke(instance,context,hook,webview);
        }catch (Exception e){
            Log.debug("ReflectUtil", "invoke error");
        }
    }
    private static Object getInstance(Class<?> clazz,String name){
        Object instance=null;
        if(instanceMap.get(name)==null){
            synchronized (ReflectUtil.class){
                if(instanceMap.get(name)==null){
                    try {
                        instance=clazz.newInstance();
                        instanceMap.put(name,instance);
                        return instance;
                    }catch (Exception e){
                        Log.debug("ReflectUtil", "instance error");
                    }
                }
            }
        }
        return  instanceMap.get(name);

    }
    public static MooLuAction getAction(String[] action){
        Class<?> clazz=null;
        try {
            clazz = Class.forName(action[0]);
            return (MooLuAction)getInstance(clazz, action[0]);
        }catch (Exception e){
            Log.debug("ReflectUtil", "invoke error");
            return null;
        }
    }
}
