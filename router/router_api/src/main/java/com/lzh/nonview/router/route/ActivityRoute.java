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
import com.lzh.nonview.router.activityresult.ActivityResultCallback;
import com.lzh.nonview.router.launcher.ActivityLauncher;
import com.lzh.nonview.router.launcher.Launcher;
import com.lzh.nonview.router.module.ActivityRouteRule;
import com.lzh.nonview.router.tools.Constants;
import com.lzh.nonview.router.tools.Utils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.app.Context;

/**
 * 用于通过uri检查路线规则和启动活动的路线工具
 *
 * @since 2021-04-06
 */
public class ActivityRoute extends BaseRoute<IActivityRoute> implements IActivityRoute {

    @Override
    public Intent createIntent(Ability context) {
        ActivityLauncher activityLauncher = (ActivityLauncher) launcher;
        activityLauncher.set(uri, bundle, callback.getExtras(), routeRule, remote);
        return activityLauncher.createIntent(context);
    }


    @Override
    public IActivityRoute requestCode(int requestCode) {
        this.callback.getExtras().setRequestCode(requestCode);
        return this;
    }

    @Override
    public IActivityRoute resultCallback(ActivityResultCallback callback) { //TODO 在这里放入callback
        this.callback.getExtras().putValue(Constants.KEY_RESULT_CALLBACK, callback);
        return this;
    }

    @Override
    public IActivityRoute setOptions(IntentParams options) {
        this.callback.getExtras().putValue(Constants.KEY_ACTIVITY_OPTIONS, options);
        return this;
    }

    @Override
    public IActivityRoute setAnim(int enterAnim, int exitAnim) {
        this.callback.getExtras().setInAnimation(enterAnim);
        this.callback.getExtras().setOutAnimation(exitAnim);
        return this;
    }

    @Override
    public IActivityRoute addFlags(int flag) {
        this.callback.getExtras().addFlags(flag);
        return this;
    }

    @Override
    public void open(Fraction fragment) {
        try {
            Utils.checkInterceptor(uri, callback.getExtras(), fragment.getFractionAbility(), getInterceptors());
            ActivityLauncher activityLauncher = (ActivityLauncher) launcher;
            activityLauncher.set(uri, bundle, callback.getExtras(), routeRule, remote);
            activityLauncher.open(fragment);
            callback.onOpenSuccess(routeRule);
        } catch (Throwable e) {
            callback.onOpenFailed(e);
        }

        callback.invoke(fragment.getFractionAbility());
    }

    @Override
    protected Launcher obtainLauncher() throws Exception {
        ActivityRouteRule rule = (ActivityRouteRule) routeRule;
        Class<? extends ActivityLauncher> launcher = rule.getLauncher();
        if (launcher == null) {
            launcher = RouterConfiguration.get().getActivityLauncher();
        }
        return launcher.newInstance();
    }


}
