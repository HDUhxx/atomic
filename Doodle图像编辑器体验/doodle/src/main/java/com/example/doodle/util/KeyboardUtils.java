/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.doodle.util;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.TextField;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.TouchEvent;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 软键盘控制
 *
 * @since 2021-04-27
 */
public class KeyboardUtils implements Component.LayoutRefreshedListener {
    private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();
    private ComponentContainer mRootView;
    private Boolean isPrevValue = null;
    private SoftKeyboardToggleListener mCallback;

    private KeyboardUtils(Component component, SoftKeyboardToggleListener listener) {
        mCallback = listener;
        mRootView = (ComponentContainer) getRootView(component);
        mRootView.setLayoutRefreshedListener(this);
    }

    private KeyboardUtils(ComponentContainer viewGroup, SoftKeyboardToggleListener listener) {
        mCallback = listener;
        mRootView = viewGroup;
        mRootView.setLayoutRefreshedListener(this);
    }

    @Override
    public void onRefreshed(Component component) {
        boolean isVisible = isSoftInputShow(mRootView);
        if (mCallback != null && (isPrevValue == null || isVisible != isPrevValue)) {
            isPrevValue = isVisible;
            mCallback.onToggleSoftKeyboard(isVisible);
        }
    }

    /**
     * fuqiangping
     *
     * @since 2021-04-29
     */
    public interface SoftKeyboardToggleListener {
        /**
         * 键盘显示状态监听回调
         *
         * @param isVisible 键盘是否显示
         */
        void onToggleSoftKeyboard(boolean isVisible);
    }

    private void removeListener() {
        mCallback = null;
        mRootView.setLayoutRefreshedListener(null);
    }

    /**
     * 软键盘以覆盖当前界面的形式出现
     *
     * @param activity
     */
    public static void setSoftInputAdjustNothing(Ability activity) {
        activity.getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_NOTHING
            | WindowManager.LayoutConfig.INPUT_STATE_HIDDEN);
    }

    /**
     * 软键盘以顶起当前界面的形式出现, 注意这种方式会使得当前布局的高度发生变化，触发当前布局onSizeChanged方法回调，这里前后高度差就是软键盘的高度了
     *
     * @param activity
     */
    public static void setSoftInputAdjustResize(Ability activity) {
        activity.getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE
            | WindowManager.LayoutConfig.INPUT_STATE_HIDDEN);
    }

    /**
     * 软键盘以上推当前界面的形式出现, 注意这种方式不会改变布局的高度
     *
     * @param activity
     */
    public static void setSoftInputAdjustPan(Ability activity) {
        activity.getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN
            | WindowManager.LayoutConfig.INPUT_STATE_HIDDEN);
    }

    /**
     * 添加软键盘监听
     *
     * @param act calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(Component act, SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);
        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    /**
     * 添加软键盘监听
     *
     * @param act calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(ComponentContainer act, SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);
        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    /**
     * 移除软键盘监听
     *
     * @param listener {@link SoftKeyboardToggleListener}
     */
    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener) {
        if (sListenerMap.containsKey(listener)) {
            KeyboardUtils keyboardUtils = sListenerMap.get(listener);
            if (keyboardUtils != null) {
                keyboardUtils.removeListener();
            }
            sListenerMap.remove(listener);
        }
    }

    /**
     * Remove all registered keyboard listeners
     */
    public static void removeAllKeyboardToggleListeners() {
        for (SoftKeyboardToggleListener listener : sListenerMap.keySet()) {
            KeyboardUtils keyboardUtils = sListenerMap.get(listener);
            if (keyboardUtils != null) {
                keyboardUtils.removeListener();
            }
        }
        sListenerMap.clear();
    }

    /**
     * 输入键盘是否在显示
     *
     * @param component
     * @return boolean
     */
    public static boolean isSoftInputShow(Component component) {
        Component rootView = getRootView(component);
        if (rootView != null && rootView instanceof ComponentContainer) {
            return isSoftInputShow((ComponentContainer) rootView);
        }
        return false;
    }

    /**
     * 输入键盘是否在显示
     *
     * @param rootView 根布局
     * @return boolean
     */
    public static boolean isSoftInputShow(ComponentContainer rootView) {
        if (rootView == null) {
            return false;
        }
        int viewHeight = rootView.getHeight();

        // 获取View可见区域的bottom
        Rect rect = new Rect();
        rootView.getWindowVisibleRect(rect);
        int space = viewHeight - rect.bottom - getNavigationBarHeight(rootView.getContext());
        return space > 0;
    }

    /**
     * 获取系统底部导航栏的高度
     *
     * @param context 上下文
     * @return 系统状态栏的高度
     */
    private static int getNavigationBarHeight(Context context) {
        Display defaultDisplay = DisplayManager.getInstance().getDefaultDisplay(context).get();
        DisplayAttributes realDisplayMetrics = defaultDisplay.getRealAttributes();

        int realHeight = realDisplayMetrics.height;
        int realWidth = realDisplayMetrics.width;

        DisplayAttributes displayAttributes = defaultDisplay.getAttributes();

        int displayHeight = displayAttributes.height;
        int displayWidth = displayAttributes.width;

        if (realHeight - displayHeight > 0) {
            return realHeight - displayHeight;
        }
        return Math.max(realWidth - displayWidth, 0);
    }

    /**
     * 禁用物理返回键
     * <p>
     * 使用方法：
     * <p>需重写 onKeyDown</p>
     *
     * @param keyCode
     * @return 是否拦截事件
     * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
     * return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
     * }
     * </p>
     */
    public static boolean onDisableBackKeyDown(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEY_BACK:
            case KeyEvent.KEY_HOME:
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写 dispatchTouchEvent</p>
     *
     * @param ev 点击事件
     * @param component 窗口
     */
    public static void dispatchTouchEvent(TouchEvent ev, Component component) {
        if (ev == null || component == null) {
            return;
        }
        if (ev.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
            if (isTouchShowHideKeyboard(component, ev)) {
                hideSoftInputClearFocus(component);
            }
        }
    }

    /**
     * 根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     *
     * @param view 窗口
     * @param event 用户点击事件
     * @return 是否隐藏键盘
     */
    public static boolean isTouchShowHideKeyboard(Component view, TouchEvent event) {
        if ((view instanceof TextField) && event != null) {
            return !isTouchView(view, event);
        }
        return false;
    }

    /**
     * 根据用户点击的坐标获取用户在窗口上触摸到的View，判断这个View是否是EditText来判断是否隐藏键盘
     *
     * @param component 窗口
     * @param event 用户点击事件
     * @return 是否隐藏键盘
     */
    public static boolean isShouldHideKeyboard(Component component, TouchEvent event) {
        if (component == null || event == null) {
            return false;
        }
        if (!isSoftInputShow(component)) {
            return false;
        }
        if (!(component instanceof TextField)) {
            return false;
        }
        if (component instanceof ComponentContainer) {
            return findTouchEditText((ComponentContainer) component, event) == null;
        }
        return false;
    }

    private static Component findTouchEditText(ComponentContainer viewGroup, TouchEvent event) {
        if (viewGroup == null) {
            return viewGroup;
        }
        Component child = null;
        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            child = viewGroup.getComponentAt(index);
            if (child == null || !child.isComponentDisplayed()) {
                continue;
            }
            if (!isTouchView(child, event)) {
                continue;
            }
            if (child instanceof TextField) {
                return child;
            } else if (child instanceof ComponentContainer) {
                return findTouchEditText((ComponentContainer) child, event);
            }
        }
        return child;
    }

    /**
     * 判断view是否在触摸区域内
     *
     * @param view view
     * @param event 点击事件
     * @return view是否在触摸区域内
     */
    private static boolean isTouchView(Component view, TouchEvent event) {
        if (view == null || event == null) {
            return false;
        }
        final int size = 2;
        int[] location = new int[size];
        view.getLocationOnScreen();
        int left = location[0];
        int top = location[1];
        int right = left + view.getEstimatedWidth();
        int bottom = top + view.getEstimatedHeight();
        return event.getPointerPosition(event.getIndex()).getY() >= top
            && event.getPointerPosition(event.getIndex()).getY() <= bottom
            && event.getPointerPosition(event.getIndex()).getX() >= left
            && event.getPointerPosition(event.getIndex()).getX() <= right;
    }

    /**
     * 动态显示软键盘并且获取当前view的焦点
     *
     * @param view 视图
     */
    public static void showSoftInputGetFocus(final Component view) {
        if (view == null) {
            return;
        }

        if (view instanceof TextField) {
            ((TextField) view).requestFocus();
        }
        showSoftInput();
    }

    /**
     * 动态隐藏软键盘并且清除当前view的焦点
     *
     * @param view 视图
     */
    public static void hideSoftInputClearFocus(final Component view) {
        if (view == null) {
            return;
        }

        if (view instanceof TextField) {
            ((TextField) view).clearFocus();
        }
        hideSoftInput();
    }

    /**
     * 手动弹窗键盘
     *
     * @return boolean
     */
    public static boolean showSoftInput() {
        try {
            Class inputClass = Class.forName("ohos.miscservices.inputmethod.InputMethodController");
            Method method = inputClass.getMethod("getInstance");
            Object object = method.invoke(new Object[]{});
            Method startInput = inputClass.getMethod("startInput", int.class, boolean.class);
            return (boolean) startInput.invoke(object, 1, true);
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    /**
     * 手动关闭键盘
     *
     * @return boolean
     */
    public static boolean hideSoftInput() {
        try {
            Class inputClass = Class.forName("ohos.miscservices.inputmethod.InputMethodController");
            Method method = inputClass.getMethod("getInstance");
            Object object = method.invoke(new Object[]{});
            Method stopInput = inputClass.getMethod("stopInput", int.class);
            return (boolean) stopInput.invoke(object, 1);
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    private static Component getRootView(Component component) {
        if (component.getComponentParent() != null) {
            return getRootView((Component) component.getComponentParent());
        } else {
            return component;
        }
    }

    /**
     * 获取屏幕高度，不包含状态栏的高度
     *
     * @param context 上下文
     * @return 屏幕高度，不包含状态栏的高度
     */
    public static float getDisplayHeightInPx(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getSize(point);
        return point.getPointY();
    }

    /**
     * 获取屏幕高度，不包含状态栏的高度
     *
     * @param context 上下文
     * @return 屏幕高度，不包含状态栏的高度
     */
    public static float getRealSize(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        Point point = new Point();
        display.getRealSize(point);
        return point.getPointY();
    }
}
