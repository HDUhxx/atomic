package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.app.Context;

/**
 * Description: 自定义全屏弹窗
 * Create by lxj, at 2019/3/12
 */
public class CustomFullScreenPopup extends FullScreenPopupView {
    public CustomFullScreenPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_fullscreen_popup;
    }
}
