package me.panavtec.title.slice;

import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.utils.AddressedInterface;
import com.ohos.trebleshot.utils.NetworkUtils;
import me.panavtec.config.AppConfig;
import me.panavtec.dialog.WebShareDetailsDialog;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.WebShareACListProvider;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.subscriber.NetworkStatusSubscriber;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.VectorElement;
import ohos.bluetooth.BluetoothHost;
import ohos.event.commonevent.*;
import ohos.rpc.RemoteException;
import ohos.telephony.NetworkState;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.RadioStateObserver;
import ohos.wifi.WifiEvents;

import java.util.ArrayList;
import java.util.List;

import static ohos.telephony.RadioStateObserver.OBSERVE_MASK_NETWORK_STATE;

public class WebShareAbilitySlice extends AbilitySlice {
    public static final String EXTRA_WEBSERVER_START_REQUIRED = "extraStartRequired";

    private Image mPlayButton;
    private TextField mTextField;

    private CommonEventSubscriber mSubscriber;
    private CommonEventSubscriber mSubscriber_;

    private ListContainer mListContainer;
    private WebShareACListProvider mWebShareACListProvider;
    private RadioInfoManager mRadioInfoManager;
    private AnimatorProperty scaleAnimatorProperty;
    private boolean mPalyButtonOnTop = true;


    public void refreshList(){
        if(!HMUtils.isNetWorkAvailable(this)) {
            findComponentById(ResourceTable.Id_web_share_empty).setVisibility(Component.VISIBLE);
            findComponentById(ResourceTable.Id_active_connection_list).setVisibility(Component.HIDE);
        }else {
            findComponentById(ResourceTable.Id_web_share_empty).setVisibility(Component.HIDE);
            findComponentById(ResourceTable.Id_active_connection_list).setVisibility(Component.VISIBLE);

            List<WebShareACListProvider.ACInfo> resultList = new ArrayList<>();
            List<AddressedInterface> interfaceList = NetworkUtils.getInterfaces(true,
                    AppConfig.DEFAULT_DISABLED_INTERFACES);

            for (AddressedInterface addressedInterface : interfaceList) {
                WebShareACListProvider.ACInfo acInfo = new WebShareACListProvider.ACInfo(addressedInterface.getNetworkInterface().getName(),
                        addressedInterface.getAssociatedAddress());

                if (filterItem(acInfo))
                    resultList.add(acInfo);
            }
            mWebShareACListProvider.setList(resultList);
            mWebShareACListProvider.filteredByTitle(mTextField.getText());
            mWebShareACListProvider.notifyDataChanged();
        }
    }

    public boolean filterItem(WebShareACListProvider.ACInfo item)
    {
//        String[] filteringKeywords = getFragment()
//                .getFilteringDelegate()
//                .getFilteringKeyword(getFragment());
//
//        return filteringKeywords == null
//                || filteringKeywords.length <= 0
//                || item.applyFilter(filteringKeywords);
        return true;
    }


    private void initListContainer() {
        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_active_connection_list);
        mWebShareACListProvider = new WebShareACListProvider(null, this);
        try {
            mListContainer.setItemProvider(mWebShareACListProvider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClickListener() {
        mListContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                WebShareACListProvider.ACInfo item = (WebShareACListProvider.ACInfo) listContainer.getItemProvider().getItem(position);
                Component componentDlg = LayoutScatter.getInstance(getContext())
                        .parse(ResourceTable.Layout_layout_web_share_details,null, false);
                WebShareDetailsDialog webShareDetailsDialog = new WebShareDetailsDialog(getContext(), componentDlg, item.getIP());
                webShareDetailsDialog.setContentCustomComponent(componentDlg);
                webShareDetailsDialog.setAutoClosable(true);
                webShareDetailsDialog.show();
            }
        });

    }

    public class CommonEventSubscriberExt extends CommonEventSubscriber {
        public CommonEventSubscriberExt(CommonEventSubscribeInfo subscribeInfo) {
            super(subscribeInfo);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            updateWebShareStatus(commonEventData.getIntent().getBooleanParam(CommunicationService.EXTRA_STATUS_STARTED,
                    false));
        }
    }

    public class CommonEventSubscriberExt_ extends CommonEventSubscriber {
        public CommonEventSubscriberExt_(CommonEventSubscribeInfo subscribeInfo) {
            super(subscribeInfo);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            refreshList();
        }
    }


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_abilityslice_web_share);

        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(CommunicationService.ACTION_WEBSHARE_STATUS);
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        mSubscriber = new CommonEventSubscriberExt(subscribeInfo);


        MatchingSkills matchingSkills1 = new MatchingSkills();

        matchingSkills1.addEvent(BluetoothHost.EVENT_HOST_STATE_UPDATE);
        matchingSkills1.addEvent(CommunicationService.ACTION_HOTSPOT_STATUS);
        matchingSkills1.addEvent(NetworkStatusSubscriber.WIFI_AP_STATE_CHANGED);
        matchingSkills1.addEvent(WifiEvents.EVENT_ACTIVE_STATE);
        matchingSkills1.addEvent(WifiEvents.EVENT_CONN_STATE);
        matchingSkills1.addEvent(WifiEvents.EVENT_P2P_STATE_CHANGED);
        matchingSkills1.addEvent(WifiEvents.EVENT_P2P_CONN_STATE_CHANGED);

        CommonEventSubscribeInfo subscribeInfo1 = new CommonEventSubscribeInfo(matchingSkills1);
        mSubscriber_ = new CommonEventSubscriberExt_(subscribeInfo1);

        mRadioInfoManager = RadioInfoManager.getInstance(this);
        mRadioInfoManager.addObserver(new RadioStateObserver(0){
            @Override
            public void onNetworkStateUpdated(NetworkState networkState){
                refreshList();
            }
        }, OBSERVE_MASK_NETWORK_STATE);

        initListContainer();
        setClickListener();

        mPlayButton = (Image) findComponentById(ResourceTable.Id_play_pause_btn);
        mPlayButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                toggleWebShare(false);
            }
        });

        scaleAnimatorProperty = mPlayButton.createAnimatorProperty();

        mTextField = (TextField) findComponentById(ResourceTable.Id_search_box_control);
        mTextField.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String s, int i, int i1, int i2) {
                mWebShareACListProvider.filteredByTitle(mTextField.getText());
                mWebShareACListProvider.notifyDataChanged();
            }
        });

        refreshList();

        findComponentById(ResourceTable.Id_previous_button).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                onBackPressed();
            }
        });

        findComponentById(ResourceTable.Id_search_icon).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                findComponentById(ResourceTable.Id_common_container).setVisibility(Component.HIDE);
                findComponentById(ResourceTable.Id_search_container).setVisibility(Component.VISIBLE);
                mTextField.setText("");
                mTextField.requestFocus();
            }
        });

        findComponentById(ResourceTable.Id_close_icon).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                findComponentById(ResourceTable.Id_common_container).setVisibility(Component.VISIBLE);
                findComponentById(ResourceTable.Id_search_container).setVisibility(Component.HIDE);
                mTextField.setText("");
            }
        });
        
        if (intent != null && intent.hasParameter(EXTRA_WEBSERVER_START_REQUIRED)
                && intent.getBooleanParam(EXTRA_WEBSERVER_START_REQUIRED, false))
            toggleWebShare(true);
    }


    public void moveAnim(boolean play){
        if(play && mPalyButtonOnTop) {
            AnimatorProperty animatorProperty = mPlayButton.createAnimatorProperty();
            mPalyButtonOnTop = false;
            scaleAnimatorProperty.cancel();
            animatorProperty.moveFromY(mPlayButton.getComponentPosition().top).moveToY(mPlayButton.getComponentPosition().top + 1000).setDuration(500);
            animatorProperty.start();
        }else if(!play && !mPalyButtonOnTop) {
            mPalyButtonOnTop = true;
            AnimatorProperty animatorProperty = mPlayButton.createAnimatorProperty();
            animatorProperty.moveFromY(mPlayButton.getComponentPosition().top + 1000).moveToY(mPlayButton.getComponentPosition().top).setDuration(500);
            animatorProperty.start();
            scaleAnim();
        }
    }

    public void scaleAnim(){
        scaleAnimatorProperty.scaleXFrom(1.0f).scaleX(1.5f).setDuration(1000);
        scaleAnimatorProperty.scaleYFrom(1.0f).scaleY(1.5f).setDuration(1000);
        scaleAnimatorProperty.setLoopedCount(1000000);
        scaleAnimatorProperty.start();
    }

    @Override
    protected void onActive() {
        super.onActive();
        try {
            if(mPalyButtonOnTop) {
                scaleAnim();
            }
            CommonEventManager.subscribeCommonEvent(mSubscriber);
            CommonEventManager.subscribeCommonEvent(mSubscriber_);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        requestWebShareStatus();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        try {
            scaleAnimatorProperty.cancel();
            CommonEventManager.unsubscribeCommonEvent(mSubscriber);
            CommonEventManager.unsubscribeCommonEvent(mSubscriber_);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void requestWebShareStatus() {
        HMUtils.startAbility(this,CommunicationService.class.getName(), CommunicationService.ACTION_REQUEST_WEBSHARE_STATUS);
    }

    public void toggleWebShare(boolean forceStart) {
        Intent intent = new Intent();

        if (forceStart)
            intent.setParam(CommunicationService.EXTRA_TOGGLE_WEBSHARE_START_ALWAYS, true);

        HMUtils.startAbility(this,intent, CommunicationService.class.getName(), CommunicationService.ACTION_TOGGLE_WEBSHARE);
    }

    public void updateWebShareStatus(boolean running) {
        VectorElement element = new VectorElement(this, ResourceTable.Graphic_ic_pause_white_24dp);
        VectorElement element1 = new VectorElement(this, ResourceTable.Graphic_ic_play_arrow_white_24dp);

        ShapeElement sp1 = new ShapeElement();
        sp1.setRgbColor(RgbColor.fromArgbInt(0xffafb42b));
        sp1.setCornerRadius(400);

        ShapeElement sp2 = new ShapeElement();
        sp2.setRgbColor(RgbColor.fromArgbInt(0xff66bb6a));
        sp2.setCornerRadius(400);

        mPlayButton.setBackground(running? sp1: sp2);
        mPlayButton.setImageElement(running ? element : element1);

        moveAnim(running);
    }
}
