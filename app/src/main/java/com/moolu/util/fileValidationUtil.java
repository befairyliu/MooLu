package com.moolu.util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nanan on 3/4/2015.
 */
public class fileValidationUtil {
    public static boolean validateFileName(String regString,String file){
        String fileName = file.substring(0, file.lastIndexOf("."));
        Pattern pattern = Pattern.compile(regString);
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }
    public static boolean validateFileSize(File file,float size){
        long sizeByByte = (long)(size*1024*1024);
        if(file.length() > sizeByByte){
            return false;
        }else{
            return true;
        }
    }
    public static boolean validateFileType(ArrayList<String> typeList,String file){
        String fileType = file.substring(file.lastIndexOf("."));
        boolean findFlag = false;
        int size = typeList.size();
        for(int i = 0;i<size;i++){
            if(typeList.get(i).equalsIgnoreCase(fileType)){
                findFlag = true;
                break;
            }
        }
        return findFlag;
    }
}
