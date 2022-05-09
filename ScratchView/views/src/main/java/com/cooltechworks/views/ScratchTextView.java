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
import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.render.BlendMode;
import ohos.agp.render.Canvas;
import ohos.agp.render.LinearShader;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.PixelMapShader;
import ohos.agp.render.Shader;
import ohos.agp.render.Texture;
import ohos.agp.text.Font;
import ohos.agp.text.RichText;
import ohos.agp.text.RichTextBuilder;
import ohos.agp.text.RichTextLayout;
import ohos.agp.text.TextForm;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;

import java.io.IOException;

/**
 * Created by Harish on 25/03/16.
 */
public class ScratchTextView extends Text implements Component.TouchEventListener, Component.DrawTask {


    public interface IRevealListener {
        public void onRevealed(ScratchTextView tv);
        public void onRevealPercentChangedListener(ScratchTextView stv, float percent);
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
     * Listener object callback reference to send back the callback when the text has been revealed.
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

    private Context mContext;


    public ScratchTextView(Context context) {
        super(context);
        mContext = context;
        init();

    }

    public ScratchTextView(Context context, AttrSet set) {
        super(context, set);
        mContext = context;
        init();
    }

    public ScratchTextView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
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
        mErasePaint.setBlendMode(BlendMode.CLEAR);
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

    /**
     * Reveals the hidden text by erasing the scratch area.
     */
    public void reveal() {

        int[] bounds = getTextBounds(1.5f);
        int left = bounds[0];
        int top = bounds[1];
        int right = bounds[2];
        int bottom = bounds[3];

        Paint paint = new Paint();
        paint.setBlendMode(BlendMode.CLEAR);

        mCanvas.drawRect(left, top, right, bottom, paint);
        checkRevealed();
        invalidate();
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
                drawPath();
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


    public void setRevealListener(IRevealListener listener) {
        this.mRevealListener = listener;
    }

    public boolean isRevealed() {
        return mRevealPercent == 1;
    }

    private void checkRevealed() {

        if(! isRevealed() && mRevealListener != null) {

            int[] bounds = getTextBounds();
            int left = bounds[0];
            int top = bounds[1];
            int width = bounds[2] - left;
            int height = bounds[3] - top;


            // Do not create multiple calls to compare.
            if(mThreadCount > 1) {
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
                            mRevealListener.onRevealPercentChangedListener(ScratchTextView.this, percentRevealed);
                        }

                        // if now revealed.
                        if( isRevealed()) {
                            mRevealListener.onRevealed(ScratchTextView.this);
                        }
                    }
                }
            }.execute(left, top, width, height);

        }
    }


    private static int[] getTextDimens(String text, Paint paint) {
        Rect bounds = paint.getTextBounds(text);
        int width = bounds.left + bounds.getWidth();
        int height = bounds.bottom + bounds.getHeight();

        return new int[] { width, height};
    }

    private int[] getTextBounds() {
        return getTextBounds(1f);
    }

    private int[] getTextBounds(float scale) {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int vwidth = getWidth();
        int vheight = getHeight();

        int centerX = vwidth/2;
        int centerY = vheight/2;


        Paint paint = new Paint();

        String text = getText().toString();

        int[] dimens = getTextDimens(text, paint);
        int width = dimens[0];
        int height = dimens[1];

        int lines = getTextLineCount(this, text);
        height = height * lines;
        width = width / lines;


        int left = 0;
        int top = 0;

        if(height > vheight) {
            height = vheight - ( paddingBottom + paddingTop);
        }
        else {
            height = (int) (height * scale);
        }

        if(width > vwidth) {
            width = vwidth - (paddingLeft + paddingRight);
        }
        else {
            width = (int) (width * scale);
        }

        int gravity = getTextAlignment();


        //todo Gravity.START
        if((gravity & TextAlignment.LEFT) == TextAlignment.LEFT) {
            left = paddingLeft;
        }
        //todo Gravity.END
        else if((gravity & TextAlignment.RIGHT) == TextAlignment.RIGHT) {
            left = (vwidth - paddingRight) - width;
        }
        else if((gravity & TextAlignment.HORIZONTAL_CENTER) == TextAlignment.HORIZONTAL_CENTER) {
            left = centerX - width / 2;
        }

        if((gravity & TextAlignment.TOP) == TextAlignment.TOP) {
            top = paddingTop;
        }
        else if((gravity & TextAlignment.BOTTOM) == TextAlignment.BOTTOM) {
            top = (vheight - paddingBottom) - height;
        }

        else if((gravity & TextAlignment.VERTICAL_CENTER) == TextAlignment.VERTICAL_CENTER) {
            top = centerY - height / 2;
        }

        return new int[] {left, top, left + width, top + height};
    }

    private int getTextLineCount(Text text, String content) {
        int deviceWidth = DisplayManager.getInstance().getDefaultDisplay(mContext).get().getAttributes().width;
        int componentWidth = text.getWidth();
        int width = componentWidth == 0 ? deviceWidth : componentWidth;
        TextForm textForm = new TextForm();
        textForm.setTextColor(Color.getIntColor("#999999"));
        textForm.setTextSize(AttrHelper.fp2px(15, mContext));
        Font boldFont = new Font.Builder("").setWeight(Font.BOLD).build();
        textForm.setTextFont(boldFont);
        RichText richText = new RichTextBuilder(textForm).addText(content).build();

        text.setRichText(richText);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, text.getWidth(), text.getHeight());
        RichTextLayout richTextLayout = new RichTextLayout(richText, paint, rect, width);
        float textWidth = richTextLayout.calculateTextWidth(richText);
        return (int) Math.ceil(textWidth / width);
    }

}
