package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.ComponentUtil;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.BubbleLayout;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.Rect;
import ohos.app.Context;

/**
 * Description: 带气泡背景的Attach弹窗
 */
public abstract class BubbleAttachPopupView extends BasePopupView {

    protected int defaultOffsetY = 0;
    protected int defaultOffsetX = 0;
    protected BubbleLayout bubbleContainer;
    protected int correctedHeightTimes = 0;

    public BubbleAttachPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        bubbleContainer = (BubbleLayout) findComponentById(ResourceTable.Id_bubbleContainer);
    }

    protected void addInnerContent() {
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), bubbleContainer, false);
        bubbleContainer.addComponent(contentView);
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_bubble_attach_popup_view;
    }

    public boolean isShowUp;
    public boolean isShowLeft;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (bubbleContainer.getChildCount() == 0) {
            addInnerContent();
        }
        if (popupInfo.getAtView() == null && popupInfo.touchPoint == null) {
            throw new IllegalArgumentException("atView() or watchView() must be called for BubbleAttachPopupView before show()！");
        }
        bubbleContainer.setShadowRadius(XPopupUtils.vp2px(getContext(), 2f));

        defaultOffsetY = popupInfo.offsetY == 0 ? XPopupUtils.vp2px(getContext(), 2) : popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX;

        bubbleContainer.setTranslationX(popupInfo.offsetX);
        bubbleContainer.setTranslationY(popupInfo.offsetY);

        XPopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    float centerY = 0;

    /**
     * 执行倚靠逻辑
     */
    public void doAttach() {
        float translationX;
        float translationY;
        int overflow = XPopupUtils.vp2px(getContext(), 10);
        float maxY = XPopupUtils.getAppHeight(getContext()) - overflow; // 弹窗显示的位置不能超过Window高度
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());

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

            // 设置气泡相关
            if (isShowUpToTarget()) {
                bubbleContainer.setLook(BubbleLayout.Look.BOTTOM);
            } else {
                bubbleContainer.setLook(BubbleLayout.Look.TOP);
            }
            if (popupInfo.isCenterHorizontal) {
                bubbleContainer.setLookPositionCenter(true);
            } else {
                if (isShowLeft) {
                    // 在目标左边，箭头在最右边
                    bubbleContainer.setLookPosition(XPopupUtils.vp2px(getContext(), 1));
                } else {
                    // 在目标右边，箭头在最开始
                    bubbleContainer.setLookPosition(bubbleContainer.getEstimatedWidth() - XPopupUtils.vp2px(getContext(), 1));
                }
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

            // 设置气泡相关
            if (isShowUpToTarget()) {
                bubbleContainer.setLook(BubbleLayout.Look.BOTTOM);
            } else {
                bubbleContainer.setLook(BubbleLayout.Look.TOP);
            }
            // 箭头对着目标Component的中心
            if (popupInfo.isCenterHorizontal) {
                bubbleContainer.setLookPositionCenter(true);
            } else {
                bubbleContainer.setLookPosition(rect.left + rect.getWidth() / 2 - (int) translationX);
            }
        }

        if (correctedHeightTimes < 2) {
            // 第一次执行到这里，需要重新绘制（气泡布局）
            // 第二次执行到这里，重新绘制完气泡布局，控件尺寸会发生变化，因此需要再次重新绘制气泡
            getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                @Override
                public void run() {
                    bubbleContainer.invalidate();
                }
            });
            correctedHeightTimes++;
        } else if (correctedHeightTimes == 2) {
            // 第三次执行到这里，控件尺寸、气泡显示已完全OK，再执行位移和显示动画
            getPopupContentView().setTranslationX(translationX);
            getPopupContentView().setTranslationY(translationY);
            initAndStartAnimation();
            focusAndProcessBackPress();
        }

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

    /**
     * 设置气泡背景颜色
     *
     * @param color
     * @return
     */
    public BubbleAttachPopupView setBubbleBgColor(int color) {
        bubbleContainer.setBubbleColor(color);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡背景圆角
     *
     * @param radius
     * @return
     */
    public BubbleAttachPopupView setBubbleRadius(int radius) {
        bubbleContainer.setBubbleRadius(radius);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡箭头的宽度
     *
     * @param width
     * @return
     */
    public BubbleAttachPopupView setArrowWidth(int width) {
        bubbleContainer.setLookWidth(width);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡箭头的高度
     *
     * @param height
     * @return
     */
    public BubbleAttachPopupView setArrowHeight(int height) {
        bubbleContainer.setLookLength(height);
        bubbleContainer.invalidate();
        return this;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.ScaleAlphaFromCenter);
    }

}
