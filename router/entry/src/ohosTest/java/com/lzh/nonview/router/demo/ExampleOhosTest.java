package com.lzh.nonview.router.demo;

import com.lzh.nonview.router.Router;
import com.lzh.nonview.router.RouterConfiguration;
import com.lzh.nonview.router.demo.interceptors.DefaultInterceptor;
import com.lzh.nonview.router.demo.interceptors.LoginInterceptor;
import com.lzh.nonview.router.demo.utils.LogUtils;
import com.lzh.nonview.router.exception.NotFoundException;
import com.lzh.nonview.router.interceptors.RouteInterceptor;
import com.lzh.nonview.router.module.RouteRule;
import com.lzh.nonview.router.route.RouteCallback;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.agp.window.dialog.ToastDialog;
import ohos.utils.net.Uri;
import org.junit.Test;


public class ExampleOhosTest {
    @Test
    public void addExtraStartAbility() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        RouterConfiguration.get().addRouteCreator(new com.haoge.studio.RouterRuleCreator());
        RouteInterceptor interceptor = new DefaultInterceptor();
        RouterConfiguration.get().setInterceptor(interceptor);
        Ability ability = AbilityDelegatorRegistry.getAbilityDelegator().getCurrentTopAbility();
        IntentParams data = new IntentParams();
        data.setParam("用户名", "测试");
        data.setParam("密码", "你猜");
        Router.create("haoge://page/intent/printer")
                .requestCode(LogUtils.REQUEST_CODE)
                .addExtras(data)
                .addInterceptor(new DefaultInterceptor())
                .setCallback(new RouteCallback() {
                    @Override
                    public void notFound(Uri uri, NotFoundException e) {
                        LogUtils.log(LogUtils.INFO, LogUtils.TAG, "没匹配到与此uri所匹配的路由目标");
                    }

                    @Override
                    public void onOpenSuccess(Uri uri, RouteRule rule) {
                        LogUtils.log(LogUtils.INFO, LogUtils.TAG, "打开路由成功");
                        ability.getUITaskDispatcher().syncDispatch(new Runnable() {
                            @Override
                            public void run() {
                                new ToastDialog(ability).setText("打开路由成功").setDuration(LogUtils.DURATION).show();
                            }
                        });

                    }

                    @Override
                    public void onOpenFailed(Uri uri, Throwable e) {
                        LogUtils.log(LogUtils.INFO, LogUtils.TAG, "打开路由失败");
                    }
                })
                .setAnim(ResourceTable.Animation_anim_fade_in, ResourceTable.Animation_anim_fade_out)
                .open(ability);
    }

    @Test
    public void noInterceptor() {
        RouterConfiguration.get().addRouteCreator(new com.haoge.studio.RouterRuleCreator());
        Ability ability = AbilityDelegatorRegistry.getAbilityDelegator().getCurrentTopAbility();
        Router.create("haoge://page/intent/printer?title=不使用拦截器进行跳转").open(ability);
    }

    @Test
    public void UseInterceptor() {
        RouterConfiguration.get().addRouteCreator(new com.haoge.studio.RouterRuleCreator());
        Ability ability = AbilityDelegatorRegistry.getAbilityDelegator().getCurrentTopAbility();
        Router.create("haoge://page/intent/printer?title=使用指定拦截器")
                .addInterceptor(new LoginInterceptor()).open(ability);
    }
}