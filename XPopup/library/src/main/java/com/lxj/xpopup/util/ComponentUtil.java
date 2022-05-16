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
package com.lxj.xpopup.util;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentParent;
import ohos.agp.utils.Rect;

/**
 * 控件相关工具类
 */
public class ComponentUtil {

    /**
     * 获取控件在根容器中的位置
     *
     * @param component 控件
     * @return 位置信息
     */
    public static Rect getLocationInDecorView(Component component) {
        Component decorView = getDecorView(component);
        int[] componentLocationOnScreen = component.getLocationOnScreen();
        int[] decorViewLocationOnScreen = decorView.getLocationOnScreen();
        Rect rect = new Rect();
        rect.left = componentLocationOnScreen[0] - decorViewLocationOnScreen[0];
        rect.right = rect.left + component.getWidth();
        rect.top = componentLocationOnScreen[1] - decorViewLocationOnScreen[1];
        rect.bottom = rect.top + component.getHeight();
        return rect;
    }

    /**
     * 获取根容器
     *
     * @param component 根容器中的任意一个控件
     * @return 根容器
     */
    public static ComponentContainer getDecorView(Component component) {
        if (component != null) {
            ComponentParent componentParent = component.getComponentParent();
            if (componentParent == null) {
                return (ComponentContainer) component;
            } else {
                return getDecorView((Component) componentParent);
            }
        } else {
            return null;
        }
    }
}
