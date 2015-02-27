package com.moolu.hook;

import com.moolu.hook.actions.GetLocaleAction;
import com.moolu.hook.actions.MooClu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanan on 2/27/2015.
 */
public class MooURLResolver {

    //holds the available action strategies
    private static class StrategiesHolder{}

    private final static Map<String, MooURLAction> actionStrategies = new HashMap<String,MooURLAction>();
    static {
        MooURLResolver.actionStrategies.put(HookConstants.GET_LOCALE,new GetLocaleAction());
        MooURLResolver.actionStrategies.put(HookConstants.TurboEngine,new MooClu());
    }

    public static MooURLAction resolve(final String url){
        return resolve(url);
    }

    public static IMooUrlAction resolveByFunction(final String function){
        MooURLAction action = MooURLResolver.actionStrategies.get(function);
        if(action instanceof IMooUrlAction){
            return (IMooUrlAction)action;
        }
        return null;
    }

    public static MooURLAction resolve(final String url, Map<String,MooURLAction> map){
        if( !url.startsWith(HookConstants.ML_URL_PREFIX)){
            throw new IllegalArgumentException(String.format("%s is not a valid MooLu URL",url));
        }

        //parse params
        String paramsString = url.substring(HookConstants.ML_URL_PREFIX.length());
        Map<String, String> params = processParams(paramsString);
        if(params != null){
            String function = params.get(HookConstants.FUNCTION);
            MooURLAction action = map.get(function);
            if(action == null){
                return null;
            }
            action.setParams(params);
            return action;
        } else {
            return null;
        }
    }

    /**
     * Extracts the key value pairs from the list of params. It must at least contain the param "function"
     *
     * @param paramsString
     * @return A list populated with params, or a empty list if there are no params. In the case
     * an error occurs, <code>null</code> rather than an empty list will be returned.
     */
    public static Map<String,String> processParams(final String paramsString){
        String[] paramsKVPairs = paramsString.split("&");
        Map<String, String> params = new HashMap<String, String>(paramsKVPairs.length);
        for (String keyValuePair : paramsKVPairs) {
            if(keyValuePair !=null && keyValuePair.indexOf("=") != -1){
                try {
                    String[] kv = keyValuePair.split("=");
                    String key = null;
                    String value = "";

                    if(kv.length>1){
                        value=kv[1];
                    }
                    if(kv.length>0){
                        key=kv[0];
                        params.put(key, URLDecoder.decode(value, "UTF-8"));
                    }
                }catch (UnsupportedEncodingException e) {
                    // Unable to parse the params, possibly incorrectly encoded, set params to null.
                    params = null;
                }
            }
        }
        if (params != null && !params.containsKey(HookConstants.FUNCTION)) {
            // This means the URL is invalid, return null;
            params = null;
        }
        return params;
    }
}
