package com.moolu.util;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Nanan on 2/26/2015.
 */
public class StringUtil {

    public static Boolean IsNullOrEmpty(String pStr){
        return pStr == null || pStr.trim().equals("") || pStr.trim().length() <= 0;

    }

    public static boolean isNotNullAndEmpty(String str){
        return !IsNullOrEmpty(str);
    }

    public static int StringToInt(String pStr){
        return Integer.valueOf(pStr.replaceAll("[^0-9.]", ""));

    }
    public static long string2Long(String size){
        return Long.parseLong(size);
    }
    public static StringBuffer inputStream2StringBuffer(InputStream is) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for(int n;(n = is.read(b))!= -1;){
            out.append(new String(b,0,n));
        }
        return out;
    }

    //when http use "GET" method request,we need put Json String into name1=value1&name2=value2
    public static String getParaFromJson(String str){
        try{
            if(str==null){
                return "";
            }
            StringBuffer paraBuf = new StringBuffer();
            JSONObject jobject = new JSONObject(str);
            if(jobject!=null){
                Iterator<String> e =jobject.keys();
                int i=0;
                while(e.hasNext()){
                    Object name=e.next();
                    Object value="";
                    if(name!=null&&name instanceof String){
                        if(i>0){
                            paraBuf.append("&");
                        }
                        value = jobject.get((String)name);
                        if(value!=null&&value instanceof String){
                            paraBuf.append(URLEncoder.encode((String) name, HTTP.UTF_8))
                                    .append("=")
                                    .append(URLEncoder.encode((String)value,HTTP.UTF_8));
                        }
                    }
                    i++;
                }
                return paraBuf.toString();
            }
        }catch(Exception e){
        }
        return "";
    }
}

