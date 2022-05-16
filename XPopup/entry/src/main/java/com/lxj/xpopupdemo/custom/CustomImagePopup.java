package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.app.Context;

/**
 * Description: 自定义大图浏览弹窗
 * Create by dance, at 2019/5/8
 */
public class CustomImagePopup extends ImageViewerPopupView {
    public CustomImagePopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_image_viewer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findComponentById(ResourceTable.Id_tv_top).setBackground(ElementUtil.getShapeElement(0xff000055));
    }
}
