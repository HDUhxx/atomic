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


import ohos.aafwk.content.IntentParams;
import ohos.app.Context;

/**
 * 您可以从此类继承以创建动作路由事件
 *
 * @since 2021-04-06
 */
public abstract class ActionSupport {

    /**
     * 接收路由束数据的回调方法。
     *
     * @param context context启动路由事件的上下文
     * @param bundle bundle和URL参数的附加数据.
     */
    public abstract void onRouteTrigger(Context context, IntentParams bundle);
}
