package com.axb.upgrade.base;

public abstract class BaseThread extends Thread {
    private static final int DEFAULT_INTERVAL = 50;
    private boolean cancelTask;
    protected int interval;
    protected int userParamInt;
    protected Object userParamObj;

    protected abstract boolean started();

    protected abstract boolean performTaskInLoop();

    protected abstract void finished();

    public BaseThread() {
        this(50, 0, (Object)null);
    }

    public BaseThread(int interval) {
        this(interval, 0, (Object)null);
    }

    public BaseThread(int interval, int userParamInt, Object userParamObj) {
        this.cancelTask = false;
        this.interval = interval;
        this.userParamInt = userParamInt;
        this.userParamObj = userParamObj;
    }

    public void run() {
        try {
            if (!this.started()) {
                return;
            }

            while(!this.cancelTask && this.performTaskInLoop()) {
                Thread.sleep((long)this.interval);
            }

            this.finished();
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

    }

    public static void cancel(BaseThread thread) {
        if (thread != null && thread.isAlive()) {
            thread.setCancelTask(true);

            try {
                thread.join(2000L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void setCancelTask(boolean cancelTask) {
        this.cancelTask = cancelTask;
    }

    public boolean isCancelTask() {
        return this.cancelTask;
    }
}
