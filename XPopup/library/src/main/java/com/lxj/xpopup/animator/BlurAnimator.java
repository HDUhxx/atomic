package com.lxj.xpopup.animator;

import ohos.agp.components.Component;
import ohos.media.image.PixelMap;

/**
 * Description: 背景模糊动画器
 * Create by dance, at 2018/12/9
 */
public class BlurAnimator extends PopupAnimator {

    public PixelMap decorBitmap;
    public boolean hasShadowBg = false;
    public int shadowColor;

    public BlurAnimator(Component target, int shadowColor) {
        super(target, 0);
        this.shadowColor = shadowColor;
    }

    @Override
    public void initAnimator() {

    }

    @Override
    public void animateShow() {

    }

    @Override
    public void animateDismiss() {

    }

}
