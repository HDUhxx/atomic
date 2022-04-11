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

package com.lzh.nonview.router.demo;

import com.lzh.nonview.router.RouterConfiguration;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * 基类
 *
 * @since 2021-03-20
 */
public class BaseAbility extends Ability {
    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        RouterConfiguration.get().dispatchActivityResult(this, requestCode, resultCode, resultData);
    }
}
