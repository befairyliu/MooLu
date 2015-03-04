package com.moolu.http.volley;

import org.apache.http.cookie.Cookie;

import java.util.Hashtable;

/**
 * Created by Nanan on 3/4/2015.
 */
public class ApiConstDef {
    public static Cookie cookie = null;
    public static String kHostUrl = "http://10.61.213.156:8080/";  //localhost_homestead 9220
    public static String ApiRootUrl=kHostUrl+"api/v1/";
    public static final int loginType = 0;

    public static final int getuiRegisterType = 1;
    public static final int getuiPushType = 2;

    public static String login = ApiRootUrl + "auth/login";
    public static String getuiRegister = ApiRootUrl + "getui/register";
    public static String getuiPush = ApiRootUrl + "getui/push";

    public static String getApiUrl (int requestType,Hashtable<String, Object> mPara) {
        String res = "";
        switch (requestType) {
            case loginType : res = login; break;
            case getuiRegisterType : res = getuiRegister; break;
            case getuiPushType : res = getuiPush; break;

            default : break;
        }
        return res;
    }

    // /////////http type define////////////////
    public static final int HTTP_ERROR_MSG = 400;
    public static final int HTTP_SUCCESS = 200;
    public final static int CID_EXPIRE_ERROR_CODE = 413;
    public final static String APIAPP = "androidphone";
    public final static String CALLBACK_MARK = "callbackmark";
}
