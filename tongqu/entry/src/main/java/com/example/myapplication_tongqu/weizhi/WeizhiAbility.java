package com.example.myapplication_tongqu.weizhi;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.guangbo.GuangboAbility;
import com.example.myapplication_tongqu.my.MyAbility;
import com.example.myapplication_tongqu.my.XieyiAbility;
import com.example.myapplication_tongqu.slice.MainAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class WeizhiAbility extends AbilitySlice {
    private Button button_weizhi_sy,button_weizhi_broadcast,button_weizhi_my;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_dingwei);
        button_weizhi_sy = (Button) findComponentById(ResourceTable.Id_button_weizhi_sy);
        button_weizhi_broadcast = (Button) findComponentById(ResourceTable.Id_button_weizhi_broadcast);
        button_weizhi_my = (Button) findComponentById(ResourceTable.Id_button_weizhi_my);

        shouye();
        guangbo();
        my();
    }

    //跳转到首页界面
    public void shouye(){
        // 为按钮设置点击回调
        button_weizhi_sy.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_weizhi_sy!=null){
                    present(new MainAbilitySlice(),new Intent());
                }
            }
        });
    }

    //跳转到广播界面
    public void my(){
        // 为按钮设置点击回调
        button_weizhi_my.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_weizhi_my!=null){
                    present(new MyAbility(),new Intent());
                }
            }
        });
    }

    //跳转到广播界面
    public void guangbo(){
        // 为按钮设置点击回调
        button_weizhi_broadcast.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_weizhi_broadcast!=null){
                    present(new GuangboAbility(),new Intent());
                }
            }
        });
    }
}
