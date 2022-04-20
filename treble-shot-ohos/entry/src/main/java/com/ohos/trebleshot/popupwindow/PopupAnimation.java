package com.ohos.trebleshot.popupwindow;

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public enum PopupAnimation {
    // 滑动 + 透明渐变
    ScrollAlphaFromLeft,
    ScrollAlphaFromLeftTop,
    ScrollAlphaFromTop,
    ScrollAlphaFromRightTop,
    ScrollAlphaFromRight,
    ScrollAlphaFromRightBottom,
    ScrollAlphaFromBottom,
    ScrollAlphaFromLeftBottom,

    // 禁用动画
    NoAnimation
}
