package com.lxj.xpopup.animator;

import com.lxj.xpopup.enums.PopupAnimation;
import ohos.agp.animation.Animator;
import ohos.agp.components.Component;

/**
 * Description: 缩放透明
 * Create by dance, at 2018/12/9
 */
public class ScaleAlphaAnimator extends PopupAnimator {

    float startScale = .75f;

    public ScaleAlphaAnimator(Component target, int animationDuration, PopupAnimation popupAnimation) {
        super(target, animationDuration, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setScaleX(startScale);
        targetView.setScaleY(startScale);
        targetView.setAlpha(0);
        // 设置动画参考点
        applyPivot();
    }

    /**
     * 根据不同的PopupAnimation来设定对应的pivot
     */
    private void applyPivot() {
        switch (popupAnimation) {
            case ScaleAlphaFromCenter:
                targetView.setPivotX(targetView.getWidth() / 2);
                targetView.setPivotY(targetView.getHeight() / 2);
                break;
            case ScaleAlphaFromLeftTop:
                targetView.setPivotX(0);
                targetView.setPivotY(0);
                break;
            case ScaleAlphaFromRightTop:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(0f);
                break;
            case ScaleAlphaFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getHeight());
                break;
            case ScaleAlphaFromRightBottom:
                targetView.setPivotX(targetView.getWidth());
                targetView.setPivotY(targetView.getHeight());
                break;
            default:
                break;
        }

    }

    @Override
    public void animateShow() {
        targetView.createAnimatorProperty().scaleX(1.0f).scaleY(1.0f).alpha(1.0f)
                .setDuration(animationDuration)
                .setCurveType(Animator.CurveType.OVERSHOOT)
                .start();
    }

    @Override
    public void animateDismiss() {
        if (animating) {
            return;
        }
        observerAnimator(targetView.createAnimatorProperty().scaleX(startScale).scaleY(startScale).alpha(0.0f)
                .setDuration(animationDuration))
                .setCurveType(Animator.CurveType.ACCELERATE_DECELERATE)
                .start();
    }

}
