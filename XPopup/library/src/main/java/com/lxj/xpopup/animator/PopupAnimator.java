package com.lxj.xpopup.animator;

import com.lxj.xpopup.enums.PopupAnimation;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;

/**
 * Description: 弹窗动画执行器
 * Create by dance, at 2018/12/9
 */
public abstract class PopupAnimator {

    protected boolean animating = false;
    public Component targetView;
    public int animationDuration = 0;
    public PopupAnimation popupAnimation; // 内置的动画

    public PopupAnimator() {
    }

    public PopupAnimator(Component target, int animationDuration) {
        this(target, animationDuration, null);
    }

    public PopupAnimator(Component target, int animationDuration, PopupAnimation popupAnimation) {
        this.targetView = target;
        this.animationDuration = animationDuration;
        this.popupAnimation = popupAnimation;
    }

    public abstract void initAnimator();

    public abstract void animateShow();

    public abstract void animateDismiss();

    public int getDuration() {
        return animationDuration;
    }

    protected AnimatorValue observerAnimator(AnimatorValue animator) {
        animator.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                animating = false;
            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });
        return animator;
    }

    protected AnimatorProperty observerAnimator(AnimatorProperty animator) {
        animator.setStateChangedListener(new Animator.StateChangedListener() {
            @Override
            public void onStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onStop(Animator animator) {

            }

            @Override
            public void onCancel(Animator animator) {

            }

            @Override
            public void onEnd(Animator animator) {
                animating = false;
            }

            @Override
            public void onPause(Animator animator) {

            }

            @Override
            public void onResume(Animator animator) {

            }
        });
        return animator;
    }

}
