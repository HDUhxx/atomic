package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * Description: 自定义背景的Attach弹窗
 * Create by lxj, at 2019/3/13
 */
public class CustomAttachPopup2 extends AttachPopupView {
    public CustomAttachPopup2(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_attach_popup2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        final Text tv = (Text) findComponentById(ResourceTable.Id_tv);
        tv.setText("床前明月光，\n疑是地上霜，\n举头望明月，\n低头思故乡。");
        tv.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
            }
        });
        getPopupImplView().setBackground(XPopupUtils.createDrawable(0xFFFFFFFF, popupInfo.borderRadius, 0, popupInfo.borderRadius, 0));
    }

}
