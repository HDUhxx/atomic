package com.daimajia.swipe.entry.provider;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.entry.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

public class GridViewProvider extends BaseSwipeAdapter {
    private Context context;

    public GridViewProvider(Context context) {
        this.context = context;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return ResourceTable.Id_swipe;
    }

    @Override
    public Component generateView(int position, ComponentContainer parent) {
        Component v = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_grid_item, null, false);
        v.setWidth(context.getResourceManager().getDeviceCapability().screenDensity / 160 * context.getResourceManager().getDeviceCapability().width / 2);
        SwipeLayout swipeLayout = (SwipeLayout) v.findComponentById(getSwipeLayoutResourceId(position));
        Component right = v.findComponentById(ResourceTable.Id_Bottom5);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, right);
        return v;
    }

    @Override
    public void fillValues(int position, Component convertView) {

    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
