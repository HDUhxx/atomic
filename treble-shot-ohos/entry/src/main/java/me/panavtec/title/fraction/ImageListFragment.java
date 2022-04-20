package me.panavtec.title.fraction;

import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.Entity.ImageEntity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.ImageListProvider;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.FileUtils;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.data.resultset.ResultSet;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends BaseListFraction {
    List<ImageEntity> imaList;
    ImageListProvider imageListProvider;

    public ImageListFragment(SliceToMainI sliceToMainI) {
        super(sliceToMainI);
    }

    @Override
    public List<? extends BaseSelectEntity> getSelectedList() {
        if (imageListProvider == null) return null;
        return imageListProvider.getSelectedList();
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        initListView();
        setEmptyView(true, ResourceTable.String_text_listEmptyImage, ResourceTable.Graphic_ic_photo_white_24dp);
        getMediaInfoInfo();
    }

    private void initListView() {
        imaList = new ArrayList<>();
        imageListProvider = new ImageListProvider(imaList, getMyContext());
        getListContainer().setItemProvider(imageListProvider);
        getListContainer().setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                ImageEntity songHolder = imaList.get(i);
                if (isLongClickState()) {
                    sliceToMainI.allSelectNum(songHolder.isSelected() ? -1 : 1);
                    imageListProvider.longClickItem(i);
                } else {
                    Intent intent = new Intent();
//                    intent.setAction("android.media.action.IMAGE_CAPTURE");
                    intent.setAction("android.intent.action.MAIN");
                    intent.addEntity("android.intent.category.APP_GALLERY");
                    intent.setParam("output", songHolder.getUri());
                    intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                    getMyContext().startAbility(intent, 0);
                }
            }
        });
        getListContainer().setItemLongClickedListener(new ListContainer.ItemLongClickedListener() {
            @Override
            public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
                setLongClickState(true);
                ImageEntity songHolder = imaList.get(i);
                sliceToMainI.allSelectNum(songHolder.isSelected() ? -1 : 1);
                imageListProvider.longClickItem(i);
                return false;
            }
        });
    }


    @Override
    public void onRefresh() {
//        getMediaInfoInfo();
    }

    @Override
    public void onLongClickChanged(boolean longClickState) {
        if (!longClickState && imageListProvider != null) imageListProvider.clearSelected();
    }

    @Override
    public void onTextSeared(String s) {
        if (imageListProvider != null) imageListProvider.setFilterData(s);
    }


    @Override
    public int setXMLId() {
        return ResourceTable.Layout_layout_resource_template_list;
    }

    private void getMediaInfoInfo() {
        DataAbilityHelper helper = DataAbilityHelper.creator(getMyContext());
        try {
            ResultSet result = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
            if (result == null) return;
            if (imaList != null) imaList.clear();
            ImageEntity entity;
            while (result.goToNextRow()) {
                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Images.Media.ID));// 获取id字段的值
                String displayName = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.DISPLAY_NAME));
                String title = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.TITLE));
                String mineType = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.MIME_TYPE));
                String duration = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.DURATION));
                Uri uri = FileUtils.getUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(mediaId));
                String data = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.DATA));
                String dateAdded = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.DATE_ADDED));
                String size = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.SIZE));
                String volumeName = result.getString(result.getColumnIndexForName(AVStorage.Images.Media.VOLUME_NAME));

                entity = new ImageEntity();
                entity.setUri(uri);
                entity.setMediaId(mediaId);
                entity.setDisplayName(displayName);
                entity.setTitle(title);
                entity.setMineType(mineType);
                entity.setDuration(duration);
                entity.setData(data);
                entity.setDateAdded(dateAdded);
                entity.setSize(size);
                entity.setVolumeName(volumeName);

                imaList.add(entity);
                System.out.println("-----tagima--" + printInfo(mediaId + "", displayName, title, mineType
                        , duration, uri.toString(), data, dateAdded, size, volumeName));
            }
            result.close();
            imageListProvider.notifyDataChanged();
            setEmptyView(imaList == null || imaList.size() == 0, ResourceTable.String_text_listEmptyImage,
                    ResourceTable.Graphic_ic_photo_white_24dp);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }finally {
            helper.release();
        }
    }

}
