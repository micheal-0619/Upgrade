package com.axb.upgrade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public abstract class BaseDialog extends Dialog {
    protected Context mContext;
    protected Window mWindow;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.mWindow = this.getWindow();
        this.requestWindowFeature(1);
        this.setContentView(this.getLayoutResource());
        this.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = this.mWindow.getAttributes();
        params.width = this.getWidth();
        params.height = this.getHeight();
        this.mWindow.setAttributes(params);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.bindWidgets();
    }

    protected void onStart() {
        super.onStart();
        this.hideBottomUIMenu();
    }

    public void dismiss() {
        super.dismiss();
        this.hideBottomUIMenu();
    }

    public void show() {
        this.mWindow.setFlags(8, 8);
        super.show();
        this.hideBottomUIMenu();
        this.mWindow.clearFlags(8);
    }

    protected abstract int getLayoutResource();

    protected abstract void bindWidgets();

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected void hideBottomUIMenu() {
        View decorView;
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            decorView = this.getWindow().getDecorView();
            decorView.setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            decorView = this.getWindow().getDecorView();
            int uiOptions = 4102;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

}
