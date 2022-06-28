package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.HorizontalAttachPopupView;
import com.lxj.xpopup.util.ToastUtil;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.app.Context;

/**
 * Description: 自定义Attach弹窗，水平方向的
 * Create by lxj, at 2019/3/13
 */
public class CustomAttachPopup extends HorizontalAttachPopupView {
    public CustomAttachPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findComponentById(ResourceTable.Id_tv_zan).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                ToastUtil.showToast(getContext(), "赞");
                dismiss();
            }
        });
        findComponentById(ResourceTable.Id_tv_comment).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                ToastUtil.showToast(getContext(), "评论");
                dismiss();
            }
        });
        if (shadowLayout != null) {
            shadowLayout.setRadius(0);
        }
    }

}