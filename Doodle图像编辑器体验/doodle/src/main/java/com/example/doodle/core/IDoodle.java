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

import ohos.media.image.PixelMap;

import java.util.List;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public interface IDoodle {
    /**
     * Gets the unit size in the current doodle coordinate system, which refers to the dp, independent of the image
     * 获取当前涂鸦坐标系中的单位大小，该单位参考dp，独立于图片
     *
     * @return float
     */
    float getUnitSize();

    /**
     * 设置图片旋转值
     *
     * @param degree
     */
    void setDoodleRotation(int degree);

    /**
     * 获取图片旋转值
     *
     * @return int
     */
    int getDoodleRotation();

    /**
     * 设置图片缩放倍数
     *
     * @param scale
     * @param pivotX
     * @param pivotY
     */
    void setDoodleScale(float scale, float pivotX, float pivotY);

    /**
     * 获取图片缩放倍数
     *
     * @return float
     */
    float getDoodleScale();

    /**
     * 设置画笔
     *
     * @param pen
     */
    void setPen(IDoodlePen pen);

    /**
     * 获取画笔
     *
     * @return IDoodlePen
     */
    IDoodlePen getPen();

    /**
     * 设置画笔形状
     *
     * @param shape
     */
    void setShape(IDoodleShape shape);

    /**
     * 获取画笔形状
     *
     * @return IDoodleShape
     */
    IDoodleShape getShape();

    /**
     * 设置图片偏移量x
     *
     * @param transX
     * @param transY
     */
    void setDoodleTranslation(float transX, float transY);

    /**
     * 设置图片偏移量x
     *
     * @param transX
     */
    void setDoodleTranslationX(float transX);

    /**
     * 获取图片偏移量x
     *
     * @return float
     */
    float getDoodleTranslationX();

    /**
     * 设置图片偏移量y
     *
     * @param transY
     */
    void setDoodleTranslationY(float transY);

    /**
     * 获取图片偏移量y
     *
     * @return float
     */
    float getDoodleTranslationY();

    /**
     * 设置大小
     *
     * @param paintSize
     */
    void setSize(float paintSize);

    /**
     * 获取大小
     *
     * @return float
     */
    float getSize();

    /**
     * 设置颜色
     *
     * @param color
     */
    void setColor(IDoodleColor color);

    /**
     * 获取颜色
     *
     * @return IDoodleColor
     */
    IDoodleColor getColor();

    /**
     * 最小缩放倍数限制
     *
     * @param minScale
     */
    void setDoodleMinScale(float minScale);

    /**
     * 最小缩放倍数限制
     *
     * @return float
     */
    float getDoodleMinScale();

    /**
     * 最大缩放倍数限制
     *
     * @param maxScale
     */
    void setDoodleMaxScale(float maxScale);

    /**
     * 最大缩放倍数限制
     *
     * @return float
     */
    float getDoodleMaxScale();

    /**
     * 添加item
     *
     * @param doodleItem
     */
    void addItem(IDoodleItem doodleItem);

    /**
     * 移除item
     *
     * @param doodleItem
     */
    void removeItem(IDoodleItem doodleItem);

    /**
     * total item count
     * exclude redo items
     *
     * @return count
     */
    int getItemCount();

    /**
     * 获取所有的涂鸦(不包括重做)
     * exclude redo items
     *
     * @return list
     */
    List<IDoodleItem> getAllItem();

    /**
     * total redo item count
     *
     * @return int
     */
    int getRedoItemCount();

    /**
     * 获取所有重做的涂鸦
     *
     * @return list
     */
    List<IDoodleItem> getAllRedoItem();

    /**
     * 设置放大镜倍数
     *
     * @param scale
     */
    void setZoomerScale(float scale);

    /**
     * 获取放大镜倍数
     *
     * @return float
     */
    float getZoomerScale();

    /**
     * 是否允许涂鸦显示在图片边界之外
     *
     * @param isDrawableOutside
     */
    void setIsDrawableOutside(boolean isDrawableOutside);

    /**
     * 是否允许涂鸦显示在图片边界之外
     *
     * @return boolean
     */
    boolean isDrawableOutside();

    /**
     * 是否显示原图
     *
     * @param isJustDrawOriginal
     */
    void setShowOriginal(boolean isJustDrawOriginal);

    /**
     * 是否显示原图
     *
     * @return boolean
     */
    boolean isShowOriginal();

    /**
     * 保存当前涂鸦图片
     */
    void save();

    /**
     * 清楚所有涂鸦
     */
    void clear();

    /**
     * 置顶item
     *
     * @param item
     */
    void topItem(IDoodleItem item);

    /**
     * 置底item
     *
     * @param item
     */
    void bottomItem(IDoodleItem item);

    /**
     * 撤销一步
     *
     * @return boolean
     */
    boolean undo();

    /**
     * 指定撤销的步数
     *
     * @param step
     * @return boolean
     */
    boolean undo(int step);

    /**
     * 指定重做的步数
     *
     * @param step
     * @return boolean
     */
    boolean redo(int step);

    /**
     * 获取当前显示的图片(无涂鸦)
     *
     * @return PixelMap
     */
    PixelMap getPixelMap();

    /**
     * 获取当前显示的图片(包含涂鸦)
     *
     * @return PixelMap
     */
    PixelMap getDoodlePixelMap();

    /**
     * 刷新
     */
    void refresh();
}
