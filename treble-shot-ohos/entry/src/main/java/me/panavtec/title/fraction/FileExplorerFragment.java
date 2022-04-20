package me.panavtec.title.fraction;

import me.panavtec.bean.FristFolderData;
import me.panavtec.bean.PageData;
import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.FolderPro;
import me.panavtec.title.hmadapter.FristFolderPro;
import me.panavtec.title.hmadapter.StringListPro;
import me.panavtec.title.hminterface.FolderImageClick;
import me.panavtec.title.hminterface.OwnClick;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileExplorerFragment extends BaseListFraction {

    public static final String EXTRA_FILE_PATH = "filePath";

    final HmSharedPerfrence perfrence;
    final Context context;
    public boolean pageFlage = false;

    public FileExplorerFragment(Context context, SliceToMainI sliceToMainI) {
        super(sliceToMainI);
        this.context = context;
        this.sliceToMainI = sliceToMainI;
//        perfrence = new HmSharedPerfrence(context);
        perfrence = HmSharedPerfrence.getInstance(context);
    }

    List<FristFolderData> dataList1 = new ArrayList<>();
    List<FristFolderData> dataList2 = new ArrayList<>();
    List<FristFolderData> dataList3 = new ArrayList<>();
    FristFolderPro pro1, pro2, pro3;
    public final SliceToMainI sliceToMainI;

    Text text_helloworld, gz;
    ListContainer dl1, dl2, dl3;
    DirectionalLayout tab_page, tab_list, text_layout;
    String indexPath = "/mnt";
    int type;
    ListContainer lc_list;
    public FolderPro pro;
    PageData currentPageData;
    Text tx_folder, text_home;
    DirectionalLayout tx_empt;
    ListContainer item_list;
    StringListPro stringListPro;

    public final List<String> pathList = new ArrayList<>();//发送的文件路径集合

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        Component view = scatter.parse(ResourceTable.Layout_layout_file_explorer, container, false);
        initDataAndView(view);
        return view;
    }

    @Override
    public int setXMLId() {
        return 0;
    }

    public void initDataAndView(Component component) {
        dataList1 = loadType("type1", 1);
        dataList2 = loadType("type2", 2);
        dataList3 = loadType("type3", 3);
        dl1 = (ListContainer) component.findComponentById(ResourceTable.Id_dl1);
        dl2 = (ListContainer) component.findComponentById(ResourceTable.Id_dl2);
        dl3 = (ListContainer) component.findComponentById(ResourceTable.Id_dl3);
        tab_page = (DirectionalLayout) component.findComponentById(ResourceTable.Id_tab_page);
        tab_list = (DirectionalLayout) component.findComponentById(ResourceTable.Id_tab_list);
        text_layout = (DirectionalLayout) component.findComponentById(ResourceTable.Id_text_layout);
        pro1 = new FristFolderPro(dataList1, context, 1);
        pro2 = new FristFolderPro(dataList2, context, 2);
        pro3 = new FristFolderPro(dataList3, context, 3);

        dl1.setItemProvider(pro1);
        dl2.setItemProvider(pro2);
        dl3.setItemProvider(pro3);
        setListViewHeight(dl1);
        setListViewHeight(dl2);
        setListViewHeight(dl3);
        text_helloworld = (Text) component.findComponentById(ResourceTable.Id_text_helloworld);
        text_home = (Text) component.findComponentById(ResourceTable.Id_text_home);
        item_list = (ListContainer) component.findComponentById(ResourceTable.Id_item_list);

        pro1.setOwnClick(ownClick1);
        pro2.setOwnClick(ownClick2);
        pro3.setOwnClick(ownClick3);
        gz = (Text) component.findComponentById(ResourceTable.Id_gz);
        gz.setClickedListener(component1 -> startNewAbFroResult("/mnt", 1));
        dl1.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                startNewAbFroResult(pro1.dataList.get(i).getPath(), 1);
            }
        });
        dl2.setItemClickedListener((listContainer, component12, i, l) -> startNewAbFroResult(pro2.dataList.get(i).getPath(), 2));
        dl3.setItemClickedListener((listContainer, component13, i, l) -> startNewAbFroResult(pro3.dataList.get(i).getPath(), 3));
        dl2.setItemLongClickedListener(dl2LongClick);
        //二级页面
        lc_list = (ListContainer) component.findComponentById(ResourceTable.Id_lc_list);
        tx_empt = (DirectionalLayout) component.findComponentById(ResourceTable.Id_tx_empt);
        tx_folder = (Text) component.findComponentById(ResourceTable.Id_tx_folder);
        text_home.setClickedListener(component14 -> {
            if (pageFlage) {//转回一级页面
                pageFlage = false;
                item_list.setVisibility(Component.HIDE);
                tab_list.setVisibility(Component.HIDE);
                tab_page.setVisibility(Component.VISIBLE);
//                        pathList.clear();
                loadWithWords("");
            } else {

            }
        });
    }

    public List<FristFolderData> loadType(String name, int type) {
        List<FristFolderData> dataList = new ArrayList<>();
//        System.out.println("sssssssssssssss"+perfrence.getOtherInConFile("name"));
        System.out.println(name + "查询出来的str>>>>>>>>>>>>>>>>.." + type + "----------" + perfrence.getParam(name));
        String[] strs = perfrence.getParam(name).split("&&");

        if (strs != null && strs.length > 0) {
            for (int i = 0; i < strs.length; i++) {
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
                folderData.isSelect = pathList.contains(file.getAbsolutePath());
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

    public void setAddViewHeight(ListContainer listView) {
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
        if (listAdapter.getCount() > 0) {
            params.height = listAdapter.getComponent(0, null, listView).getHeight() * listAdapter.getCount();
        } else {
            params.height = 0;
        }
        listView.setLayoutConfig(params);
    }

    public void startNewAbFroResult(String path, int requestCode) {
//        Toast.show(context, "路径" + path);
        System.out.println("路径>>>>>>>>>>>>" + path);
        indexPath = path;
        type = requestCode;
        pageFlage = true;
        tab_page.setVisibility(Component.HIDE);
        tab_list.setVisibility(Component.VISIBLE);
        pro = new FolderPro(indexPath, context, pathList);
        pro.setFolderImageClick(folderImageClick);
        lc_list.setItemProvider(pro);
        setEmptView(pro);
//        tx_folder.setText(getParentPath(pro.path));
        lc_list.setItemClickedListener(itemClick);
        lc_list.setItemLongClickedListener(lcListlongClick);
    }

    public void reNameFolder(FristFolderData deal, String newName, int type) {
        File oldFile = new File(deal.getPath());
        File newFile = new File(deal.getPath().replace(deal.getShowName(), newName));
        if (deal.isCanWrite) {
            if (oldFile.renameTo(newFile)) {
                switch (type) {
                    case 1:
                        perfrence.replaceParam("type1", deal.getPath(), deal.getPath().replace(deal.getShowName(), newName));
                        dataList1 = loadType("type1", 1);
                        pro1 = new FristFolderPro(dataList1, context, 1);
                        dl1.setItemProvider(pro1);
                        setAddViewHeight(dl1);
                        pro1.setOwnClick(ownClick1);
                        break;
                    case 2:
                        perfrence.replaceParam("type2", deal.getPath(), deal.getPath().replace(deal.getShowName(), newName));
                        dataList2 = loadType("type2", 2);
                        pro2 = new FristFolderPro(dataList2, context, 2);
                        dl2.setItemProvider(pro2);
                        setAddViewHeight(dl2);
                        pro2.setOwnClick(ownClick2);
                        break;
                    case 3:
                        perfrence.replaceParam("type3", deal.getPath(), deal.getPath().replace(deal.getShowName(), newName));
                        dataList1 = loadType("type3", 3);
                        pro3 = new FristFolderPro(dataList3, context, 3);
                        dl3.setItemProvider(pro3);
                        setAddViewHeight(dl3);
                        pro3.setOwnClick(ownClick3);
                        break;

                }
                new ToastDialog(context).setText("文件重命名成功").setDuration(2000).show();

            } else {
                new ToastDialog(context).setText("文件重命名失败").setDuration(2000).show();
            }
        } else {
            new ToastDialog(context).setText("文件禁止操作").setDuration(2000).show();
        }

    }

    public void showInputDialog(FristFolderData deal, int type) {
        CommonDialog dlg = new CommonDialog(context);
        Component layout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_input_rename, null, true);
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
                if (textField.getText().length() > 0 && !textField.getText().equals(deal.getShowName())) {
                    reNameFolder(deal, textField.getText().trim(), type);
                } else {
                    new ToastDialog(context).setText("请输入新名字").setDuration(2000).show();
                }
            }
        });
        dlg.setContentCustomComponent(layout);
        dlg.show();
    }

    final OwnClick ownClick1 = new OwnClick() {
        @Override
        public void doClick(int type, int position, FristFolderData deal) {
            switch (position) {
                case 0:
                    perfrence.apendParam("type2", deal.getPath());
                    dataList2 = loadType("type2", 2);
                    pro2 = new FristFolderPro(dataList2, context, 2);
                    dl2.setItemProvider(pro2);
                    pro2.setOwnClick(ownClick2);
                    setAddViewHeight(dl2);
                    break;
                case 1:
                    showInputDialog(deal, type);
                    break;
                case 2:
                    if (deal.isCanWrite()) {
                        dataList1.remove(deal);
                        pro1 = new FristFolderPro(dataList1, context, 1);
                        dl1.setItemProvider(pro1);
                        setListViewHeight(dl1);
                        String str2 = perfrence.getParam("type1").replace(deal.getPath(), "");
                        String newStr2 = str2.replace("&&&&", "&&");
                        perfrence.saveParam("type1", newStr2);
                    } else {
                        new ToastDialog(context).setText("文件禁止操作").setDuration(2000).show();
                    }
                    break;
            }
        }
    };
    final OwnClick ownClick2 = new OwnClick() {
        @Override
        public void doClick(int type, int position, FristFolderData deal) {
            switch (position) {
                case 0:
                    perfrence.apendParam("type3", deal.getPath());
                    dataList3 = loadType("type3", 3);
                    pro3 = new FristFolderPro(dataList3, context, 3);
                    dl3.setItemProvider(pro3);
                    setAddViewHeight(dl3);
                    pro3.setOwnClick(ownClick3);
                    break;
                case 1:
                    showInputDialog(deal, type);
                    break;
                case 2:
                    perfrence.cutParam("type2", deal.getPath());
                    dataList2 = loadType("type2", 2);
                    pro2 = new FristFolderPro(dataList2, context, 2);
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
                    perfrence.cutParam("type3", deal.getPath());
                    new ToastDialog(context).setText(perfrence.getParam("type3")).setDuration(2000).show();
                    System.out.println("处理之后的type3>>>>>>>>>>>" + perfrence.getParam("type3"));
                    dataList3 = loadType("type3", 3);
                    pro3 = new FristFolderPro(dataList3, context, 3);
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
                        pro2 = new FristFolderPro(dataList2, context, 2);
                        dl2.setItemProvider(pro2);
                        setListViewHeight(dl2);
                        String str2 = perfrence.getParam("type2").replace(deal.getPath(), "");
                        String newStr2 = str2.replace("&&&&", "&&");
                        perfrence.saveParam("type2", newStr2);
                    } else {
                        new ToastDialog(context).setText("文件禁止操作").setDuration(2000).show();
                    }
                    break;
            }
        }
    };
    final ListContainer.ItemLongClickedListener dl2LongClick = new ListContainer.ItemLongClickedListener() {
        @Override
        public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
            dataList2.get(i).setSelect(!dataList2.get(i).isSelect);
            if (pathList.contains(dataList2.get(i).getPath())) {
                pathList.remove(dataList2.get(i).getPath());
            } else {
                pathList.add(dataList2.get(i).getPath());
            }
            pro2.notifyDataChanged();
            setAddViewHeight(dl2);
            sliceToMainI.slectNumAndPathProd(pathList);
            return false;
        }
    };

    //二级页面方法
    public String getParentPath(String rootPath) {
        File file = new File(rootPath);
        return file.getAbsoluteFile().toString();
    }

    @Override
    public List<? extends BaseSelectEntity> getSelectedList() {
      return null;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLongClickChanged(boolean longClickState) {

    }

    @Override
    public void onTextSeared(String s) {

    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    final FolderImageClick folderImageClick = new FolderImageClick() {
        @Override
        public void doClick(int position, PageData deal) {
            switch (position) {
                case 0:
                    if (deal.isFloder) {
                        perfrence.apendParam("type2", deal.getFilePath());
                        new ToastDialog(context).setText("创建文件夹快捷方式成功").setDuration(2000).show();
                    } else {
//                        FileUtils.openFile(context,new File(deal.getFilePath()));
                        new ToastDialog(context).setText("请自行打开").setDuration(2000).show();
                    }
                    break;
                case 1:
                    showInputDialog2(deal, type);
                    break;
                case 2:
                    if (deal.isCanWrite) {
                        File file = new File(deal.getFilePath());
                        if (file.exists()) {
                            if (file.delete()) {
                                perfrence.cutParam("type" + type, deal.getFilePath());
                                new ToastDialog(context).setText("文件删除成功").setDuration(2000).show();
                                pro = new FolderPro(file.getParent(), context, pathList);//刷新父页面
                                lc_list.setItemProvider(pro);
                                pro.setFolderImageClick(folderImageClick);
                                setEmptView(pro);
//                                tx_folder.setText(getParentPath(pro.path));
                                lc_list.setItemClickedListener(itemClick);
                                lc_list.setItemLongClickedListener(lcListlongClick);
                            } else {
                                new ToastDialog(context).setText("文件删除失败").setDuration(2000).show();
                            }
                        }
                    } else {
                        new ToastDialog(context).setText("文件禁止操作").setDuration(2000).show();
                    }
                    break;
            }
        }
    };

    public void showInputDialog2(PageData deal, int type) {
        CommonDialog dlg = new CommonDialog(context);
        Component layout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_input_rename, null, true);
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
                if (textField.getText().length() > 0 && !textField.getText().equals(deal.getFileName())) {
                    reNameFolder2(deal, textField.getText().trim(), type, dlg);
                } else {
                    new ToastDialog(context).setText("请输入新名字").setDuration(2000).show();
                }
            }
        });
        dlg.setContentCustomComponent(layout);
        dlg.show();
    }

    public void setEmptView(FolderPro folderPro) {
        stringListPro = new StringListPro(context, indexPath);
        item_list.setItemProvider(stringListPro);
//        text_home.setVisibility(Component.HIDE);
        item_list.setVisibility(Component.VISIBLE);
        item_list.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                startNewAbFroResult(stringListPro.getIndexPath(stringListPro.fileList.size() - 1 - i), 4);
            }
        });
        if (folderPro.dataList == null || folderPro.dataList.size() == 0) {
            tx_empt.setVisibility(Component.VISIBLE);
            lc_list.setVisibility(Component.HIDE);
        } else {
            tx_empt.setVisibility(Component.HIDE);
            lc_list.setVisibility(Component.VISIBLE);

        }
    }

    public void reNameFolder2(PageData deal, String newName, int type, CommonDialog dig) {
        File oldFile = new File(deal.getFilePath());
        File newFile = new File(deal.getFilePath().replace(deal.getFileName(), newName));
        if (deal.isCanWrite) {
            if (oldFile.renameTo(newFile)) {
                switch (type) {
                    case 1:
                        perfrence.replaceParam("type1", deal.getFilePath(), deal.getFilePath().replace(deal.getFileName(), newName));
                        break;
                    case 2:
                        perfrence.replaceParam("type2", deal.getFilePath(), deal.getFilePath().replace(deal.getFileName(), newName));
                        break;
                    case 3:
                        perfrence.replaceParam("type3", deal.getFilePath(), deal.getFilePath().replace(deal.getFileName(), newName));
                        break;

                }
                dig.destroy();
                new ToastDialog(context).setText("文件重命名成功").setDuration(2000).show();
                //重置adapter
                pro = new FolderPro(pro.path, context, pathList);
                lc_list.setItemProvider(pro);
                setEmptView(pro);
                pro.setFolderImageClick(folderImageClick);
//                tx_folder.setText(getParentPath(pro.path));
                lc_list.setItemClickedListener(itemClick);
                lc_list.setItemLongClickedListener(lcListlongClick);
            } else {
                new ToastDialog(context).setText("文件重命名失败").setDuration(2000).show();
            }
        } else {
            new ToastDialog(context).setText("文件禁止操作").setDuration(2000).show();
        }

    }

    final ListContainer.ItemClickedListener itemClick = new ListContainer.ItemClickedListener() {
        @Override
        public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
            if (pro.dataList.get(i).isFloder) {
                if (pro.dataList.get(i).isCanRead) {
                    currentPageData = pro.dataList.get(i);
                    indexPath = currentPageData.filePath;
                    pro = new FolderPro(currentPageData.filePath, context, pathList);
                    lc_list.setItemProvider(pro);
                    setEmptView(pro);
                    pro.setFolderImageClick(folderImageClick);
//                    tx_folder.setText(getParentPath(pro.path));
                    lc_list.setItemClickedListener(itemClick);
                    lc_list.setItemLongClickedListener(lcListlongClick);
                } else {
                    new ToastDialog(context).setText("文件不可读取").setDuration(2000).show();
                }
            } else {
//                FileUtils.openFile(context,new File(pro.dataList.get(i).getFilePath()));
                new ToastDialog(context).setText("文件请自行打开").setDuration(2000).show();

            }
        }
    };
    final ListContainer.ItemLongClickedListener lcListlongClick = new ListContainer.ItemLongClickedListener() {
        @Override
        public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
            setLongClickState(true);
            pro.setItemSelect(i);
            if (pathList.contains(pro.getSelectString(i))) {
                pathList.remove(pro.getSelectString(i));
            } else {
                pathList.add(pro.getSelectString(i));
            }
            sliceToMainI.slectNumAndPathProd(pathList);
            return false;
        }
    };


    private void setLongClickUI() {
        setLongClickState(true);


    }

    public boolean isToFrist() {
        System.out.println("in isToFrist");
        File file;
        if (pro == null) {
            file = new File("/mnt");
        } else {
            file = new File(pro.path);
        }
        System.out.println(file.getAbsolutePath() + "返回的地址>>>>>>>>>" + file.getParent() + "====" + file.getParent().length());
        if (!file.getParent().isEmpty() && file.getParent().length() > 2
//                && !file.getParent().equals("/")
        ) {
            indexPath = file.getParent();
            pro = new FolderPro(file.getParent(), context, pathList);
            lc_list.setItemProvider(pro);
            setEmptView(pro);
//            tx_folder.setText(getParentPath(pro.path));
            pro.setFolderImageClick(folderImageClick);
            lc_list.setItemClickedListener(itemClick);
            lc_list.setItemLongClickedListener(lcListlongClick);
            return false;
        } else {
            if (pageFlage) {
                tab_list.setVisibility(Component.HIDE);
                tab_page.setVisibility(Component.VISIBLE);
                dataList2 = loadType("type2", 2);
                pro2 = new FristFolderPro(dataList2, context, 2);
                dl2.setItemProvider(pro2);
                setAddViewHeight(dl2);
                pro2.setOwnClick(ownClick2);
                pageFlage = false;
                return false;
            } else {
                return true;
            }
        }
    }

    public void loadWithWords(String str) {
        dataList1.clear();
        dataList1.addAll(loadType("type1", 2));
        for (int i = dataList1.size() - 1; i >= 0; i--) {
            System.out.println(dataList1.get(i).getShowName() + "====" + str);
            if (!dataList1.get(i).getShowName().contains(str)) {
                dataList1.remove(i);
            }
        }
        pro1.notifyDataChanged();
        setAddViewHeight(dl1);
        dataList2.clear();
        dataList2.addAll(loadType("type2", 2));
        for (int i = dataList2.size() - 1; i >= 0; i--) {
            System.out.println(dataList2.get(i).getShowName() + "====" + str);
            if (!dataList2.get(i).getShowName().contains(str)) {
                dataList2.remove(i);
            }
        }
        pro2.notifyDataChanged();
        setAddViewHeight(dl2);
        dataList3.clear();
        dataList3.addAll(loadType("type3", 3));
        for (int i = dataList3.size() - 1; i >= 0; i--) {
            System.out.println(dataList3.get(i).getShowName() + "====" + str);
            if (!dataList3.get(i).getShowName().contains(str)) {
                dataList3.remove(i);
            }
        }
        pro3.notifyDataChanged();
        setAddViewHeight(dl3);
    }

    public void notifyData() {
        if (pageFlage) {
//            pro.notifyDataChanged();
            pro = new FolderPro(indexPath, context, pathList);
            lc_list.setItemProvider(pro);
            setEmptView(pro);
            pro.setFolderImageClick(folderImageClick);
//            tx_folder.setText(getParentPath(pro.path));
            lc_list.setItemClickedListener(itemClick);
            lc_list.setItemLongClickedListener(lcListlongClick);
        } else {

            dataList2 = loadType("type2", 2);
            pro2 = new FristFolderPro(dataList2, context, 2);
            dl2.setItemProvider(pro2);
            setAddViewHeight(dl2);
            pro2.setOwnClick(ownClick2);
        }
    }
}
