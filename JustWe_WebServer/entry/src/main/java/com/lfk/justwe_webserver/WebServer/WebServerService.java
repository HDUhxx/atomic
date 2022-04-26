package com.lfk.justwe_webserver.WebServer;

import com.lfk.justwe_webserver.ResourceTable;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.LocalRemoteObject;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.event.intentagent.IntentAgentInfo;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.global.resource.Resource;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.wifi.WifiDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for Ohos
 *
 * @author liufengkai
 *         Created by liufengkai on 16/1/6.
 */
public class WebServerService extends Ability {
    private WebServer.MessageHandler logResult;
    private NotificationRequest notification;
    private final IRemoteObject mBinder = new LocalBinder();
    private Servers webServers;
    private static Ability engine;
    private IntentAgent contentIntent;
    private boolean IsRunning;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        updateNotification("Open Service");
    }

    public static void init(Ability engine) {
        WebServerService.engine = engine;
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(engine.getBundleName())
                .withAbilityName(WebServerService.class)
                .build();
        intent.setOperation(operation);
        engine.startAbility(intent);
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return mBinder;
    }

    private void updateNotification(String text) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName(engine.getClass())
                .build();
        intent.setOperation(operation);
        List<Intent> intentList = new ArrayList<Intent>();
        intentList.add(intent);
        List<IntentAgentConstant.Flags> flags = new ArrayList<>();
        flags.add(IntentAgentConstant.Flags.ONE_TIME_FLAG);
        IntentAgentInfo paramsInfo = new IntentAgentInfo(0, IntentAgentConstant.OperationType.START_ABILITY, flags, intentList, null);
        contentIntent = IntentAgentHelper.getIntentAgent(this, paramsInfo);

        NotificationRequest.NotificationPictureContent builder = new NotificationRequest.NotificationPictureContent()
                .setTitle("WebServer")
                .setText(text)
                .setBigPicture(getPixelMap(this, ResourceTable.Media_icon));
        notification = new NotificationRequest(1);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(builder);
        notification.setContent(notificationContent);
        notification.setIntentAgent(contentIntent);
        try {
            NotificationHelper.publishNotification(notification);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private PixelMap getPixelMap(Context context, int imgRes){
        byte[] imgData = null;
        try{
            Resource imgResource = context.getResourceManager().getResource(imgRes);
            int imgLength = imgResource.available();
            imgData = new byte[imgLength];
            imgResource.read(imgData);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(imgData == null)
            return null;
        ImageSource imageSource = ImageSource.create(imgData, null);
        return imageSource.createPixelmap(null);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public class LocalBinder extends LocalRemoteObject {
        WebServerService getService() {
            return WebServerService.this;
        }
    }

    public void startServer(WebServer.MessageHandler logResult, int port) throws RemoteException {
        this.logResult = logResult;
        // running
        setIsRunning(true);

//        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        WifiDevice wifiDevice = WifiDevice.getInstance(this);

        WebServerDefault.setWebServerIp(WebServerDefault.intToIp(wifiDevice.getIpInfo().get().getIpAddress()));
        // if not in wifi
        if (!wifiDevice.isConnected()) {
            this.logResult.OnError("Please connect to a WIFI-network.");
            try {
                throw new Exception("Please connect to a WIFI-network.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        webServers = new Servers(engine.getApplicationContext(), logResult, port);
        webServers.start();

        updateNotification("running on " +
                WebServerDefault.WebServerIp + ":" + port);
    }

    public void stopServer() {
        setIsRunning(false);
        if(webServers != null) {
            webServers.stopServer();
            webServers.interrupt();
        }
    }

    public void setIsRunning(boolean isRunning) {
        IsRunning = isRunning;
    }
}
