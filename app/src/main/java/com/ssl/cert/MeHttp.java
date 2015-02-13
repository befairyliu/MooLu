package com.ssl.cert;

import android.content.Context;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;


/**
 * Created by Nanan on 2/10/2015.
 */
public class MeHttp {
    private static final int MAX_CONN_PER_ROUTE = 10;
    private static final int MAX_CONNECTIONS = 20;
    private static final int TIMEOUT = 30*1000;

    public static HttpClient getClient(){
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params,true);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params,schReg);
        return new DefaultHttpClient(connMgr,params);

    }

    public static HttpClient getHttpsClient(){
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params,true);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params,schReg);
        return new DefaultHttpClient(connMgr,params);
    }

    public static HttpClient getTrustAllClient(){
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params,true);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        schReg.register(new Scheme("https", SSLTrustAllSocketFactory.getSocketFactory(),443));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params,schReg);
        return new DefaultHttpClient(connMgr,params);
    }

    public static HttpClient getCustomClient(Context context){
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params,true);

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        schReg.register(new Scheme("https", SSLCustomSocketFactory.getSocketFactory(context),443));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params,schReg);
        return new DefaultHttpClient(connMgr,params);
    }
}
