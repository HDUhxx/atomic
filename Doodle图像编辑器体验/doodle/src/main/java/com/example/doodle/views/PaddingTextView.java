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
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * 直接在xml布局文件设置TextView的内容padding, 支持靠边对齐
 * layout_width/height必须为精确值。用于解决点击区域大于内容区域的问题。
 *
 * @since 2021-04-29
 */
public class PaddingTextView extends Text {
    /**
     * PaddingTextView
     *
     * @param context
     */
    public PaddingTextView(Context context) {
        this(context, null);
    }

    /**
     * PaddingTextView
     *
     * @param context
     * @param attrSet
     */
    public PaddingTextView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * PaddingTextView
     *
     * @param context
     * @param attrSet
     * @param styleName
     */
    public PaddingTextView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    private void init() {
        // PaddingViewAttrs.obtainsAttrs(this.getContext(), this, attrs); 待完善
    }
}
