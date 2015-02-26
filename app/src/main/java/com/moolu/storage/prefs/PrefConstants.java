package com.moolu.storage.prefs;

/**
 * Created by Nanan on 2/4/2015.
 */
public final class PrefConstants {

    public final static String PREFS_NAME = "MooLuybridSharedPrefs";
    public final static String SOTP_PREFS_NAME = "SOTPValueStore";
    public final static String LAST_MOD_KEY = "lastModified";
    public final static String UUID_KEY = "uuid";
    public final static String VERSION = "version";

    public final static String CHECKSUN_KEY = "checksum";
    public final static String CHECKING_TIME_STAMP="checking_time_stamp";
    public final static String CHECKING_HOME_BANNER_TIME_STAMP="home_banner";
    public final static String CONFIG_CHECKING_TIME_STAMP="config_checking_time_stamp";
    public final static String CONFIGURATION="configuration";

    public final static String APP_ID_KEY = "app_id";
    public final static String APP_SHORT_NAME_KEY = "app_short_name";

    public final static String LOCALE = "locale";
    public final static String ENTITY_PATH = "entity_path";
    public final static String BACKUP_ENTITY_PATH = "backup_entity_path";

    public final static String CHECKSUM_LAST_MODIFIED = "checksumLastModified";

    //public final static String EULA_VERSION = "eula_version";
    public final static String EULA_APPVERSION = "eula_appVersion";

    public final static String USER_TYPE_MASS = "mass";
    public final static String USER_TYPE_ADVANCE = "advance";
    public final static String USER_TYPE_PREMIER = "premier";
    public final static String USER_TYPE_PB = "pvb";
    public final static String HSBCNET_TYPE_PREFIX = "hsbcnet";

    public final static String MASS_IMAGE_PORTRAIT_2x = "mass_portrait_S.jpg";
    public final static String ADVANCE_IMAGE_PORTRAIT_2x = "advance_portrait_S.jpg";
    public final static String PVB_IMAGE_PORTRAIT_2x = "pb_portrait_S.jpg";
    public final static String PREMIER_IMAGE_PORTRAIT_2x = "premier_portrait_S.jpg";

    public final static String MASS_IMAGE_LANDSCAPE_2x = "mass_landscape_T.jpg";
    public final static String ADVANCE_IMAGE_LANDSCAPE_2x = "advance_landscape_T.jpg";
    public final static String PVB_IMAGE_LANDSCAPE_2x = "pb_landscape_T.jpg";
    public final static String PREMIER_IMAGE_LANDSCAPE_2x = "premier_landscape_T.jpg";

    public final static String EULA_ACCEPTED_VERSION = "eula_version";
    public final static String EULA_ACCEPTED_LOCALE = "eula_accepted_locale";
    public final static String EULA_ACCEPTED_READ_URL = "eula_accepted_read_url";
    public final static String EULA_CONT_VERSION = "eula_cont_version";
    public final static String EULA_CONT_NOTICE_PERIOD = "eula_cont_notice_period";

    public final static String PAGE_EQ_READ = "page=read";
    public final static String UI_EQ_A = "&ui=a";

    /**
     * Constant class, private constructor
     */
    private PrefConstants() {}

    /**
     * @author capgemini [9 Oct 13]
     * @description Manage Application Theme (Background Image) based on CustomerSegment Type for the logged in User.
     */
    public final static String APPLICATION_TEME_ID = "AppCustomerTypeBackground";

    public final static String ORIENTATION_PORTRAIT = "portrait";
    public final static String ORIENTATION_LANDSCAPE = "landscape";
    public final static String IS_THEMECHANGE_ALLOWED ="is_theme_change";

    public final static String NETWORK_TYPE_REACHABLE_VIA_WWAN ="ReachableViaWWAN";
    public final static String NETWORK_TYPE_REACHABLE_VIA_WIFI = "ReachableViaWiFi";
    public final static String NETWORK_TYPE_NOTREACHABLE ="NotReachable";

    /**
     * @author capgemini [7 Oct 13]
     * @description Flag to Manage the client pack download skip count
     */
    public final static String DOWNLOAD_SKIP_COUNT ="download_skip_count";
    public final static int MAX_DOWNLOAD_SKIP_LIMIT = 5;

    public final static String MASS_IMAGE = "mass_portrait.jpg";
    public final static String ADVANCE_IMAGE = "advance_portrait.jpg";
    public final static String PREMIER_IMAGE = "premier_portrait.jpg";
    /**
     * Home logo RTL support. Suffix of the logo image.
     */
    public final static String RTL_SUFFIX = "_rtl";

    public final static int WEBRESOURCE_REF = 1;
    public final static int PREPATE_UNZIP_RESOURCE_TASK_REF=2;
    public final static int PREPATE_DOWNLOAD_APK_TASK_REF=3;
    public final static int DOWNLOAD_PROGRESS_UPDATE_MSG=1001;
}
