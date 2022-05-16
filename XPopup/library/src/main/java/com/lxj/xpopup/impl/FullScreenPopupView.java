package com.lxj.xpopup.impl;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.enums.PopupAnimation;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.app.Context;

/**
 * Description: 宽高撑满的全屏弹窗
 * Create by lxj, at 2019/2/1
 */
public class FullScreenPopupView extends BasePopupView {

    protected Component contentView;
    protected StackLayout fullPopupContainer;

    public FullScreenPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        fullPopupContainer = (StackLayout) findComponentById(ResourceTable.Id_fullPopupContainer);
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_fullscreen_popup_view;
    }

    protected void addInnerContent() {
        contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), fullPopupContainer, false);
        fullPopupContainer.addComponent(contentView);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (fullPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        getPopupContentView().setTranslationX(popupInfo.offsetX);
        getPopupContentView().setTranslationY(popupInfo.offsetY);
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.TranslateFromBottom);
    }
}
