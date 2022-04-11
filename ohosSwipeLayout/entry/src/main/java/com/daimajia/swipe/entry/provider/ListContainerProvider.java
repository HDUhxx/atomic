package com.daimajia.swipe.entry.provider;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.entry.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class ListContainerProvider extends BaseSwipeAdapter {
    private Context context;

    public ListContainerProvider(Context context) {
        super();
        this.context = context;
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return ResourceTable.Id_swipe;
    }

    @Override
    public Component generateView(int position, ComponentContainer parent) {
        Component v = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_listcontainer_item, null, false);
        SwipeLayout swipeLayout = (SwipeLayout) v.findComponentById(getSwipeLayoutResourceId(position));
        Component right = v.findComponentById(ResourceTable.Id_Bottom4);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, right);
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                new ToastDialog(context).setText("DoubleClick").show();
            }
        });
        v.findComponentById(ResourceTable.Id_delete).setClickedListener(component -> {

        });

        return v;
    }

    @Override
    public void fillValues(int position, Component convertView) {
        Text text = (Text) convertView.findComponentById(ResourceTable.Id_position);
        text.setText((position + 1) + ".");
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
