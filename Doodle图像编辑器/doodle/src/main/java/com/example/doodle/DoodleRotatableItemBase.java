package com.example.doodle;

import com.example.doodle.core.IDoodle;
import com.example.doodle.util.DrawUtil;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;

/**
 * 可旋转的item
 *
 * @since 2021-04-29
 */
public abstract class DoodleRotatableItemBase extends DoodleSelectableItemBase {

    private Point mTemp = new Point();
    private Rect mRectTemp = new Rect();
    private boolean mIsRotating = false;
    private Paint mPaint = new Paint();

    /**
     * 初始化
     *
     * @param doodle
     * @param itemRotate
     * @param x
     * @param y
     */
    public DoodleRotatableItemBase(IDoodle doodle, int itemRotate, float x, float y) {
        super(doodle, itemRotate, x, y);
    }

    /**
     * 初始化
     *
     * @param doodle
     * @param attrs
     * @param itemRotate
     * @param x
     * @param y
     */
    public DoodleRotatableItemBase(IDoodle doodle, DoodlePaintAttrs attrs, int itemRotate, float x, float y) {
        super(doodle, attrs, itemRotate, x, y);
    }

    @Override
    public void doDrawAtTheTop(Canvas canvas) {
        if (isSelected()) {

            // 反向缩放画布，使视觉上选中边框不随图片缩放而变化
            int count = canvas.save();
            canvas.scale(1 / getDoodle().getDoodleScale(), 1 / getDoodle().getDoodleScale(), getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY());
            mRectTemp = new Rect(getBounds());
            DrawUtil.scaleRect(mRectTemp, getDoodle().getDoodleScale(), getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY());

            float unit = getDoodle().getUnitSize();
            mRectTemp.left -= ITEM_PADDING * unit;
            mRectTemp.top -= ITEM_PADDING * unit;
            mRectTemp.right += ITEM_PADDING * unit;
            mRectTemp.bottom += ITEM_PADDING * unit;
            mPaint.setShader(null, Paint.ShaderType.LINEAR_SHADER);
            mPaint.setColor(new Color(0x00888888));
            mPaint.setStyle(Paint.Style.FILL_STYLE);
            mPaint.setStrokeWidth(1);
            canvas.drawRect(mRectTemp, mPaint);

            // border
            if (isRotating()) {
                mPaint.setColor(new Color(0x88ffd700));
            } else {
                mPaint.setColor(new Color(0x88ffffff));
            }
            mPaint.setStyle(Paint.Style.STROKE_STYLE);
            mPaint.setStrokeWidth(2 * unit);
            canvas.drawRect(mRectTemp, mPaint);
            // border line
            mPaint.setColor(new Color(0x44888888));
            mPaint.setStrokeWidth(0.8f * unit);
            canvas.drawRect(mRectTemp, mPaint);

            // rotation
            if (isRotating()) {
                mPaint.setColor(new Color(0x88ffd700));
            } else {
                mPaint.setColor(new Color(0x88ffffff));
            }
            mPaint.setStyle(Paint.Style.STROKE_STYLE);
            mPaint.setStrokeWidth(2 * unit);
            canvas.drawLine(new Point(mRectTemp.right, mRectTemp.top + mRectTemp.getHeight() / 2), new Point(mRectTemp.right + (DoodleSelectableItemBase.ITEM_CAN_ROTATE_BOUND - 16) * unit, mRectTemp.top + mRectTemp.getHeight() / 2)
                , mPaint);
            canvas.drawCircle(mRectTemp.right + (DoodleSelectableItemBase.ITEM_CAN_ROTATE_BOUND - 8) * unit, mRectTemp.top + mRectTemp.getHeight() / 2, 8 * unit, mPaint);
            // rotation line
            mPaint.setColor(new Color(0x44888888));
            mPaint.setStrokeWidth(0.8f * unit);
            canvas.drawLine(new Point(mRectTemp.right, mRectTemp.top + mRectTemp.getHeight() / 2), new Point(mRectTemp.right + (DoodleSelectableItemBase.ITEM_CAN_ROTATE_BOUND - 16) * unit, mRectTemp.top + mRectTemp.getHeight() / 2)
                , mPaint);
            canvas.drawCircle(mRectTemp.right + (DoodleSelectableItemBase.ITEM_CAN_ROTATE_BOUND - 8) * unit, mRectTemp.top + mRectTemp.getHeight() / 2, 8 * unit, mPaint);


            // pivot
            mPaint.setColor(new Color(0xffffffff));
            mPaint.setStrokeWidth(1f * unit);
            mPaint.setStyle(Paint.Style.STROKE_STYLE);
            // +
            int length = 3;
            canvas.drawLine(new Point(getPivotX() - getLocation().getPointX() - length * unit, getPivotY() - getLocation().getPointY()), new Point(getPivotX() - getLocation().getPointX() + length * unit, getPivotY() - getLocation().getPointY()), mPaint);
            canvas.drawLine(new Point(getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY() - length * unit), new Point(getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY() + length * unit), mPaint);
            mPaint.setStrokeWidth(0.5f * unit);
            mPaint.setColor(new Color(0xff888888));
            canvas.drawLine(new Point(getPivotX() - getLocation().getPointX() - length * unit, getPivotY() - getLocation().getPointY()), new Point(getPivotX() - getLocation().getPointX() + length * unit, getPivotY() - getLocation().getPointY()), mPaint);
            canvas.drawLine(new Point(getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY() - length * unit), new Point(getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY() + length * unit), mPaint);
            mPaint.setStrokeWidth(1f * unit);
            mPaint.setStyle(Paint.Style.FILL_STYLE);
            mPaint.setColor(new Color(0xffffffff));
            canvas.drawCircle(getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY(), unit, mPaint);

            canvas.restoreToCount(count);
        }
    }

    /**
     * 是否可以旋转
     *
     * @param x
     * @param y
     * @return boolean
     */
    public boolean canRotate(float x, float y) {
        IDoodle doodle = getDoodle();
        Point location = getLocation();
        // 把触摸点转换成在item坐标系（即以item起始点作为坐标原点）内的点
        x = x - location.getPointX();
        y = y - location.getPointY();
        // 把变换后矩形中的触摸点，还原回未变换前矩形中的点，然后判断是否矩形中
        Point xy = DrawUtil.rotatePoint(mTemp, (int) -getItemRotate(), x, y, getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY());

        // 计算旋转把柄的位置，由于绘制时反向缩放了画布，所以这里也应算上相应的getDoodle().getDoodleScale()
        mRectTemp = new Rect(getBounds());
        float padding = 13 * getDoodle().getUnitSize() / getDoodle().getDoodleScale();
        mRectTemp.top -= padding;
        mRectTemp.right += padding;
        mRectTemp.bottom += padding;
        return xy.getPointX() >= mRectTemp.right
            && xy.getPointX() <= mRectTemp.right + ITEM_CAN_ROTATE_BOUND * doodle.getUnitSize() / getDoodle().getDoodleScale()
            && xy.getPointY() >= mRectTemp.top
            && xy.getPointY() <= mRectTemp.bottom;
    }

    public boolean isRotating() {
        return mIsRotating;
    }

    public void setIsRotating(boolean isRotating) {
        mIsRotating = isRotating;
    }
}
