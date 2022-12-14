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
package com.lzh.nonview.router.module;



import com.lzh.nonview.router.launcher.ActivityLauncher;
import ohos.aafwk.ability.Ability;

/**
 * 主页面
 *
 * @since 2021-04-06
 */
public class ActivityRouteRule extends RouteRule<ActivityRouteRule, ActivityLauncher> {

    /**
     * ActivityRouteRule
     *
     * @param clz
     * @param <T>
     */
    public <T extends Ability> ActivityRouteRule(Class<T> clz) {
        super(clz.getCanonicalName());
    }

    /**
     * ActivityRouteRule
     *
     * @param clzName
     */
    public ActivityRouteRule(String clzName) {
        super(clzName);
    }
}
