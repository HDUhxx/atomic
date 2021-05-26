package com.example.jltflianxi;

import com.example.jltflianxi.slice.*;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        //创建VideoAbilitySlice对应的Action供跳转使用
        super.addActionRoute("action.vedio_layout",  VideoAbilitySlice.class.getName());
        //创建VideoAbilitySlice1对应的Action供跳转使用
        super.addActionRoute("action.vedio_layout1", VideoAbilitySlice1.class.getName());

        super.addActionRoute("action.vedio_layout3", VideoAbilitySlice3.class.getName());

        super.addActionRoute("action.vedio_layout4", VideoAbilitySlice4.class.getName());

        super.addActionRoute("action.vedio_layout5", VideoAbilitySlice5.class.getName());
    }
}
