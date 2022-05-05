package com.github.chrisbanes.sample;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.PageSlider;

import java.util.ArrayList;

public class PageAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_page);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(ResourceTable.Media_wallpaper);
        list.add(ResourceTable.Media_wallpaper);
        list.add(ResourceTable.Media_wallpaper);

        PageSlider slider = (PageSlider) findComponentById(ResourceTable.Id_page_m);
        slider.setProvider(new PageProvider(list,getContext(),slider));
    }
}
