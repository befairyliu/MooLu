package com.ssl.cert;

import android.content.Context;
import android.util.Log;

import com.moolu.R;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Created by Nanan on 2/10/2015.
 */
public class SSLCustomSocketFactory extends SSLSocketFactory{

    private static final String Tag = "SSLCustomSocketFactory";
    private static final String KEY_PASS ="pw12306";

    public SSLCustomSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
    }

    public static SSLSocketFactory getSocketFactory(Context context){
        try{
            InputStream ins = context.getResources().openRawResource(R.raw.cert12306);

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try{
                trustStore.load(ins,KEY_PASS.toCharArray());
            } finally {
                ins.close();
            }

            SSLSocketFactory factory = new SSLCustomSocketFactory(trustStore);
            return factory;
        } catch(Throwable e){
            Log.d(Tag, e.getMessage());
            e.printStackTrace();
        }

        return null;

    }
}
