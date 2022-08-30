package com.allenliu.badgeview;


import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.StackLayout;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.Optional;


public class BadgeView extends Component implements Component.DrawTask {

    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_RECTANGLE = 2;
    public static final int SHAPE_OVAL = 3;
    public static final int SHAPTE_ROUND_RECTANGLE = 4;
    public static final int SHAPE_SQUARE = 5;
    private Paint numberPaint;
    private Paint backgroundPaint;
    private int currentShape = SHAPE_CIRCLE;
    private int defaultTextColor = Color.WHITE.getValue();
    private int defaultTextSize;
    private int defaultBackgroundColor = Color.RED.getValue();
    private String showText = "";
    private int badgeGravity = LayoutAlignment.RIGHT | LayoutAlignment.TOP;
    private int leftMargin = 0;
    private int topMargin = 0;
    private int bottomMargin = 0;
    private int rightMargin = 0;
    private boolean hasBind=false;
    private int horiontalSpace=0;
    private int verticalSpace=0;
    public BadgeView(Context context) {
        super(context);
        init(context);
    }
    public BadgeView(Context context, AttrSet attrs) {
        super(context, attrs);
        init(context);
    }
    public BadgeView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        defaultTextSize = dip2px(context, 1);
        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(new Color(defaultTextColor));
        numberPaint.setStyle(Paint.Style.FILL_STYLE);
        numberPaint.setTextSize(defaultTextSize);
        numberPaint.setTextAlign(TextAlignment.CENTER);
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(new Color(defaultBackgroundColor));
        backgroundPaint.setStyle(Paint.Style.FILL_STYLE);
        StackLayout.LayoutConfig params = new StackLayout.LayoutConfig(StackLayout.LayoutConfig.MATCH_CONTENT, StackLayout.LayoutConfig.MATCH_CONTENT);
        params.alignment = badgeGravity;
        setLayoutConfig(params);
        addDrawTask(this);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        RectFloat rectF = new RectFloat(0, 0, getWidth(), getHeight());
        Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
        float textH = fontMetrics.descent - fontMetrics.ascent;
        switch (currentShape) {
            case SHAPE_CIRCLE:
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2, backgroundPaint);
                canvas.drawText(numberPaint,showText, getWidth() / 2f, getHeight() / 2f + (textH / 2f - fontMetrics.descent));
                break;
            case SHAPE_OVAL:

                canvas.drawOval(rectF, backgroundPaint);
                canvas.drawText( numberPaint,showText, getWidth() / 2f, getHeight() / 2f + (textH / 2f - fontMetrics.descent));
                break;
            case SHAPE_RECTANGLE:
                canvas.drawRect(rectF, backgroundPaint);
                canvas.drawText(numberPaint,showText, getWidth() / 2f, getHeight() / 2f + (textH / 2f - fontMetrics.descent));
                break;
            case SHAPE_SQUARE:
                int sideLength = Math.min(getHeight(), getWidth());
                RectFloat squareF = new RectFloat(0, 0, sideLength, sideLength);
                canvas.drawRect(squareF, backgroundPaint);
                canvas.drawText(numberPaint,showText, sideLength / 2f, sideLength / 2f + (textH / 2f - fontMetrics.descent));
                break;
            case SHAPTE_ROUND_RECTANGLE:
                canvas.drawRoundRect(rectF, dip2px(getContext(), 5), dip2px(getContext(), 5), backgroundPaint);
                canvas.drawText(numberPaint,showText, getWidth() / 2f, getHeight() / 2f + (textH / 2f - fontMetrics.descent));
                break;
        }

    }




    private int dip2px(Context context, int dip) {
        Optional<Display> defaultDisplay = DisplayManager.getInstance().getDefaultDisplay(getContext());
        if(defaultDisplay.isPresent()){
            Display display = defaultDisplay.get();
            return (int)(display.getAttributes().scalDensity*dip+0.5f);
        }
        return 0;
    }

    /** 将sp转换成px
     *
     * @param context 上下文
     * @param spValue 需要修改的值
     * @return 修改后的值
     */
    private int sp2px(Context context, float spValue) {
        Optional<Display> defaultDisplay = DisplayManager.getInstance().getDefaultDisplay(getContext());
        if(defaultDisplay.isPresent()){
            Display display = defaultDisplay.get();
            return (int)(display.getAttributes().scalDensity*spValue+0.5f);
        }
        return 0;
    }

    public BadgeView setShape(int shape) {
        currentShape = shape;
        invalidate();
        return this;
    }

    /**
     * 设置badgeview宽和高
     * @param w dip
     * @param h dip this unit is dip
     * @return BadgeView对象
     */
    public BadgeView setWidthAndHeight(int w, int h) {
        StackLayout.LayoutConfig params = (StackLayout.LayoutConfig) getLayoutConfig();
        params.width = dip2px(getContext(), w);
        params.height = dip2px(getContext(), h);
        setLayoutConfig(params);
        return this;
    }



    /**
     * set bindview margin that you can change badges positon
     *
     * @param left   the unit is dip
     * @param top
     * @param right
     * @param bottom
     * @return BadgeView对象
     *
     */
    @Deprecated
    public BadgeView setMargin(int left, int top, int right, int bottom) {
        leftMargin = dip2px(getContext(), left);
        bottomMargin = dip2px(getContext(), bottom);
        topMargin = dip2px(getContext(), top);
        rightMargin = dip2px(getContext(), right);
        invalidate();
        return this;
    }

    /**
     *设置badgeview内容间距
     *
     * @param horitontal  horitontal space  unit dp
     * @param vertical    vertical space unnit dp
     * @return BadgeView对象
     */
    public BadgeView setSpace(int horitontal, int vertical){
        horiontalSpace=dip2px(getContext(), horitontal);
        verticalSpace=dip2px(getContext(), vertical);
        invalidate();
        return  this;
    }
    /**
     * 设置badgeview内字体大小
     * @param sp the unit is sp
     * @return BadgeView对象
     */
    public BadgeView setTextSize(int sp) {
        defaultTextSize = sp2px(getContext(), sp);
        numberPaint.setTextSize(sp2px(getContext(), sp));
        invalidate();
        return this;
    }

    /**
     * 设置badgeview颜色值
     *
     * @param color 颜色值
     * @return badgeview对象
     */
    public BadgeView setTextColor(int color) {
        defaultTextColor = color;
        numberPaint.setColor(new Color(color));
        invalidate();
        return this;
    }

    /**
     * 设置背景颜色值
     *
     * @param color 背景颜色
     * @return badgeview对象
     */
    public BadgeView setBadgeBackground(int color) {
        defaultBackgroundColor = color;
        backgroundPaint.setColor(new Color(color));
        invalidate();
        return this;
    }

    public BadgeView setBadgeCount(int count) {
        showText = String.valueOf(count);
        invalidate();
        return this;
    }

    public BadgeView setBadgeCount(String count) {
        showText = count;
        invalidate();
        return this;
    }
    /**
     * set gravity must be before @link bind() method
     *
     * @param gravity
     * @return BadgeView对象
     */
    public BadgeView setBadgeGravity(int gravity) {
        badgeGravity = gravity;
        StackLayout.LayoutConfig params = (StackLayout.LayoutConfig) getLayoutConfig();
        params.alignment = gravity;
        setLayoutConfig(params);
        return this;
    }

    public BadgeView bind(Component view) {
        if (getComponentParent() != null){
            ((ComponentContainer) getComponentParent()).removeComponent(this);
        }
        if (view == null){
            return this;
        }
        if ((view.getComponentParent() instanceof StackLayout)&&hasBind==true)
        {
            ((StackLayout) view.getComponentParent()).addComponent(this);
            return this;
        } else if (view.getComponentParent() instanceof ComponentContainer) {
            ComponentContainer parentContainer = (ComponentContainer) view.getComponentParent();
            int viewIndex = ((ComponentContainer) view.getComponentParent()).getChildIndex(view);
            ((ComponentContainer) view.getComponentParent()).removeComponent(view);
            StackLayout container = new StackLayout(getContext());
            ComponentContainer.LayoutConfig containerParams = view.getLayoutConfig();
            int origionHeight=containerParams.height;
            int origionWidth=containerParams.width;
            StackLayout.LayoutConfig viewLayoutParams =new StackLayout.LayoutConfig( origionWidth, origionHeight);
            if(origionHeight==ComponentContainer.LayoutConfig.MATCH_CONTENT){
                containerParams.height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
                viewLayoutParams.setMarginTop(topMargin);
                viewLayoutParams.setMarginBottom(bottomMargin);
            }else{
                containerParams.height =origionHeight+topMargin+bottomMargin+verticalSpace;
            }
            if(origionWidth==ComponentContainer.LayoutConfig.MATCH_CONTENT){
                containerParams.width = ComponentContainer.LayoutConfig.MATCH_CONTENT;
                viewLayoutParams.setMarginLeft(leftMargin);
                viewLayoutParams.setMarginRight(rightMargin);
            }else{
                containerParams.width=origionWidth+rightMargin+horiontalSpace+leftMargin;
            }
            container.setLayoutConfig(containerParams);

            //setGravity
            StackLayout.LayoutConfig params = (StackLayout.LayoutConfig) getLayoutConfig();
            if(params.alignment==(LayoutAlignment.RIGHT|LayoutAlignment.TOP)||params.alignment==LayoutAlignment.RIGHT||params.alignment==LayoutAlignment.TOP){
                view.setPadding(0,verticalSpace,horiontalSpace,0);
                viewLayoutParams.alignment=LayoutAlignment.LEFT|LayoutAlignment.BOTTOM;
            }else if(params.alignment==(LayoutAlignment.LEFT|LayoutAlignment.TOP)||params.alignment==LayoutAlignment.LEFT||params.alignment==LayoutAlignment.TOP){
                view.setPadding(horiontalSpace,verticalSpace,0,0);
                viewLayoutParams.alignment=LayoutAlignment.RIGHT|LayoutAlignment.BOTTOM;
            }else if(params.alignment==(LayoutAlignment.LEFT|LayoutAlignment.BOTTOM)){
                view.setPadding(horiontalSpace,0,0,verticalSpace);
                viewLayoutParams.alignment=LayoutAlignment.RIGHT|LayoutAlignment.TOP;
            }else if(params.alignment==(LayoutAlignment.RIGHT|LayoutAlignment.BOTTOM)){
                view.setPadding(0,0,horiontalSpace,verticalSpace);
                viewLayoutParams.alignment=LayoutAlignment.LEFT|LayoutAlignment.TOP;
            }else{
                view.setPadding(0,verticalSpace,horiontalSpace,0);
                viewLayoutParams.alignment=LayoutAlignment.LEFT|LayoutAlignment.BOTTOM;
            }

            view.setLayoutConfig(viewLayoutParams);
            container.setId(view.getId());
            container.addComponent(view);
            container.addComponent(this);
            parentContainer.addComponent(container, viewIndex);
            hasBind=true;
        } else if (view.getComponentParent() == null) {
           return this;
        }
        return this;
    }

    public boolean unbind() {
        if (getComponentParent() != null) {
            ((ComponentContainer) getComponentParent()).removeComponent(this);
            return true;
        }
        return false;
    }

    public String getBadgeCount() {
        return showText;
    }

}
