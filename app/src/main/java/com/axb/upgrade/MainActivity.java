package com.axb.upgrade;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import com.axb.upgrade.base.BaseActivity;
import com.axb.upgrade.base.IBaseAnswer;
import com.axb.upgrade.contract.MainContract;
import com.axb.upgrade.databinding.ActivityMainBinding;
import com.axb.upgrade.dialog.NewVersionDialog;
import com.axb.upgrade.entities.BaseUpdateInfo;
import com.axb.upgrade.global.GCons;
import com.axb.upgrade.presenters.MainPresenter;
import com.axb.upgrade.utils.ToastEx;

import java.util.Locale;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainPresenter> implements MainContract.View {

    // Fields
    private ProgressDialog progressDialog;
    private final static String TAG = "MainPresenter";

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GCons.REQ_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                mPresenter.permissionGranted(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED, true);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void init() {
        binding.tvNewVersion.setOnClickListener(view -> mPresenter.checkUpdate());

        mPresenter.attachView(this);
        mPresenter.verifyPermissions(this, new String[] {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        });
    }

    @Override
    protected void readIntentExtras(Intent intent, boolean newIntent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // TODO 需要的话，可以在这里读取外部传进来的参数
            if (newIntent) {
                mPresenter.handleIntent(true);
            }
        }
    }

    @NonNull
    protected MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onError(int code, String msg, int innerCode, String innerMsg) {

    }

    @Override
    public void beforeCheckAppUpdate() {
        binding.tvCheckAppUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterCheckAppUpdate() {
        binding.tvCheckAppUpdate.setVisibility(View.GONE);
    }

    @Override
    public void indicateNewVersionFound(int version, String versionName) {
        binding.tvNewVersion.setVisibility(View.VISIBLE);
    }

    @Override
    public void indicateNoNeedUpdate(int version, String versionName) {
        ToastEx.showText(this, String.format(Locale.ENGLISH, getResources().getString(R.string.msg_already_newest), BuildConfig.VERSION_NAME));
    }

    @Override
    public void onCheckUpdateFailed(String error) {
        ToastEx.showText(this, String.format(Locale.ENGLISH, getResources().getString(R.string.msg_check_update_failed), error));
    }

    @Override
    public void askUserForUpgrade(BaseUpdateInfo updateInfo, IBaseAnswer answer) {
        NewVersionDialog newVersionDialog = new NewVersionDialog(this);
        newVersionDialog.setOnDismissListener(d -> hideBottomUIMenu());
        newVersionDialog.setMsgTitle(String.format(Locale.ENGLISH, getResources().getString(R.string.title_new_version_found_ex), updateInfo.getVersionName()));
        newVersionDialog.setMsgContent(updateInfo.getDescription());
        newVersionDialog.setListener((d, accepted) -> {
            answer.onAnswerResult(accepted);
        });
        newVersionDialog.show();
    }

    @Override
    public void onDownloadApkStarted(int maxLen) {
        hideProgressDialog();
        Log.d(TAG, "onDownloadApkStarted: maxLen = "+maxLen);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(maxLen);
        progressDialog.setMessage(getResources().getString(R.string.title_downloading_apk));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(dialog -> {
        });
        progressDialog.show();
    }

    @Override
    public void onDownloadApkProgress(int progress) {
        if (progressDialog != null) {
            progressDialog.setProgress(progress);
        }
    }

    @Override
    public void onDownloadApkFinished() {
        hideProgressDialog();
    }

    @Override
    public void onDownloadApkError(String error) {
        hideProgressDialog();
        ToastEx.showText(this, error);
    }

    // Private functions
    private void hideProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }
}