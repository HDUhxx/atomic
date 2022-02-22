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

package com.example.doodle.core;

import ohos.agp.components.element.Element;

/**
 * 取色器
 *
 * @since 2021-04-21
 */
public interface ColorChangedListener {
    /**
     * 回调函数
     *
     * @param color 选中的颜色
     * @param size
     */
    void colorChanged(int color, int size);

    /**
     * 回调函数
     *
     * @param color
     * @param size
     */
    void colorChanged(Element color, int size);
}