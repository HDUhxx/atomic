package com.jltf.jltfcardtemplate05.widget.widget;

import com.jltf.jltfcardtemplate05.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

/**
 * WidgetSlice used to implement phone call
 */
public class WidgetSlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_immersion_list_widget, null, false);
        super.setUIContent((ComponentContainer) component);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
