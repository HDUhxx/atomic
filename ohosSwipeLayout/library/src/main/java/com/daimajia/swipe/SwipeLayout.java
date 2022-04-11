package com.daimajia.swipe;

import com.daimajia.swipe.util.AttrUtils;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.*;

public class SwipeLayout extends StackLayout implements Component.TouchEventListener, Component.LayoutRefreshedListener {
    @Deprecated
    public static final int EMPTY_LAYOUT = -1;
    private static final int DRAG_LEFT = 1;
    private static final int DRAG_RIGHT = 2;
    private static final int DRAG_TOP = 4;
    private static final int DRAG_BOTTOM = 8;
    private static final DragEdge DefaultDragEdge = DragEdge.Right;

    private int mTouchSlop;

    private DragEdge mCurrentDragEdge = DefaultDragEdge;

    private int mDragDistance = 0;
    private boolean isDragOpen = false;
    private LinkedHashMap<DragEdge, Component> mDragEdges = new LinkedHashMap<>();
    private ShowMode mShowMode;

    private float[] mEdgeSwipesOffset = new float[4];

    private List<SwipeListener> mSwipeListeners = new ArrayList<>();
    private List<SwipeDenier> mSwipeDeniers = new ArrayList<>();
    private Map<Component, ArrayList<OnRevealListener>> mRevealListeners = new HashMap<>();
    private Map<Component, Boolean> mShowEntirely = new HashMap<>();
    private Map<Component, Rect> mViewBoundCache = new HashMap<>();//save all children's bound, restore in onLayout

    private DoubleClickListener mDoubleClickListener;

    private boolean mSwipeEnabled = true;
    private boolean[] mSwipesEnabled = new boolean[]{true, true, true, true};
    private boolean mClickToClose = false;
    private float mWillOpenPercentAfterOpen = 0.75f;
    private float mWillOpenPercentAfterClose = 0.25f;

    private AnimatorValue scrollAnimatorValue = new AnimatorValue();

    private Map<Integer, Object> tags = new HashMap<>();

    private boolean needReset;

    public enum DragEdge {
        Left,
        Top,
        Right,
        Bottom
    }

    public enum ShowMode {
        LayDown,
        PullOut
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttrSet attrs) {
        this(context, attrs, null);
    }

    public SwipeLayout(Context context, AttrSet attrs, String defStyle) {
        super(context, attrs, defStyle);
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mTouchSlop = dp2px(8);

//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeLayout);
        int dragEdgeChoices = AttrUtils.getIntFromAttr(attrs, "drag_edge", DRAG_RIGHT);
//        a.getInt(R.styleable.SwipeLayout_drag_edge, DRAG_RIGHT);
        mEdgeSwipesOffset[DragEdge.Left.ordinal()] = AttrUtils.getDimensionFromAttr(attrs, "leftEdgeSwipeOffset", 0);
        mEdgeSwipesOffset[DragEdge.Right.ordinal()] = AttrUtils.getDimensionFromAttr(attrs, "rightEdgeSwipeOffset", 0);
        mEdgeSwipesOffset[DragEdge.Top.ordinal()] = AttrUtils.getDimensionFromAttr(attrs, "topEdgeSwipeOffset", 0);
        mEdgeSwipesOffset[DragEdge.Bottom.ordinal()] = AttrUtils.getDimensionFromAttr(attrs, "bottomEdgeSwipeOffset", 0);
        setClickToClose(AttrUtils.getBooleanFromAttr(attrs, "clickToClose", mClickToClose));

        if ((dragEdgeChoices & DRAG_LEFT) == DRAG_LEFT) {
            mDragEdges.put(DragEdge.Left, null);
        }
        if ((dragEdgeChoices & DRAG_TOP) == DRAG_TOP) {
            mDragEdges.put(DragEdge.Top, null);
        }
        if ((dragEdgeChoices & DRAG_RIGHT) == DRAG_RIGHT) {
            mDragEdges.put(DragEdge.Right, null);
        }
        if ((dragEdgeChoices & DRAG_BOTTOM) == DRAG_BOTTOM) {
            mDragEdges.put(DragEdge.Bottom, null);
        }
        int ordinal = AttrUtils.getIntFromAttr(attrs, "show_mode", ShowMode.PullOut.ordinal());
        mShowMode = ShowMode.values()[ordinal];

        setTouchEventListener(this);
        setLayoutRefreshedListener(this);
    }

    public interface SwipeListener {
        void onStartOpen(SwipeLayout layout);

        void onOpen(SwipeLayout layout);

        void onStartClose(SwipeLayout layout);

        void onClose(SwipeLayout layout);

        void onUpdate(SwipeLayout layout, int leftOffset, int topOffset);

        void onHandRelease(SwipeLayout layout, float xvel, float yvel);
    }

    public void addSwipeListener(SwipeListener l) {
        mSwipeListeners.add(l);
    }

    public void removeSwipeListener(SwipeListener l) {
        mSwipeListeners.remove(l);
    }

    public void removeAllSwipeListener() {
        mSwipeListeners.clear();
    }

    public interface SwipeDenier {
        /*
         * Called in onInterceptTouchEvent Determines if this swipe event should
         * be denied Implement this interface if you are using views with swipe
         * gestures As a child of SwipeLayout
         *
         * @return true deny false allow
         */
        boolean shouldDenySwipe(TouchEvent ev);
    }

    public void addSwipeDenier(SwipeDenier denier) {
        mSwipeDeniers.add(denier);
    }

    public void removeSwipeDenier(SwipeDenier denier) {
        mSwipeDeniers.remove(denier);
    }

    public void removeAllSwipeDeniers() {
        mSwipeDeniers.clear();
    }

    public interface OnRevealListener {
        void onReveal(Component child, DragEdge edge, float fraction, int distance);
    }

    /**
     * bind a view with a specific
     * {@link OnRevealListener}
     *
     * @param childId the view id.
     * @param l       the target
     *                {@link OnRevealListener}
     */
    public void addRevealListener(int childId, OnRevealListener l) {
        Component child = findComponentById(childId);
        if (child == null) {
            throw new IllegalArgumentException("Child does not belong to SwipeListener.");
        }

        if (!mShowEntirely.containsKey(child)) {
            mShowEntirely.put(child, false);
        }
        if (mRevealListeners.get(child) == null)
            mRevealListeners.put(child, new ArrayList<OnRevealListener>());

        mRevealListeners.get(child).add(l);
    }

    /**
     * bind multiple views with an
     * {@link OnRevealListener}.
     *
     * @param childIds the view id.
     * @param l        the {@link OnRevealListener}
     */
    public void addRevealListener(int[] childIds, OnRevealListener l) {
        for (int i : childIds)
            addRevealListener(i, l);
    }

    public void removeRevealListener(int childId, OnRevealListener l) {
        Component child = findComponentById(childId);

        if (child == null) return;

        mShowEntirely.remove(child);
        if (mRevealListeners.containsKey(child)) mRevealListeners.get(child).remove(l);
    }

    public void removeAllRevealListeners(int childId) {
        Component child = findComponentById(childId);
        if (child != null) {
            mRevealListeners.remove(child);
            mShowEntirely.remove(child);
        }
    }


    /**
     * save children's bounds, so they can restore the bound in
     */
    private void captureChildrenBound() {
        Component currentBottomView = getCurrentBottomView();
        if (getOpenStatus() == Status.Close) {
            mViewBoundCache.remove(currentBottomView);
            return;
        }

        Component[] views = new Component[]{getSurfaceView(), currentBottomView};
        for (Component child : views) {
            Rect rect = mViewBoundCache.get(child);
            if (rect == null) {
                rect = new Rect();
                mViewBoundCache.put(child, rect);
            }
            rect.left = child.getLeft();
            rect.top = child.getTop();
            rect.right = child.getRight();
            rect.bottom = child.getBottom();
        }
    }

    protected boolean isViewTotallyFirstShowed(Component child, Rect relativePosition, DragEdge edge, int surfaceLeft,
                                               int surfaceTop, int surfaceRight, int surfaceBottom) {
        if (mShowEntirely.get(child)) return false;
        int childLeft = relativePosition.left;
        int childRight = relativePosition.right;
        int childTop = relativePosition.top;
        int childBottom = relativePosition.bottom;
        boolean r = false;
        if (getShowMode() == ShowMode.LayDown) {
            if ((edge == DragEdge.Right && surfaceRight <= childLeft)
                    || (edge == DragEdge.Left && surfaceLeft >= childRight)
                    || (edge == DragEdge.Top && surfaceTop >= childBottom)
                    || (edge == DragEdge.Bottom && surfaceBottom <= childTop)) r = true;
        } else if (getShowMode() == ShowMode.PullOut) {
            if ((edge == DragEdge.Right && childRight <= getWidth())
                    || (edge == DragEdge.Left && childLeft >= getPaddingLeft())
                    || (edge == DragEdge.Top && childTop >= getPaddingTop())
                    || (edge == DragEdge.Bottom && childBottom <= getHeight())) r = true;
        }
        return r;
    }

    protected boolean isViewShowing(Component child, Rect relativePosition, DragEdge availableEdge, int surfaceLeft,
                                    int surfaceTop, int surfaceRight, int surfaceBottom) {
        int childLeft = relativePosition.left;
        int childRight = relativePosition.right;
        int childTop = relativePosition.top;
        int childBottom = relativePosition.bottom;
        if (getShowMode() == ShowMode.LayDown) {
            switch (availableEdge) {
                case Right:
                    if (surfaceRight > childLeft && surfaceRight <= childRight) {
                        return true;
                    }
                    break;
                case Left:
                    if (surfaceLeft < childRight && surfaceLeft >= childLeft) {
                        return true;
                    }
                    break;
                case Top:
                    if (surfaceTop >= childTop && surfaceTop < childBottom) {
                        return true;
                    }
                    break;
                case Bottom:
                    if (surfaceBottom > childTop && surfaceBottom <= childBottom) {
                        return true;
                    }
                    break;
            }
        } else if (getShowMode() == ShowMode.PullOut) {
            switch (availableEdge) {
                case Right:
                    if (childLeft <= getWidth() && childRight > getWidth()) return true;
                    break;
                case Left:
                    if (childRight >= getPaddingLeft() && childLeft < getPaddingLeft()) return true;
                    break;
                case Top:
                    if (childTop < getPaddingTop() && childBottom >= getPaddingTop()) return true;
                    break;
                case Bottom:
                    if (childTop < getHeight() && childTop >= getPaddingTop()) return true;
                    break;
            }
        }
        return false;
    }

    protected Rect getRelativePosition(Component child) {
        Component t = child;
        Rect r = new Rect((int) t.getTranslationX(), (int) t.getTranslationY(), 0, 0);
        while (t.getComponentParent() != null) {
            t = (Component) t.getComponentParent();
            if (t == this) break;
            r.left += (int) t.getTranslationX();
            r.top += (int) t.getTranslationY();
        }
        r.right = r.left + child.getWidth();
        r.bottom = r.top + child.getHeight();
        return r;
    }

    private int mEventCounter = 0;

    protected void dispatchSwipeEvent(int surfaceLeft, int surfaceTop, int dx, int dy) {
        DragEdge edge = getDragEdge();
        boolean open = true;
        if (edge == DragEdge.Left) {
            if (dx < 0) open = false;
        } else if (edge == DragEdge.Right) {
            if (dx > 0) open = false;
        } else if (edge == DragEdge.Top) {
            if (dy < 0) open = false;
        } else if (edge == DragEdge.Bottom) {
            if (dy > 0) open = false;
        }

        dispatchSwipeEvent(surfaceLeft, surfaceTop, open);
    }

    protected void dispatchSwipeEvent(int surfaceLeft, int surfaceTop, boolean open) {
//        safeBottomView();
        Status status = getOpenStatus();

        if (!mSwipeListeners.isEmpty()) {
            mEventCounter++;
            for (SwipeListener l : mSwipeListeners) {
                if (mEventCounter == 1) {
                    if (status == Status.Middle) {
                        if (open) {
                            l.onStartOpen(this);
                        } else {
                            l.onStartClose(this);
                        }
                    }
                }
                l.onUpdate(SwipeLayout.this, surfaceLeft - getPaddingLeft(), surfaceTop - getPaddingTop());
            }

            if (status == Status.Close) {
                for (SwipeListener l : mSwipeListeners) {
                    l.onClose(SwipeLayout.this);
                }
                mEventCounter = 0;
            }

            if (status == Status.Open) {
                Component currentBottomView = getCurrentBottomView();
                if (currentBottomView != null) {
                    currentBottomView.setEnabled(true);
                }
                for (SwipeListener l : mSwipeListeners) {
                    l.onOpen(SwipeLayout.this);
                }
                mEventCounter = 0;
            }
        }
    }

    /**
     * prevent bottom view get any touch event. Especially in LayDown mode.
     */
    private void safeBottomView() {
        Status status = getOpenStatus();
        List<Component> bottoms = getBottomViews();

        if (status == Status.Close) {
            for (Component bottom : bottoms) {
                if (bottom != null && bottom.getVisibility() != INVISIBLE) {
                    bottom.setVisibility(INVISIBLE);
                }
            }
        } else {
            Component currentBottomView = getCurrentBottomView();
            if (currentBottomView != null && currentBottomView.getVisibility() != VISIBLE) {
                currentBottomView.setVisibility(VISIBLE);
            }
        }
    }

    protected void dispatchRevealEvent(final int surfaceLeft, final int surfaceTop, final int surfaceRight,
                                       final int surfaceBottom) {
        if (mRevealListeners.isEmpty()) return;
        for (Map.Entry<Component, ArrayList<OnRevealListener>> entry : mRevealListeners.entrySet()) {
            Component child = entry.getKey();
            Rect rect = getRelativePosition(child);
            if (isViewShowing(child, rect, mCurrentDragEdge, surfaceLeft, surfaceTop,
                    surfaceRight, surfaceBottom)) {
                mShowEntirely.put(child, false);
                int distance = 0;
                float fraction = 0f;
                if (getShowMode() == ShowMode.LayDown) {
                    switch (mCurrentDragEdge) {
                        case Left:
                            distance = rect.left - surfaceLeft;
                            fraction = distance / (float) child.getWidth();
                            break;
                        case Right:
                            distance = rect.right - surfaceRight;
                            fraction = distance / (float) child.getWidth();
                            break;
                        case Top:
                            distance = rect.top - surfaceTop;
                            fraction = distance / (float) child.getHeight();
                            break;
                        case Bottom:
                            distance = rect.bottom - surfaceBottom;
                            fraction = distance / (float) child.getHeight();
                            break;
                    }
                } else if (getShowMode() == ShowMode.PullOut) {
                    switch (mCurrentDragEdge) {
                        case Left:
                            distance = rect.right - getPaddingLeft();
                            fraction = distance / (float) child.getWidth();
                            break;
                        case Right:
                            distance = rect.left - getWidth();
                            fraction = distance / (float) child.getWidth();
                            break;
                        case Top:
                            distance = rect.bottom - getPaddingTop();
                            fraction = distance / (float) child.getHeight();
                            break;
                        case Bottom:
                            distance = rect.top - getHeight();
                            fraction = distance / (float) child.getHeight();
                            break;
                    }
                }

                for (OnRevealListener l : entry.getValue()) {
                    l.onReveal(child, mCurrentDragEdge, Math.abs(fraction), distance);
                    if (Math.abs(fraction) == 1) {
                        mShowEntirely.put(child, true);
                    }
                }
            }

            if (isViewTotallyFirstShowed(child, rect, mCurrentDragEdge, surfaceLeft, surfaceTop,
                    surfaceRight, surfaceBottom)) {
                mShowEntirely.put(child, true);
                for (OnRevealListener l : entry.getValue()) {
                    if (mCurrentDragEdge == DragEdge.Left
                            || mCurrentDragEdge == DragEdge.Right)
                        l.onReveal(child, mCurrentDragEdge, 1, child.getWidth());
                    else
                        l.onReveal(child, mCurrentDragEdge, 1, child.getHeight());
                }
            }

        }
    }

    /**
     * {@link ohos.agp.components.Component.LayoutRefreshedListener} added in API 11. I need
     * to support it from API 8.
     */
    public interface OnLayout {
        void onLayout(SwipeLayout v);
    }

    private List<OnLayout> mOnLayoutListeners;

    public void addOnLayoutListener(OnLayout l) {
        if (mOnLayoutListeners == null) mOnLayoutListeners = new ArrayList<OnLayout>();
        mOnLayoutListeners.add(l);
    }

    public void removeOnLayoutListener(OnLayout l) {
        if (mOnLayoutListeners != null) mOnLayoutListeners.remove(l);
    }

    public void clearDragEdge() {
        mDragEdges.clear();
    }

    public void setDrag(DragEdge dragEdge, int childId) {
        clearDragEdge();
        addDrag(dragEdge, childId);
    }

    public void setDrag(DragEdge dragEdge, Component child) {
        clearDragEdge();
        addDrag(dragEdge, child);
    }

    public void addDrag(DragEdge dragEdge, int childId) {
        addDrag(dragEdge, findComponentById(childId), null);
    }

    public void addDrag(DragEdge dragEdge, Component child) {
        addDrag(dragEdge, child, null);
    }

    public void addDrag(DragEdge dragEdge, Component child, ComponentContainer.LayoutConfig params) {
        if (child == null) return;

        if (params == null) {
            params = generateDefaultLayoutParams();
        }
        int gravity = -1;
        switch (dragEdge) {
            case Left:
                gravity = LayoutAlignment.LEFT;
                break;
            case Right:
                gravity = LayoutAlignment.RIGHT;
                break;
            case Top:
                gravity = LayoutAlignment.TOP;
                break;
            case Bottom:
                gravity = LayoutAlignment.BOTTOM;
                break;
        }
        if (params instanceof LayoutConfig) {
            ((LayoutConfig) params).alignment = gravity;
        }
        addComponent(child, 0, params);
        needReset = true;
        postLayout();
    }

    @Override
    public void onRefreshed(Component component) {
        if (needReset) {
            needReset = false;
            resetLayout();
        }
        refreshLayout(false, false);
        if (mOnLayoutListeners != null) for (int i = 0; i < mOnLayoutListeners.size(); i++) {
            mOnLayoutListeners.get(i).onLayout(this);
        }
    }

    private LayoutConfig generateDefaultLayoutParams() {
        return new LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
    }

    @Override
    public void addComponent(Component child, int index, ComponentContainer.LayoutConfig params) {
        if (child == null) return;
        int gravity;
        try {
            gravity = ((LayoutConfig) params).alignment;//(Integer) params.getClass().getField("gravity").get(params);
        } catch (Exception e) {
            gravity = LayoutAlignment.UNSET;
        }

        if (gravity > 0) {

            if ((gravity & LayoutAlignment.LEFT) == LayoutAlignment.LEFT) {
                mDragEdges.put(DragEdge.Left, child);
            }
            if ((gravity & LayoutAlignment.RIGHT) == LayoutAlignment.RIGHT) {
                mDragEdges.put(DragEdge.Right, child);
            }
            if ((gravity & LayoutAlignment.TOP) == LayoutAlignment.TOP) {
                mDragEdges.put(DragEdge.Top, child);
            }
            if ((gravity & LayoutAlignment.BOTTOM) == LayoutAlignment.BOTTOM) {
                mDragEdges.put(DragEdge.Bottom, child);
            }
        } else {
            for (Map.Entry<DragEdge, Component> entry : mDragEdges.entrySet()) {
                if (entry.getValue() == null) {
                    //means used the drag_edge attr, the no gravity child should be use set
                    mDragEdges.put(entry.getKey(), child);
                    break;
                }
            }
        }
        if (child.getComponentParent() == this) {
            return;
        }
        super.addComponent(child, index, params);
    }

    void layoutPullOut() {
        Component surfaceView = getSurfaceView();
        Rect surfaceRect = mViewBoundCache.get(surfaceView);
        if (surfaceRect == null) surfaceRect = computeSurfaceLayoutArea(false);
        if (surfaceView != null) {
            surfaceView.arrange(surfaceRect.left, surfaceRect.top, surfaceRect.right - surfaceRect.left, surfaceRect.bottom - surfaceRect.top);
            moveChildToFront(surfaceView);
        }
        Component currentBottomView = getCurrentBottomView();
        Rect bottomViewRect = mViewBoundCache.get(currentBottomView);
        if (bottomViewRect == null)
            bottomViewRect = computeBottomLayoutAreaViaSurface(ShowMode.PullOut, surfaceRect);
        if (currentBottomView != null) {
            currentBottomView.arrange(bottomViewRect.left, bottomViewRect.top, bottomViewRect.right - bottomViewRect.left, bottomViewRect.bottom - bottomViewRect.top);
        }
    }

    void layoutLayDown() {
        Component surfaceView = getSurfaceView();
        Rect surfaceRect = mViewBoundCache.get(surfaceView);
        if (surfaceRect == null) surfaceRect = computeSurfaceLayoutArea(false);
        if (surfaceView != null) {
            surfaceView.arrange(surfaceRect.left, surfaceRect.top, surfaceRect.right - surfaceRect.left, surfaceRect.bottom - surfaceRect.top);
            moveChildToFront(surfaceView);
        }
        Component currentBottomView = getCurrentBottomView();
        Rect bottomViewRect = mViewBoundCache.get(currentBottomView);
        if (bottomViewRect == null)
            bottomViewRect = computeBottomLayoutAreaViaSurface(ShowMode.LayDown, surfaceRect);
        if (currentBottomView != null) {
            currentBottomView.arrange(bottomViewRect.left, bottomViewRect.top, bottomViewRect.right - bottomViewRect.left, bottomViewRect.bottom - bottomViewRect.top);
        }
    }

    private boolean mIsBeingDragged;


    private float sX = -1;
    private float sY = -1;
    private long downTime = 0;
    private long lastClickTime = 0;

    private void checkCanDrag(TouchEvent ev) {
        if (mIsBeingDragged) return;
        if (getOpenStatus() == Status.Middle) {
            mIsBeingDragged = true;
            return;
        }
        Status status = getOpenStatus();
        float distanceX = ev.getPointerScreenPosition(0).getX() - sX;
        float distanceY = ev.getPointerScreenPosition(0).getY() - sY;
        float angle = Math.abs(distanceY / distanceX);
        angle = (float) Math.toDegrees(Math.atan(angle));
        if (getOpenStatus() == Status.Close) {
            DragEdge dragEdge;
            if (angle < 45) {
                if (distanceX > 0 && isLeftSwipeEnabled()) {
                    dragEdge = DragEdge.Left;
                } else if (distanceX < 0 && isRightSwipeEnabled()) {
                    dragEdge = DragEdge.Right;
                } else return;

            } else {
                if (distanceY > 0 && isTopSwipeEnabled()) {
                    dragEdge = DragEdge.Top;
                } else if (distanceY < 0 && isBottomSwipeEnabled()) {
                    dragEdge = DragEdge.Bottom;
                } else return;
            }
            setCurrentDragEdge(dragEdge);
        }

        boolean doNothing = false;
        if (mCurrentDragEdge == DragEdge.Right) {
            boolean suitable = (status == Status.Open && distanceX > mTouchSlop)
                    || (status == Status.Close && distanceX < -mTouchSlop);
            suitable = suitable || (status == Status.Middle);

            if (angle > 30 || !suitable) {
                doNothing = true;
            }
        }

        if (mCurrentDragEdge == DragEdge.Left) {
            boolean suitable = (status == Status.Open && distanceX < -mTouchSlop)
                    || (status == Status.Close && distanceX > mTouchSlop);
            suitable = suitable || status == Status.Middle;

            if (angle > 30 || !suitable) {
                doNothing = true;
            }
        }

        if (mCurrentDragEdge == DragEdge.Top) {
            boolean suitable = (status == Status.Open && distanceY < -mTouchSlop)
                    || (status == Status.Close && distanceY > mTouchSlop);
            suitable = suitable || status == Status.Middle;

            if (angle < 60 || !suitable) {
                doNothing = true;
            }
        }

        if (mCurrentDragEdge == DragEdge.Bottom) {
            boolean suitable = (status == Status.Open && distanceY > mTouchSlop)
                    || (status == Status.Close && distanceY < -mTouchSlop);
            suitable = suitable || status == Status.Middle;

            if (angle < 60 || !suitable) {
                doNothing = true;
            }
        }
        mIsBeingDragged = !doNothing;
    }

    private void scroll() {
        Component dragComponent = mDragEdges.get(mCurrentDragEdge);
        scroll(isDragOpen ? getDragEdge() == DragEdge.Left || getDragEdge() == DragEdge.Right ? dragComponent.getWidth() : dragComponent.getHeight() : 0);
    }

    private void scroll(int distance) {
        int start = mDragDistance;
        int end = distance;
        if (scrollAnimatorValue == null) {
            scrollAnimatorValue = new AnimatorValue();
        }
        scrollAnimatorValue.setDuration(200);
        scrollAnimatorValue.setCurveType(Animator.CurveType.DECELERATE);
        scrollAnimatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                mDragDistance = start + (int) ((end - start) * v);
                if (end == 0) {
                    refreshLayout(true);
                } else {
                    refreshLayout();
                }
            }
        });
        scrollAnimatorValue.start();
    }

    private void drag(TouchEvent touchEvent) {
        Component dragComponent = mDragEdges.get(mCurrentDragEdge);
        if (dragComponent == null) return;
        int currentOffset = mDragDistance;
        if (mCurrentDragEdge == DragEdge.Right) {
            float offset = sX - touchEvent.getPointerScreenPosition(0).getX();
            if (offset > 0) {
                isDragOpen = true;
            } else {
                isDragOpen = false;
            }
            currentOffset += offset;
            if (currentOffset < 0) {
                currentOffset = 0;
            }
            if (currentOffset > dragComponent.getWidth()) {
                currentOffset = dragComponent.getWidth();
            }
            mDragDistance = currentOffset;
            refreshLayout();
        } else if (mCurrentDragEdge == DragEdge.Left) {
            float offset = touchEvent.getPointerScreenPosition(0).getX() - sX;
            if (offset > 0) {
                isDragOpen = true;
            } else {
                isDragOpen = false;
            }
            currentOffset += offset;
            if (currentOffset < 0) {
                currentOffset = 0;
            }
            if (currentOffset > dragComponent.getWidth()) {
                currentOffset = dragComponent.getWidth();
            }
            mDragDistance = currentOffset;
            refreshLayout();
        } else if (mCurrentDragEdge == DragEdge.Top) {
            float offset = touchEvent.getPointerScreenPosition(0).getY() - sY;
            if (offset > 0) {
                isDragOpen = true;
            } else {
                isDragOpen = false;
            }
            currentOffset += offset;
            if (currentOffset < 0) {
                currentOffset = 0;
            }
            if (currentOffset > dragComponent.getHeight()) {
                currentOffset = dragComponent.getHeight();
            }
            mDragDistance = currentOffset;
            refreshLayout();
        } else if (mCurrentDragEdge == DragEdge.Bottom) {
            float offset = sY - touchEvent.getPointerScreenPosition(0).getY();
            if (offset > 0) {
                isDragOpen = true;
            } else {
                isDragOpen = false;
            }
            currentOffset += offset;
            if (currentOffset < 0) {
                currentOffset = 0;
            }
            if (currentOffset > dragComponent.getHeight()) {
                currentOffset = dragComponent.getHeight();
            }
            mDragDistance = currentOffset;
            refreshLayout();
        }
        sX = touchEvent.getPointerScreenPosition(0).getX();
        sY = touchEvent.getPointerScreenPosition(0).getY();
    }

    private void resetLayout() {
        mDragDistance = 0;
        Component coverComponent = getSurfaceView();
        coverComponent.setTranslation(0, 0);
//        coverComponent.arrange(0, 0, coverComponent.getWidth(), coverComponent.getHeight());

        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count - 1; i++) {
                Component component = getComponentAt(i);
                if (component == mDragEdges.get(DragEdge.Left)) {
                    component.setTranslationX(-component.getWidth());
//                    component.arrange(-component.getWidth(), 0, component.getWidth(), component.getHeight());
                } else if (component == mDragEdges.get(DragEdge.Right)) {
                    component.setTranslationX(getWidth() - getPaddingLeft() - getPaddingRight());
//                    component.arrange(getWidth() - getPaddingLeft() - getPaddingRight(), 0, component.getWidth(), component.getHeight());
                } else if (component == mDragEdges.get(DragEdge.Top)) {
                    component.setTranslationY(-component.getHeight());
//                    component.arrange(0, -component.getHeight(), component.getWidth(), component.getHeight());
                } else if (component == mDragEdges.get(DragEdge.Bottom)) {
                    component.setTranslationY(getHeight() - getPaddingTop() - getPaddingBottom());
//                    component.arrange(0, getHeight() - getPaddingTop() - getPaddingBottom(), component.getWidth(), component.getHeight());
                } else {
                    component.setTranslationX(getWidth() - getPaddingLeft() - getPaddingRight());
//                    component.arrange(getWidth() - getPaddingLeft() - getPaddingRight(), 0, component.getWidth(), component.getHeight());
                }
            }
        }
    }

    private void refreshLayout() {
        refreshLayout(false, true);
    }

    private void refreshLayout(boolean isClose) {
        refreshLayout(isClose, true);
    }

    private void refreshLayout(boolean isClose, boolean notify) {
        Component dragComponent = mDragEdges.get(mCurrentDragEdge);
        Component coverComponent = getSurfaceView();
        if (mCurrentDragEdge == DragEdge.Right) {
            coverComponent.setTranslationX(-mDragDistance);
//            coverComponent.arrange(-mDragDistance, 0, coverComponent.getWidth(), coverComponent.getHeight());
            if (dragComponent != null) {
                if (getShowMode() == ShowMode.PullOut) {
                    dragComponent.setTranslationX(getWidth() - getPaddingLeft() - getPaddingRight() - mDragDistance);
//                    dragComponent.arrange(getWidth() - getPaddingLeft() - getPaddingRight() - mDragDistance, 0, dragComponent.getWidth(), dragComponent.getHeight());
                } else if (getShowMode() == ShowMode.LayDown) {
                    dragComponent.setTranslationX(getWidth() - getPaddingLeft() - getPaddingRight() - dragComponent.getWidth());
//                    dragComponent.arrange(getWidth() - getPaddingLeft() - getPaddingRight() - dragComponent.getWidth(), 0, dragComponent.getWidth(), dragComponent.getHeight());
                }
            }
        } else if (mCurrentDragEdge == DragEdge.Left) {
            coverComponent.setTranslationX(mDragDistance);
//            coverComponent.arrange(mDragDistance, 0, coverComponent.getWidth(), coverComponent.getHeight());
            if (dragComponent != null) {
                if (getShowMode() == ShowMode.PullOut) {
                    dragComponent.setTranslationX(mDragDistance - dragComponent.getWidth());
//                    dragComponent.arrange(mDragDistance - dragComponent.getWidth(), 0, dragComponent.getWidth(), dragComponent.getHeight());
                } else if (getShowMode() == ShowMode.LayDown) {
                    dragComponent.setTranslationX(0);
                }
            }
        } else if (mCurrentDragEdge == DragEdge.Top) {
            coverComponent.setTranslationY(mDragDistance);
//            coverComponent.arrange(0, mDragDistance, coverComponent.getWidth(), coverComponent.getHeight());
            if (dragComponent != null) {
                if (getShowMode() == ShowMode.PullOut) {
                    dragComponent.setTranslationY(mDragDistance - dragComponent.getHeight());
//                    dragComponent.arrange(0, mDragDistance - dragComponent.getHeight(), dragComponent.getWidth(), dragComponent.getHeight());
                } else if (getShowMode() == ShowMode.LayDown) {
//                    dragComponent.arrange(0, 0, dragComponent.getWidth(), dragComponent.getHeight());
                    dragComponent.setTranslationY(0);
                }
            }
        } else if (mCurrentDragEdge == DragEdge.Bottom) {
            coverComponent.setTranslationY(-mDragDistance);
//            coverComponent.arrange(0, -mDragDistance, coverComponent.getWidth(), coverComponent.getHeight());
            if (dragComponent != null) {
                if (getShowMode() == ShowMode.PullOut) {
                    dragComponent.setTranslationY(getHeight() - getPaddingTop() - getPaddingBottom() - mDragDistance);
//                    dragComponent.arrange(0, getHeight() - getPaddingTop() - getPaddingBottom() - mDragDistance, dragComponent.getWidth(), dragComponent.getHeight());
                } else if (getShowMode() == ShowMode.LayDown) {
                    dragComponent.setTranslationY(0);
//                    dragComponent.arrange(0, getHeight() - getPaddingTop() - getPaddingBottom() - dragComponent.getHeight(), dragComponent.getWidth(), dragComponent.getHeight());
                }
            }
        }
        if (notify) {
            dispatchRevealEvent((int) coverComponent.getTranslationX(), (int) coverComponent.getTranslationY(), (int) coverComponent.getTranslationX() + coverComponent.getWidth(),
                    (int) coverComponent.getTranslationY() + coverComponent.getHeight());
            dispatchSwipeEvent((int) coverComponent.getTranslationX(), (int) coverComponent.getTranslationY(), !isClose);
        }
//        dispatchRevealEvent(coverComponent.getLeft(), coverComponent.getTop(), coverComponent.getRight(), coverComponent.getBottom());
//        dispatchSwipeEvent(coverComponent.getLeft(), coverComponent.getTop(), !isClose);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (!isSwipeEnabled()) {
            return false;
        }

        for (SwipeDenier denier : mSwipeDeniers) {
            if (denier != null && denier.shouldDenySwipe(touchEvent)) {
                return false;
            }
        }

        if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
            sX = touchEvent.getPointerScreenPosition(0).getX();
            sY = touchEvent.getPointerScreenPosition(0).getY();
            downTime = System.currentTimeMillis();
            if (scrollAnimatorValue != null && scrollAnimatorValue.isRunning()) {
                scrollAnimatorValue.stop();
            }
        } else if (touchEvent.getAction() == TouchEvent.POINT_MOVE) {
            if (longClickListener != null && !mIsBeingDragged && getOpenStatus() == Status.Close && downTime != 0 && System.currentTimeMillis() > downTime + 500) {
                longClickListener.onLongClicked(this);
                downTime = 0;
            }
            checkCanDrag(touchEvent);
            if (mIsBeingDragged) {
                drag(touchEvent);
//                disableParent();
            }
            return true;
        } else if (touchEvent.getAction() == TouchEvent.CANCEL || touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP) {
            if (mDoubleClickListener != null && System.currentTimeMillis() - lastClickTime < 300) {
                mDoubleClickListener.onDoubleClick(this, isTouchOnSurface(touchEvent));
                lastClickTime = 0;
            }

            if (!mIsBeingDragged && downTime != 0 && getOpenStatus() == Status.Close && isTouchOnSurface(touchEvent)) {
                if (clickListener != null) {
                    clickListener.onClick(this);
                }
                lastClickTime = System.currentTimeMillis();
            } else if (mClickToClose && !mIsBeingDragged && getOpenStatus() == Status.Open && isTouchOnSurface(touchEvent)) {
                scroll(0);
            } else if (getOpenStatus() == Status.Middle) {
                scroll();
            }

            downTime = 0;
            mIsBeingDragged = false;
//            enableParent();
        }
        return true;
    }

    public boolean isClickToClose() {
        return mClickToClose;
    }

    public void setClickToClose(boolean mClickToClose) {
        this.mClickToClose = mClickToClose;
    }

    public void setSwipeEnabled(boolean enabled) {
        mSwipeEnabled = enabled;
    }

    public boolean isSwipeEnabled() {
        return mSwipeEnabled;
    }

    public boolean isLeftSwipeEnabled() {
        Component bottomView = mDragEdges.get(DragEdge.Left);
        return bottomView != null && bottomView.getComponentParent() == this
                && bottomView != getSurfaceView() && mSwipesEnabled[DragEdge.Left.ordinal()];
    }

    public void setLeftSwipeEnabled(boolean leftSwipeEnabled) {
        this.mSwipesEnabled[DragEdge.Left.ordinal()] = leftSwipeEnabled;
    }

    public boolean isRightSwipeEnabled() {
        Component bottomView = mDragEdges.get(DragEdge.Right);
        return bottomView != null && bottomView.getComponentParent() == this
                && bottomView != getSurfaceView() && mSwipesEnabled[DragEdge.Right.ordinal()];
    }

    public void setRightSwipeEnabled(boolean rightSwipeEnabled) {
        this.mSwipesEnabled[DragEdge.Right.ordinal()] = rightSwipeEnabled;
    }

    public boolean isTopSwipeEnabled() {
        Component bottomView = mDragEdges.get(DragEdge.Top);
        return bottomView != null && bottomView.getComponentParent() == this
                && bottomView != getSurfaceView() && mSwipesEnabled[DragEdge.Top.ordinal()];
    }

    public void setTopSwipeEnabled(boolean topSwipeEnabled) {
        this.mSwipesEnabled[DragEdge.Top.ordinal()] = topSwipeEnabled;
    }

    public boolean isBottomSwipeEnabled() {
        Component bottomView = mDragEdges.get(DragEdge.Bottom);
        return bottomView != null && bottomView.getComponentParent() == this
                && bottomView != getSurfaceView() && mSwipesEnabled[DragEdge.Bottom.ordinal()];
    }

    public void setBottomSwipeEnabled(boolean bottomSwipeEnabled) {
        this.mSwipesEnabled[DragEdge.Bottom.ordinal()] = bottomSwipeEnabled;
    }

    public float getWillOpenPercentAfterOpen() {
        return mWillOpenPercentAfterOpen;
    }

    /***
     * Allows to stablish at what percentage of revealing the view below should the view finish opening
     * if it was already open before dragging
     *
     * @param willOpenPercentAfterOpen The percentage of view revealed to trigger, default value is 0.25
     */
    public void setWillOpenPercentAfterOpen(float willOpenPercentAfterOpen) {
        this.mWillOpenPercentAfterOpen = willOpenPercentAfterOpen;
    }

    public float getWillOpenPercentAfterClose() {
        return mWillOpenPercentAfterClose;
    }

    /***
     * Allows to stablish at what percentage of revealing the view below should the view finish opening
     * if it was already closed before dragging
     *
     * @param willOpenPercentAfterClose The percentage of view revealed to trigger, default value is 0.75
     */
    public void setWillOpenPercentAfterClose(float willOpenPercentAfterClose) {
        this.mWillOpenPercentAfterClose = willOpenPercentAfterClose;
    }

    private boolean insideAdapterView() {
        return getAdapterView() != null;
    }

    private ListContainer getAdapterView() {
        ComponentParent t = getComponentParent();
        if (t instanceof ListContainer) {
            return (ListContainer) t;
        }
        return null;
    }

    private void performAdapterViewItemClick() {
        if (getOpenStatus() != Status.Close) return;
        ComponentParent t = getComponentParent();
        if (t instanceof ListContainer) {
            ListContainer view = (ListContainer) t;
            int p = view.getIndexForComponent(SwipeLayout.this);
            if (p != ListContainer.INVALID_INDEX) {
                view.executeItemClick(view.getComponentAt(p - view.getItemPosByVisibleIndex(0)), p, view
                        .getItemProvider().getItemId(p));
            }
        }
    }

    ClickedListener clickListener;

    @Override
    public void setClickedListener(ClickedListener listener) {
        clickListener = listener;
    }

    LongClickedListener longClickListener;

    @Override
    public void setLongClickedListener(LongClickedListener listener) {
        longClickListener = listener;
    }

    private Rect hitSurfaceRect;

    private boolean isTouchOnSurface(TouchEvent ev) {
        Component surfaceView = getSurfaceView();
        if (surfaceView == null) {
            return false;
        }
        if (hitSurfaceRect == null) {
            hitSurfaceRect = new Rect();
        }
        hitSurfaceRect.left = surfaceView.getLeft();
        hitSurfaceRect.right = surfaceView.getRight();
        hitSurfaceRect.top = surfaceView.getTop();
        hitSurfaceRect.bottom = surfaceView.getBottom();
        return contains(hitSurfaceRect, (int) getTouchX(ev, 0), (int) getTouchY(ev, 0));
//        surfaceView.getHitRect(hitSurfaceRect);
//        return hitSurfaceRect.contains((int) ev.getX(), (int) ev.getY());
    }

    private boolean contains(Rect rect, int x, int y) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return left < right && top < bottom  // check for empty first
                && x >= left && x < right && y >= top && y < bottom;
    }

    private float getTouchX(TouchEvent touchEvent, int index) {
        float touchX = 0;
        if (touchEvent.getPointerCount() > index) {
            int[] xy = getLocationOnScreen();
            if (xy != null && xy.length == 2) {
                touchX = touchEvent.getPointerScreenPosition(index).getX() - xy[0];
            } else {
                touchX = touchEvent.getPointerPosition(index).getX();
            }
        }
        return touchX;
    }

    private float getTouchY(TouchEvent touchEvent, int index) {
        float touchY = 0;
        if (touchEvent.getPointerCount() > index) {
            int[] xy = getLocationOnScreen();
            if (xy != null && xy.length == 2) {
                touchY = touchEvent.getPointerScreenPosition(index).getY() - xy[1];
            } else {
                touchY = touchEvent.getPointerPosition(index).getY();
            }
        }
        return touchY;
    }

    /**
     * set the drag distance, it will force set the bottom view's width or
     * height via this value.
     *
     * @param max max distance in dp unit
     */
    public void setDragDistance(int max) {
        if (max < 0) max = 0;
        mDragDistance = dp2px(max);
        postLayout();
    }

    /**
     * There are 2 diffirent show mode.
     * {@link ShowMode}.PullOut and
     * {@link ShowMode}.LayDown.
     *
     * @param mode
     */
    public void setShowMode(ShowMode mode) {
        mShowMode = mode;
        postLayout();
    }

    public DragEdge getDragEdge() {
        return mCurrentDragEdge;
    }

    public int getDragDistance() {
        return mDragDistance;
    }

    public ShowMode getShowMode() {
        return mShowMode;
    }

    public Component getSurfaceView() {
        if (getChildCount() == 0) return null;
        return getComponentAt(getChildCount() - 1);
    }

    public Component getCurrentBottomView() {
        List<Component> bottoms = getBottomViews();
        if (mCurrentDragEdge.ordinal() < bottoms.size()) {
            return bottoms.get(mCurrentDragEdge.ordinal());
        }
        return null;
    }

    public List<Component> getBottomViews() {
        ArrayList<Component> bottoms = new ArrayList<Component>();
        for (DragEdge dragEdge : DragEdge.values()) {
            bottoms.add(mDragEdges.get(dragEdge));
        }
        return bottoms;
    }

    public enum Status {
        Middle,
        Open,
        Close
    }

    /**
     * get the open status.
     *
     * @return {@link Status} Open , Close or
     * Middle.
     */
    public Status getOpenStatus() {
        Component surfaceView = getSurfaceView();
        if (surfaceView == null) {
            return Status.Close;
        }
        int surfaceLeft = (int) surfaceView.getTranslationX();
        int surfaceTop = (int) surfaceView.getTranslationY();
//        int surfaceLeft = surfaceView.getLeft();
//        int surfaceTop = surfaceView.getTop();
        if (surfaceLeft == 0 && surfaceTop == 0) return Status.Close;

        if (getDragEdge() == DragEdge.Left && mDragEdges.get(mCurrentDragEdge) != null && mDragEdges.get(mCurrentDragEdge).getWidth() == mDragDistance ||
                getDragEdge() == DragEdge.Right && mDragEdges.get(mCurrentDragEdge) != null && mDragEdges.get(mCurrentDragEdge).getWidth() == mDragDistance ||
                getDragEdge() == DragEdge.Top && mDragEdges.get(mCurrentDragEdge) != null && mDragEdges.get(mCurrentDragEdge).getHeight() == mDragDistance ||
                getDragEdge() == DragEdge.Bottom && mDragEdges.get(mCurrentDragEdge) != null && mDragEdges.get(mCurrentDragEdge).getHeight() == mDragDistance) {
            return Status.Open;
        }

        return Status.Middle;
    }


    /**
     * smoothly open surface.
     */
    public void open() {
        open(true, true);
    }

    public void open(boolean smooth) {
        open(smooth, true);
    }

    public void open(boolean smooth, boolean notify) {
        Component dragComponent = mDragEdges.get(mCurrentDragEdge);
        if (smooth) {
            scroll(getDragEdge() == DragEdge.Left || getDragEdge() == DragEdge.Right ? dragComponent.getWidth() : dragComponent.getHeight());
        } else {
            mDragDistance = getDragEdge() == DragEdge.Left || getDragEdge() == DragEdge.Right ? dragComponent.getWidth() : dragComponent.getHeight();
            refreshLayout();
        }
    }

    public void open(DragEdge edge) {
        setCurrentDragEdge(edge);
        open(true, true);
    }

    public void open(boolean smooth, DragEdge edge) {
        setCurrentDragEdge(edge);
        open(smooth, true);
    }

    public void open(boolean smooth, boolean notify, DragEdge edge) {
        setCurrentDragEdge(edge);
        open(smooth, notify);
    }

    /**
     * smoothly close surface.
     */
    public void close() {
        close(true, true);
    }

    public void close(boolean smooth) {
        close(smooth, true);
    }

    /**
     * close surface
     *
     * @param smooth smoothly or not.
     * @param notify if notify all the listeners.
     */
    public void close(boolean smooth, boolean notify) {
        Component dragComponent = mDragEdges.get(mCurrentDragEdge);
        if (smooth) {
            scroll(0);
        } else {
            mDragDistance = 0;
            refreshLayout(true);
        }
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean smooth) {
        if (getOpenStatus() == Status.Open)
            close(smooth);
        else if (getOpenStatus() == Status.Close) open(smooth);
    }


    private Rect computeSurfaceLayoutArea(boolean open) {
        int l = getPaddingLeft(), t = getPaddingTop();
        if (open) {
            if (mCurrentDragEdge == DragEdge.Left)
                l = getPaddingLeft() + mDragDistance;
            else if (mCurrentDragEdge == DragEdge.Right)
                l = getPaddingLeft() - mDragDistance;
            else if (mCurrentDragEdge == DragEdge.Top)
                t = getPaddingTop() + mDragDistance;
            else t = getPaddingTop() - mDragDistance;
        }
        return new Rect(l, t, l + getWidth(), t + getHeight());
    }

    private Rect computeBottomLayoutAreaViaSurface(ShowMode mode, Rect surfaceArea) {
        Rect rect = surfaceArea;
        Component bottomView = getCurrentBottomView();

        int bl = rect.left, bt = rect.top, br = rect.right, bb = rect.bottom;
        if (mode == ShowMode.PullOut) {
            if (mCurrentDragEdge == DragEdge.Left)
                bl = rect.left - mDragDistance;
            else if (mCurrentDragEdge == DragEdge.Right)
                bl = rect.right;
            else if (mCurrentDragEdge == DragEdge.Top)
                bt = rect.top - mDragDistance;
            else bt = rect.bottom;

            if (mCurrentDragEdge == DragEdge.Left || mCurrentDragEdge == DragEdge.Right) {
                bb = rect.bottom;
                br = bl + (bottomView == null ? 0 : bottomView.getWidth());
            } else {
                bb = bt + (bottomView == null ? 0 : bottomView.getHeight());
                br = rect.right;
            }
        } else if (mode == ShowMode.LayDown) {
            if (mCurrentDragEdge == DragEdge.Left)
                br = bl + mDragDistance;
            else if (mCurrentDragEdge == DragEdge.Right)
                bl = br - mDragDistance;
            else if (mCurrentDragEdge == DragEdge.Top)
                bb = bt + mDragDistance;
            else bt = bb - mDragDistance;

        }
        return new Rect(bl, bt, br, bb);

    }

    private Rect computeBottomLayDown(DragEdge dragEdge) {
        int bl = getPaddingLeft(), bt = getPaddingTop();
        int br, bb;
        if (dragEdge == DragEdge.Right) {
            bl = getWidth() - mDragDistance;
        } else if (dragEdge == DragEdge.Bottom) {
            bt = getHeight() - mDragDistance;
        }
        if (dragEdge == DragEdge.Left || dragEdge == DragEdge.Right) {
            br = bl + mDragDistance;
            bb = bt + getHeight();
        } else {
            br = bl + getWidth();
            bb = bt + mDragDistance;
        }
        return new Rect(bl, bt, br, bb);
    }

    public void setOnDoubleClickListener(DoubleClickListener doubleClickListener) {
        mDoubleClickListener = doubleClickListener;
    }

    public interface DoubleClickListener {
        void onDoubleClick(SwipeLayout layout, boolean surface);
    }

    private int dp2px(float dp) {
        return (int) (dp * getResourceManager().getDeviceCapability().screenDensity / 160 + 0.5f);
    }


    @Deprecated
    public void setDragEdge(DragEdge dragEdge) {
        clearDragEdge();
        if (getChildCount() >= 2) {
            mDragEdges.put(dragEdge, getComponentAt(getChildCount() - 2));
        }
        setCurrentDragEdge(dragEdge);
    }

    public void onViewRemoved(Component child) {
        for (Map.Entry<DragEdge, Component> entry : new HashMap<DragEdge, Component>(mDragEdges).entrySet()) {
            if (entry.getValue() == child) {
                mDragEdges.remove(entry.getKey());
            }
        }
    }

    public Map<DragEdge, Component> getDragEdgeMap() {
        return mDragEdges;
    }

    @Deprecated
    public List<DragEdge> getDragEdges() {
        return new ArrayList<DragEdge>(mDragEdges.keySet());
    }

    @Deprecated
    public void setDragEdges(List<DragEdge> dragEdges) {
        clearDragEdge();
        for (int i = 0, size = Math.min(dragEdges.size(), getChildCount() - 1); i < size; i++) {
            DragEdge dragEdge = dragEdges.get(i);
            mDragEdges.put(dragEdge, getComponentAt(i));
        }
        if (dragEdges.size() == 0 || dragEdges.contains(DefaultDragEdge)) {
            setCurrentDragEdge(DefaultDragEdge);
        } else {
            setCurrentDragEdge(dragEdges.get(0));
        }
    }

    @Deprecated
    public void setDragEdges(DragEdge... mDragEdges) {
        clearDragEdge();
        setDragEdges(Arrays.asList(mDragEdges));
    }

    @Deprecated
    public void setBottomViewIds(int leftId, int rightId, int topId, int bottomId) {
        addDrag(DragEdge.Left, findComponentById(leftId));
        addDrag(DragEdge.Right, findComponentById(rightId));
        addDrag(DragEdge.Top, findComponentById(topId));
        addDrag(DragEdge.Bottom, findComponentById(bottomId));
    }

    private float getCurrentOffset() {
        if (mCurrentDragEdge == null) return 0;
        return mEdgeSwipesOffset[mCurrentDragEdge.ordinal()];
    }

    private void setCurrentDragEdge(DragEdge dragEdge) {
        mCurrentDragEdge = dragEdge;
//        updateBottomViews();
    }

    private void updateBottomViews() {
        Component currentBottomView = getCurrentBottomView();
        if (currentBottomView != null) {
            if (mCurrentDragEdge == DragEdge.Left || mCurrentDragEdge == DragEdge.Right) {
                mDragDistance = currentBottomView.getWidth() - dp2px(getCurrentOffset());
            } else {
                mDragDistance = currentBottomView.getHeight() - dp2px(getCurrentOffset());
            }
        }

        if (mShowMode == ShowMode.PullOut) {
            layoutPullOut();
        } else if (mShowMode == ShowMode.LayDown) {
            layoutLayDown();
        }

        safeBottomView();
    }

    public Object getTag(int key) {
        return tags.get(key);
    }

    public void setTag(int key, Object value) {
        tags.put(key, value);
    }

    private List<Component> disableComponents;

    private void disableParent() {
        disableComponents = new ArrayList<>();
        disableDo(this);

    }

    private void disableDo(Component component) {
        final ComponentContainer parent = (ComponentContainer) component.getComponentParent();
        if (parent != null) {
            if (parent.isEnabled()) {
                disableComponents.add(parent);
                parent.setEnabled(false);
            }
            disableDo(parent);
        }
    }

    private void enableParent() {
        if (disableComponents != null && disableComponents.size() > 0) {
            for (int i = 0; i < disableComponents.size(); i++) {
                disableComponents.get(i).setEnabled(true);
            }
        }
    }
}
