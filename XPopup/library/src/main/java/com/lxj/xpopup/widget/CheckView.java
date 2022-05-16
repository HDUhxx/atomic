package com.lxj.xpopup.widget;

import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * Description: 对勾View
 * Create by dance, at 2018/12/21
 */
public class CheckView extends Component implements Component.DrawTask {

    Paint paint;
    Color color = Color.TRANSPARENT;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public CheckView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(XPopupUtils.vp2px(context, 2));
        paint.setStyle(Paint.Style.STROKE_STYLE);
        addDrawTask(this);
    }

    /**
     * 设置对勾View
     *
     * @param color 对勾的画笔颜色
     */
    public void setColor(Color color) {
        this.color = color;
        paint.setColor(color);
        invalidate();
    }

    Path path = new Path();

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (color == Color.TRANSPARENT) {
            return;
        }
        // first part
        path.moveTo(getWidth() / 4, getHeight() / 2);
        path.lineTo(getWidth() / 2, getHeight() * 3 / 4);
        // second part
        path.lineTo(getWidth(), getHeight() / 4);
        canvas.drawPath(path, paint);
    }

}
