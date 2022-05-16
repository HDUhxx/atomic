package com.lxj.xpopup.widget;

import com.lxj.xpopup.interfaces.OnClickOutsideListener;
import com.lxj.xpopup.util.EventUtil;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Description:
 * Create by dance, at 2019/1/10
 */
public class PartShadowContainer extends StackLayout implements Component.TouchEventListener {
    public boolean isDismissOnTouchOutside = true;

    public PartShadowContainer(Context context) {
        this(context, null);
    }

    public PartShadowContainer(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    public PartShadowContainer(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        setTouchEventListener(this);
    }

    private float x;
    private float y;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        // 计算implView的Rect
        Component implView = getComponentAt(0);
        int[] location = new int[2];
        Rect rect = implView.getComponentPosition();
        location[0] = rect.top;
        location[1] = rect.left;
        Rect implViewRect = new Rect(location[0], location[1], location[0] + implView.getWidth(),
                location[1] + implView.getHeight());
        if (!XPopupUtils.isInRect(EventUtil.getRawX(event), EventUtil.getRawY(event), implViewRect)) {
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    x = EventUtil.getX(event);
                    y = EventUtil.getY(event);
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    if (isDismissOnTouchOutside) {
                        if (listener != null){
                            listener.onClickOutside();
                        }
                    }
                    x = 0;
                    y = 0;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private OnClickOutsideListener listener;

    public void setOnClickOutsideListener(OnClickOutsideListener listener) {
        this.listener = listener;
    }

}
