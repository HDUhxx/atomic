package com.example.myapplication_tongqu.guangbo;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.shop.MianmoAbility;
import com.example.myapplication_tongqu.slice.LoginAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class GuangboAbility extends AbilitySlice {
    private Button button_fabu;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_guangbo);
        button_fabu = (Button) findComponentById(ResourceTable.Id_button_fabu);

        fabu();
    }

    //跳转到登录详情界面
    public void fabu(){
        // 为按钮设置点击回调
        button_fabu.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_fabu!=null){
                    present(new LoginAbility(),new Intent());
                }
            }
        });
    }
}
