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

import com.lzh.nonview.router.demo.interceptors.LoginInterceptor;
import com.lzh.nonview.router.demo.slice.UserAbilitySlice;
import com.lzh.nonview.router.anno.RouteInterceptors;
import com.lzh.nonview.router.anno.RouterRule;
/**
 * 动作
 *
 * @since 2021-03-20
 */

import ohos.aafwk.content.Intent;

/**
 * 用户信息展示的能力
 *
 * @author wjt
 * @since 2021-03-20
 */

@RouteInterceptors(LoginInterceptor.class)// 指定所有往此页面跳转的路由，均要进行登录检查
@RouterRule("user-info")
public class UserAbility extends BaseAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(UserAbilitySlice.class.getName());
    }
}
