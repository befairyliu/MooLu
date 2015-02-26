package com.moolu.util;

import android.content.Context;

import com.moolu.framework.NananLog;
import com.moolu.framework.entity.ProcessConstants;

import org.slf4j.Logger;

import java.io.File;
import java.util.Date;

/**
 * Created by Nanan on 2/26/2015.
 */
public class ProcessUtil {
    private final static Logger Log = new NananLog(ProcessUtil.class);
    public static String getNewWebResourcePath(Context context,String eid, String webVersion){
        StringBuffer path = getBasicWebResourcePath(context, eid, webVersion);
        if (path != null) {
            Date now = new Date();
            long timestamp = now.getTime();
            String timeStr = String.valueOf(timestamp);
            File webfolder = new File(path.toString());
            File[] fileList = webfolder.listFiles();
            if (fileList != null) {
                if (fileList.length > 1) {
                    // updateInfo.setAllUnzipFlag(true);
                    FileUtil.deleteDirectory(path.toString());
                } else if (fileList.length == 1) {
                    for (File file : fileList) {
                        if (file.isFile()) {
                            file.delete();
                        } else {
                            StringBuffer oldPath = path;
                            StringBuffer targetPathName = path.append(File.separator)
                                    .append(ProcessConstants.WEB_RESOURCE_PREFIX + timeStr);

                            if (renameFile(targetPathName.toString(), file)) {
                                //updateInfo.setAllUnzipFlag(false);
                            } else {
                                FileUtil.deleteDirectory(oldPath.toString());
                                //updateInfo.setAllUnzipFlag(true);
                            }
                            return targetPathName.toString();
                        }
                    }
                } else {
                    // updateInfo.setAllUnzipFlag(true);
                }
            } else {
                //  updateInfo.setAllUnzipFlag(true);
            }
            path.append(File.separator).append(ProcessConstants.WEB_RESOURCE_PREFIX + timeStr);
            return path.toString();
        }

        return null;
    }

    public static StringBuffer getBasicWebResourcePath(Context context,String eid,String webVersion){
        try{
            if(context == null){
                return null;
            }
            StringBuffer path = new StringBuffer(context.getFilesDir().toString());
            path.append(File.separator)
                    .append(ProcessConstants.RESOURCE_PATH)
                    .append(File.separator)
                    .append(eid)
                    .append(File.separator)
                    .append(ProcessConstants.WEB_SRC_PATH)
                    .append(File.separator)
                    .append(webVersion)
                    .append(File.separator)
                    .append(ProcessConstants.WEB_PATH);

            return path;

        } catch (Exception e){
            Log.error("get resource path error", e);
        }
        return null;
    }

    private static boolean renameFile(String targetPathName, File sourceFile){
        File file = new File(targetPathName);
        return sourceFile.renameTo(file);
    }

    public static String getGlobalResourcePath(Context context, String webVersion){
        try{
            if(context == null){
                return null;
            }
            StringBuffer path = new StringBuffer(context.getFilesDir().toString());
            path.append(File.separator)
                    .append(ProcessConstants.RESOURCE_PATH)
                    .append(File.separator)
                    .append(ProcessConstants.GLOBAL_TYPE)
                    .append(File.separator)
                    .append(ProcessConstants.WEB_SRC_PATH)
                    .append(File.separator)
                    .append(webVersion)
                    .append(File.separator)
                    .append(ProcessConstants.ZIP_PATH);

            return path.toString();
        } catch (Exception e){
            Log.error("get resource path error", e);
        }

        return null;
    }

    public static String getFileNameByURL(String url){
        if(url != null){
            int postion = url.lastIndexOf(File.separator);
            if(postion != -1){
                return url.substring(postion+1);
            }
        }
        return null;
    }

    public static String getPathName(String resourceDir, String fileName){
        StringBuffer pathName = new StringBuffer(resourceDir);
        return pathName.append(File.separator)
                .append(fileName)
                .toString();
    }

    public static boolean isExists(String path, String fileName){
        String pathName = path + File.separator + fileName;
        File file = new File(pathName);

        return file.exists()? true: false;
    }
}
