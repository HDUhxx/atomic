package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.util.ToastUtil;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.app.Context;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public abstract class BaseStackLayout extends StackLayout {
    Component component;
    boolean isInit = false;

    public BaseStackLayout(Context context) {
        super(context);
        component = LayoutScatter.getInstance(context).parse(getLayoutId(), this, true);
        setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
        if (!isInit) {
            isInit = true;
            init(component);
        }
    }

    /**
     * 获取布局xml文件资源id
     *
     * @return 布局xml文件资源id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化
     *
     * @param component 布局控件
     */
    public abstract void init(Component component);

    /**
     * 页面跳转
     *
     * @param aAbility 当前页面
     * @param bAbility 需要跳转的页面
     */
    public static void startAbility(Context aAbility, Class bAbility) {
        Intent secondIntent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(aAbility.getBundleName())
                .withAbilityName(bAbility.getName())
                .build();
        secondIntent.setOperation(operation);
        aAbility.startAbility(secondIntent, 0);
    }

    /**
     * 吐司
     *
     * @param msg 需要吐司的文本
     */
    public void toast(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

}
