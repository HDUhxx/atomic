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
package com.lxj.xpopup.animator;

import com.lxj.xpopup.enums.PopupAnimation;
import ohos.agp.animation.Animator;
import ohos.agp.components.Component;

/**
 * Description: 缩放，不带透明
 */
public class ScaleAnimator extends PopupAnimator {

    float startScale = .75f;

    public ScaleAnimator(Component target, int animationDuration, PopupAnimation popupAnimation) {
        super(target, animationDuration, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setScaleX(startScale);
        targetView.setScaleY(startScale);
        // 设置动画参考点
        applyPivot();
    }

    /**
     * 根据不同的PopupAnimation来设定对应的pivot
     */
    private void applyPivot() {
        switch (popupAnimation) {
            case ScaleFromCenter:
                targetView.setPivotX(targetView.getWidth() / 2);
                targetView.setPivotY(targetView.getHeight() / 2);
                break;
            case ScaleFromLeftTop:
                targetView.setPivotX(0);
                targetView.setPivotY(0);
                break;
            case ScaleFromRightTop:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(0f);
                break;
            case ScaleFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getHeight());
                break;
            case ScaleFromRightBottom:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(targetView.getHeight());
                break;
            default:
                break;
        }

    }

    @Override
    public void animateShow() {
        targetView.createAnimatorProperty().scaleX(1.0f).scaleY(1.0f)
                .setDuration(animationDuration)
                .setCurveType(Animator.CurveType.OVERSHOOT)
                .start();
    }

    @Override
    public void animateDismiss() {
        if (animating) {
            return;
        }
        observerAnimator(targetView.createAnimatorProperty().scaleX(startScale).scaleY(startScale)
                .setDuration(animationDuration))
                .setCurveType(Animator.CurveType.ACCELERATE_DECELERATE)
                .start();
    }

}
