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

package com.allenliu.app.slice;

import com.allenliu.app.ResourceTable;
import com.allenliu.badgeview.BadgeFactory;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.agp.utils.Color;

/**
 * 程序入口展示页面
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Image image1 = (Image) findComponentById(ResourceTable.Id_iv1);
        Image image2 = (Image) findComponentById(ResourceTable.Id_iv2);
        Image image3 = (Image) findComponentById(ResourceTable.Id_iv3);
        Image image4 = (Image) findComponentById(ResourceTable.Id_iv4);
        Image image5 = (Image) findComponentById(ResourceTable.Id_iv5);
        Image image6 = (Image) findComponentById(ResourceTable.Id_iv6);
        BadgeFactory.createDot(this).setBadgeCount(20).bind(image1);
        BadgeFactory.createCircle(this).setBadgeCount(20).bind(image2);
        BadgeFactory.createRectangle(this).setBadgeCount(20).bind(image3);
        BadgeFactory.createOval(this).setBadgeCount(20).bind(image4);
        BadgeFactory.createSquare(this).setBadgeCount(20).bind(image5);
        BadgeFactory.create(this).setTextColor(Color.RED.getValue()).setTextSize(10).setWidthAndHeight(20,20).setBadgeCount(20).setBadgeBackground(Color.GREEN.getValue()).bind(image6);
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
