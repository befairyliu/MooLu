package com.moolu.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.moolu.framework.NananLog;
import com.moolu.framework.entity.model.Entity;
import com.moolu.http.pack.MooLuHttpClient;
import com.moolu.json.gson.JsonUtil;
import com.moolu.storage.prefs.PrefConstants;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Nanan on 2/4/2015.
 */
public class DownloadUtil {

    private final static Logger Log = new NananLog(DownloadUtil.class);
    private static final String LAST_MODIFIED = "Last-Modified";

    private final Context context;
    String lastModified = null;
    AsyncTask asyncTask = null;
    int inputStreamLength = 0;

    public DownloadUtil(Context context){
        this.context = context;
    }
    public DownloadUtil(Context context,AsyncTask task){
        this.context = context;
        this.asyncTask=task;
    }

    public String getLastModified() {
        return lastModified;
    }

    public ByteArrayOutputStream downloadResource(String url,String cookieStr)throws ClientProtocolException,IOException{
        return downloadResourceAndLastModified(url,cookieStr);
    }

    public StringBuffer downloadText(String url) throws ClientProtocolException, IOException{
        MooLuHttpClient httpclient = MooLuHttpClient.getInstance(context);
        StringBuffer strBuf = null;
        if (url.indexOf('?') != -1) {
            url += "&" + new Date().getTime();
        } else {
            url += "?" + new Date().getTime();
        }
        strBuf = httpclient.connectURL(url, null);
        return strBuf;
    }

    public ByteArrayOutputStream downloadResourceAndLastModified(String url,String cookieStr)throws ClientProtocolException,IOException {

        MooLuHttpClient httpclient = MooLuHttpClient.getInstance(context);
        InputStream is=null;
        try{
            if(url.indexOf('?')!=-1){
                url+="&"+new Date().getTime();
            }else{
                url+="?"+new Date().getTime();
            }
            //Log.debug("download:"+url);
            HttpResponse response = httpclient.getHttpResponse(url, cookieStr,false);
            if (response != null) {
                is= httpclient.getResponseInputStream(response);
                Header[] headers = response.getHeaders(LAST_MODIFIED);
                if (headers.length > 0) {
                    lastModified = headers[0].getValue();
                }
            }
            if(is!=null){
                if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copyIs(baos,is);
                    return baos;
                }else{
                    Log.error("Response error:{},{}",response.getStatusLine().getReasonPhrase(),
                            response.getStatusLine().getStatusCode());
                }
            }
        }finally{
            IOUtils.close(is);
        }
        return null;
    }

    public boolean downloadResourceAndSave(String url, String resourcePath, String fileName,
                                           Handler handler,final int updateScale)
            throws ClientProtocolException, IOException {

        MooLuHttpClient httpclient = MooLuHttpClient.getInstance(context);
        InputStream is = null;
        try {
            if (url.indexOf('?') != -1) {
                url += "&" + new Date().getTime();
            } else {
                url += "?" + new Date().getTime();
            }
            HttpResponse response = httpclient.getHttpResponse(url, null, false);
            if (response != null) {
                is = httpclient.getResponseInputStream(response);
            }
            if (is != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    //Log.debug(is.)
                    return saveResource(resourcePath, fileName, is,handler,updateScale);
                } else {
                    Log.error("Response error:{},{}", response.getStatusLine().getReasonPhrase(),
                            response.getStatusLine().getStatusCode());
                }
            }
        } finally {
            IOUtils.close(is);
        }
        return false;
    }

    public boolean writeToFile(final byte[] bytes,String path) throws IOException {
        return saveFile(new ByteArrayInputStream(bytes),path);
    }

    public boolean writeToFile(final byte[] bytes,String path, Handler handler,
                               final int updateScale) throws IOException {
        return saveFile(new ByteArrayInputStream(bytes),path,handler,updateScale);
    }

    public boolean saveFile(InputStream is,String path, Handler handler,
                            final int updateScale)throws IOException{

        OutputStream os= null;
        boolean flag=false;
        try{
            File savedFile=new File(path);
            os = new FileOutputStream(savedFile);
            byte[] buf = new byte[1024];
            int len;
            int sendScale=updateScale;

            while ((len = is.read(buf)) > 0){
                if(this.asyncTask!=null&&this.asyncTask.isCancelled()){
                    Log.debug("===download canceled:{}==",path);
                    os.close();
                    savedFile.delete();
                    break;
                }
                int updateCounter =len;
                os.write(buf, 0, len);
                //send message to update progress bar
                //if(updateCounter>=sendScale){

                //System.out.print("=======updateCounter"+updateCounter);
                sendUpdateProgressBarMsg(handler, updateCounter);
                //updateCounter=0;
                //}
            }
            //send message to update progress bar
            //sendUpdateProgressBarMsg(handler,updateCounter);
            flag=true;
        }finally{
            IOUtils.close(os);
            IOUtils.close(is);
        }
        return flag;
    }

    public void sendUpdateProgressBarMsg(Handler handler,int updateCounter){
        Message msg = Message.obtain();
        msg.what = PrefConstants.DOWNLOAD_PROGRESS_UPDATE_MSG;
        msg.arg1=updateCounter;
        handler.sendMessage(msg);
    }

    public boolean saveFile(InputStream is,String path)throws IOException{
        OutputStream os= null;
        boolean flag=false;
        try{
            os = new FileOutputStream(new File(path));
            byte[] buf = new byte[1024];
            int len;
            int i=0;
            while ((len = is.read(buf)) > 0){
                i+=len;
                os.write(buf, 0, len);
            }
            Log.debug("========++File Size ==++======="+i);
            is = new FileInputStream(new File(path));
            if (is != null) {
                Entity entity = JsonUtil.getObjectFromJson(is, Entity.class);
                Log.debug("Using cached entities file");
            }
            os.flush();
            flag=true;
        }finally{
            IOUtils.close(os);
            IOUtils.close(is);
        }
        return flag;
    }

    public String getStrFormByte(ByteArrayOutputStream baos) throws IOException{
        byte[] buf = null;
        String result = null;
        try {
            if(baos!=null){
                buf = baos.toByteArray();
                if(buf != null){
                    result = new String(buf, HTTP.UTF_8);
                }
            }
        }finally{
            IOUtils.close(baos);
        }
        return result;

    }

    public  boolean saveResource(String resourcePath, String fileName, InputStream in,
                                 Handler handler,final int updateScale)throws IOException {
        String pathName = resourcePath + File.separator + fileName;
        File dir = new File(resourcePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        boolean flag = false;
        try {
            flag = saveFile(in, pathName,handler,updateScale);
        } finally {
            IOUtils.close(in);
        }
        return flag;
    }
    public boolean createAndSaveFile(InputStream is,String path,String name)throws IOException{
        OutputStream os= null;
        boolean flag=false;
        try{
            File file = new File(path, name);
            if(!file.exists()){
                file.createNewFile();

            }
            os = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len;
            int i=0;
            while ((len = is.read(buf)) > 0){
                i+=len;
                os.write(buf, 0, len);
            }
            flag=true;
        }finally{
            IOUtils.close(os);
            IOUtils.close(is);
        }
        return flag;
    }
}
