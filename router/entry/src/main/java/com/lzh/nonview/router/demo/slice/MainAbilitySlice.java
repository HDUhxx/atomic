/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzh.nonview.router.demo.slice;

import com.lzh.nonview.router.demo.ResourceTable;
import com.lzh.nonview.router.demo.interceptors.LoginInterceptor;
import com.lzh.nonview.router.demo.pojo.User;
import com.lzh.nonview.router.demo.utils.LogUtils;
import com.lzh.nonview.router.Router;
import com.lzh.nonview.router.activityresult.ActivityResultCallback;
import com.lzh.nonview.router.exception.NotFoundException;
import com.lzh.nonview.router.module.RouteRule;
import com.lzh.nonview.router.route.RouteCallback;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.utils.net.Uri;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 主页
 *
 * @since 2021-03-20
 */
public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    ExecutorService pool = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("action_executor");
            return thread;
        }
    });

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        final Button openbrower = (Button) findComponentById(ResourceTable.Id_open_browser);
        final Button toPrinterActivityWithRequestLogin =
                (Button) findComponentById(ResourceTable.Id_toPrinterActivityWithRequestLogin);
        final Button toPrinterActivityWithInterceptor =
                (Button) findComponentById(ResourceTable.Id_toPrinterActivityWithInterceptor);
        final Button toPrinterActivityWithoutInterceptor =
                (Button) findComponentById(ResourceTable.Id_toPrinterActivityWithoutInterceptor);
        final Button toUserActivity = (Button) findComponentById(ResourceTable.Id_toUserActivity);
        final Button toPrinterActivityWithExtras =
                (Button) findComponentById(ResourceTable.Id_toPrinterActivityWithExtras);
        final Button toResultActivity = (Button) findComponentById(ResourceTable.Id_toResultActivity);
        final Button launchActionRoute =
                (Button) findComponentById(ResourceTable.Id_launchActionRoute);
        final Button launchActionRouteWithExecutorAnnotation =
                (Button) findComponentById(ResourceTable.Id_launchActionRouteWithExecutorAnnotation);
        final Button launchActionRouteWithExecutorConfig =
                (Button) findComponentById(ResourceTable.Id_launchActionRouteWithExecutorConfig);
        final Button createInstanceForJavaBean =
                (Button) findComponentById(ResourceTable.Id_createInstanceForJavaBean);
        final Button toAutoParseActivity =
                (Button) findComponentById(ResourceTable.Id_toAutoParseActivity);
        openbrower.setClickedListener(MainAbilitySlice.this);
        toPrinterActivityWithRequestLogin.setClickedListener(MainAbilitySlice.this);
        toPrinterActivityWithInterceptor.setClickedListener(MainAbilitySlice.this);
        toPrinterActivityWithoutInterceptor.setClickedListener(MainAbilitySlice.this);
        toUserActivity.setClickedListener(MainAbilitySlice.this);
        toPrinterActivityWithExtras.setClickedListener(MainAbilitySlice.this);
        toResultActivity.setClickedListener(MainAbilitySlice.this);
        launchActionRoute.setClickedListener(MainAbilitySlice.this);
        launchActionRouteWithExecutorAnnotation.setClickedListener(MainAbilitySlice.this);
        launchActionRouteWithExecutorConfig.setClickedListener(MainAbilitySlice.this);
        createInstanceForJavaBean.setClickedListener(MainAbilitySlice.this);
        toAutoParseActivity.setClickedListener(MainAbilitySlice.this);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_open_browser:
                Router.create(getString(ResourceTable.String_baiduurl)).open(getAbility());break;
            case ResourceTable.Id_toPrinterActivityWithRequestLogin:
                Router.create("haoge://page/intent/printer?title=动态登录检查&requestLogin=1").open(getAbility());break;
            case ResourceTable.Id_toPrinterActivityWithInterceptor:
                Router.create("haoge://page/intent/printer?title=使用指定拦截器")
                .addInterceptor(new LoginInterceptor()).open(getAbility());break;
            case ResourceTable.Id_toPrinterActivityWithoutInterceptor:
                Router.create("haoge://page/intent/printer?title=不使用拦截器进行跳转").open(getAbility());break;
            case ResourceTable.Id_toUserActivity:
                Router.create("haoge://page/user-info?username=测试账号").open(getAbility());break;
            /**
             * 跳转到数据打印页(添加额外数据)
             */
            case ResourceTable.Id_toPrinterActivityWithExtras:
                addExtraStartAbility();break;
            /**
             * 使用ActivityResult回调
             */
            case ResourceTable.Id_toResultActivity: requestStartAbility();break;
            case ResourceTable.Id_toAutoParseActivity: autoParseActivity();break;
            /**
             * 启动简单的动作路由
             */
            case ResourceTable.Id_launchActionRoute:
                Router.create("haoge://page/say/hello").open(getAbility());break;
            /**
             * 启动一个简单的动作路由(使用注解进行线程指定)
             */
            case ResourceTable.Id_launchActionRouteWithExecutorAnnotation:
                Router.create("haoge://page/executor/switcher").open(getAbility());break;
            /**
             * 启动一个简单的动作路由(使用执行器进行线程指定)
             */
            case ResourceTable.Id_launchActionRouteWithExecutorConfig:
                Router.create("haoge://page/executor/switcher").setExecutor(pool).open(getAbility());break;
            case ResourceTable.Id_createInstanceForJavaBean:
                User user = Router.createInstanceRouter("haoge://page/creator/user?name=CreatorRouter")
                        .<User>createInstance();
                showToast("user==" + user.toString());break;
            default: break;
        }
    }

    /**
     * 显示土司
     *
     * @param msg
     */
    private void showToast(String msg) {
        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(getAbility())
                .parse(ResourceTable.Layout_layout_toast_and_image, null, false);
        Text text = (Text) layout.findComponentById(ResourceTable.Id_msg_toast);
        text.setText(msg);
        new ToastDialog(getAbility())
                .setComponent(layout)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_PARENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAlignment(LayoutAlignment.CENTER)
                .show();
    }

    /**
     * 传参
     */
    private void autoParseActivity() {
        IntentParams intentParams = new IntentParams();
        intentParams.setParam("mBoolean", true);
        byte bb = 0;
        intentParams.setParam("mByte", bb);
        short ms = 1;
        intentParams.setParam("mShort", ms);
        intentParams.setParam("mChar", 'c');
        intentParams.setParam("mInt", LogUtils.WARN);
        intentParams.setParam("mFloat", LogUtils.PAI);
        intentParams.setParam("mDouble", LogUtils.DOUBLE);
        intentParams.setParam("mLong", LogUtils.LONG);
        intentParams.setParam("mString", "HaogeStudio");
        User user = new User();
        user.setName("HaogeStudio");
        intentParams.setParam("mUser", user.toString());
        intentParams.setParam("mUrl", LogUtils.getBaiduUrl(getAbility()));
        Router.create("haoge://page/parceler-args").addExtras(intentParams).open(getAbility());
    }

    private void requestStartAbility() {
        Router.create("haoge://page/result").resultCallback(new ActivityResultCallback() {
            @Override
            public void onResult(int resultCode, Intent data) {
                new ToastDialog(getContext()).setText("返回码是" + resultCode)
                        .setAlignment(LayoutAlignment.CENTER).setDuration(LogUtils.DURATION).show();
                LogUtils.log(LogUtils.INFO, LogUtils.TAG, "返回码是" + resultCode);
            }
        }).open(this.getAbility());
    }

    private void addExtraStartAbility() {
        IntentParams data = new IntentParams();
        data.setParam("用户名", "测试");
        data.setParam("密码", "你猜");
        Router.create("haoge://page/intent/printer")
                /**
                 * // 指定请求码，使用startActivityForResult跳转
                 */
                .requestCode(LogUtils.REQUEST_CODE)
                /**
                 * // 添加额外数据。将放入Intent中进行传递:Intent.putExtras(data)
                 */
                .addExtras(data)
                /**
                 * // 添加拦截器,若添加有多个拦截器，将被依次触发
                 */
                .addInterceptor(new LoginInterceptor())
                .setCallback(new RouteCallback() {
                    @Override
                    public void notFound(Uri uri, NotFoundException e) {
                        LogUtils.log(LogUtils.INFO, LogUtils.TAG, "没匹配到与此uri所匹配的路由目标");
                    }

                    @Override
                    public void onOpenSuccess(Uri uri, RouteRule rule) {
                        LogUtils.log(LogUtils.INFO, LogUtils.TAG, "打开路由成功");
                        new ToastDialog(getContext()).setText("打开路由成功").setDuration(LogUtils.DURATION).show();
                    }

                    @Override
                    public void onOpenFailed(Uri uri, Throwable e) {
                        LogUtils.log(LogUtils.INFO, LogUtils.TAG, "打开路由失败");
                    }
                })
                // 设置转场动画
                .setAnim(ResourceTable.Animation_anim_fade_in, ResourceTable.Animation_anim_fade_out)
                .open(MainAbilitySlice.this.getAbility());
    }
}
