package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.ImageEntity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.FileUtils;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.util.List;

public class ImageListProvider extends BaseListProvider<ImageEntity, ImageListProvider.ImageHolder> {


    public ImageListProvider(List<ImageEntity> list, Context context) {
        super(list, context);
    }

    @Override
    int getItemLayout() {
        return ResourceTable.Layout_list_image;
    }

    @Override
    void convertItem(int position, List<ImageEntity> data, ImageHolder viewHolder) {
        ImageEntity imageEntity = data.get(position);

        viewHolder.text1.setText(imageEntity.getDisplayName());
        viewHolder.text2.setText("大小：" + FileUtils.sizeExpression(Long.parseLong(imageEntity.getSize()), false));
        setImage(imageEntity.getUri(), viewHolder);
        viewHolder.selector.setVisibility(imageEntity.isSelected() ? Component.VISIBLE : Component.HIDE);
    }

    private void setImage(Uri uri, ImageHolder viewHolder) {
        context.getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {
                try {
                    DataAbilityHelper helper = DataAbilityHelper.creator(context);
                    FileDescriptor filedesc = helper.openFile(uri, "r");
                    ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();

                    int i = AttrHelper.vp2px(150f, context);
                    decodingOpts.desiredSize = new Size(i, i);
                    ImageSource imageSource = ImageSource.create(filedesc, null);
                    PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);
                    viewHolder.image.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
                    viewHolder.image.setPixelMap(pixelMap);
                    pixelMap.release();
                    imageSource.release();
                } catch (Exception e) {
                    viewHolder.image.setPixelMap(null);
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    ImageHolder getViewHolder(Component component) {
        return new ImageHolder(component);
    }

    public static class ImageHolder extends BaseListProvider.BaseViewHolder {
        final Image image;
        final Image selector;
        final Text text1;
        final Text text2;

        public ImageHolder(Component component) {
            super(component);
            selector = (Image) component.findComponentById(ResourceTable.Id_selector);
            image = (Image) component.findComponentById(ResourceTable.Id_image1);
            text1 = (Text) component.findComponentById(ResourceTable.Id_text);
            text2 = (Text) component.findComponentById(ResourceTable.Id_text2);
        }
    }
}
