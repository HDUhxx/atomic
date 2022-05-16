package com.lxj.xpopup.impl;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * Description: 加载对话框
 * Create by dance, at 2018/12/16
 */
public class LoadingPopupView extends CenterPopupView {

    private Text tv_title;

    /**
     * @param context      上下文
     * @param bindLayoutId layoutId 如果要显示标题，则要求必须有id为tv_title的Text，否则无任何要求
     * @param popupInfo    弹窗信息
     */
    public LoadingPopupView(Context context, int bindLayoutId, PopupInfo popupInfo) {
        super(context, popupInfo);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : ResourceTable.Layout__xpopup_center_impl_loading;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tv_title = (Text) findComponentById(ResourceTable.Id_tv_title);
        if (bindLayoutId == 0) {
            getPopupImplView().setBackground(XPopupUtils.createDrawable(0x000000CF, popupInfo.borderRadius));
        }
        setup();
    }

    protected void setup() {
        if (tv_title == null) {
            return;
        }
        if (title != null && title.length() != 0) {
            tv_title.setVisibility(VISIBLE);
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(HIDE);
        }
    }

    private String title;

    public LoadingPopupView setTitle(String title) {
        this.title = title;
        setup();
        return this;
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        if (tv_title == null) {
            return;
        }
        tv_title.setText("");
        tv_title.setVisibility(HIDE);
    }

}
