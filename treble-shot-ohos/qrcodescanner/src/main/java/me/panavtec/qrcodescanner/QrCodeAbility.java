package me.panavtec.qrcodescanner;

import me.panavtec.qrcodescanner.slice.QrCodeAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class QrCodeAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(QrCodeAbilitySlice.class.getName());
    }
}
