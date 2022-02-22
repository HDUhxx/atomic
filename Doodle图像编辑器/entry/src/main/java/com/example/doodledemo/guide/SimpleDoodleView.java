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
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 初级涂鸦
 * 没有图片 仅支持 手绘
 *
 * @since 2021-04-14
 */
public class SimpleDoodleView extends Component {
    private static final String TAG = SimpleDoodleView.class.getSimpleName();

    private Paint mPaint = new Paint();
    private List<Path> mPathList = new ArrayList<>(); // 保存涂鸦轨迹的集合
    private TouchGestureDetector mTouchGestureDetector; // 触摸手势监听
    private float mLastX;
    private float mLastY;
    private Path mCurrentPath; // 当前的涂鸦轨迹
    private final int mStrokeWidth = 20;
    private final int mCoorParm = 2;
    private TouchEventListener mTouchEventListener = new TouchEventListener() {
        @Override
        public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
            mTouchGestureDetector.onTouchEvent(touchEvent); // 由手势识别器处理手势
            // if (!isConsumed) {
            // return super.dispatchTouchEvent(event);
            // }
            return true;
        }
    };
    private DrawTask mDrawTask = new DrawTask() {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            for (Path path : mPathList) { // 绘制涂鸦轨迹
                canvas.drawPath(path, mPaint);
            }
        }
    };

    /**
     * 构造函数
     *
     * @param context
     */
    public SimpleDoodleView(Context context) {
        super(context);

        // 设置画笔
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE_STYLE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        setTouchEventListener(mTouchEventListener);
        addDrawTask(mDrawTask);

        // 由手势识别器处理手势
        mTouchGestureDetector = new TouchGestureDetector(getContext(),
            new TouchGestureDetector.OnTouchGestureListener() {
                @Override
                public void onScrollBegin(TouchEvent e) { // 滑动开始
                    LogUtil.d(TAG, "onScrollBegin: ");
                    mCurrentPath = new Path(); // 新的涂鸦
                    mPathList.add(mCurrentPath); // 添加的集合中
                    mCurrentPath.moveTo(e.getPointerPosition(0).getX(), e.getPointerPosition(0).getY());
                    mLastX = e.getPointerPosition(0).getX();
                    mLastY = e.getPointerPosition(0).getY();
                    invalidate(); // 刷新
                }

                @Override
                public boolean onScroll(TouchEvent e1, TouchEvent e2, float distanceX, float distanceY) { // 滑动中
                    LogUtil.d(TAG, "onScroll");
                    mCurrentPath.quadTo(
                        mLastX,
                        mLastY,
                        (e2.getPointerPosition(0).getX() + mLastX) / mCoorParm,
                        (e2.getPointerPosition(0).getY() + mLastY) / mCoorParm); // 使用贝塞尔曲线 让涂鸦轨迹更圆滑
                    mLastX = e2.getPointerPosition(0).getX();
                    mLastY = e2.getPointerPosition(0).getY();
                    invalidate(); // 刷新
                    return true;
                }

                @Override
                public void onScrollEnd(TouchEvent e) { // 滑动结束
                    setOnScrollEnd(e);
                }
            });
    }

    private void setOnScrollEnd(TouchEvent e) {
        LogUtil.d(TAG, "onScrollEnd: ");
        if (e != null) {
            mCurrentPath.quadTo(
                mLastX,
                mLastY,
                (e.getPointerPosition(e.getIndex()).getX() + mLastX) / mCoorParm,
                (e.getPointerPosition(e.getIndex()).getY() + mLastY) / mCoorParm); // 使用贝塞尔曲线 让涂鸦轨迹更圆滑
            mCurrentPath = null; // 轨迹结束
            invalidate(); // 刷新
        }
    }
}