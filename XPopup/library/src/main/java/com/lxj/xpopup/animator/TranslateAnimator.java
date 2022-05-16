package com.lxj.xpopup.animator;

import com.lxj.xpopup.enums.PopupAnimation;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;

/**
 * Description: 平移动画，不带渐变
 * Create by dance, at 2018/12/9
 */
public class TranslateAnimator extends PopupAnimator {

    // 动画起始坐标
    private float startTranslationX = 0;
    private float startTranslationY = 0;
    private float endTranslationX = 0;
    private float endTranslationY = 0;
    private float offsetX = 0;
    private float offsetY = 0;
    private int oldWidth;
    private int oldHeight;

    public TranslateAnimator(Component target, int animationDuration, PopupAnimation popupAnimation) {
        super(target, animationDuration, popupAnimation);
    }

    @Override
    public void initAnimator() {
        applyTranslation();
        oldWidth = targetView.getWidth();
        oldHeight = targetView.getHeight();
    }

    private void applyTranslation() {
        offsetX = targetView.getTranslationX();
        offsetY = targetView.getTranslationY();
        switch (popupAnimation) {
            case TranslateFromLeft:
                endTranslationX = targetView.getLeft();
                targetView.setTranslationX(-targetView.getRight());
                startTranslationX = -targetView.getWidth();
                break;
            case TranslateFromRight:
                endTranslationX = targetView.getLeft();
                targetView.setTranslationX(((Component) targetView.getComponentParent()).getWidth() - targetView.getLeft());
                startTranslationX = ((Component) targetView.getComponentParent()).getWidth();
                break;
            case TranslateFromTop:
                endTranslationY = targetView.getTop();
                targetView.setTranslationY(-targetView.getBottom());
                startTranslationY = -targetView.getHeight();
                break;
            case TranslateFromBottom:
                endTranslationY = targetView.getTop();
                targetView.setTranslationY(((Component) targetView.getComponentParent()).getHeight() - targetView.getTop());
                startTranslationY = ((Component) targetView.getComponentParent()).getHeight();
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
                switch (popupAnimation) {
                    case TranslateFromLeft:
                    case TranslateFromRight:
                        targetView.setTranslationX((1 - value) * (startTranslationX - endTranslationX) + offsetX);
                        break;
                    case TranslateFromTop:
                    case TranslateFromBottom:
                        targetView.setTranslationY((1 - value) * (startTranslationY - endTranslationY) + offsetY);
                        break;
                    default:
                        break;
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
        // 执行消失动画的时候，宽高可能改变了，所以需要修正动画的起始值
        switch (popupAnimation) {
            case TranslateFromLeft:
                startTranslationX -= targetView.getWidth() - oldWidth;
                endTranslationX -= targetView.getWidth() - oldWidth;
                break;
            case TranslateFromRight:
                endTranslationX += targetView.getWidth() - oldWidth;
                break;
            case TranslateFromTop:
                startTranslationY -= targetView.getHeight() - oldHeight;
                endTranslationY -= targetView.getHeight() - oldHeight;
                break;
            case TranslateFromBottom:
                endTranslationY += targetView.getHeight() - oldHeight;
                break;
        }
        endTranslationY = Math.abs(endTranslationY);
        // 适配智能拖拽布局
        endTranslationY += targetView.getTranslationY();
        switch (popupAnimation) {
            case TranslateFromLeft:
            case TranslateFromRight:
                observerAnimator(targetView.createAnimatorProperty().moveFromX(endTranslationX).moveToX(startTranslationX).setDuration(animationDuration)).start();
                break;
            case TranslateFromTop:
            case TranslateFromBottom:
                observerAnimator(targetView.createAnimatorProperty().moveFromY(endTranslationY).moveToY(startTranslationY).setDuration(animationDuration)).start();
                break;
        }
    }
}
