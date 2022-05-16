package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;

import static com.lxj.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;

/**
 * Description: 在中间显示的Popup
 * Create by dance, at 2018/12/8
 */
public class CenterPopupView extends BasePopupView {

    protected StackLayout centerPopupContainer;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    public CenterPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        centerPopupContainer = (StackLayout) findComponentById(ResourceTable.Id_centerPopupContainer);
    }

    protected void addInnerContent() {
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), centerPopupContainer, false);
        LayoutConfig params = (LayoutConfig) contentView.getLayoutConfig();
        params.alignment = LayoutAlignment.CENTER;
        centerPopupContainer.addComponent(contentView, params);

    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_center_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (centerPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        getPopupContentView().setTranslationX(popupInfo.offsetX);
        getPopupContentView().setTranslationY(popupInfo.offsetY);
        XPopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), null);
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
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        centerPopupContainer.setBackground(XPopupUtils.createDrawable(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_bg_dark_color), popupInfo.borderRadius));
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        centerPopupContainer.setBackground(XPopupUtils.createDrawable(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_light_color), popupInfo.borderRadius));
    }

    /**
     * 具体实现的类的布局
     *
     * @return 布局资源id
     */
    @Override
    protected int getImplLayoutId() {
        return 0;
    }

    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? (int) (XPopupUtils.getWindowWidth(getContext()) * 0.8f)
                : popupInfo.maxWidth;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
    }

}
