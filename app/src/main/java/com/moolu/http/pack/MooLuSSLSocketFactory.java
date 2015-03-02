package com.moolu.http.pack;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by banliu on 3/2/2015.
 */
public class MooLuSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MooLuSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException, UnrecoverableKeyException {

        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //System.err.println("checkClientTrusted1");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //System.err.println("checkClientTrusted2");
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket connectSocket(Socket sock, String host, int port,
                                InetAddress localAddress, int localPort, HttpParams params)
            throws IOException {
        return super.connectSocket(sock, host, port, localAddress, localPort, params);
    }

    @Override
    public boolean isSecure(Socket sock) throws IllegalArgumentException {
        return super.isSecure(sock);
    }

    @Override
    public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
        super.setHostnameVerifier(hostnameVerifier);
    }

    @Override
    public X509HostnameVerifier getHostnameVerifier() {
        return super.getHostnameVerifier();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
                               boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

}
