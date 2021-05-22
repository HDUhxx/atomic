package com.example.myapplication_huibianli;

import com.example.myapplication_huibianli.slice.LoginAbility;
import com.example.myapplication_huibianli.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
