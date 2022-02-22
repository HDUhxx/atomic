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
 *
 */

package com.example.doodledemo.guide;

import com.example.doodledemo.ResourceTable;
import com.example.doodle.util.LogUtil;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentContainer;
import ohos.global.resource.NotExistException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.io.InputStream;

/**
 * fuqiangping
 *
 * @since 2021-04-22
 */
public class DoodleGuideAbility extends Ability {
    private static final String TAG = DoodleGuideAbility.class.getSimpleName();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_guide);

        // 初级涂鸦
        ComponentContainer simpleContainer = (ComponentContainer) findComponentById(
            ResourceTable.Id_container_simple_doodle);
        SimpleDoodleView simpleDoodleView = new SimpleDoodleView(this);
        simpleContainer.addComponent(simpleDoodleView, new ComponentContainer.LayoutConfig(
            ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));

        // 中级涂鸦
        ComponentContainer middleContainer = (ComponentContainer) findComponentById(
            ResourceTable.Id_container_middle_doodle);
        MiddleDoodleView middleDoodleView = new MiddleDoodleView(this);
        middleContainer.addComponent(middleDoodleView, new ComponentContainer.LayoutConfig(
            ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));

        // 高级级涂鸦
        ComponentContainer advancedContainer = (ComponentContainer) findComponentById(
            ResourceTable.Id_container_advanced_doodle);
        PixelMap pixelMap = getPixelMapFromResource(ResourceTable.Media_thelittleprince2);
        AdvancedDoodleView advancedDoodleView = new AdvancedDoodleView(this, pixelMap);
        advancedContainer.addComponent(advancedDoodleView, new ComponentContainer.LayoutConfig(
            ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
    }

    /**
     * 通过图片ID返回PixelMap
     *
     * @param resourceId 图片的资源ID
     * @return 图片的PixelMap
     */
    private PixelMap getPixelMapFromResource(int resourceId) {
        InputStream inputStream = null;
        PixelMap pixelMap = null;
        try {
            // 创建图像数据源ImageSource对象
            inputStream = getResourceManager().getResource(resourceId);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(inputStream, srcOpts);

            // 设置图片参数
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            pixelMap = imageSource.createPixelmap(decodingOptions);
        } catch (IOException e) {
            LogUtil.i(TAG, "IOException");
        } catch (NotExistException e) {
            LogUtil.i(TAG, "NotExistException");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LogUtil.i(TAG, "inputStream IOException");
                }
            }
        }
        return pixelMap;
    }
}
