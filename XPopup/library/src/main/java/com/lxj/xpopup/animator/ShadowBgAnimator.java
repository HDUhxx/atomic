package com.lxj.xpopup.animator;

import com.lxj.xpopup.util.ArgbEvaluator;
import com.lxj.xpopup.util.ElementUtil;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;

/**
 * Description: 背景Shadow动画器，负责执行半透明的渐入渐出动画
 * Create by dance, at 2018/12/9
 */
public class ShadowBgAnimator extends PopupAnimator {

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int startColor = 0x00000000;
    private boolean isZeroDuration = false;
    private int shadowColor;

    public ShadowBgAnimator() {
    }

    public ShadowBgAnimator(Component target, int animationDuration, int shadowColor) {
        super(target, animationDuration);
        this.shadowColor = shadowColor;
    }

    @Override
    public void initAnimator() {
        targetView.setBackground(ElementUtil.getShapeElement(startColor));
    }

    @Override
    public void animateShow() {
        AnimatorValue animator = new AnimatorValue();
        animator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float value) {
                int nowColor = argbEvaluator.evaluate(value, startColor, shadowColor);
                targetView.setBackground(ElementUtil.getShapeElement(nowColor));
            }
        });
        animator.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
        animator.setDuration(isZeroDuration ? 0 : animationDuration);
        animator.start();
    }

    @Override
    public void animateDismiss() {
        if (animating) {
            return;
        }
        AnimatorValue animator = new AnimatorValue();
        animator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float value) {
                int nowColor = argbEvaluator.evaluate(value, shadowColor, startColor);
                targetView.setBackground(ElementUtil.getShapeElement(nowColor));
            }
        });
        observerAnimator(animator);
        animator.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
        animator.setDuration(isZeroDuration ? 0 : animationDuration);
        animator.start();
    }

    public int calculateBgColor(float fraction) {
        return argbEvaluator.evaluate(fraction, startColor, shadowColor);
    }

}
