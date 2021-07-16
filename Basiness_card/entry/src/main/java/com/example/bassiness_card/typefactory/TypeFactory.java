package com.example.bassiness_card.typefactory;

import com.example.bassiness_card.datamodel.DefaultDoubleLineListItemInfo;
import com.example.bassiness_card.datamodel.SingleButtonDoubleLineListItemInfo;
import com.example.bassiness_card.viewholder.ViewHolder;

import ohos.agp.components.Component;

/**
 * Type Factory
 */
public interface TypeFactory {
    /**
     * Get resource ID of DefaultDoubleLineList
     *
     * @param itemInfo Item model
     * @return Corresponding resource ID
     */
    int type(DefaultDoubleLineListItemInfo itemInfo);

    int type(SingleButtonDoubleLineListItemInfo itemInfo);

    /**
     * Get view holder corresponding to itemComponent
     *
     * @param type          resource ID
     * @param itemComponent itemComponent
     * @return View holder
     */
    ViewHolder getViewHolder(int type, Component itemComponent);
}
