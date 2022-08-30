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
package com.youth.banner;


import ohos.agp.components.AttrSet;
import ohos.agp.components.ScrollView;
import ohos.app.Context;

public class BannerScroller extends ScrollView {
    private int mDuration = BannerConfig.DURATION;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public BannerScroller(Context context, AttrSet attrSet, String sn) {
        super(context, attrSet, sn);
    }


    public void setDuration(int time) {
        mDuration = time;
    }

}