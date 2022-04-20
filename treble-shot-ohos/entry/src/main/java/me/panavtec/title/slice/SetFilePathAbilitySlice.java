package me.panavtec.title.slice;

import me.panavtec.bean.FristFolderData;
import me.panavtec.bean.PageData;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.FolderPro;
import me.panavtec.title.hmadapter.FristFolderPro;
import me.panavtec.title.hmadapter.StringListPro;
import me.panavtec.title.hminterface.FolderImageClick;
import me.panavtec.title.hminterface.OwnClick;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.VectorElement;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SetFilePathAbilitySlice extends AbilitySlice implements Component.ClickedListener,
        Text.TextObserver{

    HmSharedPerfrence perfrence;
    public boolean pageFlage = false;
    List<FristFolderData> dataList1 = new ArrayList<>();
    List<FristFolderData> dataList2 = new ArrayList<>();
    List<FristFolderData> dataList3 = new ArrayList<>();
    FristFolderPro pro1,pro2,pro3;
//    public SliceToMainI sliceToMainI;
    DirectionalLayout tabPage, tabList, textLayout;

    int type;
    ListContainer lc_list;
    public FolderPro pro;
    PageData currentPageData;
    Text tx_folder,text_home;
    DirectionalLayout tx_empt;
    ListContainer item_list;
    StringListPro stringListPro;

    Text text_helloworld,gz;
    ListContainer dl1,dl2,dl3;
    String filepath;
    Text txt_zero;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        System.out.println("传递的参数>>>>>>>>"+filepath);
        super.setUIContent(ResourceTable.Layout_layout_file_explorer_setting);
        filepath = intent.getStringParam("path");
        initDataAndView();
        initToolBar();

    }
    public void initDataAndView(){
        perfrence = HmSharedPerfrence.getInstance(this);
        //bar
        ((Text)findComponentById(ResourceTable.Id_txt_zero)).setText("存储路径");
        findComponentById(ResourceTable.Id_back_text).setVisibility(Component.HIDE);
        Image backHeader = (Image)findComponentById(ResourceTable.Id_back_header);
        backHeader.setScale(0.8f,0.8f);
        VectorElement element = new VectorElement(getContext(),ResourceTable.Graphic_ic_close_white_24dp);
        backHeader.setImageElement(element);

        backHeader.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                onBackPressed();
            }
        });
        findComponentById(ResourceTable.Id_confirm).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                intent.setParam("path",filepath);
                setResult(intent);
                onBackPressed();
            }
        });
        //
        dataList1 = loadType("type1",1);
        dataList2 = loadType("type2",2);
        dataList3 = loadType("type3",3);
        dl1 = (ListContainer) findComponentById(ResourceTable.Id_dl1);
        dl2 = (ListContainer) findComponentById(ResourceTable.Id_dl2);
        dl3 = (ListContainer) findComponentById(ResourceTable.Id_dl3);
        tabPage = (DirectionalLayout) findComponentById(ResourceTable.Id_tab_page);
        tabList = (DirectionalLayout) findComponentById(ResourceTable.Id_tab_list);
        textLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_text_layout);
        pro1 = new FristFolderPro(dataList1,this,1);
        pro2 = new FristFolderPro(dataList2,this,2);
        pro3 = new FristFolderPro(dataList3,this,3);
        if (filepath!=null&&filepath.length()>0){
            tabList.setVisibility(Component.VISIBLE);
            tabPage.setVisibility(Component.HIDE);
        }else {
            filepath = "/mnt";
            tabList.setVisibility(Component.VISIBLE);
            tabPage.setVisibility(Component.HIDE);
        }

        dl1.setItemProvider(pro1);
        dl2.setItemProvider(pro2);
        dl3.setItemProvider(pro3);
        setListViewHeight(dl1);
        setListViewHeight(dl2);
        setListViewHeight(dl3);
        text_helloworld = (Text) findComponentById(ResourceTable.Id_text_helloworld);
        text_home = (Text) findComponentById(ResourceTable.Id_text_home);
        item_list = (ListContainer) findComponentById(ResourceTable.Id_item_list);
//        text_helloworld.setText( perfrence.getParam("type1"));

        pro1.setOwnClick(ownClick1);
        pro2.setOwnClick(ownClick2);
        pro3.setOwnClick(ownClick3);
        gz = (Text) findComponentById(ResourceTable.Id_gz);
        gz.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                startNewAbFroResult("/mnt",1);
            }
        });
        dl1.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                startNewAbFroResult(pro1.dataList.get(i).getPath(),1);
            }
        });
        dl2.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                startNewAbFroResult(pro2.dataList.get(i).getPath(),2);
            }
        });
        dl3.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                startNewAbFroResult(pro3.dataList.get(i).getPath(),3);
            }
        });
//        dl2.setItemLongClickedListener(dl2LongClick);
        //二级页面
        lc_list = (ListContainer) findComponentById(ResourceTable.Id_lc_list);
        tx_empt = (DirectionalLayout) findComponentById(ResourceTable.Id_tx_empt);
        tx_folder = (Text) findComponentById(ResourceTable.Id_tx_folder);
        text_home.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (pageFlage){//转回一级页面
                    pageFlage = false;
                    item_list.setVisibility(Component.HIDE);
                    tabList.setVisibility(Component.HIDE);
                    tabPage.setVisibility(Component.VISIBLE);
//                        pathList.clear();
//                    loadWithWords("");
                }else{

                }
            }
        });
        startNewAbFroResult(filepath,2001);
    }
    public List<FristFolderData> loadType(String name,int type){
        List<FristFolderData> dataList = new ArrayList<>();
        String[] strs = perfrence.getParam(name).split("&&");
        if(strs!=null&&strs.length>0){
            for (int i = 0;i<strs.length;i++) {
                FristFolderData folderData = new FristFolderData();
                File file = new File(strs[i]);
                if (file.listFiles() != null && file.listFiles().length > 0) {
                    folderData.childCount = file.listFiles().length;
                } else {
                    folderData.childCount = 0;
                }
                folderData.path = file.getAbsolutePath();
                folderData.showName = file.getName();
                folderData.isCanRead = Files.isReadable(Paths.get(strs[i]));
                folderData.isCanWrite = Files.isWritable(Paths.get(strs[i]));
//                folderData.isSelect = pathList.contains(file.getAbsolutePath());
                switch (type) {
                    case 1:
                        folderData.type = "存储";
                        break;
                    case 2:
                        folderData.type = "文件夹";
                        break;
                    case 3:
                        folderData.type = folderData.childCount + "个文件";
                        break;
                }
                dataList.add(folderData);
            }
        }
        return dataList;
    }
    public void setListViewHeight(ListContainer listView) {
        // 获取ListView对应的Adapter
        BaseItemProvider listAdapter = listView.getItemProvider();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            Component listItem = listAdapter.getComponent(i, null, listView);
//            listItem.measure(0, 0); // 计算子项View 的宽高
            listView.setLayoutConfig(new ComponentContainer.LayoutConfig());
            totalHeight += listItem.getHeight(); // 统计所有子项的总高度
        }

        ComponentContainer.LayoutConfig params = listView.getLayoutConfig();
        params.width = ComponentContainer.LayoutConfig.MATCH_PARENT;
        params.height = totalHeight + (listView.getHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutConfig(params);
    }
    public void setAddViewHeight(ListContainer listView){
        BaseItemProvider listAdapter = listView.getItemProvider();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            Component listItem = listAdapter.getComponent(i, null, listView);
//            listItem.measure(0, 0); // 计算子项View 的宽高
            listView.setLayoutConfig(new ComponentContainer.LayoutConfig());
            totalHeight += listItem.getHeight(); // 统计所有子项的总高度
        }

        ComponentContainer.LayoutConfig params = listView.getLayoutConfig();
        params.width = ComponentContainer.LayoutConfig.MATCH_PARENT;
        if (listAdapter.getCount()>0) {
            params.height = listAdapter.getComponent(0, null, listView).getHeight() * listAdapter.getCount();
        }else {
            params.height = 0;
        }
        listView.setLayoutConfig(params);
    }
    public void startNewAbFroResult(String path,int requestCode){
//        Toast.show(this,"路径"+path);
        System.out.println("路径>>>>>>>>>>>>"+path);
        filepath = path;
        type = requestCode;
        pageFlage = true;
        tabPage.setVisibility(Component.HIDE);
        tabList.setVisibility(Component.VISIBLE);
        pro = new FolderPro(filepath,this);
        pro.setFolderImageClick(folderImageClick);
        lc_list.setItemProvider(pro);
        setEmptView(pro);
        tx_folder.setText(getParentPath(pro.path));
        lc_list.setItemClickedListener(itemClick);
//        lc_list.setItemLongClickedListener(lcListlongClick);
    }
    final OwnClick ownClick1 = new OwnClick() {
        @Override
        public void doClick(int type, int position, FristFolderData deal) {
            switch (position){
                case 0:
                    perfrence.apendParam("type2",deal.getPath());
                    dataList2 = loadType("type2",2);
                    pro2 = new FristFolderPro(dataList2,SetFilePathAbilitySlice.this,2);
                    dl2.setItemProvider(pro2);
                    pro2.setOwnClick(ownClick2);
                    setAddViewHeight(dl2);
                    break;
                case 1:
                    showInputDialog(deal,type);
                    break;
                case 2:
                    if (deal.isCanWrite()){
                        dataList1.remove(deal);
                        pro1 = new FristFolderPro(dataList1,SetFilePathAbilitySlice.this,1);
                        dl1.setItemProvider(pro1);
                        setListViewHeight(dl1);
                        String str2 = perfrence.getParam("type1").replace(deal.getPath(),"");
                        String newStr2 = str2.replace("&&&&","&&");
                        perfrence.saveParam("type1",newStr2);
                    }else{
                        new ToastDialog(SetFilePathAbilitySlice.this).setText("文件禁止操作").setDuration(2000).show();
                    }
                    break;
            }
        }
    };
    final OwnClick ownClick2 = new OwnClick() {
        @Override
        public void doClick(int type, int position, FristFolderData deal) {
            switch (position){
                case 0:
                    perfrence.apendParam("type3",deal.getPath());
                    dataList3 = loadType("type3",3);
                    pro3 = new FristFolderPro(dataList3,SetFilePathAbilitySlice.this,3);
                    dl3.setItemProvider(pro3);
                    setAddViewHeight(dl3);
                    pro3.setOwnClick(ownClick3);
                    break;
                case 1:
                    showInputDialog(deal,type);
                    break;
                case 2:
                    perfrence.cutParam("type2",deal.getPath());
                    dataList2 = loadType("type2",2);
                    pro2 = new FristFolderPro(dataList2,SetFilePathAbilitySlice.this,2);
                    dl2.setItemProvider(pro2);
                    setAddViewHeight(dl2);
                    pro2.setOwnClick(ownClick2);
                    break;
            }
        }
    };
    final OwnClick ownClick3 = new OwnClick() {
        @Override
        public void doClick(int type, int position, FristFolderData deal) {
            switch (position) {
                case 0:
                    perfrence.cutParam("type3",deal.getPath());
                    new ToastDialog(SetFilePathAbilitySlice.this).setText(perfrence.getParam("type3")).setDuration(2000).show();
                    System.out.println("处理之后的type3>>>>>>>>>>>"+perfrence.getParam("type3"));
                    dataList3 = loadType("type3", 3);
                    pro3 = new FristFolderPro(dataList3, SetFilePathAbilitySlice.this, 3);
                    dl3.setItemProvider(pro3);
                    setAddViewHeight(dl3);
                    pro3.setOwnClick(ownClick3);
                    break;
                case 1:
                    showInputDialog(deal, type);
                    break;
                case 2:
                    if (deal.isCanWrite()) {
                        dataList2.remove(deal);
                        pro2 = new FristFolderPro(dataList2, SetFilePathAbilitySlice.this, 2);
                        dl2.setItemProvider(pro2);
                        setListViewHeight(dl2);
                        String str2 = perfrence.getParam("type2").replace(deal.getPath(), "");
                        String newStr2 = str2.replace("&&&&", "&&");
                        perfrence.saveParam("type2", newStr2);
                    } else {
                        new ToastDialog(SetFilePathAbilitySlice.this).setText("文件禁止操作").setDuration(2000).show();
                    }
                    break;
            }
        }
    };
//    ListContainer.ItemLongClickedListener dl2LongClick = new ListContainer.ItemLongClickedListener() {
//        @Override
//        public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
//            dataList2.get(i).setSelect(!dataList2.get(i).isSelect);
//            if (pathList.contains(dataList2.get(i).getPath())) {
//                pathList.remove(dataList2.get(i).getPath());
//            } else {
//                pathList.add(dataList2.get(i).getPath());
//            }
//            pro2.notifyDataChanged();
//            setAddViewHeight(dl2);
//            sliceToMainI.slectNumAndPathProd(pathList);
//            return false;
//        }
//    };
    public void showInputDialog(FristFolderData deal,int type){
        CommonDialog dlg = new CommonDialog(this);
        Component layout = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_dialog_input_rename, null, true);
//        dlg.setTransparent(true);
        TextField textField = (TextField) layout.findComponentById(ResourceTable.Id_textf_rename);
        textField.setText(deal.showName);
        layout.findComponentById(ResourceTable.Id_but_close).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                dlg.destroy();
            }
        });
        layout.findComponentById(ResourceTable.Id_but_rename).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (textField.getText().length()>0&&!textField.getText().equals(deal.getShowName())) {
                    reNameFolder(deal,textField.getText().trim(),type);
                }else {
                    new ToastDialog(SetFilePathAbilitySlice.this).setText("请输入新名字").setDuration(2000).show();
                }
            }
        });
        dlg.setContentCustomComponent(layout);
        dlg.show();
    }
    //二级页面方法
    public String  getParentPath(String rootPath){
        File file = new File(rootPath);
        return file.getAbsoluteFile().toString();
    }
    final FolderImageClick folderImageClick = new FolderImageClick() {
        @Override
        public void doClick(int position, PageData deal) {
            switch (position){
                case 0:
                    if (deal.isFloder){
                        perfrence.apendParam("type2",deal.getFilePath());
                        new ToastDialog(SetFilePathAbilitySlice.this).setText("创建文件夹快捷方式成功").setDuration(2000).show();
                    }else {
                        new ToastDialog(SetFilePathAbilitySlice.this).setText("请自行打开").setDuration(2000).show();
                    }
                    break;
                case 1:
                    showInputDialog2(deal,type);
                    break;
                case 2:
                    if (deal.isCanWrite){
                        File file = new File(deal.getFilePath());
                        if (file.exists()){
                            if (file.delete()){
                                perfrence.cutParam("type"+type,deal.getFilePath());
                                new ToastDialog(SetFilePathAbilitySlice.this).setText("文件删除成功").setDuration(2000).show();
                                pro = new FolderPro(file.getParent(),SetFilePathAbilitySlice.this);//刷新父页面
                                lc_list.setItemProvider(pro);
                                pro.setFolderImageClick(folderImageClick);
                                setEmptView(pro);
                                tx_folder.setText(getParentPath(pro.path));
                                lc_list.setItemClickedListener(itemClick);
//                                lc_list.setItemLongClickedListener(lcListlongClick);
                            }else {
                                new ToastDialog(SetFilePathAbilitySlice.this).setText("文件删除失败").setDuration(2000).show();
                            }
                        }
                    }else{
                        new ToastDialog(SetFilePathAbilitySlice.this).setText("文件禁止操作").setDuration(2000).show();
                    }
                    break;
            }
        }
    };
    public void showInputDialog2(PageData deal,int type){
        CommonDialog dlg = new CommonDialog(SetFilePathAbilitySlice.this);
        Component layout = LayoutScatter.getInstance(SetFilePathAbilitySlice.this).parse(ResourceTable.Layout_dialog_input_rename, null, true);
//        dlg.setTransparent(true);
        TextField textField = (TextField) layout.findComponentById(ResourceTable.Id_textf_rename);
        textField.setText(deal.getFileName());
        layout.findComponentById(ResourceTable.Id_but_close).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                dlg.destroy();
            }
        });
        layout.findComponentById(ResourceTable.Id_but_rename).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (textField.getText().length()>0&&!textField.getText().equals(deal.getFileName())) {
                    reNameFolder2(deal,textField.getText().trim(),type,dlg);
                }else {
                    new ToastDialog(SetFilePathAbilitySlice.this).setText("请输入新名字").setDuration(2000).show();
                }
            }
        });
        dlg.setContentCustomComponent(layout);
        dlg.show();
    }
    public void setEmptView(FolderPro folderPro){
        stringListPro = new StringListPro(this,filepath);
        item_list.setItemProvider(stringListPro);
//        text_home.setVisibility(Component.HIDE);
        item_list.setVisibility(Component.VISIBLE);
        item_list.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                startNewAbFroResult(stringListPro.getIndexPath(stringListPro.fileList.size()-1-i),4);
            }
        });
        if (folderPro.dataList==null||folderPro.dataList.size()==0){
            tx_empt.setVisibility(Component.VISIBLE);
            lc_list.setVisibility(Component.HIDE);
        }else {
            tx_empt.setVisibility(Component.HIDE);
            lc_list.setVisibility(Component.VISIBLE);

        }
    }
    public void reNameFolder2(PageData deal,String newName,int type,CommonDialog dig){
        File oldFile = new File(deal.getFilePath());
        File newFile = new File(deal.getFilePath().replace(deal.getFileName(),newName));
        if (deal.isCanWrite){
            if(oldFile.renameTo(newFile)) {
                switch (type){
                    case 1:
                        perfrence.replaceParam("type1",deal.getFilePath(),deal.getFilePath().replace(deal.getFileName(),newName));
                        break;
                    case 2:
                        perfrence.replaceParam("type2",deal.getFilePath(),deal.getFilePath().replace(deal.getFileName(),newName));
                        break;
                    case 3:
                        perfrence.replaceParam("type3",deal.getFilePath(),deal.getFilePath().replace(deal.getFileName(),newName));
                        break;

                }
                dig.destroy();
                new ToastDialog(this).setText("文件重命名成功").setDuration(2000).show();
                //重置adapter
                pro = new FolderPro(pro.path, SetFilePathAbilitySlice.this);
                lc_list.setItemProvider(pro);
                setEmptView(pro);
                pro.setFolderImageClick(folderImageClick);
                tx_folder.setText(getParentPath(pro.path));
                lc_list.setItemClickedListener(itemClick);
//                lc_list.setItemLongClickedListener(lcListlongClick);
            } else {
                new ToastDialog(SetFilePathAbilitySlice.this).setText("文件重命名失败").setDuration(2000).show();
            }
        }else {
            new ToastDialog(SetFilePathAbilitySlice.this).setText("文件禁止操作").setDuration(2000).show();
        }

    }
    final ListContainer.ItemClickedListener itemClick = new ListContainer.ItemClickedListener() {
        @Override
        public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
            if (pro.dataList.get(i).isFloder) {
                if (pro.dataList.get(i).isCanRead) {
                    currentPageData = pro.dataList.get(i);
                    filepath = currentPageData.filePath;
                    pro = new FolderPro(currentPageData.filePath, SetFilePathAbilitySlice.this);
                    lc_list.setItemProvider(pro);
                    setEmptView(pro);
                    pro.setFolderImageClick(folderImageClick);
                    tx_folder.setText(getParentPath(pro.path));
                    lc_list.setItemClickedListener(itemClick);
//                    lc_list.setItemLongClickedListener(lcListlongClick);
                } else {
                    new ToastDialog(SetFilePathAbilitySlice.this).setText("文件不可读取").setDuration(2000).show();
                }
            }else {
                new ToastDialog(SetFilePathAbilitySlice.this).setText("文件请自行打开").setDuration(2000).show();

            }
        }
    };
//    ListContainer.ItemLongClickedListener lcListlongClick = new ListContainer.ItemLongClickedListener() {
//        @Override
//        public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
//            pro.setItemSelect(i);
//            if (pathList.contains(pro.getSelectString(i))) {
//                pathList.remove(pro.getSelectString(i));
//            } else {
//                pathList.add(pro.getSelectString(i));
//            }
//            sliceToMainI.slectNumAndPathProd(pathList);
//            return false;
//        }
//    };
public void reNameFolder(FristFolderData deal,String newName,int type){
    File oldFile = new File(deal.getPath());
    File newFile = new File(deal.getPath().replace(deal.getShowName(),newName));
    if (deal.isCanWrite){
        if(oldFile.renameTo(newFile)) {
            switch (type){
                case 1:
                    perfrence.replaceParam("type1",deal.getPath(),deal.getPath().replace(deal.getShowName(),newName));
                    dataList1 = loadType("type1",1);
                    pro1 = new FristFolderPro(dataList1,this,1);
                    dl1.setItemProvider(pro1);
                    setAddViewHeight(dl1);
                    pro1.setOwnClick(ownClick1);
                    break;
                case 2:
                    perfrence.replaceParam("type2",deal.getPath(),deal.getPath().replace(deal.getShowName(),newName));
                    dataList2 = loadType("type2",2);
                    pro2 = new FristFolderPro(dataList2,this,2);
                    dl2.setItemProvider(pro2);
                    setAddViewHeight(dl2);
                    pro2.setOwnClick(ownClick2);
                    break;
                case 3:
                    perfrence.replaceParam("type3",deal.getPath(),deal.getPath().replace(deal.getShowName(),newName));
                    dataList1 = loadType("type3",3);
                    pro3 = new FristFolderPro(dataList3,this,3);
                    dl3.setItemProvider(pro3);
                    setAddViewHeight(dl3);
                    pro3.setOwnClick(ownClick3);
                    break;

            }
            new ToastDialog(this).setText("文件重命名成功").setDuration(2000).show();

        } else {
            new ToastDialog(this).setText("文件重命名失败").setDuration(2000).show();
        }
    }else {
        new ToastDialog(this).setText("文件禁止操作").setDuration(2000).show();
    }
}
    private void initToolBar() {
        Component searchContainer = findComponentById(ResourceTable.Id_search_container);
        TextField searchBoxControl = (TextField) findComponentById(ResourceTable.Id_search_box_control);
        Image closeIcon = (Image) findComponentById(ResourceTable.Id_close_icon);
        txt_zero = (Text) findComponentById(ResourceTable.Id_txt_zero);
        Image searchIcon1 = (Image) findComponentById(ResourceTable.Id_search_icon1);
        Image moreMain = (Image) findComponentById(ResourceTable.Id_more_main);
        searchIcon1.setClickedListener(component -> searchContainer.setVisibility(Component.VISIBLE));
        closeIcon.setClickedListener(component -> {
            searchContainer.setVisibility(Component.HIDE);
            searchBoxControl.setText("");
            searchBoxControl.clearFocus();
//            reSetPageByCloseIcon(currentPage);
            if (pageFlage) {
                pro.doSearchInSelf("",true);
            } else {
                loadWithWords("");
            }


        });

        moreMain.setClickedListener(component -> Toast.show(SetFilePathAbilitySlice.this, "点击了更多"));
        searchBoxControl.addTextObserver(this);
    }

    @Override
    public void onClick(Component component) {

    }

    @Override
    public void onTextUpdated(String s, int i, int i1, int i2) {
        doCase1(s);
    }
    public void loadWithWords(String str){
        dataList1.clear();
        dataList1.addAll(loadType("type1",2));
        for (int i = dataList1.size()-1;i>=0;i--){
            System.out.println(dataList1.get(i).getShowName()+"===="+str);
            if (!dataList1.get(i).getShowName().contains(str)){
                dataList1.remove(i);
            }
        }
        pro1.notifyDataChanged();
        setAddViewHeight(dl1);
        dataList2.clear();
        dataList2.addAll(loadType("type2",2));
        for (int i = dataList2.size()-1;i>=0;i--){
            System.out.println(dataList2.get(i).getShowName()+"===="+str);
            if (!dataList2.get(i).getShowName().contains(str)){
                dataList2.remove(i);
            }
        }
        pro2.notifyDataChanged();
        setAddViewHeight(dl2);
        dataList3.clear();
        dataList3.addAll(loadType("type3",3));
        for (int i = dataList3.size()-1;i>=0;i--){
            System.out.println(dataList3.get(i).getShowName()+"===="+str);
            if (!dataList3.get(i).getShowName().contains(str)){
                dataList3.remove(i);
            }
        }
        pro3.notifyDataChanged();
        setAddViewHeight(dl3);
    }
    public void doCase1(String str) {
        if (pageFlage) {
            pro.doSearchInSelf(str,true);
        } else {
            loadWithWords(str);
        }
    }
}
