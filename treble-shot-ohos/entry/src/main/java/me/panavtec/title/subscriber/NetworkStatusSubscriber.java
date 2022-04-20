package me.panavtec.title.subscriber;

import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.HotspotUtils;
import me.panavtec.title.MyApplication;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.data.preferences.Preferences;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.CommonEventSubscriber;
import ohos.wifi.WifiDevice;
import ohos.wifi.WifiEvents;

public class NetworkStatusSubscriber extends CommonEventSubscriber
{
    public static final String WIFI_AP_STATE_CHANGED = WifiEvents.EVENT_CONN_STATE;

    public NetworkStatusSubscriber(CommonEventSubscribeInfo subscribeInfo) {
        super(subscribeInfo);
    }

    protected void evaluateTheCondition(WifiDevice info, final Context context, Preferences preferences)
    {
        if (preferences.getBoolean("allow_autoconnect", false) && info.isConnected())
            HMUtils.startAbility(context, CommunicationService.class.getName(), "");

        if (preferences.getBoolean("scan_devices_auto", false) && info.isConnected())
            new Thread()
            {
                @Override
                public void run()
                {
                    super.run();
                    try {
                        // The interface may not be created properly yet and we should give some time
                        Thread.sleep(1700);
                        scanDevicesStart(context);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
    }

    @Override
    public void onReceiveEvent(CommonEventData commonEventData) {
        Intent intent = commonEventData.getIntent();
        Context context = MyApplication.mContext;
        Preferences preferences = AppUtils.getPreferences(context);

        if (WIFI_AP_STATE_CHANGED.equals(intent.getAction())) {
            HotspotUtils hotspotUtils = HotspotUtils.getInstance(null);

            if (WifiEvents.STATE_DISCONNECTED
                    == intent.getIntParam(WifiEvents.EVENT_ACTIVE_STATE, -1) % 10)
                hotspotUtils.unloadPreviousConfig();
        }

//        if (intent.hasParameter("networkInfo"))
            evaluateTheCondition(WifiDevice.getInstance(context), context, preferences);

    }

    private void scanDevicesStart(Context context){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(CommunicationService.class)
                .withAction(CommunicationService.ACTION_DEVICE_DISCOVERY)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent, 0);
    }
}
