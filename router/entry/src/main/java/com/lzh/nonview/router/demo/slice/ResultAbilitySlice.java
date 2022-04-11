/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lzh.nonview.router.demo.slice;

import com.lzh.nonview.router.demo.ResourceTable;
import com.lzh.nonview.router.extras.RouteBundleExtras;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.utils.net.Uri;

/**
 * ResultAbilitySlice
 *
 * @since 2021-03-20
 */
public class ResultAbilitySlice extends AbilitySlice {
    /**
     * 返回码
     */
    public static final int REQUEST_CODE = 300;
    private Uri uri = null;
    private RouteBundleExtras extras = null;
    private IntentParams intentParams = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        uri = intent.getSequenceableParam("uri");
        extras = intent.getSequenceableParam("extras");
        intentParams = intent.getParam("extrasIntentPararms");
        super.setUIContent(ResourceTable.Layout_ability_result);
        /**
        * 销毁页面不返回数据
        */
        Button button = (Button) findComponentById(ResourceTable.Id_backWithoutIntent);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                getAbility().terminateAbility();
            }
        });
        /**
        * 销毁页面并返回数据
        */
        Button button2 = (Button) findComponentById(ResourceTable.Id_backWithIntent);
        button2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                intent.setParam("value", "返回数据");
                getAbility().setResult(REQUEST_CODE, intent);
                getAbility().terminateAbility();
            }
        });
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
