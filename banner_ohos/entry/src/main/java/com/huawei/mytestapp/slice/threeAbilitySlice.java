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
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.bundle.AbilityInfo;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class threeAbilitySlice extends AbilitySlice implements OnBannerListener {

    private DirectionalLayout myLayout = new DirectionalLayout(this);
    private DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);

    Banner banner;
    Button button;
    Text text;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setDisplayOrientation(AbilityInfo.DisplayOrientation.PORTRAIT);
        layoutConfig.setMargins(20,20,20,20);
        layoutConfig.alignment= LayoutAlignment.HORIZONTAL_CENTER;
        myLayout.setLayoutConfig(layoutConfig);
        layoutConfig.width= ComponentContainer.LayoutConfig.MATCH_CONTENT;
        layoutConfig.height = ComponentContainer.LayoutConfig.MATCH_CONTENT;
        try {
            banner = new Banner(this);
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ShapeElement element = new ShapeElement();
        element.setShape(ShapeElement.RECTANGLE);
        element.setRgbColor(new RgbColor(255, 255, 255));
        myLayout.setBackground(element);
//        text.setLayoutConfig(layoutConfig);
        banner.setLayoutConfig(layoutConfig);
        button = new Button(this);
        button.setText("Banner演示:");
        button.setTextSize(150);
        button.setBackground(element);
        button.setLayoutConfig(layoutConfig);
        banner.setOnBannerListener(this);

        List<Integer> list=new ArrayList<>();


        list.add(ResourceTable.Media_1);
        list.add(ResourceTable.Media_2);
        list.add(ResourceTable.Media_3);
        list.add(ResourceTable.Media_4);
        list.add(ResourceTable.Media_5);

        text = new Text(this);
        text.setText("  Banner 是一种灵巧好用的鸿蒙广告轮播控件，现在的绝大数app(如淘宝，支付宝，知乎，bilibili）都需要使用banner界面，Banner实现了循环播放多个广告图片和手动滑动循环等功能");
        text.setTextSize(50);
        text.setMaxTextLines(100);
        text.setMultipleLine(true);
        text.setTruncationMode(Text.TruncationMode.NONE);
        text.setLayoutConfig(layoutConfig);
        Text text1 = new Text(this);
        text1.setText("111111111111111111");
        text1.setTextSize(50);
        text1.setMaxTextLines(100);
        text1.setMultipleLine(true);
        text1.setTruncationMode(Text.TruncationMode.NONE);
        text1.setLayoutConfig(layoutConfig);

        Text text2 = new Text(this);
        text2.setText("222222222222222222222222");
        text2.setTextSize(100);
        text2.setMaxTextLines(100);
        text2.setMultipleLine(true);
        text2.setTruncationMode(Text.TruncationMode.NONE);
        text2.setLayoutConfig(layoutConfig);

        Text text3 = new Text(this);
        text3.setText("3333333333333333333333333333");
        text3.setTextSize(100);
        text3.setMaxTextLines(100);
        text3.setMultipleLine(true);
        text3.setTruncationMode(Text.TruncationMode.NONE);
        text3.setLayoutConfig(layoutConfig);

        Text text4 = new Text(this);
        text4.setText("44444444444444444444444444444");
        text4.setTextSize(50);
        text4.setMaxTextLines(100);
        text4.setMultipleLine(true);
        text4.setTruncationMode(Text.TruncationMode.NONE);
        text4.setLayoutConfig(layoutConfig);

        Text text5 = new Text(this);
        text5.setText("5555555555555555555555555555");
        text5.setTextSize(50);
        text5.setMaxTextLines(100);
        text5.setMultipleLine(true);
        text5.setTruncationMode(Text.TruncationMode.NONE);
        text5.setLayoutConfig(layoutConfig);

        List<String> title=new ArrayList<>();
        title.add("标题1");
        title.add("园您星梦");
        title.add("拳击");
        title.add("红色法拉利大促销");
        title.add("边看边买");

        try {
            banner.setImages(list).setBannerTitles(title).setScaleType(3).setDelayTime(3000).setBannerStyle(1).setTitleTextSize(60).start();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, 1000);
        banner.setLayoutConfig(layoutConfig);


        myLayout.addComponent(button);
        myLayout.addComponent(text);
        myLayout.addComponent(banner);






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

    @Override
    public void OnBannerClick(int position) {
        if (position == 3){
            present(new SecondAbilitySlice(),new Intent());

        }
    }
}
