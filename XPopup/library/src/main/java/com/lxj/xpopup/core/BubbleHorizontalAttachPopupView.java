package com.lxj.xpopup.core;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.ComponentUtil;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.BubbleLayout;
import ohos.agp.utils.Rect;
import ohos.app.Context;

/**
 * Description: 水平方向带气泡的Attach弹窗
 */
public abstract class BubbleHorizontalAttachPopupView extends BubbleAttachPopupView {
    public BubbleHorizontalAttachPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        defaultOffsetY = popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX == 0 ? XPopupUtils.vp2px(getContext(), 4) : popupInfo.offsetX;
    }

    /**
     * 执行附着逻辑
     */
    public void doAttach() {
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());
        float translationX;
        float translationY;
        int width = getPopupContentView().getWidth();
        int height = getPopupContentView().getHeight();
        // 0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            if (XPopup.longClickPoint != null) {
                popupInfo.touchPoint = XPopup.longClickPoint;
            }
            // 依附于指定点
            isShowLeft = popupInfo.touchPoint.getX() > XPopupUtils.getWindowWidth(getContext()) / 2;

            // translationX: 在左边就和点左边对齐，在右边就和其右边对齐
            if (isRTL) {
                translationX = isShowLeft ? -(XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.getX() + defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.getX() - getPopupContentView().getWidth() - defaultOffsetX);
            } else {
                translationX = isShowLeftToTarget() ? (popupInfo.touchPoint.getX() - width - defaultOffsetX) : (popupInfo.touchPoint.getX() + defaultOffsetX);
            }
            translationY = popupInfo.touchPoint.getY() - height * .5f + defaultOffsetY;
        } else {
            // 依附于指定View
            // 1. 获取atView在屏幕上的位置
            Rect rect = ComponentUtil.getLocationInDecorView(popupInfo.getAtView());

            int centerX = (rect.left + rect.right) / 2;

            isShowLeft = centerX > XPopupUtils.getWindowWidth(getContext()) / 2;
            if (isRTL) {
                translationX = isShowLeft ? -(XPopupUtils.getWindowWidth(getContext()) - rect.left + defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext()) - rect.right - getPopupContentView().getWidth() - defaultOffsetX);
            } else {
                translationX = isShowLeftToTarget() ? (rect.left - width - defaultOffsetX) : (rect.right + defaultOffsetX);
            }
            translationY = rect.top + (rect.getHeight() - height) / 2 + defaultOffsetY;
        }

        // 设置气泡相关
        if (isShowLeftToTarget()) {
            bubbleContainer.setLook(BubbleLayout.Look.RIGHT);
        } else {
            bubbleContainer.setLook(BubbleLayout.Look.LEFT);
        }
        bubbleContainer.setLookPositionCenter(true);

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

    private boolean isShowLeftToTarget() {
        return (isShowLeft || popupInfo.popupPosition == PopupPosition.Left)
                && popupInfo.popupPosition != PopupPosition.Right;
    }

}
