package com.example.myapplication_huibianli.shop;

import com.example.myapplication_huibianli.ResourceTable;
import com.example.myapplication_huibianli.kefu.KefuAbility;
import com.example.myapplication_huibianli.slice.LoginAbility;
import com.example.myapplication_huibianli.slice.MainAbilitySlice;
import com.example.myapplication_huibianli.yuding.YudingAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class CaomeiAbility extends AbilitySlice {
    private Button button_yuding1,button_kefu1;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_shangpin_one);
        button_yuding1 = (Button) findComponentById(ResourceTable.Id_button_yuding1);
        button_kefu1 = (Button) findComponentById(ResourceTable.Id_button_kefu1);

        yuding();
        kefu();
    }

    //跳转到订单界面
    public void yuding(){
        // 为按钮设置点击回调
        button_yuding1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_yuding1!=null){
                    present(new LoginAbility(),new Intent());
                }
            }
        });
    }
    //跳转到订单界面
    public void kefu(){
        // 为按钮设置点击回调
        button_kefu1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_kefu1!=null){
                    present(new KefuAbility(),new Intent());
                }
            }
        });
    }
}
