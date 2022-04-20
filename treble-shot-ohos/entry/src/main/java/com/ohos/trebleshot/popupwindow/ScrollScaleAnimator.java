package com.ohos.trebleshot.popupwindow;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
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

public class ScrollScaleAnimator extends PopupAnimator {

    private float startAlpha = 0f;
    private float startScale = 0f;
    public boolean isOnlyScaleX = false;

    public ScrollScaleAnimator(Component target, PopupAnimation popupAnimation) {
        super(target, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setAlpha(startAlpha);
        targetView.setScaleX(startScale);
        if (!isOnlyScaleX) {
            targetView.setScaleY(startScale);
        }
        // 设置参考点
        applyPivot();
    }

    private void applyPivot() {
        switch (popupAnimation) {
            case ScrollAlphaFromLeft:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getHeight() / 2);
                break;
            case ScrollAlphaFromLeftTop:
                targetView.setPivotX(0f);
                targetView.setPivotY(0f);
                break;
            case ScrollAlphaFromTop:
                targetView.setPivotX(targetView.getWidth() / 2);
                targetView.setPivotY(0f);
                break;
            case ScrollAlphaFromRightTop:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(0f);
                break;
            case ScrollAlphaFromRight:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(targetView.getHeight() / 2);
                break;
            case ScrollAlphaFromRightBottom:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(targetView.getHeight());
                break;
            case ScrollAlphaFromBottom:
                targetView.setPivotX(targetView.getWidth() / 2);
                targetView.setPivotY(targetView.getHeight());
                break;
            case ScrollAlphaFromLeftBottom:
                targetView.setPivotX(0);
                targetView.setPivotY(targetView.getHeight());
                break;
            default:
                break;
        }
    }

    @Override
    public void animateShow() {
        AnimatorValue animator = new AnimatorValue();
        animator.setDuration(350);
        animator.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
        animator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float value) {
                targetView.setAlpha(value);
                targetView.setScaleX(value);
                if (!isOnlyScaleX) {
                    targetView.setScaleY(value);
                }
            }
        });
        animator.start();
    }

    @Override
    public void animateDismiss() {
        AnimatorValue animator = new AnimatorValue();
        animator.setDuration(350);
        animator.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
        animator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float value) {
                // 旋转imageView
                targetView.setAlpha(1 - value);
                targetView.setScaleX(1 - value);
                if (!isOnlyScaleX) {
                    targetView.setScaleY(1 - value);
                }
            }
        });
        animator.start();
    }

}
