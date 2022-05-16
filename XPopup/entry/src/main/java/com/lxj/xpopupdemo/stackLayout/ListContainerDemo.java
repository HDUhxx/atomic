package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.ArrayList;

/**
 * Description:
 * Create by dance, at 2021/10/18
 */
public class ListContainerDemo extends BaseStackLayout {
    ListContainer listContainer;

    public ListContainerDemo(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return ResourceTable.Layout_stacklayout_list_container_demo;
    }

    ArrayList<String> dataStr;

    @Override
    public void init(Component component) {
        listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);

        dataStr = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataStr.add("条目" + i);
        }

        final EasyProvider<String> commonAdapter = new EasyProvider<String>(getContext(), dataStr, ResourceTable.Layout_simple_list_item_2) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_text1, itemData);
                holder.getView(ResourceTable.Id_text1).setClickedListener(new ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        new XPopup.Builder(getContext())
                                .hasShadowBg(false)
                                .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                                .atView(component)  // 依附于所点击的Commonent，内部会自动判断在上方或者下方显示
                                .isComponentMode(true, component) // Component实现模式
                                .asAttachList(new String[]{"分享", "编辑"},
                                        new int[]{ResourceTable.Media_icon, ResourceTable.Media_icon},
                                        new OnSelectListener() {
                                            @Override
                                            public void onSelect(int position, String text) {
                                                toast("click " + text);
                                            }
                                        }, 0, 0).show();
                    }
                });
            }
        };
        listContainer.setItemProvider(commonAdapter);
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                new XPopup.Builder(getContext())
                        .hasShadowBg(false)
                        .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                        .atView(component)  // 依附于所点击的Commonent，内部会自动判断在上方或者下方显示
                        .isCenterHorizontal(true) // 是否与依附的Commonent水平居中对齐
                        .asAttachList(new String[]{"分享", "编辑", "不带icon不带icon", "分享分享分享"},
                                new int[]{ResourceTable.Media_icon, ResourceTable.Media_icon},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                }, 0, 0).show();
            }
        });

    }

}
