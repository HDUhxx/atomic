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
package com.lzh.nonview.router.host;

import com.lzh.nonview.router.ServiceStub;
import com.lzh.nonview.router.module.RemoteRule;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 远程服务以存储路线规则.
 * @author
 */
public class RouterHostService extends Ability {
    private final static String Slash="/";
    private static RemoteVerify verify = new DefaultVerify();
    public static void setVerify(RemoteVerify verify) {
        RouterHostService.verify = verify;
    }

    private MyRemote remote = new MyRemote("MyRemoteAbility");

 @Override
 protected void onStart(Intent intent) {
   super.onStart(intent);
   System.out.println("服务启动");
    }

    @Override
    protected void onCommand(Intent intent, boolean restart, int startId) {
        super.onCommand(intent, restart, startId);
        System.out.println("服务onCommand启动");
    }//IPC通信
    @Override
    public IRemoteObject onConnect(Intent intent) {
        System.out.println("onConnect");
        return remote.asObject();

    }

    @Override
    public void onDisconnect(Intent intent) {
        System.out.println("onDisconnect");
        super.onDisconnect(intent);
    }

    @Override
    public void onStop() {
        System.out.println("onStop");
        super.onStop();
    }


    class MyRemote extends ServiceStub {
        Map<String, RemoteRule> activities = new HashMap<>();
        Map<String, RemoteRule> actions = new HashMap<>();
        List<String> plugins = new ArrayList<>();

        public MyRemote(String descriptor) {
            super(descriptor);
        }

        @Override
        public void register(String pluginName) throws RemoteException {
            if (!plugins.contains(pluginName)) {
                plugins.add(pluginName);
            }
        }

        @Override
        public boolean isRegister(String pluginName) throws RemoteException {
            return plugins.contains(pluginName);
        }

        @Override
        public void addActivityRules(Map<String, RemoteRule> rules) throws RemoteException {
            activities.putAll(rules);
        }

        @Override
        public void addActionRules(Map<String, RemoteRule> rules) throws RemoteException {
            actions.putAll(rules);
        }

        @Override
        public RemoteRule getActionRule(String uri) throws RemoteException {

            return findRule(Uri.parse(uri), actions);
        }

        @Override
        public RemoteRule getActivityRule(String uri) throws RemoteException {
            return findRule(Uri.parse(uri), activities);
        }
    }
    private RemoteRule findRule(Uri uri, Map<String, RemoteRule> rules) {
        String route = uri.getScheme() + "://" + uri.getDecodedHost() + uri.getDecodedPath();
        for (String key:rules.keySet()) {
            if (format(key).equals(format(route))) {
                return rules.get(key);
            }
        }
        return null;
    }

    private String format(String url) {
        if (url.endsWith(Slash)){
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
