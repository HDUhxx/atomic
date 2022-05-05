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

import com.lzh.nonview.router.demo.slice.IntentPrinterAbilitySlice;
import com.lzh.nonview.router.anno.RouterRule;

import ohos.aafwk.content.Intent;

/**
 * 打印显示屏幕的能力
 *
 * @since 2021-03-20
 */
@RouterRule("intent/printer")
public class IntentPrinterAbility extends BaseAbility {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(IntentPrinterAbilitySlice.class.getName());
    }
}