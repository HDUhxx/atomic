package com.daimajia.swipe.entry;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.entry.provider.ListContainerProvider;
import com.daimajia.swipe.util.Attributes;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.utils.Color;

public class ListAbility extends Ability {
    private ListContainer listContainer;
    private ListContainerProvider listContainerProvider;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_listcontainer);
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listcontainer);
        listContainer.setBoundaryColor(new Color(0xffdddddd));
        listContainer.setBoundaryThickness(2);
        listContainer.setBoundarySwitch(true);
        listContainerProvider = new ListContainerProvider(this);
        listContainerProvider.setMode(Attributes.Mode.Single);

        listContainer.setItemProvider(listContainerProvider);
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long l) {
                ((SwipeLayout) (listContainer.getComponentAt(position))).open(true);
            }
        });

        listContainer.setItemLongClickedListener(new ListContainer.ItemLongClickedListener() {
            @Override
            public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
                return false;
            }
        });
    }
}
