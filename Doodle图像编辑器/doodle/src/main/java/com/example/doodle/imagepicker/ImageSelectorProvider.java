/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.doodle.imagepicker;

import com.example.doodle.ResourceTable;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.media.image.PixelMap;

import java.util.ArrayList;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class ImageSelectorProvider extends BaseItemProvider {
    private static final int MINUS_INDEX = -1;
    private ArrayList<PixelMap> mList;
    private Ability ability;
    private ImageSelectorProvider.Componentholder viewholder;
    private int slecitem = MINUS_INDEX;
    private boolean isTtype = false;

    /**
     * 构造函数
     *
     * @param list
     * @param ability
     * @since 2021-05-06
     */
    public ImageSelectorProvider(ArrayList<PixelMap> list, Ability ability) {
        this.mList = list;
        this.ability = ability;
    }

    public void setSlecitem(int index) {
        slecitem = index;
    }

    public void setType(boolean isType) {
        this.isTtype = isType;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (mList != null) {
            size = mList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        if (null == convertComponent) {
            viewholder = new ImageSelectorProvider.Componentholder();
            convertComponent = LayoutScatter.getInstance(ability)
                .parse(ResourceTable.Layout_doodle_imageselector_item, null, false);
            viewholder.mImage = (Image) convertComponent.findComponentById(ResourceTable.Id_doodle_image);
            viewholder.mImageSelected = (Image) convertComponent
                .findComponentById(ResourceTable.Id_doodle_image_selected);
            convertComponent.setTag(viewholder);
        } else {
            viewholder = (ImageSelectorProvider.Componentholder) convertComponent.getTag();
        }
        if (!isTtype) {
            if (slecitem == position) {
                viewholder.mImageSelected.setVisibility(Component.VISIBLE);
            } else {
                viewholder.mImageSelected.setVisibility(Component.HIDE);
            }
        } else {
            viewholder.mImageSelected.setVisibility(Component.HIDE);
        }
        PixelMap pixelMap = mList.get(position);
        viewholder.mImage.setPixelMap(pixelMap);
        return convertComponent;
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void refreshPathList(ArrayList<PixelMap> list) {
        mList = new ArrayList<PixelMap>(list);
        notifyDataChanged();
    }

    /**
     * holder
     *
     * @since 2021-04-29
     */
    static class Componentholder {
        Image mImage;
        Image mImageSelected;
    }
}
