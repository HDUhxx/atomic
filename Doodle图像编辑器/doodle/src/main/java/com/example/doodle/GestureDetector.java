/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.doodle;

import ohos.agp.components.Component;
import ohos.agp.components.VelocityDetector;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Detects various gestures and events using the supplied {@link }s.
 * The {@link OnGestureListener} callback will notify users when a particular
 * motion event has occurred. This class should only be used with {@link TouchEvent}s
 * reported via touch (don't use for trackball events).
 * <p>
 * To use this class:
 * <ul>
 *  <li>Create an instance of the {@code GestureDetector} for your {@link Component}
 *
 * @since 2021-04-29
 */
public class GestureDetector {
    /**
     * The listener that is used to notify when gestures occur.
     * If you want to listen for all the different gestures then implement
     * this interface. If you only want to listen for a subset it might
     * be easier to extend {@link SimpleOnGestureListener}.
     *
     * @since 2021-04-29
     */
    public interface OnGestureListener {

        /**
         * Notified when a tap occurs with the down {@link TouchEvent}
         * that triggered it. This will be triggered immediately for
         * every down event. All other events should be preceded by this.
         *
         * @param e The down motion event.
         * @return boolean
         */
        boolean onDown(TouchEvent e);

        /**
         * The user has performed a down {@link TouchEvent} and not performed
         * a move or up yet. This event is commonly used to provide visual
         * feedback to the user to let them know that their action has been
         * recognized i.e. highlight an element.
         *
         * @param e The down motion event
         */
        void onShowPress(TouchEvent e);

        /**
         * Notified when a tap occurs with the up {@link TouchEvent}
         * that triggered it.
         *
         * @param e The up motion event that completed the first tap
         * @return true if the event is consumed, else false
         */
        boolean onSingleTapUp(TouchEvent e);

        /**
         * Notified when a scroll occurs with the initial on down {@link TouchEvent} and the
         * current move {@link TouchEvent}. The distance in x and y is also supplied for
         * convenience.
         *
         * @param e1 The first down motion event that started the scrolling.
         * @param e2 The move motion event that triggered the current onScroll.
         * @param distanceX The distance along the X axis that has been scrolled since the last
         * call to onScroll. This is NOT the distance between {@code e1}
         * and {@code e2}.
         * @param distanceY The distance along the Y axis that has been scrolled since the last
         * call to onScroll. This is NOT the distance between {@code e1}
         * and {@code e2}.
         * @return true if the event is consumed, else false
         */
        boolean onScroll(TouchEvent e1, TouchEvent e2, float distanceX, float distanceY);

        /**
         * Notified when a long press occurs with the initial on down {@link TouchEvent}
         * that trigged it.
         *
         * @param e The initial on down motion event that started the longpress.
         */
        void onLongPress(TouchEvent e);

        /**
         * Notified of a fling event when it occurs with the initial on down {@link TouchEvent}
         * and the matching up {@link TouchEvent}. The calculated velocity is supplied along
         * the x and y axis in pixels per second.
         *
         * @param e1 The first down motion event that started the fling.
         * @param e2 The move motion event that triggered the current onFling.
         * @param velocityX The velocity of this fling measured in pixels per second
         * along the x axis.
         * @param velocityY The velocity of this fling measured in pixels per second
         * along the y axis.
         * @return true if the event is consumed, else false
         */
        boolean onFling(TouchEvent e1, TouchEvent e2, float velocityX, float velocityY);
    }

    /**
     * The listener that is used to notify when a double-tap or a confirmed
     * single-tap occur.
     *
     * @since 2021-04-29
     */
    public interface OnDoubleTapListener {
        /**
         * Notified when a single-tap occurs.
         * <p>
         * Unlike {@link OnGestureListener#onSingleTapUp(TouchEvent)}, this
         * will only be called after the detector is confident that the user's
         * first tap is not followed by a second tap leading to a double-tap
         * gesture.
         *
         * @param e The down motion event of the single-tap.
         * @return true if the event is consumed, else false
         */
        boolean onSingleTapConfirmed(TouchEvent e);

        /**
         * Notified when a double-tap occurs.
         *
         * @param e The down motion event of the first tap of the double-tap.
         * @return true if the event is consumed, else false
         */
        boolean onDoubleTap(TouchEvent e);

        /**
         * Notified when an event within a double-tap gesture occurs, including
         * the down, move, and up events.
         *
         * @param e The motion event that occurred during the double-tap gesture.
         * @return true if the event is consumed, else false
         */
        boolean onDoubleTapEvent(TouchEvent e);
    }

    /**
     * @since 2021-04-29
     */
    public interface OnContextClickListener {
        /**
         * Notified when a context click occurs.
         *
         * @param e The motion event that occurred during the context click.
         * @return true if the event is consumed, else false
         */
        boolean onContextClick(TouchEvent e);
    }

    /**
     * A convenience class to extend when you only want to listen for a subset
     * of all the gestures. This implements all methods in the
     * {@link OnGestureListener}, {@link OnDoubleTapListener}, and {@link OnContextClickListener}
     * but does nothing and return {@code false} for all applicable methods.
     *
     * @since 2021-04-29
     */
    public static class SimpleOnGestureListener implements OnGestureListener, OnDoubleTapListener,
        OnContextClickListener {
        /**
         * onSingleTapUp
         *
         * @param e The up motion event that completed the first tap
         * @return boolean
         */
        public boolean onSingleTapUp(TouchEvent e) {
            return false;
        }

        /**
         * onLongPress
         *
         * @param e The initial on down motion event that started the longpress.
         */
        public void onLongPress(TouchEvent e) {
        }

        /**
         * onScroll
         *
         * @param e1 The first down motion event that started the scrolling.
         * @param e2 The move motion event that triggered the current onScroll.
         * @param distanceX The distance along the X axis that has been scrolled since the last
         * call to onScroll. This is NOT the distance between {@code e1}
         * and {@code e2}.
         * @param distanceY The distance along the Y axis that has been scrolled since the last
         * call to onScroll. This is NOT the distance between {@code e1}
         * and {@code e2}.
         * @return boolean
         */
        public boolean onScroll(TouchEvent e1, TouchEvent e2, float distanceX, float distanceY) {
            return false;
        }

        /**
         * onFling
         *
         * @param e1 The first down motion event that started the fling.
         * @param e2 The move motion event that triggered the current onFling.
         * @param velocityX The velocity of this fling measured in pixels per second
         * along the x axis.
         * @param velocityY The velocity of this fling measured in pixels per second
         * along the y axis.
         * @return boolean
         */
        public boolean onFling(TouchEvent e1, TouchEvent e2, float velocityX, float velocityY) {
            return false;
        }

        /**
         * onShowPress
         *
         * @param e The down motion event
         */
        public void onShowPress(TouchEvent e) {
        }

        /**
         * onDown
         *
         * @param e The down motion event.
         * @return boolean
         */
        public boolean onDown(TouchEvent e) {
            return false;
        }

        /**
         * onDoubleTap
         *
         * @param e The down motion event of the first tap of the double-tap.
         * @return boolean
         */
        public boolean onDoubleTap(TouchEvent e) {
            return false;
        }

        /**
         * boolean
         *
         * @param e The motion event that occurred during the double-tap gesture.
         * @return boolean
         */
        public boolean onDoubleTapEvent(TouchEvent e) {
            return false;
        }

        /**
         * onSingleTapConfirmed
         *
         * @param e The down motion event of the single-tap.
         * @return boolean
         */
        public boolean onSingleTapConfirmed(TouchEvent e) {
            return false;
        }

        /**
         * onContextClick
         *
         * @param e The motion event that occurred during the context click.
         * @return boolean
         */
        public boolean onContextClick(TouchEvent e) {
            return false;
        }
    }

    private int mTouchSlopSquare;
    private int mDoubleTapTouchSlopSquare;
//    private int mDoubleTapSlopSquare;
    private int mMinimumFlingVelocity;
    private float mMaximumFlingVelocity;

    // 以下缺少对应api 设为默认值
    // private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int LONGPRESS_TIMEOUT = 500;
    private static final int TAP_TIMEOUT = 100;
    private static final int DOUBLE_TAP_TIMEOUT = 300;
    private static final int DOUBLE_TAP_MIN_TIME = 40;

    // constants for Message.what used by GestureHandler below
    private static final int SHOW_PRESS = 1;
    private static final int LONG_PRESS = 2;
    private static final int TAP = 3;

    private EventHandler mHandler;
    private OnGestureListener mListener;
    private OnDoubleTapListener mDoubleTapListener;
//    private OnContextClickListener mContextClickListener;

    private boolean mStillDown;
    private boolean mDeferConfirmSingleTap;
    private boolean mInLongPress;
    private boolean mInContextClick;
    private boolean mAlwaysInTapRegion;
//    private boolean mAlwaysInBiggerTapRegion;
    private boolean mIgnoreNextUpEvent;

    private TouchEvent mCurrentDownEvent;
    private TouchEvent mPreviousUpEvent;

    /**
     * True when the user is still touching for the second tap (down, move, and
     * up events). Can only be true if there is a double tap listener attached.
     */
    private boolean mIsDoubleTapping;

    private float mLastFocusX;
    private float mLastFocusY;
    private float mDownFocusX;
    private float mDownFocusY;

    private boolean mIsLongpressEnabled;

    /**
     * Determines speed during touch scrolling
     */
    private VelocityDetector mVelocityDetector;


    // private final InputEventConsistencyVerifier mInputEventConsistencyVerifier =
    // InputEventConsistencyVerifier.isInstrumentationEnabled()
    // new InputEventConsistencyVerifier(this, 0) : null;

    /**
     * Consistency verifier for debugging purposes.
     *
     * @since 2021-04-29
     */
    private class GestureHandler extends EventHandler {
        public GestureHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case SHOW_PRESS:
                    mListener.onShowPress(mCurrentDownEvent);
                    break;

                case LONG_PRESS:
                    dispatchLongPress();
                    break;

                case TAP:
                    // If the user's finger is still down, do not count it as a tap
                    if (mDoubleTapListener != null) {
                        if (!mStillDown) {
                            mDoubleTapListener.onSingleTapConfirmed(mCurrentDownEvent);
                        } else {
                            mDeferConfirmSingleTap = true;
                        }
                    }
                    break;

                default:
                    throw new RuntimeException("Unknown message " + event.eventId); //never
            }
        }
    }

    /**
     * Creates a GestureDetector with the supplied listener.
     * This variant of the constructor should be used from a non-UI thread
     * (as it allows specifying the Handler).
     *
     * @param listener the listener invoked for all the callbacks, this must
     * not be null.
     * @param handler the handler to use
     * @throws NullPointerException if either {@code listener} or
     * {@code handler} is null.
     */
    @Deprecated
    public GestureDetector(OnGestureListener listener, EventHandler handler) {
        this(null, listener, handler);
    }

    /**
     * Creates a GestureDetector with the supplied listener.
     * You may only use this constructor from a UI thread (this is the usual situation).
     *
     * @param listener the listener invoked for all the callbacks, this must
     * not be null.
     * @throws NullPointerException if {@code listener} is null.
     */
    @Deprecated
    public GestureDetector(OnGestureListener listener) {
        this(null, listener, null);
    }

    /**
     * Creates a GestureDetector with the supplied listener.
     *
     * @param context the application's context
     * @param listener the listener invoked for all the callbacks, this must
     * not be null.
     * @throws NullPointerException if {@code listener} is null.
     */
    public GestureDetector(Context context, OnGestureListener listener) {
        this(context, listener, null);
    }

    /**
     * Creates a GestureDetector with the supplied listener that runs deferred events on the
     *
     * @param context the application's context
     * @param listener the listener invoked for all the callbacks, this must
     * not be null.
     * @param handler the handler to use for running deferred listener events.
     * @throws NullPointerException if {@code listener} is null.
     */
    public GestureDetector(Context context, OnGestureListener listener, EventHandler handler) {
//        if (handler != null) { // 源项目，此处处理逻辑不同
        EventRunner eventRunner = EventRunner.current();
        if (eventRunner == null) {
            return;
        }
        mHandler = new GestureHandler(eventRunner);
//        }
        mListener = listener;
        if (listener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) listener);
        }
        if (listener instanceof OnContextClickListener) {
            setContextClickListener((OnContextClickListener) listener);
        }
        init(context);
    }

    /**
     * Creates a GestureDetector with the supplied listener that runs deferred events on the
     *
     * @param context the application's context
     * @param listener the listener invoked for all the callbacks, this must
     * not be null.
     * @param handler the handler to use for running deferred listener events.
     * @param unused currently not used.
     * @throws NullPointerException if {@code listener} is null.
     */
    public GestureDetector(Context context, OnGestureListener listener, EventHandler handler,
                           boolean unused) {
        this(context, listener, handler);
    }

    private void init(Context context) {
        if (mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        mIsLongpressEnabled = true;

        // 以下缺少对应api
      /*  // Fallback to support pre-donuts releases
        int touchSlop, doubleTapSlop, doubleTapTouchSlop;
        if (context == null) {
            //noinspection deprecation
            touchSlop = ViewConfiguration.getTouchSlop();
            doubleTapTouchSlop = touchSlop; // Hack rather than adding a hiden method for this
            doubleTapSlop = ViewConfiguration.getDoubleTapSlop();
            //noinspection deprecation
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        } else {
            final ViewConfiguration configuration = ViewConfiguration.get(context);
            touchSlop = configuration.getScaledTouchSlop();
            doubleTapTouchSlop = configuration.getScaledDoubleTapTouchSlop();
            doubleTapSlop = configuration.getScaledDoubleTapSlop();
            mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        }
        mTouchSlopSquare = touchSlop * touchSlop;
        mDoubleTapTouchSlopSquare = doubleTapTouchSlop * doubleTapTouchSlop;
        mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;*/
    }

    /**
     * Sets the listener which will be called for double-tap and related
     * gestures.
     *
     * @param onDoubleTapListener the listener invoked for all the callbacks, or
     * null to stop listening for double-tap gestures.
     */
    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        mDoubleTapListener = onDoubleTapListener;
    }

    /**
     * Sets the listener which will be called for context clicks.
     *
     * @param onContextClickListener the listener invoked for all the callbacks, or null to stop
     * listening for context clicks.
     */
    public void setContextClickListener(OnContextClickListener onContextClickListener) {
//        mContextClickListener = onContextClickListener;
    }

    /**
     * Set whether longpress is enabled, if this is enabled when a user
     * presses and holds down you get a longpress event and nothing further.
     * If it's disabled the user can press and hold down and then later
     * moved their finger and you will get scroll events. By default
     * longpress is enabled.
     *
     * @param isLongpressEnabled whether longpress should be enabled.
     */
    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        mIsLongpressEnabled = isLongpressEnabled;
    }

    /**
     * isLongpressEnabled
     *
     * @return true if longpress is enabled, else false.
     */
    public boolean isLongpressEnabled() {
        return mIsLongpressEnabled;
    }

    /**
     * Analyzes the given motion event and if applicable triggers the
     * appropriate callbacks on the {@link OnGestureListener} supplied.
     *
     * @param ev The current motion event.
     * @return true if the {@link OnGestureListener} consumed the event,
     * else false.
     */
    public boolean onTouchEvent(TouchEvent ev) {
       /* if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(ev, 0);
        }
*/
        final int action = ev.getAction();

        if (mVelocityDetector == null) {
            mVelocityDetector = VelocityDetector.obtainInstance();
        }else {
            mVelocityDetector.addEvent(ev);
        }

        final boolean pointerUp =
            // (action & TouchEvent.ACTION_MASK) == TouchEvent.OTHER_POINT_UP;
            action == TouchEvent.OTHER_POINT_UP;
        final int skipIndex = pointerUp ? ev.getIndex() : -1;
        //final boolean isGeneratedGesture =
        // (ev. () & TouchEvent.FLAG_IS_GENERATED_GESTURE) !=0;

        // Determine focal point
        float sumX = 0, sumY = 0;
        final int count = ev.getPointerCount();
        MmiPoint mmiPoint;
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;
            mmiPoint = ev.getPointerPosition(i);
            sumX += mmiPoint.getX();
            sumY += mmiPoint.getY();
        }
        final int div = pointerUp ? count - 1 : count;
        final float focusX = sumX / div;
        final float focusY = sumY / div;

        boolean handled = false;

        // switch (action & TouchEvent.ACTION_MASK) {
        switch (action) {
            case TouchEvent.OTHER_POINT_DOWN:
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                // Cancel long press and taps
                cancelTaps();
                break;

            case TouchEvent.OTHER_POINT_UP:
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;

                // Check the dot product of current velocities.
                // If the pointer that left was opposing another velocity vector, clear.
                mVelocityDetector.calculateCurrentVelocity(1000, mMaximumFlingVelocity, mMaximumFlingVelocity);
                final int upIndex = ev.getIndex();
//                final int id1 = ev.getPointerId(upIndex);
                final float x1 = mVelocityDetector.getHorizontalVelocity();
                final float y1 = mVelocityDetector.getVerticalVelocity();
                for (int i = 0; i < count; i++) {
                    if (i == upIndex) continue;

//                    final int id2 = ev.getPointerId(i);
                    final float x = x1 * mVelocityDetector.getHorizontalVelocity();
                    final float y = y1 * mVelocityDetector.getVerticalVelocity();

                    final float dot = x + y;
                    if (dot < 0) {
                        mVelocityDetector.clear();
                        break;
                    }
                }
                break;

            case TouchEvent.PRIMARY_POINT_DOWN:
                if (mDoubleTapListener != null) {
                    boolean hadTapMessage = mHandler.hasInnerEvent(TAP);
                    if (hadTapMessage) mHandler.removeEvent(TAP);
                    if ((mCurrentDownEvent != null) && (mPreviousUpEvent != null) && hadTapMessage &&
                        isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, ev)) {
                        // This is a second tap
                        mIsDoubleTapping = true;
                        // Give a callback with the first tap of the double-tap
                        handled |= mDoubleTapListener.onDoubleTap(mCurrentDownEvent);
                        // Give a callback with down event of the double-tap
                        handled |= mDoubleTapListener.onDoubleTapEvent(ev);
                    } else {
                        // This is a first tap
                        mHandler.sendEvent(TAP, DOUBLE_TAP_TIMEOUT);
                    }
                }

                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                if (mCurrentDownEvent != null) {
                    // mCurrentDownEvent.recycle();
                }
                // mCurrentDownEvent = TouchEvent.obtain(ev); // 待处理，缺少对应api
                mCurrentDownEvent = ev;
                mAlwaysInTapRegion = true;
//                mAlwaysInBiggerTapRegion = true;
                mStillDown = true;
                mInLongPress = false;
                mDeferConfirmSingleTap = false;

                if (mIsLongpressEnabled) {
                    mHandler.removeEvent(LONG_PRESS);
                    mHandler.sendEvent(LONG_PRESS,
                        mCurrentDownEvent.getStartTime() + LONGPRESS_TIMEOUT);
                }
                mHandler.sendTimingEvent(SHOW_PRESS,
                    mCurrentDownEvent.getStartTime() + TAP_TIMEOUT);
                handled |= mListener.onDown(ev);
                break;

            case TouchEvent.POINT_MOVE:
                if (mInLongPress || mInContextClick) {
                    break;
                }
                final float scrollX = mLastFocusX - focusX;
                final float scrollY = mLastFocusY - focusY;
                if (mIsDoubleTapping) {
                    // Give the move events of the double-tap
                    handled |= mDoubleTapListener.onDoubleTapEvent(ev);
                } else if (mAlwaysInTapRegion) {
                    final int deltaX = (int) (focusX - mDownFocusX);
                    final int deltaY = (int) (focusY - mDownFocusY);
                    int distance = (deltaX * deltaX) + (deltaY * deltaY);
//                    int slopSquare = isGeneratedGesture ? 0 : mTouchSlopSquare;
                    int slopSquare = mTouchSlopSquare;
                    if (distance > slopSquare) {
                        handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                        mLastFocusX = focusX;
                        mLastFocusY = focusY;
                        mAlwaysInTapRegion = false;
                        mHandler.removeEvent(TAP);
                        mHandler.removeEvent(SHOW_PRESS);
                        mHandler.removeEvent(LONG_PRESS);
                    }
//                    int doubleTapSlopSquare = isGeneratedGesture ? 0 : mDoubleTapTouchSlopSquare;
                    int doubleTapSlopSquare = mDoubleTapTouchSlopSquare;
                    if (distance > doubleTapSlopSquare) {
//                        mAlwaysInBiggerTapRegion = false;
                    }
                } else if ((Math.abs(scrollX) >= 1) || (Math.abs(scrollY) >= 1)) {
                    handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                    mLastFocusX = focusX;
                    mLastFocusY = focusY;
                }
                break;

            case TouchEvent.PRIMARY_POINT_UP:
                mStillDown = false;
//                TouchEvent currentUpEvent = TouchEvent.obtain(ev); 缺少api
                if (mIsDoubleTapping) {
                    // Finally, give the up event of the double-tap
                    handled |= mDoubleTapListener.onDoubleTapEvent(ev);
                } else if (mInLongPress) {
                    mHandler.removeEvent(TAP);
                    mInLongPress = false;
                } else if (mAlwaysInTapRegion && !mIgnoreNextUpEvent) {
                    handled = mListener.onSingleTapUp(ev);
                    if (mDeferConfirmSingleTap && mDoubleTapListener != null) {
                        mDoubleTapListener.onSingleTapConfirmed(ev);
                    }
                } else if (!mIgnoreNextUpEvent) {

                    // A fling must travel the minimum tap distance
                    final VelocityDetector velocityDetector = mVelocityDetector;
//                    final int pointerId = ev.getPointerId(0);
                    velocityDetector.calculateCurrentVelocity(1000, mMaximumFlingVelocity, mMaximumFlingVelocity);
                    final float velocityY = velocityDetector.getHorizontalVelocity();
                    final float velocityX = velocityDetector.getVerticalVelocity();

                    if ((Math.abs(velocityY) > mMinimumFlingVelocity)
                        || (Math.abs(velocityX) > mMinimumFlingVelocity)) {
                        handled = mListener.onFling(mCurrentDownEvent, ev, velocityX, velocityY);
                    }
                }
                if (mPreviousUpEvent != null) {
//                    mPreviousUpEvent.recycle(); 无对应api
                }
                // Hold the event we obtained above - listeners may have changed the original.
//                mPreviousUpEvent = currentUpEvent;
                if (mVelocityDetector != null) {
                    // This may have been cleared when we called out to the
                    // application above.
//                    mVelocityDetector.recycle(); 无对应api
                    mVelocityDetector = null;
                }
                mIsDoubleTapping = false;
                mDeferConfirmSingleTap = false;
                mIgnoreNextUpEvent = false;
                mHandler.removeEvent(SHOW_PRESS);
                mHandler.removeEvent(LONG_PRESS);
                break;

            case TouchEvent.CANCEL:
                cancel();
                break;
        }

        //  无对应类或方案
    /*    if (!handled && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 0);
        }*/
        return handled;
    }

    private void cancel() {
        mHandler.removeEvent(SHOW_PRESS);
        mHandler.removeEvent(LONG_PRESS);
        mHandler.removeEvent(TAP);
        mVelocityDetector.clear();
        mVelocityDetector = null;
        mIsDoubleTapping = false;
        mStillDown = false;
        mAlwaysInTapRegion = false;
//        mAlwaysInBiggerTapRegion = false;
        mDeferConfirmSingleTap = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }

    private void cancelTaps() {
        mHandler.removeEvent(SHOW_PRESS);
        mHandler.removeEvent(LONG_PRESS);
        mHandler.removeEvent(TAP);
        mIsDoubleTapping = false;
        mAlwaysInTapRegion = false;
//        mAlwaysInBiggerTapRegion = false;
        mDeferConfirmSingleTap = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }

    // 未使用暂不移植
    private boolean isConsideredDoubleTap(TouchEvent firstDown, TouchEvent firstUp,
                                          TouchEvent secondDown) {
      /*  if (!mAlwaysInBiggerTapRegion) {
            return false;
        }

        final long deltaTime = secondDown.getOccurredTime() - firstUp.getOccurredTime();
        if (deltaTime > DOUBLE_TAP_TIMEOUT || deltaTime < DOUBLE_TAP_MIN_TIME) {
            return false;
        }
        int deltaX = (int) firstDown.getX() - (int) secondDown.getX();
        int deltaY = (int) firstDown.getY() - (int) secondDown.getY();
        final boolean isGeneratedGesture =
                (firstDown.getFlags() & TouchEvent.FLAG_IS_GENERATED_GESTURE) != 0;
        int slopSquare = isGeneratedGesture ? 0 : mDoubleTapSlopSquare;
        return (deltaX * deltaX + deltaY * deltaY < slopSquare);*/

        return false;
    }

    private void dispatchLongPress() {
        mHandler.removeEvent(TAP);
        mDeferConfirmSingleTap = false;
        mInLongPress = true;
        mListener.onLongPress(mCurrentDownEvent);
    }
}
