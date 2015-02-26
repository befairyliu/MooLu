package com.moolu.hook;

/**
 * Created by Nanan on 2/26/2015.
 */
public class HookConstants {

    public static final String ML_URL_PREFIX = "MooLu://";
    public final static String HOOK_OBJECT = "hook";
    public final static String LOCAL_URL_PREFIX = "file://";
    public final static String MAIL_TO = "mailto";
    public final static String TEL = "tel";

    public final static String CHECKLOGON="checkLogon";
    /** Hook API name ***/
    public final static String OPEN_IN_APP = "OpenInApp";
    public final static String OPEN_IN_BROWSER = "OpenInBrowser";
    public final static String TOGGLE_POST_LOGON_MENU = "TogglePostLogonMenu";
    public static final String TOGGLE_LANGUAGE = "ToggleLanguage";
    public final static String OPEN_DIALOG = "OpenDialog";
    public final static String UPDATE_POST_LOGON_MENU = "UpdatePostLogonMenu";
    public final static String IS_SUPPORTED_FEATURE_INAPP= "IsSupportedFeatureInApp";
    public final static String POPUP_BUBBLE = "PopUpBubble";
    public final static String OPEN_MAINBROWSER_IN_APP = "OpenMainBrowserInApp";
    public final static String UPDATE_MENU_HTML_BANNER = "UpdateMenuHtmlBanner";
    public final static String SCAN_BAR_CODE = "ScanBarCode";
    public final static String UPDATE_MENU_BANNER = "UpdateMenuBanner";
    public final static String PROXY = "Proxy";
    public final static String GSPPROXY = "GSPProxy";
    public final static String MULTIPLE_GSP_PROXY = "MultipleGSPProxy";
    public final static String MULTIPLE_PROXY = "MultipleProxy";//RDC Project : Proxy Connections Scholes 13Jun2013
    public final static String PAGE_TRANSITION = "PageTransition";
    public final static String PAGE_TRANSITION_FORM = "PageTransitionForm";
    public final static String GET_LOCALE = "GetLocale";
    public final static String TurboEngine = "TurboEngine";
    public final static String GET_APP_VERSION = "GetAppVersion";
    public final static String GET_DEVICE_TYPE = "GetDeviceType";
    public final static String GET_DEVICE_INFO = "GetDeviceInfo";
    public final static String GET_APPLICATION_NAME = "GetApplicationName";
    public final static String BACK_TO_APP = "BackToApp";
    public final static String BACK_TO_HOME = "BackToHome";
    public final static String GETTER = "Getter";
    public final static String SETTER = "Setter";
    public final static String MULTIPLE_SETTER = "MultipleSetter";
    public final static String VOLATILEGETTER = "VolatileGetter";
    public final static String VOLATILESETTER = "VolatileSetter";
    public final static String VOLATILESETTERFORTHEMEID = "VolatileSetterForThemeId";
    public final static String GET_COOKIES = "GetCookies";
    public final static String SET_COOKIES = "SetCookies";
    public final static String OPEN_ASSIST_BROWSER_IN_APP = "OpenAssistBrowserInApp";
    public final static String CLOSE_ASSIST_BROWSER_IN_APP = "CloseAssistBrowserInApp";
    public final static String PROXYJSON = "ProxyJSON";
    public final static String SET_HEADER_TITLE = "SetHeaderTitle";
    public final static String COMFIRM_ACCEPTANCE = "ConfirmAcceptance";
    public final static String SWITCH_ENTITY_WITH_SSO = "SwitchEntityWithSSO";
    public final static String SWITCH_WEBVIEW_ACTION = "SwitchWebview";
    public final static String LOGON_LOADING_FINISH = "logonLoadingFinish";
    public final static int OPEN_SSO_RESULT = 3;
    public final static int ACTION_RESULT = 4;
    public final static String MESSAGE_DATA = "data";
    public final static String SET_HEADER = "setHeader";

    // ** Start API name is for SOTP **//
    public final static String GET_DEVICE_ID = "GetDeviceID";

    public final static String SHOW_LOADING_VIEW = "ShowLoadingView";
    public final static String HIDE_LOADING_VIEW = "HideLoadingView";

    public final static String CHECK_NET_WORK = "CheckNetwork";

    public final static String GET_CURRENT_ENTITY_INFO = "GetCurrentEntityInfo";
    public final static String GEN_MOBILE_DOSI = "GenerateMobileDOSI";
    public final static String GET_MOBILE_DOSI = "GetMobileDOSI";
    public final static String GET_RANDOM_NUMBER = "GenerateRandomNumber";
    public final static String GET_GUID = "GetEncryptedGUID";
    public final static String UPDATE_GUID = "UpdateGUID";
    public final static String GET_LOGON_OTP = "GenerateLogonOTP";
    public final static String GET_REAUTH_OTP = "GenerateReauthOTP";
    public final static String GET_TXN_OTP = "GenerateTxnOTP";
    public final static String ACTIVE_XFAD = "ActivateXFAD";
    public final static String GET_APPINFO = "GetAppInfo";
    public final static String RESYNCHORNIZE_TIME = "ResynchronizeTime";
    public final static String IS_SUPPORT_SOTP = "IsSupportSoftOTP";
    public final static String GET_ACTIVATION_ARGUMENTS = "GetActivationArguments";
    public final static String GET_PASSCODE_ARGUMENTS = "GetPasscodeArguments";
    public final static String GET_ENTITYID = "GetEntityID";
    public final static String SHOW_MESSAGE_BOX = "ShowMessageBox";
    public final static String REMOVE_SOTP = "RemoveSoftToken";
    public final static String REMOVE_PROGRESSBAR = "RemoveProgressBar";
    public final static String CHANGE_PASSWORD = "ReencryptSeed";
    public final static String CALL_DIALPAD = "CallDialPad";
    public final static String SET_LOGOFF_FLAG = "SetCurrentLogonState";
    public final static String SET_TOKEN_TYPE = "SetTokenType";
    public final static String GET_TOKEN_TYPE = "GetTokenType";
    public final static String SWITCH_MODULE = "SwitchWebModule";
    public final static String FILE_UPLOAD="FileUpload";
    public final static String POST_PROCESS_END="PostProcessEnd";
    public final static String CAPTURE_IMAGE = "CaptureImage";
    /**Switch language done hook**/
    public final static String TOGGLE_SERVER_LANGUAGE_END = "ToggleServerLanguageEnd";
    public final static String CHECK_WEB_MODULE_RESOURCE = "CheckWebModuleResource";
    public final static String SWITCH_SWIPE_GESTURE = "SwitchSwipeGesture";
    public final static String SHOULD_KEEP_ALIVE_WITH_PERIOD = "ShouldKeepAliveWithPeriod";
    public final static String UPDATE_KEEP_ALIVE_FLAG = "UpdateKeepAliveFlag";
    public final static String LOAD_DETAILS_WEB_VIEW = "LoadDetailsWebView";
    public final static String CLOSE_DETAILS_WEB_VIEW = "CloseDetailsWebView";

    /* Error code Start */
    /* Error code Start */
    // ** End API for SOTP **//

    // JW [Mar-2013] SGH Timeout Re-auth, Hook API name //
    public final static String SET_SESSION_COUNTDOWN_TIMER= "SetSessionCountdownTimer";
    public final static String REGISTER_BG_TO_FG_EVENT = "RegisterBackToForeEvent";
    public final static String CLEAR_SESSION_COUNTDOWN_TIMER = "ClearSessionCountdownTimer";
    public final static String UNREGISTER_BG_TO_FG_EVENT = "UnregisterBackToForeEvent";
    public final static String SHOW_OVERLAY = "ShowOverlay";
    public final static String REMOVE_OVERLAY = "RemoveOverlay";
    // ** End Hook API name for SGH Timeout Re-auth **//
    /** Hook API parameter name ***/
    public final static String FUNCTION = "function";
    public final static String URL = "url";
    public final static String NEEDCUSTOM_STYLE= "needCustomStyle";
    public final static String CALLBACK_JS = "callback";
    //Tracy added CLOSE_CALLBACK_JS for oadDetailsWebView hook [18-Sep-2013]
    public final static String CLOSE_CALLBACK_JS = "closejs";
    public final static String EVENT_JS = "eventjs";
    public final static String HEADERS = "headers";
    public final static String DATA_JS = "datajs";
    public final static String SLIDE = "slide";
    public final static String IMAGE_LINK = "imglink";
    public final static String APPLOCALE = "locale";
    public final static String ONCLICK_URL = "onclickurl";
    public final static String METHOD = "method";
    public final static String TASKID = "taskId";//RDC Project : Proxy Connections Scholes 13Jun2013
    public final static String ID = "id";
    public final static String ENTITY_SHORTNAME="shortname";
    public final static String LANG = "lang";
    public final static String KEY = "key";
    public final static String VALUE = "value";
    public final static String ALERT_HOOK_FUNCTION = "function=";
    public final static String FEATURE_NAME="featureName";
    public final static String RESULT = "result";
    public final static String DVICE_TYPE = "A";
    public final static String REGISTER_MENU_STATUS_EVENT = "RegisterMenuStatusEvent";
    public final static String UNREGISTER_MENU_STATUS_EVENT = "UnregisterMenuStatusEvent";
    public final static String SHOW_LOGOFF_DIALOG = "ShowLogoffDialog";
    /** for PageTransitionForm API*/
    public final static String DATA_URL="dataurl";
    public final static String REQUEST_DATA="requestData";

    public final static int NO_SLIDE = 0;
    public final static int SHOW_PROGRESS_MSG = 1;
    public final static int HIDE_PROGRESS_MSG = 2;
    public final static int OPEN_MAINBROWSER_INAPP = 3;
    public final static int OPEN_SOTP_STANALON_PROGRESS_MSG = 20;
    public final static int OPEN_SOTP_INT_PROGRESS_MSG = 21;
    public final static int HIDE_SOTP_PROGRESS_MSG = 22;
    public final static int ALERT_FOR_NEW_MSG = 4;
    public final static int UPDATE_POST_LOGON_MENU_MSG = 5;
    public final static int TOGGLE_POST_LOGON_MENU_MSG = 6;
    public final static int SLIDE_L_MSG = 7;
    public final static int SLIDE_R_MSG = 8;
    public final static int UPDATE_MENU_BANNER_MSG = 9;
    public final static int NO_SUCH_JAVASCRIPT = 10;
    public final static int HOOK_ERROR = 11;
    public final static int SET_HEADER_TITLE_MSG = 12;
    public final static int ENABLE_LOGOFF_BUTTON_MSG = 13;
    public final static int UPDATE_MENU_HTML_BANNER_URL = 15;
    public final static int TOGGLE_LANGUAGE_MSG = 16;
    public final static int EXECUTE_JAVASCRIPT = 17;
    public final static int SWITCH_WEBVIEW = 18;
    public final static int SWITCH_LOCALE = 19;
    public final static int ANIMATION_BACK_MSG = 20;
    //For show webView
    public final static int LOGON_LOADING_FINISH_MSG = 21;
    // Project Cobra [JW] Jul-2013
    public final static int WEBVIEW_LOAD_URL_ERROR = 22;
    public final static int WEBVIEW_LOAD_URL_FINISH = 23;
    public final static int REGISTER_MENU_STATUS_EVENT_MSG = 24;
    public final static int UNREGISTER_MENU_STATUS_EVENT_MSG = 25;
    public final static int SHOW_LOGOFF_DIALOG_MSG = 26;
    //for client pack download performance enhancement
    //Tracy wang added [28 Oct 2013]
    public final static int RESOURCE_PROCESSOR_FINISH_FOR_HOME = 27;
    public final static int NO_NETWORK_N_DAY_ALERT_FINISH = 28;
    // MACHAEL ADDED START - for SOTO issue.
    public final static int FORCE_DISABLE_MENU = 29;
    // MACHAEL ADDED END - for SOTO issue.

    // added start for UI enhancement show pad lock in menu [6 Mar 2014]
    public final static int SET_MENU_STATUS_MSG = 30;
    public final static int SET_MENU_HIGHLIGHT_MSG = 31;
    // added end

    public final static int SPECIAL_ANNOUNCEMENT_FINISH = 32;
    public final static int FUNCTIONAL_ONBOARDING_PAGE_FNISH= 33;

    // added start for UI enhancement hide splash page after onboarding apge close in menu [Jun 2014]
    public final static int HOME_ONBOARDING_FINISH_MSG= 34;

    public final static int SHOW_LOADING_VIEW_MSG = 35;
    public final static int HIDE_LOADING_VIEW_MSG = 36;
    public final static int SHOW_NETWORK_ERROR_DIALOG = 37;

    /** Animation value ***/
    public final static String SLIDE_L = "RTL";
    public final static String SLIDE_R = "LTR";

    /*** for action ***/
    public final static String JAVASCRIPT = "javascript:";
    public final static String QUOTA = "\"";

    public final static String TIMEOUT_ERROR_CODE = "P001";
    public final static String OTHER_ERROR_CODE = "P002";
    public final static String API_EXCUTE_ERROR_CODE = "P003";
    public final static String NETWORK_ERROR_CODE = "P004";
    public final static String SUCCESS = "0000";
    public final static String API_FAIL_CALL_FUNC = "hookAPIFailCall";

    public final static String CHANGE_LOGO_PREMIER = "premier";
    public final static String CHANGE_LOGO_ADVANCE = "advance";
    public final static String CHANGE_LOGO_DEFAULT = "default";
    public final static String CHANGE_LOGO_HSBC = "hsbc";
    public final static String CHANGE_LOGO_HSBCNET = "hsbcnet";
    public final static String CHANGE_LOGO_NONE = "none";
    public final static String CHANGE_LOGO_SABB = "sabb";

    public static final String DEVICE_STATUS = "devicestatus";

    //Key for mappting the Theme ID.
    public final static String KEY_CUSTID="custId";

    // Parameter added for implementing SOTP

    // parameter set fo BG Image Implementation.
    public final static String GETMOBILETHEMEID = "GetMobileThemeId";
    public final static String KEY_THEMEID = "themeId";
    public final static String KEY_CUSTOMER_TYPE_DEPENDENT_BACKGROUND = "customerTypeDependentBackground";

    // Parameters related to SOTP Functionality
    public final static String IS_TIMESYNC_REQUIRED ="isSyncronizeTimerequired";
    public final static String NETWORK_TYPE_REACHABLE_VIA_WWAN ="Cellular";
    public final static String NETWORK_TYPE_REACHABLE_VIA_WIFI = "WiFi";
    public final static String NETWORK_TYPE_NOTREACHABLE =null;
    public final static String NETWORK_TYPE="networkType";

    // Key for the implementation of tehe Proposition Based BG Image
    public final static String APP_REMEMBER_CUSTINFO = "appRemmeberCustInfo";
    public final static String REGION_SPECIFIC_CUSTOMERTYPE_DEPENDENT_BACKGROUND = "regionSpecificCustomerTypeDependentBackground";

    //P2P start
    public final static String OPEN_CONTACT_LIST = "OpenContactList";
    public final static String ACCOUNT_PROXY_TYPE = "accountProxyType";
    public final static String ACCOUNT_PROXY_VALUE = "accountProxyValue";
    public final static String REQ_PARAM_MISSING = "request parameter missing";
    public final static String HOOK_NOT_EXECUTED = "Fail to execute the hook:{}";

    // added for p2p payment 2014-5-12
    public final static String GET_P2P_RECIPIENT_LIST = "SearchContactListByKey";
    public final static String P2P_RECIPIENT_INFO = "recipientInfo";
    //P2P end

    //Added for getselected payment option
    public final static String SELECTED_PAYMENTTYPE = "paymentType";

    //added for UI enhancement
    public final static String IS_PADLOCKED = "isPadlocked";
    public final static String SET_MENU_STATUS = "SetMenuStatus";
    public final static String SET_MENU_HIGHLIGHT = "SetMenuHighlight";
    public final static String MENU_ID = "menuId";
    public final static String OPEN_ONBOARDING_PAGE = "OpenOnBoardingPage";
    public final static String CHANGE_ONBOARDING_PAGE_BUTTON_STYLE = "ChangeOnBoardingPageButtonStyle";
}
