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

import com.lzh.nonview.router.demo.interceptors.DefaultInterceptor;
import com.lzh.nonview.router.Router;
import com.lzh.nonview.router.RouterConfiguration;
import com.lzh.nonview.router.anno.RouteConfig;
import com.lzh.nonview.router.interceptors.RouteInterceptor;

import ohos.aafwk.ability.AbilityPackage;

/**
 * APPaction
 *
 * @since 2021-03-20
 */
@RouteConfig(baseUrl = "haoge://page/", pack = "com.haoge.studio")
public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();
        /**
         * 注册通过apt生成的路由表
         */
        RouterConfiguration.get().addRouteCreator(new com.haoge.studio.RouterRuleCreator());
        /**
         * 设置默认路由拦截器：所有路由跳转均会被触发(除了需要直接打开浏览器的链接)
         */
        RouteInterceptor interceptor = new DefaultInterceptor();
        RouterConfiguration.get().setInterceptor(interceptor);
        Router.setDebug(true);
    }
}
