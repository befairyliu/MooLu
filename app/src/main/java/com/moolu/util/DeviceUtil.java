package com.moolu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.moolu.framework.NananLog;
import com.moolu.json.gson.JsonUtil;

import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Nanan on 2/26/2015.
 */
public class DeviceUtil {
    // declare the Global Constant, use Protected to allow future extensibility
    protected final static String ANDROID_ID = "Android_ID";
    protected final static String SERIAL = "SerialNumber";
    protected final static String IMEI = "IMEI";
    protected final static String IMSI = "IMSI";
    protected final static String WIFI_MAC_ADDRESS = "Wifi_MAC_Address";
    protected final static String ANDROID_UUID = "Android_UUID";
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static final int NETTYPE_WIFI = 0x01;
    protected final static Logger LOG = new NananLog(DeviceUtil.class);

    /*
     * Program main logic
     * This method is set to public to allow visibility from external caller
     */
    public static String generateDeviceID(Context context) {
        final String TOKEN_SEPARATOR = "_";
        String deviceID_str = "";
        ArrayList <String> deviceSpecificAttributeList;

        // detect if the device specific attribute has been instantiated before
        deviceSpecificAttributeList = retrieveMapping(context);
        if (deviceSpecificAttributeList==null || deviceSpecificAttributeList.size()<=0) {
            LOG.debug("device specific attribute not instaniated");
            deviceSpecificAttributeList = analyzeDeviceSpecificAttribute(context);
        } else {
            LOG.debug("device specific attribute already instaniated");
        }

        /*
         * Device ID is constructed by 3 major parts:
         * Platform Name | Model | Device Unique Attribute
         *
         * For example:
         * Platform Name: Android
         * Model: Galaxy Nexus
         * Device Specific Attribute: Android ID + Serial Number
         */
        // generate the platform name part
        String platformName = getPlatform();

        // generate the hardware model
        String hardwareModel = getModel();

        // generate the device specific attribute part
        String deviceSpecificAttribute = generateDeviceSpecificAttribute(deviceSpecificAttributeList,context);

        // combine all 3 elements to form the device ID string
        deviceID_str = platformName + TOKEN_SEPARATOR + hardwareModel + TOKEN_SEPARATOR + deviceSpecificAttribute;
        LOG.debug("Generate the Device ID String: " + deviceID_str);

        return deviceID_str;
    }

    /*
     * This method contain the logic which govern what attributes will be chosen and
     * how they are selected to form the device specific attributes
     *
     * The selected attribute names will be contained in an ArrayList object and stored
     * into local device storage (SharedPreference) for future referencing.
     *
     * It will return the ArrayList object to the external caller
     */
    protected static ArrayList <String> analyzeDeviceSpecificAttribute(Context context) {
        ArrayList <String> deviceSpecificAttributeList = new ArrayList <String> ();

        String android_id;
        String serial_number;
        String imei;
        String imsi;
        String wifi_mac_address;
        String uuid;

        android_id = getAndroidID(context);
        serial_number = getSerialNumber();
        imei = getIMEI(context); // added by Michael Y W Yeung

        if (android_id != null || serial_number != null) { // anyone of them is NOT null
            if (android_id != null) {
                deviceSpecificAttributeList.add(ANDROID_ID);
            }
            if (serial_number != null) {
                deviceSpecificAttributeList.add(SERIAL);
            }


            // Added by Michael Y W Yeung [140228] EPS
            // To get the IMEi as well
            // proceed to get IMEI

            if (imei != null) {
                LOG.info ("get IMEI ***** ");
                deviceSpecificAttributeList.add(IMEI);
            } else {
                // proceed to get IMSI
                imsi = getIMSI(context);
                if (imsi != null) {
                    deviceSpecificAttributeList.add(IMSI);
                } else {
                    // proceed to get Wifi MAC address
                    wifi_mac_address = getWifiMacAddress(context);
                    if (wifi_mac_address != null) {
                        deviceSpecificAttributeList.add(WIFI_MAC_ADDRESS);
                    } else {
                        // last resort, get UUID as in mobile 1.3
                        uuid = getUUID(context);
                        deviceSpecificAttributeList.add(ANDROID_UUID);
                    }
                }
            }
        }
        else {
            // proceed to get IMEI
            imei = getIMEI(context);
            if (imei != null) {
                deviceSpecificAttributeList.add(IMEI);
            } else {
                // proceed to get IMSI
                imsi = getIMSI(context);
                if (imsi != null) {
                    deviceSpecificAttributeList.add(IMSI);
                } else {
                    // proceed to get Wifi MAC address
                    wifi_mac_address = getWifiMacAddress(context);
                    if (wifi_mac_address != null) {
                        deviceSpecificAttributeList.add(WIFI_MAC_ADDRESS);
                    } else {
                        // last resort, get UUID as in mobile 1.3
                        uuid = getUUID(context);
                        deviceSpecificAttributeList.add(ANDROID_UUID);
                    }
                }
            }
        }

        generateMapping(context, deviceSpecificAttributeList); // save to local storage
        deviceSpecificAttributeList = retrieveMapping(context); // ensure to retrieve from local storage
        return deviceSpecificAttributeList;
    }

    /*
     * This method is to rebuild the device specific attribute string
     * from previous selected attributes (the ArrayList object re-constructed
     * from SharedPreference)
     */
    protected static String generateDeviceSpecificAttribute(ArrayList <String> arrayList,Context context) {
        String deviceSpecificAttribute = "";

        String tmp; // temporary local variable
        for (int i=0; i<arrayList.size(); i++) {
            String attributeName = (String) arrayList.get(i);

            if (attributeName.equalsIgnoreCase(ANDROID_ID)) {
                tmp = getAndroidID(context);
                LOG.debug("Getting Android ID " + tmp);
                deviceSpecificAttribute += tmp;

            } else if (attributeName.equalsIgnoreCase(SERIAL)) {
                tmp = getSerialNumber();
                LOG.debug("Getting Serial Number " + tmp);
                deviceSpecificAttribute += tmp;

            } else if (attributeName.equalsIgnoreCase(IMEI)) {
                tmp = getIMEI(context);
                LOG.debug("Getting IMEI " + tmp);
                deviceSpecificAttribute += tmp;

            } else if (attributeName.equalsIgnoreCase(IMSI)) {
                tmp = getIMSI(context);
                LOG.debug("Getting IMSI " + tmp);
                deviceSpecificAttribute += tmp;

            } else if (attributeName.equalsIgnoreCase(WIFI_MAC_ADDRESS)) {
                tmp = getWifiMacAddress(context);
                LOG.debug("Getting Wi-Fi MAC Address " + tmp);
                deviceSpecificAttribute += tmp;

            } else if (attributeName.equalsIgnoreCase(ANDROID_UUID)) {
                tmp = getUUID(context);
                LOG.debug("Getting UUID " + tmp);
                deviceSpecificAttribute += tmp;

            } else {
                //unknown attribute name, cannot be supported
            }
        }

        return deviceSpecificAttribute;
    }

    /*
     * Retrieve the Android Release Version
     */
    public static String getReleaseVersion() {
        String releaseVersion = null;

        // The user-visible version string (Android version)
        // i.e. 4.1.2
        releaseVersion = android.os.Build.VERSION.RELEASE;

        return releaseVersion;
    }

    /*
     * Retrieve the Android SDK Version
     */
    protected static int getSDKVersion() {
        int sdkVersion;

        // The user-visible SDK version of the framework
        // Android 4.1		(API 16)
        // Android 4.0.3	(API 15)
        // Android 4.0		(API 14)
        // Android 3.2		(API 13)
        // Android 3.1		(API 12)
        // Android 3.0		(API 11)
        // Android 2.3.3	(API 10)
        // Android 2.2		(API 8)
        // Android 2.1		(API 7)
        // Android 1.6		(API 4)
        // Android 1.5		(API 3)
        sdkVersion = android.os.Build.VERSION.SDK_INT;

        return sdkVersion;
    }

    /*
     * Return the name of the platform
     */
    protected static String getPlatform() {
        return "Android";
    }

    /*
     * Return the name of the manufacturer
     */
    protected static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /*
     * Return the name of the hardware model
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /*
     * Retrieve the Android ID
     */
    protected static String getAndroidID(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        // convert the android id to null if its blank value
        if (android_id!=null && android_id.trim().equalsIgnoreCase("")) {
            android_id = null;
        }

        return android_id;
    }

    /*
     * Retrieve the Serial Number
     *
     * [IMPORTANT]
     * Serial Number only available since API level 9
     * use java reflection technique to obtain serial number
     */
    protected static String getSerialNumber() {

        String serial = null;

        try {
            Class<?> c = Class.forName("android.os.Build");
            Field field = c.getField("SERIAL");
            serial = (String) field.get(null);

        } catch (Exception exc) {
            LOG.error("Get Serial Number Error", exc);
            serial = null;
        }

        // convert the serial to null if its blank value
        if (serial!=null && serial.trim().equalsIgnoreCase("")) {
            serial = null;
        }

        return serial;
    }

    /*
     * Retrieve the IMEI via getDeviceId() function
     * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
     */
    protected static String getIMEI(Context context) {
        String imei = null;
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        imei = telephonyManager.getDeviceId();

        int phoneType = telephonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_NONE:
                imei = "NONE:" + imei;
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                imei = "GSM:" + imei;
                break;
            case TelephonyManager.PHONE_TYPE_CDMA:
                imei = "MEID/ESN:" + imei;
                break;
    		 /*
    		  *  for API Level 11 or above
    		  *  case TelephonyManager.PHONE_TYPE_SIP:
    		  *  return "SIP";
    		  */
            default:
                imei = "UNKNOWN:" + imei;
                break;
        }

        // convert the IMEI to null if its blank value
        if (imei!=null && imei.trim().equalsIgnoreCase("")) {
            imei = null;
        }

        return imei;
    }

    /*
     * Retrieve the IMSI via getSubscriberId() function
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
     */
    protected static String getIMSI(Context context) {
        String imsi = null;

        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        imsi = telephonyManager.getSubscriberId();

        // convert the IMSI to null if its blank value
        if (imsi!=null && imsi.trim().equalsIgnoreCase("")) {
            imsi = null;
        }

        return imsi;
    }

    /*
     * Retrieve the Wi-Fi MAC address
     */
    protected static String getWifiMacAddress(Context context) {
        String macAddr = null;

        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        macAddr = wifiInfo.getMacAddress();

        // convert the mac address to null if its blank value
        if (macAddr!=null && macAddr.trim().equalsIgnoreCase("")) {
            macAddr = null;
        }

        return macAddr;
    }

    /*
     * Retrieve the Android UUID
     */
    protected static String getUUID(final Context context) {
        // references Header.java
        SharedPreferences prefs = context.getSharedPreferences("HSBCHybridSharedPrefs", Context.MODE_PRIVATE);
        if (!prefs.contains("uuid")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("uuid", UUID.randomUUID().toString());
            editor.commit();
        }
        return prefs.getString("uuid", null);
    }

    /*
     * Store the preferred device specific attribute (just the attribute name, not its value)
     * Note that the device specific attribute can be composed of more than one attribute
     * For example Android ID must combined together with Serial Number
     *
     * Pass in the attribute names in an ArrayList, and use GsonBuilder to convert
     * the ArrayList object into a JSON string for storage into SharedPreferences
     */
    protected static synchronized void generateMapping(final Context context, ArrayList <String> attributeList) {
        SharedPreferences prefs = context.getSharedPreferences("DeviceSpecificAttributeMap", Context.MODE_PRIVATE);

        // Do not overwrite existing value if already exist!
        if (!prefs.contains("DeviceSpecificAttribute")) {
            // Convert the Object into JSON format string

            String gsonStr = JsonUtil.getJsonFromJavaObject(attributeList);
            LOG.debug("Saving DeviceSpecificAttribute" + gsonStr);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("DeviceSpecificAttribute", gsonStr);
            editor.commit();
        }
    }

    /*
     * Retrieve the previous stored device specific attribute
     *
     * The attribute names are stored in the form of JSON string in SharedPreferences,
     * so need to convert back into an ArrayList object using GsonBuilder
     */
    protected static synchronized ArrayList<String> retrieveMapping(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences("DeviceSpecificAttributeMap", Context.MODE_PRIVATE);

        String gsonStr = prefs.getString("DeviceSpecificAttribute", null);
        if (gsonStr != null) {
            // convert the JSON String into original Object
            ArrayList <String> arrayList = JsonUtil.getObjectFromJson(gsonStr, ArrayList.class);
            LOG.debug("Restoring DeviceSpecificAttribute" + arrayList.toString());
            return arrayList;
        } else {
            // if the mapping does NOT exist just return null
            return null;
        }
    }


    public static boolean deviceOnline(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null) {
            LOG.debug("device not on line");
            return false;
        }
        // [17-Sept-2012] As discussed with jeffrey, mobile 1.5 app does not transmit any network data when the app is in background, so checking background setting is not necessary
        //if (!info.isAvailable() || !mConnectivity.getBackgroundDataSetting()) {
        if (!info.isAvailable()|| !info.isConnected()) {
            LOG.debug("device not on line");
            return false;
        } else {

            /**
             * @author CapGemini
             * @description
             * Saving date and time of last net connectivity so that we can check for how many days the user has not connected to net.
             **/

            Locale locale = Locale.getDefault();
            Calendar cal = Calendar.getInstance(locale);
            Long dtMillies = cal.getTimeInMillis();

            SharedPreferences prefDtMillie = context.getSharedPreferences("prefDtMillie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefDtMillie.edit();
            editor.putLong("dtMillies", dtMillies);
            editor.commit();
            // CG ends
            return true;
        }
    }



    public static String getAppVersion(Context context)throws Exception{
        PackageManager pm=context.getPackageManager();
        PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(),0);
        String version= packInfo.versionName;
        return version;
    }
    /**
     * 检测网络是否可用
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!StringUtil.isNotNullAndEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public  static void deleteFile(String path){
        File file=new File(path);
        if(file.exists()){
            file.delete();
        }
    }
}
