package com.moolu.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import org.apache.http.client.ClientProtocolException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nanan on 2/4/2015.
 */
public class DownloadUtil {

    private final Context context;
    String lastModified = null;
    AsyncTask asyncTask=null;
    int inputStreamLength=0;

    public DownloadUtil(Context context){
        this.context = context;
    }
    public DownloadUtil(Context context,AsyncTask task){
        this.context = context;
        this.asyncTask=task;
    }

    public ByteArrayOutputStream downloadResource(String url,String cookieStr)throws ClientProtocolException,IOException{
        return downloadResourceAndLastModified(url,cookieStr);
    }

    public ByteArrayOutputStream downloadResourceAndLastModified(String url,String cookieStr)throws ClientProtocolException,IOException {

        return null;
    }

    public boolean writeToFile(final byte[] bytes,String path) throws IOException {
        //return saveFile(new ByteArrayInputStream(bytes),path);
        return false;
    }

    public boolean saveFile(InputStream is,String path, Handler handler,final int updateScale)throws IOException{

        return false;
    }
}
