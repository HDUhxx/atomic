package com.zxy.recovery.tools;

import com.zxy.recovery.core.Recovery;

/**
 * 描述
 *
 * @author wjt
 * @since 2021-05-08
 */
public class RecoveryLog {
    private static final String TAG = "Recovery";

    /**
     * 私有构造
     */
    private RecoveryLog() {
    }

    /**
     * 打印数据
     *
     * @param message
     */
    public static void e(String message) {
        if (Recovery.getInstance().isDebug()) {
            LogUtil.debug(TAG, message);
        }
    }
}
