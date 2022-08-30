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



import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.app.Context;


public class SampleAdapter extends RecycleItemProvider {

    private String[] mDataSet;
    private Context context;

    public SampleAdapter(Context context, String[] dataSet) {
        this.mDataSet = dataSet;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataSet.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertView, ComponentContainer parent) {
        DirectionalLayout dependentLayout = new DirectionalLayout(context);
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        dependentLayout.setLayoutConfig(layoutConfig);
        Text text = new Text(context);
        text.setText(mDataSet[position]);
        text.setTextColor(Color.BLACK);
        text.setTextSize(50);
        text.setPadding (15,15,15,15);
        text.setMultipleLine(true);
        text.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));

//        if (position % 2 == 0) {
//            text.setBackground(Color.parseColor("#f5f5f5"));
//        } else {
//            text.setBackground(harmonyos.agp.utils.Color.WHITE);
//        }
        dependentLayout.addComponent(text);
        return dependentLayout;
    }
}
