package me.panavtec.title;

import me.panavtec.title.slice.FileDetailsAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class FileDetailsAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(FileDetailsAbilitySlice.class.getName());
    }

}
