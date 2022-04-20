package me.panavtec.title.slice;

import me.panavtec.title.ResourceTable;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;

public class BaseSlice extends AbilitySlice {


    public void setBack() {
        findComponentById(ResourceTable.Id_ima_back).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                onBackPressed();
            }
        });
    }
}
