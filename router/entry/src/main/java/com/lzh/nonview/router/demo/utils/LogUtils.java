/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lzh.nonview.router.demo.utils;

import com.lzh.nonview.router.demo.ResourceTable;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * description 日志类
 *
 * @since 2021-03-20
 */
public class LogUtils {
    /**
     * 持续时间
     */
    public static final int DURATION = 2000;

    /**
     * info
     */
    public static final int INFO = 0;
    /**
     * error
     */
    public static final int ERROR = 1;
    /**
     * debug
     */
    public static final int DEBUG = 2;
    /**
     * warning
     */
    public static final int WARN = 3;
    /**
     * 返回码
     */
    public static final int REQUEST_CODE = 300;
    /**
     * System
     */
    public static final int SYETEM = 6;
    /**
     * 3.14f
     */
    public static final float PAI = 3.14f;
    /**
     * 5F
     */
    public static final float FIVE = 5f;
    /**
     * 5l
     */
    public static final long LONG = 5L;
    /**
     * double
     */
    public static final double DOUBLE = 3.1415;
    /**
     * 持续时间
     */
    public static final String TAG = "wjtt";

    /**
     * 构造函数
     */
    private LogUtils() {
    }

    /**
     * Log
     *
     * @param logType LogUtils.INFO || LogUtils.ERROR || LogUtils.DEBUG|| LogUtils.WARN
     * @param tag 日志标识  根据喜好，自定义
     * @param message 需要打印的日志信息
     */
    public static void log(int logType, String tag, String message) {
        HiLogLabel lable = new HiLogLabel(HiLog.LOG_APP, 0x0, tag);
        switch (logType) {
            case INFO:
                HiLog.info(lable, message);
                break;
            case ERROR:
                HiLog.error(lable, message);
                break;
            case DEBUG:
                HiLog.debug(lable, message);
                break;
            case WARN:
                HiLog.warn(lable, message);
                break;
            default:
                break;
        }
    }

    /**
     * 获取百度ur
     *
     * @param context 上下文
     * @return url
     */
    public static String getBaiduUrl(Context context) {
        return context.getString(ResourceTable.String_baiduurl);
    }
}
