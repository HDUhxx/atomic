package me.panavtec.title.hmadapter;

import com.ohos.trebleshot.object.NetworkDevice;
import me.panavtec.dialog.DeviceSetDialog;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.DeviceActionInterface;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class DeviceListPro extends BaseItemProvider {

    final Context context;
    final List<NetworkDevice> devices = new ArrayList<>();
    final DeviceActionInterface actionInterface;
    public DeviceListPro(Context context,List<NetworkDevice> devices,DeviceActionInterface actionInterface) {
        this.context = context;
        this.devices.addAll(devices);
        this.actionInterface = actionInterface;
    }
    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_device, null, false);
        } else {
            cpt = component;
        }
        Text nickName = (Text) cpt.findComponentById(ResourceTable.Id_nick_name);
        Text deviceName = (Text) cpt.findComponentById(ResourceTable.Id_device_name);
        Image imageToast = (Image) cpt.findComponentById(ResourceTable.Id_image_toast);
        Button firstChar = (Button) cpt.findComponentById(ResourceTable.Id_first_char);

        NetworkDevice device = devices.get(i);
        firstChar.setText(device.nickname.charAt(0));
        nickName.setText(device.nickname);
        deviceName.setText(device.model);
        imageToast.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showMessage(device);
            }
        });
        return cpt;
    }
    public void showMessage(NetworkDevice dev){
        DeviceSetDialog dialog = new DeviceSetDialog(context,dev);
        dialog.setActionInterface(actionInterface);
        dialog.show();
    }
}
