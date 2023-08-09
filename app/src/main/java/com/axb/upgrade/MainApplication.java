package com.axb.upgrade;

import android.app.Application;
import android.content.Context;

import com.axb.upgrade.global.GCons;
import com.axb.upgrade.utils.SpUtil;


public class MainApplication extends Application {

    // Fields
    private static SpUtil spUtil;

    // Override functions
    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    // Private functions
    private void initialize() {
        Context context = getApplicationContext();
        spUtil = new SpUtil(context, GCons.SP_FILE_NAME);
        MyUncaughtExceptionHandler myUncaughtExceptionHandler = new MyUncaughtExceptionHandler(context);
        Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);

    }

    // Properties
    public static SpUtil getSpUtil() {
        return spUtil;
    }

    // Classes
    private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        // Fields
        private final Thread.UncaughtExceptionHandler defaultHandler;

        // Constructors
        MyUncaughtExceptionHandler(Context context) {
            this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        }

        // Override functions
        @Override
        public void uncaughtException(Thread thread, Throwable e) {
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, e);
            }
        }
    }
}
