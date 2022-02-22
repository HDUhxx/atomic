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

import ohos.agp.utils.Rect;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public interface IDoodleSelectableItem extends IDoodleItem {
    /**
     * 设置是否选中
     *
     * @param isSelected
     */
    void setSelected(boolean isSelected);

    /**
     * 是否选中
     *
     * @return boolean
     */
    boolean isSelected();

    /**
     * item的矩形(缩放scale之后)范围
     *
     * @return Rect
     */
    Rect getBounds();

    /**
     * 判断点（x,y）是否在item内，用于判断是否点中item
     *
     * @param x
     * @param y
     * @return boolean
     */
    boolean contains(float x, float y);
}
