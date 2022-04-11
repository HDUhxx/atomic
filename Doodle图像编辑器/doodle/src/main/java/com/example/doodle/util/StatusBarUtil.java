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

import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class StatusBarUtil {
    private StatusBarUtil() {
    }

    /**
     * 设置沉浸式状态栏，配合布局文件中fitssystemwindows属性
     *
     * @param win
     * @param isTranslucent
     * @param isDarkMode 状态栏文字是否为黑色
     */
    public static void setStatusBarTranslucent(Window win, boolean isTranslucent, boolean isDarkMode) {
        WindowManager.LayoutConfig winParams = win.getLayoutConfig().get();
        if (isTranslucent) {
            winParams.flags |= WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS;
        } else {
            winParams.flags &= ~WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS;
        }
        win.setLayoutConfig(winParams);
    }
}