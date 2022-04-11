package com.lzh.nonview.router.demo.interceptors;

import com.lzh.nonview.router.demo.LoginAbility;
import com.lzh.nonview.router.demo.manager.DataManager;
import com.lzh.nonview.router.extras.RouteBundleExtras;
import com.lzh.nonview.router.interceptors.RouteInterceptor;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.utils.net.Uri;

/**
 * 默认拦截器。所有路由(除掉直接以浏览器方式打开的路由)均会触发此拦截器以用作登录开关控制：为登录拦截添加动态登录控制
 *
 * @since 2021-03-20
 **/
public class LoginInterceptor implements RouteInterceptor {
    @Override
    public boolean intercept(Uri uri, RouteBundleExtras extras, Ability context) {
        // 判断是否已登录。已登录：不拦截、登录：拦截
        return !DataManager.isIsLogin();
    }

    @Override
    public void onIntercepted(Uri uri, RouteBundleExtras extras, Ability context) {
        // 拦截后，将数据传递到登录页去。待登录完成后进行路由恢复
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(context.getBundleName())
                .withAbilityName(LoginAbility.class)
                .build();
        Intent intent = new Intent();
        intent.setParam("uri", uri);
        intent.setParam("extras", extras);
        intent.setParam("extrasIntentPararms", extras.getExtras());
        intent.setOperation(operation);
        context.startAbility(intent);
    }
}
