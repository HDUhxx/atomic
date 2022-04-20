package com.developer.filepicker.file;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

import java.util.ArrayList;

public class FileListAdapter extends BaseItemProvider {

    private final ArrayList<ListItem> listItems;
    private final Context context;

    public FileListAdapter(ArrayList<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public ListItem getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        final Component cpt;
        if (convertComponent == null) {
            cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_file_list_item, null, false);
        } else {
            cpt = convertComponent;
        }
        Text name = (Text) cpt.findComponentById(ResourceTable.Id_name);
        Text path = (Text) cpt.findComponentById(ResourceTable.Id_path);
        name.setText(listItems.get(position).getName());
        path.setText(listItems.get(position).getPath());
        return cpt;
    }
}