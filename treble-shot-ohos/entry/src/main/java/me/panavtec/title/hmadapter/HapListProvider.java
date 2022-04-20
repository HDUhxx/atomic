package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVThumbnailUtils;

import java.io.File;
import java.util.List;

public class HapListProvider extends BaseListProvider<BaseSelectEntity, HapListProvider.VideoHolder> {

    public HapListProvider(List<BaseSelectEntity> list, Context context) {
        super(list, context);
    }

    @Override
    int getItemLayout() {
        return ResourceTable.Layout_list_video;
    }

    @Override
    void convertItem(int position, List<BaseSelectEntity> data, VideoHolder viewHolder) {
        BaseSelectEntity baseSelectEntity = data.get(position);
        viewHolder.text1.setText(nullCheck(baseSelectEntity.getTitle()));

        viewHolder.selector.setVisibility(baseSelectEntity.isSelected() ? Component.VISIBLE : Component.HIDE);

//----------------------------------------------------------------------------
        String path = baseSelectEntity.getData();
        File file = new File(baseSelectEntity.getUri().getDecodedPath());
        PixelMap videoThumbnail = AVThumbnailUtils.createVideoThumbnail(file, new Size(150, 150));
        viewHolder.image.setPixelMap(videoThumbnail);
//----------------------------------------------------------------------------
//        setVideoImage(BaseSelectEntity.getUri(), viewHolder);

//----------------------------------------------------------------------------
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
