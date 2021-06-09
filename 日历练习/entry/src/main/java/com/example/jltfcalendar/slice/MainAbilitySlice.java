package com.example.jltfcalendar.slice;


import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.agp.components.DependentLayout.LayoutConfig;

import java.util.Date;

public class MainAbilitySlice extends AbilitySlice {

    // 星期
    private final String[] days = new String[]{"日","一","二","三","四","五","六"};

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        // 新建table布局并设置样式
        TableLayout tableLayout =  new TableLayout(getContext());
        tableLayout.setWidth(LayoutConfig.MATCH_PARENT);
        tableLayout.setHeight(LayoutConfig.MATCH_PARENT);
        ShapeElement backgroundShape = new ShapeElement();
        backgroundShape.setRgbColor(new RgbColor(255,255,0));
        tableLayout.setBackground(backgroundShape);
        tableLayout.setRowCount(6);
        tableLayout.setColumnCount(7);

        // 循环星期
        for (String s : days) {
            // 调用text并设置值
            Text text = text();
            text.setText(s);
            // 追加到table布局中
            tableLayout.addComponent(text);
        }

        // 日期
        Date date = new Date();
        // 获取年
        int year = date.getYear();
        // 获取月
        int month = date.getMonth() + 1;
        // 获取日
        int today = date.getDate();
        // 当月的天数
        int Count= new Date(year, month, 0).getDate();
        // 当月的第一天的星期
        int day = new Date(year, new Date().getMonth(), 1).getDay();
        // 是否为星期日
        if(day != 0) {
            // 循环空格
            for (int i = 0; i < day; i++) {
                // 调用text并设置值
                Text text = text();
                text.setText("");
                // 追加到table布局中
                tableLayout.addComponent(text);
            }
        }

        // 循环天数
        for (int i = 1; i <= Count; i++) {
            // 调用text并设置值
            Text text = text();
            if(today == i) {
                text.setTextColor(Color.RED);
            }
            text.setText("" + i);
            // 追加到table布局中
            tableLayout.addComponent(text);
        }
        super.setUIContent(tableLayout);
    }


    public Text text() {
        Text text = new Text(getContext());
        text.setWidth(150);
        text.setHeight(150);
        text.setTextSize(100);
        text.setTextColor(Color.BLUE);
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
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
