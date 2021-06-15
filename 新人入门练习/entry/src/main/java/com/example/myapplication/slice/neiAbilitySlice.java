package com.example.myapplication.slice;

import com.example.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;

public class neiAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_nei);

        Button button = (Button) findComponentById(ResourceTable.Id_fhbutton);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 修改 Button 背景颜色
                ShapeElement shapeElement = new ShapeElement();
                // 设置 组件 背景
                button.setBackground(shapeElement);
                // 设置文本
                button.setText("返回");
                // 设置文本颜色
                button.setTextColor(Color.WHITE);
                // 设置文本大小
                button.setTextSize(60);

                present(new MainAbilitySlice(),new Intent());
            }
        });
    }
}
