package me.panavtec.title;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.CursorItem;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.object.TextStreamObject;
import com.ohos.trebleshot.object.TransferGroup;
import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.service.DeviceDiscoveryTask;
import com.ohos.trebleshot.ui.UIConnectionUtils;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.CommonEventHelper;
import com.ohos.trebleshot.utils.NetworkDeviceLoader;
import me.panavtec.dialog.ContentDialog;
import me.panavtec.qrcodescanner.decode.QrManager;
import me.panavtec.title.hmadapter.DeviceListPro;
import me.panavtec.title.hminterface.DeviceActionInterface;
import me.panavtec.title.hmutils.DBHelper;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.hmutils.TextUtils;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.rpc.RemoteException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static me.panavtec.title.hmutils.FileUtils.vp2px;
import static me.panavtec.title.slice.FileDetailsAbilitySlice.KEY_GROUNDID;
import static me.panavtec.title.slice.FileDetailsAbilitySlice.KEY_REQUESTID;

public class ConnectionManagerActivity extends BaseAbility implements Component.ClickedListener, DeviceActionInterface {

    public static final String ACTION_CHANGE_FRAGMENT = "com.genonbeta.intent.action.CONNECTION_MANAGER_CHANGE_FRAGMENT";
    public static final String EXTRA_FRAGMENT_ENUM = "extraFragmentEnum";
    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_CONNECTION_ADAPTER = "extraConnectionAdapter";
    public static final String EXTRA_REQUEST_TYPE = "extraRequestType";
    public static final String EXTRA_ACTIVITY_SUBTITLE = "extraActivitySubtitle";
    private CommonEventHelper.SubCommonEventListener mSubCommonEventListener;
    private CommonEventHelper.SubCommonEventListener mSubCommonEventListener1;

    public enum RequestType {
        RETURN_RESULT,
        MAKE_ACQUAINTANCE
    }

    private static final int toastShotTime = 2000;
    private String groundId;
    private ArrayList<String> requestIds;
    final List<TransferObject> transferObjects = new ArrayList<>();
    final List<NetworkDevice> networkDevices = new ArrayList<>();

    ListContainer listText1;
    DirectionalLayout emptyView1;
    Button btScan;
    NetworkDevice.Connection nowConection;
    DeviceListPro pro;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_connection_manager_activity);
        requestIds = intent.getStringArrayListParam(KEY_REQUESTID);
        groundId = intent.getStringParam(KEY_GROUNDID);
        initView();
        initData();
        initDeviceData();
        createScanObserver();
    }

    @Override
    protected void onActive() {
        super.onActive();
        registerDeviceScanObserver(true);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        registerDeviceScanObserver(false);
    }

    private void initData() {
        transferObjects.clear();
        if (requestIds == null || requestIds.size() == 0) return;
        for (String requestId : requestIds) {
            if (TextUtils.isEmpty(requestId)) continue;
            transferObjects.addAll(DBHelper.queryDataByRequestId(getContext(), String.valueOf(requestId)));
        }
    }

    private void initView() {
        findComponentById(ResourceTable.Id_ima_back).setClickedListener(this);
        findComponentById(ResourceTable.Id_ima_find).setClickedListener(this);
        findComponentById(ResourceTable.Id_connection_option_guide).setClickedListener(this);
        findComponentById(ResourceTable.Id_connection_option_hotspot).setClickedListener(this);
        findComponentById(ResourceTable.Id_connection_option_network).setClickedListener(this);
        findComponentById(ResourceTable.Id_connection_option_scan).setClickedListener(this);
        findComponentById(ResourceTable.Id_connection_option_manual_ip).setClickedListener(this);
        findComponentById(ResourceTable.Id_connection_option_devices).setClickedListener(this);
        findComponentById(ResourceTable.Id_bt_scan).setClickedListener(this);
        listText1 = (ListContainer) findComponentById(ResourceTable.Id_listText1);
        emptyView1 = (DirectionalLayout) findComponentById(ResourceTable.Id_emptyView1);
        btScan = (Button) findComponentById(ResourceTable.Id_bt_scan);
    }


    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_ima_back:
                terminateAbility();
                break;
            case ResourceTable.Id_ima_find:
                scanDevices();
                break;
            case ResourceTable.Id_connection_option_guide:
                showGuideDialog();
                break;
            case ResourceTable.Id_connection_option_hotspot: //建立热点
                showHotDialog();
                break;
            case ResourceTable.Id_connection_option_network:
                userOldNetDevice();
                break;
            case ResourceTable.Id_connection_option_scan:
                getCarmeraPermision();
                break;
            case ResourceTable.Id_connection_option_manual_ip:
                inputIpDialog();
                break;
            case ResourceTable.Id_connection_option_devices:
                jumpAbility(ManageDevicesActivity.class);
                break;
            case ResourceTable.Id_bt_scan:
                scanDevices();
                break;
        }
    }

    //使用已知的设备
    private void userOldNetDevice() {
        Intent intent = new Intent();
        Operation build = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName(IPDetailsAbility.class)
                .withBundleName(getBundleName())
                .build();
        intent.setOperation(build);
        startAbility(intent);
    }

    //扫描
    private void startScanQR() {
//        Toast.show(this, "one--------");
        QrManager.getInstance().startScan(this, new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                JSONObject object = new JSONObject(result);
                String ip = object.getString("ipAdr");
//                        show(getContext(), ip);
                startConnect(ip);
//                getUITaskDispatcher().asyncDispatch(new Runnable() {
//                    @Override
//                    public void run() {
////                                JSONObject object = new JSONObject();
//
//                    }
//                });
            }
        });
    }

    //输入ip
    private void inputIpDialog() {
        CommonDialog commonDialog = new CommonDialog(getContext());
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_input_dialog_layout, null, false);
        commonDialog.setContentCustomComponent(component);
        commonDialog.setTransparent(true);
        commonDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);

        TextField textField = (TextField) component.findComponentById(ResourceTable.Id_input_dialog_text_field);
        textField.setClickedListener(component1 -> textField.requestFocus());
        textField.setText("10.71.12.112");
        Button ok = (Button) component.findComponentById(ResourceTable.Id_input_dialog_ok2);
        ok.setClickedListener(component1 -> {
            startConnect(textField.getText());
            commonDialog.destroy();
        });
        Button cancle = (Button) component.findComponentById(ResourceTable.Id_input_dialog_cancle);
        cancle.setClickedListener(component1 -> {
            commonDialog.destroy();
        });
        commonDialog.setContentCustomComponent(component);
        if (!commonDialog.isShowing()) commonDialog.show();
    }

    /**
     * 返回连接结果给上一个界面
     *
     * @param deviceId    .
     * @param adapterName .
     */
    private void returnData(String deviceId, String adapterName) {
        Intent intent = new Intent();
        intent.setParam(ConnectionManagerActivity.EXTRA_DEVICE_ID, deviceId);
        intent.setParam(ConnectionManagerActivity.EXTRA_CONNECTION_ADAPTER, adapterName);
        setResult(0, intent);
        terminateAbility();
        onBackPressed();
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
                initDeviceData();
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

    private boolean checkIp(String ip) {
        boolean isCurrentIP = false;
        if (TextUtils.isEmpty(ip)) Toast.show(getContext(), "ip不能为空");
        if (!TextUtils.isEmpty(ip)) isCurrentIP = ip.matches("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
        return isCurrentIP;
    }


    String deviceId, nickname;

    private void startConnect(String ip) {
        if (!checkIp(ip)) return;
        UIConnectionUtils.setupConnection(this, ip, 11, new NetworkDeviceLoader.OnDeviceRegisteredErrorListener() {
            @Override
            public void onError(Exception error) {
                Toast.show(getContext(), "建立连接失败" + error.getMessage());
            }

            @Override
            public void onDeviceRegistered(AccessDatabase database, NetworkDevice device, NetworkDevice.Connection connection) {
                nowConection = connection;
                deviceId = device.deviceId;
                nickname = device.nickname;
                if (groundId==null||groundId.length()==0){
                    returnData(connection.deviceId, connection.adapterName);
                }else {
                    startSendFile(groundId,"","");
                }
//                Toast.show(getContext(), "连接成功" + groundId + "&&" + deviceId + "||" + nickname);
//                List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
//                for (CursorItem item : items0) {
//                    NetworkDevice.Connection connection1 = new NetworkDevice.Connection();
//                    connection1.reconstruct(item);
////            nowConection = connection;
//                    System.out.println(JSONObject.toJSONString(connection1));
//                }

//                if (TextUtils.isEmpty(groundId)) {
//                    returnData(deviceId, nickname);
//                } else {
//                    startSendFile(groundId, deviceId, nickname);
//                }
                initDeviceData();
                Toast.show(ConnectionManagerActivity.this, "刷新设备成功");
            }
        }, null);
    }


    private void startSendFile(String groundId, String devideId, String adapterName) {
/*//        Toast.show(getContext(), "开始发送" + groundId + "&&" + devideId + "||" + adapterName);
        AccessDatabase database = AppUtils.getDatabase(getContext());

        TransferGroup group = new TransferGroup(Long.parseLong(groundId));
//        group.groupId=1;
        group.isServedOnWeb = true;
        group.dateCreated = System.currentTimeMillis();
        AppUtils.getDatabase(getContext()).publish(group);
        System.out.println("show group");
        List<CursorItem> items2 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
        System.out.println("group---->" + items2.size());
        for (CursorItem item : items2) {
            TransferGroup group1 = new TransferGroup();
            group1.reconstruct(item);
            System.out.println(JSONObject.toJSONString(group1));
        }
        try {
            database.reconstruct(group);
            System.out.println("发送数据>>>>>>>>>>>>>>>>>>>>" + devideId + "==" + adapterName + "==" + group);
            UIConnectionUtils.sendFile(this, devideId, adapterName, group);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //                inputIpDialog();
        List<TransferObject> transferObjects = DBHelper.queryDataByGroundId(getContext(), groundId);

        for (TransferObject transferObject : transferObjects) {
            System.out.println("show tra" + transferObject.toString());
        }
//     {friendlyName='type2', file='/data/data/com.loser007/MainAbility/preferences/type2', fileMimeType='FOLDER', directory='/data/data/com.loser007/MainAbility/preferences/type2', deviceId='null', requestId=1619688833412, groupId=1, skippedBytes=0, fileSize=0, accessPort=58732, type=OUTGOING, flag=PENDING, mIsSelected=false}


        try {

            AccessDatabase database = AppUtils.getDatabase(getContext());
//                    web share 测试数据
            TransferGroup group = new TransferGroup();
            group.groupId = Long.parseLong(groundId);
            group.isServedOnWeb = true;
            group.dateCreated = System.currentTimeMillis();
//                    group.savePath = "/data/data/com.loser007/MainAbility/preferences";
            AppUtils.getDatabase(getContext()).insert(group);
            System.out.println("show group------------------------------>" + group.toString());

//                    TransferObject object = new TransferObject();
//                    object.groupId = 1;
//                    object.accessPort=58732;
//                    object.file ="/data/data/com.loser007/MainAbility/preferences/type1";
//                    object.fileMimeType =".jpg";
//                    object.fileSize=new File(object.file).length();
//                    object.friendlyName="tp1.jpg";
//                    object.requestId =1;
//                    object.type = TransferObject.Type.OUTGOING;
//                    object.flag= TransferObject.Flag.PENDING;
//                    AppUtils.getDatabase(getContext()).publish(object);
//                    System.out.println("show object-----------------------"+object.toString());

            final List<TransferObject> existingRegistry =
                    new ArrayList<>(AppUtils.getDatabase(getContext()).castQuery(
                            new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER)
                                    .setWhere(AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND "
                                                    + AccessDatabase.FIELD_TRANSFER_TYPE + "=?",
                                            String.valueOf(groundId),
                                            TransferObject.Type.OUTGOING.toString()), TransferObject.class));


            List<CursorItem> items2 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
            System.out.println("groupsize---->" + items2.size());
            for (TransferObject item : existingRegistry) {
//                        TransferGroup group1 = new TransferGroup();
//                        group1.reconstruct(item);
                System.out.println("List------------------------------" + item.toString());
            }
            List<CursorItem> items3 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER));
            System.out.println("objectsize---->" + items3.size());
            List<CursorItem> items4 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_CLIPBOARD));
            for (CursorItem item : items4) {
                TextStreamObject object1 = new TextStreamObject();
                object1.reconstruct(item);
                System.out.println(object1.text);
            }
//                    TransferObject object = new TransferObject();
//                    object.groupId = 1;
//                    object.accessPort = 58732;
//                    object.directory = "/data/data/com.loser007/files";
////                    object.file ="/data/data/com.loser007/files/tp1.jpg";
//                    object.fileMimeType = AccessDatabase.FIELD_TRANSFER_FILE;
//                    object.fileSize = new File(object.directory).length();
//                    object.friendlyName = "files";
//                    object.requestId = 1;
//                    object.type = TransferObject.Type.OUTGOING;
//                    object.flag = TransferObject.Flag.PENDING;
//                    AppUtils.getDatabase(getContext()).publish(object);
//                    TransferGroup group = new TransferGroup();
//                    group.groupId=1;
//                    group.isServedOnWeb=true;
//                    group.dateCreated = System.currentTimeMillis();
//                    group.savePath = "/data/data/com.loser007/MainAbility/preferences/type1";
//                    database.insert(group);

//                    UIConnectionUtils.sendMsg(ConnectionManagerActivity.this,nowConection.deviceId,nowConection.adapterName,"thank you!");
            UIConnectionUtils.sendFile(ConnectionManagerActivity.this, nowConection.deviceId, nowConection.adapterName, group);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
//                startSendFile(groundId, nowConection.deviceId, nowConection.adapterName);
    }


    private void show(Context context, String text) {
        DirectionalLayout directionalLayout = (DirectionalLayout) LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_picker, null, false);
        Text result = (Text) directionalLayout.findComponentById(ResourceTable.Id_tv_result);
        Text confirm = (Text) directionalLayout.findComponentById(ResourceTable.Id_tv_confirm);
        CommonDialog dialog = new CommonDialog(context);
        dialog.setContentCustomComponent(directionalLayout);
        dialog.setAutoClosable(true);
        dialog.setAutoClosable(true);
        dialog.setSize(vp2px(this, 340), DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        dialog.setAlignment(LayoutAlignment.CENTER);
        dialog.setCornerRadius(vp2px(this, 15));
        dialog.show();
        result.setText(text);
        confirm.setClickedListener(v -> {
            dialog.destroy();
        });
    }


    //你在另一个设备上刚配置好了吗
    private void showGuideDialog() {
        try {
            ContentDialog guideDialog = new ContentDialog(getContext(), component -> showQRDialog(), component -> showHotDialog()).setContent(getResourceManager().getElement(ResourceTable.String_ques_connectionWizardIsOtherDeviceReady).getString());
            guideDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //在其屏幕上你看到了二维码吗
    private void showQRDialog() {
        try {
            ContentDialog guideDialog1 = new ContentDialog(getContext(), component -> startScanQR()
                    , component -> showHotDialog())
                    .setContent(getResourceManager().getElement(ResourceTable.String_ques_connectionWizardIsThereQRCode).getString());
            guideDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo
    //你想要其他设备通过热点连接至该设备吗？如果你不知道热点是什么，轻触“是”。
    private void showHotDialog() { }
    private void showHotDialog_() {
        try {
            ContentDialog guideDialog1 = new ContentDialog(getContext()
                    ,component -> HMUtils.startAbility(ConnectionManagerActivity.this, HotspotFractionAbility.class.getName(), "TF")
                    ,component -> showOtherWifiDialog()).setContent(getResourceManager().getElement(ResourceTable.String_ques_connectionWizardUseHotspot).getString());
            guideDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //你想要使用你和你的朋友连接到的路由器或者其他Wi-Fi网络吗？
    private void showOtherWifiDialog() {
        try {
            ContentDialog guideDialog1 = new ContentDialog(getContext(), component -> userOldNetDevice(), component -> showOldDeviceWifiDialog()).setContent(getResourceManager().getElement(ResourceTable.String_ques_connectionWizardUseNetwork).getString());
            guideDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //你想要访问你之前连接的设备吗
    private void showOldDeviceWifiDialog() {
        try {
            ContentDialog guideDialog1 = new ContentDialog(getContext()
                    , component -> showQRDialog(),
                    component -> showGuideDialog())
                    .setContent(getResourceManager().getElement(ResourceTable.String_ques_connectionWizardUseKnownDevices).getString())
                    .setTeNo("重试");
            guideDialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initDeviceData() {
        networkDevices.clear();
        AccessDatabase database = AppUtils.getDatabase(getContext());
        List<CursorItem> items = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICES));
        for (CursorItem item : items) {
            NetworkDevice device = new NetworkDevice();
            device.reconstruct(item);
            networkDevices.add(device);
            System.out.println(device.toString());
        }
        List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
        for (CursorItem item : items0) {
            NetworkDevice.Connection connection = new NetworkDevice.Connection();
            connection.reconstruct(item);
//            nowConection = connection;
            System.out.println(connection.toString());
        }
        if (networkDevices.size() > 0) {
            listText1.setVisibility(Component.VISIBLE);
            emptyView1.setVisibility(Component.HIDE);
            btScan.setVisibility(Component.HIDE);
        } else {
            listText1.setVisibility(Component.HIDE);
            emptyView1.setVisibility(Component.VISIBLE);
            btScan.setVisibility(Component.VISIBLE);
        }
        pro = new DeviceListPro(this, networkDevices, this);
        listText1.setItemProvider(pro);
        listText1.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                itemClickDo(networkDevices.get(i));
/*//                inputIpDialog();
                List<TransferObject> transferObjects = DBHelper.queryDataByGroundId(getContext(), groundId);

                for (TransferObject transferObject : transferObjects) {
                    System.out.println("show tra" + transferObject.toString());
                }
//     {friendlyName='type2', file='/data/data/com.loser007/MainAbility/preferences/type2', fileMimeType='FOLDER', directory='/data/data/com.loser007/MainAbility/preferences/type2', deviceId='null', requestId=1619688833412, groupId=1, skippedBytes=0, fileSize=0, accessPort=58732, type=OUTGOING, flag=PENDING, mIsSelected=false}


                try {

                    AccessDatabase database = AppUtils.getDatabase(getContext());
//                    web share 测试数据
                    TransferGroup group = new TransferGroup();
                    group.groupId = Long.parseLong(groundId);
                    group.isServedOnWeb = true;
                    group.dateCreated = System.currentTimeMillis();
//                    group.savePath = "/data/data/com.loser007/MainAbility/preferences";
                    AppUtils.getDatabase(getContext()).insert(group);
                    System.out.println("show group------------------------------>" + group.toString());

//                    TransferObject object = new TransferObject();
//                    object.groupId = 1;
//                    object.accessPort=58732;
//                    object.file ="/data/data/com.loser007/MainAbility/preferences/type1";
//                    object.fileMimeType =".jpg";
//                    object.fileSize=new File(object.file).length();
//                    object.friendlyName="tp1.jpg";
//                    object.requestId =1;
//                    object.type = TransferObject.Type.OUTGOING;
//                    object.flag= TransferObject.Flag.PENDING;
//                    AppUtils.getDatabase(getContext()).publish(object);
//                    System.out.println("show object-----------------------"+object.toString());

                    final List<TransferObject> existingRegistry =
                            new ArrayList<>(AppUtils.getDatabase(getContext()).castQuery(
                                    new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER)
                                            .setWhere(AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND "
                                                            + AccessDatabase.FIELD_TRANSFER_TYPE + "=?",
                                                    String.valueOf(groundId),
                                                    TransferObject.Type.OUTGOING.toString()), TransferObject.class));


                    List<CursorItem> items2 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
                    System.out.println("groupsize---->" + items2.size());
                    for (TransferObject item : existingRegistry) {
//                        TransferGroup group1 = new TransferGroup();
//                        group1.reconstruct(item);
                        System.out.println("List------------------------------" + item.toString());
                    }
                    List<CursorItem> items3 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER));
                    System.out.println("objectsize---->" + items3.size());
                    List<CursorItem> items4 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_CLIPBOARD));
                    for (CursorItem item : items4) {
                        TextStreamObject object1 = new TextStreamObject();
                        object1.reconstruct(item);
                        System.out.println(object1.text);
                    }
//                    TransferObject object = new TransferObject();
//                    object.groupId = 1;
//                    object.accessPort = 58732;
//                    object.directory = "/data/data/com.loser007/files";
////                    object.file ="/data/data/com.loser007/files/tp1.jpg";
//                    object.fileMimeType = AccessDatabase.FIELD_TRANSFER_FILE;
//                    object.fileSize = new File(object.directory).length();
//                    object.friendlyName = "files";
//                    object.requestId = 1;
//                    object.type = TransferObject.Type.OUTGOING;
//                    object.flag = TransferObject.Flag.PENDING;
//                    AppUtils.getDatabase(getContext()).publish(object);
//                    TransferGroup group = new TransferGroup();
//                    group.groupId=1;
//                    group.isServedOnWeb=true;
//                    group.dateCreated = System.currentTimeMillis();
//                    group.savePath = "/data/data/com.loser007/MainAbility/preferences/type1";
//                    database.insert(group);

//                    UIConnectionUtils.sendMsg(ConnectionManagerActivity.this,nowConection.deviceId,nowConection.adapterName,"thank you!");
                    UIConnectionUtils.sendFile(ConnectionManagerActivity.this, nowConection.deviceId, nowConection.adapterName, group);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
//                startSendFile(groundId, nowConection.deviceId, nowConection.adapterName);*/
            }
        });
    }


    @Override
    public void update(NetworkDevice device) {
        startConnect(getIPByDeviceID(device));
    }

    @Override
    public void remove(NetworkDevice device) {
        AccessDatabase database = AppUtils.getDatabase(getContext());
        database.remove(device);
        initDeviceData();

    }

    @Override
    public void close(NetworkDevice device) {

    }

    public void getCarmeraPermision() {
        //判断是否有相机权限
        if (verifySelfPermission("ohos.permission.CAMERA") != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限，判断是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
            if (canRequestPermission("ohos.permission.CAMERA")) {
                //申请相机权限弹框
                requestPermissionsFromUser(new String[]{"ohos.permission.CAMERA"}, 1);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
            }
        } else {
            // 权限已被授予
            startScanQR();
//            Toast.show(this, "已经授权");
        }
    }

    public void doClick(NetworkDevice device) {
        AccessDatabase database = AppUtils.getDatabase(getContext());
//        database.r
        List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
        for (CursorItem item : items0) {
            NetworkDevice.Connection connection = new NetworkDevice.Connection();
            connection.reconstruct(item);
            if (device.deviceId.equals(connection.deviceId)) {
                startConnect(connection.ipAddress);
            }
            break;
//            nowConection = connection;
//            System.out.println(JSONObject.toJSONString(connection));
        }
    }

    public String getIPByDeviceID(NetworkDevice networkDevice) {
        AccessDatabase database = AppUtils.getDatabase(getContext());
        List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
        for (CursorItem item : items0) {
            NetworkDevice.Connection connection = new NetworkDevice.Connection();
            connection.reconstruct(item);
            if (networkDevice.deviceId.equals(connection.deviceId)) {
                return connection.ipAddress;
            }
        }
        return "";
    }

    public void itemClickDo(NetworkDevice networkDevice){
        List<NetworkDevice.Connection> c = AppUtils.getDatabase(getContext()).castQuery(
                new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION)
                        .setWhere(AccessDatabase.FIELD_DEVICECONNECTION_DEVICEID + "= ?"
                                ,networkDevice.deviceId), NetworkDevice.Connection.class);
        System.out.println("-------------------------------+++++++++++++++"+c.size());
        if (c!=null&&c.size()>0&&c.get(0).ipAddress.length()>0){
            startConnect(c.get(0).ipAddress);
            System.out.println(c.get(0).toString());
        }else{
            Toast.show(ConnectionManagerActivity.this,"没有查询到连接，请输入ip连接");
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    // 权限被授予
                    // 注意：因时间差导致接口权限检查时有无权限，所以对那些因无权限而抛异常的接口进行异常捕获处理
                        startScanQR();
                } else {
                    // 权限被拒绝
                    Toast.show(this,"请开通相机权限");
                }
                break;
        }
    }
}
