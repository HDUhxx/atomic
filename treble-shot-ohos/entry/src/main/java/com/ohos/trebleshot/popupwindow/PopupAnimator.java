package com.ohos.trebleshot.popupwindow;

import ohos.agp.components.Component;

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

public abstract class PopupAnimator {

    public Component targetView;
    public PopupAnimation popupAnimation; // 内置的动画

    public PopupAnimator(Component target) {
        this(target, null);
    }

    public PopupAnimator(Component target, PopupAnimation popupAnimation) {
        this.targetView = target;
        this.popupAnimation = popupAnimation;
    }

    public abstract void initAnimator();

    public abstract void animateShow();

    public abstract void animateDismiss();
}
