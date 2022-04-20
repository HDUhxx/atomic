package me.panavtec.title.slice;

import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.title.ContentSharingActivity;
import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.FileDetailsAbility;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.fraction.*;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.DBHelper;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionManager;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentSharingActivitySlice extends AbilitySlice implements
        Text.TextObserver, SliceToMainI, Component.ClickedListener {
    private TabList tabList;
    FractionManager fractionManager;
    final List<BaseListFraction> baseListFractionList = new ArrayList<>();
    private int allSelectNum = 0;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_content_sharing_activity);
        fractionManager = ((FractionAbility) getAbility()).getFractionManager();
        initData();
        try {
            initView();
            initActionBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TextField searchBoxControl;
    Component searchContainer;

    /**
     * 初始化标题栏
     *
     * @throws NotExistException  。
     * @throws WrongTypeException 。
     * @throws IOException        。
     */
    private void initActionBar() throws NotExistException, WrongTypeException, IOException {
        findComponentById(ResourceTable.Id_back_header).setClickedListener(this);
        searchContainer = findComponentById(ResourceTable.Id_search_container);
        searchBoxControl = (TextField) findComponentById(ResourceTable.Id_search_box_control);
        Image closeIcon = (Image) findComponentById(ResourceTable.Id_close_icon);
        Text txt_zero = (Text) findComponentById(ResourceTable.Id_txt_zero);
        txt_zero.setText(getResourceManager().getElement(ResourceTable.String_butn_send).getString());
        Image more_main = (Image) findComponentById(ResourceTable.Id_more_main);
        findComponentById(ResourceTable.Id_search_icon1)
                .setClickedListener(component -> searchContainer.setVisibility(Component.VISIBLE));
        closeIcon.setClickedListener(this);

        more_main.setClickedListener(component -> Toast.show(ContentSharingActivitySlice.this, "点击了更多"));
        searchBoxControl.addTextObserver(this);

        mBottomHeader = findComponentById(ResourceTable.Id_bottom_header);
        mBottomHeader.setVisibility(Component.HIDE);

        findComponentById(ResourceTable.Id_action_close).setClickedListener(this);
        selectNum = (Text) findComponentById(ResourceTable.Id_action_num);
        findComponentById(ResourceTable.Id_action_send).setClickedListener(this);
        findComponentById(ResourceTable.Id_action_more).setClickedListener(this);

    }

    @Override
    public void onTextUpdated(String s, int i, int i1, int i2) {
        int position = tabList.getSelectedTab().getPosition();
        baseListFractionList.get(position).onTextSeared(s);
    }


    private Component mBottomHeader;
    private Text selectNum;

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_back_header:
                terminateAbility();
                break;
            case ResourceTable.Id_close_icon: //
                searchContainer.setVisibility(Component.HIDE);
                searchBoxControl.setText("");
                searchBoxControl.clearFocus();
                clearLongStateUi();
                break;
            case ResourceTable.Id_action_send:
                ArrayList<TransferObject> list = new ArrayList<>();
                ArrayList<String> requestIds = new ArrayList<>();
                long groundId = DBHelper.getGroupId();
                for (BaseListFraction baseListFraction : baseListFractionList) {
                    List<? extends BaseSelectEntity> selectedList1 = baseListFraction.getSelectedList();
                    if (selectedList1 == null) continue;
                    for (BaseSelectEntity baseSelectEntity : selectedList1) {
                        TransferObject transferObject = DBHelper.getTransferObject(baseSelectEntity, groundId);
                        list.add(transferObject);
                        requestIds.add(transferObject.requestId + "");
                    }
                }
                if (tabList.getSelectedTab().getPosition() == 0) {
                    FileExplorerFragment baseListFraction = (FileExplorerFragment) baseListFractionList.get(0);
                    List<String> pathList = baseListFraction.pathList;
                    List<TransferObject> transferObjectForFolder = DBHelper.getTransferObjectForFolder(pathList, groundId);
                    list.addAll(transferObjectForFolder);

                    for (TransferObject transferObject : transferObjectForFolder) {
                        long requestId = transferObject.requestId;
                        requestIds.add(requestId + "");
                    }
                }
                AppUtils.getDatabase(getContext()).insert(list);
                clearLongStateUi();
                jumpAbility(FileDetailsAbility.class, String.valueOf(groundId), requestIds);
                break;
            case ResourceTable.Id_action_more:
                Toast.show(this, "底部操作");
                break;
            case ResourceTable.Id_action_close:
                clearLongStateUi();
                break;
        }
    }


    public void jumpAbility(Class<?> tclass, String groundId, ArrayList<String> requestIds) {
        Intent intent = new Intent();
        Operation build = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName(tclass)
                .withBundleName(getBundleName())
                .build();
        intent.setOperation(build);
        intent.setParam(FileDetailsAbilitySlice.KEY_GROUNDID, groundId);
        intent.setStringArrayListParam(FileDetailsAbilitySlice.KEY_REQUESTID, requestIds);
        startAbility(intent, 1);
    }

    /**
     * 复位长按状态的Ui
     */
    private void clearLongStateUi() {
        allSelectNum = 0;
        mBottomHeader.setVisibility(Component.HIDE);
        for (BaseListFraction baseListFraction : baseListFractionList) {
            baseListFraction.setLongClickState(false);
        }
    }


    @Override
    public void allSelectNum(int num) {
        allSelectNum = allSelectNum + num;
        findComponentById(ResourceTable.Id_bottom_header).setVisibility(Component.VISIBLE);
        selectNum.setText(allSelectNum + "");
    }

    @Override
    public void slectNumAndPathProd(List<String> path) {
        findComponentById(ResourceTable.Id_bottom_header).setVisibility(Component.VISIBLE);
        selectNum.setText(path.size() + "");
    }

    private void initView() throws NotExistException, WrongTypeException, IOException {
        tabList = (TabList) findComponentById(ResourceTable.Id_tab_list_share);
        //列表项
//        TabList.Tab appTab = tabList.new Tab(this);
//        appTab.setText(getResourceManager().getElement(ResourceTable.String_text_application).getString());
//        tabList.addTab(appTab, true);

        TabList.Tab fileTab = tabList.new Tab(this);
        fileTab.setText(getResourceManager().getElement(ResourceTable.String_text_file).getString());
        tabList.addTab(fileTab,true);

        TabList.Tab musicTab = tabList.new Tab(this);
        musicTab.setText(getResourceManager().getElement(ResourceTable.String_text_music).getString());
        tabList.addTab(musicTab);

        TabList.Tab photoTab = tabList.new Tab(this);
        photoTab.setText(getResourceManager().getElement(ResourceTable.String_text_photo).getString());
        tabList.addTab(photoTab);

        TabList.Tab videoTab = tabList.new Tab(this);
        videoTab.setText(getResourceManager().getElement(ResourceTable.String_text_video).getString());
        tabList.addTab(videoTab);

        FractionScheduler fractionScheduler = fractionManager.startFractionScheduler();
        fractionScheduler
                .add(ResourceTable.Id_shareRroundContainer, baseListFractionList.get(0))
                .submit();
        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                int position = tab.getPosition();
                replaceMyFraction(baseListFractionList.get(position));
                onClick(findComponentById(ResourceTable.Id_close_icon));
            }

            @Override
            public void onUnselected(TabList.Tab tab) {
            }

            @Override
            public void onReselected(TabList.Tab tab) {

            }
        });
    }


    /**
     * 替换fraction
     *
     * @param fraction 页面即将展示的fraction
     */
    private void replaceMyFraction(Fraction fraction) {
        ((FractionAbility) getAbility()).getFractionManager().startFractionScheduler()
                .replace(ResourceTable.Id_shareRroundContainer, fraction)
                .submit();
    }


    /**
     * 初始化页面数据
     */
    public void initData() {
//        HapListFragment hapListFragment = new HapListFragment(this);
        MusicListFragment musicListFragment = new MusicListFragment(this);
        ImageListFragment imageListFragment = new ImageListFragment(this);
        VideoListFragment videoListFragment = new VideoListFragment(this);
        FileExplorerFragment fileExplorerFragment = new FileExplorerFragment(this, this);
//        System.out.println(getApplicationContext()+":::::::::::::::"+getAbilityPackageContext());
//        System.out.println(new HmSharedPerfrence(this).getFromOther("type1")+"..................");
//        System.out.println(HmSharedPerfrence.getInstance(this).getFromOther("type1")+"..................");
//        baseListFractionList.add(hapListFragment);
        baseListFractionList.add(fileExplorerFragment);
        baseListFractionList.add(musicListFragment);
        baseListFractionList.add(imageListFragment);
        baseListFractionList.add(videoListFragment);


        ((ContentSharingActivity) getAbility()).setPermissionCall(() -> {
            for (BaseListFraction baseListFraction : baseListFractionList) {
                baseListFraction.onRefresh();
            }
        });
    }

    @Override
    protected void onBackPressed() {
        if (tabList != null && tabList.getSelectedTab() != null) {
            int position = tabList.getSelectedTab().getPosition();
            if (position == 1) {
                if (((FileExplorerFragment) baseListFractionList.get(0)).isToFrist())
                    super.onBackPressed();
            } else terminateAbility();
        }else terminateAbility();
    }
}
