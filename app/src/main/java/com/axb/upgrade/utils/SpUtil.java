package com.axb.upgrade.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private SharedPreferences sp;

    public SpUtil(Context context, String spFileName) {
        this.sp = context.getSharedPreferences(spFileName, 0);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor edit = this.sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public void set(String key, int value) {
        SharedPreferences.Editor edit = this.sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public void set(String key, long value) {
        SharedPreferences.Editor edit = this.sp.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public void set(String key, boolean value) {
        SharedPreferences.Editor edit = this.sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public void set(String key, float value) {
        SharedPreferences.Editor edit = this.sp.edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    public String get(String key, String defaultValue) {
        return this.sp.getString(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return this.sp.getInt(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return this.sp.getLong(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return this.sp.getBoolean(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return this.sp.getFloat(key, defaultValue);
    }
}
