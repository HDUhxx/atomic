package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;

/**
 * Description: 在底部显示的Popup
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {

    protected SmartDragLayout bottomPopupContainer;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    public BottomPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        bottomPopupContainer = (SmartDragLayout) findComponentById(ResourceTable.Id_bottomPopupContainer);
    }

    protected void addInnerContent() {
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), bottomPopupContainer, false);
        LayoutConfig params = (LayoutConfig) contentView.getLayoutConfig();
        params.alignment = LayoutAlignment.BOTTOM | LayoutAlignment.HORIZONTAL_CENTER;
        bottomPopupContainer.addComponent(contentView);
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_bottom_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (bottomPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        bottomPopupContainer.setDuration(getAnimationDuration() / 2);
        bottomPopupContainer.enableDrag(popupInfo.enableDrag);
        bottomPopupContainer.dismissOnTouchOutside(popupInfo.isDismissOnTouchOutside);
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        XPopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), null);
        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                dismiss();
            }

            @Override
            public void onDrag(int value, float percent, boolean isScrollUp) {
                if (popupInfo == null) {
                    return;
                }
                if (popupInfo.xPopupCallback != null) {
                    popupInfo.xPopupCallback.onDrag(BottomPopupView.this, value, percent, isScrollUp);
                }
                if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg && popupInfo.isComponentMode) {
                    setBackground(ElementUtil.getShapeElement(shadowBgAnimator.calculateBgColor(percent)));
                }
            }

        });
    }

    @Override
    protected void doShowAnimation() {
        if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateShow();
        }
        super.doShowAnimation();
    }

    @Override
    protected void doDismissAnimation() {
        if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateDismiss();
        }
        super.doDismissAnimation();
    }

    protected void applyTheme() {
        if (bindLayoutId == 0) {
            if (popupInfo.isDarkTheme) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }
        }
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new TranslateAnimator(getPopupImplView(), getAnimationDuration(), PopupAnimation.TranslateFromBottom);
    }

    @Override
    protected int getImplLayoutId() {
        return 0;
    }

    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? XPopupUtils.getWindowWidth(getContext())
                : popupInfo.maxWidth;
    }

}
