package com.ohos.trebleshot.popupwindow;

import ohos.agp.components.ComponentContainer;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class MyPopup extends CommonDialog {
    private PopupWindowView contentView;
    private final Context context;

    public MyPopup(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (getWindow() == null || contentView == null || contentView.popupEntity == null) {
            return;
        }
        if (contentView.popupEntity.enableShowWhenAppBackground) {
            // 支持悬浮窗（即应用后台弹出Xpopup）
            WindowManager.LayoutConfig layoutConfig = getWindow().getLayoutConfig().get();
            layoutConfig.type = WindowManager.LayoutConfig.MOD_APPLICATION_OVERLAY;
            getWindow().setLayoutConfig(layoutConfig);
        }
        setTransparent(true);
        setSize(getWindowWdith(context), getWindowHeight(context));
        contentView.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));

        setContentCustomComponent(contentView);
    }


    public MyPopup setContent(PopupWindowView view) {
        this.contentView = view;
        return this;
    }

    /**
     * 把事件传递给Ability，暂无实现
     *
     * @param event 触摸事件
     */
    public void passClick(TouchEvent event) {
//        if (contentView != null && contentView.getContext() instanceof Ability) {
//            // 把事件传递给Ability
////            LogUtil.debug("XPopup", "waiting for improvement");
//        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度，单位px
     */
    public static int getWindowWdith(Context context) {
        return context.getResourceManager().getDeviceCapability().width * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度，单位px
     */
    public static int getWindowHeight(Context context) {
        return context.getResourceManager().getDeviceCapability().height * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }


}