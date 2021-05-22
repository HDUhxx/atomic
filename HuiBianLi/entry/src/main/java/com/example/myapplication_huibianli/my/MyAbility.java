package com.example.myapplication_huibianli.my;

import com.example.myapplication_huibianli.ResourceTable;
import com.example.myapplication_huibianli.dianpu.DianpuAbility;
import com.example.myapplication_huibianli.shezhi.SheZhiAbility;
import com.example.myapplication_huibianli.slice.LoginAbility;
import com.example.myapplication_huibianli.slice.LoginZcAbility;
import com.example.myapplication_huibianli.slice.MainAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MyAbility extends AbilitySlice {
    private Button button_denglu,button_zhuce,button_set,button_mendian,
            button_mendianlog,button_shouye,button_xieyi,button_shengming;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layou_my);
        button_denglu = (Button) findComponentById(ResourceTable.Id_button_denglu);
        button_zhuce = (Button) findComponentById(ResourceTable.Id_button_zhuce);
        button_set = (Button) findComponentById(ResourceTable.Id_button_set);
        button_mendian = (Button) findComponentById(ResourceTable.Id_button_mendian);
        button_shouye = (Button) findComponentById(ResourceTable.Id_button_shouye);
        button_mendianlog = (Button) findComponentById(ResourceTable.Id_button_mendianlog);
        button_xieyi = (Button) findComponentById(ResourceTable.Id_button_xieyi);
        button_shengming = (Button) findComponentById(ResourceTable.Id_button_shengming);

       denglu();//登录
       zhuce();//注册
       shezi();//设置
       mendain();
       shouye();
       mendianlog();
       xieyi();
       shengming();
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

    //跳转到店铺界面
    public void mendain(){
        // 为按钮设置点击回调
        button_mendian.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_mendian!=null){
                    present(new DianpuAbility(),new Intent());
                }
            }
        });
    }

    //跳转到订单界面
    public void shouye(){
        // 为按钮设置点击回调
        button_shouye.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shouye!=null){
                    present(new MainAbilitySlice(),new Intent());
                }
            }
        });
    }
    //跳转到订单界面
    public void mendianlog(){
        // 为按钮设置点击回调
        button_mendianlog.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_mendianlog!=null){
                    present(new DianpuAbility(),new Intent());
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



}
