package com.lzh.nonview.router.tools;


import com.lzh.nonview.router.Router;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public final class RouterLog {
    private static final String TAG = "RouterLog";
    // 定义日志标签
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MyLog");

    /**
     * d
     *
     * @param message
     */
    public static void d(String message) {
        if (Router.isDebug()) {
            HiLog.debug(LABEL, message);
        }
    }
    /**
     * e
     *
     * @param message
     */
    public static void e(String message, Throwable t) {
        if (Router.isDebug()) {
            HiLog.error(LABEL, message);
        }
    }
}
