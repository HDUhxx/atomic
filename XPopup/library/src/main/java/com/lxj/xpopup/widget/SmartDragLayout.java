package com.lxj.xpopup.widget;

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
 * Description: 智能的拖拽布局(垂直)，优先滚动整体，整体滚到头，则滚动内部能滚动的View
 * Create by dance, at 2018/12/23
 */
public class SmartDragLayout extends StackLayout implements Component.BindStateChangedListener, ComponentContainer.ArrangeListener {

    private Component child;
    boolean enableDrag = true; // 是否启用手势拖拽
    boolean dismissOnTouchOutside = true;
    int duration = 400;

    public SmartDragLayout(Context context) {
        this(context, null);
    }

    public SmartDragLayout(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public SmartDragLayout(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        setBindStateChangedListener(this);
        setArrangeListener(this);
        setDraggedListener(DRAG_VERTICAL, mDragListener);
    }

    int maxY;
    int minY;

    @Override
    public void addComponent(Component childComponent) {
        super.addComponent(childComponent);
        child = childComponent;
    }

    @Override
    public boolean onArrange(int left, int top, int right, int bottom) {
        if (child != null) {
            maxY = child.getEstimatedHeight();
            minY = 0;
            int childLeft = getEstimatedWidth() / 2 - child.getEstimatedWidth() / 2;
            child.arrange(childLeft, getEstimatedHeight() - maxY, childLeft + child.getEstimatedWidth(), getEstimatedHeight());
        }
        return false;
    }

    Component.DraggedListener mDragListener = new Component.DraggedListener() {
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
                int ty = (int) (dragInfo.updatePoint.getPointY() - startPoint.getPointY());
                ty = ty > maxY - 1 ? maxY - 1 : ty;
                isScrollUp = ty < 0;
                ty = ty < 0 ? 0 : ty;
                translationY(ty);
            }
        }


        @Override
        public void onDragEnd(Component component, DragInfo dragInfo) {
            if (enableDrag) {
                int ty = (int) (dragInfo.updatePoint.getPointY() - startPoint.getPointY());
                if (ty > maxY / 2) {
                    close();
                } else {
                    finishTranslationY(ty);
                }
            }
        }

        @Override
        public void onDragCancel(Component component, DragInfo dragInfo) {
            // Do something
        }
    };

    boolean isScrollUp;

    public void translationY(int dy) {
        float fraction = dy * 1f / maxY;
        if (listener != null) {
            listener.onDrag(dy, 1f - fraction, isScrollUp);
        }
        child.setTranslationY(dy);
    }

    public void finishTranslationY(float dy) {
        if (child == null || dy < 0) {
            return;
        }
        AnimatorValue anim = new AnimatorValue();
        anim.setDuration(duration);
        anim.setCurveType(Animator.CurveType.LINEAR);
        anim.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float value) {
                child.setTranslationY(dy - dy * value);
            }
        });
        anim.start();
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        isScrollUp = false;
        setTranslationY(0);
    }

    public void close() {
        if (listener != null) {
            listener.onClose();
        }
    }

    public void enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void dismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    private OnCloseListener listener;

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }


    public interface OnCloseListener {

        void onClose();

        void onDrag(int y, float percent, boolean isScrollUp);

    }

}
