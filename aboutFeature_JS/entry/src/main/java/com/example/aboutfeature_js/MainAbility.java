package com.example.aboutfeature_js;

import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceAbility;

/**
 * MainAbility
 */

//AceAbility类是JS FA在HarmonyOS上运行环境的基类，继承自Ability。开发者的应用运行入口类应该从该类派生
//    应用通过AceAbility类中setInstanceName()接口设置该Ability的实例资源
//    setInstanceName(String name)的参数“name”指实例名称，实例名称与config.json文件中module.js.name的值对应。若开发者未修改实例名，而使用了缺省值default，则无需调用此接口。
public class MainAbility extends AceAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
