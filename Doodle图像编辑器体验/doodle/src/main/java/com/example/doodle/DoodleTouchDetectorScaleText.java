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

import ohos.app.Context;

import com.example.doodle.core.IDoodleTouchDetector;

/**
 * @since 2021-05-15
 */
public class DoodleTouchDetectorScaleText extends TouchGestureDetectorScaleText implements IDoodleTouchDetector {
    /**
     * DoodleTouchDetector
     *
     * @param context
     * @param listener
     */
    public DoodleTouchDetectorScaleText(Context context, TouchGestureDetectorScaleText.IOnTouchGestureListener listener) {
        super(context, listener);
        // 下面两行绘画场景下应该设置间距为大于等于1，否则设为0双指缩放后抬起其中一个手指仍然可以移动
        this.setScaleSpanSlop(1); // 手势前识别为缩放手势的双指滑动最小距离值
        this.setScaleMinSpan(1); // 缩放过程中识别为缩放手势的双指最小距离值

        this.setIsLongpressEnabled(false);
        this.setIsScrollAfterScaled(false);
    }
}