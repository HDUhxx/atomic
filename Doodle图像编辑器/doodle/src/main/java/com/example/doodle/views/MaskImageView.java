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

import com.example.doodle.util.AttrValue;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.ColorFilter;
import ohos.agp.render.ColorMatrix;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * // 缺少重要api,未移植完成
 * 可在背景图和前景图显示遮罩效果的ImageView (前提设置了setClickable(true))
 *
 * @since 2021-04-29
 */
public class MaskImageView extends PaddingImageView implements Component.DrawTask {
    // 遮罩的范围
    /**
     * 背景图显示遮罩
     */
    public static final int MASK_LEVEL_BACKGROUND = 1;
    /**
     * 前景图显示遮罩
     */
    public static final int MASK_LEVEL_FOREGROUND = 2;
    private boolean mIsIgnoreAlpha = true; // 是否忽略图片的透明度，默认为true,透明部分不显示遮罩

    private boolean mIsShowMaskOnClick = true; // 点击时是否显示遮罩，默认开启
    private final static int mMaskDefColor = 0x00ffffff;
    private int mMaskColor = mMaskDefColor; // 遮罩颜色（argb,需要设置透明度）
    private float mPressedAlpha = 1.0f;

    private int mMaskLevel = MASK_LEVEL_FOREGROUND; // 默认为前景图显示遮罩

    private ColorMatrix mColorMatrix = new ColorMatrix(); // 颜色矩阵
    private ColorFilter mColorFilter;

    // all drawables instances loaded from  the same resource share getScreenHeight common state
    // 从同一个资源文件获取的drawable对象共享一个状态信息，为了避免修改其中一个drawable导致其他drawable被影响，需要调用mutate()
    // 因为背景图在draw()阶段绘制，所以修改了背景图状态后必须调用invalidateSelf（）刷新
    private ColorFilter mLastColorFilter; // 记录上一次设置的filter避免重复设置导致递归调用ondraw
    /**
     * view状态改变
     */

    private ComponentStateChangedListener mStateChangedListener = new ComponentStateChangedListener() {
        @Override
        public void onComponentStateChanged(Component component, int i) {
            invalidate();
        }
    };


    /**
     * MaskImageView
     *
     * @param context
     */
   /* public MaskImageView(Context context) {
        this(context, null);
    }*/

    /**
     * MaskImageView
     *
     * @param context
     * @param attrs
     */
    public MaskImageView(Context context, AttrSet attrs) {
        this(context, attrs, null);
    }

    /**
     * MaskImageView
     *
     * @param context
     * @param attrs
     * @param styleName
     */
    public MaskImageView(Context context, AttrSet attrs, String styleName) {
        super(context, attrs, styleName);
        init(attrs);
    }

    private void init(AttrSet attrs) {
        mIsIgnoreAlpha = AttrValue.get(attrs, "miv_is_ignore_alpha", mIsIgnoreAlpha);
        mIsShowMaskOnClick = AttrValue.get(attrs, "miv_is_show_mask_on_click", mIsShowMaskOnClick);
        mMaskColor = AttrValue.get(attrs, "miv_mask_color", mMaskColor);
        mMaskLevel = AttrValue.get(attrs, "miv_mask_level", mMaskLevel);
        mPressedAlpha = AttrValue.get(attrs, "miv_pressed_alpha", mPressedAlpha);

        setMaskColor(mMaskColor);
        SelectorAttrs.obtainsAttrs(getContext(), this, attrs);
        addDrawTask(this);
        setComponentStateChangedListener(mStateChangedListener);
    }

    private void setColorMatrix(float[] matrix) {
        mColorMatrix.setMatrix(matrix);

        // 无对应api
        // mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
    }

    private void setDrawableColorFilter(ColorFilter colorFilter) {
        if (mMaskLevel == MASK_LEVEL_BACKGROUND) {
            if (getBackgroundElement() != null) {
                if (mLastColorFilter == colorFilter) {
                    return;
                }
                // getBackgroundElement().mutate();
                // getBackgroundElement().setColorFilter(colorFilter);
            }
        } else if (mMaskLevel == MASK_LEVEL_FOREGROUND) {
            if (getImageElement() != null) {
                if (mLastColorFilter == colorFilter) {
                    return;
                }
                // getImageElement().mutate();
                // 无对应api
                // getImageElement().setColorFilter(colorFilter);无对应api
            }
        }
        mLastColorFilter = colorFilter;
    }

    /**
     * draw:
     * 1.绘制背景。background.draw(canvas)(背景图是在draw()方法里面绘制的).
     * 2.绘制自己。调用onDraw(canvas).
     * 3.绘制子控件。调用dispatchDraw(canvas).
     * 4.绘制装饰。调用onDrawScrollBars(canvas).
     *
     * @param component
     * @param canvas
     */
    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (mIsIgnoreAlpha) { // 忽略透明度，只在不透明部分绘制遮罩
            if (mIsShowMaskOnClick && isPressed()) {
                // 绘制遮罩层
                setDrawableColorFilter(mColorFilter);
            } else {
                setDrawableColorFilter(null);
            }
        }
        if (!mIsIgnoreAlpha) { // 不忽略透明度,直接通过canvas.drawColor绘制遮罩层
            setDrawableColorFilter(null);
            if (mMaskLevel == MASK_LEVEL_BACKGROUND) { // 背景图
//                if (mIsShowMaskOnClick && isPressed()) {
//                    // 绘制遮罩层
//                    canvas.drawColor(mMaskColor, Canvas.PorterDuffMode.DST_OVER);
//                }
                // super.onDraw(canvas);
            } else { // 前景图
                // super.onDraw(canvas);
                if (mIsShowMaskOnClick && isPressed()) {
                    // 绘制遮罩层
                    canvas.drawColor(mMaskColor, Canvas.PorterDuffMode.DST_OVER);
                }
            }
        } else {
            // super.onDraw(canvas);
        }
    }

    @Override
    public void setPressState(boolean isPressState) {
        super.setPressState(isPressState);
        if (isPressState) {
            setAlpha(mPressedAlpha);
        } else {
            setAlpha(1.0f);
        }
    }

    /**
     * isIsIgnoreAlpha
     *
     * @return boolean
     */
    public boolean isIsIgnoreAlpha() {
        return mIsIgnoreAlpha;
    }

    /**
     * setIsIgnoreAlpha
     *
     * @param isIgnoreAlpha
     */
    public void setIsIgnoreAlpha(boolean isIgnoreAlpha) {
        this.mIsIgnoreAlpha = isIgnoreAlpha;
        invalidate();
    }

    public boolean isIsShowMaskOnClick() {
        return mIsShowMaskOnClick;
    }

    /**
     * setIsShowMaskOnClick
     *
     * @param isShowMaskOnClick
     */
    public void setIsShowMaskOnClick(boolean isShowMaskOnClick) {
        this.mIsShowMaskOnClick = isShowMaskOnClick;
        invalidate();
    }

    public int getShadeColor() {
        return getMaskColor();
    }

    /**
     * setShadeColor
     *
     * @param shadeColor
     */
    public void setShadeColor(int shadeColor) {
        setMaskColor(shadeColor);
    }

    public int getMaskColor() {
        return mMaskColor;
    }

    /**
     * 忽略透明度，添加遮罩原理
     * <p>
     * 创建新的滤镜
     * ColorMatrix colorMatrix = new ColorMatrix(new float[]{
     * a,b,c,d,e,
     * f,g,h,i,j,
     * k,l,m,n,o,
     * p,q,r,s,t});
     * <p>
     * 已知一个颜色值ARGB，则经过下面的矩阵运算可得出新的颜色值
     * int red   = a*R + b*R + c*R + d*R + e;
     * int green = f*G + g*G + h*G + i*G + j;
     * int blue  = k*B + l*B + m*B + n*B + o;
     * int alpha = p*A + q*A + r*A + s*A + t;
     * <p>
     * 设置图片滤镜
     * getDrawable().setColorFilter(new ColorMatrixColorFilter(colorMatrix));
     * <p>
     * 绘图
     * mDrawable.draw(canvas)
     *
     * @param maskColor
     */

    public void setMaskColor(int maskColor) {
        this.mMaskColor = maskColor;
        final float var255 = 255f;
        final int varFf = 0xFF;
        final int var8 = 8;
        final int var16 = 16;
        final float varDouble015 = 0.15f;
        final float varDouble15 = 1.15f;

        // 忽略透明度时的颜色矩阵
        float red = Color.alpha(maskColor) / var255;
        red = red - (1 - red) * varDouble015;
        float rr = (1 - red) * varDouble15;
        setColorMatrix(new float[]{
            rr, 0, 0, 0, ((maskColor >> var16) & varFf) * red,
            0, rr, 0, 0, ((maskColor >> var8) & varFf) * red,
            0, 0, rr, 0, (maskColor & varFf) * red,
            0, 0, 0, 1, 0,
        });
        invalidate();
    }

    public int getMaskLevel() {
        return mMaskLevel;
    }

    /**
     * setMaskLevel
     *
     * @param maskLevel
     */
    public void setMaskLevel(int maskLevel) {
        this.mMaskLevel = maskLevel;
        invalidate();
    }

    /**
     * setPressedAlpha
     *
     * @param pressedAlpha
     */
    public void setPressedAlpha(float pressedAlpha) {
        mPressedAlpha = pressedAlpha;
        invalidate();
    }
}
