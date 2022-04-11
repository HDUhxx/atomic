package com.lzh.nonview.router.activityresult;


import ohos.aafwk.content.Intent;

/**
 * ActivityResultCallback
 *
 * @since 2021-04-06
 */
public interface ActivityResultCallback {
    /**
     * 回调结果
     *
     * @param resultCode 返回码
     * @param data 数据
     */
    void onResult(int resultCode, Intent data);
}
