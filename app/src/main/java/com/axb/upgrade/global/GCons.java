package com.axb.upgrade.global;

public class GCons {
   public static final String AUTO_UPGRADE_URL = "https://www.ebookxz.cn:12760/update/favorite-apps/favorite_apps.xml";
   public static final String DB_NAME = "AxbFavoriteApps.db";
   public static final String SP_FILE_NAME = "AxbFavoriteApps.sp";

   public static final String SP_DEVICE_MAC = "DEVICE_MAC";
   public static final String SP_CHECK_UPDATE_TIME = "CHECK_UPDATE_TIME";
   public static final String SP_NEW_VERSION_FOUND = "NEW_VERSION_FOUND";

   public static final String DATE_IN_HISTORY = "1978-09-24 00:00:00";
   public static final String DIR_ROOT = "Root";
   public static final String DIR_TEMP = "Temp";
   public static final int MAX_SECONDS_TO_TRUST_LOCAL = 60 * 60 * 24;

   public static final int APP_FAVORITE_APPS = 410;
   public static final int APP_FAVORITE_APPS_EXTRA = 0;
   public static final int APP_EVENT_UPLOAD_INTERVAL = 30 * 1000;

   public static final int REQ_PERMISSIONS = 10001;

   public static final int MSG_UPGRADE_REQUEST = 10003;
   public static final int MSG_UPGRADE_IGNORE = 10004;
   public static final int MSG_UPGRADE_ERROR = 10005;
   public static final int MSG_DOWNLOAD_APK_STARTED = 10006;
   public static final int MSG_DOWNLOAD_APK_PROGRESS = 10007;
   public static final int MSG_DOWNLOAD_APK_FINISHED = 10008;
   public static final int MSG_DOWNLOAD_APK_ERROR = 10009;
}
