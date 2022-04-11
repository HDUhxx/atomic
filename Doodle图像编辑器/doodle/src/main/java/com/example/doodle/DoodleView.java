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
 * 涂鸦框架
 *
 * @since 2021-04-29
 */
public class DoodleView extends StackLayout implements IDoodle {

    /**
     * TAG
     */
    public static final String TAG = "DoodleView";
    /**
     * 最大缩放倍数
     */
    public final static float MAX_SCALE = 5f;
    /**
     * 最小缩放倍数
     */
    public final static float MIN_SCALE = 0.25f;
    /**
     * 默认画笔大小
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

    private final PixelMap mPixelMap; // 当前涂鸦的原图

    private float mCenterScale; // 图片适应屏幕时的缩放倍数
    private int mCenterHeight, mCenterWidth;// 图片适应屏幕时的大小（View窗口坐标系上的大小）
    private float mCentreTranX, mCentreTranY;// 图片在适应屏幕时，位于居中位置的偏移（View窗口坐标系上的偏移）

    private float mRotateScale = 1;  // 在旋转后适应屏幕时的缩放倍数
    private float mRotateTranX, mRotateTranY; // 旋转后适应屏幕居中时的偏移

    private float mScale = 1; // 在适应屏幕时的缩放基础上的缩放倍数 （ 图片真实的缩放倍数为 mCenterScale*mScale ）
    private float mTransX = 0, mTransY = 0; // 图片在适应屏幕且处于居中位置的基础上的偏移量（ 图片真实偏移量为mCentreTranX + mTransX，View窗口坐标系上的偏移）
    private float mMinScale = MIN_SCALE; // 最小缩放倍数
    private float mMaxScale = MAX_SCALE; // 最大缩放倍数

    private float mSize;
    private IDoodleColor mColor; // 画笔底色

    private boolean isJustDrawOriginal; // 是否只绘制原图

    private boolean mIsDrawableOutside = false; // 触摸时，图片区域外是否绘制涂鸦轨迹
    private boolean mReady = false;

    // 保存涂鸦操作，便于撤销
    private List<IDoodleItem> mItemStack = new ArrayList<>();
    private List<IDoodleItem> mRedoItemStack = new ArrayList<>();

    private IDoodlePen mPen;
    private IDoodleShape mShape;

    private float mTouchX, mTouchY;
    private boolean mEnableZoomer = false; // 放大镜功能
    private boolean mEnableOverview = true; // 全图预览功能，建立在放大镜功能开启的前提下
    private float mLastZoomerY;
    private float mZoomerRadius;
    private Path mZoomerPath;
    private float mZoomerScale = 0; // 放大镜的倍数
    private Paint mZooomerPaint, mZoomerTouchPaint;
    private int mZoomerHorizonX; // 放大器的位置的x坐标，使其水平居中
    private boolean mIsScrollingDoodle = false; // 是否正在滑动，只要用于标志触摸时才显示放大镜

    private float mDoodleSizeUnit = 1; // 长度单位，不同大小的图片的长度单位不一样。该单位的意义同dp的作用类似，独立于图片之外的单位长度
    private int mDoodleRotateDegree = 0; // 相对于初始图片旋转的角度

    // 手势相关
    private IDoodleTouchDetector mDefaultTouchDetector;
    private Map<IDoodlePen, IDoodleTouchDetector> mTouchDetectorMap = new HashMap<>();

    private ForegroundView mForegroundView;
    private RectFloat mDoodleBound = new RectFloat();
    private Point mTempPoint = new Point();

    private boolean mIsEditMode = false; //是否是编辑模式，可移动缩放涂鸦
    private boolean mIsSaving = false;
    private static final int SAVA_IMAGE = 110;
    /**
     * Whether or not to optimize drawing, it is suggested to open, which can optimize the drawing speed and performance.
     * Note: When item is selected for editing after opening, it will be drawn at the top level, and not at the corresponding level until editing is completed.
     * 是否优化绘制，建议开启，可优化绘制速度和性能.
     * 注意：开启后item被选中编辑时时会绘制在最上面一层，直到结束编辑后才绘制在相应层级
     **/
    private final boolean mOptimizeDrawing; // 涂鸦及时绘制在图片上，优化性能
    private List<IDoodleItem> mItemStackOnViewCanvas = new ArrayList<>(); // 这些item绘制在View的画布上，而不是在图片PixelMap.比如正在创建或选中的item
    private List<IDoodleItem> mPendingItemsDrawToPixelMap = new ArrayList<>();
    private PixelMap mDoodlePixelMap;
    private int mFlags = 0;
    private Canvas mDoodlePixelMapCanvas;
    private BackgroundView mBackgroundView;
    private ISetingshoworhide mISetingshoworhide;

    /**
     * 初始化
     *
     * @param context
     * @param pixelMap
     * @param listener
     */
    public DoodleView(Context context, PixelMap pixelMap, IDoodleListener listener) {
        this(context, pixelMap, false, listener, null);
    }

    /**
     * 初始化
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
     * 初始化
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
     * 如果开启
     *
     * @param context
     * @param pixelMap
     * @param optimizeDrawing 是否优化绘制，开启后涂鸦会及时绘制在图片上，以此优化绘制速度和性能.
     * 如果开启了优化绘制，当绘制或编辑某个item时需要调用 {@link #markItemToOptimizeDrawing(IDoodleItem)}，无需再调用{@link #addItem(IDoodleItem)}.
     * 另外结束时需要调用对应的 {@link #notifyItemFinishedDrawing(IDoodleItem)}。
     * {@link #mOptimizeDrawing}
     * @param listener
     * @param defaultDetector 默认手势识别器
     */
    public DoodleView(Context context, PixelMap pixelMap, boolean optimizeDrawing, IDoodleListener listener, IDoodleTouchDetector defaultDetector) {
        super(context);
        setClipEnabled(false);

        mPixelMap = pixelMap;
        if (mPixelMap != null) {
            if (mPixelMap.getImageInfo().pixelFormat != PixelFormat.RGB_565) {
                // 如果位图包含透明度，则可能会导致橡皮擦无法对透明部分进行擦除
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
        mZooomerPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);// 圆滑
        mZooomerPaint.setStrokeWidth(Util.vp2px(getContext(), 10));

        mZoomerTouchPaint = new Paint();
        mZoomerTouchPaint.setStyle(Paint.Style.STROKE_STYLE);
        mZoomerTouchPaint.setAntiAlias(true);
        mZoomerTouchPaint.setStrokeJoin(Paint.Join.ROUND_JOIN);
        mZoomerTouchPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);// 圆滑

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
                        //触摸屏幕超过一定时间才判断为需要隐藏设置面板
//                        mSettingsPanel.postDelayed(mHideDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        mISetingshoworhide.hide();
//                    postTask(mHideDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        break;
                    case TouchEvent.CANCEL:
                    case TouchEvent.PRIMARY_POINT_UP:
//                        mSettingsPanel.removeCallbacks(mHideDelayRunnable);
//                        mSettingsPanel.removeCallbacks(mShowDelayRunnable);
                        //离开屏幕超过一定时间才判断为需要显示设置面板
//                        mSettingsPanel.postDelayed(mShowDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        mISetingshoworhide.show();
//                    postTask(mShowDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                        break;
                }
            }
            mTouchX = touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
            mTouchY = touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
            // 把事件转发给innerView，避免在区域外不可点击
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

    private void init(int width, int height) {// 不用resize preview
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
        // 使图片居中
        mCentreTranX = (width - mCenterWidth) / 2f;
        mCentreTranY = (height - mCenterHeight) / 2f;

        mZoomerRadius = Math.min(width, height) / 4;
        mZoomerPath = new Path();
        mZoomerPath.addCircle(mZoomerRadius, mZoomerRadius, mZoomerRadius, Path.Direction.COUNTER_CLOCK_WISE);
        mZoomerHorizonX = (int) (Math.min(width, height) / 2 - mZoomerRadius);

        mDoodleSizeUnit = Util.vp2px(getContext(), 1) / mCenterScale;

        if (!mReady) { // 只有初始化时才需要设置画笔大小
            mSize = DEFAULT_SIZE * mDoodleSizeUnit;
        }
        // 居中适应屏幕
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
     * 获取当前图片在View坐标系中的矩型区域
     *
     * @return RectFloat
     */
    public RectFloat getDoodleBound() {
        float width = mCenterWidth * mRotateScale * mScale;
        float height = mCenterHeight * mRotateScale * mScale;
        if (mDoodleRotateDegree % 90 == 0) { // 对0,90,180，270度旋转做简化计算
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
            // 转换成屏幕坐标
            // 左上
            float ltX = toTouchX(0);
            float ltY = toTouchY(0);
            //右下
            float rbX = toTouchX(mPixelMap.getImageInfo().size.width);
            float rbY = toTouchY(mPixelMap.getImageInfo().size.height);
            // 左下
            float lbX = toTouchX(0);
            float lbY = toTouchY(mPixelMap.getImageInfo().size.height);
            //右上
            float rtX = toTouchX(mPixelMap.getImageInfo().size.width);
            float rtY = toTouchY(0);

            //转换到View坐标系
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
            && mEnableZoomer && mZoomerScale > 0) { //启用放大镜
            canvas.save(); // ***

            float unitSize = getUnitSize();
            if (mTouchY <= mZoomerRadius * 2) { // 在放大镜的范围内， 把放大镜放在底部
                mLastZoomerY = getHeight() - mZoomerRadius * 2;
            } else if (mTouchY >= getHeight() - mZoomerRadius * 2) {
                mLastZoomerY = 0;
            }
            canvas.translate(mZoomerHorizonX, mLastZoomerY);
            canvas.clipPath(mZoomerPath, Canvas.ClipOp.INTERSECT);
            canvas.drawColor(0xff000000, Canvas.PorterDuffMode.SRC_OVER);

            canvas.save();
            float scale = mZoomerScale / mScale; // 除以mScale，无论当前图片缩放多少，都产生图片在居中状态下缩放mAmplifierScale倍的效果
            canvas.scale(scale, scale);
            canvas.translate(-mTouchX + mZoomerRadius / scale, -mTouchY + mZoomerRadius / scale);
            // draw inner
            dispatchChildDraw(canvas);
            // 触摸点
            float left = getAllTranX();
            float top = getAllTranY();
            // 画布和图片共用一个坐标系，只需要处理屏幕坐标系到图片（画布）坐标系的映射关系
            canvas.translate(left, top); // 偏移画布
            scale = getAllScale();
            canvas.scale(scale, scale); // 缩放画布
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

            // 画放大器的边框
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
     * 将屏幕触摸坐标x转换成在图片中的坐标
     *
     * @param touchX
     * @return float
     */
    public final float toX(float touchX) {
        return (touchX - getAllTranX()) / getAllScale();
    }

    /**
     * 将屏幕触摸坐标y转换成在图片中的坐标
     *
     * @param touchY
     * @return float
     */
    public final float toY(float touchY) {
        return (touchY - getAllTranY()) / getAllScale();
    }

    /**
     * 将图片坐标x转换成屏幕触摸坐标
     *
     * @param x
     * @return float
     */
    public final float toTouchX(float x) {
        return x * getAllScale() + getAllTranX();
    }

    /**
     * 将图片坐标y转换成屏幕触摸坐标
     *
     * @param y
     * @return float
     */
    public final float toTouchY(float y) {
        return y * getAllScale() + getAllTranY();
    }

    /**
     * 坐标换算
     * （公式由toX()中的公式推算出）
     *
     * @param touchX 触摸坐标
     * @param doodleX 在涂鸦图片中的坐标
     * @return 偏移量
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
     * 根据画笔绑定手势识别器
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
     * 获取画笔绑定的手势识别器
     *
     * @param pen
     * @return IDoodleTouchDetector
     */
    public IDoodleTouchDetector getDefaultTouchDetector(IDoodlePen pen) {
        return mTouchDetectorMap.get(pen);
    }

    /**
     * 移除指定画笔的手势识别器
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
     * 设置默认手势识别器
     *
     * @param touchGestureDetector
     */
    public void setDefaultTouchDetector(IDoodleTouchDetector touchGestureDetector) {
        mDefaultTouchDetector = touchGestureDetector;
    }

    /**
     * 默认手势识别器
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
     * 相对于初始图片旋转的角度
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

        // 居中
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

        // 缩放后，偏移图片，以产生围绕某个点缩放的效果
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
     * 标志item绘制在View的画布上，而不是在图片PixelMap. 比如正创建或选中的item. 结束绘制时应调用 {@link #notifyItemFinishedDrawing(IDoodleItem)}
     * 仅在开启优化绘制（mOptimizeDrawing=true）时生效
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
     * 把item从View画布中移除并绘制在涂鸦图片上. 对应 {@link #notifyItemFinishedDrawing(IDoodleItem)}
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
     * 保存, 回调DoodleListener.onSaved()的线程和调用save()的线程相同
     */
//    @SuppressLint("StaticFieldLeak")
    @Override
    public void save() {
        if (mIsSaving) {
            return;
        }

        mIsSaving = true;

//            //获取当前主线程的EventRunner，用来存放事件队列，用EventRunner其他方法会报错
        EventRunner eventRunner = EventRunner.current();
        if (eventRunner == null) {
            LogUtil.d(TAG, "save---当前线程未获取到");
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
            LogUtil.d(TAG, "processEvent()---事件Id" + event.eventId);
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
     * 清屏
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
     * 撤销
     *
     * @return 是否撤销成功
     */
    public boolean undo() {
        return undo(1);
    }

    /**
     * 只绘制原图
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
     * 设置画笔底色
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
     * 围绕某个点缩放
     * 图片真实的缩放倍数为 mCenterScale*mScale
     *
     * @param scale
     * @param pivotX 缩放的中心点
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

        // 缩放后，偏移图片，以产生围绕某个点缩放的效果
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
     * 设置画笔
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
     * 设置画笔形状
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
     * 设置图片G偏移
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
     * 触摸时，图片区域外是否绘制涂鸦轨迹
     *
     * @param isDrawableOutside
     */
    @Override
    public void setIsDrawableOutside(boolean isDrawableOutside) {
        mIsDrawableOutside = isDrawableOutside;
    }

    /**
     * 触摸时，图片区域外是否绘制涂鸦轨迹
     *
     * @return 是否绘制涂鸦轨迹
     */
    @Override
    public boolean isDrawableOutside() {
        return mIsDrawableOutside;
    }

    /**
     * 设置放大镜的倍数，当小于等于0时表示不使用放大器功能
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
     * 设置是否开启放大镜
     *
     * @param enable
     */
    public void enableZoomer(boolean enable) {
        mEnableZoomer = enable;
    }

    /**
     * 是否开启放大镜
     *
     * @return boolean
     */
    public boolean isEnableZoomer() {
        return mEnableZoomer;
    }

    /**
     * 设置是否开启全图预览功能，开启后可以在放大镜功能下显示全图涂鸦
     *
     * @param enableOverview
     */
    public void enableOverview(boolean enableOverview) {
        mEnableOverview = enableOverview;
    }

    /**
     * 是否开启全图预览功能
     *
     * @return boolean
     */
    public boolean isEnableOverview() {
        return mEnableOverview;
    }

    /**
     * 是否正在滚动涂鸦，只要用于标志触摸时才显示放大镜
     *
     * @return boolean
     */
    public boolean isScrollingDoodle() {
        return mIsScrollingDoodle;
    }

    /**
     * setScrollingDoodle
     *
     * @param scrollingDoodle 是否正在滚动，即是否发生滑动事件。用于放大镜的显示，放大镜只在滑动时显示
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
     * 是否为编辑模式
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
     * 背景图层，只在背景发生变化时绘制， 用于绘制原始图片或在优化绘制时的非编辑状态的item
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

            // 画布和图片共用一个坐标系，只需要处理屏幕坐标系到图片（画布）坐标系的映射关系
            canvas.translate(left, top); // 偏移画布
            float scale = getAllScale();
            canvas.scale(scale, scale); // 缩放画布
            if (isJustDrawOriginal) { // 只绘制原图
                canvas.drawPixelMapHolder(new PixelMapHolder(mPixelMap), 0, 0, new Paint());
                return;
            }

            PixelMap pixelMap = mOptimizeDrawing ? mDoodlePixelMap : mPixelMap;

            // 绘制涂鸦后的图片
            canvas.drawPixelMapHolder(new PixelMapHolder(pixelMap), 0, 0, new Paint());
        }
    }

    /**
     * 前景图层，每次刷新都会绘制，用于绘制正在创建或选中的item
     *
     * @since 2021-04-29
     */
    private class ForegroundView extends Component {
        public ForegroundView(Context context) {
            super(context);

            // 关闭硬件加速，某些绘图操作不支持硬件加速
//            setLayerType(LAYER_TYPE_SOFTWARE, null); 无对应api
            addDrawTask(new DrawTask() {
                @Override
                public void onDraw(Component component, Canvas canvas) {
                    onDrawInner(canvas);
                }
            });
        }

        public boolean onTouchEvent(TouchEvent event) {
            // 綁定的识别器
            IDoodleTouchDetector detector = mTouchDetectorMap.get(mPen);
            if (detector != null) {
                return detector.onTouchEvent(event);
            }
            // 默认识别器
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
            if (isJustDrawOriginal) { // 只绘制原图
                return;
            }

            float left = getAllTranX();
            float top = getAllTranY();

            // 画布和图片共用一个坐标系，只需要处理屏幕坐标系到图片（画布）坐标系的映射关系
            canvas.translate(left, top); // 偏移画布
            float scale = getAllScale();
            canvas.scale(scale, scale); // 缩放画布

            PixelMap pixelMap = mOptimizeDrawing ? mDoodlePixelMap : mPixelMap;

            int saveCount = canvas.save(); // 1
            List<IDoodleItem> items = mItemStack;
            if (mOptimizeDrawing) {
                items = mItemStackOnViewCanvas;
            }
            boolean canvasClipped = false;
            if (!mIsDrawableOutside) { // 裁剪绘制区域为图片区域
                canvasClipped = true;
                if (pixelMap != null) {
                    canvas.clipRect(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
                }
            }
            for (IDoodleItem item : items) {
                if (!item.isNeedClipOutside()) { // 1.不需要裁剪
                    if (canvasClipped) {
                        canvas.restore();
                    }

                    item.draw(canvas, pixelMap);

                    if (canvasClipped) { // 2.恢复裁剪
                        canvas.save();
                        canvas.clipRect(0, 0, pixelMap.getImageInfo().size.width, pixelMap.getImageInfo().size.height);
                    }
                } else {
                    item.draw(canvas, pixelMap);
                }
            }

            // draw at the top
            for (IDoodleItem item : items) {
                if (!item.isNeedClipOutside()) { // 1.不需要裁剪
                    if (canvasClipped) {
                        canvas.restore();
                    }
                    item.drawAtTheTop(canvas);

                    if (canvasClipped) { // 2.恢复裁剪
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
