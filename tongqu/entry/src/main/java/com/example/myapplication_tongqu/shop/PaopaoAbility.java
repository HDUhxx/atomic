package com.example.myapplication_tongqu.shop;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.kefu.KefuAbility;
import com.example.myapplication_tongqu.yuding.YudingAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class PaopaoAbility extends AbilitySlice {
    private Button button_yuding2,button_kefu2;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_shangpin_two);
        button_yuding2 = (Button) findComponentById(ResourceTable.Id_button_yuding2);
        button_kefu2 = (Button) findComponentById(ResourceTable.Id_button_kefu2);

        yuding();
        kefu();
    }
    //跳转到订单界面
    public void yuding(){
        // 为按钮设置点击回调
        button_yuding2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_yuding2!=null){
                    present(new YudingAbility(),new Intent());
                }
            }
        });
    }
    public void kefu(){
        // 为按钮设置点击回调
        button_kefu2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_kefu2!=null){
                    present(new KefuAbility(),new Intent());
                }
            }
        });
    }
}
