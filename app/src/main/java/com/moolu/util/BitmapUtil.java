package com.moolu.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;

/**
 * Created by Nanan on 3/4/2015.
 */
public class BitmapUtil {
    public static Bitmap getThumbnailByPath(String filePath,int thumbnailSize){
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)){
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outWidth
                : bounds.outHeight;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / thumbnailSize;
        return BitmapFactory.decodeFile(filePath, opts);
    }
    public static Bitmap getThumbnailByPath(String filePath,Activity activity){
        int thumbnailSize=1;
        Rect rect = new Rect();
        View a = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        a.getWindowVisibleDisplayFrame(rect);
        int screenWidthPX = rect.width();
        int screenHeightPX = rect.height();
        float screenRatio=screenWidthPX/screenHeightPX;
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)){
            return null;
        }
        float imageRatio=bounds.outWidth/bounds.outHeight;
        if(screenRatio>imageRatio){
            thumbnailSize=screenHeightPX;
        }else{
            thumbnailSize=screenWidthPX;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outWidth
                : bounds.outHeight;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / thumbnailSize;
        return BitmapFactory.decodeFile(filePath, opts);
    }
}
