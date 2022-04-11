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
import com.lzh.nonview.router.demo.manager.DataManager;
import com.lzh.nonview.router.Router;
import com.lzh.nonview.router.extras.RouteBundleExtras;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.utils.net.Uri;

/**
 * 登录页面
 *
 * @since 2021-03-20
 */
public class LoginAbilitySlice extends AbilitySlice {
    private Uri uri = null;
    private RouteBundleExtras extras = null;
    private IntentParams intentParams = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        uri = intent.getSequenceableParam("uri");
        extras = intent.getSequenceableParam("extras");
        intentParams = intent.getParam("extrasIntentPararms");

        super.setUIContent(ResourceTable.Layout_ability_login);
        Button button = (Button) findComponentById(ResourceTable.Id_login);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 登录完成后恢复路由启动。
                DataManager.setIsLogin(true);
                extras.addExtras(intentParams);
                Router.resume(uri.toString(),extras)
                        .open(getAbility());
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
