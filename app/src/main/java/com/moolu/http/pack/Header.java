package com.moolu.http.pack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.moolu.R;
import com.moolu.framework.NananLog;
import com.moolu.storage.prefs.PrefConstants;
import com.moolu.util.DeviceUtil;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Nanan on 3/2/2015.
 */
public class Header {
    private final static String NATIVE_APP_HEADER_NAME = "native-app";
    private final static String DEVICE_ID_HEADER_NAME = "device-id";
    private final static String DEVICE_TYPE_HEADER_NAME = "device-type";
    private final static String DEVICE_STATUS_HEADER_NAME = "device-status";
    private final static Logger Log = new NananLog(Header.class);

    public Map<String, String> createHeaders(final Context context) {

        return createHeaders(context, DeviceUtil.generateDeviceID(context),false);
    }

    /**
     * The addDeviceStatus parameter,if you want to put device security status in header, pass true.
     */
    public Map<String, String> createHeaders(final Context context, boolean addDeviceStatus) {
        return createHeaders(context, DeviceUtil.generateDeviceID(context), addDeviceStatus);
    }

    /**
     * Setups up the additional headers to be sent to HSBC mobile websites.
     */
    public Map<String, String> createHeaders(final Context context, final String deviceID, boolean addDeviceStatus) {
        Log.debug("createHeaders function show debug message.");
        Map<String, String> headers = new HashMap<String, String>();
        String deviceTypeHeader = String.format(context.getString(R.string.device_type_header), Build.VERSION.RELEASE);
        headers.put(Header.DEVICE_TYPE_HEADER_NAME, deviceTypeHeader);

        headers.put(Header.DEVICE_ID_HEADER_NAME, deviceID);

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String appName = String.format(context.getString(R.string.native_app_header), info.versionName);
            headers.put(Header.NATIVE_APP_HEADER_NAME, appName);
        }
        catch (NameNotFoundException e) {
            Log.error("Can't get app header");
        }
        return headers;
    }

    public Map<String, String> createEPSHeaders(final Context context) {
        return createHeaders(context, DeviceUtil.generateDeviceID(context),false);
    }

//end EPS
    /**
     * Gets the UUID for this install, if a UUID hasn't been generated for this install,
     * it will be generated. If a previously generated UUID exists, this will be returned instead.
     */
    public synchronized String getUUID(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PrefConstants.PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.contains(PrefConstants.UUID_KEY)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PrefConstants.UUID_KEY, UUID.randomUUID().toString());
            editor.commit();
        }
        return prefs.getString(PrefConstants.UUID_KEY, null);
    }

}
