/**
 * GraphView
 * Copyright 2016 Jonas Gehring
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jjoe64.graphview;

import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.Series;

import com.jjoe64.graphview.utils.LogUtil;
import com.jjoe64.graphview.utils.PointF;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jjoe64
 */
public class GraphView extends Component implements Component.DrawTask, Component.TouchEventListener {

    public static String TAG = "GraphView : ";
    // 表格起始X坐标适配
    private int adaptationStartPoint = 20;
    // 表格总宽度的适配
    private int adaptationWidth = 40;

    @Override
    public void onDraw(Component component, Canvas canvas) {

        drawGraphElements(canvas);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        boolean b = mViewport.onTouchEvent(event);
//        boolean a = super.onTou

        // is it a click?
        if (mTapDetector.onTouchEvent(event)) {
            for (Series s : mSeries) {
                s.onTap(event.getPointerPosition(0).getX(), event.getPointerPosition(0).getY());
            }
            if (mSecondScale != null) {
                for (Series s : mSecondScale.getSeries()) {
                    s.onTap(event.getPointerPosition(0).getX(), event.getPointerPosition(0).getY());
                }
            }

        }

        return b;
    }


    /**
     * Class to wrap style options that are general
     * to graphs.
     *
     * @author jjoe64
     */
    private static final class Styles {
        /**
         * The font size of the title that can be displayed
         * above the graph.
         *
         * @see GraphView#setTitle(String)
         */
        float titleTextSize;

        /**
         * The font color of the title that can be displayed
         * above the graph.
         *
         * @see GraphView#setTitle(String)
         */
        int titleColor;
    }

    /**
     * Helper class to detect tap events on the
     * graph.
     *
     * @author jjoe64
     */
    private class TapDetector {
        /**
         * save the time of the last down event
         */
        private long lastDown;

        /**
         * point of the tap down event
         */
        private PointF lastPoint;

        /**
         * to be called to process the events
         *
         * @param event
         * @return true if there was a tap event. otherwise returns false.
         */
        public boolean onTouchEvent(TouchEvent event) {

            if (event.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
                lastDown = System.currentTimeMillis();
                lastPoint = new PointF(event.getPointerPosition(0).getX(), event.getPointerPosition(0).getY());
                LogUtil.info(TAG, event.getPointerPosition(0).getX() + "/PRIMARY_POINT_DOWN/" + event.getPointerPosition(0).getY());
            } else if (lastDown > 0 && event.getAction() == TouchEvent.POINT_MOVE) {
                if (Math.abs(event.getPointerPosition(0).getX() - lastPoint.x) > 60
                        || Math.abs(event.getPointerPosition(0).getY() - lastPoint.y) > 60) {
                    lastDown = 0;
                }
                LogUtil.info(TAG, event.getPointerPosition(0).getX() + "/POINT_MOVE/" + event.getPointerPosition(0).getY());
            } else if (event.getAction() == TouchEvent.PRIMARY_POINT_UP) {
                if (System.currentTimeMillis() - lastDown < 400) {
                    return true;
                }
                LogUtil.info(TAG, event.getPointerPosition(0).getX() + "/PRIMARY_POINT_UP/" + event.getPointerPosition(0).getY());
            }
            return false;
        }
    }

    /**
     * our series (this does not contain the series
     * that can be displayed on the right side. The
     * right side series is a special feature of
     * the {@link SecondScale} feature.
     */
    private List<Series> mSeries;

    /**
     * the renderer for the grid and labels
     */
    private GridLabelRenderer mGridLabelRenderer;

    /**
     * viewport that holds the current bounds of
     * view.
     */
    private Viewport mViewport;

    /**
     * title of the graph that will be shown above
     */
    private String mTitle;

    /**
     * wraps the general styles
     */
    private Styles mStyles;

    /**
     * feature to have a second scale e.g. on the
     * right side
     */
    protected SecondScale mSecondScale;

    /**
     * tap detector
     */
    private TapDetector mTapDetector;

    /**
     * renderer for the legend
     */
    private LegendRenderer mLegendRenderer;

    /**
     * paint for the graph title
     */
    private Paint mPaintTitle;

    private boolean mIsCursorMode;

    /**
     * paint for the preview (in the SDK)
     */
    private Paint mPreviewPaint;

    private CursorMode mCursorMode;

    /**
     * Initialize the GraphView view
     *
     * @param context
     */
    public GraphView(Context context) {
        super(context);
        init();
    }

    /**
     * Initialize the GraphView view.
     *
     * @param context
     * @param attrs
     */
    public GraphView(Context context, AttrSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialize the GraphView view
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GraphView(Context context, AttrSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * initialize the internal objects.
     * This method has to be called directly
     * in the constructors.
     */
    protected void init() {
        mPreviewPaint = new Paint();
        mPreviewPaint.setTextAlign(1);
        mPreviewPaint.setColor(Color.BLACK);
        mPreviewPaint.setTextSize(50);

        mStyles = new Styles();
        mViewport = new Viewport(this);
        mGridLabelRenderer = new GridLabelRenderer(this);
        mLegendRenderer = new LegendRenderer(this);

        mSeries = new ArrayList<Series>();
        mPaintTitle = new Paint();

        mTapDetector = new TapDetector();

        addDrawTask(this);
        setTouchEventListener(this);
        loadStyles();
    }

    /**
     * loads the font
     */
    protected void loadStyles() {
        mStyles.titleColor = mGridLabelRenderer.getHorizontalLabelsColor();
        mStyles.titleTextSize = mGridLabelRenderer.getTextSize();
    }

    /**
     * ss
     *
     * @return the renderer for the grid and labels
     */
    public GridLabelRenderer getGridLabelRenderer() {
        return mGridLabelRenderer;
    }

    /**
     * Add a new series to the graph. This will
     * automatically redraw the graph.
     *
     * @param s the series to be added
     */
    public void addSeries(Series s) {
        s.onGraphViewAttached(this);
        mSeries.add(s);
        onDataChanged(false, false);
    }

    /**
     * important: do not do modifications on the list
     *
     * @return all series
     */
    public List<Series> getSeries() {
        // TODO immutable array
        return mSeries;
    }

    /**
     * call this to let the graph redraw and
     * recalculate the viewport.
     * This will be called when a new series
     *
     * @param keepLabelsSize true if you don't want
     *                       to recalculate the size of
     *                       the labels. It is recommended
     *                       to use "true" because this will
     *                       improve performance and prevent
     *                       a flickering.
     * @param keepViewport   true if you don't want that
     *                       the viewport will be recalculated.
     *                       It is recommended to use "true" for
     *                       performance.
     */
    public void onDataChanged(boolean keepLabelsSize, boolean keepViewport) {
        // adjustSteps grid system
        mViewport.calcCompleteRange();
        if (mSecondScale != null) {
            mSecondScale.calcCompleteRange();
        }
        mGridLabelRenderer.invalidate(keepLabelsSize, keepViewport);
        invalidate();
    }

    /**
     * draw all the stuff on canvas
     *
     * @param canvas
     */
    protected void drawGraphElements(Canvas canvas) {

        drawTitle(canvas);
        mViewport.drawFirst(canvas);
        mGridLabelRenderer.draw(canvas, getWidth(), getHeight());
        for (Series s : mSeries) {
            s.draw(this, canvas, false);
        }
        if (mSecondScale != null) {
            for (Series s : mSecondScale.getSeries()) {
                s.draw(this, canvas, true);
            }
        }

        if (mCursorMode != null) {
            mCursorMode.draw(canvas, getWidth(), getHeight());
        }

        mViewport.draw(canvas);
        mLegendRenderer.draw(canvas);
    }


    /**
     * Draws the Graphs title that will be
     * shown above the viewport.
     * Will be called by GraphView.
     *
     * @param canvas Canvas
     */
    protected void drawTitle(Canvas canvas) {
        if (mTitle != null && mTitle.length() > 0) {
            mPaintTitle.setColor(new Color(mStyles.titleColor));
            mPaintTitle.setTextSize((int) (mStyles.titleTextSize >= 0 ? mStyles.titleTextSize : 20));
            mPaintTitle.setTextAlign(1);
            float x = getWidth() / 2;
            float y = mPaintTitle.getTextSize();
            canvas.drawText(mPaintTitle, mTitle, x, y);
        }
    }

    /**
     * Calculates the height of the title.
     *
     * @return the actual size of the title.
     * if there is no title, 0 will be
     * returned.
     */
    protected int getTitleHeight() {
        if (mTitle != null && mTitle.length() > 0) {
            return (int) mPaintTitle.getTextSize();
        } else {
            return 0;
        }
    }

    /**
     * ss
     *
     * @return the viewport of the Graph.
     */
    public Viewport getViewport() {
        return mViewport;
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        onDataChanged(false, false);
//        postLayout();
//    }

    /**
     * ss
     *
     * @return the space on the left side of the
     * view from the left border to the
     * beginning of the graph viewport.
     */
    public int getGraphContentLeft() {
        int border = getGridLabelRenderer().getStyles().padding;
        return border + getGridLabelRenderer().getLabelVerticalWidth() +
                getGridLabelRenderer().getVerticalAxisTitleWidth() + adaptationStartPoint;
    }

    /**
     * ss
     *
     * @return the space on the top of the
     * view from the top border to the
     * beginning of the graph viewport.
     */
    public int getGraphContentTop() {
        int border = getGridLabelRenderer().getStyles().padding + getTitleHeight();
        return border;
    }

    /**
     * ss
     *
     * @return the height of the graph viewport.
     */
    public int getGraphContentHeight() {
        int border = getGridLabelRenderer().getStyles().padding;
        int graphheight = getHeight() - (2 * border) - getGridLabelRenderer().getLabelHorizontalHeight() - getTitleHeight();
        graphheight -= getGridLabelRenderer().getHorizontalAxisTitleHeight();
        return graphheight;
    }

    /**
     * ss
     *
     * @return the width of the graph viewport.
     */
    public int getGraphContentWidth() {
        int border = getGridLabelRenderer().getStyles().padding;
        int graphwidth = getWidth() - (2 * border) - getGridLabelRenderer().getLabelVerticalWidth();
        if (mSecondScale != null) {
            graphwidth -= getGridLabelRenderer().getLabelVerticalSecondScaleWidth();
            graphwidth -= mSecondScale.getVerticalAxisTitleTextSize();
        }
        return graphwidth - adaptationWidth;
    }

    /**
     * will be called from system.
     *
     * @param event
     * @return ss
     */


    /**
     * ss
     *
     * @return the legend renderer.
     */
    public LegendRenderer getLegendRenderer() {
        return mLegendRenderer;
    }

    /**
     * use a specific legend renderer
     *
     * @param mLegendRenderer the new legend renderer
     */
    public void setLegendRenderer(LegendRenderer mLegendRenderer) {
        this.mLegendRenderer = mLegendRenderer;
    }

    /**
     * ss
     *
     * @return the title that will be shown
     * above the graph.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set the title of the graph that will
     * be shown above the graph's viewport.
     *
     * @param mTitle the title
     * @see #setTitleColor(int) to set the font color
     * @see #setTitleTextSize(float) to set the font size
     */
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * ss
     *
     * @return the title font size
     */
    public float getTitleTextSize() {
        return mStyles.titleTextSize;
    }

    /**
     * Set the title's font size
     *
     * @param titleTextSize font size
     * @see #setTitle(String)
     */
    public void setTitleTextSize(float titleTextSize) {
        mStyles.titleTextSize = titleTextSize;
    }

    /**
     * ss
     *
     * @return font color of the title
     */
    public int getTitleColor() {
        return mStyles.titleColor;
    }

    /**
     * Set the title's font color
     *
     * @param titleColor font color of the title
     * @see #setTitle(String)
     */
    public void setTitleColor(int titleColor) {
        mStyles.titleColor = titleColor;
    }

    /**
     * creates the second scale logic and returns it
     *
     * @return second scale object
     */
    public SecondScale getSecondScale() {
        if (mSecondScale == null) {
            // this creates the second scale
            mSecondScale = new SecondScale(this);
            mSecondScale.setVerticalAxisTitleTextSize(mGridLabelRenderer.mStyles.textSize);
        }
        return mSecondScale;
    }

    /**
     * clears the second scale
     */
    public void clearSecondScale() {
        if (mSecondScale != null) {
            mSecondScale.removeAllSeries();
            mSecondScale = null;
        }
    }

    /**
     * Removes all series of the graph.
     */
    public void removeAllSeries() {
        mSeries.clear();
        onDataChanged(false, false);
    }

    /**
     * Remove a specific series of the graph.
     * This will also re-draw the graph, but
     * without recalculating the viewport and
     * label sizes.
     * If you want this, you have to call {@link #onDataChanged(boolean, boolean)}
     * manually.
     *
     * @param series ss
     */
    public void removeSeries(Series<?> series) {
        mSeries.remove(series);
        onDataChanged(false, false);
    }


    /**
     * ss
     *
     * @param b ss
     */
    public void setCursorMode(boolean b) {
        mIsCursorMode = b;
        if (mIsCursorMode) {
            if (mCursorMode == null) {
                mCursorMode = new CursorMode(this);
            }
        } else {
            mCursorMode = null;
            invalidate();
        }
        for (Series series : mSeries) {
            if (series instanceof BaseSeries) {
                ((BaseSeries) series).clearCursorModeCache();
            }
        }
    }

    /**
     * ss
     *
     * @return ss
     */
    public CursorMode getCursorMode() {
        return mCursorMode;
    }

    /**
     * ss
     *
     * @return ss
     */
    public boolean isCursorMode() {
        return mIsCursorMode;
    }
}
