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
package com.lzh.nonview.router.launcher;


import com.lzh.nonview.router.activityresult.ActivityResultCallback;
import com.lzh.nonview.router.activityresult.ActivityResultDispatcher;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.startsetting.AbilityStartSetting;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;

public class DefaultActivityLauncher extends ActivityLauncher {

    @Override
    public Intent createIntent(Ability context) {
//        Intent intent = new Intent();
//        intent.setClassName(context, rule.getRuleClz());
//        intent.setParam(bundle);
//        intent.putExtras(extras.getExtras());
//        intent.addFlags(extras.getFlags());
        Intent intent = new Intent();
        Operation operationCommonComponts = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(context.getBundleName())
                .withAbilityName(rule.getRuleClz())
                .build();
        intent.setOperation(operationCommonComponts);
        intent.setParam("Flag", extras.getFlags());
        //数据
        intent.setParams(bundle);
        //额外数据
        if(extras.getExtras().size()!=0){
            intent.setParam("IntentParams",extras.getExtras());
        }
        return intent;
    }


    @Override
    public void open(Fraction fragment) {
        if (resumeContext != null) {
            open(resumeContext);
        } else if (resultCallback != null) {
            open(fragment.getFractionAbility());
        } else {
            Intent intent = createIntent(fragment.getFractionAbility());
            fragment.startAbility(intent, extras.getRequestCode());
            overridePendingTransition(fragment.getFractionAbility(), extras);
        }
    }

    /**
     * 真实打开
     * @param context context
     */
    @Override
    public void open(Ability context) {
        Ability resume = resumeContext;
        if (resume != null) {
            context = resume;
        }
        ActivityResultCallback callback = resultCallback;
        int requestCode = extras.getRequestCode();
        System.out.println("111"+requestCode);
        if(requestCode ==-1){
            requestCode=0;
        }
        Intent intent = createIntent(context);
        if (context instanceof Ability) {
            Ability activity = (Ability) context;
            if (options != null) {
                intent.setParam("IntentParams", options);         //这里是不是空的
                activity.startAbilityForResult(intent,requestCode);
            } else {

                activity.startAbilityForResult(intent, requestCode);
            }
            overridePendingTransition((Ability) context, extras);
            ActivityResultDispatcher.get().bindRequestArgs(activity, requestCode, callback);
        } else {
            intent.setFlags(Intent.FLAG_ABILITY_NEW_MISSION);
            context.startAbility(intent);
        }
    }

    /**
     * 启动动画
     *
     * @param ability
     * @param extras
     */
    protected void overridePendingTransition(Ability ability, RouteBundleExtras extras) {
        if (ability == null || extras == null) {
            return;
        }

        int inAnimation = extras.getInAnimation();
        int outAnimation = extras.getOutAnimation();
        if (inAnimation >= 0 && outAnimation >= 0) {
            ability.setTransitionAnimation(inAnimation, outAnimation);
        }
    }

}
