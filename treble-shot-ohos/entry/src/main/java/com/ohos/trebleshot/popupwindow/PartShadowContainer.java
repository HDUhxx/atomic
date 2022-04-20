package com.ohos.trebleshot.popupwindow;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.StackLayout;
import ohos.agp.utils.Rect;
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

    private float down_x;
    private float down_y;
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
        if (!PopupUtils.isInRect(EventUtil.getRawX(event), EventUtil.getRawY(event), implViewRect)) {
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    down_x = EventUtil.getX(event);
                    down_y = EventUtil.getY(event);
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    if (isDismissOnTouchOutside && listener != null) {
                        listener.onClickOutside();
                    }
                    down_x = 0;
                    down_y = 0;
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

    public interface OnClickOutsideListener {
        void onClickOutside();
    }
}
