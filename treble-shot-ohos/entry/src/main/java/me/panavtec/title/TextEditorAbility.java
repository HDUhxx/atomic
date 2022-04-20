package me.panavtec.title;

import me.panavtec.title.slice.TextEditorAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class TextEditorAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(TextEditorAbilitySlice.class.getName());
    }

}
