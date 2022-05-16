package com.lxj.xpopup.core;

import com.lxj.xpopup.util.LogUtil;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.ComponentContainer;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;

public class FullScreenDialog extends CommonDialog {

    protected BasePopupView contentView;
    private Context context;

    public FullScreenDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (getWindow() == null || contentView == null || contentView.popupInfo == null) {
            return;
        }
        if (contentView.popupInfo.enableShowWhenAppBackground) {
            // 支持悬浮窗（即应用后台弹出Xpopup）
            WindowManager.LayoutConfig layoutConfig = getWindow().getLayoutConfig().get();
            layoutConfig.type = WindowManager.LayoutConfig.MOD_APPLICATION_OVERLAY;
            getWindow().setLayoutConfig(layoutConfig);
        }
        setTransparent(true);
        setSize(XPopupUtils.getWindowWidth(context), XPopupUtils.getAppHeight(context));
        if (!contentView.popupInfo.isRequestFocus) {
            // 不获取焦点
            LogUtil.debug("XPopup", "waiting for improvement");
        }
        // 自动设置状态色调，亮色还是暗色
        autoSetStatusBarMode();
        contentView.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
        setContentCustomComponent(contentView);
    }

    /**
     * 是否是亮色调状态栏
     */
    public void autoSetStatusBarMode() {
        // 隐藏状态栏
        if (!contentView.popupInfo.hasStatusBar) {
            return;
        }
    }

    public FullScreenDialog setContent(BasePopupView view) {
        if (view.getComponentParent() != null) {
            view.getComponentParent().removeComponent(view);
        }
        this.contentView = view;
        return this;
    }

}
