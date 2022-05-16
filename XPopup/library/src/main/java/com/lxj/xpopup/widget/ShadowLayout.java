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
package com.lxj.xpopup.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.Arc;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;

/**
 * 具有阴影效果的布局
 */
public class ShadowLayout extends ComponentContainer implements Component.DrawTask {

    private final String STROKE = "stroke"; // 阴影宽度
    private final String RADIUS = "radius"; // 圆角宽度

    private int width;
    private int height;
    private Paint mPaint;
    private int stroke = 15;
    private int radius = 15;
    private int alpha = 30;
    private int red = 90;
    private int green = 90;
    private int blue = 90;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public ShadowLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);

        if (attrSet != null) {
            attrSet.getAttr(STROKE).ifPresent(attr -> stroke = attr.getDimensionValue());
            attrSet.getAttr(RADIUS).ifPresent(attr -> radius = attr.getDimensionValue());
        }
        addDrawTask(this);
    }

    /**
     * 设置阴影宽度
     *
     * @param stroke 阴影宽度，单位px，默认值15px
     */
    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    /**
     * 设置阴影圆角宽度
     *
     * @param radius 阴影圆角宽度。单位px，默认值15px
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        int rectWidth = stroke + radius + stroke + radius;
        width = getWidth();
        height = getHeight();
        for (int i = 0; i < stroke; i++) {
            float fraction = ((float) i) / ((float) stroke);
            mPaint.setColor(new Color(Color.argb((int) (fraction * alpha), (int) (fraction * red), (int) (fraction * green), (int) (fraction * blue))));
            canvas.drawLine(stroke + radius, i, width - stroke - radius, i, mPaint); // 左上-右上
            canvas.drawLine(stroke + radius, height - i, width - stroke - radius, height - i, mPaint); // 左下-右下
            canvas.drawLine(i, stroke + radius, i, height - stroke - radius, mPaint); // 左上-左下
            canvas.drawLine(width - i, stroke + radius, width - i, height - stroke - radius, mPaint); // 右上-右下

            canvas.drawArc(new RectFloat(i, i, rectWidth - i, rectWidth - i), new Arc(180, 90, false), mPaint);
            canvas.drawArc(new RectFloat(width - rectWidth + i, i, width - i, rectWidth - i), new Arc(270, 90, false), mPaint);
            canvas.drawArc(new RectFloat(width - rectWidth + i, height - rectWidth + i, width - i, height - i), new Arc(0, 90, false), mPaint);
            canvas.drawArc(new RectFloat(i, height - rectWidth + i, rectWidth - i, height - i), new Arc(90, 90, false), mPaint);
        }
    }

}
