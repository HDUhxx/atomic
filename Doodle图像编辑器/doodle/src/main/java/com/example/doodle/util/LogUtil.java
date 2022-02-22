/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.doodle.util;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class LogUtil {
    /**
     * TAG
     *
     * @since 2021-04-27
     */
    public static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x01, "com.example.doodle");

    private LogUtil() {
    }

    /**
     * From: width=%d, height=%f, text=%s
     * To: width=%{public}d, height=%{public}f, text=%{public}s
     * 不支持类似 %02d 这样的补位
     *
     * @param logMessageFormat
     * @return String
     */
    public static String replaceFormat(String logMessageFormat) {
        return logMessageFormat.replaceAll("%([d|f|s])", "%{public}$1");
    }

    /**
     * d
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void d(String tag, String format, Object... args) {
        HiLog.debug(LABEL, tag + "" + replaceFormat(format), args);
    }

    /**
     * i
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void i(String tag, String format, Object... args) {
        HiLog.info(LABEL, tag + "" + replaceFormat(format), args);
    }

    /**
     * w
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void w(String tag, String format, Object... args) {
        HiLog.warn(LABEL, tag + " " + replaceFormat(format), args);
    }

    /**
     * e
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void e(String tag, String format, Object... args) {
        HiLog.error(LABEL, tag + " " + replaceFormat(format), args);
    }
}
