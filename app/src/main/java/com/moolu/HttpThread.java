package com.moolu;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by banliu on 2/2/2015.
 */
public class HttpThread extends Thread{

    private String strUrl;
    public HttpThread(String url){
        this.strUrl = url;
    }

    @Override
    public void run(){
        try {
            URL httpUrl = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);

            InputStream in = conn.getInputStream();
            File downloadFile;
            File sdFile;

            FileOutputStream out = null;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                downloadFile = Environment.getExternalStorageDirectory();
                sdFile = new File(downloadFile,"test.apk");
                out = new FileOutputStream(sdFile);
            }

            byte[] b = new byte[6*1024];
            int len;
            while((len =in.read())!= -1){
                if(out != null){
                    out.write(b,0,len);
                }
            }

            if(out != null){
                out.close();
            }
            if(in != null){
                in.close();
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
