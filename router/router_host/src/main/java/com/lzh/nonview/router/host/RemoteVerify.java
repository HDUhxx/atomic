package com.lzh.nonview.router.host;


import ohos.app.Context;

/**
 * 提供用于安全验证的界面。
 */
public interface RemoteVerify {
    /**
     * 验证您要连接的客户端
     *
     * @param context 应用程序上下文
     * @param id id
     * @return 是否连接成功
     */
    boolean verify(Context context,int id);
}
