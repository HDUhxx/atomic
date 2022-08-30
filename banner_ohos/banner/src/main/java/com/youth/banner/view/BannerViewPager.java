/*
 *    Copyright 2021 youth5201314
 *    Copyright 2021 Institute of Software Chinese Academy of Sciences, ISRC

 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youth.banner.view;


import ohos.agp.components.AttrSet;
import ohos.agp.components.PageSlider;
import ohos.app.Context;


public class BannerViewPager extends PageSlider {
    private boolean scrollable = true;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttrSet attrs) {
        super(context, attrs);
    }



    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        this.setSlidingPossible(scrollable);
    }
}