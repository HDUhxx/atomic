package com.allenliu.badgeview;


import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;

/**
 * Created by Allen Liu on 2016/7/15.
 */
public class BadgeFactory {
    public static BadgeView createDot(Context context){
        return  new BadgeView(context).setWidthAndHeight(10,10).setTextSize(0).setBadgeGravity(LayoutAlignment.RIGHT| LayoutAlignment.TOP).setShape(BadgeView.SHAPE_CIRCLE);
    }
    public static BadgeView createCircle(Context context){
        return  new BadgeView(context).setWidthAndHeight(20,20).setTextSize(12).setBadgeGravity(LayoutAlignment.RIGHT| LayoutAlignment.TOP).setShape(BadgeView.SHAPE_CIRCLE);
    }
    public static BadgeView createRectangle(Context context){
        return  new BadgeView(context).setWidthAndHeight(25,20).setTextSize(12).setBadgeGravity(LayoutAlignment.RIGHT| LayoutAlignment.TOP).setShape(BadgeView.SHAPE_RECTANGLE);
    }
    public static BadgeView createOval(Context context){
        return  new BadgeView(context).setWidthAndHeight(25,20).setTextSize(12).setBadgeGravity(LayoutAlignment.RIGHT| LayoutAlignment.TOP).setShape(BadgeView.SHAPE_OVAL);
    }
    public static BadgeView createSquare(Context context){
        return  new BadgeView(context).setWidthAndHeight(20,20).setTextSize(12).setBadgeGravity(LayoutAlignment.RIGHT| LayoutAlignment.TOP).setShape(BadgeView.SHAPE_SQUARE);
    }
    public static BadgeView createRoundRect(Context context){
        return  new BadgeView(context).setWidthAndHeight(25,20).setTextSize(12).setBadgeGravity(LayoutAlignment.RIGHT| LayoutAlignment.TOP).setShape(BadgeView.SHAPTE_ROUND_RECTANGLE);
    }
    public static BadgeView create(Context context){
        return  new BadgeView(context);
    }

}
