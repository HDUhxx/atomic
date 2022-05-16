package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView extends PartShadowPopupView {
    public CustomPartShadowPopupView(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_part_shadow_popup;
    }

    Text text;

    @Override
    protected void onCreate() {
        super.onCreate();
        text = (Text) findComponentById(ResourceTable.Id_text);
        findComponentById(ResourceTable.Id_ch).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                text.setText(text.getText() + "\n 啦啦啦啦啦啦");
            }
        });
        findComponentById(ResourceTable.Id_btnClose).setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                dismiss();
            }
        });
    }

}
