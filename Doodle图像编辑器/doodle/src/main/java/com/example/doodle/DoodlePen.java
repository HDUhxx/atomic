package com.example.doodle;


import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodlePen;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;

/**
 * 常用画笔
 *
 * @since 2021-04-29
 */
public enum DoodlePen implements IDoodlePen {
    /**
     * 画笔
     */
    BRUSH,
    /**
     * 仿制
     */
    COPY,
    /**
     * 橡皮擦
     */
    ERASER,
    /**
     * 文本
     */
    TEXT,
    /**
     * 贴图
     */
    BITMAP,
    /**
     * 马赛克
     */
    MOSAIC;
    private CopyLocation mCopyLocation;

    @Override
    public void config(IDoodleItem item, Paint paint) {
        if (this == DoodlePen.COPY || this == DoodlePen.ERASER) {
            IDoodle doodle = item.getDoodle();
            if ((item.getColor() instanceof DoodleColor)
                && ((DoodleColor) item.getColor()).getBitmap() == doodle.getPixelMap()) {
                // nothing
            } else {
                item.setColor(new DoodleColor(doodle.getPixelMap()));
            }
        }
    }

    /**
     * 获取CopyLocation
     *
     * @return CopyLocation
     */
    public CopyLocation getCopyLocation() {
        if (this != COPY) {
            return null;
        }
        if (mCopyLocation == null) {
            synchronized (this) {
//                if (mCopyLocation == null) { TODO 过 findBug注释
                    mCopyLocation = new CopyLocation();
//                }
            }
        }
        return mCopyLocation;
    }

    @Override
    public IDoodlePen copy() {
        return this;
    }

    @Override
    public void drawHelpers(Canvas canvas, IDoodle doodle) {
        if (this == COPY) {
            if (doodle instanceof DoodleView && !((DoodleView) doodle).isEditMode()) {
                mCopyLocation.drawItSelf(canvas, doodle.getSize());
            }
        }
    }
}
