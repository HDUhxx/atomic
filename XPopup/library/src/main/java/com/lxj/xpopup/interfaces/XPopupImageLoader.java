package com.lxj.xpopup.interfaces;

import ohos.agp.components.Image;
import ohos.app.Context;

import java.io.File;

/**
 * 加载图片接口
 */
public interface XPopupImageLoader {
    /**
     * 加载图片
     *
     * @param position  ViewPager中的序号
     * @param uri       要加载的图片地址
     * @param imageView 要加载图片的控件
     */
    void loadImage(int position, String uri, Image imageView);

    /**
     * 获取图片对应的文件
     *
     * @param context 上下文
     * @param uri     图片网络地址
     * @return File图片文件
     */
    File getImageFile(Context context, String uri);
}
