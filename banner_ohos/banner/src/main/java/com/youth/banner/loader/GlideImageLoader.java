package com.youth.banner.loader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.youth.banner.ResourceTable;
import ohos.agp.components.Image;
import ohos.app.Context;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, Image imageView) {

        Glide.with(context)
                .load(path)
                .placeholder(ResourceTable.Media_no_banner)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(imageView);
    }
}
