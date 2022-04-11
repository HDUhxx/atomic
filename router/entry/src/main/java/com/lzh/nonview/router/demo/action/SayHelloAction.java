package com.lzh.nonview.router.demo.action;

import com.lzh.nonview.router.anno.RouterRule;
import com.lzh.nonview.router.route.ActionSupport;

import ohos.aafwk.content.IntentParams;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * 接收路由束数据的回调方法。
 *
 * @since 2021-03-20
 */
@RouterRule("say/hello")
public class SayHelloAction extends ActionSupport {
    /**
     * 持续时间
     */
    public static final int DURATION = 2000;

    /**
     * 回调入口
     *
     * @param context
     * @param bundle
     */
    @Override
    public void onRouteTrigger(Context context, IntentParams bundle) {
        new ToastDialog(context).setText("请求动作成功").setAlignment(LayoutAlignment.CENTER).setDuration(DURATION).show();
    }
}
