package me.panavtec.title.fraction;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.title.ConnectionManagerActivity;
import me.panavtec.title.ContentSharingActivity;
import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.Entity.PreloadedGroup;
import me.panavtec.title.FileDetailsAbility;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.TransferListProvider;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.DBHelper;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.ListContainer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static me.panavtec.title.slice.FileDetailsAbilitySlice.KEY_GROUNDID;
import static me.panavtec.title.slice.FileDetailsAbilitySlice.KEY_REQUESTID;

public class TransferGroupListFragment extends BaseActionFraction implements Component.ClickedListener {

    public static final String ACTION_LIST_TRANSFERS = "com.genonbeta.TrebleShot.action.LIST_TRANSFERS";
    public static final String EXTRA_GROUP_ID = "extraGroupId";
    public static final String EXTRA_REQUEST_ID = "extraRequestId";
    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_REQUEST_TYPE = "extraRequestType";


    private ListContainer listContainer;
    private DirectionalLayout emptyView;
    private TransferListProvider sampleItemProvider;
    private List<PreloadedGroup> fileList;

    public TransferGroupListFragment(SliceToMainI sliceToMainI) {
        super(sliceToMainI);
    }


    @Override
    public int setXMLId() {
        return ResourceTable.Layout_layout_transfer_group_list;
    }


    @Override
    public List<? extends BaseSelectEntity> getSelectedList() {
        if (sampleItemProvider == null) return null;
        return sampleItemProvider.getSelectedList();
    }

    @Override
    protected void onActive() {
        super.onActive();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        fileList.clear();
        fileList.addAll(getData());
        listContainer.setVisibility(fileList.size() == 0 ? Component.HIDE : Component.VISIBLE);
        emptyView.setVisibility(fileList.size() == 0 ? Component.VISIBLE : Component.HIDE);
        sampleItemProvider.notifyDataChanged();
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        listContainer = (ListContainer) viewComponent.findComponentById(ResourceTable.Id_listText);
        viewComponent.findComponentById(ResourceTable.Id_sendLayoutButton).setClickedListener(this);
        viewComponent.findComponentById(ResourceTable.Id_receiveLayoutButton).setClickedListener(this);
        emptyView = (DirectionalLayout) viewComponent.findComponentById(ResourceTable.Id_emptyView);
        initListContainer();
    }

    private void initListContainer() {
        fileList = getData();
        boolean b = fileList == null || fileList.size() == 0;
        listContainer.setVisibility(b ? Component.HIDE : Component.VISIBLE);
        emptyView.setVisibility(b ? Component.VISIBLE : Component.HIDE);

        sampleItemProvider = new TransferListProvider(fileList, getMyContext());
        listContainer.setItemProvider(sampleItemProvider);
        listContainer.setItemClickedListener((listContainer, component, i, id) -> {
            if (isLongClickState()) clickItem(i);
            else {
                PreloadedGroup preloadedGroup = sampleItemProvider.getListData().get(i);
                Intent intent = new Intent();
                Operation build = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withAbilityName(FileDetailsAbility.class)
                        .withBundleName(getMyContext().getBundleName())
                        .build();
                intent.setOperation(build);
                intent.setParam(KEY_GROUNDID, preloadedGroup.getGroupId() + "");
                ArrayList<String> requestIds = new ArrayList<>();
                requestIds.add(preloadedGroup.getRequestId() + "");
                intent.setStringArrayListParam(KEY_REQUESTID, requestIds);
                getMyContext().startAbility(intent, 1);
            }
        });
        listContainer.setItemLongClickedListener((listContainer, component, i, l) -> {
            setLongClickState(true);
            clickItem(i);
            return false;
        });
    }

    private void clickItem(int i) {
        sampleItemProvider.longClickItem(i);
        int size = sampleItemProvider.getSelectedList().size();
        sliceToMainI.allSelectNum(size);
    }

    private List<PreloadedGroup> getData() {
        ArrayList<TransferObject> objects = new ArrayList<>();
        List<TransferObject> transferObjects = queryWillUpData();
        List<TransferObject> transferObjects1 = queryIncommeData();
        objects.addAll(transferObjects);
        objects.addAll(transferObjects1);
        return DBHelper.convertViewData(objects);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_sendLayoutButton:
                jumpAbility(ContentSharingActivity.class);
                break;
            case ResourceTable.Id_receiveLayoutButton:
                jumpAbility(ConnectionManagerActivity.class);
                break;
        }
    }

    @Override
    public void clearSelected() {
        if (sampleItemProvider != null) sampleItemProvider.clearSelected();
    }

    @Override
    public void deleteItems() {
        if (sampleItemProvider != null) {
            List<PreloadedGroup> selectedList = sampleItemProvider.getSelectedList();
            sampleItemProvider.clearSelected();
            DBHelper.deleteTransferInDb(selectedList, getMyContext());
            fileList.removeAll(selectedList);
            sampleItemProvider.notifyDataChanged();
        }
    }

    @Override
    public void doAction(int action) {
        Toast.show(getMyContext(), "更多Action");
    }

    @Override
    public void onTextSeared(String s) {
        super.onTextSeared(s);
        if (sampleItemProvider != null) sampleItemProvider.setFilterData(s);
    }

    @Override
    public void onLongClickChanged(boolean longClickState) {
        if (!longClickState && sampleItemProvider != null) sampleItemProvider.clearSelected();
    }

    private List<TransferObject> queryWillUpData() {
        String exr = AccessDatabase.FIELD_TRANSFER_TYPE + "=?";
        return AppUtils.getDatabase(getMyContext()).castQuery(
                new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER).setWhere(exr,
                        TransferObject.Type.OUTGOING.toString()), TransferObject.class);
    }

    private List<TransferObject> queryIncommeData() {
        String exr = AccessDatabase.FIELD_TRANSFER_TYPE + "=?";
        return AppUtils.getDatabase(getMyContext()).castQuery(
                new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER).setWhere(exr,
                        TransferObject.Type.INCOMING.toString()), TransferObject.class);
    }


    public void sortBySize(boolean isZ) {
        fileList.sort(new Comparator<PreloadedGroup>() {
            @Override
            public int compare(PreloadedGroup preloadedGroup, PreloadedGroup t1) {
                long size = Long.parseLong(preloadedGroup.getSize() == null ? "0" : preloadedGroup.getSize());
                long size1 = Long.parseLong(t1.getSize() == null ? "0" : t1.getSize());
                return isZ ? Long.compare(size1, size) : Long.compare(size, size1);
            }
        });
        sampleItemProvider.notifyDataChanged();
    }


    public void sortByTime(boolean isZ) {
        fileList.sort(new Comparator<PreloadedGroup>() {
            @Override
            public int compare(PreloadedGroup preloadedGroup, PreloadedGroup t1) {
                long size = Long.parseLong(preloadedGroup.getDateAdded() == null ? "0" : preloadedGroup.getDateAdded());
                long size1 = Long.parseLong(t1.getDateAdded() == null ? "0" : t1.getDateAdded());
                return isZ ? Long.compare(size1, size) : Long.compare(size, size1);
            }
        });
        sampleItemProvider.notifyDataChanged();
    }

}
