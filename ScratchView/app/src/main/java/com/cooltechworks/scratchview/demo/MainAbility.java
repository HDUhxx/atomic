package com.cooltechworks.scratchview.demo;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

public class MainAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        findComponentById(ResourceTable.Id_scratch_text).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent1 = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName("com.cooltechworks.scratchview.demo")
                        .withAbilityName(DemoClothingAbility.class)
                        .build();
                intent1.setOperation(operation);
                startAbility(intent1);
            }
        });
        findComponentById(ResourceTable.Id_scratch_image).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent2 = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName("com.cooltechworks.scratchview.demo")
                        .withAbilityName(CaptchaAbility.class)
                        .build();
                intent2.setOperation(operation);
                startAbility(intent2);
            }
        });
    }

}
