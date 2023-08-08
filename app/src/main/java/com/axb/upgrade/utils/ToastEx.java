package com.axb.upgrade.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.axb.upgrade.R;

public class ToastEx extends Toast {
    private static final String TAG = "TOAST_EX";
    private static ToastEx toast;

    public ToastEx(Context context) {
        super(context);
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception var2) {
        }

    }

    public void show() {
        try {
            super.show();
        } catch (Exception var2) {
        }

    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }

    }

    public static void showText(Context context, CharSequence text) {
        showToast(context, text, 1);
    }

    public static void showText(Context context, CharSequence text, int duration) {
        showToast(context, text, duration);
    }

    public static void showText(Context context, int text) {
        showToast(context, getText(context, text), 1);
    }

    public static void showText(Context context, int text, int duration) {
        showToast(context, getText(context, text), duration);
    }

    private static void initToast(Context context, CharSequence text) {
        try {
            cancelToast();
            toast = new ToastEx(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.view_toast_ex, (ViewGroup)null);
            TextView tvContent = (TextView)layout.findViewById(R.id.tvMessage);
            tvContent.setText(text);
            toast.setView(layout);
            toast.setGravity(80, 0, 0);
        } catch (Exception var5) {
        }

    }

    private static void showToast(Context context, CharSequence text, int duration) {
        initToast(context, text);
        if (duration == 1) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        toast.show();
    }

    private static String getText(Context context, int text) {
        return context.getResources().getString(text);
    }
}
