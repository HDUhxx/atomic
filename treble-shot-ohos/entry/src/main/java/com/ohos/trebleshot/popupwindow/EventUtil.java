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
package com.ohos.trebleshot.popupwindow;

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
final class EventUtil {

    private EventUtil() {
    }

    /**
     * 获取相对于偏移位置的x坐标
     *
     * @param touchEvent 触摸事件
     * @return x坐标
     */
    public static float getX(TouchEvent touchEvent) {
        return touchEvent.getPointerPosition(0).getX();
    }

    /**
     * 获取相对于偏移位置的y坐标
     *
     * @param touchEvent 触摸事件
     * @return y坐标
     */
    public static float getY(TouchEvent touchEvent) {
        return touchEvent.getPointerPosition(0).getY();
    }

    /**
     * 获取相对于屏幕坐标原点的x坐标
     *
     * @param touchEvent 触摸事件
     * @return x坐标
     */
    public static float getRawX(TouchEvent touchEvent) {
        return touchEvent.getPointerScreenPosition(0).getX();
    }

    /**
     * 获取相对于屏幕坐标原点的y坐标
     *
     * @param touchEvent 触摸事件
     * @return y坐标
     */
    public static float getRawY(TouchEvent touchEvent) {
        return touchEvent.getPointerScreenPosition(0).getY();
    }

}
