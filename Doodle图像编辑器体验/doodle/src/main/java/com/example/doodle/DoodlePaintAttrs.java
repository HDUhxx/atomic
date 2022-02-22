package com.example.doodle;

import com.example.doodle.core.IDoodleColor;
import com.example.doodle.core.IDoodlePen;
import com.example.doodle.core.IDoodleShape;

/**
 * 画笔属性
 *
 * @since 2021-04-29
 */
public class DoodlePaintAttrs {
    private IDoodlePen mPen; // 画笔类型
    private IDoodleShape mShape; // 画笔形状
    private float mSize; // 大小
    private IDoodleColor mColor; // 颜色

    /**
     * pen
     *
     * @return IDoodlePen
     */
    public IDoodlePen pen() {
        return mPen;
    }

    /**
     * 初始化
     *
     * @param pen
     * @return DoodlePaintAttrs
     */
    public DoodlePaintAttrs pen(IDoodlePen pen) {
        mPen = pen;
        return this;
    }

    /**
     * shape
     *
     * @return IDoodleShape
     */
    public IDoodleShape shape() {
        return mShape;
    }

    /**
     * shape
     *
     * @param shape
     * @return DoodlePaintAttrs
     */
    public DoodlePaintAttrs shape(IDoodleShape shape) {
        mShape = shape;
        return this;
    }

    /**
     * size
     *
     * @return float
     */
    public float size() {
        return mSize;
    }

    /**
     * size
     *
     * @param size
     * @return DoodlePaintAttrs
     */
    public DoodlePaintAttrs size(float size) {
        mSize = size;
        return this;
    }

    /**
     * color
     *
     * @return IDoodleColor
     */
    public IDoodleColor color() {
        return mColor;
    }

    /**
     * color
     *
     * @param color
     * @return DoodlePaintAttrs
     */
    public DoodlePaintAttrs color(IDoodleColor color) {
        mColor = color;
        return this;
    }

    /**
     * create
     *
     * @return DoodlePaintAttrs
     */
    public static DoodlePaintAttrs create() {
        return new DoodlePaintAttrs();
    }
}
