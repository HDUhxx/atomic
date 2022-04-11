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
package com.lzh.nonview.router.interceptors;


import com.lzh.nonview.router.Router;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import ohos.aafwk.ability.Ability;
import ohos.app.Context;
import ohos.utils.net.Uri;

/**
 * An interceptor interface
 * @author haoge
 */
public interface RouteInterceptor {

    /**
     * 是否在uri进行公开活动时被打断
     *
     * @param uri uri打开
     * @param extras 附加数据
     * @param context 上下文
     * @return 是否在uri进行公开活动时被打断
     */
    boolean intercept(Uri uri, RouteBundleExtras extras, Ability context);

    /**
     * 当您被拦截时，应调用此方法
     *
     * @param uri uri打开
     * @param extras 附加数据，
     * @param context 上下文
     */
    void onIntercepted(Uri uri, RouteBundleExtras extras, Ability context);
}
