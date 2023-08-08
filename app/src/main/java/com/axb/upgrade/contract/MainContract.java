package com.axb.upgrade.contract;

import android.app.Activity;

import com.axb.upgrade.base.IBaseAnswer;
import com.axb.upgrade.base.IBasePresenter;
import com.axb.upgrade.base.IBaseView;
import com.axb.upgrade.entities.BaseUpdateInfo;


public interface MainContract {
    interface View extends IBaseView {
        void onError(int code, String msg, int innerCode, String innerMsg);
        void beforeCheckAppUpdate();
        void afterCheckAppUpdate();
        void indicateNewVersionFound(int version, String versionName);
        void indicateNoNeedUpdate(int version, String versionName);
        void onCheckUpdateFailed(String error);
        void askUserForUpgrade(BaseUpdateInfo updateInfo, IBaseAnswer answer);
        void onDownloadApkStarted(int maxLen);
        void onDownloadApkProgress(int progress);
        void onDownloadApkFinished();
        void onDownloadApkError(String error);
    }
    interface Presenter extends IBasePresenter<View> {
        void verifyPermissions(Activity activity, String[] permissions);
        void permissionGranted(String permission, boolean granted, boolean newRequested);
        void handleIntent(boolean newIntent);
        void checkUpdate();
    }
}
