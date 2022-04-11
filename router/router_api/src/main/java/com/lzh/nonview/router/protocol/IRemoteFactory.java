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
package com.lzh.nonview.router.protocol;


import com.lzh.nonview.router.RouterConfiguration;
import com.lzh.nonview.router.module.RouteRule;
import ohos.aafwk.content.IntentParams;
import ohos.app.Context;

/**
 * <p>
 *     该工厂用于创建和提供远程捆绑数据。
 *当需要通过{@link RouterConfiguration＃startHostService（String，Context）}将路由规则注册到远程网桥服务时，*将被调用工厂以创建捆绑包并将其从aidl接口传递给远程服务
 */
public interface IRemoteFactory {
    /**
     * 创建一个额外的包数据，以便其他进程或插件可以兼容。
     * @param application The application context.
     * @param rule The routing rule
     * @return new extra bundle or null.
     */
    IntentParams createRemote(Context application, RouteRule rule);
}
