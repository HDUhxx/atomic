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

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public interface IDoodleItemListener {
    /**
     * PROPERTY_SCALE
     */
    int PROPERTY_SCALE = 1;

    /**
     * PROPERTY_ROTATE
     */
    int PROPERTY_ROTATE = 2;

    /**
     * PROPERTY_PIVOT_X
     */
    int PROPERTY_PIVOT_X = 3;

    /**
     * PROPERTY_PIVOT_Y
     */
    int PROPERTY_PIVOT_Y = 4;

    /**
     * PROPERTY_SIZE
     */
    int PROPERTY_SIZE = 5;

    /**
     * PROPERTY_COLOR
     */
    int PROPERTY_COLOR = 6;

    /**
     * PROPERTY_LOCATION
     */
    int PROPERTY_LOCATION = 7;

    /**
     * 属性改变时回调
     *
     * @param property 属性
     */
    void onPropertyChanged(int property);
}
