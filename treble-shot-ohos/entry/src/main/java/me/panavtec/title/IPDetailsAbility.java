package me.panavtec.title;

import me.panavtec.title.slice.IPDetailsAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class IPDetailsAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(IPDetailsAbilitySlice.class.getName());
    }

}
