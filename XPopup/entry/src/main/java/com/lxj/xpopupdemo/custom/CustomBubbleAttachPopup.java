package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.BubbleAttachPopupView;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * Description: 自定义气泡Attach弹窗,
 * Create by lxj, at 2019/3/13
 */
public class CustomBubbleAttachPopup extends BubbleAttachPopupView {
    public CustomBubbleAttachPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_bubble_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        final Text tv = (Text) findComponentById(ResourceTable.Id_tv);
        tv.setText("床前明月光，\n疑是地上霜，\n举头望明月，\n低头思故乡。");
        tv.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                dismiss();
            }
        });
    }

}
