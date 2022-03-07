package com.zxy.recovery.tools;

import com.zxy.recovery.exception.RecoveryException;

/**
 * DefaultHandlerUtil
 *
 * @author:wzgxiaoyongjt
 * @since 2021-04-06
 */
public class DefaultHandlerUtil {
    private DefaultHandlerUtil() {
        throw new RecoveryException("Stub!");
    }

    private static Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
        try {
            Class<?> clazz;
            clazz = Class.forName("com.zxy.recovery.test.MyCrashHandler");
            Object object = clazz.getDeclaredConstructor().newInstance();
            return (Thread.UncaughtExceptionHandler) object;
        } catch (Throwable e) {
            LogUtil.error("DefaultHandlerUtil","Throwable");
        }
        return uncaughtExceptionHandler;
    }

    /**
     * 是否系统默认异常捕获器
     *
     * @param handler
     * @return boolean
     */
    public static boolean isSystemDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        if (handler == null) {
            return false;
        }
        Thread.UncaughtExceptionHandler defHandler = getDefaultUncaughtExceptionHandler();
        return defHandler != null && defHandler.getClass().isInstance(handler);
    }
}
