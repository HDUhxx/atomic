package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.ArrayList;

/**
 * Description: 自定义带列表的Drawer弹窗
 * Create by dance, at 2019/1/9
 */
public class ListDrawerPopupView extends DrawerPopupView {
    ListContainer listContainer;

    public ListDrawerPopupView(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_list_drawer;
    }

    final ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate() {
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);

        for (int i = 0; i < 50; i++) {
            data.add("" + i);
        }

        final EasyProvider<String> commonAdapter = new EasyProvider<String>(getContext(), data, ResourceTable.Layout_simple_list_item_1) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_text1, itemData);
            }
        };

        listContainer.setItemProvider(commonAdapter);
        findComponentById(ResourceTable.Id_btn).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                dismiss();
            }
        });
    }

}
