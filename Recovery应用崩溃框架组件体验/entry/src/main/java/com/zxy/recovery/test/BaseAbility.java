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

package com.zxy.recovery.test;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * 描述 base
 *
 * @author wjt
 * @since 2021-05-08
 */
public class BaseAbility extends Ability {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }
}
