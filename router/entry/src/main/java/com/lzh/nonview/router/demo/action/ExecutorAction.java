package com.lzh.nonview.router.demo.action;

import com.lzh.nonview.router.demo.ResourceTable;
import com.lzh.nonview.router.demo.executor.TestExecutor;
import com.lzh.nonview.router.anno.RouteExecutor;
import com.lzh.nonview.router.anno.RouterRule;
import com.lzh.nonview.router.route.ActionSupport;

import ohos.aafwk.content.IntentParams;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * 动作
 *
 * @since 2021-03-20
 */
@RouteExecutor(TestExecutor.class)
@RouterRule("executor/switcher")
public class ExecutorAction extends ActionSupport {
    @Override
    public void onRouteTrigger(Context context, IntentParams bundle) {
        String threadName = Thread.currentThread().getName();
        context.getUITaskDispatcher().asyncDispatch(() -> {
            /**
             * 更新UI的操作
             */
            DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_layout_toast_and_image, null, false);
            Text text = (Text) layout.findComponentById(ResourceTable.Id_msg_toast);
            text.setText("线程切换器测试动作路由被启动  启动线程为:" + threadName);
            new ToastDialog(context)
                    .setComponent(layout)
                    .setSize(DirectionalLayout.LayoutConfig.MATCH_PARENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                    .setAlignment(LayoutAlignment.CENTER)
                    .show();
        });
    }
}
