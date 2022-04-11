/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzh.nonview.router.launcher;


import com.lzh.nonview.router.route.ActionSupport;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.IntentParams;
import ohos.app.Context;

import java.util.Set;

/**
 * 默认启动器 {@link com.lzh.nonview.router.route.ActionRoute}
 */
public class DefaultActionLauncher extends ActionLauncher{

    @Override
    public void open(Ability context) {
        final ActionSupport support = newInstance(rule.getRuleClz());
        final IntentParams data = new IntentParams();
        Set<String> keySet = bundle.keySet();
        for(String key : keySet) {
            Object value = bundle.getParam(key);
            data.setParam(key,value);
        }


        Set<String> keySet2 = extras.getExtras().keySet();
        for(String key2 : keySet2) {
            Object value2 = extras.getExtras().getParam(key2);
            data.setParam(key2,value2);
        }
        getExecutor().execute(new ActionRunnable(support, context, data));
    }

    private static class ActionRunnable implements Runnable {

        ActionSupport support;
        Context context;
        IntentParams data;

        ActionRunnable(ActionSupport support, Context context, IntentParams data) {
            this.support = support;
            this.context = context;
            this.data = data;
        }

        @Override
        public void run() {
            support.onRouteTrigger(context, data);
        }
    }

    private ActionSupport newInstance(String name) {
        try {
            return (ActionSupport) Class.forName(name).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("create instance of %s failed", name), e);
        }
    }
}
