package com.jltf.jltf_navigation;

import com.jltf.jltf_navigation.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.utils.net.Uri;

public class MainAbility extends Ability {

    String lat="深圳市";
    String destination;
    String mode;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
