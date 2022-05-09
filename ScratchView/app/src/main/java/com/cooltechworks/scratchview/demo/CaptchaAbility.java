package com.cooltechworks.scratchview.demo;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class CaptchaAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_captcha);
    }

}
