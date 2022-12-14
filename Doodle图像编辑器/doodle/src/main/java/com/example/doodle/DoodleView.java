/*
  MIT License

  Copyright (c) 2018 huangziwei

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 *
 */

package com.example.doodle;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.StackLayout;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.multimodalinput.event.TouchEvent;

import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleColor;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodlePen;
import com.example.doodle.core.IDoodleShape;
import com.example.doodle.core.IDoodleTouchDetector;
import com.example.doodle.core.ISetingshoworhide;
import com.example.doodle.util.DrawUtil;
import com.example.doodle.util.ImageUtils;
import com.example.doodle.util.LogUtil;
import com.example.doodle.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ????????????
 *
 * @since 2021-04-29
 */
public class DoodleView extends StackLayout implements IDoodle {

    /**
     * TAG
     */
    public static final String TAG = "DoodleView";
    /**
     * ??????????????????
     */
    public final static float MAX_SCALE = 5f;
    /**
     * ??????????????????
     */
    public final static float MIN_SCALE = 0.25f;
    /**
     * ??????????????????
     */
    public final static int DEFAULT_SIZE = 6;
    /**
     * ERROR_INIT
     */
    public static final int ERROR_INIT = -1;
    /**
     * ERROR_SAVE
     */
    public static final int ERROR_SAVE = -2;

    private static final int FLAG_RESET_BACKGROUND = 1 << 1;
    private static final int FLAG_DRAW_PENDINGS_TO_BACKGROUND = 1 << 2;
    private static final int FLAG_REFRESH_BACKGROUND = 1 << 3;

    private IDoodleListener mDoodleListener;

    private final PixelMap mPixelMap; // ?????????????????????

    private float mCenterScale; // ????????????????????????????????????
    private int mCenterHeight, mCenterWidth;// ?????????????????????????????????View??????????????????????????????
    private float mCentreTranX, mCentreTranY;// ?????????????????????????????????????????????????????????View??????????????????????????????

    private float mRotateScale = 1;  // ??????????????????????????????????????????
    private float mRotateTranX, mRotateTranY; // ???????????????????????????????????????

    private float mScale = 1; // ??????????????????????????????????????????????????? ??? ?????????????????????????????? mCenterScale*mScale ???
    private float mTransX = 0, mTransY = 0; // ????????????????????????????????????????????????????????????????????? ????????????????????????mCentreTranX + mTransX???View??????????????????????????????
    private float mMinScale = MIN_SCALE; // ??????????????????
    private float mMaxScale = MAX_SCALE; // ??????????????????

    private float mSize;
    private IDoodleColor mColor; // ????????????

    private boolean isJustDrawOriginal; // ?????????????????????

    private boolean mIsDrawableOutside = false; // ???????????????????????????????????????????????????
    private boolean mReady = false;

    // ?????????????????????????????????
    private List<IDoodleItem> mItemStack = new ArrayList<>();
    private List<IDoodleItem> mRedoItemStack = new ArrayList<>();

    private IDoodlePen mPen;
    private IDoodleShape mShape;

    private float mTouchX, mTouchY;
    private boolean mEnableZoomer = false; // ???????????????
    private boolean mEnableOverview = true; // ???????????????????????????????????????????????????????????????
    private float mLastZoomerY;
    private float mZoomerRadius;
    private Path mZoomerPath;
    private float mZoomerScale = 0; // ??????????????????
    private Paint mZooomerPaint, mZoomerTouchPaint;
    private int mZoomerHorizonX; // ?????????????????????x???????????????????????????
    private boolean mIsScrollingDoodle = false; // ??????????????????????????????????????????????????????????????????

    private float mDoodleSizeUnit = 1; // ????????????????????????????????????????????????????????????????????????????????????dp??????????????????????????????????????????????????????
    private int mDoodleRotateDegree = 0; // ????????????????????????????????????

    // ????????????
    private IDoodleTouchDetector mDefaultTouchDetector;
    private Map<IDoodlePen, IDoodleTouchDetector> mTouchDetectorMap = new HashMap<>();

    private ForegroundView mForegroundView;
    private RectFloat mDoodleBound = new RectFloat();
    private Point mTempPoint = new Point();

    private boolean mIsEditMode = false; //?????????????????????????????????????????????
    private boolean mIsSaving = false;
    private static final int SAVA_IMAGE = 110;
    /**
     * Whether or not to optimize drawing, it is suggested to open, which can optimize the drawing speed and performance.
     * Note: When item is selected for editing after opening, it will be drawn at the top level, and not at the corresponding level until editing is completed.
     * ??????????????????????????????????????????????????????????????????.
     * ??????????????????item????????????????????????????????????????????????????????????????????????????????????????????????
     **/
    private final boolean mOptimizeDrawing; // ?????????????????????????????????????????????
    private List<IDoodleItem> mItemStackOnViewCanvas = new ArrayList<>(); // ??????item?????????View?????????????????????????????????PixelMap.??????????????????????????????item
    private List<IDoodleItem> mPendingItemsDrawToPixelMap = new ArrayList<>();
    private PixelMap mDoodlePixelMap;
    private int mFlags = 0;
    private Canvas mDoodlePixelMapCanvas;
    private BackgroundView mBackgroundView;
    private ISetingshoworhide mISetingshoworhide;

    /**
     * ?????????
     *
     * @param context
     * @param pixelMap
     * @param listener
     */
    public DoodleView(Context context, PixelMap pixelMap, IDoodleListener listener) {
        this(context, pixelMap, false, listener, null);
    }

    /**
     * ?????????
     *
     * @param context
     * @param pixelMap
     * @param listener
     * @param defaultDetector
     */
    public DoodleView(Context context, PixelMap pixelMap, IDoodleListener listener, IDoodleTouchDetector defaultDetector) {
        this(context, pixelMap, false, listener, defaultDetector);
    }

    /**
     * ?????????
     *
     * @param context
     * @param pixelMap
     * @param optimizeDrawing
     * @param listener
     */
    public DoodleView(Context context, PixelMap pixelMap, boolean optimizeDrawing, IDoodleListener listener) {
        this(context, pixelMap, optimizeDrawing, listener, null);
    }

    /**
     * ????????????
     *
     * @param context
     * @param pixelMap
     * @param optimizeDrawing ???????????????????????????????????????????????????????????????????????????????????????????????????.
     * ??????????????????????????????????????????????????????item??????????????? {@link #markItemToOptimizeDrawing(IDoodleItem)}??????????????????{@link #addItem(IDoodleItem)}.
     * ???????????????????????????????????? {@link #notifyItemFinishedDrawing(IDoodleItem)}???
     * {@link #mOptimizeDrawing}
     * @param listener
     * @param defaultDetector ?????????????????????
     */
    public DoodleView(Context context, PixelMap pixelMap, boolean optimizeDrawing, IDoodleListener listener, IDoodleTouchDetector defaultDetector) {
        super(context);
        setClipEnabled(false);

        mPixelMap = pixelMap;
        if (mPixelMap != null) {
            if (mPixelMap.getImageInfo().pixelFormat != PixelFormat.RGB_565) {
                // ??????????????????????????????????????????????????????????????????????????????????????????
                LogUtil.w(TAG, "the PixelMap may contain alpha, which will cause eraser don't work well.");
            }
        }
        mDoodleListener = listener;
        if (mDoodleListener == null) {
            throw new RuntimeException("IDoodleListener is null!!!");
        }
        if (mPixelMap == null) {
            throw new RuntimeException("PixelMap is null!!!");
        }

        mOptimizeDrawing = optimizeDrawing;

        mScale = 1f;
        mColor = new DoodleColor(Color.RED.getValue());

        mPen = DoodlePen.BRUSH;
        mShape = DoodleShape.HAND_WRITE;

        mZooomerPaint = new Paint();
        mZooomerPaint.setColor(new Color(0xaaffffff));
        mZooomerPaint.setStyle(Paint.Style.STROKE_STYLE);
        mZooomerPaint.setAntiAlias(true);
        mZooomerPaint.setStrokeJoin(Paint.Join.ROUND_JOIN);
        mZooomerPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);// ??????
        mZooomerPaint.setStrokeWidth(Util.vp2px(getContext(), 10));

        mZoomerTouchPaint = new Paint();
        mZoomerTouchPaint.setStyle(Paint.Style.STROKE_STYLE);
        mZoomerTouchPaint.setAntiAlias(true);
        mZoomerTouchPaint.setStrokeJoin(Paint.Join.ROUND_JOIN);
        mZoomerTouchPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);// ??????

        mDefaultTouchDetector = defaultDetector;

        mForegroundView = new ForegroundView(context);
        mBackgroundView = new BackgroundView(context);
        addComponent(mBackgroundView, new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
        addComponent(mForegroundView, new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
        setEstimateSizeListener(mEstimateSizeListener);
        addDrawTask(mDrawTask);
        setTouchEventListener(mTouchEventListener);
    }

    private EstimateSizeListener mEstimateSizeListener = new EstimateSizeListener() {
        @Override
        public boolean onEstimateSize(int i, int i1) {
            int width = EstimateSpec.getSize(i);
            int height = EstimateSpec.getSize(i1);
            init(width, height);
            if (!mReady) {
                mDoodleListener.onReady(DoodleView.this, width, height);
                mReady = true;
            }
            return false;
        }
    };
    private DrawTask mDrawTask = new DrawTask() {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            dispatchDraw(canvas);
        }
    };

    private Matrix mTouchEventMatrix = new Matrix();
    private TouchEventListener mOnTouchListener;

    private TouchEventListener mTouchEventListener = new TouchEventListener() {
        @Override
        public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
            if (mOnTouchListener != null) {
                if (mOnTouchListener.onTouchEvent(DoodleView.this, touchEvent)) {
                    return true;
                }
            }
            if (mISetingshoworhide != null) {
                switch (touchEvent.getAction()) {
                    case TouchEvent.PRIMARY_POINT_DOWN:
//                        mSettingsPanel.removeCallbacks(mHideDelayRunnable);
//                        mSettingsPanel.removeCallbacks(mShowDelayRunnable);
                        //??????????????????????????????????????????????????????????????????
//                        mSettingsPanel.postDelayed(mHideDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        mISetingshoworhide.hide();
//                    postTask(mHideDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        break;
                    case TouchEvent.CANCEL:
                    case TouchEvent.PRIMARY_POINT_UP:
//                        mSettingsPanel.removeCallbacks(mHideDelayRunnable);
//                        mSettingsPanel.removeCallbacks(mShowDelayRunnable);
                        //??????????????????????????????????????????????????????????????????
//                        mSettingsPanel.postDelayed(mShowDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        mISetingshoworhide.show();
//                    postTask(mShowDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        break;
                }
            }
            mTouchX = touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
            mTouchY = touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
            // ??????????????????innerView?????????????????????????????????
//        TouchEvent transformedEvent = TouchEvent.obtain(event);
            TouchEvent transformedEvent = touchEvent;
//        final float offsetX = mForegroundView.getScrollX() - mForegroundView.getLeft();
//        final float offsetY = mForegroundView.getScrollY() - mForegroundView.getTop();
//        transformedEvent.setScreenOffset(offsetX, offsetY);
            mTouchEventMatrix.reset();
            mTouchEventMatrix.setRotate(-mDoodleRotateDegree, getWidth() / 2, getHeight() / 2);
//            transformedEvent.transform(mTouchEventMatrix);
            boolean handled = mForegroundView.onTouchEvent(transformedEvent);
//        transformedEvent.recycle();
            LogUtil.d(TAG, "handled=" + handled);
            return handled;
        }
    };

    /**
     * setExtTouchEventListener
     *
     * @param listener
     */
    public void setExtTouchEventListener(TouchEventListener listener) {
//        mOnTouchListener = listener;
//        super.setTouchEventListener(listener);
    }

    private void init(int width, int height) {// ??????resize preview
        int w = mPixelMap.getImageInfo().size.width;
        int h = mPixelMap.getImageInfo().size.height;
        float nw = w * 1f / width;
        float nh = h * 1f / height;
        if (nw > nh) {
            mCenterScale = 1f / nw;
            mCenterWidth = width;
            mCenterHeight = (int) (h * mCenterScale);
        } else {
            mCenterScale = 1f / nh;
            mCenterWidth = (int) (w * mCenterScale);
            mCenterHeight = height;
        }
        // ???????????????
        mCentreTranX = (width - mCenterWidth) / 2f;
        mCentreTranY = (height - mCenterHeight) / 2f;

        mZoomerRadius = Math.min(width, height) / 4;
        mZoomerPath = new Path();
        mZoomerPath.addCircle(mZoomerRadius, mZoomerRadius, mZoomerRadius, Path.Direction.COUNTER_CLOCK_WISE);
        mZoomerHorizonX = (int) (Math.min(width, height) / 2 - mZoomerRadius);

        mDoodleSizeUnit = Util.vp2px(getContext(), 1) / mCenterScale;

        if (!mReady) { // ?????????????????????????????????????????????
            mSize = DEFAULT_SIZE * mDoodleSizeUnit;
        }
        // ??????????????????
        mTransX = mTransY = 0;
        mScale = 1;

        initDoodlePixelMap();

        refreshWithBackground();
    }

    private void initDoodlePixelMap() {
        if (!mOptimizeDrawing) {
            return;
        }

        if (mDoodlePixelMap != null) {
            mDoodlePixelMap.release();
        }
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.editable = true;
        mDoodlePixelMap = PixelMap.create(mPixelMap, options);
        mDoodlePixelMapCanvas = new Canvas(new Texture(mDoodlePixelMap));
    }

    /**
     * ?????????????????????View???????????????????????????
     *
     * @return RectFloat
     */
    public RectFloat getDoodleBound() {
        float width = mCenterWidth * mRotateScale * mScale;
        float height = mCenterHeight * mRotateScale * mScale;
        if (mDoodleRotateDegree % 90 == 0) { // ???0,90,180???270????????????????????????
            if (mDoodleRotateDegree == 0) {
                mTempPoint = new Point(toTouchX(0), toTouchY(0));
            } else if (mDoodleRotateDegree == 90) {
                mTempPoint = new Point(toTouchX(0), toTouchY(mPixelMap.getImageInfo().size.height));
                float t = width;
                width = height;
                height = t;
            } else if (mDoodleRotateDegree == 180) {
                mTempPoint = new Point(toTouchX(mPixelMap.getImageInfo().size.width), toTouchY(mPixelMap.getImageInfo().size.height));
            } else if (mDoodleRotateDegree == 270) {
                mTempPoint = new Point(toTouchX(mPixelMap.getImageInfo().size.width), toTouchY(0));
                float t = width;
                width = height;
                height = t;
            }
            DrawUtil.rotatePoint(mTempPoint, mDoodleRotateDegree, mTempPoint.getPointX(), mTempPoint.getPointY(), getWidth() / 2, getHeight() / 2);
            mDoodleBound.modify(mTempPoint.getPointX(), mTempPoint.getPointY(), mTempPoint.getPointX() + width, mTempPoint.getPointY() + height);
        } else {
            // ?????????????????????
            // ??????
            float ltX = toTouchX(0);
            float ltY = toTouchY(0);
            //??????
            float rbX = toTouchX(mPixelMap.getImageInfo().size.width);
            float rbY = toTouchY(mPixelMap.getImageInfo().size.height);
            // ??????
            float lbX = toTouchX(0);
            float lbY = toTouchY(mPixelMap.getImageInfo().size.height);
            //??????
            float rtX = toTouchX(mPixelMap.getImageInfo().size.width);
            float rtY = toTouchY(0);

            //?????????View?????????
            DrawUtil.rotatePoint(mTempPoint, mDoodleRotateDegree, ltX, ltY, getWidth() / 2, getHeight() / 2);
            ltX = mTempPoint.getPointX();
            ltY = mTempPoint.getPointY();
            DrawUtil.rotatePoint(mTempPoint, mDoodleRotateDegree, rbX, rbY, getWidth() / 2, getHeight() / 2);
            rbX = mTempPoint.getPointX();
            rbY = mTempPoint.getPointY();
            DrawUtil.rotatePoint(mTempPoint, mDoodleRotateDegree, lbX, lbY, getWidth() / 2, getHeight() / 2);
            lbX = mTempPoint.getPointX();
            lbY = mTempPoint.getPointY();
            DrawUtil.rotatePoint(mTempPoint, mDoodleRotateDegree, rtX, rtY, getWidth() / 2, getHeight() / 2);
            rtX = mTempPoint.getPointX();
            rtY = mTempPoint.getPointY();

            mDoodleBound.left = Math.min(Math.min(ltX, rbX), Math.min(lbX, rtX));
            mDoodleBound.top = Math.min(Math.min(ltY, rbY), Math.min(lbY, rtY));
            mDoodleBound.right = Math.max(Math.max(ltX, rbX), Math.max(lbX, rtX));
            mDoodleBound.bottom = Math.max(Math.max(ltY, rbY), Math.max(lbY, rtY));
        }
        return mDoodleBound;
    }

    /**
     * dispatchDraw
     *
     * @param canvas
     */
    protected void dispatchDraw(Canvas canvas) {
        if (mPixelMap.isReleased()) {
            return;
        }

        if (hasFlag(FLAG_RESET_BACKGROUND)) {
            LogUtil.d(TAG, "FLAG_RESET_BACKGROUND");
            clearFlag(FLAG_RESET_BACKGROUND);
            clearFlag(FLAG_DRAW_PENDINGS_TO_BACKGROUND);
            clearFlag(FLAG_REFRESH_BACKGROUND);
            refreshDoodlePixelMap(false);
            mPendingItemsDrawToPixelMap.clear();
            mBackgroundView.invalidate();
        } else if (hasFlag(FLAG_DRAW_PENDINGS_TO_BACKGROUND)) {
            LogUtil.d(TAG, "FLAG_DRAW_PENDINGS_TO_BACKGROUND");
            clearFlag(FLAG_DRAW_PENDINGS_TO_BACKGROUND);
            clearFlag(FLAG_REFRESH_BACKGROUND);
            drawToDoodlePixelMap(mPendingItemsDrawToPixelMap);
            mPendingItemsDrawToPixelMap.clear();
            mBackgroundView.invalidate();
        } else if (hasFlag(FLAG_REFRESH_BACKGROUND)) {
            LogUtil.d(TAG, "FLAG_REFRESH_BACKGROUND");
            clearFlag(FLAG_REFRESH_BACKGROUND);
            mBackgroundView.invalidate();
        }

        int count = canvas.save();
        dispatchChildDraw(canvas);
        canvas.restoreToCount(count);

        /*// test
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(20);
        canvas.drawRect(getDoodleBound(), mPaint);*/

        if (mIsScrollingDoodle
            && mEnableZoomer && mZoomerScale > 0) { //???????????????
            canvas.save(); // ***

            float unitSize = getUnitSize();
            if (mTouchY <= mZoomerRadius * 2) { // ??????????????????????????? ????????????????????????
                mLastZoomerY = getHeight() - mZoomerRadius * 2;
            } else if (mTouchY >= getHeight() - mZoomerRadius * 2) {
                mLastZoomerY = 0;
            }
            canvas.translate(mZoomerHorizonX, mLastZoomerY);
            canvas.clipPath(mZoomerPath, Canvas.ClipOp.INTERSECT);
            canvas.drawColor(0xff000000, Canvas.PorterDuffMode.SRC_OVER);

            canvas.save();
            float scale = mZoomerScale / mScale; // ??????mScale???????????????????????????????????????????????????????????????????????????mAmplifierScale????????????
            canvas.scale(scale, scale);
            canvas.translate(-mTouchX + mZoomerRadius / scale, -mTouchY + mZoomerRadius / scale);
            // draw inner
            dispatchChildDraw(canvas);
            // ?????????
            float left = getAllTranX();
            float top = getAllTranY();
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            canvas.translate(left, top); // ????????????
            scale = getAllScale();
            canvas.scale(scale, scale); // ????????????
            mZoomerTouchPaint.setStrokeWidth(unitSize / 2);
            float radius = mSize / 2 - unitSize / 2;
            float radius2 = radius - unitSize / 2;
            if (radius <= 1) {
                radius = 1;
                radius2 = radius / 2;
                mZoomerTouchPaint.setStrokeWidth(mSize);
            }
            mZoomerTouchPaint.setColor(new Color(0xaa000000));
            DrawUtil.drawCircle(canvas, toX(mTouchX), toY(mTouchY), radius, mZoomerTouchPaint);
            mZoomerTouchPaint.setColor(new Color(0xaaffffff));
            DrawUtil.drawCircle(canvas, toX(mTouchX), toY(mTouchY), radius2, mZoomerTouchPaint);
            canvas.restore();

            // ?????????????????????
            DrawUtil.drawCircle(canvas, mZoomerRadius, mZoomerRadius, mZoomerRadius, mZooomerPaint);

            canvas.restore(); // ***

            // overview
            canvas.save();
            canvas.translate(mZoomerHorizonX, mLastZoomerY);
            scale = (mZoomerRadius / 2) / getWidth();
            canvas.scale(scale, scale);
            float strokeWidth = 1 / scale;
            canvas.clipRect(-strokeWidth, -strokeWidth, getWidth() + strokeWidth, getHeight() + strokeWidth);
            canvas.drawColor(0x88888888, Canvas.PorterDuffMode.SRC_OVER);
            canvas.save();
//            float tempScale = mScale;
//            float tempTransX = mTransX;
//            float tempTransY = mTransY;
//            mScale = 1;
//            mTransX = mTransY = 0;
            dispatchChildDraw(canvas);
//            mScale = tempScale;
//            mTransX = tempTransX;
//            mTransY = tempTransY;
            canvas.restore();
            mZoomerTouchPaint.setStrokeWidth(strokeWidth);
            mZoomerTouchPaint.setColor(new Color(0xaa000000));
            DrawUtil.drawRect(canvas, 0, 0, getWidth(), getHeight(), mZoomerTouchPaint);
            mZoomerTouchPaint.setColor(new Color(0xaaffffff));
            DrawUtil.drawRect(canvas, strokeWidth, strokeWidth, getWidth() - strokeWidth, getHeight() - strokeWidth, mZoomerTouchPaint);
            canvas.restore();
        }

    }

    private void dispatchChildDraw(Canvas canvas) {
        mBackgroundView.onDrawInner(canvas);
        mForegroundView.onDrawInner(canvas);
    }

    private boolean hasFlag(int flag) {
        return (mFlags & flag) != 0;
    }

    private void addFlag(int flag) {
        mFlags = mFlags | flag;
    }

    private void clearFlag(int flag) {
        mFlags = mFlags & ~flag;
    }

    public float getAllScale() {
        return mCenterScale * mRotateScale * mScale;
    }

    public float getAllTranX() {
        return mCentreTranX + mRotateTranX + mTransX;
    }

    public float getAllTranY() {
        return mCentreTranY + mRotateTranY + mTransY;
    }

    /**
     * ?????????????????????x??????????????????????????????
     *
     * @param touchX
     * @return float
     */
    public final float toX(float touchX) {
        return (touchX - getAllTranX()) / getAllScale();
    }

    /**
     * ?????????????????????y??????????????????????????????
     *
     * @param touchY
     * @return float
     */
    public final float toY(float touchY) {
        return (touchY - getAllTranY()) / getAllScale();
    }

    /**
     * ???????????????x???????????????????????????
     *
     * @param x
     * @return float
     */
    public final float toTouchX(float x) {
        return x * getAllScale() + getAllTranX();
    }

    /**
     * ???????????????y???????????????????????????
     *
     * @param y
     * @return float
     */
    public final float toTouchY(float y) {
        return y * getAllScale() + getAllTranY();
    }

    /**
     * ????????????
     * ????????????toX()????????????????????????
     *
     * @param touchX ????????????
     * @param doodleX ???????????????????????????
     * @return ?????????
     */
    public final float toTransX(float touchX, float doodleX) {
        return -doodleX * getAllScale() + touchX - mCentreTranX - mRotateTranX;
    }

    /**
     * toTransY
     *
     * @param touchY
     * @param doodleY
     * @return float
     */
    public final float toTransY(float touchY, float doodleY) {
        return -doodleY * getAllScale() + touchY - mCentreTranY - mRotateTranY;
    }

    /**
     * ?????????????????????????????????
     *
     * @param pen
     * @param detector
     */
    public void bindTouchDetector(IDoodlePen pen, IDoodleTouchDetector detector) {
        if (pen == null) {
            return;
        }
        mTouchDetectorMap.put(pen, detector);
    }

    /**
     * ????????????????????????????????????
     *
     * @param pen
     * @return IDoodleTouchDetector
     */
    public IDoodleTouchDetector getDefaultTouchDetector(IDoodlePen pen) {
        return mTouchDetectorMap.get(pen);
    }

    /**
     * ????????????????????????????????????
     *
     * @param pen
     */
    public void removeTouchDetector(IDoodlePen pen) {
        if (pen == null) {
            return;
        }
        mTouchDetectorMap.remove(pen);
    }

    /**
     * ???????????????????????????
     *
     * @param touchGestureDetector
     */
    public void setDefaultTouchDetector(IDoodleTouchDetector touchGestureDetector) {
        mDefaultTouchDetector = touchGestureDetector;
    }

    /**
     * ?????????????????????
     *
     * @return IDoodleTouchDetector
     */
    public IDoodleTouchDetector getDefaultTouchDetector() {
        return mDefaultTouchDetector;
    }

    /**
     * drawToDoodlePixelMap
     *
     * @param items
     */
    private void drawToDoodlePixelMap(List<IDoodleItem> items) {
        if (!mOptimizeDrawing) {
            return;
        }

        for (IDoodleItem item : items) {
            item.draw(mDoodlePixelMapCanvas, mDoodlePixelMap);
        }
    }

    /**
     * refreshDoodlePixelMap
     *
     * @param drawAll
     */
    private void refreshDoodlePixelMap(boolean drawAll) {
        if (!mOptimizeDrawing) {
            return;
        }

        initDoodlePixelMap();
        List<IDoodleItem> items = null;
        if (drawAll) {
            items = mItemStack;
        } else {
            items = new ArrayList<>(mItemStack);
            items.removeAll(mItemStackOnViewCanvas);
        }
        for (IDoodleItem item : items) {
            item.draw(mDoodlePixelMapCanvas, mDoodlePixelMap);
        }
    }

    /**
     * refreshWithBackground
     */
    private void refreshWithBackground() {
        addFlag(FLAG_REFRESH_BACKGROUND);
        refresh();
    }

    // ========================= api ================================

    @Override
    public void invalidate() {
        refresh();
    }

    @Override
    public void refresh() {
        super.invalidate();
        mForegroundView.invalidate();
    }

    @Override
    public int getDoodleRotation() {
        return mDoodleRotateDegree;
    }

    /**
     * ????????????????????????????????????
     *
     * @param degree positive degree means rotate right, negative degree means rotate left
     */

    @Override
    public void setDoodleRotation(int degree) {
        mDoodleRotateDegree = degree;
        mDoodleRotateDegree = mDoodleRotateDegree % 360;
        if (mDoodleRotateDegree < 0) {
            mDoodleRotateDegree = 360 + mDoodleRotateDegree;
        }

        // ??????
        RectFloat rectF = getDoodleBound();
        int w = (int) (rectF.getWidth() / getAllScale());
        int h = (int) (rectF.getHeight() / getAllScale());
        float nw = w * 1f / getWidth();
        float nh = h * 1f / getHeight();
        float scale;
        float tx, ty;
        if (nw > nh) {
            scale = 1 / nw;
        } else {
            scale = 1 / nh;
        }

        int pivotX = mPixelMap.getImageInfo().size.width / 2;
        int pivotY = mPixelMap.getImageInfo().size.height / 2;

        mTransX = mTransY = 0;
        mRotateTranX = mRotateTranY = 0;
        this.mScale = 1;
        mRotateScale = 1;
        float touchX = toTouchX(pivotX);
        float touchY = toTouchY(pivotY);
        mRotateScale = scale / mCenterScale;

        // ??????????????????????????????????????????????????????????????????
        tx = toTransX(touchX, pivotX);
        ty = toTransY(touchY, pivotY);

        mRotateTranX = tx;
        mRotateTranY = ty;

        refreshWithBackground();
    }

    public boolean isOptimizeDrawing() {
        return mOptimizeDrawing;
    }

    /**
     * ??????item?????????View?????????????????????????????????PixelMap. ???????????????????????????item. ???????????????????????? {@link #notifyItemFinishedDrawing(IDoodleItem)}
     * ???????????????????????????mOptimizeDrawing=true????????????
     *
     * @param item
     * @throws RuntimeException
     */
    public void markItemToOptimizeDrawing(IDoodleItem item) {
        if (!mOptimizeDrawing) {
            return;
        }

        if (mItemStackOnViewCanvas.contains(item)) {
            throw new RuntimeException("The item has been added");
        }

        mItemStackOnViewCanvas.add(item);

        if (mItemStack.contains(item)) {
            addFlag(FLAG_RESET_BACKGROUND);
        }

        refresh();
    }

    /**
     * ???item???View??????????????????????????????????????????. ?????? {@link #notifyItemFinishedDrawing(IDoodleItem)}
     *
     * @param item
     */
    public void notifyItemFinishedDrawing(IDoodleItem item) {
        if (!mOptimizeDrawing) {
            return;
        }

        if (mItemStackOnViewCanvas.remove(item)) {
            if (mItemStack.contains(item)) {
                addFlag(FLAG_RESET_BACKGROUND);
            } else {
                addItem(item);
            }
        }

        refresh();
    }

    /**
     * ??????, ??????DoodleListener.onSaved()??????????????????save()???????????????
     */
//    @SuppressLint("StaticFieldLeak")
    @Override
    public void save() {
        if (mIsSaving) {
            return;
        }

        mIsSaving = true;

//            //????????????????????????EventRunner?????????????????????????????????EventRunner?????????????????????
        EventRunner eventRunner = EventRunner.current();
        if (eventRunner == null) {
            LogUtil.d(TAG, "save---????????????????????????");
            return;
        }
        CursorAnimEventHandler handler = new CursorAnimEventHandler(eventRunner);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PixelMap savedPixelMap = null;

                if (mOptimizeDrawing) {
                    refreshDoodlePixelMap(true);
                    savedPixelMap = mDoodlePixelMap;
                } else {
                    PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
                    options.editable = true;
                    savedPixelMap = PixelMap.create(mPixelMap, options);
                    Canvas canvas = new Canvas();
                    for (IDoodleItem item : mItemStack) {
                        item.draw(canvas, savedPixelMap);
                    }
                }
                savedPixelMap = ImageUtils.rotate(savedPixelMap, mDoodleRotateDegree, true);
                handler.sendEvent(InnerEvent.get(SAVA_IMAGE, savedPixelMap));
            }
        }).start();
    }

    /**
     * CursorAnimEventHandler
     *
     * @since 2021-04-29
     */
    class CursorAnimEventHandler extends EventHandler {

        public CursorAnimEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            LogUtil.d(TAG, "processEvent()---??????Id" + event.eventId);
            if (event.eventId == SAVA_IMAGE) {
                PixelMap pixelMap = (PixelMap) event.object;
                mDoodleListener.onSaved(DoodleView.this, pixelMap, new Runnable() {
                    @Override
                    public void run() {
                        mIsSaving = false;
                        if (mOptimizeDrawing) {
                            refreshDoodlePixelMap(false);
                        }
                        refresh();
                    }
                });
            }
        }

        /**
         * distributeEvent
         *
         * @param event
         */
        @Override
        public void distributeEvent(InnerEvent event) {
            super.distributeEvent(event);
        }
    }


    /**
     * ??????
     */
    @Override
    public void clear() {
        List<IDoodleItem> temp = new ArrayList<>(mItemStack);
        mItemStack.clear();
        mRedoItemStack.clear();
        mItemStackOnViewCanvas.clear();
        mPendingItemsDrawToPixelMap.clear();

        for (int i = temp.size() - 1; i >= 0; i--) {
            IDoodleItem item = temp.get(i);
            item.onRemove();
        }

        addFlag(FLAG_RESET_BACKGROUND);

        refresh();
    }

    @Override
    public boolean undo(int step) {
        if (mItemStack.size() > 0) {
            step = Math.min(mItemStack.size(), step);
            List<IDoodleItem> list = new ArrayList<IDoodleItem>(mItemStack.subList(mItemStack.size() - step, mItemStack.size()));
            for (IDoodleItem item : list) {
                removeItem(item);
                mRedoItemStack.add(0, item);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean redo(int step) {
        if (mRedoItemStack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < step && !mRedoItemStack.isEmpty(); i++) {
            addItemInner(mRedoItemStack.remove(0));
        }
        return true;
    }

    /**
     * ??????
     *
     * @return ??????????????????
     */
    public boolean undo() {
        return undo(1);
    }

    /**
     * ???????????????
     *
     * @param justDrawOriginal
     */
    @Override
    public void setShowOriginal(boolean justDrawOriginal) {
        isJustDrawOriginal = justDrawOriginal;
        refreshWithBackground();
    }

    @Override
    public boolean isShowOriginal() {
        return isJustDrawOriginal;
    }

    /**
     * ??????????????????
     *
     * @param color
     */
    @Override
    public void setColor(IDoodleColor color) {
        mColor = color;
        refresh();
    }

    @Override
    public IDoodleColor getColor() {
        return mColor;
    }

    /**
     * ?????????????????????
     * ?????????????????????????????? mCenterScale*mScale
     *
     * @param scale
     * @param pivotX ??????????????????
     * @param pivotY
     */
    @Override
    public void setDoodleScale(float scale, float pivotX, float pivotY) {
        if (scale < mMinScale) {
            scale = mMinScale;
        } else if (scale > mMaxScale) {
            scale = mMaxScale;
        }

        float touchX = toTouchX(pivotX);
        float touchY = toTouchY(pivotY);
        this.mScale = scale;

        // ??????????????????????????????????????????????????????????????????
        mTransX = toTransX(touchX, pivotX);
        mTransY = toTransY(touchY, pivotY);

        addFlag(FLAG_REFRESH_BACKGROUND);
        refresh();
    }

    @Override
    public float getDoodleScale() {
        return mScale;
    }

    /**
     * ????????????
     *
     * @param pen
     */
    @Override
    public void setPen(IDoodlePen pen) {
        if (pen == null) {
            throw new RuntimeException("Pen can't be null");
        }
        IDoodlePen old = mPen;
        mPen = pen;
        refresh();
    }

    @Override
    public IDoodlePen getPen() {
        return mPen;
    }

    /**
     * ??????????????????
     *
     * @param shape
     */
    @Override
    public void setShape(IDoodleShape shape) {
        if (shape == null) {
            throw new RuntimeException("Shape can't be null");
        }
        mShape = shape;
        refresh();
    }

    @Override
    public IDoodleShape getShape() {
        return mShape;
    }

    @Override
    public void setDoodleTranslation(float transX, float transY) {
        mTransX = transX;
        mTransY = transY;
        refreshWithBackground();
    }

    /**
     * ????????????G??????
     *
     * @param transX
     */
    @Override
    public void setDoodleTranslationX(float transX) {
        this.mTransX = transX;
        refreshWithBackground();
    }

    @Override
    public float getDoodleTranslationX() {
        return mTransX;
    }

    @Override
    public void setDoodleTranslationY(float transY) {
        this.mTransY = transY;
        refreshWithBackground();
    }

    @Override
    public float getDoodleTranslationY() {
        return mTransY;
    }


    @Override
    public void setSize(float paintSize) {
        mSize = paintSize;
        refresh();
    }

    @Override
    public float getSize() {
        return mSize;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param isDrawableOutside
     */
    @Override
    public void setIsDrawableOutside(boolean isDrawableOutside) {
        mIsDrawableOutside = isDrawableOutside;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @return ????????????????????????
     */
    @Override
    public boolean isDrawableOutside() {
        return mIsDrawableOutside;
    }

    /**
     * ??????????????????????????????????????????0?????????????????????????????????
     *
     * @param scale
     */
    @Override
    public void setZoomerScale(float scale) {
        mZoomerScale = scale;
        refresh();
    }

    @Override
    public float getZoomerScale() {
        return mZoomerScale;
    }

    /**
     * ???????????????????????????
     *
     * @param enable
     */
    public void enableZoomer(boolean enable) {
        mEnableZoomer = enable;
    }

    /**
     * ?????????????????????
     *
     * @return boolean
     */
    public boolean isEnableZoomer() {
        return mEnableZoomer;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param enableOverview
     */
    public void enableOverview(boolean enableOverview) {
        mEnableOverview = enableOverview;
    }

    /**
     * ??????????????????????????????
     *
     * @return boolean
     */
    public boolean isEnableOverview() {
        return mEnableOverview;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @return boolean
     */
    public boolean isScrollingDoodle() {
        return mIsScrollingDoodle;
    }

    /**
     * setScrollingDoodle
     *
     * @param scrollingDoodle ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    public void setScrollingDoodle(boolean scrollingDoodle) {
        mIsScrollingDoodle = scrollingDoodle;
        refresh();
    }

    @Override
    public void topItem(IDoodleItem item) {
        if (item == null) {
            throw new RuntimeException("item is null");
        }

        mItemStack.remove(item);
        mItemStack.add(item);

        addFlag(FLAG_RESET_BACKGROUND);

        refresh();
    }

    @Override
    public void bottomItem(IDoodleItem item) {
        if (item == null) {
            throw new RuntimeException("item is null");
        }

        mItemStack.remove(item);
        mItemStack.add(0, item);

        addFlag(FLAG_RESET_BACKGROUND);

        refresh();
    }

    @Override
    public void setDoodleMinScale(float minScale) {
        mMinScale = minScale;
        setDoodleScale(mScale, 0, 0);
    }

    @Override
    public float getDoodleMinScale() {
        return mMinScale;
    }

    @Override
    public void setDoodleMaxScale(float maxScale) {
        mMaxScale = maxScale;
        setDoodleScale(mScale, 0, 0);
    }

    @Override
    public float getDoodleMaxScale() {
        return mMaxScale;
    }

    @Override
    public float getUnitSize() {
        return mDoodleSizeUnit;
    }

    @Override
    public void addItem(IDoodleItem item) {
        addItemInner(item);
        mRedoItemStack.clear();
    }

    private void addItemInner(IDoodleItem item) {
        if (item == null) {
            throw new RuntimeException("item is null");
        }

        if (this != item.getDoodle()) {
            throw new RuntimeException("the object Doodle is illegal");
        }
        if (mItemStack.contains(item)) {
            throw new RuntimeException("the item has been added");
        }

        mItemStack.add(item);
        item.onAdd();

        mPendingItemsDrawToPixelMap.add(item);
        addFlag(FLAG_DRAW_PENDINGS_TO_BACKGROUND);

        refresh();
    }

    @Override
    public void removeItem(IDoodleItem doodleItem) {
        if (!mItemStack.remove(doodleItem)) {
            return;
        }

        mItemStackOnViewCanvas.remove(doodleItem);
        mPendingItemsDrawToPixelMap.remove(doodleItem);
        doodleItem.onRemove();

        addFlag(FLAG_RESET_BACKGROUND);

        refresh();
    }

    @Override
    public int getItemCount() {
        return mItemStack.size();
    }

    @Override
    public List<IDoodleItem> getAllItem() {
        return new ArrayList<>(mItemStack);
    }

    @Override
    public int getRedoItemCount() {
        return mRedoItemStack.size();
    }

    @Override
    public List<IDoodleItem> getAllRedoItem() {
        return new ArrayList<>(mRedoItemStack);
    }

    @Override
    public PixelMap getPixelMap() {
        return mPixelMap;
    }

    @Override
    public PixelMap getDoodlePixelMap() {
        return mDoodlePixelMap;
    }

    public int getCenterWidth() {
        return mCenterWidth;
    }

    public int getCenterHeight() {
        return mCenterHeight;
    }

    public float getCenterScale() {
        return mCenterScale;
    }

    public float getCentreTranX() {
        return mCentreTranX;
    }

    public float getCentreTranY() {
        return mCentreTranY;
    }

    public float getRotateScale() {
        return mRotateScale;
    }

    public float getRotateTranX() {
        return mRotateTranX;
    }

    public float getRotateTranY() {
        return mRotateTranY;
    }

    /**
     * ?????????????????????
     *
     * @return boolean
     */
    public boolean isEditMode() {
        return mIsEditMode;
    }

    /**
     * setEditMode
     *
     * @param editMode
     */
    public void setEditMode(boolean editMode) {
        mIsEditMode = editMode;
        refresh();
    }

    /**
     * ??????????????????????????????????????????????????? ??????????????????????????????????????????????????????????????????item
     *
     * @since 2021-04-29
     */
    private class BackgroundView extends Component {

        public BackgroundView(Context context) {
            super(context);
            addDrawTask(new DrawTask() {
                @Override
                public void onDraw(Component component, Canvas canvas) {
                    onDrawInner(canvas);
                }
            });
        }

        public void onDrawInner(Canvas canvas) {
            int count = canvas.save();
            canvas.rotate(mDoodleRotateDegree, getWidth() / 2, getHeight() / 2);
            doDraw(canvas);
            canvas.restoreToCount(count);
        }

        private void doDraw(Canvas canvas) {
            float left = getAllTranX();
            float top = getAllTranY();

            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            canvas.translate(left, top); // ????????????
            float scale = getAllScale();
            canvas.scale(scale, scale); // ????????????
            if (isJustDrawOriginal) { // ???????????????
                canvas.drawPixelMapHolder(new PixelMapHolder(mPixelMap), 0, 0, new Paint());
                return;
            }

            PixelMap pixelMap = mOptimizeDrawing ? mDoodlePixelMap : mPixelMap;

            // ????????????????????????
            canvas.drawPixelMapHolder(new PixelMapHolder(pixelMap), 0, 0, new Paint());
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????item
     *
     * @since 2021-04-29
     */
    private class ForegroundView extends Component {
        public ForegroundView(Context context) {
            super(context);

            // ????????????????????????????????????????????????????????????
//            setLayerType(LAYER_TYPE_SOFTWARE, null); ?????????api
            addDrawTask(new DrawTask() {
                @Override
                public void onDraw(Component component, Canvas canvas) {
                    onDrawInner(canvas);
                }
            });
        }

        public boolean onTouchEvent(TouchEvent event) {
            // ??????????????????
            IDoodleTouchDetector detector = mTouchDetectorMap.get(mPen);
            if (detector != null) {
                return detector.onTouchEvent(event);
            }
            // ???????????????
            if (mDefaultTouchDetector != null) {
                return mDefaultTouchDetector.onTouchEvent(event);
            }
            return false;
        }

        public void onDrawInner(Canvas canvas) {
            int count = canvas.save();
            canvas.rotate(mDoodleRotateDegree, getWidth() / 2, getHeight() / 2);
            doDraw(canvas);
            canvas.restoreToCount(count);
        }

        private void doDraw(Canvas canvas) {
            if (isJustDrawOriginal) { // ???????????????
                return;
            }

            float left = getAllTranX();
            float top = getAllTranY();

            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            canvas.translate(left, top); // ????????????
            float scale = getAllScale();
            canvas.scale(scale, scale); // ????????????

            PixelMap pixelMap = mOptimizeDrawing ? mDoodlePixelMap : mPixelMap;

            int saveCount = canvas.save(); // 1
            List<IDoodleItem> items = mItemStack;
            if (mOptimizeDrawing) {
                items = mItemStackOnViewCanvas;
            }
            boolean canvasClipped = false;
            if (!mIsDrawableOutside) { // ?????????????????????????????????
                canvasClipped = true;
                if (pixelMap != null) {
                    canvas.clipRect(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
                }
            }
            for (IDoodleItem item : items) {
                if (!item.isNeedClipOutside()) { // 1.???????????????
                    if (canvasClipped) {
                        canvas.restore();
                    }

                    item.draw(canvas, pixelMap);

                    if (canvasClipped) { // 2.????????????
                        canvas.save();
                        canvas.clipRect(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
                    }
                } else {
                    item.draw(canvas, pixelMap);
                }
            }

            // draw at the top
            for (IDoodleItem item : items) {
                if (!item.isNeedClipOutside()) { // 1.???????????????
                    if (canvasClipped) {
                        canvas.restore();
                    }
                    item.drawAtTheTop(canvas);

                    if (canvasClipped) { // 2.????????????
                        canvas.save();
                        canvas.clipRect(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
                    }
                } else {
                    item.drawAtTheTop(canvas);
                }
            }
            canvas.restoreToCount(saveCount);

            if (mPen != null) {
                mPen.drawHelpers(canvas, DoodleView.this);
            }
            if (mShape != null) {
                mShape.drawHelpers(canvas, DoodleView.this);
            }
        }
    }

    public void setIsetshoworhide(ISetingshoworhide mISetingshoworhide) {
        this.mISetingshoworhide = mISetingshoworhide;
    }
}
