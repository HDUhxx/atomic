package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.app.Context;

import static com.lxj.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;

/**
 * Description: 用于自由定位的弹窗
 * Create by dance, at 2019/6/14
 */
public class PositionPopupView extends BasePopupView {
    StackLayout positionPopupContainer;

    public PositionPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        positionPopupContainer = (StackLayout) findComponentById(ResourceTable.Id_positionPopupContainer);
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), positionPopupContainer, false);
        positionPopupContainer.addComponent(contentView);
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_position_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        XPopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                if (popupInfo == null) {
                    return;
                }
                if (popupInfo.isCenterHorizontal) {
                    float left = !XPopupUtils.isLayoutRtl(getContext()) ? (XPopupUtils.getWindowWidth(getContext()) - positionPopupContainer.getWidth()) / 2f
                            : -(XPopupUtils.getWindowWidth(getContext()) - positionPopupContainer.getWidth()) / 2f;
                    positionPopupContainer.setTranslationX(left);
                } else {
                    positionPopupContainer.setTranslationX(popupInfo.offsetX);
                }
                positionPopupContainer.setTranslationY(popupInfo.offsetY);
                initAndStartAnimation();
            }
        });
    }

    protected void initAndStartAnimation() {
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
    }
}
