package com.ohos.trebleshot.popupwindow;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.render3d.math.Quaternion;

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

public class PopupEntity {

    public Boolean isDismissOnTouchOutside = true; // 点击外部消失
    public Boolean autoDismiss = true; // 操作完毕后是否自动关闭
    public Component atView = null; // 依附于那个View显示
    public Component watchView = null; // 依附于那个View显示

    // 动画执行器，如果不指定，则会根据窗体类型popupType字段生成默认合适的动画执行器
    public PopupAnimation popupAnimation = null;
    public PopupAnimator customAnimator = null;
    public Quaternion touchPoint = null; // 触摸的点
    public int maxWidth; // 最大宽度
    public int maxHeight; // 最大高度
    public int popupWidth; // 指定弹窗的宽，受max的宽高限制
    public int popupHeight; // 指定弹窗的高，受max的宽高限制
    public float borderRadius = 15; // 圆角
    public ComponentContainer decorView; // 每个弹窗所属的DecorView
    public int offsetX; // x方向的偏移量
    public int offsetY; // y方向的偏移量
    public boolean isCenterHorizontal = false; // 是否水平居中
    public boolean isRequestFocus = true; // 弹窗是否强制抢占焦点
    public boolean autoFocusEditText = true; // 是否让输入框自动获取焦点
    public boolean isClickThrough = false; // 是否点击透传，默认弹背景点击是拦截的
    public boolean isDarkTheme = false; // 是否是暗色调主题
    public boolean enableShowWhenAppBackground = false; // 是否允许应用在后台的时候也能弹出弹窗
    public boolean isDestroyOnDismiss = true; // 是否关闭后进行资源释放
    public boolean positionByWindowCenter = false; // 是否已屏幕中心进行定位，默认根据Material范式进行定位

    public Component getAtView() {
        return atView;
    }

}
