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

package com.example.doodle;

import ohos.agp.animation.AnimatorValue;

/**
 * 数值属性动画，只支持float和int类型
 *
 * @since 2021-04-23
 */
public class ValueAnimator extends AnimatorValue {
    private AnimatorUpdateListener updateListener;
    private final Object[] values = new Object[2];

    /**
     * 构造一个动画实例
     *
     * @param start 开始值
     * @param end 结束值
     * @return 动画实例
     */
    public static ValueAnimator ofFloat(float start, float end) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(start, end);
        return valueAnimator;
    }

    /**
     * 构造一个动画实例
     *
     * @param start 开始值
     * @param end 结束值
     * @return 动画实例
     */
    public static ValueAnimator ofInt(int start, int end) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(start, end);
        return valueAnimator;
    }

    /**
     * 构造方法
     */
    public ValueAnimator() {
        setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                if (updateListener != null) {
                    Object value = values[0];
                    if (value != null) {
                        if (value instanceof Integer) {
                            int start = (int) values[0];
                            int end = (int) values[1];
                            value = start + v * (end - start);
                        } else {
                            float start = (float) values[0];
                            float end = (float) values[1];
                            value = start + v * (end - start);
                        }
                    }
                    updateListener.onAnimationUpdate(animatorValue, v, value);
                }
            }
        });
    }

    /**
     * 设置int值的变化范围
     *
     * @param start 开始值
     * @param end 结束值
     */
    public void setIntValues(int start, int end) {
        values[0] = start;
        values[1] = end;
    }

    /**
     * 设置float值的变化范围
     *
     * @param start 开始值
     * @param end 结束值
     */
    public void setFloatValues(float start, float end) {
        values[0] = start;
        values[1] = end;
    }

    /**
     * float和int类型的数值属性动画的监听器
     *
     * @since 2021-04-23
     */
    public interface AnimatorUpdateListener {
        /**
         * 动画值的监听器
         *
         * @param animatorValue ohos的属性动画
         * @param fraction 动画当前进度百分比
         * @param animatedValue 当前的值
         */
        void onAnimationUpdate(AnimatorValue animatorValue, float fraction, Object animatedValue);
    }

    /**
     * 设置数值属性动画监听器
     *
     * @param listener 数值属性动画监听器
     */
    public void addUpdateListener(AnimatorUpdateListener listener) {
        this.updateListener = listener;
    }
}
