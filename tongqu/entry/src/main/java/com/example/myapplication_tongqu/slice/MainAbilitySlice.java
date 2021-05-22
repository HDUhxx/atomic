package com.example.myapplication_tongqu.slice;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.guangbo.GuangboAbility;
import com.example.myapplication_tongqu.my.MyAbility;
import com.example.myapplication_tongqu.shop.BianxingAbility;
import com.example.myapplication_tongqu.shop.MianmoAbility;
import com.example.myapplication_tongqu.shop.PaopaoAbility;
import com.example.myapplication_tongqu.weizhi.WeizhiAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice {
    private Button button_my,button_mianmo,button_paopao,
            button_bianxing,button_broadcast,button_dingwei;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        button_my = (Button) findComponentById(ResourceTable.Id_button_my);
        button_mianmo = (Button) findComponentById(ResourceTable.Id_button_mianmo);
        button_paopao = (Button) findComponentById(ResourceTable.Id_button_paopao);
        button_bianxing = (Button) findComponentById(ResourceTable.Id_button_bianxing);
        button_broadcast = (Button) findComponentById(ResourceTable.Id_button_broadcast);
        button_dingwei  = (Button) findComponentById(ResourceTable.Id_button_dingwei);

        myHome();
        mianmo();
        paopao();
        bianxing();
        broadcast();
        dingwei();
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
    public void mianmo(){
        // 为按钮设置点击回调
        button_mianmo.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_mianmo!=null){
                    present(new MianmoAbility(),new Intent());
                }
            }
        });
    }

    //跳转到商品详情界面
    public void paopao(){
        // 为按钮设置点击回调
        button_paopao.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_paopao!=null){
                    present(new PaopaoAbility(),new Intent());
                }
            }
        });
    }

    //跳转到商品详情界面
    public void bianxing(){
        // 为按钮设置点击回调
        button_bianxing.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_bianxing!=null){
                    present(new BianxingAbility(),new Intent());
                }
            }
        });
    }


    //跳转到订单详情界面
    public void broadcast(){
        // 为按钮设置点击回调
        button_broadcast.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_broadcast!=null){
                    present(new GuangboAbility(),new Intent());
                }
            }
        });
    }

    //跳转到定位详情界面
    public void dingwei(){
        // 为按钮设置点击回调
        button_dingwei.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_dingwei!=null){
                    present(new WeizhiAbility(),new Intent());
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
