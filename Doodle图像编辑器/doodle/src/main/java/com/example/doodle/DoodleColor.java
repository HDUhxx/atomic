package com.example.doodle;

import com.example.doodle.core.IDoodleColor;
import com.example.doodle.core.IDoodleItem;

import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.PixelMapShader;
import ohos.agp.render.Shader;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.media.image.PixelMap;

/**
 * 涂鸦画笔颜色，用于手绘
 *
 * @since 2021-04-29
 */
public class DoodleColor implements IDoodleColor {
    /**
     * Type
     *
     * @since 2021-04-29
     */
    public enum Type {
        /**
         * 颜色值
         */
        COLOR,
        /**
         * 图片
         */
        BITMAP
    }

    private int mColor;
    private PixelMap mPixelMap;
    private Type mType;
    private Matrix mMatrix;

    private int mLevel = 1;

    // bitmap相关
    private Shader.TileMode mTileX = Shader.TileMode.MIRROR_TILEMODE;
    private Shader.TileMode mTileY = Shader.TileMode.MIRROR_TILEMODE;  // 镜像

    /**
     * 初始化
     *
     * @param color
     */
    public DoodleColor(int color) {
        mType = Type.COLOR;
        mColor = color;
    }

    /**
     * 初始化
     *
     * @param pixelMap
     */
    public DoodleColor(PixelMap pixelMap) {
        this(pixelMap, null);
    }

    /**
     * 初始化
     *
     * @param bitmap
     * @param matrix
     */
    public DoodleColor(PixelMap bitmap, Matrix matrix) {
        this(bitmap, matrix, Shader.TileMode.MIRROR_TILEMODE, Shader.TileMode.MIRROR_TILEMODE);
    }

    /**
     * 初始化
     *
     * @param pixelMap
     * @param matrix
     * @param tileX
     * @param tileY
     */
    public DoodleColor(PixelMap pixelMap, Matrix matrix, Shader.TileMode tileX, Shader.TileMode tileY) {
        mType = Type.BITMAP;
        mMatrix = matrix;
        mPixelMap = pixelMap;
        mTileX = tileX;
        mTileY = tileY;
    }

    @Override
    public void config(IDoodleItem item, Paint paint) {
//        DoodleItemBase doodleItem = (DoodleItemBase) item;
        if (mType == Type.COLOR) {
            paint.setColor(new Color(mColor));
            paint.setShader(null, Paint.ShaderType.LINEAR_SHADER);
        } else if (mType == Type.BITMAP) {
            PixelMapShader shader = new PixelMapShader(new PixelMapHolder(mPixelMap), mTileX, mTileY);
            shader.setShaderMatrix(mMatrix);
            paint.setShader(shader, Paint.ShaderType.LINEAR_SHADER);
        }
    }

    /**
     * setColor
     *
     * @param color
     */
    public void setColor(int color) {
        mType = Type.COLOR;
        mColor = color;
    }

    /**
     * setColor
     *
     * @param bitmap
     */
    public void setColor(PixelMap bitmap) {
        mType = Type.BITMAP;
        mPixelMap = bitmap;
    }

    /**
     * setColor
     *
     * @param bitmap
     * @param matrix
     */
    public void setColor(PixelMap bitmap, Matrix matrix) {
        mType = Type.BITMAP;
        mMatrix = matrix;
        mPixelMap = bitmap;
    }

    /**
     * setColor
     *
     * @param bitmap
     * @param matrix
     * @param tileX
     * @param tileY
     */
    public void setColor(PixelMap bitmap, Matrix matrix, Shader.TileMode tileX, Shader.TileMode tileY) {
        mType = Type.BITMAP;
        mPixelMap = bitmap;
        mMatrix = matrix;
        mTileX = tileX;
        mTileY = tileY;
    }

    /**
     * setMatrix
     *
     * @param matrix
     */
    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    /**
     * getMatrix
     *
     * @return Matrix
     */
    public Matrix getMatrix() {
        return mMatrix;
    }

    /**
     * getColor
     *
     * @return int
     */
    public int getColor() {
        return mColor;
    }

    /**
     * getBitmap
     *
     * @return PixelMap
     */
    public PixelMap getBitmap() {
        return mPixelMap;
    }

    /**
     * getType
     *
     * @return Type
     */
    public Type getType() {
        return mType;
    }

    @Override
    public IDoodleColor copy() {
        DoodleColor color = null;
        if (mType == Type.COLOR) {
            color = new DoodleColor(mColor);
        } else {
            color = new DoodleColor(mPixelMap);
        }
        color.mTileX = mTileX;
        color.mTileY = mTileY;
        color.mMatrix = new Matrix(mMatrix);
        color.mLevel = mLevel;
        return color;
    }

    /**
     * setLevel
     *
     * @param level
     */
    public void setLevel(int level) {
        mLevel = level;
    }

    /**
     * getLevel
     *
     * @return int
     */
    public int getLevel() {
        return mLevel;
    }
}


