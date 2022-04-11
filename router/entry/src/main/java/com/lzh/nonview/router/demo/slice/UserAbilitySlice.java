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
import ohos.agp.components.Text;

/**
* 显示bean信息
*
* @since 2021-03-20
*/
public class UserAbilitySlice extends AbilitySlice {
    private String username = null;
    private RouteBundleExtras extras = null;
    private IntentParams intentParams;
    private IntentParams intentParams2;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        intentParams = intent.getParams();
        intentParams2 = intent.getParam("IntentParams");
        username = (String) intentParams.getParam("username");
        super.setUIContent(ResourceTable.Layout_ability_user);
        Text text = (Text) findComponentById(ResourceTable.Id_user);
        text.setText("用户名为：" + username);
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
