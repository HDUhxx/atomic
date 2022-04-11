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

import com.lzh.nonview.router.module.ActionRouteRule;
import com.lzh.nonview.router.tools.Cache;
import com.lzh.nonview.router.tools.Constants;

import java.util.concurrent.Executor;

/**
 * 动作启动器的基类
 */
public abstract class ActionLauncher extends Launcher {

    /**
     * 将执行程序实例返回到切换线程.
     *
     * @return 切换线程.
     */
    protected Executor getExecutor() {
        Executor executor = extras.getValue(Constants.KEY_ACTION_EXECUTOR);
        if (executor == null) {
            executor = Cache.findOrCreateExecutor(((ActionRouteRule) rule).getExecutor());
        }
        return executor;
    }
}
