package com.developer.filepicker.controller.adapters;

import com.developer.filepicker.ResourceTable;
import com.developer.filepicker.controller.NotifyItemChecked;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.model.FileListItem;
import com.developer.filepicker.model.MarkedItemList;
import com.developer.filepicker.widget.MaterialCheckbox;
import com.developer.filepicker.widget.OnCheckedChangeListener;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author akshay sunil masram
 */
public class FileListAdapter extends BaseItemProvider {

    private ArrayList<FileListItem> listItem;
    private Context context;
    private DialogProperties properties;
    private NotifyItemChecked notifyItemChecked;

    public FileListAdapter(ArrayList<FileListItem> listItem, Context context,
                           DialogProperties properties) {
        this.listItem = listItem;
        this.context = context;
        this.properties = properties;
    }

    public void setData(ArrayList<FileListItem> listData) {
        this.listItem = listData;
        notifyDataChanged();
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public FileListItem getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        final Component cpt;
        if (convertComponent == null) {
            cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_file_list_item, null, false);
        } else {
            cpt = convertComponent;
        }
        Image type_icon = (Image) cpt.findComponentById(ResourceTable.Id_image_type);
        Text name = (Text) cpt.findComponentById(ResourceTable.Id_fname);
        Text type = (Text) cpt.findComponentById(ResourceTable.Id_ftype);
        MaterialCheckbox checkbox = (MaterialCheckbox) cpt.findComponentById(ResourceTable.Id_file_mark);
        final FileListItem item = listItem.get(position);
        if (item.isDirectory()) {
            type_icon.setImageAndDecodeBounds(ResourceTable.Media_ic_type_folder);
            if (properties.selection_type == DialogConfigs.FILE_SELECT) {
                checkbox.setVisibility(Component.INVISIBLE);
            } else {
                checkbox.setVisibility(Component.VISIBLE);
            }
        } else {
            type_icon.setImageAndDecodeBounds(ResourceTable.Media_ic_type_file);
            if (properties.selection_type == DialogConfigs.DIR_SELECT) {
                checkbox.setVisibility(Component.INVISIBLE);
            } else {
                checkbox.setVisibility(Component.VISIBLE);
            }
        }
        type_icon.setComponentDescription(item.getFilename());
        name.setText(item.getFilename());
        DateFormat dateFormatter = DateFormat.getDateInstance();
        DateFormat timeFormatter = DateFormat.getTimeInstance();
        Date date = new Date(item.getTime());
        if (position == 0 && item.getFilename().startsWith("..")) {
            type.setText("Parent Directory");
        } else {
            type.setText(String.format("Last edited: %1$s, %2$s",
                    dateFormatter.format(date), timeFormatter.format(date)));
        }
        if (checkbox.getVisibility() == Component.VISIBLE) {
            if (position == 0 && item.getFilename().startsWith("..")) {
                checkbox.setVisibility(Component.INVISIBLE);
            }
            if (MarkedItemList.hasItem(item.getLocation())) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }
        }

        checkbox.setOnCheckedChangedListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialCheckbox checkbox, boolean isChecked) {
                item.setMarked(isChecked);
                if (item.isMarked()) {
                    if (properties.selection_mode == DialogConfigs.MULTI_MODE) {
                        MarkedItemList.addSelectedItem(item);
                    } else {
                        MarkedItemList.addSingleFile(item);
                    }
                } else {
                    MarkedItemList.removeSelectedItem(item.getLocation());
                }
                notifyItemChecked.notifyCheckBoxIsClicked();
            }
        });
        return cpt;
    }

    public void setNotifyItemCheckedListener(NotifyItemChecked notifyItemChecked) {
        this.notifyItemChecked = notifyItemChecked;
    }
}
