package com.example.splash_screen_ability.slice;

import com.example.splash_screen_ability.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class SplashScreenAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}