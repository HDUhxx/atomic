package com.lzh.nonview.router.demo.manager;

/**
 * 一个本地的内存数据管理容器。提供登录状态进行使用
 *
 * @since 2021-03-20
 */
public class DataManager {
    /**
     * 登录状态
     */
    private static boolean isLogin = false;
    /**
     * username
     */
    private static String username = "HaogeStudio";

    /**
     * 构造函数
     */
    private DataManager() {
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DataManager.username = username;
    }

    public static boolean isIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        DataManager.isLogin = isLogin;
    }
}
