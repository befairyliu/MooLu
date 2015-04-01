package com.agl.rsa;

/**
 * Created by Nanan on 4/1/2015.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;


public class GetCert {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        String net = "https://www1.qualityassurance.fileupload.asiapacific.hsbc.com/imageAttach/forMobileTeam/AppTestEnv/sslpin/EntityList-1.5.11-android-test.xml";
        //String net = "https://github.com/";
        //String net = "https://www.google.com";
        //downloadCert(net);
        try {
            testConnectionTo(net);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void testConnectionTo(String net) throws Exception {
        //for proxy
        String proxyServer = "10.61.208.54";
        String proxyPort = "8080";

        String proxyUsername = "";
        String proxyPassword = "";

        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyServer, Integer.parseInt(proxyPort)));

		/*
		System.setProperty("http.proxyHost", proxyServer);
		System.setProperty("http.proxyPort", proxyPort);
		*/

        URL url = new URL(net);
        //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(proxy);

        //for proxy-Authorization
		/*
		String uname_pwd = proxyUsername + ":" + proxyPassword;
		String authString = "Basic" + Base64.encodeBase64String(uname_pwd.getBytes());
		conn.setRequestProperty("Proxy-Authorization", authString);
		*/

        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        for (Certificate cert : certs) {
            System.out.println("Certificate is: " + cert);
            if(cert instanceof X509Certificate) {
                X509Certificate certificate = (X509Certificate) cert;
                certificate.getSerialNumber().toString();
                certificate.getTBSCertificate();
            } else {
                System.err.println("Unknown certificate type: " + cert);
            }
        }
    }

}
