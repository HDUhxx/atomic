package com.lxj.xpopup.widget;

import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.ElementUtil;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DragInfo;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.Point;
import ohos.app.Context;

/**
 * Description: 智能的拖拽布局(水平)
 * Create by dance, at 2018/12/23
 */
public class PopupDrawerLayout extends StackLayout implements Component.BindStateChangedListener, ComponentContainer.ArrangeListener {

    private static final String TAG = "SmartDragLayout";
    private Component placeHolder;
    private Component mChild;
    public PopupPosition position = PopupPosition.Left;
    ShadowBgAnimator bgAnimator;
    boolean enableDrag = true; // 是否启用手势拖拽
    boolean dismissOnTouchOutside = true;
    boolean hasShadowBg = true;

    public PopupDrawerLayout(Context context) {
        this(context, null);
    }

    public PopupDrawerLayout(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public PopupDrawerLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        setBindStateChangedListener(this);
        setDraggedListener(DRAG_HORIZONTAL, mDragListener);
        setArrangeListener(this);
    }

    int parentWidth = 0;
    int parentHeight = 0;
    int childWidth = 0;
    int childHeight = 0;
    private boolean hasLayout = false;

    @Override
    public boolean onArrange(int left, int top, int right, int bottom) {
        if (placeHolder == null) {
            placeHolder = getComponentAt(0);
            mChild = getComponentAt(1);
        }
        parentWidth = getEstimatedWidth();
        parentHeight = getEstimatedHeight();
        childWidth = mChild.getEstimatedWidth();
        childHeight = mChild.getEstimatedHeight();
        placeHolder.arrange(0, 0, parentWidth, parentHeight);
        if (!hasLayout) {
            if (position == PopupPosition.Left) {
                mChild.arrange(0, 0, childWidth, childHeight);
            } else {
                mChild.arrange(parentWidth - childWidth, 0, parentWidth, childHeight);
            }
        } else {
            mChild.arrange(mChild.getLeft(), mChild.getTop(), mChild.getRight(), mChild.getEstimatedHeight());
        }
        return true;
    }

    DraggedListener mDragListener = new DraggedListener() {
        Point startPoint;

        @Override
        public void onDragDown(Component component, DragInfo dragInfo) {

        }

        @Override
        public void onDragStart(Component component, DragInfo dragInfo) {
            startPoint = dragInfo.startPoint;
        }

        @Override
        public void onDragUpdate(Component component, DragInfo dragInfo) {
            if (enableDrag) {
                int tx = (int) (dragInfo.updatePoint.getPointX() - startPoint.getPointX());
                tx = tx > childWidth - 1 ? childWidth - 1 : tx;
                tx = tx < -childWidth + 1 ? -childWidth + 1 : tx;
                if (position == PopupPosition.Left) {
                    tx = tx > 0 ? 0 : tx;
                } else {
                    tx = tx < 0 ? 0 : tx;
                }
                isToLeft = tx < 0;
                translationX(tx);
            }
        }

        @Override
        public void onDragEnd(Component component, DragInfo dragInfo) {
            if (enableDrag) {
                int tx = (int) (dragInfo.updatePoint.getPointX() - startPoint.getPointX());
                tx = tx > childWidth ? childWidth : tx;
                tx = tx < -childWidth ? -childWidth : tx;
                if (((position == PopupPosition.Left && tx < -childWidth / 2)) ||
                        (position == PopupPosition.Right && tx > childWidth / 2)) {
                    close();
                } else {
                    finishTranslationX(tx);
                }
            }
        }

        @Override
        public void onDragCancel(Component component, DragInfo dragInfo) {
            // Do something
        }
    };

    boolean isToLeft;

    public void translationX(int dx) {
        float fraction = 1f - Math.abs(dx * 1f / childWidth);
        if (hasShadowBg) {
            ((ComponentContainer) getComponentParent()).setBackground(ElementUtil.getShapeElement(bgAnimator.calculateBgColor(fraction)));
        }
        if (listener != null) {
            listener.onDrag(dx, fraction, isToLeft);
        }
        mChild.setTranslationX(dx);
    }

    public void finishTranslationX(float dx) {
        if (position == PopupPosition.Left) {
            if (mChild == null || dx > 0) {
                return;
            }
        } else {
            if (mChild == null || dx < 0) {
                return;
            }
        }
        AnimatorValue anim = new AnimatorValue();
        anim.setDuration(200);
        anim.setCurveType(Animator.CurveType.LINEAR);
        anim.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float value) {
                mChild.setTranslationX(dx - dx * value);
            }
        });
        anim.start();
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        isToLeft = false;
        setTranslationY(0);
    }

    /**
     * 关闭Drawer
     */
    public void close() {
        if (listener != null) {
            listener.onClose();
        }
    }

    public void enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
    }

    public void setDrawerPosition(PopupPosition position) {
        this.position = position;
    }

    public void dismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    public void hasShadowBg(boolean hasShadowBg) {
        this.hasShadowBg = hasShadowBg;
    }

    private OnCloseListener listener;

    public void setBgAnimator(ShadowBgAnimator animator) {
        this.bgAnimator = animator;
    }

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public interface OnCloseListener {

        void onClose();

        void onDrag(int x, float percent, boolean isToLeft);

    }

}
