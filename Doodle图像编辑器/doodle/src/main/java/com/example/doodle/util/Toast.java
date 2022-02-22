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

import ohos.agp.colors.RgbColor;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class Toast {
    /**
     * LENGTH_LONG
     */
    public static final int LENGTH_LONG = 4000;

    /**
     * LENGTH_SHORT
     */
    public static final int LENGTH_SHORT = 2000;

    private Toast() {
    }

    /**
     * 枚举
     *
     * @since 2021-04-29
     */
    public enum ToastLayout {
        /**
         * DEFAULT
         */
        DEFAULT,

        /**
         * CENTER
         */
        CENTER,

        /**
         * TOP
         */
        TOP,

        /**
         * BOTTOM
         */
        BOTTOM,
    }

    /**
     * showShort
     *
     * @param context
     * @param content
     */
    public static void showShort(Context context, String content) {
        createTost(context, content, LENGTH_SHORT, ToastLayout.DEFAULT);
    }

    /**
     * showLong
     *
     * @param context
     * @param content
     */
    public static void showLong(Context context, String content) {
        createTost(context, content, LENGTH_LONG, ToastLayout.DEFAULT);
    }

    /**
     * show
     *
     * @param context
     * @param content
     */
    public static void show(Context context, String content) {
        createTost(context, content, LENGTH_SHORT, ToastLayout.DEFAULT);
    }

    /**
     * show
     *
     * @param context
     * @param content
     * @param duration
     */
    public static void show(Context context, String content, int duration) {
        createTost(context, content, duration, ToastLayout.DEFAULT);
    }

    /**
     * show
     *
     * @param context
     * @param content
     * @param layout
     */
    public static void show(Context context, String content, ToastLayout layout) {
        createTost(context, content, LENGTH_SHORT, layout);
    }

    /**
     * show
     *
     * @param context
     * @param content
     * @param duration
     * @param layout
     */
    public static void show(Context context, String content, int duration, ToastLayout layout) {
        createTost(context, content, duration, layout);
    }

    /**
     * showShort
     *
     * @param context
     * @param content
     */
    public static void showShort(Context context, int content) {
        createTost(context, getString(context, content), LENGTH_SHORT, ToastLayout.DEFAULT);
    }

    /**
     * showLong
     *
     * @param context
     * @param content
     */
    public static void showLong(Context context, int content) {
        createTost(context, getString(context, content), LENGTH_LONG, ToastLayout.DEFAULT);
    }

    /**
     * show
     *
     * @param context
     * @param content
     */
    public static void show(Context context, int content) {
        createTost(context, getString(context, content), LENGTH_SHORT, ToastLayout.DEFAULT);
    }

    /**
     * show
     *
     * @param context
     * @param content
     * @param duration
     */
    public static void show(Context context, int content, int duration) {
        createTost(context, getString(context, content), duration, ToastLayout.DEFAULT);
    }

    /**
     * show
     *
     * @param context
     * @param content
     * @param layout
     */
    public static void show(Context context, int content, ToastLayout layout) {
        createTost(context, getString(context, content), LENGTH_SHORT, layout);
    }

    /**
     * show
     *
     * @param context
     * @param content
     * @param duration
     * @param layout
     */
    public static void show(Context context, int content, int duration, ToastLayout layout) {
        createTost(context, getString(context, content), duration, layout);
    }

    private static void createTost(Context context, String content, int duration, ToastLayout layout) {
        final int textSize = 16;
        final int padding16 = 16;
        final int padding4 = 16;
        final int radius = 20;

        DirectionalLayout toastLayout = new DirectionalLayout(context);
        DirectionalLayout.LayoutConfig textConfig = new DirectionalLayout.LayoutConfig(DirectionalLayout
            .LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        Text text = new Text(context);
        text.setText(content);
        text.setTextColor(new Color(Color.getIntColor("#ffffff")));
        text.setPadding(vp2px(context, padding16), vp2px(context, padding4),
            vp2px(context, padding16), vp2px(context, padding4));
        text.setTextSize(vp2px(context, textSize));
        text.setBackground(buildDrawableByColorRadius(Color.getIntColor("#70000000"), vp2px(context, radius)));
        text.setLayoutConfig(textConfig);
        toastLayout.addComponent(text);
        int mLayout = LayoutAlignment.CENTER;
        switch (layout) {
            case TOP:
                mLayout = LayoutAlignment.TOP;
                break;
            case BOTTOM:
                mLayout = LayoutAlignment.BOTTOM;
                break;
            case CENTER:
                mLayout = LayoutAlignment.CENTER;
                break;
            default:
                break;
        }
        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setComponent(toastLayout);
        toastDialog.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        toastDialog.setAlignment(mLayout);
        toastDialog.setTransparent(true);
        toastDialog.setDuration(duration);
        toastDialog.show();
    }

    private static ohos.agp.components.element.Element buildDrawableByColorRadius(int color, float radius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(0);
        drawable.setRgbColor(RgbColor.fromArgbInt(color));
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private static String getString(Context context, int resId) {
        try {
            return context.getResourceManager().getElement(resId).getString();
        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    private static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }
}