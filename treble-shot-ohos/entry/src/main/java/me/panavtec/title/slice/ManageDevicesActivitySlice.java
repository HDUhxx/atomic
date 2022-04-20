package me.panavtec.title.slice;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.CursorItem;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.object.TransferGroup;
import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.service.DeviceDiscoveryTask;
import com.ohos.trebleshot.ui.UIConnectionUtils;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.CommonEventHelper;
import com.ohos.trebleshot.utils.NetworkDeviceLoader;
import me.panavtec.title.ConnectionManagerActivity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.DeviceListPro;
import me.panavtec.title.hminterface.DeviceActionInterface;
import me.panavtec.title.hmutils.TextUtils;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.rpc.RemoteException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManageDevicesActivitySlice extends AbilitySlice implements Component.ClickedListener, DeviceActionInterface {

    TextField tf_view;
    DirectionalLayout dlayout_front,dlayout_next;
    ArrayList<String> data;
    ListContainer devices_list;
    DeviceListPro pro;
    final List<NetworkDevice> networkDevices = new ArrayList<>();
    DirectionalLayout text_empt;
    Button but_scan;
    private static final int toastShotTime = 2000;
    NetworkDevice.Connection nowConection;
    private CommonEventHelper.SubCommonEventListener mSubCommonEventListener;
    private CommonEventHelper.SubCommonEventListener mSubCommonEventListener1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_activity_manage_devices);
        data = intent.getStringArrayListParam("dataList");
        initView();
        loadDevice();
        if (data!=null&&data.size()>0) {
            Toast.show(this, data.size()+"++++++++++++"+data.get(0));
            Iterator<String> iterator=data.iterator();
            while(iterator.hasNext()){
                System.out.println("-----------"+iterator.next());
            }
        }
        createScanObserver();
    }

    @Override
    public void onActive() {
        super.onActive();
        registerDeviceScanObserver(true);
        loadDevice();
        if (networkDevices.size()>0){
            devices_list.setVisibility(Component.VISIBLE);
            text_empt.setVisibility(Component.HIDE);
            but_scan.setVisibility(Component.HIDE);
            pro = new DeviceListPro(this,networkDevices,this);
            devices_list.setItemProvider(pro);
        }else {
            devices_list.setVisibility(Component.HIDE);
            text_empt.setVisibility(Component.VISIBLE);
            but_scan.setVisibility(Component.VISIBLE);
        }
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void initView(){
        findComponentById(ResourceTable.Id_fixConnectionButton).setClickedListener(this);
//        findComponentById(ResourceTable.Id_more_main).setClickedListener(this);
        findComponentById(ResourceTable.Id_more_main).setVisibility(Component.HIDE);
        findComponentById(ResourceTable.Id_search_icon1).setClickedListener(this);
        findComponentById(ResourceTable.Id_search_icon2).setClickedListener(this);
        findComponentById(ResourceTable.Id_tf_view).setClickedListener(this);
        findComponentById(ResourceTable.Id_but_scan).setClickedListener(this);
        findComponentById(ResourceTable.Id_back_header).setClickedListener(this);
        findComponentById(ResourceTable.Id_more_main).setClickedListener(this);
        findComponentById(ResourceTable.Id_image_close).setClickedListener(this);
        dlayout_front = (DirectionalLayout) findComponentById(ResourceTable.Id_dlayout_front);
        dlayout_next = (DirectionalLayout) findComponentById(ResourceTable.Id_dlayout_next);
        devices_list = (ListContainer) findComponentById(ResourceTable.Id_devices_list);
        text_empt = (DirectionalLayout) findComponentById(ResourceTable.Id_text_empt);
        but_scan = (Button) findComponentById(ResourceTable.Id_but_scan);
        if (networkDevices.size()>0){
            devices_list.setVisibility(Component.VISIBLE);
            text_empt.setVisibility(Component.HIDE);
            but_scan.setVisibility(Component.HIDE);
        }else {
            devices_list.setVisibility(Component.HIDE);
            text_empt.setVisibility(Component.VISIBLE);
            but_scan.setVisibility(Component.VISIBLE);
        }
        pro = new DeviceListPro(this,networkDevices,this);
        devices_list.setItemProvider(pro);
        }

    @Override
    public void onClick(Component component) {
        switch (component.getId()){
            case ResourceTable.Id_fixConnectionButton:
                Intent intent = new Intent();
                Operation build = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withAbilityName(ConnectionManagerActivity.class)
                        .withBundleName(getBundleName())
                        .build();
                intent.setOperation(build);
                startAbility(intent);
                break;
            case ResourceTable.Id_search_icon1:
                scanDevices();
                break;
            case ResourceTable.Id_search_icon2:
                scanDevices();
                break;
            case ResourceTable.Id_tf_view:

                break;
            case ResourceTable.Id_but_scan:
                scanDevices();
                break;
            case ResourceTable.Id_back_header:
                onBackPressed();
                break;
            case ResourceTable.Id_more_main:
                dlayout_front.setVisibility(Component.HIDE);
                dlayout_next.setVisibility(Component.VISIBLE);
                break;
            case ResourceTable.Id_image_close:
                dlayout_front.setVisibility(Component.VISIBLE);
                dlayout_next.setVisibility(Component.HIDE);
                break;
        }
    }

    private void createScanObserver(){
        mSubCommonEventListener  = new CommonEventHelper.SubCommonEventListener() {
            @Override
            public void onReceiveEvent(CommonEventData commonEventData) {
                boolean isScanning = commonEventData.getIntent()
                        .getBooleanParam(DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS, false);
                if (!isScanning) {
                    new ToastDialog(getApplicationContext()).setDuration(toastShotTime)
                            .setText(getApplicationContext().getString(ResourceTable.String_mesg_scanningDevices))
                            .show();
                    scanDevicesStart();
                } else {
                    new ToastDialog(getApplicationContext()).setDuration(toastShotTime)
                            .setText(getApplicationContext().getString(ResourceTable.String_mesg_stillScanning))
                            .show();
                }
            }
        };

        mSubCommonEventListener1 = new CommonEventHelper.SubCommonEventListener() {
            @Override
            public void onReceiveEvent(CommonEventData commonEventData) {
                loadDevice();
                new ToastDialog(getApplicationContext()).setDuration(toastShotTime)
                        .setText(getApplicationContext().getString(ResourceTable.String_mesg_scanCompleted))
                        .show();
            }
        };
    }

    private void registerDeviceScanObserver(boolean enable){
        if(enable) {
            CommonEventHelper.register(DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS, mSubCommonEventListener);
            CommonEventHelper.register(DeviceDiscoveryTask.EVENT_DISCOVERY_END, mSubCommonEventListener1);
        }else {
            try {
                CommonEventManager.unsubscribeCommonEvent(mSubCommonEventListener.getOwner());
                CommonEventManager.unsubscribeCommonEvent(mSubCommonEventListener1.getOwner());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        registerDeviceScanObserver(false);
    }


    private void scanDevices() {
        queryScanDevicesStatus();
    }

    private void queryScanDevicesStatus() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(getBundleName())
                .withAbilityName(CommunicationService.class)
                .withAction(CommunicationService.ACTION_DEVICE_DISCOVERY_STATUS)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    private void scanDevicesStart(){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(getBundleName())
                .withAbilityName(CommunicationService.class)
                .withAction(CommunicationService.ACTION_DEVICE_DISCOVERY)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    public void loadDevice(){
        networkDevices.clear();
        System.out.println("show device:");
        AccessDatabase database = AppUtils.getDatabase(getContext());
                List<CursorItem> items = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICES));
                for(CursorItem item:items){
                    NetworkDevice device = new NetworkDevice();
                    device.reconstruct(item);
                    if((!device.isLocalAddress) || AppUtils.getPreferences(getContext()).getBoolean("developer_mode", false)) {
                        networkDevices.add(device);
                    }
                    System.out.println(device.toString());
                }
                System.out.println("show connection:");
                List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
                for(CursorItem item:items0){
                    NetworkDevice.Connection connection = new NetworkDevice.Connection();
                    connection.reconstruct(item);
//                    nowConection = connection;
                    System.out.println(connection.toString());
                }
                System.out.println("show group");
                List<CursorItem> items2 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
                System.out.println("group---->"+items2.size());
                for(CursorItem item:items2){
                    TransferGroup group = new TransferGroup();
                    group.reconstruct(item);
                    System.out.println(group.toString());
                }
                List<CursorItem> items3 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER));
                System.out.println("object---->"+items3.size());
        if (networkDevices.size()>0){
            devices_list.setVisibility(Component.VISIBLE);
            text_empt.setVisibility(Component.HIDE);
            but_scan.setVisibility(Component.HIDE);
        }else {
            devices_list.setVisibility(Component.HIDE);
            text_empt.setVisibility(Component.VISIBLE);
            but_scan.setVisibility(Component.VISIBLE);
        }
        pro = new DeviceListPro(this,networkDevices,this);
        devices_list.setItemProvider(pro);
    }

    @Override
    public void update(NetworkDevice device) {
        startConnect(getIPByDeviceID(device));
    }

    @Override
    public void remove(NetworkDevice device) {
        AccessDatabase database = AppUtils.getDatabase(getContext());
        database.remove(device);
        loadDevice();
    }

    @Override
    public void close(NetworkDevice device) {

    }
    public String getIPByDeviceID(NetworkDevice networkDevice){
        AccessDatabase database = AppUtils.getDatabase(getContext());
        List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
        for(CursorItem item:items0){
            NetworkDevice.Connection connection = new NetworkDevice.Connection();
            connection.reconstruct(item);
            if (networkDevice.deviceId.equals(connection.deviceId)) {
                return connection.ipAddress;
            }
        }
        return "";
    }
    private void startConnect(String ip) {
        if (!checkIp(ip)) return;
        UIConnectionUtils.setupConnection(getAbility(), ip, 11, new NetworkDeviceLoader.OnDeviceRegisteredErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.show(getContext(), "建立连接失败" + error.getMessage());
            }

            @Override
            public void onDeviceRegistered(AccessDatabase database, NetworkDevice device, NetworkDevice.Connection connection) {
                nowConection = connection;
                String deviceId = device.deviceId;
                String nickname = device.nickname;
                loadDevice();
                Toast.show(ManageDevicesActivitySlice.this,"刷新设备成功");
//                returnData(deviceId, nickname);
//                startSendFile(groundId, deviceId, nickname);
            }
        }, null);
    }
    private boolean checkIp(String ip) {
        boolean isCurrentIP = false;
        if (TextUtils.isEmpty(ip)) Toast.show(getContext(), "ip不能为空");
        if (!TextUtils.isEmpty(ip)) isCurrentIP = ip.matches("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
        return isCurrentIP;
    }
}
