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

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public interface IDoodlePen {
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
     * @return IDoodlePen
     */
    IDoodlePen copy();

    /**
     * 绘制画笔辅助工具，由IDoodle绘制，不属于IDoodleItem的内容
     * 比如可以用于仿制功能时 定位器的绘制
     *
     * @param canvas
     * @param doodle
     */
    void drawHelpers(Canvas canvas, IDoodle doodle);
}
