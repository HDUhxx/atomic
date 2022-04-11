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
package com.lzh.nonview.router;


import com.lzh.nonview.router.activityresult.ActivityResultDispatcher;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.launcher.ActionLauncher;
import com.lzh.nonview.router.launcher.ActivityLauncher;
import com.lzh.nonview.router.launcher.DefaultActionLauncher;
import com.lzh.nonview.router.launcher.DefaultActivityLauncher;
import com.lzh.nonview.router.module.RouteCreator;
import com.lzh.nonview.router.protocol.HostServiceWrapper;
import com.lzh.nonview.router.protocol.IRemoteFactory;
import com.lzh.nonview.router.route.ActionRoute;
import com.lzh.nonview.router.route.ActivityRoute;
import com.lzh.nonview.router.route.InternalCallback;
import com.lzh.nonview.router.route.RouteCallback;
import com.lzh.nonview.router.tools.Cache;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.util.concurrent.Executor;

/**
 *入口类，用于存储路由器配置.
 */
public final class RouterConfiguration {

    private RouteInterceptor interceptor;
    private RouteCallback callback;

    private IRemoteFactory remoteFactory = null;
    private Class<? extends ActivityLauncher> activityLauncher;
    private Class<? extends ActionLauncher> actionLauncher;


    public RouteInterceptor getInterceptor() {
        return interceptor;
    }

    /**
     * 将默认路由拦截器设置为used。所有路线都会调用它。
     * @param interceptor the default interceptor
     * @return config itself
     * @see RouteInterceptor
     */
    public RouterConfiguration setInterceptor(RouteInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public RouteCallback getCallback() {
        return callback;
    }

    /**
     * 将默认路由回调设置为used。所有路线都会调用它。
     * @param callback The default callback
     * @return config itself
     * @see RouteCallback
     */
    public RouterConfiguration setCallback(RouteCallback callback) {
        this.callback = callback;
        return this;
    }

    public IRemoteFactory getRemoteFactory() {
        return remoteFactory;
    }

    /**
     *将默认的远程工厂设置为已使用。工厂必须包含默认的空结构。
     * @param remoteFactory The remote factory class
     * @return config itself
     * @see IRemoteFactory
     */
    public RouterConfiguration setRemoteFactory(IRemoteFactory remoteFactory) {
        this.remoteFactory = remoteFactory;
        return this;
    }

    public Class<? extends ActivityLauncher> getActivityLauncher() {
        return activityLauncher == null ? DefaultActivityLauncher.class : activityLauncher;
    }

    /**
     * 将默认活动启动器设置为“已使用”
     * @param activityLauncher The launcher class for {@link ActivityRoute}
     * @return config itself
     * @see ActivityLauncher
     */
    public RouterConfiguration setActivityLauncher(Class<? extends ActivityLauncher> activityLauncher) {
        this.activityLauncher = activityLauncher;
        return this;
    }

    public Class<? extends ActionLauncher> getActionLauncher() {
        return actionLauncher == null ? DefaultActionLauncher.class : actionLauncher;
    }

    /**
     * 将默认操作启动器设置为“已使用”。
     * @param actionLauncher The launcher class for {@link ActionRoute}
     * @return config itself
     * @see ActionLauncher
     */
    public RouterConfiguration setActionLauncher(Class<? extends ActionLauncher> actionLauncher) {
        this.actionLauncher = actionLauncher;
        return this;
    }

    /**
     * 添加路由规则创建者，并将其注册为远程服务（如果已启动）。
     * @param creator Route rules creator.can't be null
     */
    public void addRouteCreator(RouteCreator creator) {
        Cache.addCreator(creator);
        HostServiceWrapper.registerRulesToHostService();
    }

    /**
     * 要注册执行者，请执行以下操作
     * @param key Executor的类
     * @param value Executor实例与该键关联
     */
    public void registerExecutors(Class<? extends Executor> key, Executor value) {
        Cache.registerExecutors(key, value);
    }

    /**
     * 启动远程服务
     *
     * @param hostPackage 主机的软件包名称。它启动主机的远程服务
     * @param context 有效的上下文
     */
    public void startHostService(String hostPackage, Ability context) {
        startHostService(hostPackage, context, null);
    }

    /**
     * 启动远程主机服务
     *
     * @param hostPackage 主机的软件包名称
     * @param context 上下文
     * @param pluginName 唯一的标识符插件名称。或null为其使用插件程序包名称
     */
    public void startHostService(String hostPackage, Ability context, String pluginName) {
        HostServiceWrapper.startHostService(hostPackage, context, pluginName);
    }

    /**
     * 检查指定的插件名称是否已注册到远程服务。
     *
     * @param pluginName 指定的插件名称*
     * @return 如果已注册，则为True
     */
    public boolean isRegister(String pluginName) {
        return HostServiceWrapper.isRegister(pluginName);
    }

    /**
     * 通过uri。仅应在以下方法的生命周期中调用此方法 RouteCallback 否则它将为null，因为它已被清除
     *
     * @param uri 您打开的uri
     * @return 您在通过uri打开路由之前可能要设置的实例.
     */
    public RouteBundleExtras restoreExtras(Uri uri) {
        return InternalCallback.findExtrasByUri(uri);
    }

    public Context restorContext(Uri uri) {
        return InternalCallback.findContextByUri(uri);
    }

    public boolean dispatchActivityResult(Ability activity, int requestCode, int resultCode, Intent data) {
        return ActivityResultDispatcher.get().dispatchActivityResult(activity, requestCode, resultCode, data);
    }

    private static RouterConfiguration config = new RouterConfiguration();
    private RouterConfiguration() {}
    public static RouterConfiguration get() {
        return config;
    }
}
