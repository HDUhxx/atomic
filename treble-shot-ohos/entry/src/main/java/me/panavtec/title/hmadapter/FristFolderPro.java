package me.panavtec.title.hmadapter;

import me.panavtec.bean.FristFolderData;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.OwnClick;
import ohos.agp.components.*;
import ohos.agp.components.element.VectorElement;
import ohos.agp.window.dialog.ListDialog;
import ohos.app.Context;

import java.util.List;
import java.util.Optional;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class FristFolderPro extends BaseItemProvider {

    public final Context context;
    public List<FristFolderData> dataList;
    public final int type;
    public OwnClick ownClick;
    public FristFolderPro(List<FristFolderData> dataList, Context context, int type) {
        this.context = context;
        this.type = type;
        this.dataList = dataList;

    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_show_folder, null, false);
        } else {
            cpt = component;
        }
        Text tx1 = (Text) cpt.findComponentById(ResourceTable.Id_tx1);
        Text tx2 = (Text) cpt.findComponentById(ResourceTable.Id_tx2);
        Image imageTop = (Image) cpt.findComponentById(ResourceTable.Id_image_top);
        Image selector = (Image) cpt.findComponentById(ResourceTable.Id_selector);
        Image image = (Image) cpt.findComponentById(ResourceTable.Id_image);
        selector.setVisibility(dataList.get(i).isSelect? Component.VISIBLE : Component.HIDE);
        setBitmap(image,type);
        FristFolderData deal = dataList.get(i);
        isSavePath(deal,image);
        tx1.setText(deal.getShowName());//+"   是否可读："+deal.isCanRead);
        tx2.setText(deal.type);
        imageTop.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showDialg2(context,type,deal);
            }
        });
        return cpt;
    }

    public void setOwnClick(OwnClick ownClick) {
        this.ownClick = ownClick;
    }

   public void showDialg2(Context context1,int type,FristFolderData deal){
            ListDialog dialog = new ListDialog(context1);
            dialog.setSize(MATCH_CONTENT, 600);
            dialog.setAutoClosable(true);
            dialog.setCornerRadius(50);
            dialog.setItems(getStringList(type));
            dialog.show();
            ListContainer container = (ListContainer) dialog.getListContainer();
            container.setWidth(MATCH_CONTENT);
            container.setHeight(400);
            container.setMarginsLeftAndRight(400, 200);
            container.setMarginsTopAndBottom(100, 200);
//            container.setItemProvider(new DialogItemPro( context1));
            container.setItemClickedListener(new ListContainer.ItemClickedListener() {
                @Override
                public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
//                    new ToastDialog(context1).setText("点击按钮").setDuration(2000).back_header();
//                    doClick(type,i,context1,deal);
                    ownClick.doClick(type,i,deal);
                    dialog.destroy();
                }
            });
    }
    public  String []  getStringList(int type){
        String []items;
        switch (type){
            case 1:
                items  = new String[]{"创建文件夹快捷方式", "重命名", "删除"};
                break;
            case 2:
                items = new String[]{"创建快捷方式", "重命名", "取消文件夹快捷方式"};
                break;
            case 3:
                items = new String[]{"删除快捷方式", "重命名", "删除"};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return items;
    }
    public void setBitmap(Image image,int dataType){
        switch (dataType){
            case 1:
                image.setImageElement(new VectorElement(context,ResourceTable.Layout_ic_save_white_24dp));
                break;
            case 2:
                break;
            case 3:
                image.setImageElement(new VectorElement(context,ResourceTable.Layout_ic_bookmark_white_24dp));
                break;
        }
    }
//    public void doClick(int type,int position,Context context,FristFolderData dealData){
//            switch (type){
//                case 1:
//                    switch (position){
//                        case 0:
//                                String oldurl =perfrence.getParam("type1");
//                                new ToastDialog(context).setText(oldurl+"&&"+dealData.getPath()).setDuration(5000).back_header();
//                                perfrence.saveParam("typ1",oldurl+"&&"+dealData.getPath());
//                                notifyDataInvalidated();
//                                break;
//                        case 1:
//                            break;
//                        case 2:
//                            break;
//                    }
//        }
//    }
    public void isSavePath(FristFolderData folderData,Image image){
        if (folderData.getPath().equals("/data/data/com.loser007/files")){
            folderData.setShowName("收到的文件");
            image.setImageElement(new VectorElement(context,ResourceTable.Layout_ic_trebleshot_original_white_128dp));
        }
    }
}
