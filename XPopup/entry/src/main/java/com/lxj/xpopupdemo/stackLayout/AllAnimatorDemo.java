package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.ArrayList;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class AllAnimatorDemo extends BaseStackLayout {
    ListContainer listContainer;

    public AllAnimatorDemo(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return ResourceTable.Layout_stacklayout_all_animator_demo;
    }

    PopupAnimation[] data;
    ArrayList<String> dataStr;

    @Override
    public void init(Component component) {
        listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);

        data = PopupAnimation.values();
        dataStr = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            dataStr.add(data[i].name());
        }

        final EasyProvider<String> commonAdapter = new EasyProvider<String>(getContext(), dataStr, ResourceTable.Layout_simple_list_item_1) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_text1, itemData);
            }
        };
        listContainer.setItemProvider(commonAdapter);
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                new XPopup.Builder(getContext())
                        .popupAnimation(data[position])
                        .asConfirm("演示应用不同的动画", "你可以为弹窗选择任意一种动画，但这并不必要，因为我已经默认给每种弹窗设定了最佳动画！对于你自定义的弹窗，可以随心选择心仪的动画方案。", null)
                        .show();
            }
        });

    }

}
