package com.jjoe64.graphview;

import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.Series;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;
import ohos.multimodalinput.event.TouchEvent;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonas on 22/02/2017.
 */

public class CursorMode {
    private final static class Styles {
        public float textSize;
        public int spacing;
        public int padding;
        public int width;
        public int backgroundColor;
        public int margin;
        public int textColor;
    }

    protected final Paint mPaintLine;
    protected final GraphView mGraphView;
    protected float mPosX;
    protected float mPosY;
    protected boolean mCursorVisible;
    protected final Map<BaseSeries, DataPointInterface> mCurrentSelection;
    protected final Paint mRectPaint;
    protected final Paint mTextPaint;
    protected double mCurrentSelectionX;
    protected Styles mStyles;
    protected int cachedLegendWidth;

    public CursorMode(GraphView graphView) {
        mStyles = new Styles();
        mGraphView = graphView;
        mPaintLine = new Paint();
        mPaintLine.setColor(new Color(Color.argb(128, 180, 180, 180)));
        mPaintLine.setStrokeWidth(10f);
        mCurrentSelection = new HashMap<>();
        mRectPaint = new Paint();
        mTextPaint = new Paint();
        resetStyles();
    }

    /**
     * resets the styles to the defaults
     * and clears the legend width cache
     */
    public void resetStyles() {
        mStyles.textSize = mGraphView.getGridLabelRenderer().getTextSize();
        mStyles.spacing = (int) (mStyles.textSize / 5);
        mStyles.padding = (int) (mStyles.textSize / 2);
        mStyles.width = 0;
        mStyles.backgroundColor = Color.argb(180, 100, 100, 100);
        mStyles.margin = (int) (mStyles.textSize);

        int color1;

        color1 = Color.BLACK.getValue();

        mStyles.textColor = color1;

        cachedLegendWidth = 0;
    }


    public void onDown(TouchEvent event) {
        mPosX = Math.max(event.getPointerPosition(0).getX(), mGraphView.getGraphContentLeft());
        mPosX = Math.min(mPosX, mGraphView.getGraphContentLeft() + mGraphView.getGraphContentWidth());
        mPosY = event.getPointerPosition(0).getY();
        mCursorVisible = true;
        findCurrentDataPoint();
        mGraphView.invalidate();
    }

    public void onMove(TouchEvent event) {
        if (mCursorVisible) {
            mPosX = Math.max(event.getPointerPosition(0).getX(), mGraphView.getGraphContentLeft());
            mPosX = Math.min(mPosX, mGraphView.getGraphContentLeft() + mGraphView.getGraphContentWidth());
            mPosY = event.getPointerPosition(0).getY();
            findCurrentDataPoint();
            mGraphView.invalidate();
        }
    }

    public void draw(Canvas canvas, int width, int height) {
        if (mCursorVisible) {

            canvas.drawLine(new Point(mPosX, 0), new Point(mPosX, height), mPaintLine);
        }

        // selection
        for (Map.Entry<BaseSeries, DataPointInterface> entry : mCurrentSelection.entrySet()) {
            entry.getKey().drawSelection(mGraphView, canvas, false, entry.getValue());
        }

        if (!mCurrentSelection.isEmpty()) {
            drawLegend(canvas);
        }
    }

    protected String getTextForSeries(Series s, DataPointInterface value) {
        StringBuffer txt = new StringBuffer();
        if (s.getTitle() != null) {
            txt.append(s.getTitle());
            txt.append(": ");
        }
        txt.append(mGraphView.getGridLabelRenderer().getLabelFormatter().formatLabel(value.getY(), false));
        return txt.toString();
    }

    protected void drawLegend(Canvas canvas) {
        mTextPaint.setTextSize((int) mStyles.textSize);
        mTextPaint.setColor(new Color(mStyles.textColor));

        int shapeSize = (int) (mStyles.textSize * 0.8d);

        // width
        int legendWidth = mStyles.width;
        if (legendWidth == 0) {
            // auto
            legendWidth = cachedLegendWidth;

            if (legendWidth == 0) {
//                Rect textBounds = new Rect();
                for (Map.Entry<BaseSeries, DataPointInterface> entry : mCurrentSelection.entrySet()) {
                    String txt = getTextForSeries(entry.getKey(), entry.getValue());
                    legendWidth = Math.max(legendWidth, mTextPaint.getTextBounds(txt).getWidth());
                }
                if (legendWidth == 0) legendWidth = 1;

                // add shape size
                legendWidth += shapeSize + mStyles.padding * 2 + mStyles.spacing;
                cachedLegendWidth = legendWidth;
            }
        }

        float legendPosX = mPosX - mStyles.margin - legendWidth;
        if (legendPosX < 0) {
            legendPosX = 0;
        }

        // rect
        float legendHeight = (mStyles.textSize + mStyles.spacing) * (mCurrentSelection.size() + 1) - mStyles.spacing;

        float legendPosY = mPosY - legendHeight - 4.5f * mStyles.textSize;
        if (legendPosY < 0) {
            legendPosY = 0;
        }

        float lLeft;
        float lTop;
        lLeft = legendPosX;
        lTop = legendPosY;

        float lRight = lLeft + legendWidth;
        float lBottom = lTop + legendHeight + 2 * mStyles.padding;
        mRectPaint.setColor(new Color(mStyles.backgroundColor));
        canvas.drawRoundRect(new RectFloat(lLeft, lTop, lRight, lBottom), 8, 8, mRectPaint);
        mTextPaint.setFakeBoldText(true);
        canvas.drawText(mTextPaint, mGraphView.getGridLabelRenderer().getLabelFormatter().formatLabel(mCurrentSelectionX, true), lLeft + mStyles.padding, lTop + mStyles.padding / 2 + mStyles.textSize);

        mTextPaint.setFakeBoldText(false);

        int i = 1;
        for (Map.Entry<BaseSeries, DataPointInterface> entry : mCurrentSelection.entrySet()) {
            mRectPaint.setColor(new Color(entry.getKey().getColor()));
            canvas.drawRect(new RectFloat(lLeft + mStyles.padding, lTop + mStyles.padding + (i * (mStyles.textSize + mStyles.spacing)), lLeft + mStyles.padding + shapeSize, lTop + mStyles.padding + (i * (mStyles.textSize + mStyles.spacing)) + shapeSize), mRectPaint);
            canvas.drawText(mTextPaint, getTextForSeries(entry.getKey(), entry.getValue()), lLeft + mStyles.padding + shapeSize + mStyles.spacing, lTop + mStyles.padding / 2 + mStyles.textSize + (i * (mStyles.textSize + mStyles.spacing)));
            i++;
        }
    }

    public boolean onUp(TouchEvent event) {
        mCursorVisible = false;
        findCurrentDataPoint();
        mGraphView.invalidate();
        return true;
    }

    private void findCurrentDataPoint() {
        double selX = 0;
        mCurrentSelection.clear();
        for (Series series : mGraphView.getSeries()) {
            if (series instanceof BaseSeries) {
                DataPointInterface p = ((BaseSeries) series).findDataPointAtX(mPosX);
                if (p != null) {
                    selX = p.getX();
                    mCurrentSelection.put((BaseSeries) series, p);
                }
            }
        }

        if (!mCurrentSelection.isEmpty()) {
            mCurrentSelectionX = selX;
        }
    }

    public void setTextSize(float t) {
        if (t <= 0) {
            mStyles.textSize = 20;
        } else {
            mStyles.textSize = t;
        }
    }

    public void setTextColor(int color) {
        mStyles.textColor = color;
    }

    public void setBackgroundColor(int color) {
        mStyles.backgroundColor = color;
    }

    public void setSpacing(int s) {
        mStyles.spacing = s;
    }

    public void setPadding(int s) {
        mStyles.padding = s;
    }

    public void setMargin(int s) {
        mStyles.margin = s;
    }

    public void setWidth(int s) {
        mStyles.width = s;
    }
}
