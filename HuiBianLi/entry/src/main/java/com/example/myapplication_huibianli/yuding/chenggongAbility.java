package com.example.myapplication_huibianli.yuding;

import com.example.myapplication_huibianli.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class chenggongAbility extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_yugouchengong);

    }
}
