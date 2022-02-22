package com.example.doodle;

import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.StylusEvent;
import ohos.multimodalinput.event.TouchEvent;

/**
 * ScaleGestureDetectorApi27
 *
 * @since 2021-04-29
 */
public class ScaleGestureDetectorApi27 {
    /**
     * OnScaleGestureListener
     *
     * @since 2021-04-29
     */
    public interface OnScaleGestureListener {
        /**
         * onScale
         *
         * @param detector
         * @return boolean
         */
        boolean onScale(ScaleGestureDetectorApi27 detector);

        /**
         * onScaleBegin
         *
         * @param detector
         * @return boolean
         */
        boolean onScaleBegin(ScaleGestureDetectorApi27 detector);

        /**
         * onScaleEnd
         *
         * @param detector
         */
        void onScaleEnd(ScaleGestureDetectorApi27 detector);
    }

    private final Context mContext;
    private final OnScaleGestureListener mListener;

    private float mFocusX;
    private float mFocusY;

    private boolean mQuickScaleEnabled;
    private boolean mStylusScaleEnabled;

    private float mCurrSpan;
    private float mPrevSpan;
    private float mInitialSpan;
    private float mCurrSpanX;
    private float mCurrSpanY;
    private float mPrevSpanX;
    private float mPrevSpanY;
    private long mCurrTime;
    private long mPrevTime;
    private boolean mInProgress;
    private int mSpanSlop;
    private int mMinSpan;

    private final EventHandler mHandler;

    private float mAnchoredScaleStartX;
    private float mAnchoredScaleStartY;
    private int mAnchoredScaleMode = ANCHORED_SCALE_MODE_NONE;

    private static final long TOUCH_STABILIZE_TIME = 128; // ms
    private static final float SCALE_FACTOR = .5f;
    private static final int ANCHORED_SCALE_MODE_NONE = 0;
    private static final int ANCHORED_SCALE_MODE_DOUBLE_TAP = 1;
    private static final int ANCHORED_SCALE_MODE_STYLUS = 2;

    private GestureDetector mGestureDetector;

    private boolean mEventBeforeOrAboveStartingGestureEvent;

    // 系统API默认值
    private static final int TOUCH_SLOP = 8;

    /**
     * ScaleGestureDetectorApi27
     *
     * @param context
     * @param listener
     */
    public ScaleGestureDetectorApi27(Context context, OnScaleGestureListener listener) {
        this(context, listener, null);
    }

    /**
     * ScaleGestureDetectorApi27
     *
     * @param context
     * @param listener
     * @param handler
     */
    public ScaleGestureDetectorApi27(Context context, OnScaleGestureListener listener,
                                     EventHandler handler) {
        mContext = context;
        mListener = listener;
//        mSpanSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 2;
        mSpanSlop = TOUCH_SLOP * 2;

//        final ResourceManager res = context.getResourceManager();
        mMinSpan = 468;
        mHandler = handler;
        // 启用快速缩放和触控笔缩放
        // Quick scale is enabled by default after JB_MR2
       /* final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        if (targetSdkVersion > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setQuickScaleEnabled(true);
        }
        // Stylus scale is enabled by default after LOLLIPOP_MR1
        if (targetSdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            setStylusScaleEnabled(true);
        }*/
    }

    /**
     * onTouchEvent
     *
     * @param touchEvent
     * @return boolean
     */
    public boolean onTouchEvent(TouchEvent touchEvent) {
        mCurrTime = touchEvent.getOccurredTime();

        final int action = touchEvent.getAction();

        // Forward the event to check for double tap gesture
        if (mQuickScaleEnabled) {
            mGestureDetector.onTouchEvent(touchEvent);
        }

        final int count = touchEvent.getPointerCount();

        // final boolean isStylusButtonDown =
        // (touchEvent.getButtonState() & TouchEvent.BUTTON_STYLUS_PRIMARY) != 0;
        final boolean isStylusButtonDown =
            (touchEvent.getAction() & StylusEvent.BUTTON_PRESS) != 0;


        final boolean anchoredScaleCancelled =
            mAnchoredScaleMode == ANCHORED_SCALE_MODE_STYLUS && !isStylusButtonDown;
        final boolean streamComplete = action == TouchEvent.PRIMARY_POINT_UP ||
            action == TouchEvent.CANCEL || anchoredScaleCancelled;

        if (action == TouchEvent.PRIMARY_POINT_DOWN || streamComplete) {
            // Reset any scale in progress with the listener.
            // If it's an ACTION_DOWN we're beginning a new event stream.
            // This means the app probably didn't give us all the events. Shame on it.
            if (mInProgress) {
                mListener.onScaleEnd(this);
                mInProgress = false;
                mInitialSpan = 0;
                mAnchoredScaleMode = ANCHORED_SCALE_MODE_NONE;
            } else if (inAnchoredScaleMode() && streamComplete) {
                mInProgress = false;
                mInitialSpan = 0;
                mAnchoredScaleMode = ANCHORED_SCALE_MODE_NONE;
            }

            if (streamComplete) {
                return true;
            }
        }

        if (!mInProgress && mStylusScaleEnabled && !inAnchoredScaleMode()
            && !streamComplete && isStylusButtonDown) {
            // Start of a button scale gesture
            MmiPoint mp = touchEvent.getPointerPosition(0);
            mAnchoredScaleStartX = mp.getX();
            mAnchoredScaleStartY = mp.getY();
            mAnchoredScaleMode = ANCHORED_SCALE_MODE_STYLUS;
            mInitialSpan = 0;
        }

        final boolean configChanged = action == TouchEvent.PRIMARY_POINT_DOWN ||
            action == TouchEvent.OTHER_POINT_UP ||
            action == TouchEvent.OTHER_POINT_DOWN || anchoredScaleCancelled;

        final boolean pointerUp = action == TouchEvent.OTHER_POINT_UP;
        final int skipIndex = pointerUp ? touchEvent.getIndex() : -1;

        // Determine focal point
        float sumX = 0, sumY = 0;
        final int div = pointerUp ? count - 1 : count;
        final float focusX;
        final float focusY;
        if (inAnchoredScaleMode()) {
            // In anchored scale mode, the focal pt is always where the double tap
            // or button down gesture started
            focusX = mAnchoredScaleStartX;
            focusY = mAnchoredScaleStartY;
            if (touchEvent.getPointerPosition(0).getY() < focusY) {
                mEventBeforeOrAboveStartingGestureEvent = true;
            } else {
                mEventBeforeOrAboveStartingGestureEvent = false;
            }
        } else {
            MmiPoint mmiPoint;
            for (int i = 0; i < count; i++) {
                if (skipIndex == i) continue;
//                返回指针索引的x和y坐标。 如果已指定控件的位置，则返回相对于控件的x和y坐标。 如果尚未指定控件的位置，则返回相对于屏幕的x和y坐标。
                mmiPoint = touchEvent.getPointerPosition(i);   // 相对于原点坐标getPointerScreenPosition
                sumX += mmiPoint.getX();
                sumY += mmiPoint.getY();
            }

            focusX = sumX / div;
            focusY = sumY / div;
        }

        // Determine average deviation from focal point
        float devSumX = 0, devSumY = 0;
        MmiPoint mmiPoint;
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;

            // Convert the resulting diameter into a radius.
            mmiPoint = touchEvent.getPointerPosition(i);
            devSumX += Math.abs(mmiPoint.getX() - focusX);
            devSumY += Math.abs(mmiPoint.getY() - focusY);
        }
        final float devX = devSumX / div;
        final float devY = devSumY / div;

        // Span is the average distance between touch points through the focal point;
        // i.e. the diameter of the circle with a radius of the average deviation from
        // the focal point.
        final float spanX = devX * 2;
        final float spanY = devY * 2;
        final float span;
        if (inAnchoredScaleMode()) {
            span = spanY;
        } else {
            span = (float) Math.hypot(spanX, spanY);
        }

        // Dispatch begin/end events as needed.
        // If the configuration changes, notify the app to reset its current state by beginning
        // a fresh scale event stream.
        final boolean wasInProgress = mInProgress;
        mFocusX = focusX;
        mFocusY = focusY;
        if (!inAnchoredScaleMode() && mInProgress && (span < mMinSpan || configChanged)) {
            mListener.onScaleEnd(this);
            mInProgress = false;
            mInitialSpan = span;
        }
        if (configChanged) {
            mPrevSpanX = mCurrSpanX = spanX;
            mPrevSpanY = mCurrSpanY = spanY;
            mInitialSpan = mPrevSpan = mCurrSpan = span;
        }

        final int minSpan = inAnchoredScaleMode() ? mSpanSlop : mMinSpan;
        if (!mInProgress && span >= minSpan &&
            (wasInProgress || Math.abs(span - mInitialSpan) > mSpanSlop)) {
            mPrevSpanX = mCurrSpanX = spanX;
            mPrevSpanY = mCurrSpanY = spanY;
            mPrevSpan = mCurrSpan = span;
            mPrevTime = mCurrTime;
            mInProgress = mListener.onScaleBegin(this);
        }

        // Handle motion; focal point and span/scale factor are changing.
        if (action == TouchEvent.POINT_MOVE) {
            mCurrSpanX = spanX;
            mCurrSpanY = spanY;
            mCurrSpan = span;

            boolean updatePrev = true;

            if (mInProgress) {
                updatePrev = mListener.onScale(this);
            }

            if (updatePrev) {
                mPrevSpanX = mCurrSpanX;
                mPrevSpanY = mCurrSpanY;
                mPrevSpan = mCurrSpan;
                mPrevTime = mCurrTime;
            }
        }

        return true;
    }

    /**
     * inAnchoredScaleMode
     *
     * @return boolean
     */
    private boolean inAnchoredScaleMode() {
        return mAnchoredScaleMode != ANCHORED_SCALE_MODE_NONE;
    }

    /**
     * setQuickScaleEnabled
     *
     * @param scales
     */
    public void setQuickScaleEnabled(boolean scales) {
        mQuickScaleEnabled = scales;
        if (mQuickScaleEnabled && mGestureDetector == null) {
            GestureDetector.SimpleOnGestureListener gestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    /**
                     * onDoubleTap
                     *
                     * @param e
                     * @return 是否处理事件
                     */
                    @Override
                    public boolean onDoubleTap(TouchEvent e) {
                        // Double tap: start watching for a swipe
                        mAnchoredScaleStartX = e.getPointerPosition(0).getX();
                        mAnchoredScaleStartY = e.getPointerPosition(0).getY();
                        mAnchoredScaleMode = ANCHORED_SCALE_MODE_DOUBLE_TAP;
                        return true;
                    }
                };
            mGestureDetector = new GestureDetector(mContext, gestureListener, mHandler);
        }
    }

    /**
     * Return whether the quick scale gesture, in which the user performs a double tap followed by a
     * swipe, should perform scaling. {@see #setQuickScaleEnabled(boolean)}.
     *
     * @return boolean
     */
    public boolean isQuickScaleEnabled() {
        return mQuickScaleEnabled;
    }

    public void setStylusScaleEnabled(boolean scales) {
        mStylusScaleEnabled = scales;
    }

    /**
     * isStylusScaleEnabled
     *
     * @return boolean
     */
    public boolean isStylusScaleEnabled() {
        return mStylusScaleEnabled;
    }

    public boolean isInProgress() {
        return mInProgress;
    }

    public float getFocusX() {
        return mFocusX;
    }

    public float getFocusY() {
        return mFocusY;
    }

    public float getCurrentSpan() {
        return mCurrSpan;
    }

    public float getCurrentSpanX() {
        return mCurrSpanX;
    }

    public float getCurrentSpanY() {
        return mCurrSpanY;
    }

    /**
     * Return the previous average distance between each of the pointers forming the
     * gesture in progress through the focal point.
     *
     * @return Previous distance between pointers in pixels.
     */
    public float getPreviousSpan() {
        return mPrevSpan;
    }

    /**
     * Return the previous average X distance between each of the pointers forming the
     * gesture in progress through the focal point.
     *
     * @return Previous distance between pointers in pixels.
     */
    public float getPreviousSpanX() {
        return mPrevSpanX;
    }

    /**
     * Return the previous average Y distance between each of the pointers forming the
     * gesture in progress through the focal point.
     *
     * @return Previous distance between pointers in pixels.
     */
    public float getPreviousSpanY() {
        return mPrevSpanY;
    }

    /**
     * Return the scaling factor from the previous scale event to the current
     * event. This value is defined as
     * ({@link #getCurrentSpan()} / {@link #getPreviousSpan()}).
     *
     * @return The current scaling factor.
     */
    public float getScaleFactor() {
        if (inAnchoredScaleMode()) {
            // Drag is moving up; the further away from the gesture
            // start, the smaller the span should be, the closer,
            // the larger the span, and therefore the larger the scale
            final boolean scaleUp =
                (mEventBeforeOrAboveStartingGestureEvent && (mCurrSpan < mPrevSpan)) ||
                    (!mEventBeforeOrAboveStartingGestureEvent && (mCurrSpan > mPrevSpan));
            final float spanDiff = (Math.abs(1 - (mCurrSpan / mPrevSpan)) * SCALE_FACTOR);
            return mPrevSpan <= 0 ? 1 : scaleUp ? (1 + spanDiff) : (1 - spanDiff);
        }
        return mPrevSpan > 0 ? mCurrSpan / mPrevSpan : 1;
    }

    /**
     * Return the time difference in milliseconds between the previous
     * accepted scaling event and the current scaling event.
     *
     * @return Time difference since the last scaling event in milliseconds.
     */
    public long getTimeDelta() {
        return mCurrTime - mPrevTime;
    }

    /**
     * Return the event time of the current event being processed.
     *
     * @return Current event time in milliseconds.
     */
    public long getEventTime() {
        return mCurrTime;
    }

    public void setMinSpan(int minSpan) {
        mMinSpan = minSpan;
    }

    public void setSpanSlop(int spanSlop) {
        mSpanSlop = spanSlop;
    }

    public int getMinSpan() {
        return mMinSpan;
    }

    public int getSpanSlop() {
        return mSpanSlop;
    }
}
