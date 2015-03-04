package com.moolu.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanan on 3/4/2015.
 */
public class StringRequestUtil {

    public static StringRequest getStringRequest(final int type,
                                                 final Map<String, String> map,
                                                 Response.Listener<String> listener,
                                                 Response.ErrorListener errorListener) {

        return new StringRequest(Request.Method.POST, ApiConstDef.getApiUrl(type, null)
                ,listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put(ApiConstDef.CALLBACK_MARK, String.valueOf(type));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Php-Auth-User", map.get("Php-Auth-User"));
                params.put("Php-Auth-Pw", map.get("Php-Auth-Pw"));
                params.put("X-path-token", map.get("X-path-token"));
                params.put(ApiConstDef.CALLBACK_MARK, map.get(ApiConstDef.CALLBACK_MARK));
                return params;
            }
        };
    }

    public static String getCallbackMark(String response) {
        String callbackMark = "";
        try {
            JSONObject json = new JSONObject(response);
            callbackMark = json.optString(ApiConstDef.CALLBACK_MARK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return callbackMark;
    }
}
