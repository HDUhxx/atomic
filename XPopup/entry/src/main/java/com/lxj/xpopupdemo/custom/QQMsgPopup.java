package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.PositionPopupView;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.app.Context;

/**
 * Description: 自定义自由定位Position弹窗
 * Create by dance, at 2019/6/14
 */
public class QQMsgPopup extends PositionPopupView {

    public QQMsgPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_popup_qq_msg;
    }

}
