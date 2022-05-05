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

package de.hdodenhof.circleimageview.sample.slice;

import de.hdodenhof.circleimageview.CircleImageView;
import de.hdodenhof.circleimageview.sample.ResourceTable;
import de.hdodenhof.circleimageview.sample.Utils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.utils.Color;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.util.Optional;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int HILOG_TYPE = 3;

    private static final int HILOG_DOMAIN = 0xD000F00;

    private static final HiLogLabel LABEL = new HiLogLabel(HILOG_TYPE, HILOG_DOMAIN, "[MainAbilitySlice] ");

    private CircleImageView civ = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_layout_main);
        findComponentById(ResourceTable.Id_test_one).setClickedListener(component1 -> testSetImageDrawable());
        findComponentById(ResourceTable.Id_test_two).setClickedListener(component1 -> testSetPixelMap());
        Component component = findComponentById(ResourceTable.Id_one_cv);
        if (component instanceof CircleImageView) {
            civ = (CircleImageView) component;
        }
    }

    /**
     * Sets image using element
     */
    private void testSetImageDrawable() {
        Optional<PixelMapElement> element = getElementByResId(ResourceTable.Media_Lighthouse);
        if (element.isPresent()) {
            civ.setImageDrawable(element.get());
            civ.setBorderColor(Color.YELLOW);
        }
    }

    private void testSetPixelMap() {
        Optional<PixelMap> pixelMap = getPixelMapByResId(ResourceTable.Media_Desert);
        if (pixelMap.isPresent()) {
            civ.setPixelMap(pixelMap.get()); // set using pixel map
            civ.setBorderColor(Color.BLACK);
        } else {
            HiLog.error(LABEL, "set pixelmap failed, pixelmap is empty");
        }
    }

    private Optional<PixelMap> getPixelMapByResId(int resourceId) {
        ResourceManager resourceManager = getContext().getResourceManager();
        Optional<PixelMap> pixelMap = Optional.empty();
        if (resourceManager == null) {
            return Optional.empty();
        }
        try {
            Resource resource = resourceManager.getResource(resourceId);
            if (resource == null) {
                HiLog.error(LABEL, "get pixelmap failed, get resource by id is null");
                return Optional.empty();
            }
            pixelMap = Utils.preparePixelmap(resource);
        } catch (IOException | NotExistException e) {
            HiLog.error(LABEL, "set imageview pixelmap failed, pixelmap is empty");
        }
        return Optional.of(pixelMap.get());
    }

    private Optional<PixelMapElement> getElementByResId(int resourceId) {
        ResourceManager resourceManager = getContext().getResourceManager();
        if (resourceManager == null) {
            return Optional.empty();
        }
        Optional<PixelMapElement> element = Optional.empty();
        if (resourceId != 0) {
            try {
                Resource resource = resourceManager.getResource(resourceId);
                element = Utils.prepareElement(resource);
            } catch (IOException | NotExistException e) {
                HiLog.error(LABEL, "set imageview pixelmap failed, pixelmap is empty");
            }
        }
        return Optional.of(element.get());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}

