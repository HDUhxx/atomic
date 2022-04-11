package com.example.doodle;


import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleColor;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Rect;

/**
 * DoodleText
 *
 * @since 2021-04-29
 */
public class DoodleText extends DoodleRotatableItemBase {

    private Rect mRect = new Rect();
    private final Paint mPaint = new Paint();
    private String mText;

    /**
     * 初始化
     *
     * @param doodle
     * @param text
     * @param size
     * @param color
     * @param x
     * @param y
     */
    public DoodleText(IDoodle doodle, String text, float size, IDoodleColor color, float x, float y) {
        super(doodle, -doodle.getDoodleRotation(), x, y);
        setPen(DoodlePen.TEXT);
        mText = text;
        setSize(size);
        setColor(color);
        setLocation(x, y);
    }

    /**
     * getText
     *
     * @return String
     */
    public String getText() {
        return mText;
    }

    /**
     * setText
     *
     * @param text
     */
    public void setText(String text) {
        mText = text;
        resetBounds(mRect);
        setPivotX(getLocation().getPointX() + mRect.getWidth() / 2);
        setPivotY(getLocation().getPointY() + mRect.getHeight() / 2);
        resetBoundsScaled(getBounds());

        refresh();
    }

    @Override
    public void resetBounds(Rect rect) {
        if (mText == null || mText.length() == 0) {
            return;
        }
        mPaint.setTextSize((int) getSize());
        mPaint.setStyle(Paint.Style.FILL_STYLE);
        Rect temp = mPaint.getTextBounds(mText);
        rect.set(temp.left, temp.top, temp.right, temp.bottom);
        rect.translate(0, rect.getHeight());
    }

    @Override
    public void doDraw(Canvas canvas) {
        getColor().config(this, mPaint);
        mPaint.setTextSize((int) getSize());
        mPaint.setStyle(Paint.Style.FILL_STYLE);
        canvas.save();
        canvas.translate(0, getBounds().getHeight() / getScale());
        canvas.drawText(mPaint, mText, 0, 0);
        canvas.restore();
    }

}


