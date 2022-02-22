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

package com.example.doodle;

import static com.example.doodle.util.DrawUtil.drawCircle;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;

/**
 * 仿制的定位器
 *
 * @since 2021-04-29
 */
public class CopyLocation {
    private float mCopyStartX;
    private float mCopyStartY; // 仿制的坐标
    private float mTouchStartX;
    private float mTouchStartY; // 开始触摸的坐标
    private float mX;
    private float mY; // 当前位置

    private Paint mPaint;

    private boolean mIsRelocating = true; // 正在定位中
    private boolean mIsCopying = false; // 正在仿制绘图中

    /**
     * 初始化
     */
    public CopyLocation() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_STYLE);
        mPaint.setStrokeJoin(Paint.Join.ROUND_JOIN);
    }

    public float getTouchStartX() {
        return mTouchStartX;
    }

    public float getTouchStartY() {
        return mTouchStartY;
    }

    public float getCopyStartX() {
        return mCopyStartX;
    }

    public float getCopyStartY() {
        return mCopyStartY;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public boolean isCopying() {
        return mIsCopying;
    }

    public boolean isRelocating() {
        return mIsRelocating;
    }

    public void setCopying(boolean isCopying) {
        mIsCopying = isCopying;
    }

    public void setRelocating(boolean isRelocating) {
        mIsRelocating = isRelocating;
    }

    /**
     * updateLocation
     *
     * @param pointX
     * @param pointY
     */
    public void updateLocation(float pointX, float pointY) {
        mX = pointX;
        mY = pointY;
    }

    /**
     * setStartPosition
     *
     * @param touchStartX
     * @param touchStartY
     */
    public void setStartPosition(float touchStartX, float touchStartY) {
        setStartPosition(touchStartX, touchStartY, mX, mY);
    }

    /**
     * setStartPosition
     *
     * @param touchStartX
     * @param touchStartY
     * @param copyStartX
     * @param copyStartY
     */
    public void setStartPosition(float touchStartX, float touchStartY, float copyStartX, float copyStartY) {
        mCopyStartX = copyStartX;
        mCopyStartY = copyStartY;
        mTouchStartX = touchStartX;
        mTouchStartY = touchStartY;
    }

    /**
     * drawItSelf
     *
     * @param canvas
     * @param size
     */
    public void drawItSelf(Canvas canvas, float size) {
        final int color1 = 0xaa666666;
        final int color2 = 0xaaffffff;
        final int color3 = 0x44ff0000;
        final int color4 = 0x44000088;
        final int mag = 4;
        final int var2 = 2;
        final int var8 = 8;
        final int var16 = 16;
        final int var32 = 32;
        mPaint.setStrokeWidth(size / mag);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setColor(new Color(color1)); // 灰色
        drawCircle(canvas, mX, mY, size / var2 + size / var8, mPaint);

        mPaint.setStrokeWidth(size / var16);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setColor(new Color(color2)); // 白色
        drawCircle(canvas, mX, mY, size / var2 + size / var32, mPaint);

        mPaint.setStyle(Paint.Style.FILL_STYLE);
        if (!mIsCopying) {
            mPaint.setColor(new Color(color3)); // 红色
            drawCircle(canvas, mX, mY, size / var2, mPaint);
        } else {
            mPaint.setColor(new Color(color4)); // 蓝色
            drawCircle(canvas, mX, mY, size / var2, mPaint);
        }
    }

    /**
     * 判断是否点中
     *
     * @param x
     * @param y
     * @param paintSize
     * @return boolean
     */
    public boolean contains(float x, float y, float paintSize) {
        return (mX - x) * (mX - x) + (mY - y) * (mY - y) <= paintSize * paintSize;
    }

    /**
     * copy
     *
     * @return CopyLocation
     */
    public CopyLocation copy() {
        CopyLocation copyLocation = new CopyLocation();
        copyLocation.mCopyStartX = mCopyStartX;
        copyLocation.mCopyStartY = mCopyStartY;
        copyLocation.mTouchStartX = mTouchStartX;
        copyLocation.mTouchStartY = mTouchStartY;
        copyLocation.mX = mX;
        copyLocation.mY = mY;
        return copyLocation;
    }

    /**
     * reset
     */
    public void reset() {
        mCopyStartX = 0;
        mCopyStartY = 0;
        mTouchStartX = 0;
        mTouchStartY = 0;
        mX = 0;
        mY = 0;
        mIsRelocating = true; // 正在定位中
        mIsCopying = false; // 正在仿制绘图中
    }
}

