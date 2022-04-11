/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain one copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.doodle.util;

import com.example.doodle.ResourceTable;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.lang.ref.WeakReference;

/**
 * toast弹窗
 *
 * @since 2021-03-27
 */
public class ToastsUtils {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x000009, "ToastsUtils");
    final static int rc = 150;
    final static int gc = 150;
    final static int bc = 150;
    final static int alpha = 46;
    final static int radius = 60;
    DirectionalLayout toastLayout;
    private ToastDialog toastDialog;

    /**
     * new Toasts（）
     *
     * @param context
     */
    private ToastsUtils(Context context) {
        throw new RuntimeException("can’t init");
    }

    private ToastsUtils(Builder builder) {
        if (toastDialog == null) {
            toastDialog = new ToastDialog((Context) builder.mContext.get());
        }
        LayoutScatter mLayoutScatter = LayoutScatter.getInstance((Context) builder.mContext.get());
        toastLayout = (DirectionalLayout) mLayoutScatter.parse(ResourceTable.Layout_test_toast, null, false);
        ShapeElement background = new ShapeElement();
        background.setRgbColor(new RgbColor(rc, gc, bc));
        background.setAlpha(alpha);
        background.setCornerRadius(radius);
        if (toastLayout != null) {
            toastLayout.setBackground(background);
        }
        toastDialog.setTransparent(true);
        toastDialog.setAlignment(builder.mAlignment);
        if (toastLayout == null) {
            return;
        }
        if (builder.mToastText != null) {
            ((Text) toastLayout.findComponentById(ResourceTable.Id_msg1_toast)).setText(builder.mToastText);
        }
        if (builder.mComponent != null) {
            toastDialog.setComponent(builder.mComponent);
        } else {
            toastDialog.setComponent(toastLayout);
        }
        toastDialog.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
    }

    /**
     * 弹窗show
     */
    public void show() {
        if (toastDialog == null) {
            return;
        } else {
            if (toastDialog.isShowing()) {
                toastDialog.cancel();
                toastDialog.show();
            } else {
                toastDialog.show();
            }
        }
    }

    /**
     * buider模式构造器
     *
     * @since 2021-03-27
     */
    public static final class Builder {
        private static final int BOTTOM = 80;
        private WeakReference mContext;
        private String mToastText = "";

        // 对齐模式
        private int mAlignment = BOTTOM;

        // 自定义UI界面

        private DirectionalLayout mComponent;

        /**
         * 初始化Builder
         *
         * @param context
         */
        public Builder(Context context) {
            mContext = new WeakReference(context);
        }

        /**
         * 设置显示内容
         *
         * @param string
         * @return Builder
         */
        public Builder setToastText(String string) {
            this.mToastText = string;
            return this;
        }

        /**
         * 设置自定义布局
         *
         * @param directionalLayout
         * @return Builder
         */
        public Builder setComponent(DirectionalLayout directionalLayout) {
            return this;
        }

        /**
         * 最后调用
         *
         * @return ToastsUtils
         */
        public ToastsUtils build() {
            return new ToastsUtils(this);
        }
    }
}

