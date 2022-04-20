package me.panavtec.title.hmadapter;

import me.panavtec.title.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StringListPro extends BaseItemProvider {

    final String path;
    public final List<File> fileList = new ArrayList<>();
    final Context context;
    public StringListPro(Context context,String path) {
        this.context = context;
        this.path = path;
        explanString(path);
    }
    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int i) {
        return fileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_string_list, null, false);
        } else {
            cpt = component;
        }
        Text butPathClick = (Text) cpt.findComponentById(ResourceTable.Id_but_path_click);
//        Image image_ic = (Image) cpt.findComponentById(ResourceTable.Id_image_ic);
        butPathClick.setText(fileList.get(fileList.size()-1-i).getName());
        return cpt;
    }

    public void explanString(String str){
        File file = new File(str);
        if (!file.getParent().isEmpty() && !file.getParent().equals("/")) {
            System.out.println(file.getParent()+"------------");
            fileList.add(new File(file.getParent()));
            explanString(file.getParent());
        }
    }
    public String getIndexPath(int i){
        return fileList.get(i).getAbsolutePath();
    }
}
