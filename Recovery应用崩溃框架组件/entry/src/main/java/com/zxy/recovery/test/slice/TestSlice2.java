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

package com.zxy.recovery.test.slice;

import com.zxy.recovery.test.MainAbility;
import com.zxy.recovery.test.ResourceTable;
import com.zxy.recovery.test.SuperButton;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

/**
 * 描述 第三个页面
 *
 * @author wjt
 * @since 2021-05-08
 */
public class TestSlice2 extends AbilitySlice implements Component.ClickedListener {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_test2);
        SuperButton btn = (SuperButton) findComponentById(ResourceTable.Id_test_btn);
        SuperButton btn2 = (SuperButton) findComponentById(ResourceTable.Id_test_btn2);
        btn2.setClickedListener(this);
        btn.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_test_btn) {
            Intent intent = null;
            startAbility(intent);
        } else if (component.getId() == ResourceTable.Id_test_btn2) {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName(getBundleName())
                    .withAbilityName(MainAbility.class)
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
    }
}
