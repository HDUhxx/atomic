package com.example.immersivedata.widget.widget1;

import com.example.immersivedata.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

/**
 * Widget1Slice used to implement phone call
 */
public class Widget1Slice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_immersive_data_widget1, null, false);
        super.setUIContent((ComponentContainer) component);
    }

    @Override
    public void onActive() {super.onActive();}

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
