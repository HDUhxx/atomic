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
import ohos.agp.utils.Point;
import ohos.media.image.PixelMap;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public interface IDoodleItem {
    /**
     * setDoodle
     *
     * @param doodle
     */
    void setDoodle(IDoodle doodle);

    /**
     * IDoodle
     *
     * @return IDoodle
     */
    IDoodle getDoodle();

    /**
     * 获取画笔
     *
     * @return IDoodlePen
     */
    IDoodlePen getPen();

    /**
     * 设置画笔
     *
     * @param pen
     */
    void setPen(IDoodlePen pen);

    /**
     * 获取画笔形状
     *
     * @return IDoodleShape
     */
    IDoodleShape getShape();

    /**
     * 设置画笔形状
     *
     * @param shape
     */
    void setShape(IDoodleShape shape);

    /**
     * 获取大小
     *
     * @return float
     */
    float getSize();

    /**
     * 设置大小
     *
     * @param size
     */
    void setSize(float size);

    /**
     * 获取颜色
     *
     * @return IDoodleColor
     */
    IDoodleColor getColor();

    /**
     * 设置颜色
     *
     * @param color
     */
    void setColor(IDoodleColor color);

    /**
     * 绘制item
     *
     * @param canvas
     * @param pixelMap
     */
    void draw(Canvas canvas, PixelMap pixelMap);

    /**
     * 画在所有item的上面
     *
     * @param canvas
     */
    void drawAtTheTop(Canvas canvas);

    /**
     * 设置在当前涂鸦中的左上角位置
     *
     * @param x
     * @param y
     */
    void setLocation(float x, float y);

    /**
     * 获取当前涂鸦中的起始坐标
     *
     * @return Point
     */
    Point getLocation();

    /**
     * item中心点x
     *
     * @param pivotX
     */
    void setPivotX(float pivotX);

    /**
     * item中心点x
     *
     * @return float
     */
    float getPivotX();

    /**
     * item中心点y
     *
     * @param pivotY
     */
    void setPivotY(float pivotY);

    /**
     * item中心点y
     *
     * @return float
     */
    float getPivotY();

    /**
     * 设置item的旋转值，围绕中心点Pivot旋转
     *
     * @param degree
     */
    void setItemRotate(float degree);

    /**
     * 获取item的旋转值
     *
     * @return float
     */
    float getItemRotate();

    /**
     * 是否需要裁剪图片区域外的部分
     *
     * @return boolean
     */
    boolean isNeedClipOutside();

    /**
     * 设置是否需要裁剪图片区域外的部分
     *
     * @param isClip
     */
    void setNeedClipOutside(boolean isClip);

    /**
     * 添加进涂鸦时回调
     */
    void onAdd();

    /**
     * 移除涂鸦时回调
     */
    void onRemove();

    /**
     * 刷新
     */
    void refresh();

    /**
     * item是否可以编辑。用于编辑模式下对item的操作
     *
     * @return boolean
     */
    boolean isDoodleEditable();

    /**
     * 缩放倍数，围绕(PivotX,PivotY)旋转
     *
     * @param scale
     */
    void setScale(float scale);

    /**
     * getScale
     *
     * @return float
     */
    float getScale();

    /**
     * 监听器
     *
     * @param listener
     */
    void addItemListener(IDoodleItemListener listener);

    /**
     * removeItemListener
     *
     * @param listener
     */
    void removeItemListener(IDoodleItemListener listener);
}
