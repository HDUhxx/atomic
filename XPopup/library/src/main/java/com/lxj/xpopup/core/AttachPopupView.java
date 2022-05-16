package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.ComponentUtil;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.ShadowLayout;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.Rect;
import ohos.app.Context;

/**
 * Description: 依附于某个View的弹窗，弹窗会出现在目标的上方或下方，如果你想要出现在目标的左边或者右边，请使用HorizontalAttachPopupView。
 * 支持通过popupPosition()方法手动指定想要出现在目标的上边还是下边，但是对Left和Right则不生效。
 * Create by dance, at 2018/12/11
 */
public abstract class AttachPopupView extends BasePopupView {

    protected int defaultOffsetY = 0;
    protected int defaultOffsetX = 0;
    protected StackLayout attachPopupContainer;

    public AttachPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        attachPopupContainer = (StackLayout) findComponentById(ResourceTable.Id_attachPopupContainer);
    }

    protected void addInnerContent() {
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), attachPopupContainer, false);
        if (popupInfo.isComponentMode && !popupInfo.hasShadowBg) {
            shadowLayout = new ShadowLayout(getContext());
            shadowLayout.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
            shadowLayout.setPadding(15, 15, 15, 15);
            shadowLayout.addComponent(contentView);
            attachPopupContainer.addComponent(shadowLayout);
        } else {
            attachPopupContainer.addComponent(contentView);
        }
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_attach_popup_view;
    }

    public boolean isShowUp;
    public boolean isShowLeft;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (attachPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        if (popupInfo.getAtView() == null && popupInfo.touchPoint == null) {
            throw new IllegalArgumentException("atView() or watchView() must be called for AttachPopupView before show()！");
        }

        defaultOffsetY = popupInfo.offsetY == 0 ? XPopupUtils.vp2px(getContext(), 2) : popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX;

        attachPopupContainer.setTranslationX(popupInfo.offsetX);
        attachPopupContainer.setTranslationY(popupInfo.offsetY);
        applyBg();
        XPopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    protected void applyBg() {
        if (!isCreated) {
            // 优先使用implView的背景
            if (getPopupImplView().getBackgroundElement() == null) {
                getPopupImplView().setBackground(XPopupUtils.createDrawable((popupInfo.isDarkTheme ? ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_bg_dark_color)
                        : ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_light_color)), popupInfo.borderRadius));
            }
        }
    }

    /**
     * 执行倚靠逻辑
     */
    public float translationX = 0;
    public float translationY = 0;

    // 弹窗显示的位置不能超越Window高度
    float maxY = XPopupUtils.getAppHeight(getContext());
    int overflow = XPopupUtils.vp2px(getContext(), 10);
    float centerY = 0;

    public void doAttach() {
        maxY = XPopupUtils.getAppHeight(getContext()) - overflow;
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());
        if (popupInfo == null) {
            return;
        }
        // 0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            if (XPopup.longClickPoint != null) {
                popupInfo.touchPoint = XPopup.longClickPoint;
            }
            centerY = popupInfo.touchPoint.getY();
            // 依附于指定点,尽量优先放在下方，当不够的时候在显示在上方
            if ((popupInfo.touchPoint.getY() + getPopupContentView().getHeight()) > maxY) { // 如果下方放不下，超出window高度
                isShowUp = popupInfo.touchPoint.getY() > XPopupUtils.getAppHeight(getContext()) / 2;
            } else {
                isShowUp = false;
            }
            isShowLeft = popupInfo.touchPoint.getX() < XPopupUtils.getWindowWidth(getContext()) / 2;

            // 限制最大宽高
            ComponentContainer.LayoutConfig params = getPopupContentView().getLayoutConfig();
            int maxHeight = (int) (isShowUpToTarget() ? (popupInfo.touchPoint.getY() - overflow)
                    : (XPopupUtils.getAppHeight(getContext()) - popupInfo.touchPoint.getY() - overflow));
            int maxWidth = (int) (isShowLeft ? (XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.getX() - overflow) : (popupInfo.touchPoint.getX() - overflow));
            if (getPopupContentView().getHeight() > maxHeight) {
                params.height = maxHeight;
            }
            if (getPopupContentView().getHeight() > maxWidth) {
                params.width = maxWidth;
            }
            getPopupContentView().setLayoutConfig(params);

            if (isRTL) {
                translationX = isShowLeft ? -(XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.getX() - getPopupContentView().getWidth() - defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.getX() + defaultOffsetX);
            } else {
                translationX = isShowLeft ? (popupInfo.touchPoint.getX() + defaultOffsetX) : (popupInfo.touchPoint.getX() - getPopupContentView().getWidth() - defaultOffsetX);
            }
            if (popupInfo.isCenterHorizontal) {
                // 水平居中
                if (isShowLeft) {
                    if (isRTL) {
                        translationX += getPopupContentView().getHeight() / 2f;
                    } else {
                        translationX -= getPopupContentView().getHeight() / 2f;
                    }
                } else {
                    if (isRTL) {
                        translationX -= getPopupContentView().getWidth() / 2f;
                    } else {
                        translationX += getPopupContentView().getWidth() / 2f;
                    }
                }
            }
            if (isShowUpToTarget()) {
                // 应显示在point上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationY = popupInfo.touchPoint.getY() - getPopupContentView().getHeight() - defaultOffsetY;
            } else {
                translationY = popupInfo.touchPoint.getY() + defaultOffsetY;
            }
        } else {
            // 依附于指定View
            // 1. 获取atView在屏幕上的位置
            final Rect rect = ComponentUtil.getLocationInDecorView(popupInfo.getAtView());
            final int centerX = (rect.left + rect.right) / 2;

            // 尽量优先放在下方，当不够的时候在显示在上方
            centerY = (rect.top + rect.bottom) / 2;
            int contentHeight = getPopupContentView().getHeight();
            if (contentHeight > maxY - rect.bottom) { // 如果下方放不下，超出window高度
                int upAvailableSpace = rect.top - overflow; // 上方可用大小
                if (contentHeight > upAvailableSpace) { // 超出上方可用大小
                    // 如果也超出了上方可用区域则哪里空间大显示在哪个方向
                    isShowUp = upAvailableSpace > maxY - rect.bottom;
                } else {
                    isShowUp = true;
                }
            } else {
                isShowUp = false;
            }
            isShowLeft = centerX < XPopupUtils.getWindowWidth(getContext()) / 2;

            // 修正高度，弹窗的高有可能超出window区域
            ComponentContainer.LayoutConfig params = getPopupContentView().getLayoutConfig();
            int maxHeight = isShowUpToTarget() ? (rect.top - overflow) : (XPopupUtils.getAppHeight(getContext()) - rect.bottom - overflow);
            int maxWidth = isShowLeft ? (XPopupUtils.getWindowWidth(getContext()) - rect.left - overflow) : (rect.right - overflow);
            if (getPopupContentView().getHeight() > maxHeight) {
                params.height = maxHeight;
            }
            if (getPopupContentView().getWidth() > maxWidth) {
                params.width = maxWidth;
            }
            getPopupContentView().setLayoutConfig(params);
            if (isRTL) {
                translationX = isShowLeft ? -(XPopupUtils.getWindowWidth(getContext()) - rect.left - getPopupContentView().getWidth() - defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext()) - rect.right + defaultOffsetX);
            } else {
                translationX = isShowLeft ? (rect.left + defaultOffsetX) : (rect.right - getPopupContentView().getWidth() - defaultOffsetX);
            }
            if (popupInfo.isCenterHorizontal) {
                // 水平居中
                if (isShowLeft) {
                    if (isRTL) {
                        translationX -= (rect.getWidth() - getPopupContentView().getWidth()) / 2f;
                    } else {
                        translationX += (rect.getWidth() - getPopupContentView().getWidth()) / 2f;
                    }
                } else {
                    if (isRTL) {
                        translationX += (rect.getWidth() - getPopupContentView().getWidth()) / 2f;
                    } else {
                        translationX -= (rect.getWidth() - getPopupContentView().getWidth()) / 2f;
                    }
                }
            }
            if (isShowUpToTarget()) {
                // 说明上面的空间比较大，应显示在atView上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationY = rect.top - getPopupContentView().getHeight() - defaultOffsetY;
            } else {
                translationY = rect.bottom + defaultOffsetY;
            }
        }
        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
        initAndStartAnimation();
    }

    protected void initAndStartAnimation() {
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    // 是否显示在目标上方
    protected boolean isShowUpToTarget() {
        if (popupInfo.positionByWindowCenter) {
            // 目标在屏幕上半方，弹窗显示在下；反之，则在上
            return centerY > XPopupUtils.getAppHeight(getContext()) / 2;
        }
        // 默认是根据Material规范定位，优先显示在目标下方，下方距离不足才显示在上方
        return (isShowUp || popupInfo.popupPosition == PopupPosition.Top)
                && popupInfo.popupPosition != PopupPosition.Bottom;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        if (isShowUpToTarget()) {
            // 在上方展示
            return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), isShowLeft ? PopupAnimation.ScrollAlphaFromLeftBottom
                    : PopupAnimation.ScrollAlphaFromRightBottom);
        } else {
            // 在下方展示
            return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), isShowLeft ? PopupAnimation.ScrollAlphaFromLeftTop
                    : PopupAnimation.ScrollAlphaFromRightTop);
        }
    }

}
