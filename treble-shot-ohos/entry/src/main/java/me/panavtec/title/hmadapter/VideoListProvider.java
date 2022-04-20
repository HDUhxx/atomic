package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.VideoEntity;
import me.panavtec.title.ResourceTable;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVMetadataHelper;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.util.List;

public class VideoListProvider extends BaseListProvider<VideoEntity, VideoListProvider.VideoHolder> {

    public VideoListProvider(List<VideoEntity> list, Context context) {
        super(list, context);
    }

    @Override
    int getItemLayout() {
        return ResourceTable.Layout_list_video;
    }

    @Override
    void convertItem(int position, List<VideoEntity> data, VideoHolder viewHolder) {
        VideoEntity videoEntity = data.get(position);
        viewHolder.text1.setText(nullCheck(videoEntity.getTitle()));
        setVideo(videoEntity.getUri(), viewHolder);
        viewHolder.selector.setVisibility(videoEntity.isSelected() ? Component.VISIBLE : Component.HIDE);
    }

    private void setVideo(Uri uri, VideoHolder viewHolder) {
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
            avMetadataHelper.resolveMetadata(AVMetadataHelper.AV_KEY_HAS_IMAGE);
            PixelMap pixelMap = avMetadataHelper.fetchVideoPixelMapByTime();
            if (pixelMap != null) viewHolder.image.setPixelMap(pixelMap);
            viewHolder.text2.setText(nullCheck(artist));
            pixelMap.release();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (avMetadataHelper != null) avMetadataHelper.release();
            helper.release();
        }
    }


    @Override
    VideoHolder getViewHolder(Component component) {
        return new VideoHolder(component);
    }

    public static class VideoHolder extends BaseListProvider.BaseViewHolder {
        final Image image;
        final Image selector;
        final Image visitView;
        final Text text1;
        final Text text2;
        final Text text3;

        public VideoHolder(Component component) {
            super(component);
            image = (Image) component.findComponentById(ResourceTable.Id_image);
            selector = (Image) component.findComponentById(ResourceTable.Id_selector);
            visitView = (Image) component.findComponentById(ResourceTable.Id_visitView);

            text1 = (Text) component.findComponentById(ResourceTable.Id_text);
            text2 = (Text) component.findComponentById(ResourceTable.Id_text2);
            text3 = (Text) component.findComponentById(ResourceTable.Id_text3);
        }
    }
}
