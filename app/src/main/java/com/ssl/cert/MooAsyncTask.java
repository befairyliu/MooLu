package com.ssl.cert;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Nanan on 2/13/2015.
 */
public class MooAsyncTask extends AsyncTask<Object,Integer,String> {

    private static final String Tag = "MooAsync";
    private static int BUFFERSIZE = 8192;

    private MooAsyncListener mListener;

    private static String parseString(HttpEntity entity){
        try{
            ByteArrayOutputStream ostr = new ByteArrayOutputStream();
            InputStream istr = entity.getContent();
            byte[] buf = new byte[BUFFERSIZE];
            int count = -1;
            while((count = istr.read(buf,0,BUFFERSIZE))!= -1){
                ostr.write(buf,0,count);
            }
            buf = null;
            return new String(ostr.toByteArray());

        } catch(Throwable e) {
            e.printStackTrace();
            Log.e(Tag, e.getMessage());
            return "";
        }
    }

    public static String doSome(int n,String url,Context context){
        HttpUriRequest request = null;
        try{
            request = new HttpGet(url);
        } catch(Throwable e){
            Log.e(Tag, e.getMessage());
        }

        HttpClient httpClient;
        switch (n){
            case 1:
                httpClient = MeHttp.getClient();
                break;
            case 2:
                httpClient = MeHttp.getHttpsClient();
                break;
            case 3:
                httpClient = MeHttp.getTrustAllClient();
                break;
            case 4:
                httpClient = MeHttp.getCustomClient(context);
                break;
            default:
                return "Invalid Selection";
        }

        try{
            HttpResponse httpResponse = httpClient.execute(request);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();
            if(responseCode == 200 && entity != null){
                return parseString(entity);
            } else {
                return String.format("Http error ! \n %d: %s",responseCode, message);
            }

        } catch(ClientProtocolException e){
            return e.getMessage();
        } catch(Throwable e){
            e.printStackTrace();
            return e.getMessage();
        } finally{
            httpClient.getConnectionManager().shutdown();
        }
    }

    public MooAsyncTask(MooAsyncListener listener){
        this.mListener = listener;
    }
    @Override
    protected String doInBackground(Object... params) {
        int n = (Integer)params[0];
        String url = (String)params[1];
        Context context = (Context)params[2];

        return doSome(n,url,context);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListener.onErrorOrCancel();
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mListener.onSuccess(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    public interface MooAsyncListener {
        public void onSuccess(String result);
        public void onErrorOrCancel();
    }
}
