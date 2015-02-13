package com.upzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by banliu on 2/12/2015.
 */
public final class ZipUtil {

    private ZipUtil(){}

    public static List<File> getFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception{
        List<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String sZName ="";
        while((zipEntry = inZip.getNextEntry())!= null){
            sZName = zipEntry.getName();
            if(zipEntry.isDirectory()){
                //get the folder name of the widget
                sZName = sZName.substring(0,sZName.length() -1);
                File folder = new File(sZName);
                if(bContainFolder){
                    fileList.add(folder);
                }
            } else {
                File file = new File(sZName);
                if(bContainFile){
                    fileList.add(file);
                }
            }
        }
        //end of wile

        inZip.close();

        return fileList;
    }


    public static InputStream upZip(String zipFilePath, String fileString)throws Exception{

        ZipFile zipFile = new ZipFile(zipFilePath);
        ZipEntry zipEntry = zipFile.getEntry(fileString);

        return  zipFile.getInputStream(zipEntry);
    }

    public static void unZipFolder(InputStream input, String outPathString)throws Exception{
        ZipInputStream inZip = new ZipInputStream(input);
        ZipEntry zipEntry = null;
        String sZName = "";

        while((zipEntry = inZip.getNextEntry()) != null){
            sZName = zipEntry.getName();
            if(zipEntry.isDirectory()){
                //get the folder name of the widget
                sZName = sZName.substring(0,sZName.length()-1);
                File folder = new File(outPathString + File.separator + sZName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + sZName);
                file.createNewFile();

                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                //read bytes into buffer
                while((len = inZip.read(buffer))!= -1){
                    //write byte from buffer at the position 0
                    out.write(buffer,0,len);
                    out.flush();
                }

                out.close();
            }
       }

        inZip.close();
    }

    public static void unZipFolder(String zipFileString, String outPathString)throws Exception{
        unZipFolder(new FileInputStream(zipFileString),outPathString);
    }

    public static void zipFolder(String srcFilePath, String zipFilePath)throws Exception{
        //create .zip
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFilePath));

        //open the out file
        File file = new File(srcFilePath);

        //zip
        zipFiles(file.getParent() + File.separator, file.getName(),outZip);
        //complete
        outZip.finish();
        outZip.close();

    }

    public static void zipFiles(String folderPath, String filePath, ZipOutputStream zipOut) throws Exception{
        if(zipOut == null){
            return;
        }

        File file = new File(folderPath + filePath);

        //check file or not
        if(file.isFile()){
            ZipEntry zipEntry = new ZipEntry(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[4096];

            while((len = inputStream.read(buffer)) != -1){
                zipOut.write(buffer,0, len);
            }

            zipOut.closeEntry();
        } else {
            //get the sub file in folder
            String fileList[] = file.list();
            if(fileList.length <= 0){
                ZipEntry zipEntry = new ZipEntry(filePath + File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }

            //iterator the sub file
            for(int i = 0; i< fileList.length; i++){
                zipFiles(folderPath,filePath + File.separator + fileList[i],zipOut);
            }
        }
    }
}
