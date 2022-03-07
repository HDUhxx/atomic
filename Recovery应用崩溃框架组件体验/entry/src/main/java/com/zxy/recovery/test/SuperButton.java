/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxy.recovery.test;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentState;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * button
 *
 * @author:wjt
 * @since 2021-04-22
 */
public class SuperButton extends Button implements Component.DrawTask,
        Component.TouchEventListener, Component.ComponentStateChangedListener {
    private static final int NUM_255 = 255;
    private static final int DOWN = 18432;
    private static final int UP = 2048;
    private static final float NUMLDQ = 0.7F;
    private float mPhase;
    private float[] mDashPath;

    private int strokeWidth;
    private float cornersRadius;
    private float[] cornersRadius2;

    private RgbColor strokeColor;
    private RgbColor normalColor;
    private RgbColor selectColor;
    private RgbColor[] gradientColors;
    private RgbColor disabledColor;

    private StateElement mBackground;

    private int shape = ShapeElement.RECTANGLE;
    private ShapeElement.Orientation gorientation5;

    /**
     * 构造函数
     *
     * @param context context
     */
    public SuperButton(Context context) {
        this(context, null);
    }

    /**
     * 构造
     *
     * @param context
     * @param attrSet
     */
    public SuperButton(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * 构造
     *
     * @param context
     * @param attrSet
     * @param styleName
     */
    public SuperButton(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        parseAttrSet(attrSet);
        addDrawTask(this);
        if (isClickable() && isEnabled()) {
            setComponentStateChangedListener(this);
        }
    }

    private void parseAttrSet(AttrSet attrSet) {
        Color defaultColor = new Color();
        shape = AttrValue.get(attrSet, "super_shape", ShapeElement.RECTANGLE);
        Color normalColor2 = AttrValue.get(attrSet, "super_normal_color", defaultColor);
        if (normalColor2.getValue() != 0) {
            normalColor = RgbColor.fromArgbInt(normalColor2.getValue());
        }
        Color selectColor2 = AttrValue.get(attrSet, "super_select_color", defaultColor);
        if (selectColor2.getValue() != 0) {
            selectColor = RgbColor.fromArgbInt(selectColor2.getValue());
        }
        int strokeWidth2 = AttrValue.get(attrSet, "super_stroke_width", 0);
        if (strokeWidth2 != 0) {
            strokeWidth = AttrHelper.vp2px(strokeWidth2, getContext());
        }
        Color strokeColor2 = AttrValue.get(attrSet, "super_stroke_color", defaultColor);
        if (strokeColor2.getValue() != 0) {
            strokeColor = RgbColor.fromArgbInt(strokeColor2.getValue());
        }
        int cornersRadius3 = AttrValue.get(attrSet, "super_corners_radius", 0);
        if (cornersRadius3 != 0) {
            cornersRadius = AttrHelper.vp2px(cornersRadius3, getContext());
        }
        if (shape == ShapeElement.RECTANGLE) {
            int leftTop = AttrValue.get(attrSet, "super_lef_top_radius", 0);
            int leftBottom = AttrValue.get(attrSet, "super_lef_bottom_radius", 0);
            int rightTop = AttrValue.get(attrSet, "super_right_top_radius", 0);
            int rightBottom = AttrValue.get(attrSet, "super_right_bottom_radius", 0);
            cornersRadius2 = new float[]{leftTop, leftBottom, rightTop, rightBottom};
        }
        Color notClickColor3 = AttrValue.get(attrSet, "super_disabled_color", defaultColor);
        if (notClickColor3.getValue() != 0) {
            disabledColor = RgbColor.fromArgbInt(notClickColor3.getValue());
        }
        Color startColor3 = AttrValue.get(attrSet, "super_g_start_color", defaultColor);
        Color endColor3 = AttrValue.get(attrSet, "super_g_end_color", defaultColor);
        if (startColor3.getValue() != 0 && endColor3.getValue() != 0) {
            RgbColor startColor = RgbColor.fromArgbInt(startColor3.getValue());
            RgbColor endColor = RgbColor.fromArgbInt(endColor3.getValue());
            gradientColors = new RgbColor[]{startColor, endColor};
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        init();
        setBackground(mBackground);
    }

    private void init() {
        mBackground = new StateElement();
        apply();
    }

    private void apply() {
        if (selectColor != null) {
            applyPressedElement();
        }
        if (disabledColor != null) {
            applyDisabledElement();
        }
        applyEmptyElement();
    }

    private void applyEmptyElement() {
        ShapeElement mStateEmptyElement = new ShapeElement();
        mStateEmptyElement.setShape(shape);
        if (normalColor != null) {
            mStateEmptyElement.setRgbColor(normalColor);
        } else {
            normalColor = new RgbColor(NUM_255, NUM_255, NUM_255, NUM_255);
            mStateEmptyElement.setRgbColor(normalColor);
        }
        if (strokeWidth != 0 && strokeColor != null) {
            mStateEmptyElement.setStroke(strokeWidth, strokeColor);
        }
        if (cornersRadius != 0) {
            mStateEmptyElement.setCornerRadius(cornersRadius);
        }
        if (cornersRadius2 != null) {
            mStateEmptyElement.setCornerRadiiArray(cornersRadius2);
        }
        if (gradientColors != null) {
            mStateEmptyElement.setRgbColors(gradientColors);
        }
        if (gorientation5 != null) {
            mStateEmptyElement.setGradientOrientation(gorientation5);
        }
        if (mDashPath != null) {
            mStateEmptyElement.setDashPathEffectValues(mDashPath, mPhase);
        }
        mBackground.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, mStateEmptyElement);
    }

    private void applyDisabledElement() {
        ShapeElement mDisabledElement = new ShapeElement();
        mDisabledElement.setShape(shape);
        mDisabledElement.setRgbColor(disabledColor);
        if (cornersRadius != 0) {
            mDisabledElement.setCornerRadius(cornersRadius);
        }
        if (cornersRadius2 != null) {
            mDisabledElement.setCornerRadiiArray(cornersRadius2);
        }
        mBackground.addState(new int[]{ComponentState.COMPONENT_STATE_DISABLED}, mDisabledElement);
    }

    private void applyPressedElement() {
        ShapeElement mStatePressedElement = new ShapeElement();
        mStatePressedElement.setShape(shape);
        mStatePressedElement.setRgbColor(selectColor);
        if (cornersRadius != 0) {
            mStatePressedElement.setCornerRadius(cornersRadius);
        }
        if (cornersRadius2 != null) {
            mStatePressedElement.setCornerRadiiArray(cornersRadius2);
        }
        mBackground.addState(new int[]{ComponentState.COMPONENT_STATE_PRESSED}, mStatePressedElement);
    }

    /**
     * 设置形状
     *
     * @param shape
     * @return button
     */
    public SuperButton setShape(int shape) {
        this.shape = shape;
        invalidate();
        return this;
    }

    /**
     * 设置状态选择器颜色
     *
     * @param selectColor3
     * @return button
     */
    public SuperButton setShapeSelectorColor(RgbColor selectColor3) {
        this.selectColor = selectColor3;
        invalidate();
        return this;
    }

    /**
     * 设置填充的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperButton setShapeColor(RgbColor color) {
        this.normalColor = color;
        invalidate();
        return this;
    }

    /**
     * 设置边框宽度
     *
     * @param strokeWidth3 边框宽度值
     * @param strokeColor3 设置边框颜色
     * @return 对象
     */
    public SuperButton setShapeStrokeWidthColor(int strokeWidth3, RgbColor strokeColor3) {
        this.strokeWidth = AttrHelper.vp2px(strokeWidth3, getContext());
        this.strokeColor = strokeColor3;
        invalidate();
        return this;
    }

    /**
     * 设置圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperButton setShapeCornersRadius(float radius) {
        this.cornersRadius = radius;
        invalidate();
        return this;
    }

    /**
     * 设置背景渐变方向
     *
     * @param orientation
     * @return button
     */
    public SuperButton setShapeGradientOrientation(ShapeElement.Orientation orientation) {
        this.gorientation5 = orientation;
        invalidate();
        return this;
    }

    /**
     * 设置不能点击状态的颜色
     *
     * @param color
     * @return button
     */
    public SuperButton setDisabledColor(RgbColor color) {
        this.disabledColor = color;
        invalidate();
        return this;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (!isClickable() || !isEnabled()) {
            return false;
        }

        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
            case TouchEvent.POINT_MOVE:

                break;
            case TouchEvent.PRIMARY_POINT_UP:
                // 更新API5 不起作用了
                this.setAlpha(1F);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.setAlpha(1F);
    }

    @Override
    public void onComponentStateChanged(Component component, int state) {
        switch (state) {
            case DOWN:
                if (selectColor == null) {
                    this.setAlpha(NUMLDQ);
                }
                break;
            case UP:
            default:
                setAlpha(1);
        }
    }
}
