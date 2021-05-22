package com.example.myapplication_tongqu.guangbo;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.my.MyAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class FabuAbility extends AbilitySlice {
    private Button button_fabu;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_fabu_page);
        button_fabu = (Button) findComponentById(ResourceTable.Id_button_fabu);

        fabu();
    }

    //跳转到我的界面
    public void fabu(){
        // 为按钮设置点击回调
        button_fabu.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_fabu!=null){
                    present(new FabuyesAbility(),new Intent());
                }
            }
        });
    }
}
