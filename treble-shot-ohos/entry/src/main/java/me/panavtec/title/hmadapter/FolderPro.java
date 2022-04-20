package me.panavtec.title.hmadapter;

import me.panavtec.bean.PageData;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.FolderImageClick;
import me.panavtec.title.hmutils.Toast;
import ohos.agp.components.*;
import ohos.agp.components.element.VectorElement;
import ohos.agp.window.dialog.ListDialog;
import ohos.app.Context;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;

public class FolderPro extends BaseItemProvider {

    public final String path;
    public final Context context;
    public FolderImageClick folderImageClick;
    public final List<PageData> dataList = new ArrayList<>();
    public final List<String> pathList;
    public FolderPro(String path, Context context) {
        this.path = path;
        this.context = context;
        this.pathList = new ArrayList<>();
        getFolderData(path);
        removeFile();

    }
    public FolderPro(String path, Context context,List<String> pathList ) {
        this.path = path;
        this.context = context;
        this.pathList = pathList;
        getFolderData(path);
    }

    public void setFolderImageClick(FolderImageClick folderImageClick) {
        this.folderImageClick = folderImageClick;
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
        image.setImageElement(new VectorElement(context,dataList.get(i).isFloder?ResourceTable.Layout_ic_folder_white_24dp_os:ResourceTable.Layout_ic_photo_white_24dp));
        if (dataList.get(i).getFileName().endsWith(".jpg")){
            try {
                ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
                srcOpts.formatHint = "image/png";
                System.out.println("是jpg图片"+new File("/data/data/com.loser007/MainAbility/preferences/TShot DEBUG/tp1.jpg").length());
                ImageSource c = ImageSource.create(dataList.get(i).getFilePath(), srcOpts);
                PixelMap pixelMapNoOptions = c.createPixelmap(null);
                image.setPixelMap(pixelMapNoOptions);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

        }
        selector.setVisibility(dataList.get(i).isSelect? Component.VISIBLE : Component.HIDE);
        imageTop.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
//                ((FileListAbilitySlice)context).presentForResult(new DialogAbility(),new Intent(),2001);
                showDialg2(context,dataList.get(i));
            }
        });
        tx1.setText(dataList.get(i).fileName);//+"   是否可读："+dataList.get(i).isCanRead);
        tx2.setText(dataList.get(i).isFloder?dataList.get(i).filePath:dataList.get(i).type);

        return cpt;
//        return null;
    }


    private void getFolderData(String path){
        File file = new File(path);
        File[] subFile = file.listFiles();
        if (subFile!=null&&subFile.length>0) {
            for (int i = 0; i < subFile.length; i++) {
//                if (Files.isWritable(Paths.get(subFile[i].toString()))){
                PageData pageData = new PageData();
                pageData.setFileName(subFile[i].getName());
                pageData.filePath = subFile[i].getPath();
                pageData.isCanRead = Files.isReadable(Paths.get(subFile[i].toString()));
                pageData.isCanWrite = Files.isWritable(Paths.get(subFile[i].toString()));
                pageData.isSelect = pathList.contains(subFile[i].getPath());
                if (subFile[i].isDirectory()){
                    pageData.isFloder = true;
                    pageData.type = "文件夹";
                }else{
                    pageData.isFloder = false;
//                    pageData.type = subFile[i].getName().split(".")[subFile[i].getName().split(".").length-1];
                    pageData.type = subFile[i].length()/1024+"KB";
                }
                dataList.add(pageData);
            }
//            else{
//                    System.out.println("不可读写文件>>>>>>>>>>."+subFile[i].getAbsolutePath());
//                }
//            }
        }
    }
    public void setEmptView(){

    }
    public void showDialg2(Context context1,PageData pageData){
        ListDialog dialog = new ListDialog(context1);
        dialog.setSize(MATCH_CONTENT, 600);
        dialog.setAutoClosable(true);
        dialog.setCornerRadius(50);
        dialog.setItems(getStringList(pageData));
        dialog.show();
        ListContainer container = (ListContainer) dialog.getListContainer();
        container.setWidth(MATCH_CONTENT);
        container.setHeight(400);
        container.setMarginsLeftAndRight(400, 200);
        container.setMarginsTopAndBottom(100, 100);
        container.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                folderImageClick.doClick(i,pageData);
                dialog.destroy();
            }
        });
    }
    public  String []  getStringList(PageData pageData){
        String []items;
        if (pageData.isFloder){
            items  = new String[]{"创建文件夹快捷方式", "重命名", "删除"};
        }else {
            items  = new String[]{"打开", "重命名", "删除"};
        }
        return items;
    }

    /**
     *
     * @param str  传入查询的文字参数
     * @param isSetingAb    是否是设置界面，只需要文件夹
     */
    public void doSearchInSelf(String str,boolean isSetingAb){
        dataList.clear();
        getFolderData(path);
        if (isSetingAb){
            removeFile();
        }
        for (int i = dataList.size()-1;i>=0;i--){
            if (!dataList.get(i).fileName.contains(str)){
                dataList.remove(i);
            }
        }
        Toast.show(context,dataList.size()+"M");
        this.notifyDataChanged();
    }
    public String getSelectString(int positon){
        return dataList.get(positon).getFilePath();
    }
    public void setItemSelect(int position){
        dataList.get(position).isSelect = !dataList.get(position).isSelect;
        notifyDataChanged();
    }
    public void removeFile(){
        for (int i = dataList.size()-1;i>=0;i--){
            if (!dataList.get(i).isFloder){
                dataList.remove(i);
            }
        }
    }
}
