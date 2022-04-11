package com.lzh.nonview.router.launcher;

import com.lzh.nonview.router.Router;
import com.lzh.nonview.router.activityresult.ActivityResultCallback;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.module.RouteRule;
import com.lzh.nonview.router.tools.Constants;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.IntentParams;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 */
 public abstract class Launcher {
    private static SecureRandom sCodeGenerator = new SecureRandom();
    protected Uri uri;
    protected IntentParams bundle;
    protected RouteBundleExtras extras;
    protected RouteRule rule;
    protected IntentParams remote;

    protected Ability resumeContext;
    protected ActivityResultCallback resultCallback;
    protected IntentParams options;

    /**
     * 需要与此启动器一起打开.
     * @param context context
     * @throws Exception Some error occurs.
     */
    public abstract void open(Ability context) throws Exception;

    /**
     * 将所有附加数据设置为已使用
     *
     * @param uri 路线uri
     * @param bundle 由uri解析的bundle数据
     * @param extras 设置的Extras数据*
     * @param rule 与uri关联的规则
     * @param remote remote
     */
    public final void set(Uri uri, IntentParams bundle, RouteBundleExtras extras,  RouteRule rule, IntentParams remote) {
        this.uri = uri;
        this.bundle = bundle;
        this.extras = extras;
        this.rule = rule;
        this.remote = remote;

        resumeContext = extras.getValue(Constants.KEY_RESUME_CONTEXT);
        resultCallback = extras.getValue(Constants.KEY_RESULT_CALLBACK);
        options = extras.getValue(Constants.KEY_ACTIVITY_OPTIONS);

        int requestCode = extras.getRequestCode();
        if (resultCallback != null && requestCode == -1) {
            extras.setRequestCode(sCodeGenerator.nextInt(0x0000ffff));
        }
    }

}
