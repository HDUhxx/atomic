package me.panavtec.title.slice;

import com.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import com.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import com.ohos.trebleshot.utils.AddressedInterface;
import com.ohos.trebleshot.utils.NetworkUtils;
import me.panavtec.config.AppConfig;
import me.panavtec.title.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;
import ohos.wifi.WifiDevice;

import java.util.List;

public class IPDetailsAbilitySlice extends AbilitySlice {

    Image img_back,ima_update,layout_network_manager_qr_image;
    Button layout_network_manager_info_toggle_button;
    Text layout_network_manager_info_container_text3,layout_network_manager_info_container_text2,layout_network_manager_info_container_text1;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_layout_network_manager);
        initView();
    }

    public void initView(){
        img_back = (Image) findComponentById(ResourceTable.Id_img_back);
        ima_update = (Image) findComponentById(ResourceTable.Id_ima_update);
        layout_network_manager_qr_image = (Image) findComponentById(ResourceTable.Id_layout_network_manager_qr_image);
        layout_network_manager_info_container_text1 = (Text) findComponentById(ResourceTable.Id_layout_network_manager_info_container_text1);
        layout_network_manager_info_container_text2 = (Text) findComponentById(ResourceTable.Id_layout_network_manager_info_container_text2);
        layout_network_manager_info_container_text3 = (Text) findComponentById(ResourceTable.Id_layout_network_manager_info_container_text3);
        layout_network_manager_info_toggle_button = (Button) findComponentById(ResourceTable.Id_layout_network_manager_info_toggle_button);
        try {

            new EventHandler(EventRunner.create("QRCodeGen")).postTask(() -> {
                PixelMap pixelMap =  QRCodeEncoder.syncEncodeQRCode(getIpAddress(), BGAQRCodeUtil.dp2px(this, 160));
                getUITaskDispatcher().asyncDispatch(() -> {
                    layout_network_manager_qr_image.setPixelMap(pixelMap);
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        img_back.setClickedListener(component -> terminateAbility());
        ima_update.setClickedListener(component -> showInfo());
        showInfo();

    }

    public void showInfo(){
        WifiDevice device = WifiDevice.getInstance(this);
        if (device.isConnected()) {
            layout_network_manager_info_container_text1.setText("如果两个设备在同一网络时扫描它");
            layout_network_manager_info_container_text2.setVisibility(Component.VISIBLE);
            layout_network_manager_info_container_text3.setVisibility(Component.VISIBLE);
            layout_network_manager_info_container_text2.setText(device.isConnected() ? device.getLinkedInfo().get().getSsid() : "wifi未连接");
            layout_network_manager_info_container_text3.setText(getIpAddress());
        }else{
            layout_network_manager_info_container_text1.setText("连接至WI-FI以继续");
            layout_network_manager_info_container_text2.setVisibility(Component.HIDE);
            layout_network_manager_info_container_text3.setVisibility(Component.HIDE);
        }
        layout_network_manager_info_toggle_button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                intent.setAction("android.settings.WIFI_SETTINGS");
                intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                startAbility(intent,0);
            }
        });
    }


   public String getIpAddress() {
       List<AddressedInterface> interfaceList = NetworkUtils.getInterfaces(true, AppConfig.DEFAULT_DISABLED_INTERFACES);

       if (interfaceList.size()>0){
           return interfaceList.get(0).getAssociatedAddress();
       }
//       for (AddressedInterface addressedInterface : interfaceList) {
//           WebShareACListProvider.ACInfo acInfo = new WebShareACListProvider.ACInfo(addressedInterface.getNetworkInterface().getName(),
//                   addressedInterface.getAssociatedAddress());
//       }
       return "";
   }
}
