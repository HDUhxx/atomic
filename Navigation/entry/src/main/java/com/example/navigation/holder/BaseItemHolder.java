package com.example.navigation.holder;

import com.example.navigation.model.Item;

/**
 * BaseItemHolder
 */
public abstract class BaseItemHolder implements ItemHolder {
    /**
     * processView
     *
     * @param item data binding
     */
    public abstract void processItem(Item item);

    /**
     * initViewListener
     */
    public abstract void initItemListener();
}
