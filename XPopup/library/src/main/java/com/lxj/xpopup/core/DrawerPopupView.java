package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.widget.PopupDrawerLayout;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.app.Context;

/**
 * Description: 带Drawer的弹窗
 * Create by dance, at 2018/12/20
 */
public abstract class DrawerPopupView extends BasePopupView {

    protected PopupDrawerLayout drawerLayout;
    protected StackLayout drawerContentContainer;
    float mFraction = 0f;

    public DrawerPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        drawerLayout = (PopupDrawerLayout) findComponentById(ResourceTable.Id_drawerLayout);
        drawerContentContainer = (StackLayout) findComponentById(ResourceTable.Id_drawerContentContainer);
        Component contentView = LayoutScatter.getInstance(context).parse(getImplLayoutId(), drawerContentContainer, false);
        drawerContentContainer.addComponent(contentView);
    }

    @Override
    public Component getPopupImplView() {
        return drawerContentContainer.getComponentAt(0);
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_drawer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        drawerLayout.setBgAnimator(shadowBgAnimator);
        drawerLayout.hasShadowBg(popupInfo.hasShadowBg && popupInfo.isComponentMode);
        drawerLayout.dismissOnTouchOutside(popupInfo.isDismissOnTouchOutside);
        drawerLayout.setDrawerPosition(popupInfo.popupPosition == null ? PopupPosition.Left : popupInfo.popupPosition);
        drawerLayout.enableDrag(popupInfo.enableDrag);
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                dismiss();
            }

            @Override
            public void onDrag(int x, float fraction, boolean isToLeft) {
                if (popupInfo != null && popupInfo.xPopupCallback != null) {
                    popupInfo.xPopupCallback.onDrag(DrawerPopupView.this, x, fraction, isToLeft);
                }
                mFraction = fraction;
            }
        });
        drawerLayout.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                dismiss();
            }
        });
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        if (popupInfo.popupPosition == null || popupInfo.popupPosition == PopupPosition.Left) {
            return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.TranslateFromLeft);
        } else {
            return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.TranslateFromRight);
        }
    }


}
