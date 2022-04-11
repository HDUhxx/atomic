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

package com.lzh.nonview.router.route;

import com.lzh.nonview.router.RouterConfiguration;
import com.lzh.nonview.router.launcher.ActionLauncher;
import com.lzh.nonview.router.launcher.Launcher;
import com.lzh.nonview.router.module.ActionRouteRule;
import com.lzh.nonview.router.tools.Constants;

import java.util.concurrent.Executor;

/**
 * ActionRoute
 *
 * @since 2021-04-06
 */
public class ActionRoute extends BaseRoute<IActionRoute> implements IActionRoute {

    @Override
    protected Launcher obtainLauncher() throws Exception {
        ActionRouteRule rule = (ActionRouteRule) routeRule;
        Class<? extends ActionLauncher> launcher = rule.getLauncher();
        if (launcher == null) {
            launcher = RouterConfiguration.get().getActionLauncher();
        }
        return launcher.newInstance();
    }

    @Override
    public void setExecutor(Executor executor) {
        callback.getExtras().putValue(Constants.KEY_ACTION_EXECUTOR, executor);
    }
}
