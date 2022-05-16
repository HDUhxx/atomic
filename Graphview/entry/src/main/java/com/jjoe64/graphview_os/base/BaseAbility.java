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
package com.jjoe64.graphview_os.base;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
public abstract class BaseAbility extends Ability {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(getEntry());
        initView();
        initData();
    }


    /**
     * 无传参
     *
     * @param  cl 无传参
     */
    public void startAbility(Class cl) {
        Intent i = new Intent();
        Operation op = new Intent.OperationBuilder()
                .withBundleName(this.getBundleName())
                .withAbilityName(cl.getName())
                .build();
        i.setOperation(op);
        startAbility(i);

    }


    /**
     * 有传参
     *
     * @param cl str
     * @param parameters str
     */
    public void startAbility(Class cl, IntentParams parameters) {
        Intent i = new Intent();
        Operation op = new Intent.OperationBuilder()
                .withBundleName(this.getBundleName())
                .withAbilityName(cl.getName())
                .build();
        i.setOperation(op);
        i.setParams(parameters);
        startAbility(i);
    }

    /**
     * 获取RouteName
     *
     * @return str
     */
    protected abstract String getEntry();


    /**
     * 初始化Veiw
     */
    protected abstract void initView();

    /**
     * data处理
     */

    protected abstract void initData();
}
