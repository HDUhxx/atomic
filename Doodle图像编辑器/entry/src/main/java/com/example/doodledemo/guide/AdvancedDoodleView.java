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

package com.example.doodledemo.guide;

import com.example.doodle.ScaleGestureDetectorApi27;
import com.example.doodle.TouchGestureDetector;
import com.example.doodle.util.LogUtil;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 高级涂鸦
 * 支持对图片涂鸦, 可移动缩放图片
 *
 * @since 2021-04-14
 */
public class AdvancedDoodleView extends Component implements Component.LayoutRefreshedListener,
    Component.TouchEventListener {
    private static final String TAG = "AdvancedDoodleView";
    private static final int TWO = 2;
    private static final int WIDTH_DEF = 20;
    private static final float PIXEL_MAP_SCALE = 0.1f;
    private static final float FLOAT_TWO = 2f;
    private Paint mPaint = new Paint();
    private List<PathItem> mPathList = new ArrayList<>(); // 保存涂鸦轨迹的集合
    private TouchGestureDetector mTouchGestureDetector; // 触摸手势监听
    private float mLastX;
    private float mLastY;
    private PathItem mCurrentPathItem; // 当前的涂鸦轨迹
    private PathItem mSelectedPathItem; // 选中的涂鸦轨迹

    private PixelMap mPixelMap;
    private RectFloat mRectFloat;
    private float mPixelMapTransX;
    private float mPixelMapTransY;
    private float mPixelMapScale = 1;

    // 缩放手势操作相关
    private Float mLastFocusX;
    private Float mLastFocusY;
    private float mTouchCentreX;
    private float mTouchCentreY;

    private final int mScreenTransParamA = 4;
    private final int mScreenTransParamB = 9;
    private final Float mScreenTransParamC = 2f;
    int aj=0;

    private DrawTask mDrawTask = new DrawTask() {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            // 画布和图片共用一个坐标系，只需要处理屏幕坐标系到图片（画布）坐标系的映射关系(toX toY)
            canvas.translate(mPixelMapTransX, mPixelMapTransY);
            canvas.scale(mPixelMapScale, mPixelMapScale);

            // 绘制图片
            canvas.drawPixelMapHolder(new PixelMapHolder(mPixelMap), 0, 0, new Paint());

            for (PathItem path : mPathList) { // 绘制涂鸦轨迹
                canvas.save();
                canvas.translate(path.mX, path.mY); // 根据涂鸦轨迹偏移值，偏移画布使其画在对应位置上
                if (mSelectedPathItem == path) {
                    mPaint.setColor(Color.YELLOW); // 点中的为黄色
                } else {
                    mPaint.setColor(Color.RED); // 其他为红色
                }
                canvas.drawPath(path.mPath, mPaint);
                canvas.restore();
            }
        }
    };

    /**
     * 构造器
     *
     * @param context
     * @param pixelMap
     */
    public AdvancedDoodleView(Context context, PixelMap pixelMap) {
        super(context);
        mPixelMap = pixelMap;
        mRectFloat = new RectFloat();
        int screenwidth = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes().width;
        int screenheight = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes().height;

        mPixelMapTransX = (screenwidth - screenheight * mScreenTransParamA / mScreenTransParamB) / mScreenTransParamC;

        // 设置画笔
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setStrokeWidth(WIDTH_DEF);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);

        setLayoutRefreshedListener(this);
        setTouchEventListener(this);
        initTouchGestureDetector();

        // 针对涂鸦的手势参数设置
        // 下面两行绘画场景下应该设置间距为大于等于1，否则设为0双指缩放后抬起其中一个手指仍然可以移动
        mTouchGestureDetector.setScaleSpanSlop(1); // 手势前识别为缩放手势的双指滑动最小距离值
        mTouchGestureDetector.setScaleMinSpan(1); // 缩放过程中识别为缩放手势的双指最小距离值
        mTouchGestureDetector.setIsLongpressEnabled(false);
        mTouchGestureDetector.setIsScrollAfterScaled(false);
        addDrawTask(mDrawTask);
    }

    private void initTouchGestureDetector() {
        // 由手势识别器处理手势
        mTouchGestureDetector = new TouchGestureDetector(getContext(),
            new TouchGestureDetector.OnTouchGestureListener() {
                @Override
                public boolean onScaleBegin(ScaleGestureDetectorApi27 detector) {
                    LogUtil.d(TAG, "onScaleBegin: ");
                    mLastFocusX = null;
                    mLastFocusY = null;
                    return true;
                }

                @Override
                public void onScaleEnd(ScaleGestureDetectorApi27 detector) {
                    LogUtil.d(TAG, "onScaleEnd: ");
                }

                @Override
                public boolean onScale(ScaleGestureDetectorApi27 detector) { // 双指缩放中
                    LogUtil.d(TAG, "onScale: ");
                    updateScale(detector);
                    invalidate();
                    return true;
                }

                @Override
                public boolean onSingleTapUp(TouchEvent event) { // 单击选中
                    updateSingleTapUp(event, mRectFloat);
                    invalidate();
                    return true;
                }

                @Override
                public void onScrollBegin(TouchEvent event) { // 滑动开始
                    LogUtil.d(TAG, "onScrollBegin: ");
                    updateScrollBegin(event);
                    invalidate(); // 刷新
                }

                @Override
                public boolean onScroll(TouchEvent e1, TouchEvent e2, float distanceX, float distanceY) { // 滑动中
                    LogUtil.d(TAG, "onScroll: " + e2.getPointerPosition(0).getX()
                        + " " + e2.getPointerPosition(0).getY());
                    updateScroll(e2);
                    invalidate(); // 刷新
                    return true;
                }

                @Override
                public void onScrollEnd(TouchEvent event) { // 滑动结束
                    LogUtil.d(TAG, "onScrollEnd: ");
                    updateScrollEnd(event);
                    invalidate(); // 刷新
                }
            });
    }

    private void updateScale(ScaleGestureDetectorApi27 detector) {
        // 屏幕上的焦点
        mTouchCentreX = detector.getFocusX();
        mTouchCentreY = detector.getFocusY();

        if (mLastFocusY != null && mLastFocusX != null) { // 焦点改变
            float dx = mTouchCentreX - mLastFocusX;
            float dy = mTouchCentreY - mLastFocusY;

            // 移动图片
            mPixelMapTransX = mPixelMapTransX + dx;
            mPixelMapTransY = mPixelMapTransY + dy;
        }

        // 缩放图片
        mPixelMapScale = mPixelMapScale * detector.getScaleFactor();
        if (mPixelMapScale < PIXEL_MAP_SCALE) {
            mPixelMapScale = PIXEL_MAP_SCALE;
        }
        invalidate();

        mLastFocusX = mTouchCentreX;
        mLastFocusY = mTouchCentreY;
    }

    private void updateSingleTapUp(TouchEvent event, RectFloat rectFloat) {
        float pointX = toX(event.getPointerPosition(0).getX());
        float pointY = toY(event.getPointerPosition(0).getY());
        boolean isFound = false;
        for (PathItem path : mPathList) { // 绘制涂鸦轨迹
            path.mPath.computeBounds(rectFloat); // 计算涂鸦轨迹的矩形范围
            // path.mPath.offset(path.mX, path.mY); // 加上偏移
            rectFloat.isInclude(path.mX, path.mY);
            if (rectFloat.isInclude(pointX, pointY)) { // 判断是否点中涂鸦轨迹的矩形范围内
                isFound = true;
                mSelectedPathItem = path;
                break;
            }
        }
        if (!isFound) { // 没有点中任何涂鸦
            mSelectedPathItem = null;
        }
    }

    private void updateScrollBegin(TouchEvent event) {
        float pointX = toX(event.getPointerPosition(0).getX());
        float pointY = toY(event.getPointerPosition(0).getY());
        if (mSelectedPathItem == null) {
            mCurrentPathItem = new PathItem(); // 新的涂鸦
            mPathList.add(mCurrentPathItem); // 添加的集合中
            mCurrentPathItem.mPath.moveTo(pointX, pointY);
        }
        mLastX = pointX;
        mLastY = pointY;
    }

    private void updateScroll(TouchEvent e2) {
        float pointX = toX(e2.getPointerPosition(0).getX());
        float pointY = toY(e2.getPointerPosition(0).getY());
        if (mSelectedPathItem == null) { // 没有选中的涂鸦
            mCurrentPathItem.mPath.quadTo(
                mLastX,
                mLastY,
                (pointX + mLastX) / TWO,
                (pointY + mLastY) / TWO); // 使用贝塞尔曲线 让涂鸦轨迹更圆滑
        } else { // 移动选中的涂鸦
            if(aj==0){
                aj++;
            }else {

                mSelectedPathItem.mX = mSelectedPathItem.mX + pointX - mLastX;
                mSelectedPathItem.mY = mSelectedPathItem.mY + pointY - mLastY;
            }

        }
        mLastX = pointX;
        mLastY = pointY;
    }

    private void updateScrollEnd(TouchEvent event) {
        if (event != null) {
            float pointX = toX(event.getPointerPosition(event.getIndex()).getX());
            float pointY = toY(event.getPointerPosition(event.getIndex()).getY());
            if (mSelectedPathItem == null) {
                mCurrentPathItem.mPath.quadTo(
                    mLastX,
                    mLastY,
                    (pointX + mLastX) / TWO,
                    (pointY + mLastY) / TWO); // 使用贝塞尔曲线 让涂鸦轨迹更圆滑
                mCurrentPathItem = null; // 轨迹结束
            }
            mSelectedPathItem=null;
            aj=0;
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        mTouchGestureDetector.onTouchEvent(touchEvent); // 由手势识别器处理手势
        return true;
    }

    /**
     * 将屏幕触摸坐标x转换成在图片中的坐标
     *
     * @param touchX
     * @return 图片中的坐标
     */
    public final float toX(float touchX) {
        return (touchX - mPixelMapTransX) / mPixelMapScale;
    }

    /**
     * 将屏幕触摸坐标y转换成在图片中的坐标
     *
     * @param touchY
     * @return 图片中的坐标
     */
    public final float toY(float touchY) {
        return (touchY - mPixelMapTransY) / mPixelMapScale;
    }

    // view绘制完成时 大小确定
    @Override
    public void onRefreshed(Component component) { // 用来替代onSizeChanged（）
        int width = mPixelMap.getImageInfo().size.width;
        int height = mPixelMap.getImageInfo().size.height;
        float nw = width * 1f / getWidth();
        float nh = height * 1f / getHeight();
        float centerWidth;
        float centerHeight;

        // 1.计算使图片居中的缩放值
        if (nw > nh) {
            mPixelMapScale = 1 / nw;
            centerWidth = getWidth();
            centerHeight = (int) (height * mPixelMapScale);
        } else {
            mPixelMapScale = 1 / nh;
            centerWidth = (int) (width * mPixelMapScale);
            centerHeight = getHeight();
        }

        // 2.计算使图片居中的偏移值
        mPixelMapTransY = (getWidth() - centerWidth) / FLOAT_TWO;
        mPixelMapTransY = (getHeight() - centerHeight) / FLOAT_TWO;
        invalidate();
    }

    /**
     * 封装涂鸦轨迹对象
     *
     * @since 2021-04-27
     */
    private static class PathItem {
        float mX;
        float mY; // 轨迹偏移值
        Path mPath = new Path(); // 涂鸦轨迹
    }
}
