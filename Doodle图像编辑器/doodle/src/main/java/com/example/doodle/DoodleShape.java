package com.example.doodle;


import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodleShape;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;

/**
 * 常用图形
 *
 * @since 2021-04-29
 */
public enum DoodleShape implements IDoodleShape {
    /**
     * 手绘
     */
    HAND_WRITE,
    /**
     * 箭头
     */
    ARROW,
    /**
     * 直线
     */
    LINE,
    /**
     * 实心圆
     */
    FILL_CIRCLE,
    /**
     * 空心圆
     */
    HOLLOW_CIRCLE,
    /**
     * 实心矩形
     */
    FILL_RECT,
    /**
     * 空心矩形
     */
    HOLLOW_RECT;


    @Override
    public void config(IDoodleItem doodleItem, Paint paint) {
        if (doodleItem.getShape() == DoodleShape.ARROW || doodleItem.getShape() == DoodleShape.FILL_CIRCLE || doodleItem.getShape() == DoodleShape.FILL_RECT) {
            paint.setStyle(Paint.Style.FILL_STYLE);
        } else {
            paint.setStyle(Paint.Style.STROKE_STYLE);
        }
    }

    @Override
    public IDoodleShape copy() {
        return this;
    }

    @Override
    public void drawHelpers(Canvas canvas, IDoodle doodle) {

    }
}
