package com.axb.upgrade.utils;

import android.content.Context;

import com.axb.upgrade.entities.BaseUpdateInfo;
import com.axb.upgrade.parsers.BaseParser;
import com.axb.upgrade.parsers.XMLParser;
import com.axb.upgrade.threads.CheckUpdateThread;
import com.axb.upgrade.threads.PerformUpdateThread;

public class AutoUpgradeTool {
    public AutoUpgradeTool() {
    }

    public void checkUpdate(String updateUrl, BaseUpdateInfo updateInfo, BaseParser parser, OnCheckUpdateListener onCheckUpdateListener, Object userParam) {
        (new CheckUpdateThread(updateUrl, updateInfo, parser, onCheckUpdateListener, userParam)).start();
    }

    public void checkUpdate(String updateUrl, BaseUpdateInfo updateInfo, OnCheckUpdateListener onCheckUpdateListener, Object userParam) {
        this.checkUpdate(updateUrl, updateInfo, new XMLParser(), onCheckUpdateListener, userParam);
    }

    public void checkUpdate(String updateUrl, OnCheckUpdateListener onCheckUpdateListener, Object userParam) {
        this.checkUpdate(updateUrl, new BaseUpdateInfo() {
            protected boolean doCheckKey(String _name) {
                return false;
            }

            protected void doParse(String _name, String _value) {
            }
        }, new XMLParser(), onCheckUpdateListener, userParam);
    }

    public void performUpdate(Context context, String apkUrl, String apkFileName, String fileProviderName, OnPerformUpdateListener listener) {
        (new PerformUpdateThread(context, apkUrl, apkFileName, fileProviderName, listener)).start();
    }

    public interface OnPerformUpdateListener {
        void onDownloadStart(int var1);

        void onDownloadProgress(int var1);

        void onDownloadFinished();

        void onDownloadError(String var1);
    }

    public interface OnCheckUpdateListener {
        void onCheckSuccess(BaseUpdateInfo var1, Object var2);

        void onCheckFailed(String var1, Object var2);
    }
}
