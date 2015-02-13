package com.ssl.cert;

import android.util.Log;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Nanan on 2/10/2015.
 */
public class SSLTrustAllSocketFactory extends SSLSocketFactory{

    private static final String Tag = "SSLTrustAllSocketFactory";
    private SSLContext mSSLContext;

    public SSLTrustAllSocketFactory(KeyStore truststore) throws Throwable{
        super(truststore);
        try{
            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null,new TrustManager[]{new SSLTrustAllManager()},null);
            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        //return super.createSocket(socket, host, port, autoClose);
        return mSSLContext.getSocketFactory().createSocket(socket,host,port,autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        //return super.createSocket();
        return mSSLContext.getSocketFactory().createSocket();
    }

    public static SSLSocketFactory getSocketFactory(){
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null,null);
            SSLSocketFactory factory = new SSLTrustAllSocketFactory(trustStore);
            return factory;
        } catch (Throwable e){
            Log.d(Tag,e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public class SSLTrustAllManager implements X509TrustManager{
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            //return new X509Certificate[0];
            return null;
        }
    }
}
