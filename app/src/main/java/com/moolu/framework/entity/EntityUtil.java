package com.moolu.framework.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.moolu.R;
import com.moolu.application.NApplication;
import com.moolu.framework.NananLog;
import com.moolu.framework.entity.model.Center;
import com.moolu.framework.entity.model.Entity;
import com.moolu.json.gson.JsonUtil;
import com.moolu.storage.prefs.PrefConstants;
import com.moolu.util.DeviceUtil;
import com.moolu.util.DownloadUtil;
import com.moolu.util.IOUtils;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Nanan on 2/4/2015.
 */
public class EntityUtil {
    //TODO for BlockingQueue
    private final static Logger Log = new NananLog(EntityUtil.class);
    private final Context context;
    private String currentEntityPathName;
    private String currentEntityPath;
    private String backupEntityPath;
    public boolean isSuccess = false;
    private long entityDuration = 100;//3600*1*1000;// 1 hours
    //private long entityDuration = 60*1*1000;// 1 min
    private final String ENTITY_FILE_NAME = "entitylist.xml";
    private NApplication application;
    public static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);

    public EntityUtil(){
        this.context = null;
    }

    public EntityUtil(final Context context){
        this.context = context;
        SharedPreferences prefs = this.context.getSharedPreferences(PrefConstants.PREFS_NAME,Context.MODE_PRIVATE);
        currentEntityPath = prefs.getString(PrefConstants.ENTITY_PATH,null);
        currentEntityPathName = currentEntityPath + File.separator+ ENTITY_FILE_NAME;
        backupEntityPath = prefs.getString(PrefConstants.BACKUP_ENTITY_PATH,null);
    }

    public Entity initEntity(){
        return initEntity(true);
    }

    public void initEntityReActivite(){
        Log.debug("back to fontground and download the entity");
        initEntity(false);
    }

    public Entity initEntity(boolean read){
        return initEntity(read,0);
    }

    public Entity initEntity(boolean read, int repeatTime){
        Entity entity = null;
        if(localCopyExist(currentEntityPathName)){
            if(localEntityCopyExpired()){
                startDownloadThread();
            }

            if(read){
                try {
                    queue.take();
                    entity = readLocalCopyEntityList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            entity =dowloadEntityListInMainThread(context,repeatTime);
        }

        return entity;
    }

    public Entity dowloadEntityListInMainThread(Context context,int repeatTime){
        Entity entity=null;
        try {
            if(repeatTime>0){/**read time download fail**/
                return null;
            }
            /**At the first app launch time, will check the network is available or not for download Entitylist in main thread. if not then alert network error**/
            if(DeviceUtil.deviceOnline(context)){
                downloadChecksumAndEntitylist();
                entity=readLocalCopyEntityList();
                if(!isSuccess){
                    entity = initEntity(true,repeatTime+1);
                }

            }else{
                entity = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public static boolean localCopyExist(String path){
        return new File(path).exists()? true:false;
    }

    public boolean localEntityCopyExpired(){
        Date now = new Date();
        long currentTimestamp = now.getTime();
        SharedPreferences prefs = this.context.getSharedPreferences(PrefConstants.PREFS_NAME,Context.MODE_PRIVATE);
        long timestamp = prefs.getLong(PrefConstants.CHECKING_TIME_STAMP,-1L);
        if(currentTimestamp - timestamp > entityDuration
                ||currentTimestamp-timestamp < 0 ){
            return true;
        } else{
            return false;
        }
    }

    //to start the download flow
    public void startDownloadThread(){
        Log.debug("New download thread");
        new Thread(){
            @Override
            public void run() {
                downloadChecksumAndEntitylist();
                try{
                    queue.put(1);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void downloadChecksumAndEntitylist(){
        //to download the new entity list
        try{
            isSuccess = downloadEntityListAndSave();
            if(isSuccess){
                updateEntityPointer();
                updateCheckingTimestamp();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean downloadEntityListAndSave(){
        try{
            String pathName = this.currentEntityPath +File.separator+this.ENTITY_FILE_NAME;
            String entityURL = this.context.getString(R.string.entity_url);
            DownloadUtil downloadUtil = new DownloadUtil(this.context);
            ByteArrayOutputStream baos = downloadUtil.downloadResource(entityURL,null);

            File dir = new File(this.currentEntityPath);
            if(!dir.exists()){
                dir.mkdir();
            }
            boolean flag = downloadUtil.writeToFile(baos.toByteArray(),pathName);
            return flag;
        } catch (Exception e){
            Log.error("download Entity error",e);
            return false;
        }
    }

    public void removeLocalEntityCopy(String path) {
        Log.debug("remove loacl entitylist copy");
        File file=new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    public Entity readLocalCopyEntityList() {
        Entity entity = null;
        // Read cached file
        InputStream is = null;
        try{
            is = new FileInputStream(new File(currentEntityPathName));
            if (is != null) {
                entity = JsonUtil.getObjectFromJson(is, Entity.class);
                Log.debug("Using cached entities file");
            }
        }catch(IOException e){
            Log.error("Unable to open the cached file", e);
        }finally{
            IOUtils.close(is);
        }
        return entity;
    }

    private void saveLastModified(String lastModified) {
        SharedPreferences prefs = this.context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PrefConstants.CHECKSUM_LAST_MODIFIED,lastModified);
        editor.commit();
    }
    public static String getLastModifiedFromSave(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PrefConstants.CHECKSUM_LAST_MODIFIED,null);
    }

    public void updateEntityPointer() {
        SharedPreferences prefs = this.context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PrefConstants.ENTITY_PATH,this.currentEntityPath);
        editor.putString(PrefConstants.BACKUP_ENTITY_PATH,this.backupEntityPath);
        editor.commit();
        /**point to the correct path name***/
        currentEntityPathName=currentEntityPath+File.separator+ENTITY_FILE_NAME;
    }

    public void updateCheckingTimestamp() {
        Date now=new Date();
        long timestamp=now.getTime();

        SharedPreferences prefs = this.context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PrefConstants.CHECKING_TIME_STAMP,timestamp);
        editor.commit();
    }

    public static Center getApp(String app, Entity entity){
        for (Center center:entity.getCenter()) {
            if (center.getRegionId() != null && center.getRegionId().equals(app)) {
                return center;
            }
        }
        return null;
    }

    public static void setCurrentApp(Context context,Center center){
        SharedPreferences prefs = context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PrefConstants.APP_ID_KEY,center.getRegionId());
        editor.putString(PrefConstants.APP_SHORT_NAME_KEY,center.getRegionName());
        editor.commit();
    }

    public static String getCurrentAppId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PrefConstants.APP_ID_KEY,null);

    }

    public static boolean deviceOnline(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null) {
            Log.debug("device not on line");
            return false;
        }
        //mobile app does not transmit any network data when the app is in background,
        // checking background setting is not necessary
        if (!info.isAvailable()|| !info.isConnected()) {
            Log.debug("device not on line");
            return false;
        } else {
            //Saving date and time of last net connectivity so that we can check for how
            // many days the user has not connected to net.
            Locale locale = Locale.getDefault();
            Calendar cal = Calendar.getInstance(locale);
            Long dtMillies = cal.getTimeInMillis();

            SharedPreferences prefDtMillie = context.getSharedPreferences("prefDtMillie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefDtMillie.edit();
            editor.putLong("dtMillies", dtMillies);
            editor.commit();
            return true;
        }
    }
}
