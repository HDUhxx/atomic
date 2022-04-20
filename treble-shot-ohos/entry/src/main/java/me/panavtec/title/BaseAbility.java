package me.panavtec.title;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;

public abstract class BaseAbility extends Ability {

    public void jumpAbility(Class<?> tclass) {
        Intent intent = new Intent();
        Operation build = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName(tclass)
                .withBundleName(getBundleName())
                .build();
        intent.setOperation(build);
        startAbilityForResult(intent, 1);
    }
}
