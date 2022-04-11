package com.daimajia.swipe.adapters;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

import java.util.List;

public abstract class BaseSwipeAdapter extends BaseItemProvider implements SwipeItemMangerInterface, SwipeAdapterInterface {

    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    /**
     * return the {@link com.daimajia.swipe.SwipeLayout} resource id, int the view item.
     *
     * @param position
     * @return int
     */
    public abstract int getSwipeLayoutResourceId(int position);

    /**
     * generate a new view item.
     * Never bind SwipeListener or fill values here, every item has a chance to fill value or bind
     * listeners in fillValues.
     * to fill it in {@code fillValues} method.
     *
     * @param position
     * @param parent
     * @return component
     */
    public abstract Component generateView(int position, ComponentContainer parent);

    /**
     * fill values or bind listeners to the view.
     *
     * @param position
     * @param convertView
     */
    public abstract void fillValues(int position, Component convertView);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataChanged();
    }


    @Override
    public final Component getComponent(int position, Component convertView, ComponentContainer parent) {
        Component v = convertView;
        if (v == null) {
            v = generateView(position, parent);
        }
        mItemManger.bind(v, position);
        fillValues(position, v);
        return v;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
