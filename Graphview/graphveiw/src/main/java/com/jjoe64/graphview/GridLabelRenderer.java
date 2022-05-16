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

import com.jjoe64.graphview.utils.LogUtil;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The default renderer for the grid
 * and the labels.
 *
 * @author jjoe64
 */
public class GridLabelRenderer {

    private int LABELS_SPACE_MAX = 23;
    private int LABELS_SPACE_MIN = 1;

    /**
     * Hoziontal label alignment
     */
    public enum VerticalLabelsVAlign {
        /**
         * Above vertical line
         */
        ABOVE,
        /**
         * Mid vertical line
         */
        MID,
        /**
         * Below vertical line
         */
        BELOW
    }


    /**
     * wrapper for the styles regarding
     * to the grid and the labels
     */
    public final class Styles {
        /**
         * the general text size of the axis titles.
         * can be overwritten with #verticalAxisTitleTextSize
         * and #horizontalAxisTitleTextSize
         */
        public float textSize;

        /**
         * the alignment of the vertical labels
         */
        public int verticalLabelsAlign;

        /**
         * the alignment of the labels on the right side
         */
        public int verticalLabelsSecondScaleAlign;

        /**
         * the color of the vertical labels
         */
        public int verticalLabelsColor;

        /**
         * the color of the labels on the right side
         */
        public int verticalLabelsSecondScaleColor;

        /**
         * the color of the horizontal labels
         */
        public int horizontalLabelsColor;

        /**
         * the color of the grid lines
         */
        public int gridColor;

        /**
         * flag whether the zero-lines (vertical+
         * horizontal) shall be highlighted
         */
        public boolean highlightZeroLines;

        /**
         * the padding around the graph and labels
         */
        public int padding;

        /**
         * font size of the vertical axis title
         */
        public float verticalAxisTitleTextSize;

        /**
         * font color of the vertical axis title
         */
        public int verticalAxisTitleColor;

        /**
         * font size of the horizontal axis title
         */
        public float horizontalAxisTitleTextSize;

        /**
         * font color of the horizontal axis title
         */
        public int horizontalAxisTitleColor;

        /**
         * angle of the horizontal axis label in
         * degrees between 0 and 180
         */
        public float horizontalLabelsAngle;

        /**
         * flag whether the horizontal labels are
         * visible
         */
        boolean horizontalLabelsVisible;

        /**
         * flag whether the vertical labels are
         * visible
         */
        boolean verticalLabelsVisible;

        /**
         * defines which lines will be drawn in the background
         */
        GridStyle gridStyle;

        /**
         * the space between the labels text and the graph content
         */
        int labelsSpace;

        /**
         * vertical labels vertical align (above, below, mid of the grid line)
         */
        VerticalLabelsVAlign verticalLabelsVAlign = VerticalLabelsVAlign.MID;
    }

    /**
     * Definition which lines will be drawn in the background
     */
    public enum GridStyle {
        /**
         * show vertical and horizonal lines
         * this is the default
         */
        BOTH,

        /**
         * show only vertical lines
         */
        VERTICAL,

        /**
         * show only horizontal lines
         */
        HORIZONTAL,

        /**
         * dont draw any lines
         */
        NONE;

        public boolean drawVertical() {
            return this == BOTH || this == VERTICAL && this != NONE;
        }

        public boolean drawHorizontal() {
            return this == BOTH || this == HORIZONTAL && this != NONE;
        }
    }

    /**
     * wraps the styles regarding the
     * grid and labels
     */
    protected Styles mStyles;

    /**
     * reference to graphview
     */
    private final GraphView mGraphView;

    /**
     * cache of the vertical steps
     * (horizontal lines and vertical labels)
     * Key      = Pixel (y)
     * Value    = y-value
     */
    private Map<Integer, Double> mStepsVertical;

    /**
     * cache of the vertical steps for the
     * second scale, which is on the right side
     * (horizontal lines and vertical labels)
     * Key      = Pixel (y)
     * Value    = y-value
     */
    private Map<Integer, Double> mStepsVerticalSecondScale;

    /**
     * cache of the horizontal steps
     * (vertical lines and horizontal labels)
     * Value    = x-value
     */
    private Map<Integer, Double> mStepsHorizontal;

    /**
     * the paint to draw the grid lines
     */
    private Paint mPaintLine;

    /**
     * the paint to draw the labels
     */
    private Paint mPaintLabel;

    /**
     * the paint to draw axis titles
     */
    private Paint mPaintAxisTitle;

    /**
     * flag whether is bounds are automatically
     * adjusted for nice human-readable numbers
     */
    protected boolean mIsAdjusted;

    /**
     * the width of the vertical labels
     */
    private Integer mLabelVerticalWidth;

    /**
     * indicates if the width was set manually
     */
    private boolean mLabelVerticalWidthFixed;

    /**
     * the height of the vertical labels
     */
    private Integer mLabelVerticalHeight;

    /**
     * indicates if the height was set manually
     */
    private boolean mLabelHorizontalHeightFixed;

    /**
     * the width of the vertical labels
     * of the second scale
     */
    private Integer mLabelVerticalSecondScaleWidth;

    /**
     * the height of the vertical labels
     * of the second scale
     */
    private Integer mLabelVerticalSecondScaleHeight;

    /**
     * the width of the horizontal labels
     */
    private Integer mLabelHorizontalWidth;

    /**
     * the height of the horizontal labels
     */
    private Integer mLabelHorizontalHeight;

    /**
     * the label formatter, that converts
     * the raw numbers to strings
     */
    private LabelFormatter mLabelFormatter;

    /**
     * the title of the horizontal axis
     */
    private String mHorizontalAxisTitle;

    /**
     * the title of the vertical axis
     */
    private String mVerticalAxisTitle;

    /**
     * count of the vertical labels, that
     * will be shown at one time.
     */
    private int mNumVerticalLabels;

    /**
     * count of the horizontal labels, that
     * will be shown at one time.
     */
    private int mNumHorizontalLabels;

    /**
     * sets the space for the vertical labels on the right side
     *
     * @param newWidth set fixed width. set null to calculate it automatically
     */
    public void setSecondScaleLabelVerticalWidth(Integer newWidth) {
        mLabelVerticalSecondScaleWidth = newWidth;
    }

    /**
     * activate or deactivate human rounding of the
     * horizontal axis. GraphView tries to fit the labels
     * to display numbers that can be divided by 1, 2, or 5.
     */
    private boolean mHumanRoundingY;

    /**
     * activate or deactivate human rounding of the
     * horizontal axis. GraphView tries to fit the labels
     * to display numbers that can be divided by 1, 2, or 5.
     * <p>
     * By default this is enabled. It makes sense to deactivate it
     * when using Dates on the x axis.
     */
    private boolean mHumanRoundingX;

    /**
     * create the default grid label renderer.
     *
     * @param graphView the corresponding graphview object
     */
    public GridLabelRenderer(GraphView graphView) {
        mGraphView = graphView;
        setLabelFormatter(new DefaultLabelFormatter());
        mStyles = new Styles();
        resetStyles();
        mNumVerticalLabels = 5;
        mNumHorizontalLabels = 5;
        mHumanRoundingX = true;
        mHumanRoundingY = true;
    }

    /**
     * resets the styles. This loads the style
     * from reading the values of the current
     * theme.
     */
    public void resetStyles() {

        int color1;
        int color2;
        int size;
        int size2;

        color1 = Color.BLACK.getValue();
        color2 = Color.GRAY.getValue();
        size = 25;
        size2 = 25;

        mStyles.verticalLabelsColor = color1;
        mStyles.verticalLabelsSecondScaleColor = color1;
        mStyles.horizontalLabelsColor = color1;
        mStyles.gridColor = color2;
        mStyles.textSize = size;
        mStyles.padding = size2;
        mStyles.labelsSpace = (int) mStyles.textSize / 5;

        mStyles.verticalLabelsAlign = 2;
        mStyles.verticalLabelsSecondScaleAlign = 0;
        mStyles.highlightZeroLines = true;

        mStyles.verticalAxisTitleColor = mStyles.verticalLabelsColor;
        mStyles.horizontalAxisTitleColor = mStyles.horizontalLabelsColor;
        mStyles.verticalAxisTitleTextSize = mStyles.textSize;
        mStyles.horizontalAxisTitleTextSize = mStyles.textSize;

        mStyles.horizontalLabelsVisible = true;
        mStyles.verticalLabelsVisible = true;

        mStyles.horizontalLabelsAngle = 0f;

        mStyles.gridStyle = GridStyle.BOTH;
        reloadStyles();
    }

    /**
     * will load the styles to the internal
     * paint objects (color, text size, text align)
     */
    public void reloadStyles() {
        mPaintLine = new Paint();
        mPaintLine.setColor(new Color(mStyles.gridColor));
        mPaintLine.setStrokeWidth(0);

        mPaintLabel = new Paint();
        mPaintLabel.setTextSize((int) getTextSize());
        mPaintLabel.setAntiAlias(true);

        mPaintAxisTitle = new Paint();
        mPaintAxisTitle.setTextSize((int) getTextSize());
        mPaintAxisTitle.setTextAlign(1);
    }

    /**
     * GraphView tries to fit the labels
     * to display numbers that can be divided by 1, 2, or 5.
     * <p>
     * By default this is enabled. It makes sense to deactivate it
     * when using Dates on the x axis.
     *
     * @return if human rounding is enabled
     */
    public boolean isHumanRoundingX() {
        return mHumanRoundingX;
    }

    /**
     * GraphView tries to fit the labels
     * to display numbers that can be divided by 1, 2, or 5.
     *
     * @return if human rounding is enabled
     */
    public boolean isHumanRoundingY() {
        return mHumanRoundingY;
    }

    /**
     * activate or deactivate human rounding of the
     * horizontal axis. GraphView tries to fit the labels
     * to display numbers that can be divided by 1, 2, or 5.
     * <p>
     * By default this is enabled. It makes sense to deactivate it
     * when using Dates on the x axis.
     *
     * @param humanRoundingX false to deactivate
     * @param humanRoundingY false to deactivate
     */
    public void setHumanRounding(boolean humanRoundingX, boolean humanRoundingY) {
        this.mHumanRoundingX = humanRoundingX;
        this.mHumanRoundingY = humanRoundingY;
    }

    /**
     * activate or deactivate human rounding of the
     * horizontal axis. GraphView tries to fit the labels
     * to display numbers that can be divided by 1, 2, or 5.
     * <p>
     * By default this is enabled.
     *
     * @param humanRoundingBoth false to deactivate on both axises
     */
    public void setHumanRounding(boolean humanRoundingBoth) {
        this.mHumanRoundingX = humanRoundingBoth;
        this.mHumanRoundingY = humanRoundingBoth;
    }

    /**
     * ss
     *
     * @return the general text size for the axis titles
     */
    public float getTextSize() {
        return mStyles.textSize;
    }

    /**
     * ss
     *
     * @return the font color of the vertical labels
     */
    public int getVerticalLabelsColor() {
        return mStyles.verticalLabelsColor;
    }

    /**
     * ss
     *
     * @return the alignment of the text of the
     * vertical labels
     */
    public int getVerticalLabelsAlign() {
        return mStyles.verticalLabelsAlign;
    }

    /**
     * ss
     *
     * @return the font color of the horizontal labels
     */
    public int getHorizontalLabelsColor() {
        return mStyles.horizontalLabelsColor;
    }

    /**
     * ss
     *
     * @return the angle of the horizontal labels
     */
    public float getHorizontalLabelsAngle() {
        return mStyles.horizontalLabelsAngle;
    }

    /**
     * clears the internal cache and forces
     * to redraw the grid and labels.
     * Normally you should always call {@link GraphView#onDataChanged(boolean, boolean)}
     * which will call this method.
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
    public void invalidate(boolean keepLabelsSize, boolean keepViewport) {
        if (!keepViewport) {
            mIsAdjusted = false;
        }
        if (!keepLabelsSize) {
            if (!mLabelVerticalWidthFixed) {
                mLabelVerticalWidth = null;
            }
            mLabelVerticalHeight = null;
            mLabelVerticalSecondScaleWidth = null;
            mLabelVerticalSecondScaleHeight = null;
        }
        //reloadStyles();
    }

    /**
     * calculates the vertical steps of
     * the second scale.
     * This will not do any automatically update
     * of the bounds.
     * Use always manual bounds for the second scale.
     *
     * @return true if it is ready
     */
    protected boolean adjustVerticalSecondScale() {
        if (mLabelHorizontalHeight == null) {
            return false;
        }
        if (mGraphView.mSecondScale == null) {
            return true;
        }

        double minY = mGraphView.mSecondScale.getMinY(false);
        double maxY = mGraphView.mSecondScale.getMaxY(false);

        // TODO find the number of labels
        int numVerticalLabels = mNumVerticalLabels;

        double newMinY;
        double exactSteps;

        if (mGraphView.mSecondScale.isYAxisBoundsManual()) {
            // split range into equal steps
            exactSteps = (maxY - minY) / (numVerticalLabels - 1);

            // round because of floating error
            exactSteps = Math.round(exactSteps * 1000000d) / 1000000d;
        } else {
            // TODO auto adjusting
            throw new IllegalStateException("Not yet implemented");
        }

        if (mStepsVerticalSecondScale != null && mStepsVerticalSecondScale.size() > 1) {
            // else choose other nice steps that previous
            // steps are included (divide to have more, or multiplicate to have less)

            double d1 = 0, d2 = 0;
            int i = 0;
            for (Double v : mStepsVerticalSecondScale.values()) {
                if (i == 0) {
                    d1 = v;
                } else {
                    d2 = v;
                    break;
                }
                i++;
            }
            double oldSteps = d2 - d1;
            if (oldSteps > 0) {
                double newSteps = Double.NaN;

                if (oldSteps > exactSteps) {
                    newSteps = oldSteps / 2;
                } else if (oldSteps < exactSteps) {
                    newSteps = oldSteps * 2;
                }

                // only if there wont be more than numLabels
                // and newSteps will be better than oldSteps
                int numStepsOld = (int) ((maxY - minY) / oldSteps);
                int numStepsNew = (int) ((maxY - minY) / newSteps);

                boolean shouldChange;

                // avoid switching between 2 steps
                if (numStepsOld <= numVerticalLabels && numStepsNew <= numVerticalLabels) {
                    // both are possible
                    // only the new if it hows more labels
                    shouldChange = numStepsNew > numStepsOld;
                } else {
                    shouldChange = true;
                }

                if (newSteps != Double.NaN && shouldChange && numStepsNew <= numVerticalLabels) {
                    exactSteps = newSteps;
                } else {
                    // try to stay to the old steps
                    exactSteps = oldSteps;
                }
            }
        } else {
            // first time
            LogUtil.debug("find", "find");
        }

        // find the first data point that is relevant to display
        // starting from 1st datapoint so that the steps have nice numbers
        // goal is to start with the minY or 1 step before
        newMinY = mGraphView.getSecondScale().mReferenceY;
        // must be down-rounded
        double count = Math.floor((minY - newMinY) / exactSteps);
        newMinY = count * exactSteps + newMinY;

        // it can happen that we need to add some more labels to fill the complete screen
        numVerticalLabels = (int) ((mGraphView.getSecondScale().mCurrentViewport.height() * -1 / exactSteps)) + 2;

        // ensure that the value is valid (minimum 2)
        numVerticalLabels = Math.max(numVerticalLabels, 2);

        if (mStepsVerticalSecondScale != null) {
            mStepsVerticalSecondScale.clear();
        } else {
            mStepsVerticalSecondScale = new LinkedHashMap<>(numVerticalLabels);
        }

        int height = mGraphView.getGraphContentHeight();
        // convert data-y to pixel-y in current viewport
        double pixelPerData = height / mGraphView.getSecondScale().mCurrentViewport.height() * -1;

        for (int i = 0; i < numVerticalLabels; i++) {
            // dont draw if it is top of visible screen
            if (newMinY + (i * exactSteps) > mGraphView.getSecondScale().mCurrentViewport.top) {
                continue;
            }
            // dont draw if it is below of visible screen
            if (newMinY + (i * exactSteps) < mGraphView.getSecondScale().mCurrentViewport.bottom) {
                continue;
            }


            // where is the data point on the current screen
            double dataPointPos = newMinY + (i * exactSteps);
            double relativeToCurrentViewport = dataPointPos - mGraphView.getSecondScale().mCurrentViewport.bottom;

            double pixelPos = relativeToCurrentViewport * pixelPerData;
            mStepsVerticalSecondScale.put((int) pixelPos, dataPointPos);
        }

        return true;
    }

    /**
     * calculates the vertical steps. This will
     * automatically change the bounds to nice
     * human-readable min/max.
     *
     * @param changeBounds str
     * @return true if it is ready
     */
    protected boolean adjustVertical(boolean changeBounds) {
        if (mLabelHorizontalHeight == null) {
            return false;
        }

        double minY = mGraphView.getViewport().getMinY(false);
        double maxY = mGraphView.getViewport().getMaxY(false);

        if (minY == maxY) {
            return false;
        }

        // TODO find the number of labels
        int numVerticalLabels = mNumVerticalLabels;

        double newMinY;
        double exactSteps;

        // split range into equal steps
        exactSteps = (maxY - minY) / (numVerticalLabels - 1);

        // round because of floating error
        exactSteps = Math.round(exactSteps * 1000000d) / 1000000d;

        // smallest viewport
        if (exactSteps == 0d) {
            exactSteps = 0.0000001d;
            maxY = minY + exactSteps * (numVerticalLabels - 1);
        }

        // human rounding to have nice numbers (1, 2, 5, ...)
        if (isHumanRoundingY()) {
            exactSteps = humanRound(exactSteps, changeBounds);
        } else if (mStepsVertical != null && mStepsVertical.size() > 1) {
            // else choose other nice steps that previous
            // steps are included (divide to have more, or multiplicate to have less)

            double d1 = 0, d2 = 0;
            int i = 0;
            for (Double v : mStepsVertical.values()) {
                if (i == 0) {
                    d1 = v;
                } else {
                    d2 = v;
                    break;
                }
                i++;
            }
            double oldSteps = d2 - d1;
            if (oldSteps > 0) {
                double newSteps = Double.NaN;

                if (oldSteps > exactSteps) {
                    newSteps = oldSteps / 2;
                } else if (oldSteps < exactSteps) {
                    newSteps = oldSteps * 2;
                }

                // only if there wont be more than numLabels
                // and newSteps will be better than oldSteps
                int numStepsOld = (int) ((maxY - minY) / oldSteps);
                int numStepsNew = (int) ((maxY - minY) / newSteps);

                boolean shouldChange;

                // avoid switching between 2 steps
                if (numStepsOld <= numVerticalLabels && numStepsNew <= numVerticalLabels) {
                    // both are possible
                    // only the new if it hows more labels
                    shouldChange = numStepsNew > numStepsOld;
                } else {
                    shouldChange = true;
                }

                if (newSteps != Double.NaN && shouldChange && numStepsNew <= numVerticalLabels) {
                    exactSteps = newSteps;
                } else {
                    // try to stay to the old steps
                    exactSteps = oldSteps;
                }
            }
        } else {
            // first time
            LogUtil.debug("find", "find");
        }

        // find the first data point that is relevant to display
        // starting from 1st datapoint so that the steps have nice numbers
        // goal is to start with the minX or 1 step before
        newMinY = mGraphView.getViewport().getReferenceY();
        // must be down-rounded
        double count = Math.floor((minY - newMinY) / exactSteps);
        newMinY = count * exactSteps + newMinY;

        // now we have our labels bounds
        if (changeBounds) {
            mGraphView.getViewport().setMinY(newMinY);
            mGraphView.getViewport().setMaxY(Math.max(maxY, newMinY + (numVerticalLabels - 1) * exactSteps));
            mGraphView.getViewport().mYAxisBoundsStatus = Viewport.AxisBoundsStatus.AUTO_ADJUSTED;
        }

        // it can happen that we need to add some more labels to fill the complete screen
        numVerticalLabels = (int) ((mGraphView.getViewport().mCurrentViewport.height() * -1 / exactSteps)) + 2;

        if (mStepsVertical != null) {
            mStepsVertical.clear();
        } else {
            mStepsVertical = new LinkedHashMap<>((int) numVerticalLabels);
        }

        int height = mGraphView.getGraphContentHeight();
        // convert data-y to pixel-y in current viewport
        double pixelPerData = height / mGraphView.getViewport().mCurrentViewport.height() * -1;

        for (int i = 0; i < numVerticalLabels; i++) {
            // dont draw if it is top of visible screen
            if (newMinY + (i * exactSteps) > mGraphView.getViewport().mCurrentViewport.top) {
                continue;
            }
            // dont draw if it is below of visible screen
            if (newMinY + (i * exactSteps) < mGraphView.getViewport().mCurrentViewport.bottom) {
                continue;
            }


            // where is the data point on the current screen
            double dataPointPos = newMinY + (i * exactSteps);
            double relativeToCurrentViewport = dataPointPos - mGraphView.getViewport().mCurrentViewport.bottom;

            double pixelPos = relativeToCurrentViewport * pixelPerData;
            mStepsVertical.put((int) pixelPos, dataPointPos);
        }

        return true;
    }

    /**
     * calculates the horizontal steps.
     *
     * @param changeBounds This will automatically change the
     *                     bounds to nice human-readable min/max.
     * @return true if it is ready
     */
    protected boolean adjustHorizontal(boolean changeBounds) {
        if (mLabelVerticalWidth == null) {
            return false;
        }

        double minX = mGraphView.getViewport().getMinX(false);
        double maxX = mGraphView.getViewport().getMaxX(false);
        if (minX == maxX) return false;

        // TODO find the number of labels
        int numHorizontalLabels = mNumHorizontalLabels;

        double newMinX;
        double exactSteps;

        // split range into equal steps
        exactSteps = (maxX - minX) / (numHorizontalLabels - 1);

        // round because of floating error
        exactSteps = Math.round(exactSteps * 1000000d) / 1000000d;

        // smallest viewport
        if (exactSteps == 0d) {
            exactSteps = 0.0000001d;
            maxX = minX + exactSteps * (numHorizontalLabels - 1);
        }

        // human rounding to have nice numbers (1, 2, 5, ...)
        if (isHumanRoundingX()) {
            exactSteps = humanRound(exactSteps, false);
        } else if (mStepsHorizontal != null && mStepsHorizontal.size() > 1) {
            // else choose other nice steps that previous
            // steps are included (divide to have more, or multiplicate to have less)

            double d1 = 0, d2 = 0;
            int i = 0;
            for (Double v : mStepsHorizontal.values()) {
                if (i == 0) {
                    d1 = v;
                } else {
                    d2 = v;
                    break;
                }
                i++;
            }
            double oldSteps = d2 - d1;
            if (oldSteps > 0) {
                double newSteps = Double.NaN;

                if (oldSteps > exactSteps) {
                    newSteps = oldSteps / 2;
                } else if (oldSteps < exactSteps) {
                    newSteps = oldSteps * 2;
                }

                // only if there wont be more than numLabels
                // and newSteps will be better than oldSteps
                int numStepsOld = (int) ((maxX - minX) / oldSteps);
                int numStepsNew = (int) ((maxX - minX) / newSteps);

                boolean shouldChange;

                // avoid switching between 2 steps
                if (numStepsOld <= numHorizontalLabels && numStepsNew <= numHorizontalLabels) {
                    // both are possible
                    // only the new if it hows more labels
                    shouldChange = numStepsNew > numStepsOld;
                } else {
                    shouldChange = true;
                }

                if (newSteps != Double.NaN && shouldChange && numStepsNew <= numHorizontalLabels) {
                    exactSteps = newSteps;
                } else {
                    // try to stay to the old steps
                    exactSteps = oldSteps;
                }
            }
        } else {
            // first time
            LogUtil.debug("find", "find");
        }


        // starting from 1st datapoint
        // goal is to start with the minX or 1 step before
        newMinX = mGraphView.getViewport().getReferenceX();
        // must be down-rounded
        double count = Math.floor((minX - newMinX) / exactSteps);
        newMinX = count * exactSteps + newMinX;

        // now we have our labels bounds
        if (changeBounds) {
            mGraphView.getViewport().setMinX(newMinX);
            mGraphView.getViewport().setMaxX(newMinX + (numHorizontalLabels - 1) * exactSteps);
            mGraphView.getViewport().mXAxisBoundsStatus = Viewport.AxisBoundsStatus.AUTO_ADJUSTED;
        }

        // it can happen that we need to add some more labels to fill the complete screen
        numHorizontalLabels = (int) ((mGraphView.getViewport().mCurrentViewport.width() / exactSteps)) + 1;

        if (mStepsHorizontal != null) {
            mStepsHorizontal.clear();
        } else {
            mStepsHorizontal = new LinkedHashMap<>((int) numHorizontalLabels);
        }

        int width = mGraphView.getGraphContentWidth();
        // convert data-x to pixel-x in current viewport
        double pixelPerData = width / mGraphView.getViewport().mCurrentViewport.width();

        for (int i = 0; i < numHorizontalLabels; i++) {
            // dont draw if it is left of visible screen
            if (newMinX + (i * exactSteps) < mGraphView.getViewport().mCurrentViewport.left) {
                continue;
            }

            // where is the data point on the current screen
            double dataPointPos = newMinX + (i * exactSteps);
            double relativeToCurrentViewport = dataPointPos - mGraphView.getViewport().mCurrentViewport.left;

            double pixelPos = relativeToCurrentViewport * pixelPerData;
            mStepsHorizontal.put((int) pixelPos, dataPointPos);
        }

        return true;
    }

    /**
     * adjusts the grid and labels to match to the data
     * this will automatically change the bounds to
     * nice human-readable values, except the bounds
     * are manual.
     */
    protected void adjustSteps() {
        mIsAdjusted = adjustVertical(!Viewport.AxisBoundsStatus.FIX.equals(mGraphView.getViewport().mYAxisBoundsStatus));
        mIsAdjusted &= adjustVerticalSecondScale();
        mIsAdjusted &= adjustHorizontal(!Viewport.AxisBoundsStatus.FIX.equals(mGraphView.getViewport().mXAxisBoundsStatus));
    }

    /**
     * calculates the vertical label size
     *
     * @param canvas canvas
     */
    protected void calcLabelVerticalSize(Canvas canvas) {
        // test label with first and last label
        String testLabel = mLabelFormatter.formatLabel(mGraphView.getViewport().getMaxY(false), false);
        if (testLabel == null) testLabel = "";
        LogUtil.error("calcLabelVerticalSize--1->", testLabel);
        Rect textBounds = new Rect();
        mPaintLabel.getTextBounds(testLabel);
        mLabelVerticalWidth = textBounds.getWidth();
        mLabelVerticalHeight = textBounds.getHeight();

        testLabel = mLabelFormatter.formatLabel(mGraphView.getViewport().getMinY(false), false);
        if (testLabel == null) testLabel = "";
        LogUtil.error("calcLabelVerticalSize--2->", testLabel);
        mPaintLabel.getTextBounds(testLabel);
        mLabelVerticalWidth = Math.max(mLabelVerticalWidth, textBounds.getWidth());

        // add some pixel to get a margin
        mLabelVerticalWidth += 6;

        // space between text and graph content
        mLabelVerticalWidth += mStyles.labelsSpace;

        // multiline
        int lines = 1;
        for (byte c : testLabel.getBytes()) {
            if (c == '\n') lines++;
        }
        mLabelVerticalHeight *= lines;
    }

    /**
     * calculates the vertical second scale
     * label size
     *
     * @param canvas canvas
     */
    protected void calcLabelVerticalSecondScaleSize(Canvas canvas) {
        if (mGraphView.mSecondScale == null) {
            mLabelVerticalSecondScaleWidth = 0;
            mLabelVerticalSecondScaleHeight = 0;
            return;
        }

        // test label
        double testY = ((mGraphView.mSecondScale.getMaxY(false) - mGraphView.mSecondScale.getMinY(false)) * 0.783) + mGraphView.mSecondScale.getMinY(false);
        String testLabel = mGraphView.mSecondScale.getLabelFormatter().formatLabel(testY, false);
        Rect textBounds = new Rect();
        mPaintLabel.getTextBounds(testLabel);
        mLabelVerticalSecondScaleWidth = textBounds.getWidth();
        mLabelVerticalSecondScaleHeight = textBounds.getHeight();

        // multiline
        int lines = 1;
        for (byte c : testLabel.getBytes()) {
            if (c == '\n') lines++;
        }
        mLabelVerticalSecondScaleHeight *= lines;
    }

    /**
     * calculates the horizontal label size
     *
     * @param canvas canvas
     */
    protected void calcLabelHorizontalSize(Canvas canvas) {
        // test label
        double testX = ((mGraphView.getViewport().getMaxX(false) - mGraphView.getViewport().getMinX(false)) * 0.783) + mGraphView.getViewport().getMinX(false);
        String testLabel = mLabelFormatter.formatLabel(testX, true);
        if (testLabel == null) {
            testLabel = "";
        }
        Rect textBounds = new Rect();
        mPaintLabel.getTextBounds(testLabel);
        mLabelHorizontalWidth = textBounds.getWidth();

        if (!mLabelHorizontalHeightFixed) {
            mLabelHorizontalHeight = textBounds.getHeight();

            // multiline
            int lines = 1;
            for (byte c : testLabel.getBytes()) {
                if (c == '\n') lines++;
            }
            mLabelHorizontalHeight *= lines;

            mLabelHorizontalHeight = (int) Math.max(mLabelHorizontalHeight, mStyles.textSize);
        }

        if (mStyles.horizontalLabelsAngle > 0f && mStyles.horizontalLabelsAngle <= 180f) {
            int adjHorizontalHeightH = (int) Math.round(Math.abs(mLabelHorizontalHeight * Math.cos(Math.toRadians(mStyles.horizontalLabelsAngle))));
            int adjHorizontalHeightW = (int) Math.round(Math.abs(mLabelHorizontalWidth * Math.sin(Math.toRadians(mStyles.horizontalLabelsAngle))));
            int adjHorizontalWidthH = (int) Math.round(Math.abs(mLabelHorizontalHeight * Math.sin(Math.toRadians(mStyles.horizontalLabelsAngle))));
            int adjHorizontalWidthW = (int) Math.round(Math.abs(mLabelHorizontalWidth * Math.cos(Math.toRadians(mStyles.horizontalLabelsAngle))));

            mLabelHorizontalHeight = adjHorizontalHeightH + adjHorizontalHeightW;
            mLabelHorizontalWidth = adjHorizontalWidthH + adjHorizontalWidthW;
        }

        // space between text and graph content
        mLabelHorizontalHeight += mStyles.labelsSpace;
    }

    /**
     * do the drawing of the grid
     * and labels
     *
     * @param width  str
     * @param height str
     * @param canvas canvas
     */
    public void draw(Canvas canvas, int width, int height) {

        boolean labelSizeChanged = false;
        if (mLabelHorizontalWidth == null) {
            calcLabelHorizontalSize(canvas);
            labelSizeChanged = true;
        }
        if (mLabelVerticalWidth == null) {
            calcLabelVerticalSize(canvas);
            labelSizeChanged = true;
        }
        if (mLabelVerticalSecondScaleWidth == null) {
            calcLabelVerticalSecondScaleSize(canvas);
            labelSizeChanged = true;
        }
        if (labelSizeChanged) {
            // redraw directly
            mGraphView.drawGraphElements(canvas);
            return;
        }

        if (!mIsAdjusted) {
            adjustSteps();
        }

        if (mIsAdjusted) {
            //Y轴数据
            drawVerticalSteps(canvas);
            drawVerticalStepsSecondScale(canvas);
            //X轴数据
            drawHorizontalSteps(canvas, width, height);
        } else {
            // we can not draw anything
            return;
        }

        drawHorizontalAxisTitle(canvas, width, height);
        drawVerticalAxisTitle(canvas, width, height);

        // draw second scale axis title if it exists
        if (mGraphView.mSecondScale != null) {
            mGraphView.mSecondScale.drawVerticalAxisTitle(canvas, width, height);
        }
    }

    /**
     * draws the horizontal axis title if
     * it is set
     *
     * @param canvas canvas
     * @param width  str
     * @param height str
     */
    protected void drawHorizontalAxisTitle(Canvas canvas, int width, int height) {
        if (mHorizontalAxisTitle != null && mHorizontalAxisTitle.length() > 0) {
            mPaintAxisTitle.setColor(new Color(getHorizontalAxisTitleColor()));
            mPaintAxisTitle.setTextSize((int) getHorizontalAxisTitleTextSize());
            float x = width / 2;
            float y = height - mStyles.padding;
            canvas.drawText(mPaintAxisTitle, mHorizontalAxisTitle, x, y);
        }
    }

    /**
     * draws the vertical axis title if
     * it is set
     *
     * @param canvas canvas
     * @param width  str
     * @param height str
     */
    protected void drawVerticalAxisTitle(Canvas canvas, int width, int height) {
        if (mVerticalAxisTitle != null && mVerticalAxisTitle.length() > 0) {
            mPaintAxisTitle.setColor(new Color(getVerticalAxisTitleColor()));
            mPaintAxisTitle.setTextSize((int) getVerticalAxisTitleTextSize());
            float x = getVerticalAxisTitleWidth();
            float y = height / 2;
            canvas.save();
            canvas.rotate(-90, x, y);
            canvas.drawText(mPaintAxisTitle, mVerticalAxisTitle, x, y);
            canvas.restore();
        }
    }

    /**
     * ss
     *
     * @return the horizontal axis title height
     * or 0 if there is no title
     */
    public int getHorizontalAxisTitleHeight() {
        if (mHorizontalAxisTitle != null && mHorizontalAxisTitle.length() > 0) {
            return (int) getHorizontalAxisTitleTextSize();
        } else {
            return 0;
        }
    }

    /**
     * ss
     *
     * @return the vertical axis title width
     * or 0 if there is no title
     */
    public int getVerticalAxisTitleWidth() {
        if (mVerticalAxisTitle != null && mVerticalAxisTitle.length() > 0) {
            return (int) getVerticalAxisTitleTextSize();
        } else {
            return 0;
        }
    }

    /**
     * draws the horizontal steps
     * vertical lines and horizontal labels
     * TODO 画X轴数据
     *
     * @param canvas canvas
     * @param height str
     * @param width  str
     */
    protected void drawHorizontalSteps(Canvas canvas, int width, int height) {
        // draw horizontal steps (vertical lines and horizontal labels)
        mPaintLabel.setColor(new Color(getHorizontalLabelsColor()));
        int i = 0;
        for (Map.Entry<Integer, Double> e : mStepsHorizontal.entrySet()) {
            // draw line
            if (mStyles.highlightZeroLines) {
                if (e.getValue() == 0d) {
                    mPaintLine.setStrokeWidth(5);
                } else {
                    mPaintLine.setStrokeWidth(0);
                }
            }
            if (mStyles.gridStyle.drawVertical()) {
                // dont draw if it is right of visible screen
                if (e.getKey() <= mGraphView.getGraphContentWidth()) {
                    canvas.drawLine(new Point(mGraphView.getGraphContentLeft() + e.getKey(), mGraphView.getGraphContentTop()), new Point(mGraphView.getGraphContentLeft() + e.getKey(), mGraphView.getGraphContentTop() + mGraphView.getGraphContentHeight()), mPaintLine);
                }
            }

            // draw label
            if (isHorizontalLabelsVisible()) {
                if (mStyles.horizontalLabelsAngle > 0f && mStyles.horizontalLabelsAngle <= 180f) {
                    if (mStyles.horizontalLabelsAngle < 90f) {
                        mPaintLabel.setTextAlign((2));
                    } else if (mStyles.horizontalLabelsAngle <= 180f) {
                        mPaintLabel.setTextAlign((0));
                    }
                } else {
                    mPaintLabel.setTextAlign(1);
                    if (i == mStepsHorizontal.size() - 1)
                        mPaintLabel.setTextAlign(2);
                    if (i == 0)
                        mPaintLabel.setTextAlign(0);
                }

                // multiline labels
                String label = mLabelFormatter.formatLabel(e.getValue(), true);
                LogUtil.error("yuxh--horizontalLabel--->", label + "~~~" + e.getValue());
                if (label == null) {
                    label = "";
                }
                String[] lines = label.split("\n");

                // If labels are angled, calculate adjustment to line them up with the grid
                int labelWidthAdj = 0;
                if (mStyles.horizontalLabelsAngle > 0f && mStyles.horizontalLabelsAngle <= 180f) {
                    Rect textBounds = new Rect();
                    mPaintLabel.getTextBounds(lines[0]);
                    labelWidthAdj = (int) Math.abs(textBounds.getWidth() * Math.cos(Math.toRadians(mStyles.horizontalLabelsAngle)));
                }
                for (int li = 0; li < lines.length; li++) {
                    // for the last line y = height
                    float y = (height - mStyles.padding - getHorizontalAxisTitleHeight()) - (lines.length - li - 1) * getTextSize() * 1.1f + mStyles.labelsSpace;
                    float x = mGraphView.getGraphContentLeft() + e.getKey();
                    if (mStyles.horizontalLabelsAngle > 0 && mStyles.horizontalLabelsAngle < 90f) {
                        canvas.save();
                        canvas.rotate(mStyles.horizontalLabelsAngle, x + labelWidthAdj, y);
                        canvas.drawText(mPaintLabel, lines[li], x + labelWidthAdj - 20, y);
                        canvas.restore();
                    } else if (mStyles.horizontalLabelsAngle > 0 && mStyles.horizontalLabelsAngle <= 180f) {
                        canvas.save();
                        canvas.rotate(mStyles.horizontalLabelsAngle - 180f, x - labelWidthAdj, y);
                        canvas.drawText(mPaintLabel, lines[li], x - labelWidthAdj - 20, y);
                        canvas.restore();
                    } else {
                        canvas.drawText(mPaintLabel, lines[li], x - 20, y);
                    }
                }
            }
            i++;
        }
    }

    /**
     * draws the vertical steps for the
     * second scale on the right side
     *
     * @param canvas canvas
     */
    protected void drawVerticalStepsSecondScale(Canvas canvas) {
        if (mGraphView.mSecondScale == null) {
            return;
        }

        // draw only the vertical labels on the right
        float startLeft = mGraphView.getGraphContentLeft() + mGraphView.getGraphContentWidth();
        mPaintLabel.setColor(new Color(getVerticalLabelsSecondScaleColor()));
        //Y轴第二刻度文案颜色
        mPaintLabel.setTextAlign(getVerticalLabelsSecondScaleAlign());
        for (Map.Entry<Integer, Double> e : mStepsVerticalSecondScale.entrySet()) {
            float posY = mGraphView.getGraphContentTop() + mGraphView.getGraphContentHeight() - e.getKey();

            // draw label
            int labelsWidth = mLabelVerticalSecondScaleWidth;
            int labelsOffset = (int) startLeft;
            if (getVerticalLabelsSecondScaleAlign() == 2) {
                labelsOffset += labelsWidth;
            } else if (getVerticalLabelsSecondScaleAlign() == 1) {
                labelsOffset += labelsWidth / 2;
            }

            float y = posY;

            String[] lines = mGraphView.mSecondScale.mLabelFormatter.formatLabel(e.getValue(), false).split("\n");
            y += (lines.length * getTextSize() * 1.1f) / 2; // center text vertically
            for (int li = 0; li < lines.length; li++) {
                // for the last line y = height
                float y2 = y - (lines.length - li - 1) * getTextSize() * 1.1f;
                canvas.drawText(mPaintLabel, lines[li], labelsOffset, y2);
            }
        }
    }

    /**
     * draws the vertical steps
     * horizontal lines and vertical labels
     *
     * @param canvas canvas
     *               TODO 画Y轴数据
     */
    protected void drawVerticalSteps(Canvas canvas) {
        // draw vertical steps (horizontal lines and vertical labels)
        float startLeft = mGraphView.getGraphContentLeft();
        mPaintLabel.setColor(new Color(getVerticalLabelsColor()));
        mPaintLabel.setTextAlign(getVerticalLabelsAlign());

        int numberOfLine = mStepsVertical.size();
        int currentLine = 1;

        for (Map.Entry<Integer, Double> e : mStepsVertical.entrySet()) {
            float posY = mGraphView.getGraphContentTop() + mGraphView.getGraphContentHeight() - e.getKey();

            // draw line
            if (mStyles.highlightZeroLines) {
                if (e.getValue() == 0d) {
                    mPaintLine.setStrokeWidth(5);
                } else {
                    mPaintLine.setStrokeWidth(0);
                }
            }
            if (mStyles.gridStyle.drawHorizontal()) {
                canvas.drawLine(new Point(startLeft, posY), new Point(startLeft + mGraphView.getGraphContentWidth(), posY), mPaintLine);
            }

            //if draw the label above or below the line, we mustn't draw the first for last label, for beautiful design.
            boolean isDrawLabel = true;
            if ((mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.ABOVE && currentLine == 1)
                    || (mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.BELOW && currentLine == numberOfLine)) {
                isDrawLabel = false;
            }

            // draw label
            if (isVerticalLabelsVisible() && isDrawLabel) {
                int labelsWidth = mLabelVerticalWidth;
                int labelsOffset = 0;
                if (getVerticalLabelsAlign() == 2) {
                    labelsOffset = labelsWidth;
                    labelsOffset -= mStyles.labelsSpace;
                } else if (getVerticalLabelsAlign() == 1) {
                    labelsOffset = labelsWidth / 2;
                }
                labelsOffset += mStyles.padding + getVerticalAxisTitleWidth();

                float y = posY;

                String label = mLabelFormatter.formatLabel(e.getValue(), false);
                LogUtil.error("yuxh--verticalLabel--->", label + "~~~" + e.getValue().intValue());
                if (label == null) {
                    label = "";
                }
                String[] lines = label.split("\n");
                switch (mStyles.verticalLabelsVAlign) {
                    case MID:
                        y += (lines.length * getTextSize() * 1.1f) / 2; // center text vertically
                        break;
                    case ABOVE:
                        y -= 5;
                        break;
                    case BELOW:
                        y += (lines.length * getTextSize() * 1.1f) + 5;
                        break;
                }
                for (int li = 0; li < lines.length; li++) {
                    // for the last line y = height
                    float y2 = y - (lines.length - li - 1) * getTextSize() * 1.1f;
                    canvas.drawText(mPaintLabel, lines[li], labelsOffset - 20, y2);
                }
            }

            currentLine++;
        }
    }

    /**
     * this will do rounding to generate
     * nice human-readable bounds.
     *
     * @param in            the raw value that is to be rounded
     * @param roundAlwaysUp true if it shall always round up (ceil)
     * @return the rounded number
     */
    protected double humanRound(double in, boolean roundAlwaysUp) {
        // round-up to 1-steps, 2-steps or 5-steps
        int ten = 0;
        while (Math.abs(in) >= 10d) {
            in /= 10d;
            ten++;
        }
        while (Math.abs(in) < 1d) {
            in *= 10d;
            ten--;
        }
        if (roundAlwaysUp) {
            if (in == 1d) {
                LogUtil.debug("find", "find");
            } else if (in <= 2d) {
                in = 2d;
            } else if (in <= 5d) {
                in = 5d;
            } else if (in < 10d) {
                in = 10d;
            }
        } else { // always round down
            if (in == 1d) {
                LogUtil.debug("find", "find");
            } else if (in <= 4.9d) {
                in = 2d;
            } else if (in <= 9.9d) {
                in = 5d;
            } else if (in < 15d) {
                in = 10d;
            }
        }
        return in * Math.pow(10d, ten);
    }

    /**
     * ss
     *
     * @return the wrapped styles
     */
    public Styles getStyles() {
        return mStyles;
    }

    /**
     * ss
     *
     * @return the vertical label width
     * 0 if there are no vertical labels
     */
    public int getLabelVerticalWidth() {
        if (mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.ABOVE
                || mStyles.verticalLabelsVAlign == VerticalLabelsVAlign.BELOW) {
            return 0;
        }
        return mLabelVerticalWidth == null || !isVerticalLabelsVisible() ? 0 : mLabelVerticalWidth;
    }

    /**
     * sets a manual and fixed with of the space for
     * the vertical labels. This will prevent GraphView to
     * calculate the width automatically.
     *
     * @param width the width of the space for the vertical labels.
     *              Use null to let GraphView automatically calculate the width.
     */
    public void setLabelVerticalWidth(Integer width) {
        mLabelVerticalWidth = width;
        mLabelVerticalWidthFixed = mLabelVerticalWidth != null;
    }

    /**
     * ss
     *
     * @return the horizontal label height
     * 0 if there are no horizontal labels
     */
    public int getLabelHorizontalHeight() {
        return mLabelHorizontalHeight == null || !isHorizontalLabelsVisible() ? 0 : mLabelHorizontalHeight;
    }

    /**
     * sets a manual and fixed height of the space for
     * the horizontal labels. This will prevent GraphView to
     * calculate the height automatically.
     *
     * @param height the height of the space for the horizontal labels.
     *               Use null to let GraphView automatically calculate the height.
     */
    public void setLabelHorizontalHeight(Integer height) {
        mLabelHorizontalHeight = height;
        mLabelHorizontalHeightFixed = mLabelHorizontalHeight != null;
    }

    /**
     * ss
     *
     * @return the grid line color
     */
    public int getGridColor() {
        return mStyles.gridColor;
    }

    /**
     * ss
     *
     * @return whether the line at 0 are highlighted
     */
    public boolean isHighlightZeroLines() {
        return mStyles.highlightZeroLines;
    }

    /**
     * ss
     *
     * @return the padding around the grid and labels
     */
    public int getPadding() {
        return mStyles.padding;
    }

    /**
     * ss
     *
     * @param textSize the general text size of the axis titles.
     *                 can be overwritten with {@link #setVerticalAxisTitleTextSize(float)}
     *                 and {@link #setHorizontalAxisTitleTextSize(float)}
     */
    public void setTextSize(float textSize) {
        if (textSize <= 0) {
            mStyles.textSize = 20;
        } else {
            mStyles.textSize = textSize;
        }
        reloadStyles();
    }

    /**
     * ss
     *
     * @param verticalLabelsAlign the alignment of the vertical labels
     */
    public void setVerticalLabelsAlign(int verticalLabelsAlign) {
        mStyles.verticalLabelsAlign = verticalLabelsAlign;
    }

    /**
     * ss
     *
     * @param verticalLabelsColor the color of the vertical labels
     */
    public void setVerticalLabelsColor(int verticalLabelsColor) {
        mStyles.verticalLabelsColor = verticalLabelsColor;
    }

    /**
     * ss
     *
     * @param horizontalLabelsColor the color of the horizontal labels
     */
    public void setHorizontalLabelsColor(int horizontalLabelsColor) {
        mStyles.horizontalLabelsColor = horizontalLabelsColor;
    }

    /**
     * ss
     *
     * @param horizontalLabelsAngle the angle of the horizontal labels in degrees
     */
    public void setHorizontalLabelsAngle(int horizontalLabelsAngle) {
        mStyles.horizontalLabelsAngle = horizontalLabelsAngle;
    }

    /**
     * ss
     *
     * @param gridColor the color of the grid lines
     */
    public void setGridColor(int gridColor) {
        mStyles.gridColor = gridColor;
        reloadStyles();
    }

    /**
     * ss
     *
     * @param highlightZeroLines flag whether the zero-lines (vertical+
     *                           horizontal) shall be highlighted
     */
    public void setHighlightZeroLines(boolean highlightZeroLines) {
        mStyles.highlightZeroLines = highlightZeroLines;
    }

    /**
     * ss
     *
     * @param padding the padding around the graph and labels
     */
    public void setPadding(int padding) {
        if (padding < 15) {
            mStyles.padding = 15;
        } else {
            mStyles.padding = padding;
        }
    }

    /**
     * ss
     *
     * @return the label formatter, that converts
     * the raw numbers to strings
     */
    public LabelFormatter getLabelFormatter() {
        return mLabelFormatter;
    }

    /**
     * ss
     *
     * @param mLabelFormatter the label formatter, that converts
     *                        the raw numbers to strings
     */
    public void setLabelFormatter(LabelFormatter mLabelFormatter) {
        this.mLabelFormatter = mLabelFormatter;
        mLabelFormatter.setViewport(mGraphView.getViewport());
    }

    /**
     * ss
     *
     * @return the title of the horizontal axis
     */
    public String getHorizontalAxisTitle() {
        return mHorizontalAxisTitle;
    }

    /**
     * ss
     *
     * @param mHorizontalAxisTitle the title of the horizontal axis
     */
    public void setHorizontalAxisTitle(String mHorizontalAxisTitle) {
        this.mHorizontalAxisTitle = mHorizontalAxisTitle;
    }

    /**
     * ss
     *
     * @return the title of the vertical axis
     */
    public String getVerticalAxisTitle() {
        return mVerticalAxisTitle;
    }

    /**
     * ss
     *
     * @param mVerticalAxisTitle the title of the vertical axis
     */
    public void setVerticalAxisTitle(String mVerticalAxisTitle) {
        this.mVerticalAxisTitle = mVerticalAxisTitle;
    }

    /**
     * ss
     *
     * @return font size of the vertical axis title
     */
    public float getVerticalAxisTitleTextSize() {
        return mStyles.verticalAxisTitleTextSize;
    }

    /**
     * ss
     *
     * @param verticalAxisTitleTextSize font size of the vertical axis title
     */
    public void setVerticalAxisTitleTextSize(float verticalAxisTitleTextSize) {
        if (verticalAxisTitleTextSize <= 0) {
            mStyles.verticalAxisTitleTextSize = 20;
        } else {
            mStyles.verticalAxisTitleTextSize = verticalAxisTitleTextSize;
        }

    }

    /**
     * ss
     *
     * @return font color of the vertical axis title
     */
    public int getVerticalAxisTitleColor() {
        return mStyles.verticalAxisTitleColor;
    }

    /**
     * ss
     *
     * @param verticalAxisTitleColor font color of the vertical axis title
     */
    public void setVerticalAxisTitleColor(int verticalAxisTitleColor) {
        mStyles.verticalAxisTitleColor = verticalAxisTitleColor;
    }

    /**
     * ss
     *
     * @return font size of the horizontal axis title
     */
    public float getHorizontalAxisTitleTextSize() {
        return mStyles.horizontalAxisTitleTextSize;
    }

    /**
     * ss
     *
     * @param horizontalAxisTitleTextSize font size of the horizontal axis title
     */
    public void setHorizontalAxisTitleTextSize(float horizontalAxisTitleTextSize) {
        if (horizontalAxisTitleTextSize > 0) {
            mStyles.horizontalAxisTitleTextSize = horizontalAxisTitleTextSize;
        } else {
            mStyles.horizontalAxisTitleTextSize = 20;
        }

    }

    /**
     * ss
     *
     * @return font color of the horizontal axis title
     */
    public int getHorizontalAxisTitleColor() {
        return mStyles.horizontalAxisTitleColor;
    }

    /**
     * ss
     *
     * @param horizontalAxisTitleColor font color of the horizontal axis title
     */
    public void setHorizontalAxisTitleColor(int horizontalAxisTitleColor) {
        mStyles.horizontalAxisTitleColor = horizontalAxisTitleColor;
    }

    /**
     * ss
     *
     * @return the alignment of the labels on the right side
     */
    public int getVerticalLabelsSecondScaleAlign() {
        return mStyles.verticalLabelsSecondScaleAlign;
    }

    /**
     * ss
     *
     * @param verticalLabelsSecondScaleAlign the alignment of the labels on the right side
     */
    public void setVerticalLabelsSecondScaleAlign(int verticalLabelsSecondScaleAlign) {
        mStyles.verticalLabelsSecondScaleAlign = verticalLabelsSecondScaleAlign;
    }

    /**
     * ss
     *
     * @return the color of the labels on the right side
     */
    public int getVerticalLabelsSecondScaleColor() {
        return mStyles.verticalLabelsSecondScaleColor;
    }

    /**
     * ss
     *
     * @param verticalLabelsSecondScaleColor the color of the labels on the right side
     */
    public void setVerticalLabelsSecondScaleColor(int verticalLabelsSecondScaleColor) {
        mStyles.verticalLabelsSecondScaleColor = verticalLabelsSecondScaleColor;
    }

    /**
     * ss
     *
     * @return the width of the vertical labels
     * of the second scale
     */
    public int getLabelVerticalSecondScaleWidth() {
        return mLabelVerticalSecondScaleWidth == null ? 0 : mLabelVerticalSecondScaleWidth;
    }

    /**
     * ss
     *
     * @return flag whether the horizontal labels are
     * visible
     */
    public boolean isHorizontalLabelsVisible() {
        return mStyles.horizontalLabelsVisible;
    }

    /**
     * ss
     *
     * @param horizontalTitleVisible flag whether the horizontal labels are
     *                               visible
     */
    public void setHorizontalLabelsVisible(boolean horizontalTitleVisible) {
        mStyles.horizontalLabelsVisible = horizontalTitleVisible;
    }

    /**
     * ss
     *
     * @return flag whether the vertical labels are
     * visible
     */
    public boolean isVerticalLabelsVisible() {
        return mStyles.verticalLabelsVisible;
    }

    /**
     * ss
     *
     * @param verticalTitleVisible flag whether the vertical labels are
     *                             visible
     */
    public void setVerticalLabelsVisible(boolean verticalTitleVisible) {
        mStyles.verticalLabelsVisible = verticalTitleVisible;
    }

    /**
     * ss
     *
     * @return count of the vertical labels, that
     * will be shown at one time.
     */
    public int getNumVerticalLabels() {
        return mNumVerticalLabels;
    }

    /**
     * ss
     *
     * @param mNumVerticalLabels count of the vertical labels, that
     *                           will be shown at one time.
     */
    public void setNumVerticalLabels(int mNumVerticalLabels) {
        this.mNumVerticalLabels = mNumVerticalLabels;
    }

    /**
     * ss
     *
     * @return count of the horizontal labels, that
     * will be shown at one time.
     */
    public int getNumHorizontalLabels() {
        return mNumHorizontalLabels;
    }

    /**
     * ss
     *
     * @param mNumHorizontalLabels count of the horizontal labels, that
     *                             will be shown at one time.
     */
    public void setNumHorizontalLabels(int mNumHorizontalLabels) {
        if (mNumHorizontalLabels > 0) {
            this.mNumHorizontalLabels = mNumHorizontalLabels;
        } else {
            this.mNumHorizontalLabels = 20;
        }

    }

    /**
     * ss
     *
     * @return the grid style
     */
    public GridStyle getGridStyle() {
        return mStyles.gridStyle;
    }

    /**
     * Define which grid lines shall be drawn
     *
     * @param gridStyle the grid style
     */
    public void setGridStyle(GridStyle gridStyle) {
        mStyles.gridStyle = gridStyle;
    }

    /**
     * ss
     *
     * @return the space between the labels text and the graph content
     */
    public int getLabelsSpace() {
        return mStyles.labelsSpace;
    }

    /**
     * the space between the labels text and the graph content
     *
     * @param labelsSpace the space between the labels text and the graph content
     */
    public void setLabelsSpace(int labelsSpace) {
        if (labelsSpace > LABELS_SPACE_MAX) {
            mStyles.labelsSpace = LABELS_SPACE_MAX;
        } else if (labelsSpace < LABELS_SPACE_MIN) {
            mStyles.labelsSpace = LABELS_SPACE_MIN;
        } else {
            mStyles.labelsSpace = labelsSpace;
        }

    }


    /**
     * set horizontal label align
     *
     * @param align
     */
    public void setVerticalLabelsVAlign(VerticalLabelsVAlign align) {
        mStyles.verticalLabelsVAlign = align;
    }

    /**
     * Get horizontal label align
     *
     * @return align
     */
    public VerticalLabelsVAlign getVerticalLabelsVAlign() {
        return mStyles.verticalLabelsVAlign;
    }
}
