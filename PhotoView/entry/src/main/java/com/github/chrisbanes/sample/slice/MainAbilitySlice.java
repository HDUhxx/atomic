package com.github.chrisbanes.sample.slice;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.sample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        PhotoView photoView = (PhotoView) findComponentById(ResourceTable.Id_photo_v);
        photoView.setPixelMap(ResourceTable.Media_wallpaper);
//        Glide.with(getContext())
//                .load("https://picsum.photos/600")
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .placeholder(ResourceTable.Media_wallpaper)
//                .into(photoView);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
