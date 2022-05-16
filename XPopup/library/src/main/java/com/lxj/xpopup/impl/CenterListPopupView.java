package com.lxj.xpopup.impl;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.LogUtil;
import com.lxj.xpopup.util.TextUtils;
import com.lxj.xpopup.widget.CheckView;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

import java.util.Arrays;

/**
 * Description: 在中间的列表对话框
 * Create by dance, at 2018/12/16
 */
public class CenterListPopupView extends CenterPopupView {

    private static final String TAG = CenterListPopupView.class.getName();
    ListContainer listContainer;
    Text tv_title;

    /**
     * @param context          上下文
     * @param bindLayoutId     要求layoutId中必须有一个id为listContainer的ListContainer，如果你需要显示标题，则必须有一个id为tv_title的Text
     * @param bindItemLayoutId 条目的布局id，要求布局中必须有id为iv_image的ImageView，和id为tv_text的TextView
     * @param popupInfo        弹窗信息
     */
    public CenterListPopupView(Context context, int bindLayoutId, int bindItemLayoutId, PopupInfo popupInfo) {
        super(context, popupInfo);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? ResourceTable.Layout__xpopup_center_impl_list : bindLayoutId;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);
        tv_title = (Text) findComponentById(ResourceTable.Id_tv_title);

        if (tv_title != null) {
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(HIDE);
                if (findComponentById(ResourceTable.Id_xpopup_divider) != null) {
                    findComponentById(ResourceTable.Id_xpopup_divider).setVisibility(HIDE);
                }
            } else {
                tv_title.setText(title);
            }
        }

        final EasyProvider<String> adapter = new EasyProvider<String>(getContext(), Arrays.asList(data), bindItemLayoutId == 0 ? ResourceTable.Layout__xpopup_adapter_text_match : bindItemLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_tv_text, itemData);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(ResourceTable.Id_iv_image).setVisibility(VISIBLE);
                    holder.<Image>getView(ResourceTable.Id_iv_image).setPixelMap(iconIds[position]);
                } else {
                    holder.getView(ResourceTable.Id_iv_image).setVisibility(HIDE);
                }

                // 对勾View
                if (checkedPosition != -1) {
                    if (holder.getView(ResourceTable.Id_check_view) != null) {
                        holder.getView(ResourceTable.Id_check_view).setVisibility(position == checkedPosition ? VISIBLE : HIDE);
                        holder.<CheckView>getView(ResourceTable.Id_check_view).setColor(new Color(XPopup.getPrimaryColor()));
                    }
                    try {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(position == checkedPosition ?
                                XPopup.getPrimaryColor() : ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_title_color)));
                    } catch (Exception e) {
                        LogUtil.error(TAG, e.getMessage());
                    }
                } else {
                    if (holder.getView(ResourceTable.Id_check_view) != null) {
                        holder.getView(ResourceTable.Id_check_view).setVisibility(HIDE);
                    }
                    // 如果没有选择，则文字居中
                    holder.<Text>getView(ResourceTable.Id_tv_text).setTextAlignment(TextAlignment.CENTER);
                }
                if (bindItemLayoutId == 0) {
                    if (popupInfo.isDarkTheme) {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
                        holder.getView(ResourceTable.Id_item_dirver).setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_dark_divider)));
                    } else {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_text_dark_color)));
                        holder.getView(ResourceTable.Id_item_dirver).setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_divider)));
                    }
                }
                if (position != data.length - 1) {
                    holder.getView(ResourceTable.Id_item_dirver).setVisibility(VISIBLE);
                } else {
                    holder.getView(ResourceTable.Id_item_dirver).setVisibility(HIDE);
                }
            }
        };

        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                if (selectListener != null) {
                    if (position >= 0 && position < adapter.getData().size()) {
                        selectListener.onSelect(position, adapter.getData().get(position));
                    }
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataChanged();
                }
                if (popupInfo.autoDismiss) {
                    dismiss();
                }
            }
        });
        listContainer.setItemProvider(adapter);
        applyTheme();
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        tv_title.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
        findComponentById(ResourceTable.Id_xpopup_divider).setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_dark_divider)));
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        tv_title.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_text_dark_color)));
        findComponentById(ResourceTable.Id_xpopup_divider).setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_divider)));
    }

    String title;
    String[] data;
    int[] iconIds;

    public CenterListPopupView setStringData(String title, String[] data, int[] iconIds) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    private OnSelectListener selectListener;

    public CenterListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    int checkedPosition = -1;

    /**
     * 设置默认选中的位置
     *
     * @param position 默认选中的位置
     * @return CenterListPopupView自身
     */
    public CenterListPopupView setCheckedPosition(int position) {
        this.checkedPosition = position;
        return this;
    }

    @Override
    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? (int) (super.getMaxWidth() * .8f)
                : popupInfo.maxWidth;
    }

}
