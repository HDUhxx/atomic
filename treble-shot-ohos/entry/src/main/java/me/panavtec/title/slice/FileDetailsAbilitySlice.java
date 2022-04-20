package me.panavtec.title.slice;

import com.ohos.trebleshot.object.TransferObject;
import me.panavtec.title.ConnectionManagerActivity;
import me.panavtec.title.Entity.PreloadedGroup;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.FileDetailProvider;
import me.panavtec.title.hmutils.DBHelper;
import me.panavtec.title.hmutils.FileUtils;
import me.panavtec.title.hmutils.TextUtils;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;

import java.util.ArrayList;
import java.util.List;

public class FileDetailsAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    public static final String KEY_GROUNDID = "id_ground";
    public static final String KEY_REQUESTID = "id_request";

    public Text getTextNum() {
        return textNum;
    }

    public void setTextNum(Text textNum) {
        this.textNum = textNum;
    }

    public String getGroundId() {
        return groundId;
    }

    public void setGroundId(String groundId) {
        this.groundId = groundId;
    }

    public ArrayList<String> getRequestIds() {
        return requestIds;
    }

    public void setRequestIds(ArrayList<String> requestIds) {
        this.requestIds = requestIds;
    }

    public ListContainer getListContainer() {
        return listContainer;
    }

    public void setListContainer(ListContainer listContainer) {
        this.listContainer = listContainer;
    }

    public FileDetailProvider getProvider() {
        return provider;
    }

    public void setProvider(FileDetailProvider provider) {
        this.provider = provider;
    }

    public List<PreloadedGroup> getFileList() {
        return fileList;
    }

    public void setFileList(List<PreloadedGroup> fileList) {
        this.fileList = fileList;
    }

    public Text getTeSize() {
        return teSize;
    }

    public void setTeSize(Text teSize) {
        this.teSize = teSize;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getConnectionAdapter() {
        return connectionAdapter;
    }

    public void setConnectionAdapter(String connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    public CommonDialog getCommonDialog() {
        return commonDialog;
    }

    public void setCommonDialog(CommonDialog commonDialog) {
        this.commonDialog = commonDialog;
    }

    private Text textNum;
    private String groundId;
    private ArrayList<String> requestIds;
    private ListContainer listContainer;
    private FileDetailProvider provider;
    private List<PreloadedGroup> fileList;
    private Text teSize;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_file_details);
        requestIds = intent.getStringArrayListParam(KEY_REQUESTID);
        groundId = intent.getStringParam(KEY_GROUNDID);
        initView();
        initListView();
        initData();
    }

    private void initData() {
        if (fileList.size() == 0) return;
        long allSize = 0;
        for (PreloadedGroup preloadedGroup : fileList) {
            String size = preloadedGroup.getSize();
            if (TextUtils.isEmpty(size)) continue;
            long tempSize = Long.parseLong(size);
            allSize = allSize + tempSize;
        }
        teSize.setText(FileUtils.sizeExpression(allSize, false));
        textNum.setText(fileList.size() + "个文件");
    }

    private void initView() {
        findComponentById(ResourceTable.Id_action_close).setClickedListener(this);
        teSize = (Text) findComponentById(ResourceTable.Id_teSize);
        textNum = (Text) findComponentById(ResourceTable.Id_action_num);
        findComponentById(ResourceTable.Id_action_delete).setClickedListener(this);
        findComponentById(ResourceTable.Id_action_more).setClickedListener(this);
        findComponentById(ResourceTable.Id_actionSend).setClickedListener(this);
    }

    private void initListView() {
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listText);
        if (fileList!=null) fileList.clear();
        fileList = getData();
        provider = new FileDetailProvider(fileList, this);
        listContainer.setItemProvider(provider);
        listContainer.setItemClickedListener((listContainer, component, i, l) -> {
            PreloadedGroup preloadedGroup = fileList.get(i);
            showDetailDialog(preloadedGroup);
        });

    }

    private List<PreloadedGroup> getData() {
        if (requestIds == null || requestIds.size() == 0) return new ArrayList<>();
//        for (String requestId : requestIds) {
//            if (TextUtils.isEmpty(requestId)) continue;
            System.out.println("================"+DBHelper.queryDataByGroundId(getContext(), groundId).size());
        List<TransferObject> list = new ArrayList<>(DBHelper.queryDataAll(getContext(), groundId));
//        }
        return DBHelper.convertViewData(list);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_action_close:
                terminateAbility();
                break;
            case ResourceTable.Id_action_num:
                textNum.setText("1个文件");
                break;
            case ResourceTable.Id_action_delete:
                DBHelper.deleteTransferInDb(fileList, getContext());
                terminateAbility();
                break;
            case ResourceTable.Id_actionSend:

                jumpAbility(ConnectionManagerActivity.class);

                break;
            case ResourceTable.Id_action_more:
//                startSendFile(groundId,deviceId,connectionAdapter);
                break;
        }
    }

    public void jumpAbility(Class<?> tclass) {
        Intent intent = new Intent();
        Operation build = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName(tclass)
                .withBundleName(getBundleName())
                .build();
        intent.setOperation(build);
        intent.setParam(FileDetailsAbilitySlice.KEY_GROUNDID, groundId);
        intent.setStringArrayListParam(FileDetailsAbilitySlice.KEY_REQUESTID, requestIds);
        startAbilityForResult(intent, 1);
    }

    String deviceId, connectionAdapter;

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == 0) {
            if (requestCode == 1
                    && resultData != null
                    && resultData.hasParameter(ConnectionManagerActivity.EXTRA_DEVICE_ID)
                    && resultData.hasParameter(ConnectionManagerActivity.EXTRA_CONNECTION_ADAPTER)) {
                deviceId = resultData.getStringParam(ConnectionManagerActivity.EXTRA_DEVICE_ID);
                connectionAdapter = resultData.getStringParam(ConnectionManagerActivity.EXTRA_CONNECTION_ADAPTER);
                try {
                    Toast.show(getContext(), deviceId + "&&" + connectionAdapter);
//                    NetworkDevice networkDevice = new NetworkDevice(deviceId);
//                    NetworkDevice.Connection connection = new NetworkDevice.Connection(deviceId, connectionAdapter);
//                    AppUtils.getDatabase(this).reconstruct(networkDevice);
//                    AppUtils.getDatabase(this).reconstruct(connection);

//                    startSendFile(groundId, deviceId, connectionAdapter);
                } catch (Exception e) {
                    Toast.show(this, ResourceTable.String_mesg_somethingWentWrong, Toast.LENGTH_SHORT);
                }
            }
        }
    }


    private CommonDialog commonDialog;

    private void showDetailDialog(PreloadedGroup preloadedGroup) {
        if (commonDialog == null) commonDialog = new CommonDialog(getContext());
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_layout_transfer_info, null, false);
        commonDialog.setTransparent(true);
        commonDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);

        Text fileName = (Text) component.findComponentById(ResourceTable.Id_transfer_info_file_name);
        Text fileSize = (Text) component.findComponentById(ResourceTable.Id_transfer_info_file_size);
        Text fileMine = (Text) component.findComponentById(ResourceTable.Id_transfer_info_file_mime);
        Text fileStatus = (Text) component.findComponentById(ResourceTable.Id_transfer_info_file_status);
        Text fileLocation = (Text) component.findComponentById(ResourceTable.Id_transfer_info_pseudo_location);

        component.findComponentById(ResourceTable.Id_teOpen).setClickedListener(component1 -> commonDialog.destroy());
        component.findComponentById(ResourceTable.Id_teRemove).setClickedListener(component12 -> {
            commonDialog.destroy();
            ArrayList<PreloadedGroup> preloadedGroups = new ArrayList<>();
            preloadedGroups.add(preloadedGroup);
            fileList.remove(preloadedGroup);
            provider.notifyDataChanged();
            DBHelper.deleteTransferInDb(preloadedGroups, getContext());
            if (fileList.size() == 0) terminateAbility();
        });
        component.findComponentById(ResourceTable.Id_teClose).setClickedListener(component13 -> commonDialog.destroy());

        fileName.setText(preloadedGroup.getTitle());
        fileSize.setText(FileUtils.sizeExpression(Long.parseLong(preloadedGroup.getSize()), false));
        fileMine.setText(preloadedGroup.getMineType());
        fileStatus.setText("等待中");
        fileLocation.setText(preloadedGroup.getSavePath());

        commonDialog.setContentCustomComponent(component);
        if (!commonDialog.isShowing()) commonDialog.show();
    }

}
