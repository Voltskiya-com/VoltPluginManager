package com.voltskiya.lib.timings.scheduler;

public interface VoltTask {

    static CancellingAsyncTask cancelingAsyncTask(Runnable runnable) {
        return new CancellingAsyncTask(runnable);
    }

    void start(VoltTaskManager manager);
}
