package com.moolu.http.pack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.moolu.application.NApplication;
import com.moolu.framework.Constants;
import com.moolu.framework.NananLog;
import com.moolu.hook.HookConstants;
import com.moolu.http.fileupload.task.MooLuMultipartEntity;
import com.moolu.http.proxy.GSPProxyResponse;
import com.moolu.json.gson.JSONConstants;
import com.moolu.util.IOUtils;
import com.moolu.util.StringUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Nanan on 3/2/2015.
 */
public class MooLuHttpClient {
    private final static Logger Log = new NananLog(MooLuHttpClient.class);
    private final static int CONN_TIMEOUT = 60000;//in milsec default is 30s
    private HttpContext localContext = null;
    private DefaultHttpClient client = null;
    private BasicCookieStore cookieStore = null;

    //private final static String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.1 Desire;) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2";
    private static MooLuHttpClient myClient = null;
    private  boolean allowAllSSL = false;
    private  String cookiePolicy = CookiePolicy.NETSCAPE;
    public Context context;

    private MooLuHttpClient(Context context){
        try{
            this.context = context.getApplicationContext();
            NApplication application = (NApplication)this.context;
            allowAllSSL = application.isAllowAllSSL();
            String cookiePolicy = application.getCookiePolicy();
            this.cookiePolicy = getCookiePolicy(cookiePolicy);
            Log.debug("HttpClient instance created");
        } catch (Exception e){
            Log.error("HttpClient initial error",e);
        }

        try{
            initLocalContext(context);
        } catch (Exception e){
            Log.error("initLocalContext error");
        }
    }

    private String getCookiePolicy(String policy){
        String defualtCookiePolicy = CookiePolicy.NETSCAPE;
        if(policy == null){
            return defualtCookiePolicy;
        }
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(CookiePolicy.BEST_MATCH,"");
        map.put(CookiePolicy.BROWSER_COMPATIBILITY,"");
        map.put(CookiePolicy.NETSCAPE,"");
        map.put(CookiePolicy.RFC_2109,"");
        map.put(CookiePolicy.RFC_2965,"");
        if(map.get(policy) != null){
            return policy;
        } else {
            return defualtCookiePolicy;
        }
    }

    /**
     * The synchronous of "Synchronized" qualifier need more time-consuming
     */
    public static MooLuHttpClient getInstance(Context context) {
        synchronized (MooLuHttpClient.class) {
            if (myClient == null) {
                myClient = new MooLuHttpClient(context);
            }
            return myClient;
        }
    }

    private  DefaultHttpClient getDefaultHttpClient(final Context context){
        DefaultHttpClient httpClient = new DefaultHttpClient(){
            @Override
            protected ClientConnectionManager createClientConnectionManager() {
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                if(MooLuHttpClient.this.allowAllSSL){
                    registry.register(new Scheme("https", getInSecuSocketFactory(), 443));
                }else{
                    registry.register(new Scheme("https", getHttpsSocketFactory(), 443));
                }
                HttpParams params = getParams();
                HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
                HttpConnectionParams.setSoTimeout(params, CONN_TIMEOUT);
                HttpProtocolParams.setUserAgent(params, getUserAgent(context, HttpProtocolParams.getUserAgent(params)));
                return new ThreadSafeClientConnManager(params, registry);
            }

            /** Gets an HTTPS socket factory with SSL Session Caching if such support is available, otherwise falls back to a non-caching factory
             * @return
             */
            protected SocketFactory getHttpsSocketFactory(){
                return SSLSocketFactory.getSocketFactory();
            }
            protected SocketFactory getInSecuSocketFactory(){
                try{
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, "".toCharArray());
                    SSLSocketFactory sf = new MooLuSSLSocketFactory(trustStore);
                    sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                    return sf;
                }catch(Exception e){
                    Log.error("getInSecuSocketFactory error");
                    return SSLSocketFactory.getSocketFactory();
                }
            }
        };
        return httpClient;
    }

    private void setCredentials(String username, String password, URI uri) {
        Credentials creds = new UsernamePasswordCredentials(username, password);
        client.getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), uri.getPort()), creds);
    }

    private void initLocalContext(Context context){
        client = getDefaultHttpClient(context);
        HttpParams params = client.getParams();
        params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,true);
        setRequestInterceptor();
        //setProxyInfo(client);

        //Create a local instance of cookie store
        cookieStore = new BasicCookieStore(){
            @Override
            public synchronized void addCookie(Cookie cookie) {
                if(cookie != null && cookie.getExpiryDate()!= null){
                    BasicClientCookie newCookie = (BasicClientCookie)cookie;
                    String[] datePattern = {"d MMM yy HH:mm:ss z"};
                    for(int i=0;i<datePattern.length;i++){
                        try{
                            SimpleDateFormat df = new SimpleDateFormat(datePattern[i], Locale.US);
                            Date ed=df.parse(newCookie.getExpiryDate().toGMTString());
                            newCookie.setExpiryDate(ed);
                            //LOG.debug("exipird:"+cookie.getExpiryDate().getTime());
                            break;
                        }catch(Exception e){
                            Log.error("Transform the cookie expire time error",e);
                        }
                    }
                    super.addCookie(newCookie);
                } else {
                    super.addCookie(cookie);
                }
                /**Sync the original cookie to webview***/
                importCookie2Webview(cookie);
            }
        };

        //Create local HTTP context
        localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpConnectionParams.setConnectionTimeout(params, MooLuHttpClient.CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, MooLuHttpClient.CONN_TIMEOUT);
        HttpClientParams.setCookiePolicy(params, this.cookiePolicy);// must set it
    }

    public  void setRequestInterceptor() {
        HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
                CredentialsProvider credsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authState.getAuthScheme() == null) {
                    AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
                    Credentials creds = credsProvider.getCredentials(authScope);
                    if (creds != null) {
                        authState.setAuthScheme(new BasicScheme());
                        authState.setCredentials(creds);
                    }
                }
            }
        };
        client.addRequestInterceptor(preemptiveAuth, 0);
    }

    public StringBuffer connectURL(String url, String cookieStr) throws ClientProtocolException,IOException{
        HttpResponse response = getHttpResponse(url, cookieStr);
        if (response != null) {
            return getResponseStr(response);
        } else {
            return null;
        }
    }

    public StringBuffer connectURL(String url, String cookieStr,
                                   JSONObject jsonHeaders) throws ClientProtocolException,IOException{
        HttpResponse response = getHttpResponse(url, cookieStr, jsonHeaders);
        if (response != null) {
            return getResponseStr(response);
        } else {
            return null;
        }
    }

    public HttpResponse getHttpResponse(String url, String cookieStr,
                                        JSONObject jsonHeaders) throws ClientProtocolException,IOException{
        return getHttpResponse(url,cookieStr,jsonHeaders,true);
    }
    public HttpResponse getHttpResponse(String url, String cookieStr,JSONObject jsonHeaders,
                                        boolean setHeader) throws ClientProtocolException,IOException{
        if (cookieStr != null) {
            setCookie(url, cookieStr);
        }
        HttpUriRequest request = createRequest(url);
        if(request==null){
            return null;
        }
        /** Set default header for every request***/
        boolean addDeviceStatus = getDeviceStatusFlag(url);
        if(setHeader){
            this.setDefaultHeader(this.context, request,addDeviceStatus);
        }
        traverseGspRequestHeaderPara(jsonHeaders, request);
        HttpResponse response = executeRequest(request);
        return response;
    }

    public HttpResponse getHttpResponse(String url, String cookieStr)
            throws ClientProtocolException,IOException{
        return getHttpResponse(url,cookieStr,true);
    }
    public HttpResponse getHttpResponse(String url, String cookieStr,
                                        boolean setHeader) throws ClientProtocolException,IOException{
        if (cookieStr != null) {
            setCookie(url, cookieStr);
        }
        HttpUriRequest request = createRequest(url);
        if(request==null){
            return null;
        }
        /** Set default header for every request***/
        boolean addDeviceStatus=getDeviceStatusFlag(url);
        if(setHeader){
            this.setDefaultHeader(this.context, request,addDeviceStatus);
        }
        HttpResponse response = executeRequest(request);
        return response;
    }

    public StringBuffer connectURLByPost(String url, String cookieStr,
                                         List<NameValuePair> params)throws ClientProtocolException,IOException {
        setCookie(url, cookieStr);
        HttpUriRequest request = createPostRequest(url, params);
        /** Set default header for every request***/
        boolean addDeviceStatus=getDeviceStatusFlag(url);
        this.setDefaultHeader(this.context, request,addDeviceStatus);
        HttpResponse response = executeRequest(request);
        if (response != null) {
            return getResponseStr(response);
        } else {
            return null;
        }
    }

    public StringBuffer connectURLByPost(String url, String cookieStr, List<NameValuePair> params,
                                         JSONObject gspHeaders)throws ClientProtocolException,IOException {
        setCookie(url, cookieStr);
        HttpUriRequest request = createPostRequest(url, params);
        /** Set default header for every request***/
        boolean addDeviceStatus=getDeviceStatusFlag(url);
        this.setDefaultHeader(this.context, request,addDeviceStatus);
        traverseGspRequestHeaderPara(gspHeaders, request);
        HttpResponse response = executeRequest(request);
        if (response != null) {
            return getResponseStr(response);
        } else {
            return null;
        }
    }

    private void doPostOrPut(JSONObject jsonObjSend,HttpEntityEnclosingRequestBase request,
                             JSONObject gspHeaders,String url){
        StringEntity input;
        try {
            input = new StringEntity(jsonObjSend.toString(), HTTP.UTF_8);
            request.setEntity(input);
            request.setHeader("Content-type", Constants.CONTENT_TYPE);
            boolean addDeviceStatus = getDeviceStatusFlag(url);
            /** Set default header for every request***/
            this.setDefaultHeader(this.context, request,addDeviceStatus);
            traverseGspRequestHeaderPara(gspHeaders, request);
        } catch (UnsupportedEncodingException e) {
            Log.error("doPostOrPut error",e);
        }
    }

    private GSPProxyResponse getGspProxyResponse(HttpResponse response) throws UnsupportedEncodingException, IOException{
        GSPProxyResponse proxyRsp=new GSPProxyResponse();
        if (response != null) {
            proxyRsp.setResponseStr(streamToString(response));
            int httpStatusCode = response.getStatusLine().getStatusCode();
            proxyRsp.setHttpStatusCode(httpStatusCode);
            return proxyRsp;
        } else {
            return null;
        }
    }
    /**
     * Refactor and merge HttpGet and HttpDelete request
     */
    private void doGetOrDelete(HttpUriRequest request,JSONObject gspHeaders,String url){
        request.setHeader("Content-type", Constants.CONTENT_TYPE);
        boolean addDeviceStatus=getDeviceStatusFlag(url);
        /** Set default header for every request***/
        this.setDefaultHeader(this.context, request,addDeviceStatus);
        traverseGspRequestHeaderPara(gspHeaders,request);
    }
    /**
     * handle and traverse the custom request header
     */
    private void traverseGspRequestHeaderPara(JSONObject gspHeaders,HttpUriRequest request){
        if(gspHeaders!=null){
            try {
                //sagiller modify [sagiller]
                //Iterator<Object> e = gspHeaders.keys();
                Iterator<String> e = gspHeaders.keys();
                while(e.hasNext()){
                    Object name = e.next();
                    Object value = "";
                    if(name!=null&&name instanceof String){
                        value = gspHeaders.get((String) name);
                        if(value!=null &&value instanceof String){
                            request.addHeader((String)name, (String)value);
                        }
                    }
                }
            } catch (JSONException e) {
                Log.error("traverseGspRequestHeaderPara error",e);
            }
        }
    }

    /**
     * GSPProxy DELETE and GET request
     */
    public GSPProxyResponse GSPConnectURLByGetOrDelete(String url, String method ,String cookieStr,
                                                       JSONObject gspHeaders)throws ClientProtocolException,IOException{
        HttpResponse response = getGSPGetOrDeleteResponse(url , method,cookieStr ,gspHeaders);
        return getGspProxyResponse(response);
    }
    /**
     * merge method when GET And DELETE request
     */
    public HttpResponse getGSPGetOrDeleteResponse(String url, String method ,
                                                  String cookieStr,JSONObject gspHeaders){

        HttpUriRequest request = null;
        if (cookieStr != null) {
            setCookie(url, cookieStr);
        }
        if(!StringUtil.IsNullOrEmpty(method)&&method.toUpperCase().equals(Constants.REQUEST_GET)){
            HttpGet getRequest = new HttpGet(url);
            request = getRequest;
        } else if(!StringUtil.IsNullOrEmpty(method)&&method.toUpperCase().equals(Constants.REQUEST_DELETE)){
            HttpDelete deleteRequest = new HttpDelete (url);
            request = deleteRequest;
        }

        supportSplashPassword(url);
        HttpResponse response = null;
        try {
            doGetOrDelete(request,gspHeaders,url);
            Log.debug("send delete request:"+url);
            response = client.execute(request, localContext);
        } catch (Exception e) {
            Log.error("Create post Gsp Proxy request fail!", e);
        }
        return response;
    }
    /**
     * GSPProxy POST and PUT request use this method
     */
    public GSPProxyResponse GSPConnectURLByPostOrPut(String url,String method,
                                                     String cookieStr, JSONObject jsonObjSend,
                                                     JSONObject gspHeaders)throws ClientProtocolException,IOException{
        setCookie(url, cookieStr);
        HttpResponse response = getGSPHTTPPostOrPutResponse(url , method,jsonObjSend ,gspHeaders);
        return getGspProxyResponse(response);
    }
    /**
     * changing the Content-type to application/json of the http post request and sending out
     * and merge POST and PUT method request to optimize the source code.
     */
    public HttpResponse getGSPHTTPPostOrPutResponse (String url, String method,JSONObject jsonObjSend,JSONObject gspHeaders){

        HttpEntityEnclosingRequestBase request = null;
        if(!StringUtil.IsNullOrEmpty(method)&&method.toUpperCase().equals(Constants.REQUEST_POST)){
            HttpPost postRequest = new HttpPost(url);
            request = postRequest;
        } else if(!StringUtil.IsNullOrEmpty(method)&&method.toUpperCase().equals(Constants.REQUEST_PUT)){
            HttpPut putRequest = new HttpPut(url);
            request = putRequest;
        }
        supportSplashPassword(url);
        HttpResponse response = null;
        try {
            doPostOrPut(jsonObjSend,request,gspHeaders,url);
            Log.debug("send post or put request:"+url);
            response = client.execute(request, localContext);
        } catch (Exception e) {
            Log.error("Create post or put Gsp Proxy request fail!", e);
        }
        return response;
    }
    /**
     * To support splash password for post|put|get|delete method by GSPProxy
     */
    private void supportSplashPassword(String url){
        try {
            URI uri = new URI(url);
            setHttpsCredentials(uri);
            Log.debug("send request:"+url);
        } catch (URISyntaxException e) {
            Log.error("Create request",e);
        }
    }
    public HttpResponse getHttpUploadResponse(String url,boolean setHeader,
                                              String filePath,String newFileName,List<NameValuePair> params,
                                              MooLuMultipartEntity multipartContent) throws ClientProtocolException,IOException{
        HttpUriRequest request = this.createFilePostRequest(url, filePath, newFileName, params,multipartContent);
        if(request==null){
            return null;
        }
        /** Set default header for every request***/
        //Added addDeviceStatus by TW [Aug-2013] for end point security
        boolean addDeviceStatus = getDeviceStatusFlag(url);
        if(setHeader){
            this.setDefaultHeader(this.context, request,addDeviceStatus);
        }
        HttpResponse response = executeRequest(request);
        return response;
    }

    /**
     * Check if there is a "devicestatus" parameter after the url . This is for end point security checking .
     * If no such a parameter, it will return false.
     */
    private boolean getDeviceStatusFlag(String url){
        try{
            int position=url.indexOf("?");
            if(position<0||position==url.length()){
                return false;
            }
            String paramString =url.substring(position+1);
            String[] params=paramString.split("&");
            for (String param : params) {
                if (param != null) {
                    int pos=param.indexOf("=");
                    if(pos!=-1){
                        String key=param.substring(0,pos);
                        String value=param.substring(pos+1);
                        if (HookConstants.DEVICE_STATUS.equalsIgnoreCase(key)) {
                            return Boolean.valueOf(value);
                        }
                    }
                }
            }
        }catch (Exception e) {
            Log.error("getDeviceStatusFlag error",e);
        }
        return false;
    }
    /**
     * The addDeviceStatus parameter, if you want to put device security status in header, pass true.
     */
    public void setDefaultHeader(Context context,HttpUriRequest request,boolean addDeviceStatus){
        if(request==null){
            return;
        }
        Header header=new Header();
        Map<String, String> defaultHeaders=header.createHeaders(context,addDeviceStatus);
        Iterator<String> e = defaultHeaders.keySet().iterator();
        while(e.hasNext()){
            String key=e.next();
            request.addHeader(key, defaultHeaders.get(key));
        }
    }
    public static List<NameValuePair> parseURL(String url) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (url == null) {
            return list;
        }
        try {
            String[] params = url.split("&");
            for (int i = 0; i < params.length; i++) {
                String s = params[i];
                if (s != null) {
                    String key = null;
                    String value = "";
                    int pos=s.indexOf("=");
                    if(pos!=-1){
                        key= URLDecoder.decode(s.substring(0, pos), HTTP.UTF_8);
                        value=URLDecoder.decode(s.substring(pos+1),HTTP.UTF_8);
                    }
                    if (key != null) {
                        NameValuePair nv = new BasicNameValuePair(key, value);
                        list.add(nv);
                    }
                }

            }
        } catch (Exception e) {
            Log.error("parse the URL parameter error", e);
        }
        return list;
    }

    private void setCookie(String url, String cookieStr) {
        List<BasicClientCookie> list = createListByCookieStr(cookieStr);
        for (int i = 0; i < list.size(); i++) {
            try{
                BasicClientCookie cookie = list.get(i);
                String domain = getDomain(url);
                //LOG.debug("domain=" + domain);
                cookie.setDomain(domain);
                cookie.setPath("/");
                cookieStore.addCookie(cookie);
            }catch(Exception e){
                Log.error("set cookie error!",e);
            }
        }
    }

    private String getDomain(String url) {
        if (url.indexOf("//") != -1) {
            String host = url.substring(url.indexOf("//") + 2);
            if (host.indexOf("/") != -1) {
                host = host.substring(0, host.indexOf("/"));
            }
            return host;
        } else {
            return url;
        }
    }

    public InputStream getResponseInputStream(final HttpResponse response) throws IllegalStateException,IOException{
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        return is;

    }

    public StringBuffer getResponseStr(final HttpResponse response)throws IllegalStateException,IOException {
        StringBuffer sf = new StringBuffer();
        InputStream is = getResponseInputStream(response);
        BufferedReader br = null;
        InputStreamReader ireader=null;
        if (is == null) {
            return null;
        }
        try {
            if(response.getStatusLine().getStatusCode()<400){
                ireader=new InputStreamReader(is, HTTP.UTF_8);
                br = new BufferedReader(ireader);
                String str = "";
                while ((str = br.readLine()) != null) {
                    sf.append(str);
                }
                return sf;
            }else{
                Log.error("Http status code:{},{}",response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
            }
        } catch(OutOfMemoryError memError){
            Log.debug("OutOfMemoryError in getResponseStr");
        }finally {
            IOUtils.close(is);//Housekeeping - release the resource in the finally block
            if(ireader!=null){
                ireader.close();
            }
            if (br != null) {
                //Housekeeping - release the resource in the finally block
                br.close();
            }
        }
        return null;
    }

    /**
     * GSPProxy use this method to convert the HttpResponse to json String
     */
    public  String streamToString(final HttpResponse response) throws UnsupportedEncodingException, IOException {
        StringBuilder sb = new StringBuilder();

        InputStream is = null;
        BufferedReader br = null;

        try{
            is = getResponseInputStream(response);
            br = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch(OutOfMemoryError memEorror){
            Log.error("OutOfMemoryError in getResponseStr",memEorror);
        }

        IOUtils.close(is);
        if (br != null){
            br.close();
        }
        return sb.toString();
    }

    public List<BasicClientCookie> createListByCookieStr(String cookieStr) {
        List<BasicClientCookie> list = new ArrayList<BasicClientCookie>();
        try{
            if (cookieStr != null) {
                String[] cookies = cookieStr.split(";");
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        String cookiePair = cookies[i];
                        if (cookiePair != null && cookiePair.indexOf("=") != -1) {
                            //String[] co = cookiePair.split("=");
                            int pos=cookiePair.indexOf("=");
                            String cookieName = null;
                            String cookieValue = "";
                            if(pos!=-1){
                                cookieName=cookiePair.substring(0,pos);
                                cookieValue=cookiePair.substring(pos+1);
                            }
                            if (cookieName != null) {
                                BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
                                list.add(cookie);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            Log.error("parse cookie string error!",e);
        }
        return list;
    }

    private HttpUriRequest createRequest(String url) {
        HttpGet request = new HttpGet();
        try {
            URI uri = new URI(url);
            setHttpsCredentials(uri);
            request.setURI(uri);
        } catch (URISyntaxException e) {
            Log.error("Create request",e);
            return null;
        }
        return request;
    }

    private void setHttpsCredentials(URI uri) {
        NameValuePair pair = getHttpsUserInfo(uri);
        if (pair != null && pair.getName() != null && pair.getValue() != null) {
            setCredentials(pair.getName(), pair.getValue(),uri);
        }
    }

    private NameValuePair getUserInfo(URI uri) {
        NameValuePair pair = null;
        String userInfo = uri.getUserInfo();
        if (userInfo != null && userInfo.indexOf(":") != -1) {
            String[] info = userInfo.split(":");
            String username = info[0];
            String password = info[1];
            pair = new BasicNameValuePair(username, password);
        }
        return pair;
    }

    private NameValuePair getHttpsUserInfo(URI uri) {
        if ("https".equals(uri.getScheme()) || "http".equals(uri.getScheme())) {
            return getUserInfo(uri);
        } else {
            return null;
        }
    }

    private HttpUriRequest createPostRequest(String url, List<NameValuePair> params) {
        HttpPost request = new HttpPost(url);
        try {
            //To support splash password for post method proxy
            URI uri = new URI(url);
            setHttpsCredentials(uri);
            if(params!=null){
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
        } catch (Exception e) {
            Log.error("Create post request fail!", e);
        }
        return request;
    }
    private HttpUriRequest createFilePostRequest(String url,String filePath,String newFileName,
                                                 List<NameValuePair> params,MooLuMultipartEntity multipartEntity) {
        HttpPost request = new HttpPost(url);
        //MultipartEntity=new MultipartEntity();
        try {
            /*for (NameValuePair pair : params) {
                StringBody value = new StringBody(pair.getValue());
                multipartEntity.addPart(pair.getName(), value);
            }
            if (filePath != null && !filePath.equals("")) {
                FileBody file = new FileBody(new File(filePath),newFileName,"image","UTF-8");
                multipartEntity.addPart("photo", file);
            }
            multipartEntity.totalSize = multipartEntity.getContentLength();
            request.setEntity(multipartEntity);*/
        } catch (Exception e) {
            Log.error("Create post request fail!", e);
        }
        return request;
    }

    private HttpResponse executeRequest(final HttpUriRequest request)throws ClientProtocolException,IOException {

        HttpResponse response = null;
        response = client.execute(request, localContext);
        int statusCode = response.getStatusLine().getStatusCode();
        //LOG.debug("httpCode:" + statusCode);
        return response;
    }
    /**
     * clear all cookie in httpclient
     */
    public void clearAllCookie(){
        cookieStore.clear();
    }
    public void getAllCookie(){
        Log.debug("cookie:"+cookieStore.getCookies());
    }

    public String getCookie(String name,String domain){
        if(name==null||domain==null){
            return "";
        }
        List<Cookie> cookieList = cookieStore.getCookies();
        for(int i=0;i<cookieList.size();i++){
            Cookie cookie = cookieList.get(i);
            if(name.equals(cookie.getName())&&domain.equals(cookie.getDomain())){
                return cookie.getValue();
            }
        }
        return "";
    }
    public void setCookie(String domain,String name,String value){
        //LOG.debug("set cookie from client domain:"+domain+"name:"+name+ "="+value);
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
    }
    public void setTimeOut(int sec){
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, sec*1000);
        HttpConnectionParams.setSoTimeout(params, sec*1000);
    }
    public void setCookiePolicy(String policy){
        HttpParams params = client.getParams();
        String cookiePolicy = getCookiePolicy(policy);
        HttpClientParams.setCookiePolicy(params, cookiePolicy);
        //LOG.debug("cookiePolicy change to:"+cookiePolicy);
    }

    private String getUserAgent(Context context,String defaultHttpClientUserAgent){
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder ret = new StringBuilder();
        ret.append(context.getPackageName());
        ret.append("/");
        ret.append(versionName);
        ret.append(" (");
        ret.append("Linux; U; Android ");
        ret.append(Build.VERSION.RELEASE);
        ret.append("; ");
        ret.append(Locale.getDefault());
        ret.append("; ");
        ret.append(Build.PRODUCT);
        ret.append(")");
        if(defaultHttpClientUserAgent!=null){
            ret.append(" ");
            ret.append(defaultHttpClientUserAgent);
        }
        return ret.toString();
    }

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * sync cookie in the httpclient to webview by generating cookie string
     */
    public  void importCookie2Webview(Cookie cookie){
        if (cookie!=null){
            //Log.debug("=====httpclient add cookie:{}=========",cookie.getName());
            CookieSyncManager.createInstance(this.context);
            CookieManager cookieManager = CookieManager.getInstance();
            StringBuffer cookieStrBuf =new StringBuffer();
            cookieStrBuf.append(cookie.getName()).append("=").append(cookie.getValue());
            cookieStrBuf.append("; ").append(Constants.DOMAIN).append("=").append(cookie.getDomain());
            if(cookie.getExpiryDate()!=null){
                cookieStrBuf.append("; ").append(Constants.EXPIRES).append("=").append(cookie.getExpiryDate().toGMTString());
            }
            cookieManager.setCookie(cookie.getDomain(), cookieStrBuf.toString());
            CookieSyncManager.getInstance().sync();
        }
    }

    //Clear Cookies:According to the name to remove
    public void clearCookiesByWhiteList(ArrayList<HashMap<String, String>> mWhiteLists) {
        List<Cookie> cookieList = cookieStore.getCookies();
        List<Cookie> listRestore = new ArrayList<Cookie>();
        Log.debug("clean some cookie before:"+cookieStore.getCookies());
        int size = cookieList.size();
        if(cookieList!=null && size>0){
            for(int i=0;i<size;i++){
                Cookie cookie = cookieList.get(i);
                if(mWhiteLists!=null&&mWhiteLists.size()>0){
                    for(int j=0;j<mWhiteLists.size();j++){
                        HashMap<String, String> mWhiteList  = mWhiteLists.get(j);
                        String mWhiteListName = mWhiteList.get(JSONConstants.WHITE_LIST_NAME);
                        if(!StringUtil.IsNullOrEmpty(mWhiteListName) && cookie!=null){
                            if(mWhiteListName.equals(cookie.getName())){
                                listRestore.add(cookie);
                            }
                        }
                    }
                }

            }
        }
        Cookie[] array = (Cookie[])listRestore.toArray(new Cookie[size]);
        cookieStore.clear();
        cookieStore.addCookies(array);
        //Need keep the white list cookie for webview.
        importWhitelistCookies2Webview(array);
        //Log.debug("clean some cookie later:"+cookieStore.getCookies());
    }

    /**
     * import cookie to webview with coolie array
     */
    public void importWhitelistCookies2Webview(Cookie[] array){
        if(array!=null){
            for(int i=0;i<array.length;i++){
                importCookie2Webview(array[i]);
            }
        }
    }

    public void close() {
        try {
            if (client != null) {
                client.getConnectionManager().shutdown();
                client=null;
                myClient=null;
                cookieStore=null;
                localContext=null;
            }
        } catch (Exception e) {
            Log.error("httpclient shotdown error",e);
        }
    }
}
