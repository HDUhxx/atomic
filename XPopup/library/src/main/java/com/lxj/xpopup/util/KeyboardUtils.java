package com.lxj.xpopup.util;

import com.lxj.xpopup.core.BasePopupView;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.utils.Rect;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description:
 * Create by dance, at 2018/12/17
 */
public final class KeyboardUtils {

    // 软键盘和导航栏的高度和
    public static int sDecorViewInvisibleHeightPre;
    private static HashMap<Component, OnSoftInputChangedListener> listenerMap = new HashMap<>();

    private KeyboardUtils() {

    }

    /**
     * Register soft input changed listener.
     *
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(final BasePopupView popupView, final OnSoftInputChangedListener listener) {
        sDecorViewInvisibleHeightPre = getSoftInputHeight(popupView);
        listenerMap.put(popupView, listener);

        if (popupView.popupInfo.contentRoot != null) {
            // 设置了页面根容器，则用页面根容器监听页面高度变化
            popupView.popupInfo.contentRoot.setLayoutRefreshedListener(new Component.LayoutRefreshedListener() {
                @Override
                public void onRefreshed(Component component) {
                    component.getContext().getUITaskDispatcher().delayDispatch(new Runnable() {
                        @Override
                        public void run() {
                            int height = getSoftInputHeight(popupView);
                            if (sDecorViewInvisibleHeightPre != height) {
                                //通知所有弹窗的监听器输入法高度变化了
                                for (OnSoftInputChangedListener changedListener : listenerMap.values()) {
                                    changedListener.onSoftInputChanged(height);
                                }
                                sDecorViewInvisibleHeightPre = height;
                            }
                        }
                    }, 10);
                }
            });
        } else {
            // 没有页面根容器，则无法监听软键盘高度变化
            LogUtil.debug("XPopup", "如果要监听软键盘高度变化，请调用.setComponent(component)方法设置一个页面中的控件");
        }
    }

    public static void removeLayoutChangeListener(BasePopupView popupView) {
        listenerMap.remove(popupView);
    }

    /**
     * 获取软键盘的高度
     *
     * @param component 任意一个页面中的控件
     * @return 高度，单位px
     */
    public static int getSoftInputHeight(Component component) {
        Rect rect = new Rect();
        component.getWindowVisibleRect(rect);
        return rect.bottom == 0 ? 0 : XPopupUtils.getScreenHeight(component.getContext()) - rect.bottom - XPopupUtils.getNavBarHeight(component);
    }

    /**
     * 控制输入法的显示
     *
     * @param component TextField控件
     */
    public static void showSoftInput(Component component) {
        if (component instanceof TextField) {
            component.requestFocus();
            component.simulateClick();
        }
    }

    /**
     * 隐藏输入法
     *
     * @param component 布局中的任意一个控件
     */
    public static void hideSoftInput(Component component) {
        if (component != null) {
            ArrayList<TextField> textFields = new ArrayList<>();
            XPopupUtils.findAllEditText(textFields, ComponentUtil.getDecorView(component));
            for (int i = 0; i < textFields.size(); i++) {
                textFields.get(i).clearFocus();
            }
        }
    }

    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }

}