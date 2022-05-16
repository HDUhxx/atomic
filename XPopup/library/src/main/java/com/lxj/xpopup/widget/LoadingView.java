package com.lxj.xpopup.widget;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.app.Context;

/**
 * Description: 加载View
 * Create by dance, at 2018/12/18
 */
public class LoadingView extends Component implements Component.DrawTask {

    private Paint paint;
    private float radius;
    private float radiusOffset;
    private float stokeWidth = 2f; // 不是固定不变的，当width为30dp时，它为2dp，当宽度变大，这个也会相应的变大
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int startColor = 0xFFEEEEEE;
    private int endColor = 0xFF111111;
    private int lineCount = 10; // 线的数量
    private float avgAngle = 360f / lineCount;
    private int time = 0; // 重复次数
    private float centerX; // 中心x
    private float centerY; // 中心y
    private float startX;
    private float endX;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public LoadingView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        paint = new Paint();
        paint.setAntiAlias(true);
        stokeWidth = dp2px(context, stokeWidth);
        paint.setStrokeWidth(stokeWidth);
        addDrawTask(this);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (radius == 0) {
            radius = getWidth() / 2;
            radiusOffset = radius / 2.5f;
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
            stokeWidth = dp2px(getContext(), 2);
            paint.setStrokeWidth(stokeWidth);
            startX = centerX + radiusOffset;
            endX = startX + radius / 3f;
        }
        for (int i = lineCount - 1; i >= 0; i--) {
            int temp = Math.abs(i + time) % lineCount;
            float fraction = (temp + 1) * 1f / lineCount;
            int color = argbEvaluator.evaluate(fraction, startColor, endColor);
            paint.setColor(new Color(color));
            canvas.drawLine(new Point(startX, centerY), new Point(endX, centerY), paint);
            // 线的两端画个点，看着圆滑
            canvas.drawCircle(startX, centerY, stokeWidth / 2, paint);
            canvas.drawCircle(endX, centerY, stokeWidth / 2, paint);
            canvas.rotate(avgAngle, centerX, centerY);
        }
        getContext().getUITaskDispatcher().delayDispatch(increaseTask, 80);
    }

    private Runnable increaseTask = new Runnable() {
        @Override
        public void run() {
            time++;
            invalidate();
        }
    };

    private int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResourceManager().getDeviceCapability().screenDensity / 160);
    }

    private class ArgbEvaluator {

        private ArgbEvaluator() {
        }

        /**
         * 获取渐变色
         *
         * @param fraction   表示渐变度，取0.0F-1.0F之间某一值
         * @param startValue 表示起始颜色值
         * @param endValue   表示最终颜色值
         * @return 计算之后得到的颜色
         */
        private int evaluate(float fraction, int startValue, int endValue) {
            int startA = (startValue >> 24);
            int startR = (startValue >> 16) & 0xff;
            int startG = (startValue >> 8) & 0xff;
            int startB = startValue & 0xff;

            int endA = (endValue >> 24);
            int endR = (endValue >> 16) & 0xff;
            int endG = (endValue >> 8) & 0xff;
            int endB = endValue & 0xff;

            return (startA + (int) (fraction * (endA - startA))) << 24
                    | (startR + (int) (fraction * (endR - startR))) << 16
                    | (startG + (int) (fraction * (endG - startG))) << 8
                    | (startB + (int) (fraction * (endB - startB)));
        }
    }

}
