package com.example.myapplication_huibianli.shezhi;

import com.example.myapplication_huibianli.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class SheZhiAbility extends AbilitySlice {
    private Button button_shezi_xieyi,button_shezi_bangzhu,button_shezi_yingshi;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_shezhi);
        button_shezi_xieyi = (Button) findComponentById(ResourceTable.Id_button_shezi_xieyi);
        button_shezi_bangzhu = (Button) findComponentById(ResourceTable.Id_button_shezi_bangzhu);
        button_shezi_yingshi = (Button) findComponentById(ResourceTable.Id_button_shezi_yingshi);

        xieyi();
        yingshi();
        bangzhu();
    }

    //跳转到我的界面
    public void xieyi(){
        // 为按钮设置点击回调
        button_shezi_xieyi.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shezi_xieyi!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }
    //跳转到我的界面
    public void bangzhu(){
        // 为按钮设置点击回调
        button_shezi_bangzhu.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shezi_bangzhu!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }
    //跳转到我的界面
    public void yingshi(){
        // 为按钮设置点击回调
        button_shezi_yingshi.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shezi_yingshi!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }
}
