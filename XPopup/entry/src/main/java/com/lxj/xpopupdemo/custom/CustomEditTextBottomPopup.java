package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.app.Context;

/**
 * Description: 自定义带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
public class CustomEditTextBottomPopup extends BottomPopupView {

    public CustomEditTextBottomPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_edittext_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findComponentById(ResourceTable.Id_btn_finish).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                dismiss();
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    /**
     * 获取输入框中的文本
     *
     * @return 输入框中的文本
     */
    public String getComment() {
        TextField et = (TextField) findComponentById(ResourceTable.Id_et_comment);
        return et.getText();
    }

}
