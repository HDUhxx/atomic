package com.lxj.xpopup.impl;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopup.util.ElementUtil;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;

import java.util.Arrays;

/**
 * Description: Attach类型的列表弹窗
 * Create by dance, at 2018/12/12
 */
public class AttachListPopupView extends AttachPopupView {

    ListContainer listContainer;
    protected int bindLayoutId;
    protected int bindItemLayoutId;
    protected int contentGravity = LayoutAlignment.CENTER;

    /**
     * @param context          上下文
     * @param bindLayoutId     layoutId 要求layoutId中必须有一个id为listContainer的ListContainer
     * @param bindItemLayoutId itemLayoutId 条目的布局id，要求布局中必须有id为iv_image的Image，和id为tv_text的Text
     */
    public AttachListPopupView(Context context, int bindLayoutId, int bindItemLayoutId, PopupInfo popupInfo) {
        super(context, popupInfo);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? ResourceTable.Layout__xpopup_attach_impl_list : bindLayoutId;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);
        final EasyProvider<String> adapter = new EasyProvider<String>(getContext(), Arrays.asList(data), bindItemLayoutId == 0 ? ResourceTable.Layout__xpopup_adapter_text : bindItemLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_tv_text, itemData);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(ResourceTable.Id_iv_image).setVisibility(VISIBLE);
                    holder.<Image>getView(ResourceTable.Id_iv_image).setPixelMap(iconIds[position]);
                } else {
                    holder.getView(ResourceTable.Id_iv_image).setVisibility(HIDE);
                }

                if (bindItemLayoutId == 0) {
                    if (popupInfo.isDarkTheme) {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
                        holder.getView(ResourceTable.Id_item_dirver).setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_dark_divider)));
                    } else {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_text_dark_color)));
                        holder.getView(ResourceTable.Id_item_dirver).setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_divider)));
                    }
                    DirectionalLayout linearLayout = holder.getView(ResourceTable.Id_ll_temp);
                    linearLayout.setAlignment(contentGravity);
                    if (position == data.length - 1) {
                        holder.getView(ResourceTable.Id_item_dirver).setVisibility(HIDE);
                    } else {
                        holder.getView(ResourceTable.Id_item_dirver).setVisibility(VISIBLE);
                    }
                }
            }
        };
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getData().get(position));
                }
                if (popupInfo.autoDismiss) {
                    dismiss();
                }
            }
        });
        listContainer.setItemProvider(adapter);
        applyTheme();
    }

    protected void applyTheme() {
        if (bindLayoutId == 0) {
            if (popupInfo.isDarkTheme) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }
        }
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
    }

    String[] data;
    int[] iconIds;

    public AttachListPopupView setStringData(String[] data, int[] iconIds) {
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    public AttachListPopupView setContentGravity(int gravity) {
        this.contentGravity = gravity;
        return this;
    }

    private OnSelectListener selectListener;

    public AttachListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

}
