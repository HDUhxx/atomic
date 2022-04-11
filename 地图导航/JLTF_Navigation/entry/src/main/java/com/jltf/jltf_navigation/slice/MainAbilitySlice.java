package com.jltf.jltf_navigation.slice;

import com.jltf.jltf_navigation.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

public class MainAbilitySlice extends AbilitySlice {

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP,0x0,"地图导航");
    String address="北京市";

    Uri uri;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);


        findComponentById(ResourceTable.Id_Nabtn).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

                TextField textField = findComponentById(ResourceTable.Id_text);

                address=textField.getText();

                HiLog.info(TAG,"输入地址："+address);

                if (isAppExist("com.tencent.map")){

                    // 腾讯地图
                    uri=Uri.parse( "qqmap://map/search?keyword=" + address);

                }else if (isAppExist("com.autonavi.minimap")){

                    // 高德地图
                    uri=Uri.parse("androidamap://keywordNavi?sourceApplication="+getBundleName()+"&keyword="+address+"&style=2");

                }else if (isAppExist("com.baidu.BaiduMap")){
              
                    uri=Uri.parse("baidumap://map/geocoder?src="+getBundleName()+"&address="+address); // 百度地图

                }

                Intent intent1=new Intent();
                intent1.setUri(uri);
                startAbility(intent1);
            }
        });
    }

    boolean isAppExist(String appPkg) {

        try {

            IBundleManager manager = this.getBundleManager();

            return manager.isApplicationEnabled(appPkg);

        } catch (IllegalArgumentException e) {

            return false;

        }

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
