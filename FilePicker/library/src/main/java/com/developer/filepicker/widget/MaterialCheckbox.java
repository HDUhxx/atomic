package com.developer.filepicker.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;

/**
 * @author akshay sunil masram
 */
public class MaterialCheckbox extends Component implements Component.DrawTask, Component.EstimateSizeListener {

    private int minDim;
    private Paint paint;
    private RectFloat bounds;
    private boolean checked;
    private OnCheckedChangeListener onCheckedChangeListener;
    private Path tick;

    public MaterialCheckbox(Context context) {
        super(context);
        initView();
    }

    public MaterialCheckbox(Context context, AttrSet attrs) {
        super(context, attrs);
        initView();
    }

    public MaterialCheckbox(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        checked = false;
        tick = new Path();
        paint = new Paint();
        setClickedListener(new ClickedListener() {

            @Override
            public void onClick(Component component) {
                setChecked(!checked);
                onCheckedChangeListener.onCheckedChanged(MaterialCheckbox.this, isChecked());
            }
        });

        int height = 50;
        int width = 50;
        minDim = Math.min(width, height);
        bounds = new RectFloat(minDim / 10f, minDim / 10f, minDim - (minDim / 10f), minDim - (minDim / 10f));
        tick.moveTo(minDim / 4f, minDim / 2f);
        tick.lineTo(minDim / 2.5f, minDim - (minDim / 3f));

        tick.moveTo(minDim / 2.75f, minDim - (minDim / 3.25f));
        tick.lineTo(minDim - (minDim / 4f), minDim / 3f);
        addDrawTask(this::onDraw);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (isChecked()) {
            paint.reset();
            paint.setAntiAlias(true);
            bounds = new RectFloat(minDim / 10f, minDim / 10f, minDim - (minDim / 10f), minDim - (minDim / 10f));
            paint.setColor(Color.RED);
            canvas.drawRoundRect(bounds, minDim / 8f, minDim / 8f, paint);

            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(minDim / 10f);
            paint.setStyle(Paint.Style.STROKE_STYLE);
            paint.setStrokeJoin(Paint.Join.BEVEL_JOIN);
            canvas.drawPath(tick, paint);
        } else {
            paint.reset();
            paint.setAntiAlias(true);
            bounds = new RectFloat(minDim / 10f, minDim / 10f, minDim - (minDim / 10f), minDim - (minDim / 10f));
            paint.setColor(Color.GRAY);
            canvas.drawRoundRect(bounds, minDim / 8f, minDim / 8f, paint);

            bounds = new RectFloat(minDim / 5f, minDim / 5f, minDim - (minDim / 5f), minDim - (minDim / 5f));
            paint.setColor(Color.WHITE);
            canvas.drawRect(bounds, paint);
        }
    }

    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        return false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    public void setOnCheckedChangedListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}