package com.example.doodle;

import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleSelectableItem;
import com.example.doodle.util.DrawUtil;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;

/**
 * 可选择的涂鸦item，例如文字、图片
 *
 * @since 2021-04-29
 */
public abstract class DoodleSelectableItemBase extends DoodleItemBase implements IDoodleSelectableItem {

    /**
     * ITEM_CAN_ROTATE_BOUND
     */
    public final static int ITEM_CAN_ROTATE_BOUND = 35;
    /**
     * 绘制item矩形区域时增加的padding
     */
    public final static int ITEM_PADDING = 3; // 绘制item矩形区域时增加的paddin

    private Rect mRect = new Rect();
    private Rect mRectTemp = new Rect();
    private Paint mPaint = new Paint();

    private Point mTemp = new Point();
    private boolean mIsSelected = false;

    /**
     * 初始化
     *
     * @param doodle
     * @param itemRotate
     * @param x
     * @param y
     */
    public DoodleSelectableItemBase(IDoodle doodle, int itemRotate, float x, float y) {
        this(doodle, null, itemRotate, x, y);
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
    public DoodleSelectableItemBase(IDoodle doodle, DoodlePaintAttrs attrs, int itemRotate, float x, float y) {
        super(doodle, attrs);
        setLocation(x, y);
        setItemRotate(itemRotate);

        resetBoundsScaled(mRect);
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        resetBoundsScaled(mRect);
        refresh();
    }

    @Override
    public Rect getBounds() {
        return mRect;
    }


    @Override
    public void setSize(float size) {
        super.setSize(size);
        resetBounds(getBounds());
        setLocation(getPivotX() - getBounds().getWidth() / 2, getPivotY() - getBounds().getHeight() / 2,
            false);
        resetBoundsScaled(getBounds());
    }

    /**
     * 判断是否包含
     *
     * @param x
     * @param y
     * @return 是否包含
     */
    @Override
    public boolean contains(float x, float y) {
        resetBoundsScaled(mRect);
        Point location = getLocation();
        // 把触摸点转换成在文字坐标系（即以文字起始点作为坐标原点）内的点
        x = x - location.getPointX();
        y = y - location.getPointY();
        // 把变换后相对于矩形的触摸点，还原回未变换前的点，然后判断是否矩形中
        mTemp = DrawUtil.rotatePoint(mTemp, (int) -getItemRotate(), x, y, getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY());
        mRectTemp = new Rect(mRect);
        float unit = getDoodle().getUnitSize();
        mRectTemp.left -= ITEM_PADDING * unit;
        mRectTemp.top -= ITEM_PADDING * unit;
        mRectTemp.right += ITEM_PADDING * unit;
        mRectTemp.bottom += ITEM_PADDING * unit;
        return mRectTemp.isInclude((int) mTemp.getPointX(), (int) mTemp.getPointY());
    }

    @Override
    public void drawBefore(Canvas canvas) {

    }

    @Override
    public void drawAfter(Canvas canvas) {

    }

    @Override
    public void drawAtTheTop(Canvas canvas) {
        int count = canvas.save();
        Point location = getLocation(); // 获取旋转后的起始坐标
        canvas.translate(location.getPointX(), location.getPointY()); // 把坐标系平移到item矩形范围
        canvas.rotate(getItemRotate(), getPivotX() - getLocation().getPointX(), getPivotY() - getLocation().getPointY()); // 旋转坐标系

        doDrawAtTheTop(canvas);

        canvas.restoreToCount(count);
    }

    /**
     * doDrawAtTheTop
     *
     * @param canvas
     */
    public void doDrawAtTheTop(Canvas canvas) {
        if (isSelected()) { // 选中时的效果，在最上面，避免被其他内容遮住

            // 反向缩放画布，使视觉上选中边框不随图片缩放而变化
            canvas.save();
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
            mPaint.setColor(new Color(0x88ffffff));
            mPaint.setStyle(Paint.Style.STROKE_STYLE);
            mPaint.setStrokeWidth(2 * unit);
            canvas.drawRect(mRectTemp, mPaint);
            // border line
            mPaint.setColor(new Color(0x44888888));
            mPaint.setStrokeWidth(0.8f * unit);
            canvas.drawRect(mRectTemp, mPaint);

            canvas.restore();
        }
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        mIsSelected = isSelected;
        setNeedClipOutside(!isSelected);
        refresh();
    }

    /**
     * resetBoundsScaled
     *
     * @param rect
     */
    protected void resetBoundsScaled(Rect rect) {
        resetBounds(rect);
        float px = getPivotX() - getLocation().getPointX();
        float py = getPivotY() - getLocation().getPointY();
        DrawUtil.scaleRect(rect, getScale(), px, py);
    }

    /**
     * resetBounds
     *
     * @param rect bounds for the item, start with (0,0)
     */
    protected abstract void resetBounds(Rect rect);

    @Override
    public boolean isDoodleEditable() {
        return true;
    }
}
