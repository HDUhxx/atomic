package com.lxj.xpopup.widget;

import com.lxj.xpopup.util.MyPath;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import ohos.agp.render.BlendMode;
import ohos.agp.render.BlurDrawLooper;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.util.Optional;

/**
 * 气泡布局
 * Created by JiajiXu on 17-12-1.
 */

public class BubbleLayout extends StackLayout implements Component.DrawTask {

    private Paint mPaint;
    private MyPath mPath;
    private Look mLook;
    private int mBubblePadding;
    private int mWidth = 0, mHeight = 0;
    private int mLeft, mTop, mRight, mBottom;
    private int mLookPosition, mLookWidth, mLookLength;
    private int mShadowColor, mShadowRadius, mShadowX, mShadowY;
    private int mBubbleRadius, mBubbleColor;
    // 坐上弧度，右上弧度，右下弧度，左下弧度
    private int mLTR, mRTR, mRDR, mLDR = -1;
    // 箭头弧度
    // 箭头尖分左右两个弧度分别是由 mArrowTopLeftRadius, mArrowTopRightRadius 控制
    // 箭头底部左右两个弧度分别是由 mArrowDownLeftRadius, mArrowDownRightRadius 控制
    private int mArrowTopLeftRadius, mArrowTopRightRadius, mArrowDownLeftRadius, mArrowDownRightRadius;

    // 气泡背景图
    private PixelMap mBubbleImageBg = null;
    // 气泡背景显示区域
    private RectFloat mBubbleImageBgDstRectF = new RectFloat();
    private RectFloat mBubbleImageBgSrcRect = new RectFloat();
    private Paint mBubbleImageBgPaint;
    private Paint mBubbleImageBgBeforePaint;

    // 气泡边框颜色
    private int mBubbleBorderColor = Color.BLACK.getValue();
    // 气泡边框大小
    private int mBubbleBorderSize = 0;
    // 气泡边框画笔
    private Paint mBubbleBorderPaint;

    /**
     * 箭头指向
     */
    public enum Look {
        /**
         * 坐上右下
         */
        LEFT(1), TOP(2), RIGHT(3), BOTTOM(4);
        int value;

        Look(int v) {
            value = v;
        }

        public static Look getType(int value) {
            Look type = Look.BOTTOM;
            switch (value) {
                case 1:
                    type = Look.LEFT;
                    break;
                case 2:
                    type = Look.TOP;
                    break;
                case 3:
                    type = Look.RIGHT;
                    break;
                case 4:
                    type = Look.BOTTOM;
                    break;
            }
            return type;
        }
    }

    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttrSet attrs) {
        this(context, attrs, null);
    }

    public BubbleLayout(Context context, AttrSet attrs, String styleName) {
        super(context, attrs, styleName);
        initAttr();
        initPaint();
        addDrawTask(this, BETWEEN_BACKGROUND_AND_CONTENT);
        mPath = new MyPath();
    }

    /**
     * 初始化参数
     */
    private void initAttr() {
        mLook = Look.BOTTOM;
        mLookPosition = 0;
        mLookWidth = XPopupUtils.vp2px(getContext(), 10f);
        mLookLength = XPopupUtils.vp2px(getContext(), 9f);
        mShadowRadius = 0;
        mShadowX = 0;
        mShadowY = 0;

        mBubbleRadius = XPopupUtils.vp2px(getContext(), 8);
        mLTR = -1;
        mRTR = -1;
        mRDR = -1;
        mLDR = -1;

        mArrowTopLeftRadius = XPopupUtils.vp2px(getContext(), 3f);
        mArrowTopRightRadius = XPopupUtils.vp2px(getContext(), 3f);
        mArrowDownLeftRadius = XPopupUtils.vp2px(getContext(), 6f);
        mArrowDownRightRadius = XPopupUtils.vp2px(getContext(), 6f);

        mBubblePadding = XPopupUtils.vp2px(getContext(), 4f);
        mShadowColor = Color.DKGRAY.getValue();
        mBubbleColor = Color.getIntColor("#3b3c3d");

        mBubbleBorderColor = Color.TRANSPARENT.getValue();
        mBubbleBorderSize = 0;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_STYLE);
        mBubbleImageBgPaint = new Paint();
        mBubbleImageBgPaint.setAntiAlias(true);
        mBubbleImageBgPaint.setBlendMode(BlendMode.SRC_IN);
        mBubbleImageBgBeforePaint = new Paint();
        mBubbleImageBgBeforePaint.setAntiAlias(true);
        mBubbleBorderPaint = new Paint();
        mBubbleBorderPaint.setAntiAlias(true);
    }

    protected void onSizeChanged(int Width, int height) {
        mWidth = Width;
        mHeight = height;
        initData();
    }

    @Override
    public void invalidate() {
        initData();
        super.invalidate();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initPadding();
        if (isLookPositionCenter) {
            mLookPosition = (mLook == Look.LEFT || mLook == Look.RIGHT) ?
                    (mHeight / 2 - mLookLength / 2) : (mWidth / 2 - mLookWidth / 2);
        }
        mPaint.setBlurDrawLooper(new BlurDrawLooper(mShadowRadius, mShadowX, mShadowY, new Color(mShadowColor)));
        mBubbleBorderPaint.setColor(new Color(mBubbleBorderColor));
        mBubbleBorderPaint.setStrokeWidth(mBubbleBorderSize);
        mBubbleBorderPaint.setStyle(Paint.Style.STROKE_STYLE);

        mLeft = mShadowRadius + (mShadowX < 0 ? -mShadowX : 0) + (mLook == Look.LEFT ? mLookLength : 0);
        mTop = mShadowRadius + (mShadowY < 0 ? -mShadowY : 0) + (mLook == Look.TOP ? mLookLength : 0);
        mRight = mWidth - mShadowRadius + (mShadowX > 0 ? -mShadowX : 0) - (mLook == Look.RIGHT ? mLookLength : 0);
        mBottom = mHeight - mShadowRadius + (mShadowY > 0 ? -mShadowY : 0) - (mLook == Look.BOTTOM ? mLookLength : 0);
        mPaint.setColor(new Color(mBubbleColor));

        mPath.reset();

        int topOffset = (topOffset = mLookPosition) + mLookLength > mBottom ? mBottom - mLookWidth : topOffset;
        topOffset = Math.max(topOffset, mShadowRadius);
        int leftOffset = (leftOffset = mLookPosition) + mLookLength > mRight ? mRight - mLookWidth : leftOffset;
        leftOffset = Math.max(leftOffset, mShadowRadius);

        switch (mLook) {
            case LEFT:
                // 判断是否足够画箭头，偏移的量 > 气泡圆角 + 气泡箭头下右圆弧
                if (topOffset >= getLTR() + mArrowDownRightRadius) {
                    mPath.moveTo(mLeft, topOffset - mArrowDownRightRadius);
                    mPath.rCubicTo(0F, mArrowDownRightRadius,
                            -mLookLength, mLookWidth / 2F - mArrowTopRightRadius + mArrowDownRightRadius,
                            -mLookLength, mLookWidth / 2F + mArrowDownRightRadius);
                } else {
                    // 起点移动到箭头尖
                    mPath.moveTo(mLeft - mLookLength, topOffset + mLookWidth / 2F);
                }

                // 判断是否足够画箭头，偏移的量 + 箭头宽 <= 气泡高 - 气泡圆角 - 气泡箭头下右圆弧
                if (topOffset + mLookWidth < mBottom - getLDR() - mArrowDownLeftRadius) {
                    mPath.rCubicTo(0F, mArrowTopLeftRadius,
                            mLookLength, mLookWidth / 2F,
                            mLookLength, mLookWidth / 2F + mArrowDownLeftRadius);
                    mPath.lineTo(mLeft, mBottom - getLDR());
                }
                mPath.quadTo(mLeft, mBottom,
                        mLeft + getLDR(), mBottom);
                mPath.lineTo(mRight - getRDR(), mBottom);
                mPath.quadTo(mRight, mBottom, mRight, mBottom - getRDR());
                mPath.lineTo(mRight, mTop + getRTR());
                mPath.quadTo(mRight, mTop, mRight - getRTR(), mTop);
                mPath.lineTo(mLeft + getLTR(), mTop);
                if (topOffset >= getLTR() + mArrowDownRightRadius) {
                    mPath.quadTo(mLeft, mTop, mLeft, mTop + getLTR());
                } else {
                    mPath.quadTo(mLeft, mTop, mLeft - mLookLength, topOffset + mLookWidth / 2F);
                }
                break;
            case TOP:
                if (leftOffset >= getLTR() + mArrowDownLeftRadius) {
                    mPath.moveTo(leftOffset - mArrowDownLeftRadius, mTop);
                    mPath.rCubicTo(mArrowDownLeftRadius, 0,
                            mLookWidth / 2F - mArrowTopLeftRadius + mArrowDownLeftRadius, -mLookLength,
                            mLookWidth / 2F + mArrowDownLeftRadius, -mLookLength);
                } else {
                    mPath.moveTo(leftOffset + mLookWidth / 2F, mTop - mLookLength);
                }

                if (leftOffset + mLookWidth < mRight - getRTR() - mArrowDownRightRadius) {
                    mPath.rCubicTo(mArrowTopRightRadius, 0F,
                            mLookWidth / 2F, mLookLength,
                            mLookWidth / 2F + mArrowDownRightRadius, mLookLength);
                    mPath.lineTo(mRight - getRTR(), mTop);
                }
                mPath.quadTo(mRight, mTop, mRight, mTop + getRTR());
                mPath.lineTo(mRight, mBottom - getRDR());
                mPath.quadTo(mRight, mBottom, mRight - getRDR(), mBottom);
                mPath.lineTo(mLeft + getLDR(), mBottom);
                mPath.quadTo(mLeft, mBottom, mLeft, mBottom - getLDR());
                mPath.lineTo(mLeft, mTop + getLTR());
                if (leftOffset >= getLTR() + mArrowDownLeftRadius) {
                    mPath.quadTo(mLeft, mTop, mLeft + getLTR(), mTop);
                } else {
                    mPath.quadTo(mLeft, mTop, leftOffset + mLookWidth / 2F, mTop - mLookLength);
                }
                break;
            case RIGHT:
                if (topOffset >= getRTR() + mArrowDownLeftRadius) {
                    mPath.moveTo(mRight, topOffset - mArrowDownLeftRadius);
                    mPath.rCubicTo(0, mArrowDownLeftRadius,
                            mLookLength, mLookWidth / 2F - mArrowTopLeftRadius + mArrowDownLeftRadius,
                            mLookLength, mLookWidth / 2F + mArrowDownLeftRadius);
                } else {
                    mPath.moveTo(mRight + mLookLength, topOffset + mLookWidth / 2F);
                }

                if (topOffset + mLookWidth < mBottom - getRDR() - mArrowDownRightRadius) {
                    mPath.rCubicTo(0F, mArrowTopRightRadius,
                            -mLookLength, mLookWidth / 2F,
                            -mLookLength, mLookWidth / 2F + mArrowDownRightRadius);
                    mPath.lineTo(mRight, mBottom - getRDR());
                }
                mPath.quadTo(mRight, mBottom,
                        mRight - getRDR(), mBottom);
                mPath.lineTo(mLeft + getLDR(), mBottom);
                mPath.quadTo(mLeft, mBottom, mLeft, mBottom - getLDR());
                mPath.lineTo(mLeft, mTop + getLTR());
                mPath.quadTo(mLeft, mTop, mLeft + getLTR(), mTop);
                mPath.lineTo(mRight - getRTR(), mTop);
                if (topOffset >= getRTR() + mArrowDownLeftRadius) {
                    mPath.quadTo(mRight, mTop, mRight, mTop + getRTR());
                } else {
                    mPath.quadTo(mRight, mTop, mRight + mLookLength, topOffset + mLookWidth / 2F);
                }
                break;
            case BOTTOM:
                if (leftOffset >= getLDR() + mArrowDownRightRadius) {
                    mPath.moveTo(leftOffset - mArrowDownRightRadius, mBottom);
                    mPath.rCubicTo(mArrowDownRightRadius, 0,
                            mLookWidth / 2F - mArrowTopRightRadius + mArrowDownRightRadius, mLookLength,
                            mLookWidth / 2F + mArrowDownRightRadius, mLookLength);
                } else {
                    mPath.moveTo(leftOffset + mLookWidth / 2F, mBottom + mLookLength);
                }

                if (leftOffset + mLookWidth < mRight - getRDR() - mArrowDownLeftRadius) {
                    mPath.rCubicTo(mArrowTopLeftRadius, 0F,
                            mLookWidth / 2F, -mLookLength,
                            mLookWidth / 2F + mArrowDownLeftRadius, -mLookLength);
                    mPath.lineTo(mRight - getRDR(), mBottom);
                }
                mPath.quadTo(mRight, mBottom, mRight, mBottom - getRDR());
                mPath.lineTo(mRight, mTop + getRTR());
                mPath.quadTo(mRight, mTop, mRight - getRTR(), mTop);
                mPath.lineTo(mLeft + getLTR(), mTop);
                mPath.quadTo(mLeft, mTop, mLeft, mTop + getLTR());
                mPath.lineTo(mLeft, mBottom - getLDR());
                if (leftOffset >= getLDR() + mArrowDownRightRadius) {
                    mPath.quadTo(mLeft, mBottom, mLeft + getLDR(), mBottom);
                } else {
                    mPath.quadTo(mLeft, mBottom, leftOffset + mLookWidth / 2F, mBottom + mLookLength);
                }
                break;
        }

        mPath.close();
    }

    public void initPadding() {
        int p = mBubblePadding + mShadowRadius;
        switch (mLook) {
            case BOTTOM:
                setPadding(p, p, p + mShadowX, mLookLength + p + mShadowY);
                break;
            case TOP:
                setPadding(p, p + mLookLength, p + mShadowX, p + mShadowY);
                break;
            case LEFT:
                setPadding(p + mLookLength, p, p + mShadowX, p + mShadowY);
                break;
            case RIGHT:
                setPadding(p, p, p + mLookLength + mShadowX, p + mShadowY);
                break;
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (mWidth != getWidth()) {
            onSizeChanged(getWidth(), getHeight());
        }
        canvas.drawPath(mPath, mPaint);

        if (mBubbleImageBg != null) {
            mPath.computeBounds(mBubbleImageBgDstRectF);
            int layer = canvas.saveLayer(mBubbleImageBgDstRectF, null);
            canvas.drawPath(mPath, mBubbleImageBgBeforePaint);

            float dstRatio = mBubbleImageBgDstRectF.getWidth() / mBubbleImageBgDstRectF.getHeight();
            float imgRatio = mBubbleImageBg.getImageInfo().size.width * 1F / mBubbleImageBg.getImageInfo().size.height;
            if (dstRatio > imgRatio) {
                final int top = (int) ((mBubbleImageBg.getImageInfo().size.height - mBubbleImageBg.getImageInfo().size.width / dstRatio) / 2);
                final int bottom = top + (int) (mBubbleImageBg.getImageInfo().size.width / dstRatio);
                mBubbleImageBgSrcRect.left = 0;
                mBubbleImageBgSrcRect.top = top;
                mBubbleImageBgSrcRect.right = mBubbleImageBg.getImageInfo().size.width;
                mBubbleImageBgSrcRect.bottom = bottom;
            } else {
                final int left = (int) ((mBubbleImageBg.getImageInfo().size.width - mBubbleImageBg.getImageInfo().size.height * dstRatio) / 2);
                final int width = left + (int) (mBubbleImageBg.getImageInfo().size.height * dstRatio);
                mBubbleImageBgSrcRect.left = left;
                mBubbleImageBgSrcRect.top = 0;
                mBubbleImageBgSrcRect.right = width;
                mBubbleImageBgSrcRect.bottom = mBubbleImageBg.getImageInfo().size.height;
            }

            canvas.drawPixelMapHolderRect(new PixelMapHolder(mBubbleImageBg), mBubbleImageBgSrcRect, mBubbleImageBgDstRectF, mBubbleImageBgPaint);
            canvas.restoreToCount(layer);
        }

        if (mBubbleBorderSize != 0) {
            canvas.drawPath(mPath, mBubbleBorderPaint);
        }
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Path getPath() {
        return mPath;
    }

    public Look getLook() {
        return mLook;
    }

    public int getLookPosition() {
        return mLookPosition;
    }

    public int getLookWidth() {
        return mLookWidth;
    }

    public int getLookLength() {
        return mLookLength;
    }

    public int getMyShadowColor() {
        return mShadowColor;
    }

    public int getShadowRadius() {
        return mShadowRadius;
    }

    public int getShadowX() {
        return mShadowX;
    }

    public int getShadowY() {
        return mShadowY;
    }

    public int getBubbleRadius() {
        return mBubbleRadius;
    }

    public int getBubbleColor() {
        return mBubbleColor;
    }

    public void setBubbleColor(int mBubbleColor) {
        this.mBubbleColor = mBubbleColor;
    }

    public void setLook(Look mLook) {
        this.mLook = mLook;
        initPadding();
    }

    public void setLookPosition(int mLookPosition) {
        this.mLookPosition = mLookPosition;
    }

    boolean isLookPositionCenter;

    public void setLookPositionCenter(boolean isCenter) {
        this.isLookPositionCenter = isCenter;
    }

    public void setLookWidth(int mLookWidth) {
        this.mLookWidth = mLookWidth;
    }

    public void setLookLength(int mLookLength) {
        this.mLookLength = mLookLength;
        initPadding();
    }

    public void setShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
    }

    public void setShadowRadius(int mShadowRadius) {
        this.mShadowRadius = mShadowRadius;
    }

    public void setShadowX(int mShadowX) {
        this.mShadowX = mShadowX;
    }

    public void setShadowY(int mShadowY) {
        this.mShadowY = mShadowY;
    }

    public void setBubbleRadius(int mBubbleRadius) {
        this.mBubbleRadius = mBubbleRadius;
    }

    public int getLTR() {
        return mLTR == -1 ? mBubbleRadius : mLTR;
    }

    public void setLTR(int mLTR) {
        this.mLTR = mLTR;
    }

    public int getRTR() {
        return mRTR == -1 ? mBubbleRadius : mRTR;
    }

    public void setRTR(int mRTR) {
        this.mRTR = mRTR;
    }

    public int getRDR() {
        return mRDR == -1 ? mBubbleRadius : mRDR;
    }

    public void setRDR(int mRDR) {
        this.mRDR = mRDR;
    }

    public int getLDR() {
        return mLDR == -1 ? mBubbleRadius : mLDR;
    }

    public void setLDR(int mLDR) {
        this.mLDR = mLDR;
    }

    public int getArrowTopLeftRadius() {
        return mArrowTopLeftRadius;
    }

    public void setArrowTopLeftRadius(int mArrowTopLeftRadius) {
        this.mArrowTopLeftRadius = mArrowTopLeftRadius;
    }

    public int getArrowTopRightRadius() {
        return mArrowTopRightRadius;
    }

    public void setArrowTopRightRadius(int mArrowTopRightRadius) {
        this.mArrowTopRightRadius = mArrowTopRightRadius;
    }

    public int getArrowDownLeftRadius() {
        return mArrowDownLeftRadius;
    }

    public void setArrowDownLeftRadius(int mArrowDownLeftRadius) {
        this.mArrowDownLeftRadius = mArrowDownLeftRadius;
    }

    public int getArrowDownRightRadius() {
        return mArrowDownRightRadius;
    }

    public void setArrowDownRightRadius(int mArrowDownRightRadius) {
        this.mArrowDownRightRadius = mArrowDownRightRadius;
    }

    public void setBubblePadding(int bubblePadding) {
        this.mBubblePadding = bubblePadding;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public void setBubbleImageBg(PixelMap bitmap) {
        mBubbleImageBg = bitmap;
    }

    /**
     * 设置背景图片资源
     *
     * @param res 图片资源
     */
    public void setBubbleImageBgRes(int res) {
        mBubbleImageBg = decodeResource(getContext(), res);
    }

    private PixelMap decodeResource(Context context, int id) {
        try {
            String path = context.getResourceManager().getMediaPath(id);
            if (path.isEmpty()) {
                return null;
            }
            RawFileEntry assetManager = context.getResourceManager().getRawFileEntry(path);
            ImageSource.SourceOptions options = new ImageSource.SourceOptions();
            options.formatHint = "image/png";
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            Resource asset = assetManager.openRawFile();
            ImageSource source = ImageSource.create(asset, options);
            return Optional.ofNullable(source.createPixelmap(decodingOptions)).get();
        } catch (Exception e) {

        }
        return null;
    }

    public void setBubbleBorderSize(int bubbleBorderSize) {
        this.mBubbleBorderSize = bubbleBorderSize;
    }

    public void setBubbleBorderColor(int bubbleBorderColor) {
        this.mBubbleBorderColor = bubbleBorderColor;
    }

}
