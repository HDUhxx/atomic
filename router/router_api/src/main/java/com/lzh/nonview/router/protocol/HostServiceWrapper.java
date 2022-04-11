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


import com.lzh.nonview.router.IService;
import com.lzh.nonview.router.RouterConfiguration;
import com.lzh.nonview.router.ServiceStub;
import com.lzh.nonview.router.module.RemoteRule;
import com.lzh.nonview.router.module.RouteRule;
import com.lzh.nonview.router.route.ActionRoute;
import com.lzh.nonview.router.route.ActivityRoute;
import com.lzh.nonview.router.route.IRoute;
import com.lzh.nonview.router.route.InternalCallback;
import com.lzh.nonview.router.tools.Cache;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;
import ohos.utils.net.Uri;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 主服务包装类
 */
public class HostServiceWrapper {

    private static Context context;
    private static IService service;
    private static String pluginName;
    private static IService proxy;
    private static IAbilityConnection connection = new IAbilityConnection() {

        @Override
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
            proxy = ServiceStub.asInterface(iRemoteObject);
        }

        @Override
        public void onAbilityDisconnectDone(ElementName elementName, int i) {
            proxy = null;
        }
    };

    /**
     * 设置主机包名称。它将用于绑定主机应用程序中的远程服务。
     *
     * @param hostPackage the package name of host.
     * @param context the context to start remote service.
     * @param pluginName The plugin name to register to remote service. to help to judge if it should be
     */
    public static void startHostService(String hostPackage, Ability context, String pluginName) {
        if (service != null) {
            throw new RuntimeException("You've bind a remote service before");
        }
        if (TextTool.isNullOrEmpty(hostPackage)) {
            throw new IllegalArgumentException("Please provide a valid host package name.");
        }
        HostServiceWrapper.context = context.getApplicationContext();
        HostServiceWrapper.pluginName = TextTool.isNullOrEmpty(pluginName) ? context.getBundleName() : pluginName;

        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                        .withBundleName("com.lzh.nonview.router.host")
                        .withAbilityName("com.lzh.nonview.router.host.RouterHostService")
                .withAction(Intent.ACTION_QUERY_TRAVELLING_GUIDELINE)
                .build();
        Intent intent = new Intent();
        intent.setOperation(operation);
        context.connectAbility(intent, connection);
    }

    /**
     * 创建一个路由
     * @param url url
     * @param callback InternalCallback
     * @return 路由
     */
    public static IRoute create(String url, InternalCallback callback) {
        try {
            return createWithThrow(url, callback);
        } catch (Exception e) {
            return new IRoute.EmptyRoute(callback);
        }
    }

    private static IRoute createWithThrow(String url, InternalCallback callback) throws Exception{
        RemoteRule rule;
        if ((rule = service.getActivityRule(url)) != null) {
            return new ActivityRoute().create(Uri.parse(url == null?"":url), rule.getRule(), rule.getExtra(), callback);
        } else if ((rule = service.getActionRule(url)) != null) {
            return new ActionRoute().create(Uri.parse(url == null?"":url), rule.getRule(), rule.getExtra(), callback);
        } else {
            return new IRoute.EmptyRoute(callback);
        }
    }

    public static boolean isRegister(String pluginName) {
        try {
            return service.isRegister(pluginName);
        } catch (Exception e) {
            return false;
        }
    }

    public static void registerRulesToHostService() {
        try {
            if (service == null) {
                return;
            }
            service.register(pluginName);
            service.addActionRules(transform(Cache.getActionRules()));
            service.addActivityRules(transform(Cache.getActivityRules()));
        } catch (Exception e) {
            // ignore
        }
    }

    private static Map<String, RemoteRule> transform(Map<String, ? extends RouteRule> source){
        Map<String, RemoteRule> dest = new HashMap<>();
        Iterator<? extends Map.Entry<String, ? extends RouteRule>> ii= source.entrySet().iterator();
        while(ii.hasNext()){
            Map.Entry<String, RouteRule> next = (Map.Entry<String, RouteRule>) ii.next();
            String key = next.getKey();
            RouteRule value = next.getValue();
            RemoteRule remote = RemoteRule.create(value, getRemote(context, value));
            dest.put(key, remote);
        }
//        Map<String, RemoteRule> dest = new HashMap<>();
//        for (String route : source.keySet()) {
//            RouteRule rule = source.get(route);
//            RemoteRule remote = RemoteRule.create(rule, getRemote(context, rule));
//            dest.put(route, remote);
//        }
        return dest;
    }

    private static IntentParams getRemote(Context context, RouteRule rule){
        IRemoteFactory factory = RouterConfiguration.get().getRemoteFactory();
        return factory == null ? null : factory.createRemote(context, rule);
    }

}
