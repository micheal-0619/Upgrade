package com.axb.upgrade.threads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.axb.upgrade.base.BaseThread;
import com.axb.upgrade.utils.AutoUpgradeTool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PerformUpdateThread extends BaseThread {
    private Context context;
    private String apkUrl;
    private String apkFileName;
    private String fileProviderName;
    private AutoUpgradeTool.OnPerformUpdateListener listener;
    private final static String TAG = "PerformUpdateThread";

    public PerformUpdateThread(@NonNull Context context, @NonNull String apkUrl, @NonNull String apkFileName, @NonNull String fileProviderName, @NonNull AutoUpgradeTool.OnPerformUpdateListener listener) {
        this.context = context;
        this.apkUrl = apkUrl;
        this.apkFileName = apkFileName;
        this.fileProviderName = fileProviderName;
        this.listener = listener;
    }

    protected boolean started() {
        return true;
    }

    protected boolean performTaskInLoop() {
        File newApkFile = getFileFromServer();
        if (newApkFile != null) {
            installApk(newApkFile);
        }

        return false;
    }

    protected void finished() {
    }

    private File getFileFromServer() {
        //Log.d(TAG, "getFileFromServer: apkUrl= " + apkUrl);
        try {
            URL url = new URL(apkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            listener.onDownloadStart(conn.getContentLength());
            //Log.d(TAG, "getFileFromServer ccccc: apkUrl= " + apkUrl +" conn="+conn);
            InputStream is = conn.getInputStream();
            //Log.d(TAG, "getFileFromServer dddddd: apkUrl= " + apkUrl);
            File file = new File(apkFileName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int total = 0;

            int len;
            //Log.d(TAG, "getFileFromServer 11111: apkUrl= " + apkUrl + "  total" + total);
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                listener.onDownloadProgress(total);
            }

            //Log.d(TAG, "getFileFromServer: apkUrl= " + apkUrl + "  total" + total);

            fos.close();
            bis.close();
            is.close();
            listener.onDownloadFinished();
            return file;
        } catch (IOException var11) {
            var11.printStackTrace();
            //Log.d(TAG, "IOException getFileFromServer: var11.getMessage()= " + var11.getMessage());
            listener.onDownloadError(var11.getMessage());
            return null;
        }
    }

    private void installApk(File file) {
        Intent intent;
        if (Build.VERSION.SDK_INT > 23) {
            intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(context, fileProviderName, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

    }
}
