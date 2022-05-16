package com.lxj.xpopup.animator;

import com.lxj.xpopup.enums.PopupAnimation;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;

/**
 * Description: 像系统的PopupMenu那样的动画
 * Create by lxj, at 2018/12/12
 */
public class ScrollScaleAnimator extends PopupAnimator {

    private float startAlpha = 0f;
    private float startScale = 0f;
    public boolean isOnlyScaleX = false;

    public ScrollScaleAnimator(Component target, int animationDuration, PopupAnimation popupAnimation) {
        super(target, animationDuration, popupAnimation);
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
        animator.setDuration(animationDuration);
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
        if (animating) {
            return;
        }
        AnimatorValue animator = new AnimatorValue();
        observerAnimator(animator);
        animator.setDuration(animationDuration);
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
