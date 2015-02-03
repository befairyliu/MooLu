package com.moolu.util;

import com.moolu.framework.NananLog;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nanan on 2/3/2015.
 */
public class IOUtils {
    //TODO for Closeable, Flushable and ByteArrayOutputStream class
    private final static Logger Log = new NananLog(IOUtils.class);
    public static void close(Closeable ioObject){
        if(null != ioObject){
            try{
                if(ioObject instanceof Flushable){
                    ((Flushable)ioObject).flush();
                }
                ioObject.close();
                ioObject = null;
            } catch (IOException e){
                ioObject = null;
                Log.error("IO stream close error:{}",e);
            }
        }
    }

    public static void copyStream(final InputStream is, final OutputStream os) throws IOException{
        byte[] buffer = new byte[1024];
        int n = 0;
        while(-1 != (n = is.read(buffer))){
            os.write(buffer,0,n);
        }
    }

    public static void copyIs(ByteArrayOutputStream baos, InputStream is){
        try{
            IOUtils.copyStream(is,baos);
        } catch (IOException e){
            Log.error("copyIs error:{}",e.getMessage());
        }
    }

}
