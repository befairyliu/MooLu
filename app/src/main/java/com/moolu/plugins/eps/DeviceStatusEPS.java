package com.moolu.plugins.eps;

import java.io.UnsupportedEncodingException;

/**
 * This class stores all the status variables and getter / setter methods.
 * Created by Nanan on 3/4/2015.
 */
public class DeviceStatusEPS {
    private boolean statusCalculatedSuccessfully = false;
    private String osVersion = "";
    private int riskScore = 0;
    private int configUpdateScore = 0;
    private int osRiskScore = 0;
    private boolean rooted = false;
    private boolean malwarePresent = false;
    private String malware = "";
    private boolean playOnly = true;
    private int suspicousSystemConf = 0;

    private String SDKversion = "";
    private String deviceKey = "";

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"")
                .append(DeviceStatusConstants.EPS_SUCCESSFUL)
                .append("\":\"")
                .append(statusCalculatedSuccessfully).append("\"");
        if (statusCalculatedSuccessfully == false) {
            addField(stringBuffer, DeviceStatusConstants.RISK_SCORE, "");
            addField(stringBuffer, DeviceStatusConstants.OS_RISK_SCORE, "");
            addField(stringBuffer, DeviceStatusConstants.CONFIG_UPDATE_SCORE, "");
            addField(stringBuffer, DeviceStatusConstants.ROOTED, "");
            addField(stringBuffer, DeviceStatusConstants.MALWARE_PRESENT, "");
            addField(stringBuffer, DeviceStatusConstants.MALWARE, "");
            addField(stringBuffer, DeviceStatusConstants.PLAY_STORE_ONLY, "");
            addField(stringBuffer, DeviceStatusConstants.SUSPICIOUS_SYSTEM_CONF, "");
            addField(stringBuffer, DeviceStatusConstants.SDK_VERSION, "");
            addField(stringBuffer, DeviceStatusConstants.DEVICE_KEY, "");
        } else {
            addField(stringBuffer, DeviceStatusConstants.RISK_SCORE, getRiskScore());
            addField(stringBuffer, DeviceStatusConstants.OS_RISK_SCORE, getOsRiskScore());
            addField(stringBuffer, DeviceStatusConstants.CONFIG_UPDATE_SCORE, getConfigUpdateScore());
            addField(stringBuffer, DeviceStatusConstants.ROOTED, getRooted());
            addField(stringBuffer, DeviceStatusConstants.MALWARE_PRESENT, getMalwarePresent());
            addField(stringBuffer, DeviceStatusConstants.MALWARE, getMalware());
            addField(stringBuffer, DeviceStatusConstants.PLAY_STORE_ONLY, getPlayOnly());
            addField(stringBuffer, DeviceStatusConstants.SUSPICIOUS_SYSTEM_CONF, hasSuspicousSysConfig());
            addField(stringBuffer, DeviceStatusConstants.SDK_VERSION, getSDKversion());
            addField(stringBuffer, DeviceStatusConstants.DEVICE_KEY, getDeviceKey());
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    public static void addField(StringBuffer stringBuffer, String key, Object value) {
        stringBuffer.append(",\"").append(key).append("\":\"").append(value).append("\"");
    }

    public static byte[] reverseBytes(final String in) {
        byte[] bytes;
        try {
            bytes = in.getBytes(DeviceStatusConstants.CHARSET);
            byte[] outBytes = new byte[bytes.length];

            for (int i = 0; i < bytes.length; i++) {
                outBytes[i] = (byte) (Integer.reverse(bytes[i]) >>> (Integer.SIZE - Byte.SIZE));
            }
            return outBytes;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public void setStatusCalcSuccess(Boolean status) {
        this.statusCalculatedSuccessfully = status;
    }

    public Boolean getStatusCalcSuccess() {
        return this.statusCalculatedSuccessfully;
    }

    public void setOSVersion(String version) {
        this.osVersion = version;
    }

    public String getOSVersion() {
        return this.osVersion;
    }

    public void setRiskScore(int score) {
        this.riskScore = score;
    }

    public int getRiskScore() {
        return this.riskScore;
    }

    public void setConfigUpdateScore(int score) {
        this.configUpdateScore = score;
    }

    public int getConfigUpdateScore() {
        return this.configUpdateScore;
    }

    public void setOsRiskScore(int score) {
        this.osRiskScore = score;
    }

    public int getOsRiskScore() {
        return this.osRiskScore;
    }

    public void setRooted(Boolean rooted) {
        this.rooted = rooted;
    }

    public Boolean getRooted() {
        return this.rooted;
    }

    public void setMalwarePresent(Boolean present) {
        this.malwarePresent = present;
    }

    public Boolean getMalwarePresent() {
        return this.malwarePresent;
    }

    public void setMalware(String malwareList) {
        this.malware = malwareList;
    }

    public String getMalware() {
        return this.malware;
    }

    public void setPlayOnly(Boolean isPlayOnly) {
        this.playOnly = isPlayOnly;
    }

    public Boolean getPlayOnly() {
        return this.playOnly;
    }

    public void setSuspicousSysConfig(int suspicous) {
        this.suspicousSystemConf = suspicous;
    }

    public int hasSuspicousSysConfig() {
        return this.suspicousSystemConf;
    }

    public void setSDKversion (String version) {
        this.SDKversion = version;
    }

    public String getSDKversion() {
        return this.SDKversion;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getDeviceKey() {
        return this.deviceKey;
    }
}
