package me.panavtec.qrcodescanner.utils;

import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

/**
 * 屏幕像素工具类
 */
public final class ScreenUtils {

    private ScreenUtils(){}

    /**
     * vp转像素
     *
     * @param context       上下文
     * @param vp            vp值
     * @return int
     */
    public static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context       上下文
     * @return 屏幕宽度
     */
    public static int getDisplayWidthInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointX();
    }

    /**
     * 获取屏幕高度
     *
     * @param context       上下文
     * @return 屏幕宽度
     */
    public static int getDisplayHeightInPX(Context context){
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return (int) point.getPointY();
    }


    private int parseColor(Object color) {
        if (color instanceof String) {
            return Color.getIntColor((String) color);
        } else {
            return (int) color;
        }
    }
}