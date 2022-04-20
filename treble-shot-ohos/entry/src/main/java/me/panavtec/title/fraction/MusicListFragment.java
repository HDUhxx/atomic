package me.panavtec.title.fraction;

import me.panavtec.title.Entity.SongEntity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.MusicListProvider;
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

/** @noinspection unchecked*/
public class MusicListFragment extends BaseListFraction {
    List<SongEntity> musicList;
    MusicListProvider musicListProvider;


    public MusicListFragment(SliceToMainI sliceToMainI) {
        super(sliceToMainI);
    }

    @Override
    public int setXMLId() {
        return ResourceTable.Layout_layout_resource_template_list;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        initListView();
        getMediaInfoInfo();
        setEmptyView(musicList == null || musicList.size() == 0, ResourceTable.String_text_listEmptyMusic,
                ResourceTable.Graphic_ic_library_music_white_24dp);
    }

    private void initListView() {
        musicList = new ArrayList<>();
        musicListProvider = new MusicListProvider(musicList,  getMyContext());
        getListContainer().setItemProvider(musicListProvider);
        getListContainer().setItemClickedListener((listContainer, component, i, l) -> {
            SongEntity songHolder = musicList.get(i);
            if (isLongClickState()) {
                sliceToMainI.allSelectNum(songHolder.isSelected() ? -1 : 1);
                musicListProvider.longClickItem(i);
            } else {
//                Intent intent = new Intent();
//                intent.setAction("android.media.action.IMAGE_CAPTURE");
//                intent.setParam("output", songHolder.getUri());
//                intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
//                getMyContext().startAbility(intent, 0);
            }
        });
        getListContainer().setItemLongClickedListener((listContainer, component, i, l) -> {
            setLongClickState(true);
            SongEntity songHolder = musicList.get(i);
            sliceToMainI.allSelectNum(songHolder.isSelected() ? -1 : 1);
            musicListProvider.longClickItem(i);
            return false;
        });
    }


    @Override
    public void onRefresh() {
        System.out.println("-----------onRefresh-music-------------");
//        getMediaInfoInfo();
    }

    @Override
    public void onTextSeared(String s) {
        if (musicListProvider != null) musicListProvider.setFilterData(s);
    }

    public List getSelectedList() {
        if (musicListProvider == null) return null;
        return musicListProvider.getSelectedList();
    }


    @Override
    public void onLongClickChanged(boolean longClickState) {
        if (!longClickState && musicListProvider != null) musicListProvider.clearSelected();
    }


    private void getMediaInfoInfo() {
        DataAbilityHelper helper = DataAbilityHelper.creator(getMyContext());
        try {
            ResultSet result = helper.query(AVStorage.Audio.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
            if (result == null) return;
            if (musicList != null) musicList.clear();
            SongEntity songHolder;
//            PacMap extensions=null;
            while (result.goToNextRow()) {
                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Audio.Media.ID));// 获取id字段的值
                String displayName = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.DISPLAY_NAME));
                String title = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.TITLE));
                String mineType = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.MIME_TYPE));
                String duration = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.DURATION));
                Uri uri = FileUtils.getUri(AVStorage.Audio.Media.EXTERNAL_DATA_ABILITY_URI, mediaId + "");
                String data = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.DATA));
                String dateAdded = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.DATE_ADDED));
                String size = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.SIZE));
                String volumeName = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.VOLUME_NAME));
//                String output = result.getString(result.getColumnIndexForName(AVStorage.Audio.Media.OUTPUT));

//                String s = uri.getDecodedPathList().get(0);
//                File file = new File(s);
////                FileDescriptor r = helper.openFile(uri, "r");
//                PixelMap resMap = AVThumbnailUtils.createVideoThumbnail(file, new Size());
//                PixelMap videoThumbnail = AVThumbnailUtils.createImageThumbnail(file, new Size());


                songHolder = new SongEntity();
                songHolder.setUri(uri);
                songHolder.setMediaId(mediaId);
                songHolder.setDisplayName(displayName);
                songHolder.setTitle(title);
                songHolder.setMineType(mineType);
                songHolder.setDuration(duration);
                songHolder.setData(data);
                songHolder.setDateAdded(dateAdded);
                songHolder.setSize(size);
                songHolder.setVolumeName(volumeName);
                musicList.add(songHolder);
                System.out.println("-----tagmusic--" + printInfo(mediaId + "", displayName, title, mineType
                        , duration, uri.toString(), data, dateAdded, size, volumeName));
            }
            result.close();
            musicListProvider.notifyDataChanged();
            setEmptyView(musicList == null, ResourceTable.String_text_listEmptyMusic, ResourceTable.Graphic_ic_library_music_white_24dp);
//            Map<String, Object> all = extensions.getAll();
//            int size = all.size();

        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }finally {
            helper.release();
        }
    }


}
