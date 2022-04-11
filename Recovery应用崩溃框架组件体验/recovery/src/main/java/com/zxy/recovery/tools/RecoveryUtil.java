package com.zxy.recovery.tools;

import com.zxy.recovery.core.Recovery;
import com.zxy.recovery.exception.RecoveryException;
import ohos.aafwk.ability.RunningProcessInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.os.ProcessManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * RecoveryUtil
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public class RecoveryUtil {

    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private RecoveryUtil() {
        throw new RecoveryException("Stub!");
    }

    /**
     * checkNotNull
     *
     * @param t T
     * @param message
     * @param <T>
     * @return T
     * @throws RecoveryException
     */
    public static <T> T checkNotNull(T t, String message) {
        if (t == null) {
            throw new RecoveryException(String.valueOf(message));
        }
        return t;
    }

    /**
     * 是否可用
     *
     * @param context 上下文
     * @param intent intent
     * @return 是否可用
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        List<AbilityInfo> list = null;
        if (context == null || intent == null) {
            return false;
        }
        context.requestPermissionsFromUser(new String[]{"ohos.permission.GET_BUNDLE_INFO"}, 0);
        return true;
    }

    /**
     * isAppInBackground
     *
     * @param context
     * @return boolean
     */
    public static boolean isAppInBackground(Context context) {
        List<RunningProcessInfo> appProcesses = context.getAbilityManager().getAllRunningProcesses();
        if (appProcesses == null) {
            return true;
        }
        for (RunningProcessInfo appProcess : appProcesses) {
            if (appProcess.getProcessName().equals(context.getBundleName())) {
                return context.getAbilityManager().isBackgroundRunningRestricted();
            }

        }
        return false;
    }

    /**
     * 是否为主线程
     *
     * @param context
     * @return 是否为主线程
     */
    public static boolean isMainProcess(Context context) {
        List<RunningProcessInfo> processInfo = context.getAbilityManager().getAllRunningProcesses();
        String mainProcessName = context.getBundleName();
        int myPid = ProcessManager.getPid();
        for (RunningProcessInfo info : processInfo) {
            if (info.getPid() == myPid && mainProcessName.equals(info.getProcessName())) {
                return true;
            }
        }
        return false;
    }

    private static File getDataDir() {
        return new File(File.separator + "data" + File.separator
                + "data" + File.separator
                + Recovery.getInstance().getContext().getBundleName());
    }

    private static File getExternalDataDir() {
        File file = Recovery.getInstance().getContext().getExternalCacheDir();
        return file == null ? null : file.getParentFile();
    }

    private static boolean clearAppData(File dir) {
        if (dir == null || !dir.isDirectory() || !dir.exists()) {
            return false;
        }
        File[] files = dir.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            File file = files[i];
            if (file == null) {
                continue;
            }
            if (file.isFile() && file.exists()) {
                boolean result = file.delete();
                RecoveryLog.e(file.getName() + (result ? " delete success!" : " delete failed!"));
                continue;
            }
            if (file.isDirectory() && file.exists()) {
                clearAppData(file);
            }
        }
        return true;
    }

    /**
     * clearApplicationData
     */
    public static void clearApplicationData() {
        clearAppData(getDataDir());
        clearAppData(getExternalDataDir());
    }

    public static DateFormat getDateFormat() {
        return DATE_FORMAT_THREAD_LOCAL.get();
    }
}
