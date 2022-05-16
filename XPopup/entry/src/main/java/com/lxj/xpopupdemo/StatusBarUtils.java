package com.lxj.xpopupdemo;

import ohos.aafwk.ability.Ability;

/**
 * 状态栏操作工具类
 */
public class StatusBarUtils {

    /**
     * 设置状态栏的颜色
     *
     * @param ability   当前页面
     * @param argbColor 颜色，argb
     */
    public static void setStatusBarColor(Ability ability, int argbColor) {
        if (ability != null) {
            ability.getWindow().setStatusBarColor(argbColor);
        }
    }

}
