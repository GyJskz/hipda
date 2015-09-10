package net.jejer.hipda.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import net.jejer.hipda.utils.Connectivity;
import net.jejer.hipda.utils.NotificationMgr;
import net.jejer.hipda.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HiSettingsHelper {
    /*
     *
     * NOTE! PLEASE LINE-UP WITH PREFERENCE.XML
     *
     * */
    public static final String PERF_USERNAME = "PERF_USERNAME";
    public static final String PERF_PASSWORD = "PERF_PASSWORD";
    public static final String PERF_UID = "PERF_UID";
    public static final String PERF_SECQUESTION = "PERF_SECQUESTION";
    public static final String PERF_SECANSWER = "PERF_SECANSWER";
    public static final String PERF_SHOWSTICKTHREADS = "PERF_SHOWSTICKTHREADS";
    public static final String PERF_SHOW_POST_TYPE = "PERF_SHOW_POST_TYPE";
    public static final String PERF_LOADIMGONMOBILENWK = "PERF_LOADIMGONMOBILENWK";
    public static final String PERF_THREADLISTAVATAR = "PERF_THREADLISTAVATAR";
    public static final String PERF_SORTBYPOSTTIME_BY_FORUM = "PERF_SORTBYPOSTTIME_BY_FORUM";
    public static final String PERF_ADDTAIL = "PERF_ADDTAIL";
    public static final String PERF_TAILTEXT = "PERF_TAILTEXT";
    public static final String PERF_TAILURL = "PERF_TAILURL";
    public static final String PERF_THEME = "PERF_THEME";
    public static final String PERF_NAVBAR_COLORED = "PERF_NAVBAR_COLORED";
    public static final String PERF_FONT = "PERF_FONT";
    public static final String PERF_FORUMS = "PERF_FORUMS";
    public static final String PERF_ENCODEUTF8 = "PERF_ENCODEUTF8";
    public static final String PERF_BLANKLIST_USERNAMES = "PERF_BLANKLIST_USERNAMES";
    public static final String PERF_TEXTSIZE_POST_ADJ = "PERF_TEXTSIZE_POST_ADJ";
    public static final String PERF_TEXTSIZE_TITLE_ADJ = "PERF_TEXTSIZE_TITLE_ADJ";
    public static final String PERF_SCREEN_ORIENTATION = "PERF_SCREEN_ORIENTATION";
    public static final String PERF_GESTURE_BACK = "PERF_GESTURE_BACK";
    public static final String PERF_LAST_UPDATE_CHECK = "PERF_LAST_UPDATE_CHECK";
    public static final String PERF_AUTO_UPDATE_CHECK = "PERF_AUTO_UPDATE_CHECK";
    public static final String PERF_ABOUT = "PERF_ABOUT";
    public static final String PERF_MAX_POSTS_IN_PAGE = "PERF_MAX_POSTS_IN_PAGE";
    public static final String PERF_POST_LINE_SPACING = "PERF_POST_LINE_SPACING";
    public static final String PERF_LAST_FORUM_ID = "PERF_LAST_FORUM_ID";
    public static final String PERF_ERROR_REPORT_MODE = "PERF_ERROR_REPORT_MODE";
    public static final String PERF_INSTALLED_VERSION = "PERF_INSTALLED_VERSION";
    public static final String PERF_CLEAR_CACHE = "PERF_CLEAR_CACHE";
    public static final String PERF_NOTI_TASK_ENABLED = "PERF_NOTI_TASK_ENABLED";
    public static final String PERF_NOTI_REPEAT_MINUETS = "PERF_NOTI_REPEAT_MINUETS";
    public static final String PERF_NOTI_LED_LIGHT = "PERF_NOTI_LED_LIGHT";
    public static final String PERF_NOTI_SOUND = "PERF_NOTI_SOUND";
    public static final String PERF_NOTI_FLOAT_BUTTON = "PERF_NOTI_FLOAT_BUTTON";
    public static final String PERF_NOTI_SILENT_MODE = "PERF_NOTI_SILENT_MODE";
    public static final String PERF_NOTI_SILENT_BEGIN = "PERF_NOTI_SILENT_BEGIN";
    public static final String PERF_NOTI_SILENT_END = "PERF_NOTI_SILENT_END";

    private Context mCtx;
    private SharedPreferences mSharedPref;

    private String mUsername = "";
    private String mPassword = "";
    private String mSecQuestion = "";
    private String mSecAnswer = "";
    private String mUid = "";

    private boolean mShowStickThreads = false;
    private boolean mShowPostType = false;
    private boolean mLoadImgOnMobileNwk = true;
    private boolean mShowThreadListAvatar = true;
    private Set<String> mSortByPostTimeByForum;

    private boolean mAddTail = true;
    private String mTailText = "";
    private String mTailUrl = "";

    private String mTheme = "";
    private boolean mNavBarColor = false;
    private String mFont = "";
    private Set<String> mForums = new HashSet<>();

    private boolean mEncodeUtf8 = false;

    private List<String> mBlanklistUsernames = new ArrayList<>();

    private String mPostTextSizeAdj = "";
    private int mPostLineSpacing = 0;
    private String mTitleTextSizeAdj = "";
    private int mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
    private boolean mGestureBack = true;
    private int mMaxPostsInPage;
    private int mLastForumId = 0;
    private boolean mErrorReportMode;

    private boolean mNotiTaskEnabled;
    private int mNotiRepeatMinutes;
    private boolean mNotiLedLight;
    private boolean mNotiFloatButton;

    // --------------- THIS IS NOT IN PERF -----------
    private boolean mIsLandscape = false;

    public void setIsLandscape(boolean landscape) {
        mIsLandscape = landscape;
    }

    public boolean getIsLandscape() {
        return mIsLandscape;
    }

    private static boolean mMobileNetwork;

    public static void setMobileNetwork(boolean mobileNetwork) {
        mMobileNetwork = mobileNetwork;
    }

    public static boolean isMobileNetwork() {
        return mMobileNetwork;
    }

    public boolean isLoadImage() {
        return !isMobileNetwork() || isLoadImgOnMobileNwk();
    }

    public boolean isLoadAvatar() {
        return !isMobileNetwork() || isShowThreadListAvatar();
    }

    public static void updateMobileNetworkStatus(Context context) {
        if (context != null && Connectivity.isConnected(context))
            setMobileNetwork(!Connectivity.isConnectedWifi(context));
    }

    private long mLastCheckSmsTime;

    public long getLastCheckSmsTime() {
        return mLastCheckSmsTime;
    }

    public void setLastCheckSmsTime(long lastCheckSmsTime) {
        mLastCheckSmsTime = lastCheckSmsTime;
    }

    public boolean isCheckSms() {
        return System.currentTimeMillis() > mLastCheckSmsTime + 30 * 1000;
    }

    // --------------- THIS IS NOT IN PERF -----------


    private HiSettingsHelper() {
    }

    private static class SingletonHolder {
        public static final HiSettingsHelper INSTANCE = new HiSettingsHelper();
    }

    public static HiSettingsHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context ctx) {
        mCtx = ctx;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mCtx);
        reload();
    }

    public boolean ready() {
        return mCtx != null && mSharedPref != null;
    }

    public void reload() {
        getUsernameFromPref();
        getPasswordFromPref();
        getUidFromPref();
        getSecQuestionFromPref();
        getSecAnswerFromPref();
        isShowStickThreadsFromPref();
        isLoadImgOnMobileNwkFromPref();
        isShowThreadListAvatarFromPref();
        isSortByPostTimeByForumFromPref();
        isAddTailFromPref();
        getTailTextFromPref();
        getTailUrlFromPref();
        getThemeFromPref();
        isNavBarColoredFromPref();
        getFontFromPref();
        isEncodeUtf8FromPref();
        getBlanklistUsernamesFromPref();
        getPostTextsizeAdjFromPref();
        getTitleTextsizeAdjFromPref();
        getScreenOrietationFromPref();
        isGestureBackFromPref();
        getPostLineSpacingFromPref();
        getLastForumIdFromPerf();
        isShowPostTypeFromPref();
        isErrorReportModeFromPref();
        getForumsFromPref();
        isNotiFloatButtonFromPref();
        isNotiLedLightFromPref();
        isNotiTaskEnabledFromPref();
        getNotiRepeatMinutesFromPref();

        updateMobileNetworkStatus(mCtx);
    }

    public boolean isLoginInfoValid() {
        return (!mUsername.isEmpty() && !mPassword.isEmpty());
    }

    public String getUsername() {
        return mUsername;
    }

    public String getUsernameFromPref() {
        mUsername = mSharedPref.getString(PERF_USERNAME, "");
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_USERNAME, username).commit();
    }

    public String getPassword() {
        return mPassword;
    }

    public String getPasswordFromPref() {
        mPassword = mSharedPref.getString(PERF_PASSWORD, "");
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_PASSWORD, password).commit();
    }

    public String getUid() {
        return mUid;
    }

    public String getUidFromPref() {
        mUid = mSharedPref.getString(PERF_UID, "");
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_UID, uid).commit();
    }

    public String getSecQuestion() {
        return mSecQuestion;
    }

    public String getSecQuestionFromPref() {
        mSecQuestion = mSharedPref.getString(PERF_SECQUESTION, "");
        return mSecQuestion;
    }

    public void setSecQuestion(String secQuestion) {
        mSecQuestion = secQuestion;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_SECQUESTION, secQuestion).commit();
    }

    public String getSecAnswer() {
        return mSecAnswer;
    }

    public String getSecAnswerFromPref() {
        mSecAnswer = mSharedPref.getString(PERF_SECANSWER, "");
        return mSecAnswer;
    }

    public void setSecAnswer(String secAnswer) {
        mSecAnswer = secAnswer;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_SECANSWER, secAnswer).commit();
    }

    public boolean isShowPostType() {
        return mShowPostType;
    }

    public boolean isShowPostTypeFromPref() {
        mShowPostType = mSharedPref.getBoolean(PERF_SHOW_POST_TYPE, false);
        return mShowPostType;
    }

    public void setShowPostType(boolean showPostType) {
        mShowPostType = showPostType;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_SHOW_POST_TYPE, showPostType).commit();
    }

    public boolean isShowStickThreads() {
        return mShowStickThreads;
    }

    public boolean isShowStickThreadsFromPref() {
        mShowStickThreads = mSharedPref.getBoolean(PERF_SHOWSTICKTHREADS, false);
        return mShowStickThreads;
    }

    public void setShowStickThreads(boolean showStickThreads) {
        mShowStickThreads = showStickThreads;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_SHOWSTICKTHREADS, showStickThreads).commit();
    }

    public boolean isShowThreadListAvatar() {
        return mShowThreadListAvatar;
    }

    public boolean isShowThreadListAvatarFromPref() {
        mShowThreadListAvatar = mSharedPref.getBoolean(PERF_THREADLISTAVATAR, true);
        return mShowThreadListAvatar;
    }

    public void setShowThreadListAvatar(boolean showThreadListAvatar) {
        mShowThreadListAvatar = showThreadListAvatar;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_THREADLISTAVATAR, showThreadListAvatar).commit();
    }

    public boolean isLoadImgOnMobileNwk() {
        return mLoadImgOnMobileNwk;
    }

    public boolean isLoadImgOnMobileNwkFromPref() {
        mLoadImgOnMobileNwk = mSharedPref.getBoolean(PERF_LOADIMGONMOBILENWK, true);
        return mLoadImgOnMobileNwk;
    }

    public void setLoadImgOnMobileNwk(boolean loadImgOnMobileNwk) {
        mLoadImgOnMobileNwk = loadImgOnMobileNwk;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_LOADIMGONMOBILENWK, loadImgOnMobileNwk).commit();
    }

    public boolean isSortByPostTime(int fid) {
        return mSortByPostTimeByForum.contains(fid + "");
    }

    public void setSortByPostTime(int fid, boolean sortByPostTime) {
        if (sortByPostTime) {
            if (!mSortByPostTimeByForum.contains(fid + ""))
                mSortByPostTimeByForum.add(fid + "");
        } else {
            mSortByPostTimeByForum.remove(fid + "");
        }
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.remove(PERF_SORTBYPOSTTIME_BY_FORUM).apply();
        editor.putStringSet(PERF_SORTBYPOSTTIME_BY_FORUM, mSortByPostTimeByForum).apply();
    }

    public Set<String> isSortByPostTimeByForumFromPref() {
        mSortByPostTimeByForum = mSharedPref.getStringSet(PERF_SORTBYPOSTTIME_BY_FORUM, new HashSet<String>());
        return mSortByPostTimeByForum;
    }

    public boolean isAddTail() {
        return mAddTail;
    }

    public boolean isAddTailFromPref() {
        mAddTail = mSharedPref.getBoolean(PERF_ADDTAIL, true);
        return mAddTail;
    }

    public void setAddTail(boolean addTail) {
        mAddTail = addTail;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_ADDTAIL, addTail).commit();
    }

    public String getTailText() {
        return mTailText;
    }

    public String getTailTextFromPref() {
        mTailText = mSharedPref.getString(PERF_TAILTEXT, "");
        return mTailText;
    }

    public void setTailText(String tailText) {
        mTailText = tailText;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_TAILTEXT, tailText).commit();
    }

    public String getTailUrl() {
        return mTailUrl;
    }

    public String getTailUrlFromPref() {
        mTailUrl = mSharedPref.getString(PERF_TAILURL, "");
        return mTailUrl;
    }

    public void setTailUrl(String tailUrl) {
        mTailUrl = tailUrl;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_TAILURL, tailUrl).commit();
    }

    public String getTheme() {
        return mTheme;
    }

    public String getThemeFromPref() {
        mTheme = mSharedPref.getString(PERF_THEME, "light");
        return mTheme;
    }

    public void setTheme(String theme) {
        mTheme = theme;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_THEME, theme).commit();
    }

    public String getFont() {
        return mFont;
    }

    public String getFontFromPref() {
        mFont = mSharedPref.getString(PERF_FONT, "");
        return mFont;
    }

    public void setFont(String font) {
        mFont = font;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(mFont, font).commit();
    }

    public boolean isNavBarColored() {
        return mNavBarColor;
    }

    public boolean isNavBarColoredFromPref() {
        mNavBarColor = mSharedPref.getBoolean(PERF_NAVBAR_COLORED, false);
        return mNavBarColor;
    }

    public void setNavBarColored(boolean navBarColored) {
        mNavBarColor = navBarColored;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_NAVBAR_COLORED, navBarColored).commit();
    }

    public Set<String> getForums() {
        return mForums;
    }

    public Set<String> getForumsFromPref() {
        mForums = mSharedPref.getStringSet(PERF_FORUMS, new HashSet<String>());
        return mForums;
    }

    public void setForums(Set<String> forums) {
        mForums = forums;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.remove(PERF_FORUMS).apply();
        editor.putStringSet(PERF_FORUMS, forums).apply();
    }

    public boolean isEncodeUtf8() {
        return mEncodeUtf8;
    }

    public boolean isEncodeUtf8FromPref() {
        mEncodeUtf8 = mSharedPref.getBoolean(PERF_ENCODEUTF8, false);
        return mEncodeUtf8;
    }

    public void setEncodeUtf8(boolean encodeUtf8) {
        mEncodeUtf8 = encodeUtf8;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_ENCODEUTF8, encodeUtf8).commit();
    }

    public String getEncode() {
        if (mEncodeUtf8) {
            return "UTF-8";
        } else {
            return "GBK";
        }
    }

    public boolean isErrorReportMode() {
        return mErrorReportMode;
    }

    public void setErrorReportMode(boolean errorReportMode) {
        mErrorReportMode = errorReportMode;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_ERROR_REPORT_MODE, errorReportMode).commit();
    }

    public boolean isErrorReportModeFromPref() {
        mErrorReportMode = mSharedPref.getBoolean(PERF_ERROR_REPORT_MODE, false);
        return mErrorReportMode;
    }

    public boolean isNotiTaskEnabled() {
        return mNotiTaskEnabled;
    }

    public void setNotiTaskEnabled(boolean notiTaskEnabled) {
        mNotiTaskEnabled = notiTaskEnabled;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_NOTI_TASK_ENABLED, mNotiTaskEnabled).apply();
    }

    public boolean isNotiTaskEnabledFromPref() {
        mNotiTaskEnabled = mSharedPref.getBoolean(PERF_NOTI_TASK_ENABLED, false);
        return mNotiTaskEnabled;
    }

    public boolean isNotiLedLight() {
        return mNotiLedLight;
    }

    public void setNotiLedLight(boolean notiLedLight) {
        mNotiLedLight = notiLedLight;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_NOTI_LED_LIGHT, mNotiLedLight).apply();
    }

    public boolean isNotiLedLightFromPref() {
        mNotiLedLight = mSharedPref.getBoolean(PERF_NOTI_LED_LIGHT, true);
        return mNotiLedLight;
    }

    public boolean isNotiFloatButton() {
        return mNotiFloatButton;
    }

    public void setNotiFloatButton(boolean notiFloatButton) {
        mNotiFloatButton = notiFloatButton;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_NOTI_FLOAT_BUTTON, mNotiFloatButton).apply();
    }

    public boolean isNotiFloatButtonFromPref() {
        mNotiFloatButton = mSharedPref.getBoolean(PERF_NOTI_FLOAT_BUTTON, true);
        return mNotiFloatButton;
    }

    public int getNotiRepeatMinutes() {
        return mNotiRepeatMinutes;
    }

    public void setNotiRepeatMinutes(int notiTaskEnable) {
        mNotiRepeatMinutes = notiTaskEnable;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_NOTI_REPEAT_MINUETS, mNotiRepeatMinutes + "").apply();
    }

    public int getNotiRepeatMinutesFromPref() {
        try {
            mNotiRepeatMinutes = Integer.parseInt(mSharedPref.getString(PERF_NOTI_REPEAT_MINUETS, NotificationMgr.MIN_REPEAT_MINUTTES + ""));
        } catch (Exception e) {
            mNotiRepeatMinutes = NotificationMgr.MIN_REPEAT_MINUTTES;
        }
        return mNotiRepeatMinutes;
    }

    public List<String> getBlanklistUsernames() {
        return mBlanklistUsernames;
    }

    public List<String> getBlanklistUsernamesFromPref() {
        String[] usernames = mSharedPref.getString(PERF_BLANKLIST_USERNAMES, "").split(" ");
        mBlanklistUsernames.clear();
        mBlanklistUsernames.addAll(Arrays.asList(usernames));
        return mBlanklistUsernames;
    }

    public void setBlanklistUsernames(List<String> blanklistUsernames) {
        mBlanklistUsernames = blanklistUsernames;
        StringBuilder sb = new StringBuilder();
        for (String username : blanklistUsernames) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(username);
        }
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_BLANKLIST_USERNAMES, sb.toString()).apply();
    }

    public boolean isUserBlack(String username) {
        for (String s : mBlanklistUsernames) {
            if (s.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public int getPostTextsizeAdj() {
        return Integer.parseInt(mPostTextSizeAdj);
    }

    public String getPostTextsizeAdjFromPref() {
        mPostTextSizeAdj = mSharedPref.getString(PERF_TEXTSIZE_POST_ADJ, "0");
        return mPostTextSizeAdj;
    }

    public void setPostTextsizeAdj(String adj) {
        mPostTextSizeAdj = adj;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_TEXTSIZE_POST_ADJ, adj).commit();
    }


    public void setPostLineSpacing(int lineSpacing) {
        mPostLineSpacing = lineSpacing;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_POST_LINE_SPACING, lineSpacing + "").commit();
    }

    public int getPostLineSpacing() {
        return mPostLineSpacing;
    }

    public int getPostLineSpacingFromPref() {
        String value = mSharedPref.getString(PERF_POST_LINE_SPACING, "0");
        if (TextUtils.isDigitsOnly(value)) {
            mPostLineSpacing = Integer.parseInt(value);
        }
        return mPostLineSpacing;
    }

    public int getTitleTextsizeAdj() {
        return Integer.parseInt(mTitleTextSizeAdj);
    }

    public String getTitleTextsizeAdjFromPref() {
        mTitleTextSizeAdj = mSharedPref.getString(PERF_TEXTSIZE_TITLE_ADJ, "0");
        return mTitleTextSizeAdj;
    }

    public void setTitleTextsizeAdj(String adj) {
        mPostTextSizeAdj = adj;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_TEXTSIZE_TITLE_ADJ, adj).commit();
    }

    public int getScreenOrietation() {
        return mScreenOrientation;
    }

    public int getScreenOrietationFromPref() {
        try {
            mScreenOrientation = Integer.parseInt(mSharedPref.getString(PERF_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_USER + ""));
        } catch (Exception e) {
            mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
        }
        return mScreenOrientation;
    }

    public void setScreenOrietation(int screenOrientation) {
        mScreenOrientation = screenOrientation;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_SCREEN_ORIENTATION, mScreenOrientation + "").commit();
    }

    public boolean isGestureBack() {
        return mGestureBack;
    }

    public boolean isGestureBackFromPref() {
        mGestureBack = mSharedPref.getBoolean(PERF_GESTURE_BACK, false);
        return mGestureBack;
    }

    public void setGestureBack(boolean gestureBack) {
        mGestureBack = gestureBack;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_GESTURE_BACK, gestureBack).commit();
    }

    public Date getLastUpdateCheckTime() {
        String millis = mSharedPref.getString(PERF_LAST_UPDATE_CHECK, "");
        if (!TextUtils.isEmpty(millis) && TextUtils.isDigitsOnly(millis)) {
            try {
                return new Date(Long.parseLong(millis));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public void setLastUpdateCheckTime(Date d) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HiSettingsHelper.PERF_LAST_UPDATE_CHECK, d.getTime() + "").apply();
    }

    public void setAutoUpdateCheck(boolean b) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(HiSettingsHelper.PERF_AUTO_UPDATE_CHECK, b).apply();
    }

    public boolean isAutoUpdateCheck() {
        return mSharedPref.getBoolean(PERF_AUTO_UPDATE_CHECK, true);
    }

    public int getMaxPostsInPage() {
        if (mMaxPostsInPage <= 0) {
            mMaxPostsInPage = mSharedPref.getInt(PERF_MAX_POSTS_IN_PAGE, 50);
        }
        return mMaxPostsInPage;
    }

    public void setMaxPostsInPage(int maxPostsInPage) {
        //could be 5,10,15 default is 50
        if (maxPostsInPage > 0 && maxPostsInPage % 5 == 0 && maxPostsInPage != mMaxPostsInPage) {
            mMaxPostsInPage = maxPostsInPage;
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putInt(HiSettingsHelper.PERF_MAX_POSTS_IN_PAGE, mMaxPostsInPage).apply();
        }
    }

    public void setLastForumId(int fid) {
        mLastForumId = fid;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(PERF_LAST_FORUM_ID, fid).apply();
    }

    public int getLastForumId() {
        return mLastForumId;
    }

    public int getLastForumIdFromPerf() {
        mLastForumId = mSharedPref.getInt(PERF_LAST_FORUM_ID, 0);
        return mLastForumId;
    }

    public boolean isAutoUpdateCheckable() {
        if (!isAutoUpdateCheck())
            return false;
        Date lastCheck = HiSettingsHelper.getInstance().getLastUpdateCheckTime();
        //check update if last check is older than 12 hours
        return lastCheck == null
                || System.currentTimeMillis() > lastCheck.getTime() + 12 * 60 * 60 * 1000;
    }

    public void setInstalledVersion(String version) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HiSettingsHelper.PERF_INSTALLED_VERSION, version).apply();
    }

    public String getInstalledVersion() {
        return mSharedPref.getString(PERF_INSTALLED_VERSION, "");
    }

    public String getAppVersion() {
        String version = "0.0.00";
        try {
            version = mCtx.getPackageManager().getPackageInfo(mCtx.getPackageName(), 0).versionName;
        } catch (Exception ignored) {
        }
        return version;
    }

    public int getPostTextSize() {
        return 18 + getInstance().getPostTextsizeAdj();
    }

    public int getTitleTextSize() {
        return 18 + getInstance().getTitleTextsizeAdj();
    }

    public String getStringValue(String key, String defaultValue) {
        return mSharedPref.getString(key, defaultValue);
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(key, value).apply();
    }

    public boolean isInSilentMode() {
        return mSharedPref.getBoolean(PERF_NOTI_SILENT_MODE, false)
                && Utils.isInTimeRange(
                getStringValue(PERF_NOTI_SILENT_BEGIN, NotificationMgr.DEFAUL_SLIENT_BEGIN),
                getStringValue(PERF_NOTI_SILENT_END, NotificationMgr.DEFAUL_SLIENT_END));
    }

}
