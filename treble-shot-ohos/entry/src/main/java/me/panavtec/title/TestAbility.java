package me.panavtec.title;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.CursorItem;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.object.TransferGroup;
import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.service.DeviceDiscoveryTask;
import com.ohos.trebleshot.ui.UIConnectionUtils;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.CommonEventHelper;
import com.ohos.trebleshot.utils.NetworkDeviceLoader;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.event.commonevent.CommonEventData;

import java.io.File;
import java.util.List;

public class TestAbility extends Ability {
    TextField textField;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_test);
        this.textField = (TextField) findComponentById(ResourceTable.Id_recc_data);
        CommonEventHelper.register(DeviceDiscoveryTask.EVENT_DISCOVERY_END, new CommonEventHelper.OnCommonEventListener() {
            @Override
            public void onReceiveEvent(CommonEventData commonEventData) {
                System.out.println("----------EEEE");
            }
        });

        CommonEventHelper.register(DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS, new CommonEventHelper.OnCommonEventListener() {
            @Override
            public void onReceiveEvent(CommonEventData commonEventData) {
                System.out.println("-----------"+commonEventData.getIntent()
                        .getBooleanParam(DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS,false));
            }
        });

        findComponentById(ResourceTable.Id_button).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {


                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName(getBundleName())
                        .withAbilityName(CommunicationService.class)
                        .withAction(CommunicationService.ACTION_DEVICE_DISCOVERY_STATUS)
                        .build();
                intent.setOperation(operation);
                startAbility(intent);
            }
        });

        findComponentById(ResourceTable.Id_addData).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                AccessDatabase database = AppUtils.getDatabase(getContext());
                //web share 测试数据
                TransferGroup group = new TransferGroup();
                group.groupId=1;
                group.isServedOnWeb=true;
                group.dateCreated = System.currentTimeMillis();
                AppUtils.getDatabase(getContext()).publish(group);

                TransferObject object = new TransferObject();
                object.groupId = 1;
                object.accessPort=58732;
                object.file ="/data/data/com.loser007/files/tp1.jpg";
                object.fileMimeType =".jpg";
                object.fileSize=new File(object.file).length();
                object.friendlyName="IMG_20210404_113308.jpg";
                object.requestId =1;
                object.type = TransferObject.Type.OUTGOING;
                object.flag= TransferObject.Flag.PENDING;
                AppUtils.getDatabase(getContext()).publish(object);

//                object.friendlyName = "hello";
//                database.publish(object);
//                List<CursorItem> items3 = database.getTable(new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER));
//                for(CursorItem ii: items3){
//                    TransferObject p = new TransferObject();
//                    p.reconstruct(ii);
//                    System.out.println(p.friendlyName);
//                }


            }
        });

        findComponentById(ResourceTable.Id_recc).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
//                String ss = textField.getText();
//                Intent intent = new Intent();
//                intent.setParam(CommunicationService.EXTRA_DEVICE_ID,"1000235555");
//                intent.setParam(CommunicationService.EXTRA_GROUP_ID,Long.parseLong(ss));
//                intent.setParam(CommunicationService.EXTRA_IS_ACCEPTED,true);
//                Operation operation = new Intent.OperationBuilder()
//                        .withBundleName("com.loser007")
//                        .withAbilityName(CommunicationService.class)
//                        .withAction(CommunicationService.ACTION_FILE_TRANSFER)
//                        .build();
//                intent.setOperation(operation);
//                startAbility(intent);
                AccessDatabase database = AppUtils.getDatabase(getContext());

                TransferGroup group = new TransferGroup(1);
                try {
                    database.reconstruct(group);
                    UIConnectionUtils.sendFile(TestAbility.this,"1000235555","wlan0",group);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        findComponentById(ResourceTable.Id_showDeviceAGroup).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                AccessDatabase database = AppUtils.getDatabase(getContext());

//                System.out.println("show device:");
//                List<CursorItem> items = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICES));
//                for(CursorItem item:items){
//                    NetworkDevice device = new NetworkDevice();
//                    device.reconstruct(item);
//                    System.out.println(JSONObject.toJSONString(device));
//                }
//                System.out.println("show connection:");
//                List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
//                for(CursorItem item:items0){
//                    NetworkDevice.Connection connection = new NetworkDevice.Connection();
//                    connection.reconstruct(item);
//                    System.out.println(JSONObject.toJSONString(connection));
//                }
//                System.out.println("show group");
//                List<CursorItem> items2 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
//                System.out.println("group---->"+items2.size());
//                for(CursorItem item:items2){
//                    TransferGroup group = new TransferGroup();
//                    group.reconstruct(item);
//                    System.out.println(JSONObject.toJSONString(group));
//                }
//                List<CursorItem> items3 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER));
//                System.out.println("object---->"+items3.size());
//                List<CursorItem> items4 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_CLIPBOARD));
//                for(CursorItem item:items4){
//                    TextStreamObject object = new TextStreamObject();
//                    object.reconstruct(item);
//                    System.out.println(object.text);
//                }
//                TransferGroup group = new TransferGroup();
//                group.groupId=1;
//                group.isServedOnWeb=true;
//                group.dateCreated = System.currentTimeMillis();
//                database.insert(group);
//
//                TransferGroup group2 = new TransferGroup();
//                group2.groupId=2;
//                group2.isServedOnWeb=true;
//                group2.dateCreated = System.currentTimeMillis();
//                database.insert(group2);
//
//                List<CursorItem> items0 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
//                System.out.println(" TestSize="+items0.size());
//                List<TransferGroup> dds = new ArrayList<>();
//                dds.add(group);
//                database.remove(dds);
//                List<CursorItem> items1 = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP));
//                System.out.println(" TestSize="+items1.size());

                List<CursorItem> items = database.getTable(new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION));
                for(CursorItem i:items){
                    NetworkDevice.Connection connection = new NetworkDevice.Connection();
                    connection.reconstruct(i);
                    System.out.println(connection.deviceId+"--------"+connection.adapterName);
                }
            }
        });

        findComponentById(ResourceTable.Id_sendMsg).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                try {
//                    CommunicationBridge.Client client = new CommunicationBridge.Client(AppUtils.getDatabase(getContext()));
//                    NetworkDevice device = client.loadDevice("12345687");
//                    device.deviceId,device.
                    UIConnectionUtils.sendMsg(getContext(),"unknown","wlan0","hello word!");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        findComponentById(ResourceTable.Id_addConnect).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                getContext().getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        UIConnectionUtils.setupConnectionSync(TestAbility.this, "10.71.12.100", 1, new NetworkDeviceLoader.OnDeviceRegisteredErrorListener() {
                            @Override
                            public void onError(Exception error) {

                            }

                            @Override
                            public void onDeviceRegistered(AccessDatabase database, NetworkDevice device, NetworkDevice.Connection connection) {
                                System.out.println("conneted..........");
                            }
                        });
                    }
                });

            }
        });


        //扫描设备
        findComponentById(ResourceTable.Id_scanDevice).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName(getBundleName())
                        .withAbilityName(CommunicationService.class)
                        .withAction(CommunicationService.ACTION_DEVICE_DISCOVERY)
                        .build();
                intent.setOperation(operation);
                startAbility(intent);
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
