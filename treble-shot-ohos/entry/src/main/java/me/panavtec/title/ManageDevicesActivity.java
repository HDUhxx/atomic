package me.panavtec.title;

import me.panavtec.title.slice.ManageDevicesActivitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ManageDevicesActivity extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ManageDevicesActivitySlice.class.getName());
    }

}
