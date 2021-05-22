package com.example.myapplication_tongqu.my;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.guangbo.GuangboAbility;
import com.example.myapplication_tongqu.shezhi.SheZhiAbility;
import com.example.myapplication_tongqu.slice.LoginAbility;
import com.example.myapplication_tongqu.slice.LoginZcAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MyAbility extends AbilitySlice {
    private Button button_denglu,button_zhuce,button_set,button_xieyi,button_shengming,button_guangbo;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layou_my);
        button_denglu = (Button) findComponentById(ResourceTable.Id_button_denglu);
        button_zhuce = (Button) findComponentById(ResourceTable.Id_button_zhuce);
        button_set = (Button) findComponentById(ResourceTable.Id_button_set);
        button_xieyi = (Button) findComponentById(ResourceTable.Id_button_xieyi);
        button_shengming = (Button) findComponentById(ResourceTable.Id_button_shengming);
        button_guangbo = (Button) findComponentById(ResourceTable.Id_button_guangbo);

       denglu();
       zhuce();
       shezi();
       xieyi();
       shengming();
       guangbo();
    }

    //跳转到登录界面
    public void denglu(){
        // 为按钮设置点击回调
        button_denglu.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_denglu!=null){
                    present(new LoginAbility(),new Intent());
                }
            }
        });
    }

    //跳转到登录界面
    public void zhuce(){
        // 为按钮设置点击回调
        button_zhuce.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_zhuce!=null){
                    present(new LoginZcAbility(),new Intent());
                }
            }
        });
    }

    //跳转到设置界面
    public void shezi(){
        // 为按钮设置点击回调
        button_set.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_set!=null){
                    present(new SheZhiAbility(),new Intent());
                }
            }
        });
    }

    //跳转到订单界面
    public void xieyi(){
        // 为按钮设置点击回调
        button_xieyi.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_xieyi!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }

    //跳转到订单界面
    public void shengming(){
        // 为按钮设置点击回调
        button_shengming.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shengming!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }
    //跳转到订单界面
    public void guangbo(){
        // 为按钮设置点击回调
        button_guangbo.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_guangbo!=null){
                    present(new GuangboAbility(),new Intent());
                }
            }
        });
    }


}
