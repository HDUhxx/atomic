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

package com.example.doodle.dialog;

import com.example.doodle.ResourceTable;
import com.example.doodle.util.LogUtil;
import com.example.doodle.util.Util;

import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.LinearShader;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.PixelMapShader;
import ohos.agp.render.Shader;
import ohos.agp.render.SweepShader;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

/**
 * 颜色选择容器
 *
 * @since 2021-04-27
 */
public class ColorPickerView extends Component implements Component.TouchEventListener, Component.DrawTask {
    private static final int NUM_8 = 8;
    private static final int NUM_16 = 16;
    private static final int COLOR_FF = 0xFF;
    private Paint mPaint; // 渐变色环画笔
    private Paint mCenterPaint; // 中间圆画笔
    private Paint mLinePaint; // 分隔线画笔
    private Paint mRectPaint; // 渐变方块画笔

    private Shader rectShader; // 渐变方块渐变图像
    private float rectLeft; // 渐变方块左x坐标
    private float rectTop; // 渐变方块右x坐标
    private float rectRight; // 渐变方块上y坐标
    private float rectBottom; // 渐变方块下y坐标

    private final int[] mCircleColors; // 渐变色环颜色
    private final int[] mRectColors; // 渐变方块颜色

    private int mHeight; // View高
    private int mWidth; // View宽
    private float radius; // 色环半径(paint中部)
    private float centerRadius; // 中心圆半径

    private boolean isDownInCircle = true; // 按在渐变环上
    private boolean isDownInRect; // 按在渐变方块上
    private boolean isHighlightCenter; // 高亮
    private boolean isHighlightCenterLittle; // 微亮
    private RectFloat mRectF = new RectFloat();
    private OnSelectedColorListener mOnSelectedColorListener;
    private Element mDrawable;
    private PixelMapShader mBitmapShader;
    private int mRadiusTwo = 2;
    private int mRadiusParm50 = 50;

    // 测量的回调
    private EstimateSizeListener mEstimateSizeListener = new EstimateSizeListener() {
        @Override
        public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
            setComponentSize(mWidth, mHeight);
            return false;
        }
    };

    /**
     * 构造器
     *
     * @param context
     * @param initColor
     * @param height
     * @param width
     * @param listener
     */
    public ColorPickerView(Context context, int initColor, int height, int width, OnSelectedColorListener listener) {
        super(context);
        final int radiusParm1 = 2;
        final float radiusParm2 = 0.7f;
        final float radiusParm3 = 0.5f;
        final int strokeWidth = 30;
        final int centerStrokeWidth = 5;
        final int lineStrokeWidth = 4;
        final int lineRadiusParm = 15;
        this.mHeight = height;
        this.mWidth = width;
        setMinHeight(height);
        setMinWidth(width);

        // 渐变色环参数
        int mColor1 = 0xFF0000FF;
        int mColor2 = 0xFF00FFFF;
        int mColor3 = 0xFF00FF00;
        int mColor4 = 0xFFFFFF00;
        int mColor5 = 0xFFFF0000;
        mCircleColors = new int[]{ResourceTable.Color_ffff0000, ResourceTable.Color_ffff00ff,
            mColor1, mColor2, mColor3, mColor4, mColor5};
        Shader shader = new SweepShader(0f, 0f, intColors2Colors(), null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(shader, Paint.ShaderType.GROUP_SHADER);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setStrokeWidth(Util.vp2px(context, strokeWidth)); // 圆环大小
        radius = width / radiusParm1 *  (int)radiusParm2 -  (int)mPaint.getStrokeWidth() *  (int)radiusParm3;

        // 中心圆参数
        mCenterPaint = new Paint();

        mCenterPaint.setColor(new Color(initColor));
        mCenterPaint.setStrokeWidth(centerStrokeWidth);
        centerRadius = ( (int)radius -  (int)mPaint.getStrokeWidth() / radiusParm1) *  (int)radiusParm2;

        // 边框参数
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(new Color(Color.getIntColor("#72A1D1")));
        mLinePaint.setStrokeWidth(lineStrokeWidth);

        // 黑白渐变参数
        int mColor6 = 0xFF000000;
        int mColor7 = 0xFFFFFFFF;
        mRectColors = new int[]{mColor6, mCenterPaint.getColor().getValue(), mColor7};
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStrokeWidth(Util.vp2px(context, centerStrokeWidth));
        rectLeft = - (int)radius -  (int)mPaint.getStrokeWidth() *  (int)radiusParm3;
        rectTop =  (int)radius +  (int)mPaint.getStrokeWidth() *  (int)radiusParm3 +  (int)mLinePaint.getAngle() *  (int)radiusParm3 + lineRadiusParm;
        rectRight =  (int)radius +  (int)mPaint.getStrokeWidth() *  (int)radiusParm3;
        rectBottom =  (int)rectTop + Util.vp2px(context, strokeWidth);

        // add测量的回调
        setEstimateSizeListener(mEstimateSizeListener);
        addDrawTask(this);
        setTouchEventListener(this);
        mOnSelectedColorListener = listener;
    }

    /**
     * 设置参数
     *
     * @param drawable
     */
    public void setelement(PixelMapElement drawable) {
        LogUtil.d("setelement()", "---------");
        mDrawable = drawable;
        if (drawable == null) {
            LogUtil.e("setelement()", "drawable is null obj");
            return;
        }
        PixelMapHolder ph = new PixelMapHolder(drawable.getPixelMap());
        mBitmapShader = new PixelMapShader(ph, Shader.TileMode.MIRROR_TILEMODE, Shader.TileMode.MIRROR_TILEMODE);
        invalidate();
    }

    /**
     * getelement
     *
     * @return Element
     */
    public Element getelement() {
        return mDrawable;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        // 移动中心
        canvas.translate(mWidth / mRadiusTwo, mHeight / mRadiusTwo - mRadiusParm50);
        if (mDrawable != null) {
            mCenterPaint.setShader(mBitmapShader, Paint.ShaderType.RADIAL_SHADER);
        } else {
            mCenterPaint.setShader(null, Paint.ShaderType.RADIAL_SHADER);
        }

        // 画中心圆
        canvas.drawCircle(0, 0, centerRadius, mCenterPaint);

        // 是否显示中心圆外的小圆环
        if (isHighlightCenter || isHighlightCenterLittle) {
            Color color = mCenterPaint.getColor();
            mCenterPaint.setStyle(Paint.Style.STROKE_STYLE);
            if (isHighlightCenter) {
                mCenterPaint.setAlpha(COLOR_FF);
            } else if (isHighlightCenterLittle) {
                mCenterPaint.setAlpha(COLOR_FF);
            }
            canvas.drawCircle(0, 0,
                (int)centerRadius +  (int)mCenterPaint.getStrokeWidth(), mCenterPaint);

            mCenterPaint.setStyle(Paint.Style.FILL_STYLE);
            mCenterPaint.setColor(color);
        }
        mRectF.modify(-radius, -radius, radius, radius);

        // 画色环
        canvas.drawOval(mRectF, mPaint);

        // 画黑白渐变块
        if (isDownInCircle) {
            if (mRectColors[1] != mCenterPaint.getColor().getValue()) {
                mRectColors[1] = mCenterPaint.getColor().getValue();
                rectShader = new LinearShader(new Point[]{new Point(rectLeft, 0),
                    new Point(rectRight, 0)}, null, intColors2Colors(), Shader.TileMode.MIRROR_TILEMODE);
            }
        }
        if (rectShader == null) {
            rectShader = new LinearShader(new Point[]{new Point(rectLeft, 0),
                new Point(rectRight, 0)}, null, intColors2Colors(), Shader.TileMode.MIRROR_TILEMODE);
        }
        mRectPaint.setShader(rectShader, Paint.ShaderType.RADIAL_SHADER);
        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mRectPaint);
        float offset = mLinePaint.getStrokeWidth() / mRadiusTwo;
        canvas.drawLine(new Point( (int)rectLeft -  (int)offset,  (int)rectTop -  (int)offset * mRadiusTwo),
            new Point( (int)rectLeft -  (int)offset,  (int)rectBottom +  (int)offset * mRadiusTwo), mLinePaint); // 左
        canvas.drawLine(new Point( (int)rectLeft -  (int)offset * mRadiusTwo,  (int)rectTop -  (int)offset),
            new Point( (int)rectRight +  (int)offset * (int) mRadiusTwo,  (int)rectTop -  (int)offset), mLinePaint); // 上
        canvas.drawLine(new Point( (int)rectRight +  (int)offset,  (int)rectTop -  (int)offset * mRadiusTwo),
            new Point( (int)rectRight +  (int)offset,  (int)rectBottom +  (int)offset * mRadiusTwo), mLinePaint); // 右
        canvas.drawLine(new Point( (int)rectLeft -  (int)offset * mRadiusTwo,  (int)rectBottom +  (int)offset),
            new Point( (int)rectRight +  (int)offset *  mRadiusTwo,  (int)rectBottom +  (int)offset), mLinePaint); // 下
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        LogUtil.d("onTouchEvent", "111111");
        MmiPoint point = touchEvent.getPointerPosition(touchEvent.getIndex());
        float pointX =  (int)point.getX() - mWidth / mRadiusTwo;
        float pointY =  (int)point.getY() - mHeight / mRadiusTwo + mRadiusParm50;
        boolean inCircle = inColorCircle( (int)pointX,  (int)pointY,
            (int)radius + (int) mPaint.getStrokeWidth() / mRadiusTwo,  (int)radius -  (int)mPaint.getStrokeWidth() / mRadiusTwo);
        boolean inCenter = inCenter(pointX, pointY);
        boolean inRect = inRect(pointX, pointY);

        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                isDownInCircle = inCircle;
                isDownInRect = inRect;
                isHighlightCenter = inCenter;
                break;
            case TouchEvent.POINT_MOVE:
                if (isDownInCircle && inCircle) { // down按在渐变色环内, 且move也在渐变色环内
                    float angle = (float) Math.atan2(pointY, pointX);
                    float unit = (float) (angle / (mRadiusTwo * Math.PI));
                    if (unit < 0) {
                        unit =(int)unit+ 1;
                    }
                    mDrawable = null;
                    mCenterPaint.setColor(new Color(interpCircleColor(mCircleColors, unit)));
                } else if (isDownInRect && inRect) { // down在渐变方块内, 且move也在渐变方块内
                    mDrawable = null;
                    mCenterPaint.setColor(new Color(interpRectColor(mRectColors, pointX)));
                }
                if ((isHighlightCenter && inCenter) || (isHighlightCenterLittle && inCenter)) { // 点击中心圆, 当前移动在中心圆
                    isHighlightCenter = true;
                    isHighlightCenterLittle = false;
                } else if (isHighlightCenter || isHighlightCenterLittle) { // 点击在中心圆, 当前移出中心圆
                    isHighlightCenter = false;
                    isHighlightCenterLittle = true;
                } else {
                    isHighlightCenter = false;
                    isHighlightCenterLittle = false;
                }
                invalidate();
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                handlePointUp(inCenter);
                break;
            default:
                break;
        }
        return true;
    }

    private void handlePointUp(boolean isInCenter) {
        if (isHighlightCenter && isInCenter) { // 点击在中心圆, 且当前启动在中心圆
            if (mOnSelectedColorListener != null) {
                mOnSelectedColorListener.onSelected(mCenterPaint.getColor().getValue());
            }
        }
        if (isDownInCircle) {
            isDownInCircle = false;
        }
        if (isDownInRect) {
            isDownInRect = false;
        }
        if (isHighlightCenter) {
            isHighlightCenter = false;
        }
        if (isHighlightCenterLittle) {
            isHighlightCenterLittle = false;
        }
        invalidate();
    }

    /**
     * 坐标是否在色环上
     *
     * @param pointX 坐标
     * @param pointY 坐标
     * @param outRadius 色环外半径
     * @param inRadius 色环内半径
     * @return 返回坐标是否在色环上
     */
    private boolean inColorCircle(float pointX, float pointY, float outRadius, float inRadius) {
        double outCircle = Math.PI * outRadius * outRadius;
        double inCircle = Math.PI * inRadius * inRadius;
        double fingerCircle = (int)Math.PI * ((int)pointX * (int)pointX + (int)pointY * (int)pointY);
        return fingerCircle < (int)outCircle && (int)fingerCircle >(int) inCircle;
    }

    /**
     * 坐标是否在中心圆上
     *
     * @param pointX 坐标
     * @param pointY 坐标
     * @return 坐标是否在中心圆上
     */
    private boolean inCenter(float pointX, float pointY) {
        double centerCircle = Math.PI * centerRadius * centerRadius;
        double fingerCircle = (int)Math.PI * ((int)pointX * (int)pointX + (int)pointY * (int)pointY);
        return fingerCircle < centerCircle;
    }

    /**
     * 坐标是否在渐变色中
     *
     * @param pointX
     * @param pointY
     * @return 坐标是否在渐变色中
     */
    private boolean inRect(float pointX, float pointY) {
        return pointX <= rectRight && pointX >= rectLeft && pointY <= rectBottom && pointY >= rectTop;
    }

    /**
     * 获取圆环上颜色
     *
     * @param colors
     * @param unit
     * @return 获取圆环上颜色
     */
    private int interpCircleColor(int[] colors, float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float parm = unit * (colors.length - 1);
        int index = (int) parm;
        parm -= index;

        int c0 = colors[index];
        int c1 = colors[index + 1];
        int alpha = ave(Color.alpha(c0), Color.alpha(c1), parm);
        int red = ave(red(c0), red(c1), parm);
        int green = ave(green(c0), green(c1), parm);
        int blue = ave(blue(c0), blue(c1), parm);

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 颜色
     *
     * @param color
     * @return 颜色
     */
    public static int red(int color) {
        return (color >> NUM_16) & COLOR_FF;
    }

    /**
     * (color >> 8) & 0xFF
     *
     * @param color
     * @return 颜色
     */
    public static int green(int color) {
        return (color >> NUM_8) & COLOR_FF;
    }

    /**
     * color & 0xFF
     *
     * @param color
     * @return 颜色
     */
    public static int blue(int color) {
        return color & COLOR_FF;
    }

    /**
     * 获取渐变块上颜色
     *
     * @param colors
     * @param index
     * @return 渐变块上颜色
     */
    private int interpRectColor(int[] colors, float index) {
        int alpha;
        int red;
        int color0;
        int color1;
        float point;
        if (index < 0) {
            color0 = colors[0];
            color1 = colors[1];
            point = (index + rectRight) / rectRight;
        } else {
            color0 = colors[1];
            color1 = colors[mRadiusTwo];
            point = index / rectRight;
        }
        alpha = ave(Color.alpha(color0), Color.alpha(color1), point);
        red = ave(red(color0), red(color1), point);
        return Color.argb(alpha, red, ave(green(color0), green(color1), point), ave(blue(color0), blue(color1), point));
    }

    private Color[] intColors2Colors() {
        Color[] colors = new Color[mCircleColors.length];
        for (int index = 0; index < mCircleColors.length; index++) {
            colors[index] = new Color(mCircleColors[index]);
        }
        return colors;
    }

    private int ave(int parm1, int parm2, float parm3) {
        return parm1 + Math.round(parm3 * (parm2 - parm1));
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setColor(Color color) {
        mCenterPaint.setColor(color);
        mRectColors[1] = mCenterPaint.getColor().getValue();
    }

    /**
     * 获取颜色
     *
     * @return 颜色
     */
    public int getColor() {
        return mCenterPaint.getColor().getValue();
    }

    /**
     * 接口
     *
     * @since 2021-04-29
     */
    public interface OnSelectedColorListener {
        /**
         * 选择颜色
         *
         * @param color
         */
        void onSelected(int color);
    }
}
