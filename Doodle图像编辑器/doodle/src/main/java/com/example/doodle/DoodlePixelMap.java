package com.example.doodle;

import com.example.doodle.core.IDoodle;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

/**
 * 图片item
 *
 * @since 2021-04-29
 */
public class DoodlePixelMap extends DoodleRotatableItemBase {
    private PixelMap mPixelMap;
    private Rect mRect = new Rect();
    private RectFloat mSrcRect = new RectFloat();
    private RectFloat mDstRect = new RectFloat();

    /**
     * 初始化
     * @param doodle
     * @param PixelMap
     * @param size
     * @param x
     * @param y
     */
    public DoodlePixelMap(IDoodle doodle, PixelMap PixelMap, float size, float x, float y) {
        super(doodle, -doodle.getDoodleRotation(), x, y); // 设置item旋转角度，使其在当前状态下显示为“无旋转”效果
        setPen(DoodlePen.BITMAP);
        setPivotX(x);
        setPivotY(y);
        this.mPixelMap = PixelMap;
        setSize(size);
        setLocation(x, y);
    }

    /**
     * 设置图片
     * @param PixelMap
     */
    public void setPixelMap(PixelMap PixelMap) {
        mPixelMap = PixelMap;
        resetBounds(mRect);
        setPivotX(getLocation().getPointX() + mRect.getWidth() / 2);
        setPivotY(getLocation().getPointY() + mRect.getHeight() / 2);
        resetBoundsScaled(getBounds());

        refresh();
    }

    /**
     * 获取图片
     * @return PixelMap
     */
    public PixelMap getPixelMap() {
        return mPixelMap;
    }

    @Override
    public void resetBounds(Rect rect) {
        if (mPixelMap == null) {
            return;
        }
        float size = getSize();
        rect.set(0, 0, (int) size, (int) (size * mPixelMap.getImageInfo().size.height / mPixelMap.getImageInfo().size.width));

        mSrcRect.fuse(0, 0, mPixelMap.getImageInfo().size.width, mPixelMap.getImageInfo().size.height);
        mDstRect.fuse(0, 0, (int) size, (int) (size * mPixelMap.getImageInfo().size.height) / mPixelMap.getImageInfo().size.width);
    }

    @Override
    public void doDraw(Canvas canvas) {
        canvas.drawPixelMapHolderRect(new PixelMapHolder(mPixelMap), mSrcRect, mDstRect, new Paint());
    }

}


