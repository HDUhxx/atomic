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
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Text;

import java.util.Set;

/**
 * 打印的能力展现类
 *
 * @since 2021-03-20
 **/
public class IntentPrinterAbilitySlice extends AbilitySlice {
    private IntentParams intentParams = null;
    private IntentParams intentParams2 = null;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        intentParams = intent.getParams();
        intentParams2 = intent.getParam("IntentParams");
        super.setUIContent(ResourceTable.Layout_ability_intent_printer);
        Text text = (Text) findComponentById(ResourceTable.Id_printer_tv);
        StringBuilder stringBuilder = new StringBuilder();
        if (intentParams == null) {
            stringBuilder.append("当前页面的Intent中不含有数据:" + System.lineSeparator());
        } else {
            /**
             * 自己添加的需要去掉
             */
            intentParams.remove("callerBundleName");
            if (intentParams2 != null) {
                /**
                 * 如果有额外数据，就会多一个官方的数据，需要去掉
                 */
                intentParams.remove("IntentParams");
                Set<String> keys2 = intentParams2.keySet();
                stringBuilder.append("当前页面的Intent额外数据 有" + keys2.size() + "条数据");
                for (String key2 : keys2) {
                    stringBuilder.append(" key=" + key2 + "value==" + intentParams2.getParam(key2).toString()
                             + System.lineSeparator());
                }
            } else {
                stringBuilder.append("没有额外数据" + System.lineSeparator());
            }

            Set<String> keys = intentParams.keySet();
            for (String key : keys) {
                stringBuilder.append("key=" + key + "\t value==" + intentParams.getParam(key).toString()
                        + System.lineSeparator());
            }
            stringBuilder.append("当前页面的Intent 有" + keys.size() + "条数据" + System.lineSeparator());
        }

        text.setText(stringBuilder.toString());
    }
}
