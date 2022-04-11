package com.zxy.recovery.test;

/**
 * 描述 捕获异常
 *
 * @author wjt
 * @since 2021-05-08
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    /**
     * 构造
     */
    public MyCrashHandler() {
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        mUncaughtExceptionHandler.uncaughtException(t, e);
    }

    /**
     * 注册
     */
    public static void register() {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());
    }
}
