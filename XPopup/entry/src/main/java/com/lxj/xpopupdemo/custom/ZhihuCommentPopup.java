package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.DemoAbility;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.ArrayList;

/**
 * Description: 仿知乎底部评论弹窗
 * Create by dance, at 2018/12/25
 */
public class ZhihuCommentPopup extends BottomPopupView {
    ListContainer listContainer;
    private ArrayList<String> data;
    private EasyProvider<String> commonAdapter;

    public ZhihuCommentPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);
        findComponentById(ResourceTable.Id_tv_temp).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 弹出新的弹窗用来输入
                final CustomEditTextBottomPopup textBottomPopup = new CustomEditTextBottomPopup(getContext());
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
                        .setComponent(ZhihuCommentPopup.this) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onShow(BasePopupView popupView) {
                            }

                            @Override
                            public void onDismiss(BasePopupView popupView) {
                                String comment = textBottomPopup.getComment();
                                if (!comment.isEmpty()) {
                                    data.add(0, comment);
                                    commonAdapter.notifyDataChanged();
                                }
                            }
                        })
                        .asCustom(textBottomPopup)
                        .show();
            }
        });

        data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add("这是一个自定义Bottom类型的弹窗！你可以在里面添加任何滚动的View，我已经智能处理好嵌套滚动，你只需编写UI和逻辑即可！");
        }
        commonAdapter = new EasyProvider<String>(getContext(), data, ResourceTable.Layout_adapter_zhihu_comment) {
            @Override
            protected void bind(ViewHolder holder, String itemData, final int position) {
                holder.setText(ResourceTable.Id_name, "知乎大神 - " + position)
                        .setText(ResourceTable.Id_comment, itemData);
                holder.getView(ResourceTable.Id_btnDel).setClickedListener(new ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        data.remove(position);
                        commonAdapter.notifyDataSetItemRemoved(position);
                        commonAdapter.notifyDataSetItemRangeChanged(position, data.size());
                    }
                });
            }
        };
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                // 可以等消失动画执行完毕再开启新界面
                dismissWith(new Runnable() {
                    @Override
                    public void run() {
                        Intent secondIntent = new Intent();
                        Operation operation = new Intent.OperationBuilder()
                                .withBundleName(getContext().getBundleName())
                                .withAbilityName(DemoAbility.class.getName())
                                .build();
                        secondIntent.setOperation(operation);
                        getContext().startAbility(secondIntent, 0);
                    }
                });
            }
        });
        listContainer.setItemProvider(commonAdapter);
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getAppHeight(getContext()) * .7f);
    }
}