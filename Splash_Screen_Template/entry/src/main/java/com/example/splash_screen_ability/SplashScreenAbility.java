package com.example.splash_screen_ability;

import com.example.splash_screen_ability.slice.SplashScreenAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class SplashScreenAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SplashScreenAbilitySlice.class.getName());
    }
}