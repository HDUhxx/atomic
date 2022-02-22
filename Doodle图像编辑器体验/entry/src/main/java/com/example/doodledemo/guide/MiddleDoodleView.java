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

import com.example.doodle.TouchGestureDetector;
import com.example.doodle.util.LogUtil;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 中级涂鸦
 * 单击时可以选择某个涂鸦，进行移动
 *
 * @since 2021-04-14
 */
public class MiddleDoodleView extends Component implements Component.DrawTask, Component.TouchEventListener {
    private static final String TAG = "MiddleDoodleView";

    private Paint mPaint = new Paint();
    private List<PathItem> mPathList = new ArrayList<>(); // 保存涂鸦轨迹的集合
    private float mLastX;
    private float mLastY;
    private PathItem mCurrentPathItem; // 当前的涂鸦轨迹
    private PathItem mSelectedPathItem; // 选中的涂鸦轨迹
    int aj = 0;

    private final int mPaintStrokeWidth = 20;
    private final int mCoorParm = 2;
    private TouchGestureDetector mTouchGestureDetector = new TouchGestureDetector(getContext(),
        new TouchGestureDetector.OnTouchGestureListener() {
            RectFloat mRectF = new RectFloat();

            @Override
            public boolean onSingleTapUp(TouchEvent e) { // 单击选中
                boolean isFound = false;
                for (PathItem path : mPathList) { // 绘制涂鸦轨迹
                    path.mPath.computeBounds(mRectF); // 计算涂鸦轨迹的矩形范围
//                    path.mPath.offset(path.mX, path.mY); // 替代 mRectF.offset
                    if (mRectF.isInclude(e.getPointerPosition(e.getIndex()).getX(),
                        e.getPointerPosition(e.getIndex()).getY())) { // 判断是否点中涂鸦轨迹的矩形范围内
                        isFound = true;
                        mSelectedPathItem = path;
                        break;
                    }
                }
                if (!isFound) { // 没有点中任何涂鸦
                    mSelectedPathItem = null;
                }
                invalidate();
                return true;
            }

            @Override
            public void onScrollBegin(TouchEvent e) { // 滑动开始
                LogUtil.d(TAG, "onScrollBegin: ");
                if (mSelectedPathItem == null) {
                    mCurrentPathItem = new PathItem(); // 新的涂鸦
                    mPathList.add(mCurrentPathItem); // 添加的集合中
                    mCurrentPathItem.mPath.moveTo(e.getPointerPosition(0).getX(), e.getPointerPosition(0).getY());
                    mLastX = e.getPointerPosition(0).getX();
                    mLastY = e.getPointerPosition(0).getY();
                }
                invalidate(); // 刷新
            }

            @Override
            public boolean onScroll(TouchEvent e1, TouchEvent e2, float distanceX, float distanceY) { // 滑动中
                LogUtil.d(TAG, "onScroll: " + e2.getPointerPosition(0).getX() + " " + e2.getPointerPosition(0).getY());
                if (mSelectedPathItem == null) { // 没有选中的涂鸦
                    mCurrentPathItem.mPath.quadTo(
                        mLastX,
                        mLastY,
                        (e2.getPointerPosition(0).getX() + mLastX) / mCoorParm,
                        (e2.getPointerPosition(0).getY() + mLastY) / mCoorParm); // 使用贝塞尔曲线 让涂鸦轨迹更圆滑
                    mLastX = e2.getPointerPosition(0).getX();
                    mLastY = e2.getPointerPosition(0).getY();
                } else { // 移动选中的涂鸦
                    if (aj == 0) {
                        aj++;
                    } else {
                        mSelectedPathItem.mX = mSelectedPathItem.mX - distanceX;
                        mSelectedPathItem.mY = mSelectedPathItem.mY - distanceY;
                    }
                }
                invalidate(); // 刷新
                return true;
            }

            @Override
            public void onScrollEnd(TouchEvent e) { // 滑动结束
                setOnScrollEnd(e);
            }
        });

    /**
     * 构造函数
     *
     * @param context
     */
    public MiddleDoodleView(Context context) {
        super(context);
        mPaint.setColor(Color.RED);

        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setStrokeWidth(mPaintStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);

        addDrawTask(this);
        setTouchEventListener(this);

        // 由手势识别器处理手势
    }

    private void setOnScrollEnd(TouchEvent e) {
        LogUtil.d(TAG, "onScrollEnd: ");
        if (e != null) {
            if (mSelectedPathItem == null) {
                mCurrentPathItem.mPath.quadTo(
                    mLastX,
                    mLastY,
                    (e.getPointerPosition(e.getIndex()).getX() + mLastX) / mCoorParm,
                    (e.getPointerPosition(e.getIndex()).getY() + mLastY) / mCoorParm); // 使用贝塞尔曲线 让涂鸦轨迹更圆滑
                mCurrentPathItem = null; // 轨迹结束
            }

            aj = 0;
            mSelectedPathItem = null;
            invalidate(); // 刷新
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        mTouchGestureDetector.onTouchEvent(touchEvent); // 由手势识别器处理手势
        /*if (!isConsumed) {
                    return super.dispatchTouchEvent(event);
        }*/
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        for (PathItem path : mPathList) { // 绘制涂鸦轨迹
            canvas.save(); // 1.保存画布状态，下面要变换画布
            canvas.translate(path.mX, path.mY); // 根据涂鸦轨迹偏移值，偏移画布使其画在对应位置上
            if (mSelectedPathItem == path) {
                mPaint.setColor(Color.YELLOW); // 点中的为黄色
            } else {
                mPaint.setColor(Color.RED); // 其他为红色
            }
            canvas.drawPath(path.mPath, mPaint);
            canvas.restore(); // 2.恢复画布状态，绘制完一个涂鸦轨迹后取消上面的画布变换，不影响下一个
        }
    }

    /**
     * 封装涂鸦轨迹对象
     *
     * @since 2021-04-14
     */
    private static class PathItem {
        Path mPath = new Path(); // 涂鸦轨迹
        float mX; // 轨迹偏移值
        float mY; // 轨迹偏移值
    }
}
