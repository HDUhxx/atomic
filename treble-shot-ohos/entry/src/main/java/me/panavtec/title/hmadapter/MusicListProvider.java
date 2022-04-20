package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.SongEntity;
import me.panavtec.title.ResourceTable;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;
import ohos.media.photokit.metadata.AVMetadataHelper;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.util.List;

public class MusicListProvider extends BaseListProvider<SongEntity, MusicListProvider.MusicHolder> {

    public MusicListProvider(List<SongEntity> list,  Context context) {
        super(list, context);
    }

    @Override
    int getItemLayout() {
        return ResourceTable.Layout_list_music;
    }

    @Override
    void convertItem(int position, List<SongEntity> data, MusicHolder viewHolder) {
        SongEntity song = data.get(position);
        viewHolder.text1.setText(nullCheck(song.getTitle()));
        setMusic(song.getUri(), viewHolder);
        viewHolder.selector.setVisibility(song.isSelected() ? Component.VISIBLE : Component.HIDE);
    }


    private void setMusic(Uri uri, MusicHolder viewHolder) {
        AVMetadataHelper avMetadataHelper = new AVMetadataHelper();
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        try {
            FileDescriptor filedesc = helper.openFile(uri, "r");
            avMetadataHelper.setSource(filedesc);
//            String AUTHOR = avMetadataHelper.resolveMetadata(AVMetadataHelper.AV_KEY_AUTHOR);
//            String DURATION = avMetadataHelper.resolveMetadata(AVMetadataHelper.AV_KEY_DURATION);
//            String TITLE = avMetadataHelper.resolveMetadata(AVMetadataHelper.AV_KEY_TITLE);
//            String LOCATION = avMetadataHelper.resolveMetadata(AVMetadataHelper.AV_KEY_LOCATION);
            String artist = avMetadataHelper.resolveMetadata(AVMetadataHelper.AV_KEY_ARTIST);
            viewHolder.text2.setText(nullCheck(artist));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (avMetadataHelper != null) avMetadataHelper.release();
            helper.release();
        }
    }


    @Override
    MusicHolder getViewHolder(Component component) {
        return new MusicHolder(component);
    }

    public static class MusicHolder extends BaseListProvider.BaseViewHolder {
        final Image image;
        final Image selector;
        final Text text1;
        final Text text2;
        final Text text3;
        final Text textSeparator1;

        public MusicHolder(Component component) {
            super(component);
            image = (Image) component.findComponentById(ResourceTable.Id_image);
            selector = (Image) component.findComponentById(ResourceTable.Id_selector);

            text1 = (Text) component.findComponentById(ResourceTable.Id_text);
            text2 = (Text) component.findComponentById(ResourceTable.Id_text2);
            text3 = (Text) component.findComponentById(ResourceTable.Id_text3);
            textSeparator1 = (Text) component.findComponentById(ResourceTable.Id_textSeparator1);
        }
    }

}
