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

import ohos.agp.render.Paint;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public interface IDoodleColor {
    /**
     * 配置画笔
     *
     * @param doodleItem
     * @param paint
     */
    void config(IDoodleItem doodleItem, Paint paint);

    /**
     * 深度拷贝
     *
     * @return IDoodleColor
     */
    IDoodleColor copy();
}
