package com.lxj.xpopup.impl;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.PartShadowContainer;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Rect;
import ohos.app.Context;

/**
 * Description: 局部阴影的弹窗，类似于淘宝商品列表的下拉筛选弹窗
 * Create by dance, at 2018/12/21
 */
public abstract class PartShadowPopupView extends BasePopupView implements Component.LayoutRefreshedListener, Component.TouchEventListener {

    protected PartShadowContainer attachPopupContainer;

    public PartShadowPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        attachPopupContainer = (PartShadowContainer) findComponentById(ResourceTable.Id_attachPopupContainer);
        setLayoutRefreshedListener(this);
        setTouchEventListener(this);
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_partshadow_popup_view;
    }

    protected void addInnerContent() {
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), attachPopupContainer, false);
        attachPopupContainer.addComponent(contentView);
    }

    @Override
    protected void initPopupContent() {
        if (attachPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }

        // 指定阴影动画的目标View
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.targetView = getPopupContentView();
        }
        XPopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    protected void initAndStartAnimation() {
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    @Override
    public void onRefreshed(Component component) {
        doAttach();
    }

    public boolean isShowUp;

    public void doAttach() {
        if (popupInfo.getAtView() == null) {
            throw new IllegalArgumentException("atView must not be null for PartShadowPopupView！");
        }

        // 1. apply width and height
        ComponentContainer.LayoutConfig params = getPopupContentView().getLayoutConfig();
        params.width = getWidth();

        // 1. 获取atView在屏幕上的位置
        int[] locations = popupInfo.getAtView().getLocationOnScreen();
        Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getWidth(),
                locations[1] + popupInfo.getAtView().getHeight());

        // 水平居中
        if (popupInfo.isCenterHorizontal && getPopupImplView() != null) {
            // 参考目标View居中，而不是屏幕居中
            int tx = (rect.left + rect.right) / 2 - getPopupImplView().getWidth() / 2;
            getPopupImplView().setTranslationX(tx);
        } else {
            int tx = rect.left + popupInfo.offsetX;
            if (tx + getPopupImplView().getWidth() > XPopupUtils.getWindowWidth(getContext())) {
                // 右边超出屏幕了，往左移动
                tx -= (tx + getPopupImplView().getWidth() - XPopupUtils.getWindowWidth(getContext()));
            }
            getPopupImplView().setTranslationX(tx);
        }

        int centerY = rect.top + rect.getHeight() / 2;
        if ((centerY > getHeight() / 2 || popupInfo.popupPosition == PopupPosition.Top) && popupInfo.popupPosition != PopupPosition.Bottom) {
            // 说明atView在Window下半部分，PartShadow应该显示在它上方，计算atView之上的高度
            params.height = rect.top;
            isShowUp = true;
            // 同时自定义的impl View应该Gravity居于底部
            Component implView = ((ComponentContainer) getPopupContentView()).getComponentAt(0);
            StackLayout.LayoutConfig implParams = (LayoutConfig) implView.getLayoutConfig();
            implParams.setMarginBottom(popupInfo.getAtView().getHeight());
            implParams.alignment = LayoutAlignment.BOTTOM;
            if (getMaxHeight() != 0) {
                implParams.height = Math.min(implView.getHeight(), getMaxHeight());
            }
            implView.setLayoutConfig(implParams);
        } else {
            // atView在上半部分，PartShadow应该显示在它下方，计算atView之下的高度
            params.height = getHeight() - rect.bottom;
            isShowUp = false;
            params.setMarginTop(rect.bottom);
            // 同时自定义的impl View应该Gravity居于顶部
            Component implView = ((ComponentContainer) getPopupContentView()).getComponentAt(0);
            StackLayout.LayoutConfig implParams = (LayoutConfig) implView.getLayoutConfig();
            implParams.alignment = LayoutAlignment.TOP;
            if (getMaxHeight() != 0) {
                implParams.height = Math.min(implView.getHeight(), getMaxHeight());
            }
            implView.setLayoutConfig(implParams);
        }
        getPopupContentView().setLayoutConfig(params);
        getPopupImplView().setTranslationY(popupInfo.offsetY);

        attachPopupContainer.setLongClickedListener(new LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                if (popupInfo.isDismissOnTouchOutside) {
                    dismiss();
                }
            }
        });
        initAndStartAnimation();
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new TranslateAnimator(getPopupImplView(), getAnimationDuration(), isShowUp ?
                PopupAnimation.TranslateFromBottom : PopupAnimation.TranslateFromTop);
    }

}
