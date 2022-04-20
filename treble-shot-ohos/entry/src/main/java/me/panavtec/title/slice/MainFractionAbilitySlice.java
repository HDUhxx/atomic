package me.panavtec.title.slice;

import com.bumptech.glide.Glide;
import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.popupwindow.PopItemObject;
import com.ohos.trebleshot.popupwindow.PopupEntity;
import com.ohos.trebleshot.popupwindow.PopupWindowView;
import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.FileIntents;
import me.panavtec.config.AppConfig;
import me.panavtec.dialog.DevSurveyDialog;
import me.panavtec.dialog.ShareAppDialog;
import me.panavtec.title.Entity.PopConfig;
import me.panavtec.title.FileDetailsAbility;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.fraction.FileExplorerFragment;
import me.panavtec.title.fraction.TextStreamListFragment;
import me.panavtec.title.fraction.TransferGroupListFragment;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.DBHelper;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionManager;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.*;
import ohos.agp.components.element.VectorElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.data.resultset.ResultSet;
import ohos.event.commonevent.*;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVStorage;
import ohos.rpc.RemoteException;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.panavtec.title.slice.FileDetailsAbilitySlice.KEY_GROUNDID;
import static me.panavtec.title.slice.FileDetailsAbilitySlice.KEY_REQUESTID;

public class MainFractionAbilitySlice extends AbilitySlice implements
        Text.TextObserver, SliceToMainI {
    private DependentLayout root_layout;
    private Image image_back;
    private boolean isShow;
    private Component view;
    private final int widthAnima = 1200;
    private Text mTrustZoneToggle;
    private final List<Fraction> abilitySliceList = new ArrayList<>();
    private FractionManager fractionManager;
    private int currentPage = 0;
    private DirectionalLayout image_directional_layout;
    Image image_DL;
    private CommonEventSubscriber mSubscriber;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_fracion_ability_main);
        fractionManager = ((FractionAbility) getAbility()).getFractionManager();
        initData();
        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        initToolBar();


        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(CommunicationService.ACTION_TRUSTZONE_STATUS);
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        mSubscriber = new CommonEventSubscriberExt(subscribeInfo);
    }


    public class CommonEventSubscriberExt extends CommonEventSubscriber {
        public CommonEventSubscriberExt(CommonEventSubscribeInfo subscribeInfo) {
            super(subscribeInfo);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            if (CommunicationService.ACTION_TRUSTZONE_STATUS.equals(commonEventData.getIntent().getAction())
                    && mTrustZoneToggle != null)
                mTrustZoneToggle.setText(commonEventData.getIntent().getBooleanParam(
                        CommunicationService.EXTRA_STATUS_STARTED, false)
                        ? ResourceTable.String_butn_turnTrustZoneOff : ResourceTable.String_butn_turnTrustZoneOn);
        }
    }

    public void showLeft() {
        if (view == null) {
            view = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_left, null, false);
            view.setId(7668);
            view.setVisibility(-1);
//            DirectionalLayout left_root = (DirectionalLayout) findComponentById(ResourceTable.Id_left_root);
//            DependentLayout.LayoutConfig config = new DependentLayout.LayoutConfig();
            initPage(view);
            root_layout.addComponent(view, DependentLayout.LayoutConfig.MATCH_PARENT, DependentLayout.LayoutConfig.MATCH_PARENT);
            view.setVisibility(0);
            mTrustZoneToggle = (Text) view.findComponentById(ResourceTable.Id_menu_activity_trustzone);
        }

        AnimatorGroup animatorGroup = new AnimatorGroup();
        AnimatorProperty imageAnima = image_back.createAnimatorProperty();
        imageAnima.rotate(-180).setDuration(300);
        AnimatorProperty animatorProperty = view.createAnimatorProperty();
        animatorProperty.moveFromX(-widthAnima).moveToX(0).setDuration(300);
        animatorGroup.runParallel(imageAnima, animatorProperty);
        animatorGroup.start();
        isShow = true;
        DirectionalLayout rightOther = (DirectionalLayout) findComponentById(ResourceTable.Id_right_other);
        rightOther.setClickedListener(component -> {
            if (isShow) {
                hideLeft();
            }
        });
        setDewarListener();
    }


    public void hideLeft() {
        AnimatorProperty animatorProperty = view.createAnimatorProperty();
        animatorProperty.moveFromX(0).moveToX(-widthAnima).setDuration(300);
        AnimatorGroup animatorGroup = new AnimatorGroup();
        AnimatorProperty imageAnima = image_back.createAnimatorProperty();
        imageAnima.rotate(0).setDuration(300);
        animatorGroup.runParallel(imageAnima, animatorProperty);
        animatorGroup.start();
        isShow = false;
    }

    private void setDewarListener() {
        view.findComponentById(ResourceTable.Id_menu_activity_main_manage_devices).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_main_web_share).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_main_send_application).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_main_preferences).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_main_about).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_main_dev_survey).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_main_exit).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_feedback).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_menu_activity_trustzone).setClickedListener(clickedListener);
    }

    private final Component.ClickedListener clickedListener = component -> {
        Text text = (Text) component;
//        Toast.show(getContext(), text.getText());
        switch (component.getId()) {
            case ResourceTable.Id_menu_activity_main_manage_devices:
                present(new ManageDevicesActivitySlice(), new Intent());
                break;
            case ResourceTable.Id_menu_activity_main_web_share:
                present(new WebShareAbilitySlice(), new Intent());
//                HMUtils.startAbility(getContext(), SliceActionDef.ACTION_WEB_SHARE_ABILITY);
                break;
            case ResourceTable.Id_menu_activity_main_send_application:
                new ShareAppDialog(getContext()).show();
                break;
            case ResourceTable.Id_menu_activity_main_preferences:
                present(new PreferencesAbilitySlice(), new Intent());
                break;
            case ResourceTable.Id_menu_activity_main_dev_survey:
                new DevSurveyDialog(getContext()).show();
                break;
            case ResourceTable.Id_menu_activity_main_about:
                present(new AboutAbilitySlice(), new Intent());
                break;

            case ResourceTable.Id_menu_activity_main_exit:
                HMUtils.startService(this, false);
                HMUtils.startWorkerService(this, false);
                System.exit(0);
                break;
            case ResourceTable.Id_menu_activity_feedback:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.setParam("android.intent.extra.SUBJECT", getString(ResourceTable.String_butn_share));
                intent.setParam("android.intent.extra.SUBJECT", getString(ResourceTable.String_app_name));
                intent.setParam("android.intent.extra.EMAIL", new String[]{AppConfig.EMAIL_DEVELOPER});
                intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                startAbility(intent);
                break;

//                case ResourceTable.Id_menu_activity_main_donate:
////                    try {
////                        startActivity(new Intent(this, Class.forName("com.genonbeta.TrebleShot.activity.DonationActivity")));
////                    } catch (ClassNotFoundException e) {
////                        e.printStackTrace();
////                    }
//                    break;
            case ResourceTable.Id_menu_activity_trustzone:
                toggleTrustZone();
//                    break;
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        if (image_DL != null) {
            initPage(view);
            loadExiestImage(image_DL);
        }

        try {
            CommonEventManager.subscribeCommonEvent(mSubscriber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        requestTrustZoneStatus();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        try {
            CommonEventManager.unsubscribeCommonEvent(mSubscriber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void requestTrustZoneStatus() {
        HMUtils.startService(this, CommunicationService.class,
                CommunicationService.ACTION_REQUEST_TRUSTZONE_STATUS);
    }

    public void toggleTrustZone() {
        HMUtils.startService(this, CommunicationService.class,
                CommunicationService.ACTION_TOGGLE_SEAMLESS_MODE);
    }

    @Override
    protected void onBackPressed() {
//        super.onBackPressed();
        System.out.println("按了返回键" + currentPage);
        if (isShow) {
            hideLeft();
        } else {
            if (currentPage == 1) {
                if (((FileExplorerFragment) abilitySliceList.get(1)).isToFrist()) {
//                    super.onBackPressed();
                    exitAfterTwice();
                }
            } else {
//                super.onBackPressed();
                exitAfterTwice();
            }

        }
    }

    public void initView() throws IOException, NotExistException, WrongTypeException {
        stackLayout = (StackLayout) findComponentById(ResourceTable.Id_action_bar);
        root_layout = (DependentLayout) findComponentById(ResourceTable.Id_root_layout);
        image_back = (Image) findComponentById(ResourceTable.Id_back_header);

        image_back.setClickedListener(component -> {
            if (!isShow) {
                showLeft();
            } else {
                hideLeft();
            }
        });


        TabList tabList = (TabList) findComponentById(ResourceTable.Id_tab_list);


        //列表项
        TabList.Tab imageTab = tabList.new Tab(getContext());
        imageTab.setText(getResourceManager().getElement(ResourceTable.String_text_transfers).getString());
        imageTab.setAroundElements(null, new VectorElement(this, ResourceTable.Layout_ic_swap_vert_white_24dp), null, null);
        tabList.addTab(imageTab, true);
        imageTab.setTextColor(new Color(0xff66bb6a));

        TabList.Tab videoTab = tabList.new Tab(getContext());
        videoTab.setText(getResourceManager().getElement(ResourceTable.String_text_fileExplorer).getString());
        videoTab.setAroundElements(null, new VectorElement(this, ResourceTable.Layout_ic_folder_white_24dp), null, null);
        tabList.addTab(videoTab);

        TabList.Tab audioTab = tabList.new Tab(getContext());
        audioTab.setText(getResourceManager().getElement(ResourceTable.String_text_textStream).getString());
        audioTab.setAroundElements(null, new VectorElement(this, ResourceTable.Layout_ic_short_text_white_24dp), null, null);
        tabList.addTab(audioTab);

        FractionScheduler fractionScheduler = fractionManager.startFractionScheduler();
        fractionScheduler.add(ResourceTable.Id_groundContainer, abilitySliceList.get(0), imageTab.getName())
                .add(ResourceTable.Id_groundContainer, abilitySliceList.get(1), videoTab.getName())
                .add(ResourceTable.Id_groundContainer, abilitySliceList.get(2), audioTab.getName())
                .submit();
        hideAllFraction();
        showMyFraction(abilitySliceList.get(0));
        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                int position = tab.getPosition();
                currentPage = position;
                tab.setTextColor(new Color(0xff66bb6a));
                hideAllFraction();
                showMyFraction(abilitySliceList.get(position));
                if (position == 0) {
                    TransferGroupListFragment fraction = (TransferGroupListFragment) abilitySliceList.get(position);
                    fraction.onRefresh();
                }

            }

            @Override
            public void onUnselected(TabList.Tab tab) {
            }

            @Override
            public void onReselected(TabList.Tab tab) {

            }
        });

    }

    private void hideAllFraction() {
        fractionManager.startFractionScheduler()
                .hide(abilitySliceList.get(0))
                .hide(abilitySliceList.get(1))
                .hide(abilitySliceList.get(2))
                .submit();
    }

    private void showMyFraction(Fraction fraction) {
        fractionManager.startFractionScheduler()
                .show(fraction)
                .submit();
    }

    public void initData() {
        TransferGroupListFragment transferGroupListFragment = new TransferGroupListFragment(this);
        FileExplorerFragment fileExplorerFragment = new FileExplorerFragment(this, this);
        TextStreamListFragment textStreamListFragment = new TextStreamListFragment(this);

        abilitySliceList.add(transferGroupListFragment);
        abilitySliceList.add(fileExplorerFragment);
        abilitySliceList.add(textStreamListFragment);
    }


    Image moreMain;

    private void initToolBar() {
        Component searchContainer = findComponentById(ResourceTable.Id_search_container);
        TextField searchBoxControl = (TextField) findComponentById(ResourceTable.Id_search_box_control);
        Image closeIcon = (Image) findComponentById(ResourceTable.Id_close_icon);
//        Text  txt_zero = (Text) findComponentById(ResourceTable.Id_txt_zero);
        Image searchIcon1 = (Image) findComponentById(ResourceTable.Id_search_icon1);
        moreMain = (Image) findComponentById(ResourceTable.Id_more_main);
        searchIcon1.setClickedListener(new Component.ClickedListener() {
                                           @Override
                                           public void onClick(Component component) {
                                               searchBoxControl.setFocusable(Component.FOCUS_ENABLE);
                                               searchContainer.setVisibility(Component.VISIBLE);
                                           }
                                       }

        );
        closeIcon.setClickedListener(component -> {
            searchContainer.setVisibility(Component.HIDE);
            searchBoxControl.setText("");
            searchBoxControl.clearFocus();
            reSetPageByCloseIcon(currentPage);

        });

        searchBoxControl.addTextObserver(this);
        moreMain.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                List<PopItemObject> popItemObjects = null;
                if (currentPage == 0) {
                    popItemObjects = PopConfig.createdItemObject1();
                } else if (currentPage == 1) {
                    popItemObjects = PopConfig.createdItemObject2();
                } else if (currentPage == 2) {
                    popItemObjects = PopConfig.createdItemObject3();
                }

                List<String> itemName = PopConfig.getItemName(popItemObjects);
                String[] items = itemName.toArray(new String[0]);
                if (items != null) {
                    createdDialog(component, items).show().setOnSelectListener(new SelectDealer());
                }
            }
        });
    }

    @Override
    public void onTextUpdated(String s, int i, int i1, int i2) {
        Toast.show(MainFractionAbilitySlice.this, "数据变化" + s);
        doSearch(currentPage, s);
    }

    @Override
    public void allSelectNum(int num) {
        addActionBar(num, null);
    }

    @Override
    public void slectNumAndPathProd(List<String> path) {
        addActionBar(path.size(), path);// path用于发送功能

    }

    private Component parse;
    private StackLayout stackLayout;

    private void addActionBar(int num, List<String> path) {
        if (parse == null) {
            parse = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_main_action_header, null, false);
            stackLayout.addComponent(parse);
        }
        Image imageClose = (Image) parse.findComponentById(ResourceTable.Id_action_close);
        Text textNum = (Text) parse.findComponentById(ResourceTable.Id_action_num);
        Image imaDelete = (Image) parse.findComponentById(ResourceTable.Id_action_delete);
        Image imaMore = (Image) parse.findComponentById(ResourceTable.Id_action_more);
        textNum.setText(num + "");
        imageClose.setClickedListener(component -> {
            TransferGroupListFragment fraction = (TransferGroupListFragment) abilitySliceList.get(0);
            fraction.clearSelected();
            clearActionBar();
            fraction.setLongClickState(false);
        });
        imaDelete.setClickedListener(component -> {
            TransferGroupListFragment fraction = (TransferGroupListFragment) abilitySliceList.get(0);
            fraction.deleteItems();
            clearActionBar();
            fraction.setLongClickState(false);
        });
        if (currentPage == 1) {
            imaDelete.setImageElement(new VectorElement(this, ResourceTable.Layout_ic_send_white_24dp));
            imaDelete.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
//                    Toast.show(MainFractionAbilitySlice.this, "文件个数" + path.size());
                    if (path != null && path.size() > 0) {
                        long groundId = DBHelper.getGroupId();
                        List<TransferObject> transferObjectForFolder = DBHelper.getTransferObjectForFolder(path, groundId);
                        if (transferObjectForFolder.size() == 0) return;
                        AppUtils.getDatabase(getContext()).insert(transferObjectForFolder);

                        Intent intent = new Intent();
                        Operation build = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withAbilityName(FileDetailsAbility.class)
                                .withBundleName(getBundleName())
                                .build();
                        intent.setOperation(build);
                        intent.setParam(KEY_GROUNDID, groundId + "");
                        ArrayList<String> requestIds = new ArrayList<>();
                        for (TransferObject transferObject : transferObjectForFolder) {
                            long requestId = transferObject.requestId;
                            requestIds.add(requestId + "");
                        }
                        intent.setStringArrayListParam(KEY_REQUESTID, requestIds);
                        startAbility(intent, 1);
                    } else {
                        Toast.show(MainFractionAbilitySlice.this, "请选择文件" + path.size());
                    }
                }
            });
            imageClose.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    FileExplorerFragment fileExplorerFragment = (FileExplorerFragment) abilitySliceList.get(1);
                    fileExplorerFragment.pathList.clear();
                    fileExplorerFragment.notifyData();
                    clearActionBar();

                }
            });
        }
        imaMore.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                TransferGroupListFragment fraction = (TransferGroupListFragment) abilitySliceList.get(0);
                fraction.doAction(1111);
                clearActionBar();
            }
        });
    }

    private void clearActionBar() {
        if (parse != null) {
            stackLayout.removeComponent(parse);
            parse = null;
        }
    }

    public void doSearch(int curPage, String str) {
        switch (curPage) {
            case 0:
                TransferGroupListFragment listFragment = (TransferGroupListFragment) abilitySliceList.get(0);
                listFragment.onTextSeared(str);
                break;
            case 1:
                doCase1(str);
                break;
            case 2:
                break;
        }
    }

    public void doCase1(String str) {

        FileExplorerFragment fileEx = (FileExplorerFragment) abilitySliceList.get(1);
        if (fileEx.pageFlage) {
            fileEx.pro.doSearchInSelf(str, false);
        } else {
            fileEx.loadWithWords(str);
        }
    }

    public void reSetPageByCloseIcon(int curPage) {
        switch (curPage) {
            case 0:
                TransferGroupListFragment listFragment = (TransferGroupListFragment) abilitySliceList.get(0);
                listFragment.setLongClickState(false);
                break;
            case 1:
                doCase1("");
                break;
            case 2:
                break;
        }
    }

    private long exitTime = 0;

    /**
     * 连续点击2次退出
     */
    public void exitAfterTwice() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.show(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT);
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
    }

    public void initPage(Component cpt) {
//        cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_welcome_page_2, null, false);
        image_DL = (Image) cpt.findComponentById(ResourceTable.Id_image_DL);
//                avatarInterface.loadExiestImage(image_DL);
        loadExiestImage(image_DL);
//        image_DL.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                Intent intent = FileIntents.from(MainFractionAbilitySlice.this).pickImageFile().build();
//                startAbilityForResult(intent, 2001);
//            }
//        });
        Text txShowName = (Text) cpt.findComponentById(ResourceTable.Id_tx_show_name);
        txShowName.setText(getName());

        Text txShowName_ = (Text) cpt.findComponentById(ResourceTable.Id_tx_show_name_);
        txShowName_.setText(String.valueOf(getName().toLowerCase().charAt(0)));

        image_DL.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showNameDialog(txShowName, txShowName_);
            }
        });
//        componentContainer.addComponent(cpt);
    }

    public void loadExiestImage(Image imageDL_) {
//        this.imageDirectionalLayout = imageDirectionalLayout;
//        HmSharedPerfrence perfrence = new HmSharedPerfrence(this);
        HmSharedPerfrence perfrence = HmSharedPerfrence.getInstance(this);
        String str = perfrence.getOtherInConFile("avater");
//        System.out.println("查询出来的avater>>>>>>>>>>>>>"+str);
        if (str.length() > 0) {
            Uri uri = Uri.parse(str);
            showImage2(uri, imageDL_);
        }
    }

    private void showImage2(Uri inUri, Image imageDL_) {
        DataAbilityHelper helper = DataAbilityHelper.creator(this);
        try {
            // columns为null，查询记录所有字段，当前例子表示查询id字段
//            System.out.println("------------");
            ResultSet resultSet = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, new String[]{AVStorage.Images.Media.ID, AVStorage.Images.Media.DATA}, null);
            while (resultSet != null && resultSet.goToNextRow()) {
//                System.out.println("====================");
                PixelMap pixelMap = null;
                ImageSource imageSource = null;

                int id = resultSet.getInt(resultSet.getColumnIndexForName(AVStorage.Images.Media.ID));
                String data = resultSet.getString(resultSet.getColumnIndexForName(AVStorage.Images.Media.DATA));
//                dataability:///media/external/images/media/30
                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(id));
                if (uri.toString().split(":///")[1].equals(inUri.toString().split("://")[1])) {
//                    System.out.println("匹配的参数===="+uri.toString());
                    FileDescriptor fd = helper.openFile(uri, "r");
                    try {
                        imageSource = ImageSource.create(fd, null);
                        pixelMap = imageSource.createPixelmap(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (imageSource != null) {
                            imageSource.release();
                        }
                    }

                    Glide.with(this)
                            .asDrawable()
                            .load(pixelMap)
                            .circleCrop()
                            .into(imageDL_);

                    Text txShowName_ = (Text) ((ComponentContainer) imageDL_.getComponentParent()).findComponentById(ResourceTable.Id_tx_show_name_);
                    txShowName_.setVisibility(Component.HIDE);
                    break;
                } else {
//                    string+="false";
//                    System.out.println("不匹配的参数===="+uri.toString());
                }
                System.out.println(uri.toString() + "====" + inUri.toString());

            }

        } catch (DataAbilityRemoteException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        if (requestCode == 2001 && resultCode == -1) {//成功
            System.out.println(resultData.getUri());
//            HmSharedPerfrence sharedPerfrence = new HmSharedPerfrence(this);
            HmSharedPerfrence sharedPerfrence = HmSharedPerfrence.getInstance(this);
            showImage2(resultData.getUri(), image_DL);
            sharedPerfrence.saveOtherInConFile("avater", resultData.getUriString());
        }
    }

    public String getName() {
//        String name = new HmSharedPerfrence(this).getOtherInConFile("device_name");
        String name = HmSharedPerfrence.getInstance(this).getOtherInConFile("device_name");
        if (name.length() > 0) {
            return name;
        } else {
            HmSharedPerfrence.getInstance(this).saveOtherInConFile("device_name", "SDK_GPHONE_X86_ARM");
            return "SDK_GPHONE_X86_ARM";
        }

    }

    private void showNameDialog(Text text, Text text1) {
        CommonDialog commonDialog = new CommonDialog(this);
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_input_dialog_with_icon_layout, null, false);
        Text inputDialogTitle = (Text) component.findComponentById(ResourceTable.Id_input_dialog_title);
        TextField inputDialogTextField = (TextField) component.findComponentById(ResourceTable.Id_input_dialog_text_field);
        inputDialogTitle.setText("请输入昵称");
        commonDialog.setContentCustomComponent(component);
        commonDialog.setAutoClosable(true);
        commonDialog.setTransparent(true);
        commonDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);

        inputDialogTextField.setText(text.getText());

        Text txShowName_ = (Text) component.findComponentById(ResourceTable.Id_tx_show_name_);
        txShowName_.setText(text1.getText());

        loadExiestImage((Image) component.findComponentById(ResourceTable.Id_image_DL));

        component.findComponentById(ResourceTable.Id_image_DL).setClickedListener(component12 -> {
            commonDialog.destroy();
            Intent intent = FileIntents.from(MainFractionAbilitySlice.this).pickImageFile().build();
            startAbilityForResult(intent, 2001);
        });

        Button remove = (Button) component.findComponentById(ResourceTable.Id_input_dialog_remove);
        remove.setClickedListener(component11 -> {
            commonDialog.destroy();
            image_DL.setPixelMap(null);
            HmSharedPerfrence sharedPerfrence = HmSharedPerfrence.getInstance(this);
            sharedPerfrence.saveOtherInConFile("avater", "");
            ((ComponentContainer) image_DL.getComponentParent())
                    .findComponentById(ResourceTable.Id_tx_show_name_)
                    .setVisibility(Component.VISIBLE);

        });

        Button ok = (Button) component.findComponentById(ResourceTable.Id_input_dialog_ok2);
        ok.setClickedListener(component1 -> {
            if (inputDialogTextField.getText().trim().length() > 0) {
                text.setText(inputDialogTextField.getText().trim());
                text1.setText(String.valueOf(inputDialogTextField.getText().trim().toLowerCase().charAt(0)));
//                HmSharedPerfrence perfrence = new HmSharedPerfrence(this);
                HmSharedPerfrence perfrence = HmSharedPerfrence.getInstance(this);
                perfrence.saveOtherInConFile("device_name", inputDialogTextField.getText().trim());
                commonDialog.destroy();
            } else {
                Toast.show(this, "请输入昵称");
            }
        });
        Button cancle = (Button) component.findComponentById(ResourceTable.Id_input_dialog_cancle);
        cancle.setClickedListener(component1 -> {
            commonDialog.destroy();
        });
        if (!commonDialog.isShowing()) commonDialog.show();
    }

    private PopupWindowView createdDialog(Component component, String[] items) {
        PopupEntity popupEntity = new PopupEntity();
        popupEntity.atView = component;
        return new PopupWindowView(getContext(), 0, 0, popupEntity)
                .setStringData(items, null);
    }

    private class SelectDealer implements PopupWindowView.OnSelectListener {

        @Override
        public void onSelect(int position, String text) {
            List<PopItemObject> startItems = new ArrayList<>();
            if (currentPage == 0) {
                startItems = PopConfig.createdItemObject1();
            } else if (currentPage == 1) {
                startItems = PopConfig.createdItemObject2();
            } else if (currentPage == 2) {
                startItems = PopConfig.createdItemObject3();
            }
            //递归展示
            List<String> itemNext = PopConfig.getItemNext(startItems, text);
            if (itemNext != null && itemNext.size() > 0) {
                String[] items = itemNext.toArray(new String[0]);
                if (items != null) {
                    createdDialog(moreMain, items).show().setOnSelectListener(new SelectDealer());
                    return;
                }
            }
            //处理事件
            if (text.isEmpty()) return;
            if (currentPage == 0) {
                firstPageClick(text, position);
            } else if (currentPage == 1) {

            } else if (currentPage == 2) {

            }

        }


        private void firstPageClick(String text, int position) {
            switch (text) {
                case PopConfig.MultipleChoose:
                    if (abilitySliceList.size() > 0) {
                        TransferGroupListFragment fraction = (TransferGroupListFragment) abilitySliceList.get(0);
                        fraction.setLongClickState(true);
                    }
                    break;
                case PopConfig.NOTHING:

                    break;
                case PopConfig.DATE: //区分分组、排序的
                    if (position != 1) {  //日期排序
                        String key = PopConfig.getKey(TransferGroupListFragment.class, PopConfig.SortBy);
                        PopConfig.putValue(getContext(), PopConfig.DATE, key);
                        startSort();
                    }
                    break;
                case PopConfig.SIZE:   //大小排序
                    String key = PopConfig.getKey(TransferGroupListFragment.class, PopConfig.SortBy);
                    PopConfig.putValue(getContext(), PopConfig.SIZE, key);
                    startSort();
                    break;
                case PopConfig.SORTSHUN:
                    PopConfig.putValue(getContext(), PopConfig.SORTSHUN, PopConfig.getKey(TransferGroupListFragment.class, PopConfig.SORT_TITLE));
                    startSort();
                    break;
                case PopConfig.SORTDAO:
                    PopConfig.putValue(getContext(), PopConfig.SORTDAO, PopConfig.getKey(TransferGroupListFragment.class, PopConfig.SORT_TITLE));
                    startSort();
                    break;
            }
        }

        private void startSort() {
            String keySortStyle = PopConfig.getKey(TransferGroupListFragment.class, PopConfig.SortBy);
            String value = PopConfig.getValue(getContext(), keySortStyle);
            boolean isTime = value == null || value.equals(PopConfig.DATE);

            String key = PopConfig.getKey(TransferGroupListFragment.class, PopConfig.SORT_TITLE);
            String value1 = PopConfig.getValue(getContext(), key);
            boolean isShun = value1 == null || value1.equals(PopConfig.SORTSHUN);

            TransferGroupListFragment fraction = (TransferGroupListFragment) abilitySliceList.get(0);
            if (isTime) {
                fraction.sortByTime(isShun);
            } else {
                fraction.sortBySize(isShun);
            }
        }
    }

}
