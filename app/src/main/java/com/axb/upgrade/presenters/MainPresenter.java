package com.axb.upgrade.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import com.axb.appserverclient.AppServer;
import com.axb.appserverclient.BuildConfig;
import com.axb.appserverclient.results.BaseResult;
import com.axb.appserverclient.utils.DateTimeUtil;
import com.axb.appserverclient.utils.NetUtil;
import com.axb.upgrade.MainApplication;
import com.axb.upgrade.R;
import com.axb.upgrade.base.BasePresenter;
import com.axb.upgrade.base.BaseThread;
import com.axb.upgrade.contract.MainContract;
import com.axb.upgrade.entities.BaseUpdateInfo;
import com.axb.upgrade.entities.CheckUpdateEntity;
import com.axb.upgrade.entities.MessageParamEntity;
import com.axb.upgrade.entities.UpdateInfo;
import com.axb.upgrade.global.GCons;
import com.axb.upgrade.utils.AutoUpgradeTool;
import com.axb.upgrade.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainPresenter extends BasePresenter<MainContract.View> implements
        MainContract.Presenter,
        AppServer.OnWebResponseListener,
        AutoUpgradeTool.OnCheckUpdateListener,
        AutoUpgradeTool.OnPerformUpdateListener {

    // Fields
    private final AutoUpgradeTool autoUpgradeTool;
    private final AppServer appServer;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mView == null) return;

            switch (msg.what) {
                case GCons.MSG_UPGRADE_REQUEST:
                    onMsgUpgradeRequest(
                            (BaseUpdateInfo)((MessageParamEntity)msg.obj).getObjParam0(),
                            ((MessageParamEntity)msg.obj).isBoolParam0());
                    break;
                case GCons.MSG_UPGRADE_IGNORE:
                    onMsgUpgradeIgnore(
                            (BaseUpdateInfo)((MessageParamEntity)msg.obj).getObjParam0(),
                            ((MessageParamEntity)msg.obj).isBoolParam0());
                    break;
                case GCons.MSG_UPGRADE_ERROR:
                    onMsgUpgradeError(
                            ((MessageParamEntity)msg.obj).getStrParam0(),
                            ((MessageParamEntity)msg.obj).isBoolParam0()
                    );
                    break;
                case GCons.MSG_DOWNLOAD_APK_STARTED:
                    mView.onDownloadApkStarted(msg.arg1);
                    break;
                case GCons.MSG_DOWNLOAD_APK_PROGRESS:
                    mView.onDownloadApkProgress(msg.arg1);
                    break;
                case GCons.MSG_DOWNLOAD_APK_FINISHED:
                    mView.onDownloadApkFinished();
                    break;
                case GCons.MSG_DOWNLOAD_APK_ERROR:
                    mView.onDownloadApkError(mContext.getResources().getString(R.string.msg_download_error));
                    break;
            }
        }
    };

    // Constructors
    public MainPresenter(Context context, AppServer appServer) {
        super(context);
        this.appServer = appServer;
        autoUpgradeTool = new AutoUpgradeTool();
    }

    // Override functions for MainContract.Presenter
    @Override
    public void verifyPermissions(Activity activity, String[] permissions) {
        List<String> permissionsNeedReq = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            int p = ActivityCompat.checkSelfPermission(mContext, permissions[i]);
            if (p != PackageManager.PERMISSION_GRANTED) {
                permissionsNeedReq.add(permissions[i]);
            } else {
                permissionGranted(permissions[i], true, false);
            }
        }

        if (permissionsNeedReq.size() > 0) {
            String[] items = new String[permissionsNeedReq.size()];
            items = permissionsNeedReq.toArray(items);
            ActivityCompat.requestPermissions(
                    activity,
                    items,
                    GCons.REQ_PERMISSIONS);
        }
    }
    @Override
    public void permissionGranted(String permission, boolean granted, boolean newRequested) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (granted) {
                    //Logger.initializeLoggingSystem(Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath());
                    createAppFolders();
                    checkAppUpdate();
                }
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                break;
        }
    }
    @Override
    public void handleIntent(boolean newIntent) {
        // TODO 处理 Intent 请求
    }
    @Override
    public void checkUpdate() {
        autoUpgradeTool.checkUpdate(GCons.AUTO_UPGRADE_URL, new UpdateInfo(), this, new CheckUpdateEntity(true));
    }
    // Override functions for AppServer.OnWebResponseListener
    @Override
    public void onResult(int reqId, BaseResult baseResult) {
        // TODO 处理 AppServer 成功的请求
    }
    @Override
    public void onError(int reqId, BaseResult baseResult) {
        // TODO 处理 AppServer 失败的请求
    }
    // Override functions for AutoUpgradeTool.OnCheckUpdateListener
    @Override
    public void onCheckSuccess(BaseUpdateInfo baseUpdateInfo, Object userParam) {
        UpdateInfo updateInfo = (UpdateInfo)baseUpdateInfo;
        //Logger.getInstance().i("AutoUpgradeTool.OnCheckUpdateListener onCheckSuccess: " + updateInfo.toString());
        if (BuildConfig.VERSION_CODE < updateInfo.getVersion()) {
            Message msg = new Message();
            msg.what = GCons.MSG_UPGRADE_REQUEST;
            MessageParamEntity paramEntity = new MessageParamEntity();
            paramEntity.setObjParam0(updateInfo);
            paramEntity.setBoolParam0(((CheckUpdateEntity)userParam).isFromUser());
            msg.obj = paramEntity;
            mHandler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = GCons.MSG_UPGRADE_IGNORE;
            MessageParamEntity paramEntity = new MessageParamEntity();
            paramEntity.setObjParam0(updateInfo);
            paramEntity.setBoolParam0(((CheckUpdateEntity)userParam).isFromUser());
            msg.obj = paramEntity;
            mHandler.sendMessage(msg);
        }
    }
    @Override
    public void onCheckFailed(String message, Object userParam) {
       // Logger.getInstance().e("AutoUpgradeTool.OnCheckUpdateListener onCheckFailed: " + message);
        Message msg = new Message();
        msg.what = GCons.MSG_UPGRADE_ERROR;
        MessageParamEntity paramEntity = new MessageParamEntity();
        paramEntity.setStrParam0(message);
        paramEntity.setBoolParam0(((CheckUpdateEntity)userParam).isFromUser());
        msg.obj = paramEntity;
        mHandler.sendMessage(msg);
    }
    // Override functions for AutoUpgradeTool.OnPerformUpdateListener
    @Override
    public void onDownloadStart(int maxLen) {
        //Logger.getInstance().i("AutoUpgradeTool.OnPerformUpdateListener onDownloadStart(" + maxLen + ")");
        Message msg = new Message();
        msg.what = GCons.MSG_DOWNLOAD_APK_STARTED;
        msg.arg1 = maxLen;
        mHandler.sendMessage(msg);
    }
    @Override
    public void onDownloadProgress(int progress) {
        Message msg = new Message();
        msg.what = GCons.MSG_DOWNLOAD_APK_PROGRESS;
        msg.arg1 = progress;
        mHandler.sendMessage(msg);
    }
    @Override
    public void onDownloadFinished() {
        //Logger.getInstance().i("AutoUpgradeTool.OnPerformUpdateListener onDownloadFinished()");
        mHandler.sendEmptyMessage(GCons.MSG_DOWNLOAD_APK_FINISHED);
    }
    @Override
    public void onDownloadError(String errorMessage) {
        //Logger.getInstance().i("AutoUpgradeTool.OnPerformUpdateListener onDownloadError(" + errorMessage + ")");
        Message msg = new Message();
        msg.what = GCons.MSG_DOWNLOAD_APK_ERROR;
        msg.obj = errorMessage;
        mHandler.sendMessage(msg);
    }

    // Private functions
    private void createAppFolders() {
        File dir = mContext.getExternalFilesDir(GCons.DIR_TEMP);
        if (dir != null) {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    //Logger.getInstance().e("Failed to create the folder: " + dir.getAbsolutePath());
                }
            } else {
                clearTempDir(dir);
            }
        } else {
            //Logger.getInstance().e("getExternalFilesDir(" + GCons.DIR_TEMP + ") returned NULL!");
        }

        dir = mContext.getExternalFilesDir(GCons.DIR_ROOT);
        if (dir != null) {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    //Logger.getInstance().e("Failed to create the folder: " + dir.getAbsolutePath());
                }
            }
        } else {
            //Logger.getInstance().e("getExternalFilesDir(" + GCons.DIR_ROOT + ") returned NULL!");
        }
    }
    private void clearTempDir(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) return;

        new ClearTempDirThread(dir).start();
    }
    private void checkAppUpdate() {
        if (!NetUtil.isNetworkConnected(mContext) || checkAppUpdateRecently()) {
            handleIntent(false);
        } else {
            mView.beforeCheckAppUpdate();
            autoUpgradeTool.checkUpdate(GCons.AUTO_UPGRADE_URL, new UpdateInfo(), this, new CheckUpdateEntity(false));
        }
    }
    private boolean checkAppUpdateRecently() {
        Date lastGotAt = DateTimeUtil.strToDate(
                MainApplication.getSpUtil().get(
                        GCons.SP_CHECK_UPDATE_TIME,
                        GCons.DATE_IN_HISTORY));
        Date now = Calendar.getInstance().getTime();
        return (now.getTime() - lastGotAt.getTime()) / 1000 < GCons.MAX_SECONDS_TO_TRUST_LOCAL;
    }
    private void onMsgUpgradeRequest(BaseUpdateInfo baseUpdateInfo, boolean fromUser) {

        MainApplication.getSpUtil().set(GCons.SP_CHECK_UPDATE_TIME, DateTimeUtil.dateToStr(Calendar.getInstance().getTime()));
        mView.afterCheckAppUpdate();

        if (baseUpdateInfo.isForceUpdate() || fromUser) {
            autoUpgradeTool.performUpdate(
                    mContext,
                    baseUpdateInfo.getUrl(),
                    FileUtils.getNewFileName(
                            Objects.requireNonNull(mContext.getExternalFilesDir(GCons.DIR_TEMP)).getAbsolutePath(),
                            "APK",
                            ".apk"),
                    BuildConfig.APPLICATION_ID + ".provider",
                    this);
        } else {
            int newVersionRecorded = MainApplication.getSpUtil().get(GCons.SP_NEW_VERSION_FOUND, 0);
            if (baseUpdateInfo.getVersion() > newVersionRecorded) {
                mView.askUserForUpgrade(baseUpdateInfo, yes -> {
                    if (yes) {
                        autoUpgradeTool.performUpdate(
                                mContext,
                                baseUpdateInfo.getUrl(),
                                FileUtils.getNewFileName(
                                        Objects.requireNonNull(mContext.getExternalFilesDir(GCons.DIR_TEMP)).getAbsolutePath(),
                                        "APK",
                                        ".apk"),
                                BuildConfig.APPLICATION_ID + ".provider",
                                this);
                    } else {
                        handleIntent(false);
                    }
                });
                MainApplication.getSpUtil().set(GCons.SP_NEW_VERSION_FOUND, baseUpdateInfo.getVersion());
            } else {
                mView.indicateNewVersionFound(baseUpdateInfo.getVersion(), baseUpdateInfo.getVersionName());
                handleIntent(false);
            }
        }
    }
    private void onMsgUpgradeIgnore(BaseUpdateInfo baseUpdateInfo, boolean fromUser) {
        MainApplication.getSpUtil().set(GCons.SP_CHECK_UPDATE_TIME, DateTimeUtil.dateToStr(Calendar.getInstance().getTime()));
        if (fromUser) {
            mView.indicateNoNeedUpdate(baseUpdateInfo.getVersion(), baseUpdateInfo.getVersionName());
        } else {
            mView.afterCheckAppUpdate();
            handleIntent(false);
        }
    }
    private void onMsgUpgradeError(String errorMessage, boolean fromUser) {
        if (fromUser) {
            mView.onCheckUpdateFailed(errorMessage);
        } else {
            mView.afterCheckAppUpdate();
            handleIntent(false);
        }
    }

    // Classes
    static class ClearTempDirThread extends BaseThread {

        File dir;

        ClearTempDirThread(File dir) {
            this.dir = dir;
        }

        @Override
        protected boolean started() {
            return true;
        }

        @Override
        protected boolean performTaskInLoop() {
            deleteDir(dir);
            return false;
        }

        @Override
        protected void finished() {

        }

        private void deleteDir(File d) {
            File[] files = d.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDir(file);
                    };
                    if (!file.delete()) {
                       // Logger.getInstance().e("Failed to delete the file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
