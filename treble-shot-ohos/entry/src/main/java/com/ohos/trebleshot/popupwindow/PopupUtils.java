package com.ohos.trebleshot.popupwindow;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Rect;
import ohos.app.Context;

import java.util.ArrayList;

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

final class PopupUtils {

    private PopupUtils() {
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getWindowWidth(Context context) {
        return context.getResourceManager().getDeviceCapability().width * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }

    /**
     * 获取屏幕的高度，包含状态栏，导航栏
     *
     * @param context 上下文
     * @return 屏幕的高度，包含状态栏，导航栏
     */
    public static int getScreenHeight(Context context) {
        return context.getResourceManager().getDeviceCapability().height * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }

    /**
     * vp转px
     *
     * @param context 上下文
     * @param vpValue vp
     * @return px
     */
    public static int vp2px(Context context, float vpValue) {
        return (int) (vpValue * context.getResourceManager().getDeviceCapability().screenDensity / 160);
    }

    public static void applyPopupSize(final ComponentContainer content, final int maxWidth, final int maxHeight, final int popupWidth, final int popupHeight, final Runnable afterApplySize) {
        content.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                ComponentContainer.LayoutConfig params = content.getLayoutConfig();
                Component implView = content.getComponentAt(0);
                ComponentContainer.LayoutConfig implParams = implView.getLayoutConfig();
                // 假设默认Content宽是match，高是wrap
                int contentWidth = content.getWidth();
                // response impl view wrap_content params
                if (maxWidth > 0) {
                    // 指定了最大宽度，就限制最大宽度
                    params.width = Math.min(contentWidth, maxWidth);
                    if (popupWidth > 0) {
                        params.width = Math.min(popupWidth, maxWidth);
                        implParams.width = Math.min(popupWidth, maxWidth);
                    }
                } else if (popupWidth > 0) {
                    params.width = popupWidth;
                    implParams.width = popupWidth;
                }

                int contentHeight = content.getHeight();
                if (maxHeight > 0) {
                    params.height = Math.min(contentHeight, maxHeight);
                    if (popupHeight > 0) {
                        params.height = Math.min(popupHeight, maxHeight);
                        implParams.height = Math.min(popupHeight, maxHeight);
                    }
                } else if (popupHeight > 0) {
                    params.height = popupHeight;
                    implParams.height = popupHeight;
                }
                implView.setLayoutConfig(implParams);
                content.setLayoutConfig(params);

                if (afterApplySize != null) {
                    afterApplySize.run();
                }
            }
        });
    }


    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

    public static void findAllEditText(ArrayList<TextField> list, ComponentContainer group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Component component = group.getComponentAt(i);
            if (component instanceof TextField && component.getVisibility() == Component.VISIBLE) {
                list.add((TextField) component);
            } else if (component instanceof ComponentContainer) {
                findAllEditText(list, (ComponentContainer) component);
            }
        }
    }


    /**
     * 获取圆角背景
     *
     * @param color  背景颜色
     * @param radius 四个角的圆角度数
     * @return 圆角背景
     */
    public static ShapeElement createDrawable(int color, float radius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(new RgbColor(color));
        drawable.setCornerRadius(radius);
        return drawable;
    }


}
