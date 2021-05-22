package com.example.myapplication_huibianli.slice;

import com.example.myapplication_huibianli.ResourceTable;
import com.example.myapplication_huibianli.dianpu.DianpuAbility;
import com.example.myapplication_huibianli.my.MyAbility;
import com.example.myapplication_huibianli.shop.CaomeiAbility;
import com.example.myapplication_huibianli.shop.GuaAbility;
import com.example.myapplication_huibianli.shop.JuziAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice {
    private Button button_my,button_caomei,button_juzi,button_gua,button_broadcast;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        button_my = (Button) findComponentById(ResourceTable.Id_button_my);
        button_caomei = (Button) findComponentById(ResourceTable.Id_button_caomei);
        button_juzi = (Button) findComponentById(ResourceTable.Id_button_juzi);
        button_gua = (Button) findComponentById(ResourceTable.Id_button_gua);
        button_broadcast = (Button) findComponentById(ResourceTable.Id_button_broadcast);

        myHome();
        caomei();
        juzi();
        gua();
        mendian();
    }

    //跳转到我的界面
    public void myHome(){
        // 为按钮设置点击回调
        button_my.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_my!=null){
                    present(new MyAbility(),new Intent());
                }
            }
        });
    }


    //跳转到商品详情界面
    public void caomei(){
        // 为按钮设置点击回调
        button_caomei.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_caomei!=null){
                    present(new CaomeiAbility(),new Intent());
                }
            }
        });
    }

    //跳转到商品详情界面
    public void juzi(){
        // 为按钮设置点击回调
        button_juzi.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_juzi!=null){
                    present(new JuziAbility(),new Intent());
                }
            }
        });
    }

    //跳转到商品详情界面
    public void gua(){
        // 为按钮设置点击回调
        button_gua.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_gua!=null){
                    present(new GuaAbility(),new Intent());
                }
            }
        });
    }

    //跳转到商品详情界面
    public void mendian(){
        // 为按钮设置点击回调
        button_broadcast.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_broadcast!=null){
                    present(new DianpuAbility(),new Intent());
                }
            }
        });
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
