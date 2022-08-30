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
package com.huawei.mytestapp.slice;



import com.huawei.mytestapp.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;

public class SecondAbilitySlice extends AbilitySlice {
    private DirectionalLayout myLayout = new DirectionalLayout(this);
    private DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        layoutConfig.setMargins(20,20,20,20);
        layoutConfig.alignment= LayoutAlignment.HORIZONTAL_CENTER;
        myLayout.setLayoutConfig(layoutConfig);
        layoutConfig.width= ComponentContainer.LayoutConfig.MATCH_CONTENT;
        layoutConfig.height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
        Image image = new Image(this);
        image.setLayoutConfig(layoutConfig);
        image.setImageAndDecodeBounds(ResourceTable.Media_123123);
        image.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
        myLayout.addComponent(image);
        Text t = new Text(this);
        t.setMultipleLine(true);
        t.setLayoutConfig(layoutConfig);
        t.setTextSize(100);
        t.setTextColor(Color.RED);
        t.setText("心动价格：500,000,000$");

        Text t1 = new Text(this);
        t1.setMultipleLine(true);
        t1.setLayoutConfig(layoutConfig);
        t1.setTextSize(70);
        t1.setText("颜色分类: 法拉利-烤漆红+皮座椅+底盘装甲");

        Text t2 = new Text(this);
        t2.setMultipleLine(true);
        t2.setLayoutConfig(layoutConfig);
        t2.setTextSize(70);
        t2.setText("整车最大负载强度: 300kg(含)-400kg(含)");
        myLayout.addComponent(t);
        myLayout.addComponent(t1);
        myLayout.addComponent(t2);

        super.setUIContent(myLayout);
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
