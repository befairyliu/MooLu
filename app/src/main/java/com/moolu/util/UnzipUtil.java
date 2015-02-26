package com.moolu.util;

import android.os.Handler;
import android.os.Message;

import com.moolu.storage.prefs.PrefConstants;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nanan on 2/26/2015.
 */
public class UnzipUtil {

    public void unZipFolder(String zipPathName, String targetPath,Handler handler) throws Exception {
        InputStream zipFileIs = null;
        java.util.zip.ZipInputStream inZip = null;

        java.io.File file = null;
        java.io.FileOutputStream out = null;
        java.io.File folder = null;

        try {
            zipFileIs = new FileInputStream(zipPathName);
            inZip = new java.util.zip.ZipInputStream(zipFileIs);
            java.util.zip.ZipEntry zipEntry;
            String szName = "";

            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();

                if (zipEntry.isDirectory()) {

                    // get the folder name of the widget
                    szName = szName.substring(0, szName.length() - 1);
                    folder = new java.io.File(targetPath + java.io.File.separator + szName);
                    // LOG.debug("Folder name:"+szName);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                } else {

                    file = new java.io.File(targetPath + java.io.File.separator + szName);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                    // get the output stream of the file
                    out = new java.io.FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // read (len) bytes into buffer
                    while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                        sendUpdateProgressBarMsg(handler, len);
                    }
                    close(out);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                /*
                 * resources: Streams may introduce reliability issues in the
                 * Android native application
                 */
                close(out);
                close(inZip);
                close(zipFileIs);
            } catch (Exception e) {
                throw e;
            }
        }
    }
    public void sendUpdateProgressBarMsg(Handler handler,int updateCounter){
        Message msg = Message.obtain();
        msg.what = PrefConstants.DOWNLOAD_PROGRESS_UPDATE_MSG;
        msg.arg1=updateCounter;
        handler.sendMessage(msg);
    }
    public void close(Closeable ioObject) {
        if (null != ioObject) {
            try {
                if(ioObject instanceof Flushable){
                    ((Flushable) ioObject).flush();
                }
                ioObject.close();
                ioObject=null;
            }
            catch (IOException e) {
                ioObject=null;
            }
        }
    }
}
