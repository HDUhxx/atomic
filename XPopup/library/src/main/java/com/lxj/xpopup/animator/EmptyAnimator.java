package com.lxj.xpopup.animator;

import ohos.agp.components.Component;

/**
 * Description: 没有动画效果的动画器
 * Create by dance, at 2019/6/6
 */
public class EmptyAnimator extends PopupAnimator {

    public EmptyAnimator(Component target, int animationDuration) {
        super(target, animationDuration);
    }

    @Override
    public void initAnimator() {
        targetView.setAlpha(0.0f);
    }

    @Override
    public void animateShow() {
        targetView.createAnimatorProperty().alpha(1.0f).setDuration(animationDuration).start();
    }

    @Override
    public void animateDismiss() {
        if (animating) {
            return;
        }
        observerAnimator(targetView.createAnimatorProperty().alpha(0.0f).setDuration(animationDuration)).start();
    }

}
