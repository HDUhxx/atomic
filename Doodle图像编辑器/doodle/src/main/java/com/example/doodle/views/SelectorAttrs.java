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
 *
 */

package com.example.doodle.views;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.StateElement;
import ohos.app.Context;

/**
 * SelectorAttrs
 *
 * @since 2021-04-29
 */
public class SelectorAttrs {
    /**
     * RECTANGLE
     */
    public static final int RECTANGLE = 0;
    /**
     * OVAL
     */
    public static final int OVAL = 1;
    /**
     * LINE
     */
    public static final int LINE = 2;
    /**
     * RING
     */
    public static final int RING = 3;

    private SelectorAttrs() {
    }

    /**
     * 在布局文件中直接设置selector点击效果
     *
     * @param context
     * @param view
     * @param attrs
     */
    public static void obtainsAttrs(Context context, Component view, AttrSet attrs) {
        //背景已经设置为StateListDrawable\RippleDrawable,则不再设置Selector
        Element bitmapDrawable = view.getBackgroundElement();
        if (bitmapDrawable instanceof StateElement) {
            return;
        }
        // TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectorAttrs);
        // TypedAttribute a=
    }
}
