package com.example.bassiness_card.itemprovider;

import com.example.bassiness_card.ResourceTable;
import com.example.bassiness_card.datamodel.ItemInfo;
import com.example.bassiness_card.typefactory.ListTypeFactory;
import com.example.bassiness_card.typefactory.TypeFactory;
import com.example.bassiness_card.viewholder.ViewHolder;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.AbilityContext;

import java.util.List;

/**
 * Item Provider
 */
public class ListItemProvider extends BaseItemProvider {
    private final List
            <ItemInfo> itemList;
    private final AbilityContext context;
    private final TypeFactory typeFactory;

    /**
     * Provider Constructor
     *
     * @param itemList List contains ListItem data
     * @param context  context
     */
    public ListItemProvider(List
                                    <ItemInfo> itemList, AbilityContext context) {
        this.itemList = itemList;
        this.context = context;
        this.typeFactory = new ListTypeFactory();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public ItemInfo getItem(int index) {
        return itemList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public int getItemComponentType(int index) {
        return itemList.get(index).getType(typeFactory);
    }

    @Override
    public Component getComponent(int index, Component component, ComponentContainer componentContainer) {
        Component itemComponent = component;
        ViewHolder viewHolder;
        if (itemComponent == null) {
            itemComponent = LayoutScatter.getInstance(componentContainer.getContext())
                    .parse(getItemComponentType(index), componentContainer, false);
        }
        viewHolder = typeFactory.getViewHolder(getItemComponentType(index), itemComponent);
        viewHolder.setUpComponent(getItem(index), context);
        if (index == getCount() - 1) {
            Image mDivider = (Image) itemComponent.findComponentById(ResourceTable.Id_divider);
            if (mDivider != null) {
                mDivider.setVisibility(Component.INVISIBLE);
            }
        }
        return itemComponent;
    }
}
