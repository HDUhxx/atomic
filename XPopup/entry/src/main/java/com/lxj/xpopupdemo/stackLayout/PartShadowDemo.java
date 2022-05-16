package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopupdemo.ResourceTable;
import com.lxj.xpopupdemo.custom.CustomPartShadowPopupView;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.ArrayList;

/**
 * Description: 局部阴影的示例
 * Create by dance, at 2018/12/21
 */
public class PartShadowDemo extends BaseStackLayout implements Component.ClickedListener {
    Component ll_container;
    ListContainer listContainer;

    public PartShadowDemo(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return ResourceTable.Layout_stacklayout_part_shadow_demo;
    }

    @Override
    public void init(Component component) {
        ll_container = component.findComponentById(ResourceTable.Id_ll_container);
        listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);

        component.findComponentById(ResourceTable.Id_tv_all).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv_price).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv_sales).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv_select).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv_filter).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tvCenter).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tvCenter2).setClickedListener(this);

        final ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(i + "");
        }
        EasyProvider<String> adapter = new EasyProvider<String>(getContext(), data, ResourceTable.Layout_simple_list_item_1) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_text1, "长按我试试 - " + position);
                // 必须要在事件发生之前就watch
                final XPopup.Builder builder = new XPopup.Builder(getContext()).watchView(holder.itemView);
                holder.itemView.setLongClickedListener(new LongClickedListener() {
                    @Override
                    public void onLongClicked(Component component) {

                    }
                });
            }
        };
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                toast(data.get(position));
            }
        });
        listContainer.setItemProvider(adapter);
    }

    private void showPartShadow(final Component component) {
        new XPopup.Builder(getContext())
                .atView(component)
                .isClickThrough(true)
                .autoOpenSoftInput(true)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onShow(BasePopupView popupView) {
                        toast("显示了");
                    }

                    @Override
                    public void onDismiss(BasePopupView popupView) {
                    }
                })
                .asCustom(new CustomPartShadowPopupView(getContext()))
                .toggle();
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_tv_all:
            case ResourceTable.Id_tv_price:
            case ResourceTable.Id_tv_sales:
                showPartShadow(component);
                break;
            case ResourceTable.Id_tv_select:
                new XPopup.Builder(getContext())
                        .atView(component)
                        .asCustom(new CustomPartShadowPopupView(getContext()))
                        .show();
                break;
            default:
                break;
        }
    }
}
