package com.moolu.plugins.eps;

import android.content.Context;

import com.moolu.framework.NananLog;

import org.slf4j.Logger;

import java.io.File;

/**
 * Created by Nanan on 3/4/2015.
 */
public class DeviceStatusProvider {
    private final static Logger Log = new NananLog(DeviceStatusProvider.class);
    private static DeviceStatusProvider provider;
    private static DeviceStatus deviceStatus = null;

    private DeviceStatusProvider() {}

    public static synchronized DeviceStatusProvider getInstance() {
        if (provider == null) {
            provider = new DeviceStatusProvider();
        }
        return provider;
    }

    public boolean checkDeviceStatus(Context context) {
        deviceStatus = new DeviceStatus();
        try {
            Log.debug("start checkDeviceStatus");
            deviceStatus.setRooted(isDeviceRooted());
            Log.debug(deviceStatus.toString());
            return true;
        } catch (Exception e) {
            Log.error("=====calculate Device Status error=====", e);
        } catch (Error e) {
            Log.error("=====calculate Device Status error=====", e);
        }
        return false;
    }

    public boolean isDeviceRooted() {
        if (checkSystemBuildTag()) {
            return true;
        }
        if (checkSuperuserInstallFile()) {
            return true;
        }
        if (checkExecuteCommand()) {
            return true;
        }
        if (checkSuFileExist()) {
            return true;
        }
        return false;
    }

    public DeviceStatus getDeviceStatus() {
        if(deviceStatus == null){
            deviceStatus = new DeviceStatus();
        }
        return deviceStatus;
    }

    public boolean checkSystemBuildTag() {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    public boolean checkSuperuserInstallFile() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }

        return false;
    }

    public boolean checkExecuteCommand() {
        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSuFileExist() {
        File su1 = new File("/system/bin/su");
        if (su1.exists()) {
            return true;
        }
        File su2 = new File("/system/xbin/su");
        if (su2.exists()) {
            return true;
        }
        return false;
    }

    public void cleanDeviecStatus(){
        deviceStatus = null;
    }
}
