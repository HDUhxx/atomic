package me.panavtec.title.fraction;

import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.SliceToMainI;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.PixelMapElement;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class BaseListFraction extends BaseFraction {
    private ListContainer listContainer;
    private DirectionalLayout emptyView;
    private boolean isLongClickState = false;

    protected final SliceToMainI sliceToMainI;

    public BaseListFraction(SliceToMainI sliceToMainI) {
        this.sliceToMainI = sliceToMainI;
    }

    public abstract List<? extends BaseSelectEntity> getSelectedList();

    public abstract void onRefresh();

    public abstract void onLongClickChanged(boolean longClickState);

    public abstract void onTextSeared(String s);


    public boolean isLongClickState() {
        return isLongClickState;
    }

    public void setLongClickState(boolean longClickState) {
        isLongClickState = longClickState;
        onLongClickChanged(isLongClickState);
    }


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        initEmpty();
    }

    public void initEmpty() {
        if (viewComponent == null) return;
        emptyView = (DirectionalLayout) viewComponent.findComponentById(ResourceTable.Id_emptyView1);
        listContainer = (ListContainer) viewComponent.findComponentById(ResourceTable.Id_listText1);
    }

    public void setEmptyView(boolean empty, int textId, int imageId) {
        if (emptyView != null) {
            try {
                ResourceManager resourceManager = getMyContext().getResourceManager();
                String string = resourceManager.getElement(textId).getString();
                Text txt = (Text) emptyView.getComponentAt(1);
                txt.setText(string);

                Image image = (Image) emptyView.getComponentAt(0);
//                image.setImageElement(new PixelMapElement(getPixelMap(imageId)));

            } catch (IOException | NotExistException e) {
                e.printStackTrace();
            } catch (WrongTypeException e) {
                e.printStackTrace();
            }
        }
        setVisible(empty);
    }

    public void setVisible(boolean empty) {
        if (listContainer != null) listContainer.setVisibility(!empty ? Component.VISIBLE : Component.HIDE);
        if (emptyView != null) emptyView.setVisibility(empty ? Component.VISIBLE : Component.HIDE);
    }

    /**
     * 通过资源ID获取位图对象
     **/
    private PixelMap getPixelMap(int drawableId) {
        InputStream drawableInputStream = null;
        try {
            Resource resource = getContext().getResourceManager().getResource(drawableId);
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(drawableInputStream, sourceOptions);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(0, 0);
            decodingOptions.desiredRegion = new ohos.media.image.common.Rect(0, 0, 0, 0);
            decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
            return imageSource.createPixelmap(decodingOptions);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (drawableInputStream != null){
                    drawableInputStream.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ListContainer getListContainer() {
        return listContainer;
    }

}
