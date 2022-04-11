package com.daimajia.swipe.entry;

import com.daimajia.swipe.entry.provider.GridViewProvider;
import com.daimajia.swipe.entry.provider.ListContainerProvider;
import com.daimajia.swipe.util.Attributes;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.utils.Color;

public class GridAbility extends Ability {
    private ListContainer listContainer;
    private GridViewProvider gridViewProvider;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_listcontainer);
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listcontainer);
        listContainer.setBoundaryColor(new Color(0xffdddddd));
        listContainer.setBoundaryThickness(2);
        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(2);
        listContainer.setLayoutManager(tableLayoutManager);
        gridViewProvider = new GridViewProvider(this);
        gridViewProvider.setMode(Attributes.Mode.Multiple);
        listContainer.setItemProvider(gridViewProvider);
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {

            }
        });
    }
}
