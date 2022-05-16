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
package com.lxj.xpopup.util;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

/**
 * 背景工具类
 */
public class ElementUtil {

    public static ShapeElement getShapeElementWith6(int bgColor) {
        if (bgColor < 0 && bgColor >= -0xffffff) {
            bgColor = bgColor + 0xffffff + 1;
        }
        if (bgColor > 0 && bgColor <= 0xffffff) {
            bgColor = alphaColor(bgColor, 1.0f);
        }
        return getShapeElement(bgColor);
    }

    /**
     * 获取ShapeElement,用于设置背景
     *
     * @param bgColor rgba背景色
     * @return 纯色背景
     */
    public static ShapeElement getShapeElement(int bgColor) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(bgColor));
        return shapeElement;
    }

    /**
     * 通过资源id获取color.json文件中的颜色
     *
     * @param context    上下文
     * @param resColorId 颜色的资源id
     * @return 颜色值
     */
    public static int getColor(Context context, int resColorId) {
        return context.getColor(resColorId);
    }

    /**
     * 把一个rgb颜色和一个透明度合并成一个rgba，用于设置背景色的颜色
     *
     * @param rgbColor
     * @param alpha
     * @return
     */
    public static int alphaColor(int rgbColor, float alpha) {
        if (rgbColor < 0) {
            rgbColor = rgbColor + 0xffffff + 1;
        }
        int color = Math.max(0, rgbColor) << 8;
        int alpha255 = (int) (alpha * 255);
        return color + alpha255;
    }

}
