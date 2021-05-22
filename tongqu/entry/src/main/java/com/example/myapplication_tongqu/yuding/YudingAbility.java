package com.example.myapplication_tongqu.yuding;

import com.example.myapplication_tongqu.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class YudingAbility extends AbilitySlice {
    private Button button_tijiao;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_yuding);
        button_tijiao = (Button) findComponentById(ResourceTable.Id_button_tijiao);

        tijiao();
    }

    //跳转到订单界面
    public void tijiao(){
        // 为按钮设置点击回调
        button_tijiao.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_tijiao!=null){
                    present(new chenggongAbility(),new Intent());
                }
            }
        });
    }
}
