/*
 *
 * Copyright 2016 Harish Sridharan

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cooltechworks.views;

import com.cooltechworks.utils.AsyncTask;
import com.cooltechworks.utils.BitmapUtils;

import com.cooltechworks.scratchview.ResourceTable;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.element.Element;
import ohos.agp.render.BlendMode;
import ohos.agp.render.Canvas;
import ohos.agp.render.LinearShader;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.PixelMapShader;
import ohos.agp.render.Shader;
import ohos.agp.render.Texture;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;

import java.io.IOException;


/**
 * Created by Harish on 25/03/16.
 */
public class ScratchImageView extends Image implements Component.TouchEventListener, Component.DrawTask {


    public interface IRevealListener {
        public void onRevealed(ScratchImageView iv);
        public void onRevealPercentChangedListener(ScratchImageView siv, float percent);
    }

    public static final float STROKE_WIDTH = 12f;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    /**
     * Bitmap holding the scratch region.
     */
    private PixelMap mScratchBitmap;

    /**
     * Drawable canvas area through which the scratchable area is drawn.
     */
    private Canvas mCanvas;

    /**
     * Path holding the erasing path done by the user.
     */
    private Path mErasePath;

    /**
     * Path to indicate where the user have touched.
     */
    private Path mTouchPath;

    /**
     * Paint properties for drawing the scratch area.
     */
    private Paint mBitmapPaint;

    /**
     * Paint properties for erasing the scratch region.
     */
    private Paint mErasePaint;

    /**
     * Gradient paint properties that lies as a background for scratch region.
     */
    private Paint mGradientBgPaint;

    /**
     * Sample Drawable bitmap having the scratch pattern.
     */
    private PixelMap foregroundPixelMap;

    /**
     * Listener object callback reference to send back the callback when the image has been revealed.
     */
    private IRevealListener mRevealListener;

    /**
     * Reveal percent value.
     */
    private float mRevealPercent;

    /**
     * Thread Count
     */
    private int mThreadCount = 0;

    private HiLogLabel hiLogLabel = new HiLogLabel(HiLog.LOG_APP, 0x7845, "ScratchImageView");

    public ScratchImageView(Context context) {
        super(context);
        init();

    }

    public ScratchImageView(Context context, AttrSet set) {
        super(context, set);
        init();
    }

    public ScratchImageView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Set the strokes width based on the parameter multiplier.
     * @param multiplier can be 1,2,3 and so on to set the stroke width of the paint.
     */
    public void setStrokeWidth(int multiplier) {
        mErasePaint.setStrokeWidth(multiplier * STROKE_WIDTH);
    }

    /**
     * Initialises the paint drawing elements.
     */
    private void init() {


        mTouchPath = new Path();

        mErasePaint = new Paint();
        mErasePaint.setAntiAlias(true);
        mErasePaint.setDither(true);
        mErasePaint.setColor(new Color(0xFFFF0000));
        mErasePaint.setStyle(Paint.Style.STROKE_STYLE);
        mErasePaint.setStrokeJoin(Paint.Join.BEVEL_JOIN);
        mErasePaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        setStrokeWidth(6);

        mGradientBgPaint = new Paint();

        mErasePath = new Path();
        mBitmapPaint = new Paint();
        mBitmapPaint.setDither(true);

        try {
            Resource resource = getResourceManager().getResource(ResourceTable.Media_ic_scratch_pattern);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(resource, srcOpts);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            foregroundPixelMap = imageSource.createPixelmap(decodingOptions);
        } catch (IOException | NotExistException e) {
            e.printStackTrace();
        }

        setTouchEventListener(this);
        addDrawTask(this);
        setEraserMode();
        initLayer();
    }

    private void initLayer() {
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.size = new Size(getWidth(), getHeight());
        initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
        mScratchBitmap = PixelMap.create(initializationOptions);

        Texture texture = new Texture(mScratchBitmap);
        mCanvas = new Canvas(texture);

        Rect rect = new Rect(0, 0, mScratchBitmap.getImageInfo().size.width, mScratchBitmap.getImageInfo().size.height);

        int startGradientColor = 0;
        int endGradientColor = 0;
        try {
            ResourceManager resourceManager = getResourceManager();
            startGradientColor = resourceManager.getElement(ResourceTable.Color_scratch_start_gradient).getColor();
            endGradientColor = resourceManager.getElement(ResourceTable.Color_scratch_end_gradient).getColor();
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }

        Point point1 = new Point(0, 0);
        Point point2 = new Point(0, getHeight());
        Point[] points = new Point[]{point1, point2};
        Color[] colors = new Color[]{new Color(startGradientColor), new Color(endGradientColor)};
        mGradientBgPaint.setShader(new LinearShader(points, null, colors, Shader.TileMode.MIRROR_TILEMODE), Paint.ShaderType.LINEAR_SHADER);

        mCanvas.drawRect(rect, mGradientBgPaint);

        Paint paint = new Paint();
        PixelMapHolder pixelMapHolder = new PixelMapHolder(foregroundPixelMap);
        PixelMapShader pixelMapShader = new PixelMapShader(pixelMapHolder, Shader.TileMode.REPEAT_TILEMODE, Shader.TileMode.REPEAT_TILEMODE);
        paint.setShader(pixelMapShader, Paint.ShaderType.PIXELMAP_SHADER);
        RectFloat dstRect = new RectFloat(0, 0, getWidth(), getHeight());
        mCanvas.drawRect(dstRect, paint);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        PixelMapHolder pixelMapHolder = new PixelMapHolder(mScratchBitmap);
        canvas.drawPixelMapHolder(pixelMapHolder, 0, 0, mBitmapPaint);
        canvas.drawPath(mErasePath, mErasePaint);

    }

    private void touch_start(float x, float y) {
        mErasePath.reset();
        mErasePath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    /**
     * clears the scratch area to reveal the hidden image.
     */
    public void clear() {

        int[] bounds = getImageBounds();
        int left = bounds[0];
        int top = bounds[1];
        int right = bounds[2];
        int bottom = bounds[3];

        int width = right - left;
        int height = bottom - top;
        int centerX = left + width / 2;
        int centerY = top + height / 2;

        left = centerX - width / 2;
        top = centerY - height / 2;
        right = left + width;
        bottom = top + height;

        Paint paint = new Paint();
        paint.setBlendMode(BlendMode.CLEAR);

        mCanvas.drawRect(left, top, right, bottom, paint);
        checkRevealed();
        invalidate();
    }


    private void touch_move(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mErasePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            drawPath();
        }


        mTouchPath.reset();
        mTouchPath.addCircle(mX, mY, 30, Path.Direction.CLOCK_WISE);

    }

    private void drawPath() {
        mErasePath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mErasePath, mErasePaint);
        // kill this so we don't double draw
        mTouchPath.reset();
        mErasePath.reset();
        mErasePath.moveTo(mX, mY);

        checkRevealed();
    }

    public void reveal() {
        clear();
    }

    private void touch_up() {

        drawPath();
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        float x = event.getPointerPosition(0).getX();
        float y = event.getPointerPosition(0).getY();

        switch (event.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case TouchEvent.POINT_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                touch_up();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public int getColor() {
        return mErasePaint.getColor().getValue();
    }


    public Paint getErasePaint() {
        return mErasePaint;
    }

    public void setEraserMode() {

        getErasePaint().setBlendMode(BlendMode.CLEAR);

    }

    public void setRevealListener(IRevealListener listener) {
        this.mRevealListener = listener;
    }

    public boolean isRevealed() {
        return mRevealPercent == 1;
    }

    private void checkRevealed() {

        if(! isRevealed() && mRevealListener != null) {

            int[] bounds = getImageBounds();
            int left = bounds[0];
            int top = bounds[1];
            int width = bounds[2] - left;
            int height = bounds[3] - top;

            // Do not create multiple calls to compare.
            if(mThreadCount > 1) {
                HiLog.debug(hiLogLabel, "Count greater than 1");
                return;
            }

            mThreadCount++;

            new AsyncTask<Integer, Void, Float>() {

                @Override
                protected Float doInBackground(Integer... params) {

                    try {
                        int left = params[0];
                        int top = params[1];
                        int width = params[2];
                        int height = params[3];

                        ohos.media.image.common.Rect rect = new ohos.media.image.common.Rect(left, top, width, height);
                        PixelMap croppedBitmap = PixelMap.create(mScratchBitmap, rect, new PixelMap.InitializationOptions());

                        return BitmapUtils.getTransparentPixelPercent(croppedBitmap);
                    } finally {
                        mThreadCount--;
                    }
                }

                public void onPostExecute(Float percentRevealed) {

                    // check if not revealed before.
                    if( ! isRevealed()) {

                        float oldValue = mRevealPercent;
                        mRevealPercent = percentRevealed;

                        if(oldValue != percentRevealed) {
                            mRevealListener.onRevealPercentChangedListener(ScratchImageView.this, percentRevealed);
                        }

                        // if now revealed.
                        if( isRevealed()) {
                            mRevealListener.onRevealed(ScratchImageView.this);
                        }
                    }
                }
            }.execute(left, top, width, height);

        }
    }

    public int[] getImageBounds() {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int vwidth = getWidth() - paddingLeft - paddingRight;
        int vheight = getHeight() - paddingBottom - paddingTop;

        int centerX = vwidth/2;
        int centerY = vheight/2;


        Element drawable = getImageElement();
        int width = 0;
        int height = 0;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            width = drawable.getWidth();
            height = drawable.getHeight();

            if (bounds != null) {
                if(width <= 0) {
                    width = bounds.right - bounds.left;
                }

                if(height <= 0) {
                    height = bounds.bottom - bounds.top;
                }
            }
        }

        int left;
        int top;

        if(height > vheight) {
            height = vheight;
        }

        if(width > vwidth) {
            width = vwidth;
        }


        ScaleMode scaleType = getScaleMode();

        switch (scaleType) {
            case ZOOM_START:
                left = paddingLeft;
                top = centerY - height / 2;
                break;
            case ZOOM_END:
                left = vwidth - paddingRight - width;
                top = centerY - height / 2;
                break;
            case CENTER:
                left = centerX - width / 2;
                top = centerY - height / 2;
                break;
            default:
                left = paddingLeft;
                top = paddingTop;
                width = vwidth;
                height = vheight;
                break;

        }

        return new int[] {left, top, left + width, top + height};
    }



}
