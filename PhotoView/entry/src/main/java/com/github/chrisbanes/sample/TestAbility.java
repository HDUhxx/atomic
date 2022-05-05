package com.github.chrisbanes.sample;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;

public class TestAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_test);

        findComponentById(ResourceTable.Id_test_to_main).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent i = new Intent();
                Intent.OperationBuilder operationBuilder = new Intent.OperationBuilder();
                operationBuilder.withBundleName(getBundleName())
                        .withAbilityName(MainAbility.class)
                        .withDeviceId("");
                i.setOperation(operationBuilder.build());
                startAbility(i);
            }
        });

        findComponentById(ResourceTable.Id_test_to_page).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent i = new Intent();
                Intent.OperationBuilder operationBuilder = new Intent.OperationBuilder();
                operationBuilder.withBundleName(getBundleName())
                        .withAbilityName(PageAbility.class)
                        .withDeviceId("");
                i.setOperation(operationBuilder.build());
                startAbility(i);
            }
        });
    }
}
