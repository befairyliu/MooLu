package com.moolu.hook.resolver;

import com.moolu.framework.NananLog;
import com.moolu.hook.Hook;
import com.moolu.hook.HookConstants;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by banliu on 3/2/2015.
 */
public class ResolverUI15 {
    private final static Logger Log = new NananLog(ResolverUI15.class);
    private final static Map<String, String[]> actionStrategies = new HashMap<String, String[]>();
    static{
        actionStrategies.put(HookConstants.GETTER,new String[]{"com.moolu.hook.actions.NativeDataAction","getter"});
        actionStrategies.put(HookConstants.PROXY,new String[]{"com.moolu.hook.actions.ProxyAction","proxy"});
        actionStrategies.put(HookConstants.SETTER,new String[]{"com.moolu.hook.actions.NativeDataAction","setter"});
    }
    public static String[] resolver(String url,Hook mHook){
        return Resolver.resolve(url, actionStrategies,mHook);
    }
    public static String[] resolveByFunction(final String function) {
        String[] action = ResolverUI15.actionStrategies.get(function);
        if (action!=null) {
            return  action;
        }
        return null;
    }
}
