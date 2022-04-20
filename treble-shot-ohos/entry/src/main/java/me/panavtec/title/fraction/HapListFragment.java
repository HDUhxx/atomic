package me.panavtec.title.fraction;

import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.Entity.VideoEntity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.HapListProvider;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.FileUtils;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.data.resultset.ResultSet;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class HapListFragment extends BaseListFraction {
    List<BaseSelectEntity> appList;
    HapListProvider provider;

    public HapListFragment(SliceToMainI sliceToMainI) {
        super(sliceToMainI);
    }

    @Override
    public List<? extends BaseSelectEntity> getSelectedList() {
        if (provider == null) return null;
        return provider.getSelectedList();
    }

    @Override
    public int setXMLId() {
        return ResourceTable.Layout_layout_resource_template_list;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        initListView();
//        getVideoList();
        setVideoEmptyView();
    }

    @Override
    public void onRefresh() {
        System.out.println("-----------onRefresh-video-------------");
    }

    @Override
    public void onLongClickChanged(boolean longClickState) {
        if (!longClickState && provider != null) provider.clearSelected();
    }

    @Override
    public void onTextSeared(String s) {
        if (provider != null) provider.setFilterData(s);
    }


    private void initListView() {
        appList = new ArrayList<>();
        provider = new HapListProvider(appList, getMyContext());
        getListContainer().setItemProvider(provider);
        getListContainer().setItemClickedListener((listContainer, component, i, l) -> {
            if (isLongClickState()) {
                BaseSelectEntity videoEntity = appList.get(i);
                sliceToMainI.allSelectNum(videoEntity.isSelected() ? -1 : 1);
                provider.longClickItem(i);
            }
        });
        getListContainer().setItemLongClickedListener((listContainer, component, i, l) -> {
            setLongClickState(true);
            BaseSelectEntity videoEntity = appList.get(i);
            sliceToMainI.allSelectNum(videoEntity.isSelected() ? -1 : 1);
            provider.longClickItem(i);
            return false;
        });
    }


    private void getHapList() {
        DataAbilityHelper helper = DataAbilityHelper.creator(getMyContext());
        try {
            ResultSet result = helper.query(AVStorage.Video.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
            if (result == null) return;
            if (appList != null) appList.clear();
            VideoEntity videoEntity;
            while (result.goToNextRow()) {
                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Video.Media.ID));// 获取id字段的值
                String displayName = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.DISPLAY_NAME));
                String title = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.TITLE));
                String mine_type = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.MIME_TYPE));
                String duration = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.DURATION));
                Uri uri = FileUtils.getUri(AVStorage.Video.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(mediaId));
                String data = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.DATA));
                String dateAdded = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.DATE_ADDED));
                String size = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.SIZE));
                String volumeName = result.getString(result.getColumnIndexForName(AVStorage.Video.Media.VOLUME_NAME));

                videoEntity = new VideoEntity();
                videoEntity.setUri(uri);
                videoEntity.setMediaId(mediaId);
                videoEntity.setDisplayName(displayName);
                videoEntity.setTitle(title);
                videoEntity.setMineType(mine_type);
                videoEntity.setDuration(duration);
                videoEntity.setData(data);
                videoEntity.setDateAdded(dateAdded);
                videoEntity.setSize(size);
                videoEntity.setVolumeName(volumeName);
                appList.add(videoEntity);
//-----tagvideo--111**VID_20210423_193141.mp4**VID_20210423_193141**video/mp4**5888**dataability:///media/external/video/media/111**/storage/emulated/0/DCIM/Camera/VID_20210423_193141.mp4**1619177501**14889041**external_primary**

                System.out.println("-----tagvideo--" + printInfo(mediaId + "", displayName, title, mine_type
                        , duration, uri.toString(), data, dateAdded, size, volumeName));
            }
            result.close();
            provider.notifyDataChanged();
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        } finally {
            helper.release();
        }
    }


    private void setVideoEmptyView() {
        setEmptyView(appList == null || appList.size() == 0, ResourceTable.String_text_listEmptyApp,
                ResourceTable.Graphic_ic_android_head_white_24dp);
    }


}
