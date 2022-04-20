package me.panavtec.title.fraction;

import com.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import com.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import com.ohos.trebleshot.config.Keyword;
import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.ui.UIConnectionUtils;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.ConnectionUtils;
import com.ohos.trebleshot.utils.Settings;
import me.panavtec.dialog.AlertDialog;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.adapterBase.CustFraction;
import me.panavtec.title.adapterBase.IconSupport;
import me.panavtec.title.adapterBase.TitleSupport;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.subscriber.NetworkStatusSubscriber;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.event.commonevent.*;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;
import ohos.rpc.RemoteException;
import ohos.wifi.WifiLinkedInfo;
import org.json.JSONObject;

/**
 * created by: veli
 * date: 11/04/18 20:53
 */
public class HotspotMgrFraction extends CustFraction implements TitleSupport, IconSupport
{
    public static final int REQUEST_LOCATION_PERMISSION_FOR_HOTSPOT = 643;

    MatchingSkills matchingSkills = new MatchingSkills();
    private UIConnectionUtils mConnectionUtils;

    private Component mContainerText1;
    private Component mContainerText2;
    private Component mContainerText3;
    private Text mText1;
    private Text mText2;
    private Text mText3;
    private Image mCodeView;
    private Button mToggleButton;
    private Image mHelpAction;
    private boolean mWaitForHotspot = false;
    private boolean mWaitForWiFi = false;
    private boolean mHotspotStartedExternally = false;

    private UIConnectionUtils.RequestWatcher mHotspotWatcher = new UIConnectionUtils.RequestWatcher()
    {
        @Override
        public void onResultReturned(boolean result, boolean shouldWait)
        {
            mWaitForHotspot = shouldWait;
        }
    };

    private UIConnectionUtils.RequestWatcher mWiFiWatcher = new UIConnectionUtils.RequestWatcher()
    {
        @Override
        public void onResultReturned(boolean result, boolean shouldWait)
        {
            mWaitForWiFi = shouldWait;
        }
    };
    private CommonEventSubscriber mSubscriber;

    public HotspotMgrFraction() {
        super();
    }

    @Override
    public void onStart(Intent intent)
    {
        super.onStart(intent);

        matchingSkills.addEvent(CommunicationService.ACTION_HOTSPOT_STATUS);
        matchingSkills.addEvent(NetworkStatusSubscriber.WIFI_AP_STATE_CHANGED);

        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        mSubscriber = new CommonEventSubscriberEx(subscribeInfo);

        mHelpAction = (Image) getComponent().findComponentById(ResourceTable.Id_help_icon);
        mHelpAction.setClickedListener(this::onOptionsItemSelected);
        showMenu();
    }

    public boolean onOptionsItemSelected(Component component)
    {

        if (component.getId() == ResourceTable.Id_help_icon && getConnectionUtils().getHotspotUtils().getConfiguration() != null) {
            String hotspotName = getConnectionUtils().getHotspotUtils().getConfiguration().getSsid();
            String friendlyName = AppUtils.getFriendlySSID(hotspotName);

            new AlertDialog(this)
                    .setMessage(getString(ResourceTable.String_mesg_hotspotCreatedInfo, hotspotName, friendlyName))
                    .setPositiveButton(ResourceTable.String_ok, null)
                    .show();
        }
        return true;
    }

//    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_LOCATION_PERMISSION_FOR_HOTSPOT == requestCode)
            toggleHotspot();
    }

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        Component component= scatter.parse(ResourceTable.Layout_layout_hotspot_manager, container, false);

        //mColorPassiveState = ColorStateList.valueOf(ContextCompat.getColor(getContext(), AppUtils.getReference(ResourceTable.attr.colorPassive)));
        mCodeView = (Image) component.findComponentById(ResourceTable.Id_layout_hotspot_manager_qr_image);
        mToggleButton = (Button) component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_toggle_button);
        mContainerText1 = component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_container_text1_container);
        mContainerText2 = component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_container_text2_container);
        mContainerText3 = component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_container_text3_container);
        mText1 = (Text) component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_container_text1);
        mText2 = (Text) component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_container_text2);
        mText3 = (Text) component.findComponentById(ResourceTable.Id_layout_hotspot_manager_info_container_text3);

        mToggleButton.setClickedListener(new Component.ClickedListener()
        {
            @Override
            public void onClick(Component component)
            {
                toggleHotspot();
            }
        });

        return component;
    }

    @Override
    public void onActive()
    {
        super.onActive();
//        try {
//            CommonEventManager.subscribeCommonEvent(mSubscriber);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        updateState();
//
//        if (mWaitForHotspot)
//            toggleHotspot();
    }

    @Override
    public void onInactive()
    {
        super.onInactive();
        try {
            CommonEventManager.unsubscribeCommonEvent(mSubscriber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ConnectionUtils getConnectionUtils()
    {
        return getUIConnectionUtils().getConnectionUtils();
    }

    @Override
    public int getIconRes()
    {
        return ResourceTable.Graphic_ic_wifi_tethering_white_24dp;
    }

    public UIConnectionUtils getUIConnectionUtils()
    {
        if (mConnectionUtils == null)
            mConnectionUtils = new UIConnectionUtils(ConnectionUtils.getInstance(getContext()), this);

        return mConnectionUtils;
    }

    @Override
    public String getTitle(Context context)
    {
        return context.getString(ResourceTable.String_butn_startHotspot);
    }

    private void toggleHotspot()
    {
        if (mHotspotStartedExternally)
            HMUtils.startAbility(this, null, Settings.ACTION_WIRELESS_SETTINGS);
        else
            getUIConnectionUtils().toggleHotspot(true, this.getFractionAbility(), REQUEST_LOCATION_PERMISSION_FOR_HOTSPOT, mHotspotWatcher);
    }

    private void updateViewsWithBlank()
    {
        mHotspotStartedExternally = false;

        updateViews(null,
                getString(ResourceTable.String_text_qrCodeHotspotDisabledHelp),
                null,
                null,
                ResourceTable.String_butn_startHotspot);
    }

    private void updateViewsStartedExternally()
    {
        mHotspotStartedExternally = true;

        updateViews(null, getString(ResourceTable.String_text_hotspotStartedExternallyNotice),
                null, null, ResourceTable.String_butn_stopHotspot);
    }

    // for hotspot
    private void updateViews(String networkName, String password, int keyManagement)
    {
        mHotspotStartedExternally = false;

        try {
            JSONObject object = new JSONObject()
                    .put(Keyword.NETWORK_NAME, networkName)
                    .put(Keyword.NETWORK_PASSWORD, password)
                    .put(Keyword.NETWORK_KEYMGMT, keyManagement);

            updateViews(object, getString(ResourceTable.String_text_qrCodeAvailableHelp), networkName, password,
                    ResourceTable.String_butn_stopHotspot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateViews(JSONObject codeIndex,
                             String text1,
                             String text2,
                             String text3,
                             int buttonText)
    {
        boolean showQRCode = codeIndex != null
                && codeIndex.length() > 0
                && getContext() != null;

        try {

            if (showQRCode) {
                {
                    int networkPin = AppUtils.getUniqueNumber();

                    codeIndex.put(Keyword.NETWORK_PIN, networkPin);

                    AppUtils.getPreferences(getContext())
                            .putInt(Keyword.NETWORK_PIN, networkPin).flush();
                }

                try {
                    new EventHandler(EventRunner.create("QRCodeGen")).postTask(() -> {
                        PixelMap pixelMap =  QRCodeEncoder.syncEncodeQRCode(codeIndex.toString(), BGAQRCodeUtil.dp2px(this, 160));
                        this.getUITaskDispatcher().asyncDispatch(() -> {
                            mCodeView.setPixelMap(pixelMap);
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                mCodeView.setPixelMap(ResourceTable.Graphic_ic_qrcode_white_128dp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mContainerText1.setVisibility(text1 == null ? Component.INVISIBLE : Component.VISIBLE);
            mContainerText2.setVisibility(text2 == null ? Component.INVISIBLE : Component.VISIBLE);
            mContainerText3.setVisibility(text3 == null ? Component.INVISIBLE : Component.VISIBLE);

            mText1.setText(text1);
            mText2.setText(text2);
            mText3.setText(text3);
            mToggleButton.setText(buttonText);
        }
    }

    private void showMenu()
    {
        if (mHelpAction != null) {
            mHelpAction.setVisibility(getConnectionUtils().getHotspotUtils().getConfiguration() != null
                    && getConnectionUtils().getHotspotUtils().isEnabled() ? Component.VISIBLE : Component.INVISIBLE );
        }
    }

    private void updateState()
    {
        boolean isEnabled = getUIConnectionUtils().getConnectionUtils().getHotspotUtils().isEnabled();
        WifiLinkedInfo wifiConfiguration = getConnectionUtils().getHotspotUtils().getConfiguration();

        showMenu();

        if (!isEnabled) {
            updateViewsWithBlank();
//        } else if (getConnectionUtils().getHotspotUtils() instanceof HotspotUtils.HackAPI
//                && wifiConfiguration != null) {
//            updateViews(wifiConfiguration.getSsid(), wifiConfiguration.preSharedKey, NetworkUtils.getAllowedKeyManagement(wifiConfiguration));
        } else
            HMUtils.startAbility(this, CommunicationService.class.getName(), CommunicationService.ACTION_REQUEST_HOTSPOT_STATUS);
    }

    private class CommonEventSubscriberEx extends CommonEventSubscriber
    {
        public CommonEventSubscriberEx(CommonEventSubscribeInfo subscribeInfo) {
            super(subscribeInfo);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            Intent intent = commonEventData.getIntent();
            if (NetworkStatusSubscriber.WIFI_AP_STATE_CHANGED.equals(intent.getAction()))
                updateState();
            else if (CommunicationService.ACTION_HOTSPOT_STATUS.equals(intent.getAction())) {
                if (intent.getBooleanParam(CommunicationService.EXTRA_HOTSPOT_ENABLED, false))
                    updateViews(intent.getStringParam(CommunicationService.EXTRA_HOTSPOT_NAME),
                            intent.getStringParam(CommunicationService.EXTRA_HOTSPOT_PASSWORD),
                            intent.getIntParam(CommunicationService.EXTRA_HOTSPOT_KEY_MGMT, 0));
                else if (getConnectionUtils().getHotspotUtils().isEnabled()
                        && !intent.getBooleanParam(CommunicationService.EXTRA_HOTSPOT_DISABLING, false)) {
                    updateViewsStartedExternally();
                }
            }
        }
    }
}
