package com.example.jltflianxi.slice;

import com.example.jltflianxi.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;


public class jltfshipinwodeSlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_jltf_shipinwode);

        Text text1 = (Text) findComponentById(ResourceTable.Id_jltf_wodepaishe);
        text1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
               /* present(new jltfshiliaoSlice(),new Intent());*/
            }
        });
        Text text2 = (Text) findComponentById(ResourceTable.Id_jltf_wodeshangchuan);
        text2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfshangchuanSlice(),new Intent());
            }
        });
        Text text3 = (Text) findComponentById(ResourceTable.Id_jltf_wodeyunjian);
        text3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new MainAbilitySlice(),new Intent());
            }
        });
        Text text4 = (Text) findComponentById(ResourceTable.Id_jltf_wodehaoyou);
        text4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfhaoyouSlice(),new Intent());
            }
        });
        Text text5 = (Text) findComponentById(ResourceTable.Id_jltf_yonghuxy);
        text5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
             present(new jltfxieyiSlice(),new Intent());
            }
        });
        Text text6 = (Text) findComponentById(ResourceTable.Id_jltf_yinsism);
        text6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
               present(new jltfxieyiSlice(),new Intent());
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
