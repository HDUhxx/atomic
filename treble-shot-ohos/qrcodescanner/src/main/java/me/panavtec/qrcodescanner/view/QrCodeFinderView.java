package me.panavtec.qrcodescanner.view;

import me.panavtec.qrcodescanner.ResourceTable;
import me.panavtec.qrcodescanner.utils.ScreenUtils;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.*;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial transparency outside
 * it, as well as the laser scanner animation and result points.
 */
public final class QrCodeFinderView extends DependentLayout implements Component.DrawTask {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;

    private final Context mContext;
    private final Paint mPaint;
    private int mScannerAlpha;
    private final Color mMaskColor;
    private final Color mFrameColor;
    private final Color mLaserColor;
    private final Color mTextColor;
    private Rect mFrameRect;
    private final int mFocusThick;
    private final int mAngleThick;
    private final int mAngleLength;

    public QrCodeFinderView(Context context) {
        this(context, null);
    }

    public QrCodeFinderView(Context context, AttrSet attrs) {
        this(context, attrs, "");
    }

    public QrCodeFinderView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();


        mMaskColor = Color.TRANSPARENT;
        mFrameColor = Color.TRANSPARENT;
        mLaserColor = Color.GREEN;
        mTextColor = Color.WHITE;

        mFocusThick = 1;
        mAngleThick = 8;
        mAngleLength = 40;
        mScannerAlpha = 0;
        init(context);
    }

    private void init(Context context) {
        Component component = LayoutScatter.getInstance(context).parse(
                ResourceTable.Layout_qr_code_scanner, this, true);

        //DependentLayout relativeLayout = (DependentLayout) component.findComponentById(ResourceTable.Id_root);
        StackLayout frameLayout = (StackLayout) component.findComponentById(ResourceTable.Id_qr_code_fl_scanner);
        mFrameRect = new Rect();

        LayoutConfig layoutParams = (LayoutConfig) frameLayout.getLayoutConfig();

        mFrameRect.left = (ScreenUtils.getDisplayWidthInPx(context) - layoutParams.width) / 2;
        mFrameRect.top = layoutParams.getMarginTop();
        mFrameRect.right = mFrameRect.left + layoutParams.width;
        mFrameRect.bottom = mFrameRect.top + layoutParams.height;


        addDrawTask(this::onDraw);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        Rect frame = mFrameRect;
        if (frame == null) {
            return;
        }
//        int width = canvas.getWidth();
//        int height = canvas.getHeight();
        int width = vp2px(getContext(), 250);
        int height = vp2px(getContext(), 250);

        mPaint.setColor(mMaskColor);
        canvas.drawRect(0, 0, width, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, mPaint);
        canvas.drawRect(0, frame.bottom + 1, width, height, mPaint);

        drawFocusRect(canvas, frame);
        drawAngle(canvas, frame);
        drawText(canvas, frame);
        drawLaser(canvas, frame);
        initScanValueAnim();
        //postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }

    private void drawFocusRect(Canvas canvas, Rect rect) {
        mPaint.setColor(mFrameColor);
        //Up
        canvas.drawRect(rect.left + mAngleLength, rect.top, rect.right - mAngleLength, rect.top + mFocusThick, mPaint);
        //Left
        canvas.drawRect(rect.left, rect.top + mAngleLength, rect.left + mFocusThick, rect.bottom - mAngleLength,
                mPaint);
        //Right
        canvas.drawRect(rect.right - mFocusThick, rect.top + mAngleLength, rect.right, rect.bottom - mAngleLength,
                mPaint);
        //Down
        canvas.drawRect(rect.left + mAngleLength, rect.bottom - mFocusThick, rect.right - mAngleLength, rect.bottom,
                mPaint);
    }

    /**
     * Draw four purple angles
     *
     * @param canvas
     * @param rect
     */
    private void drawAngle(Canvas canvas, Rect rect) {
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(OPAQUE);
        mPaint.setStyle(Paint.Style.FILL_STYLE);
        mPaint.setStrokeWidth(mAngleThick);
        int left = rect.left;
        int top = rect.top;
        int right = rect.right;
        int bottom = rect.bottom;
        // Top left angle
        canvas.drawRect(left, top, left + mAngleLength, top + mAngleThick, mPaint);
        canvas.drawRect(left, top, left + mAngleThick, top + mAngleLength, mPaint);
        // Top right angle
        canvas.drawRect(right - mAngleLength, top, right, top + mAngleThick, mPaint);
        canvas.drawRect(right - mAngleThick, top, right, top + mAngleLength, mPaint);
        // bottom left angle
        canvas.drawRect(left, bottom - mAngleLength, left + mAngleThick, bottom, mPaint);
        canvas.drawRect(left, bottom - mAngleThick, left + mAngleLength, bottom, mPaint);
        // bottom right angle
        canvas.drawRect(right - mAngleLength, bottom - mAngleThick, right, bottom, mPaint);
        canvas.drawRect(right - mAngleThick, bottom - mAngleLength, right, bottom, mPaint);
    }

    private void drawText(Canvas canvas, Rect rect) {
        int margin = 40;
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(vp2px(getContext(), 13));
        String text = "Position QR Code";
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        float newY = rect.bottom + margin + offY;

        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(getContext()).get().getAttributes();
        float screenScale = attributes.densityPixels;

        float left = (getDisplayWidthInPx(getContext()) - (mPaint.getTextSize()) * text.length()) / 2;
        /*
            correctedLeft is hack to force the text in the middle of the width of the screen
            55 is an experimental value and it takes into account the scale of the screen.
         */
        float correctedLeft = left + (55 * screenScale);
        canvas.drawText(mPaint, text, correctedLeft, newY);
    }

    /**
     * vp转像素
     *
     * @param context 上下文
     * @param vp      vp值
     * @return int
     */
    public static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    private int getDisplayWidthInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointX();
    }

    private int i = 0;
    private int height;

    private void drawLaser(Canvas canvas, Rect rect) {
        height = rect.getHeight();
        i = i + 5;
        if (i >= height - 100) {
            i = 0;
        }
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(SCANNER_ALPHA[mScannerAlpha]);
        mScannerAlpha = (mScannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = rect.top + 50 + i;
        canvas.drawRect(rect.left + 2, middle - 1, rect.right - 1, middle + 2, mPaint);
    }

    private void initScanValueAnim() {
        AnimatorValue mValueAnimator = new AnimatorValue();
        mValueAnimator.setDuration(1000);
        mValueAnimator.setLoopedCount(AnimatorValue.INFINITE);
        mValueAnimator.setCurveType(Animator.CurveType.DECELERATE);
        mValueAnimator.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {

            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                height = (int) (height *  v);
                invalidate();
            }
        });
        mValueAnimator.start();
    }
}