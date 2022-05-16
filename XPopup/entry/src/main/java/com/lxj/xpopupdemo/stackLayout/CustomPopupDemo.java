package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.ArrayList;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class CustomPopupDemo extends BaseStackLayout {
    ListContainer listContainer;

    public CustomPopupDemo(Context context) {
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
        ((Text) component.findComponentById(ResourceTable.Id_temp)).setText("演示如何自定义弹窗，并给自定义的弹窗应用不同的内置动画方案；你也可以为自己的弹窗编写自定义的动画。");

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
                CustomPopup customPopup = new CustomPopup(getContext());
                new XPopup.Builder(getContext())
                        .popupAnimation(data[position])
                        .autoOpenSoftInput(true)
                        .setComponent(listContainer) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                        .asCustom(customPopup)
                        .show();
            }
        });

    }

    /**
     * 自定义布局中间弹窗
     */
    public static class CustomPopup extends CenterPopupView {
        public CustomPopup(Context context) {
            super(context, null);
        }

        @Override
        protected int getImplLayoutId() {
            return ResourceTable.Layout_custom_popup;
        }

        @Override
        protected void onCreate() {
            super.onCreate();
            findComponentById(ResourceTable.Id_tv_close).setClickedListener(new ClickedListener() {
                @Override
                public void onClick(Component component) {
                    dismiss();
                }
            });
        }

    }

}
