package com.lxj.xpopup.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;

/**
 * Description: 大图浏览弹窗显示后的占位View
 * Create by lxj, at 2019/2/2
 */
public class BlankView extends Component implements Component.DrawTask {

    public BlankView(Context context) {
        super(context);
    }

    public BlankView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init();
    }

    public BlankView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    private Paint paint = new Paint();
    private RectFloat rect = null;
    public int radius = 0;
    public int color = 0xffffffff;
    public int strokeColor = 0xddddddff;

    private void init() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        addDrawTask(this);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        rect = new RectFloat(0, 0, getWidth(), getHeight());
        paint.setColor(new Color(color));
        canvas.drawRoundRect(rect, radius, radius, paint);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setColor(new Color(strokeColor));
        canvas.drawRoundRect(rect, radius, radius, paint);
        paint.setStyle(Paint.Style.FILL_STYLE);
    }
}
