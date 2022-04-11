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

import ohos.agp.components.AttrHelper;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class Util {
    private Util() {
    }

    /**
     * vp2px
     *
     * @param context
     * @param vp
     * @return int
     */
    public static int vp2px(Context context, float vp) {
        return AttrHelper.vp2px(vp, context);
    }

    /**
     * getScreenWidth
     *
     * @param context
     * @return width
     */
    public static int getScreenWidth(Context context) {
        return DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes().width;
    }

    /**
     * getScreenHeight
     *
     * @param context
     * @return height
     */
    public static int getScreenHeight(Context context) {
        return DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes().height;
    }
}
