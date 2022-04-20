package com.ohos.trebleshot.ui;

import com.ohos.trebleshot.config.Keyword;
import com.ohos.trebleshot.coolsocket.CoolSocket;
import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.log.Log;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.object.TransferGroup;
import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.service.WorkerService;
import com.ohos.trebleshot.utils.*;
import me.panavtec.dialog.AlertDialog;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.adapterBase.SnackbarSupport;
import me.panavtec.title.hmadapter.NetworkDeviceListProvider;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.utils.net.Uri;
import ohos.wifi.WifiLinkedInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by: veli
 * date: 15/04/18 18:44
 */
public class UIConnectionUtils {

    private SnackbarSupport mSnackbarSupport;
    private boolean mWirelessEnableRequested = false;
    private ConnectionUtils mConnectionUtils;
    public UIConnectionUtils(ConnectionUtils connectionUtils, SnackbarSupport snackbarSupport)
    {
        mConnectionUtils = connectionUtils;
        mSnackbarSupport = snackbarSupport;
    }


    public ConnectionUtils getConnectionUtils()
    {
        return mConnectionUtils;
    }

    public static void setupConnection(Context context, String ipAddress, int accessPin,
                                                NetworkDeviceLoader.OnDeviceRegisteredListener listener,
                                                Component.ClickedListener retryButtonListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                setupConnectionNoThread(context,ipAddress,accessPin,listener,retryButtonListener);
            }
        }).start();
    }
    //线程中运行 发起连接
    public static NetworkDevice setupConnectionNoThread(Context context, String ipAddress, int accessPin,
                                       NetworkDeviceLoader.OnDeviceRegisteredListener listener,
                                       Component.ClickedListener retryButtonListener) {
        return CommunicationBridge.connect(AppUtils.getDatabase(context), NetworkDevice.class, client -> {
            try {
                client.setSecureKey(accessPin);

                CoolSocket.ActiveConnection activeConnection = client.connectWithHandshake(ipAddress, false);
                NetworkDevice device = client.loadDevice(activeConnection);
                System.out.println("+++======================"+device.toString());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Keyword.REQUEST, Keyword.REQUEST_ACQUAINTANCE);
                activeConnection.reply(jsonObject.toString());

                JSONObject receivedReply = new JSONObject(activeConnection.receive().response);

                if (receivedReply.has(Keyword.RESULT)
                        && receivedReply.getBoolean(Keyword.RESULT)
                        && device.deviceId != null) {
                    final NetworkDevice.Connection connection = NetworkDeviceLoader.processConnection(AppUtils.getDatabase(context), device, ipAddress);

                    device.lastUsageTime = System.currentTimeMillis();
                    device.tmpSecureKey = accessPin;
                    device.isRestricted = false;
                    device.isTrusted = true;
                    AppUtils.getDatabase(context).publish(device);
                    new EventHandler(EventRunner.getMainEventRunner()).postTask(() -> {

                        if (listener != null)
                            listener.onDeviceRegistered(AppUtils.getDatabase(context), device, connection);
                    });
                } else {
                    showConnectionRejectionInformation(context, device, receivedReply, retryButtonListener);
//                    System.out.println("请求被拒绝");
//                    //todo 拒绝
//                    new EventHandler(EventRunner.getMainEventRunner()).postTask(() -> {
//                        if (listener != null) listener.onError(new Exception("请求被拒绝"));
//                    });
                }
                client.setReturn(device);
            } catch (Exception e) {
                new EventHandler(EventRunner.getMainEventRunner()).postTask(() -> {
                    if(listener != null && listener instanceof NetworkDeviceLoader.OnDeviceRegisteredErrorListener)
                       ((NetworkDeviceLoader.OnDeviceRegisteredErrorListener)listener).onError(e);
                });
                e.printStackTrace();
            }

        });
    }

    //同步连接
    public static NetworkDevice setupConnectionSync(final Context context,
                                         final String ipAddress,
                                         final int accessPin,
                                         final NetworkDeviceLoader.OnDeviceRegisteredListener listener) {
        return CommunicationBridge.connect(AppUtils.getDatabase(context), NetworkDevice.class, new CommunicationBridge.Client.ConnectionHandler() {
            @Override
            public void onConnect(CommunicationBridge.Client client) {
                try {
                    client.setSecureKey(accessPin);

                    CoolSocket.ActiveConnection activeConnection = client.connectWithHandshake(ipAddress, false);
                    NetworkDevice device = client.loadDevice(activeConnection);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Keyword.REQUEST, Keyword.REQUEST_ACQUAINTANCE);
                    activeConnection.reply(jsonObject.toString());

                    JSONObject receivedReply =  new JSONObject(activeConnection.receive().response);

                    if (receivedReply.has(Keyword.RESULT)
                            && receivedReply.getBoolean(Keyword.RESULT)
                            && device.deviceId != null) {
                        final NetworkDevice.Connection connection = NetworkDeviceLoader.processConnection(AppUtils.getDatabase(context), device, ipAddress);

                        device.lastUsageTime = System.currentTimeMillis();
                        device.tmpSecureKey = accessPin;
                        device.isRestricted = false;
                        device.isTrusted = true;

                        AppUtils.getDatabase(context).publish(device);

                        if (listener != null)
                            listener.onDeviceRegistered(AppUtils.getDatabase(context), device, connection);
                    } else {
                        //todo 拒绝
                    }

                    client.setReturn(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public boolean toggleHotspot(boolean conditional,
                                 final FractionAbility fractionAbility,
                                 final int locationPermRequestId,
                                 final RequestWatcher watcher)
    {
        if (!HotspotUtils.isSupported())
            return false;

        Component.ClickedListener defaultNegativeListener = new Component.ClickedListener()
        {
            @Override
            public void onClick(Component component)
            {
                watcher.onResultReturned(false, false);
            }
        };

        if (conditional) {
            if (!validateLocationPermission(fractionAbility, locationPermRequestId, watcher))
                return false;

            if (false) {//!Settings.System.canWrite(getConnectionUtils().getContext())) {
                new AlertDialog(getConnectionUtils().getContext())
                        .setMessage(ResourceTable.String_mesg_errorHotspotPermission)
                        .setNegativeButton(ResourceTable.String_butn_cancel, defaultNegativeListener)
                        .setPositiveButton(ResourceTable.String_butn_settings, new Component.ClickedListener()
                        {
                            @Override
                            public void onClick(Component component)
                            {
                                HMUtils.startAbility(getConnectionUtils().getContext(), new Intent().setUri(Uri.parse("package:" + getConnectionUtils().getContext().getBundleName())),
                                        null, Settings.ACTION_MANAGE_WRITE_SETTINGS);

                                watcher.onResultReturned(false, true);
                            }
                        })
                        .show();

                return false;
            } else if (!getConnectionUtils().getHotspotUtils().isEnabled()
                    && getConnectionUtils().isMobileDataActive()) {
                new AlertDialog(getConnectionUtils().getContext())
                        .setMessage(ResourceTable.String_mesg_warningHotspotMobileActive)
                        .setNegativeButton(ResourceTable.String_butn_cancel, defaultNegativeListener)
                        .setPositiveButton(ResourceTable.String_butn_skip, component -> {
                            // no need to call watcher due to recycle
                            toggleHotspot(false, fractionAbility, locationPermRequestId, watcher);
                        })
                        .show();

                return false;
            }
        }

        WifiLinkedInfo wifiConfiguration = getConnectionUtils().getHotspotUtils().getConfiguration();

        if (!getConnectionUtils().getHotspotUtils().isEnabled()
                || (wifiConfiguration != null && AppUtils.getHotspotName(getConnectionUtils().getContext()).equals(wifiConfiguration.getSsid())))
            getSnackbarSupport().createSnackbar(getConnectionUtils().getHotspotUtils().isEnabled()
                    ? ResourceTable.String_mesg_stoppingSelfHotspot
                    : ResourceTable.String_mesg_startingSelfHotspot)
                    .show();

        HMUtils.startAbility(getConnectionUtils().getContext(), CommunicationService.class.getName(),
                CommunicationService.ACTION_TOGGLE_HOTSPOT);

        watcher.onResultReturned(true, false);

        return true;
    }

    public boolean validateLocationPermission(final FractionAbility fractionAbility, final int requestId, final RequestWatcher watcher)
    {

        final Component.ClickedListener defaultNegativeListener = new Component.ClickedListener()
        {
            @Override
            public void onClick(Component component)
            {
                watcher.onResultReturned(false, false);
            }
        };

        if (!getConnectionUtils().hasLocationPermission(getConnectionUtils().getContext())) {
            new AlertDialog(getConnectionUtils().getContext())
                    .setMessage(ResourceTable.String_mesg_locationPermissionRequiredSelfHotspot)
                    .setNegativeButton(ResourceTable.String_butn_cancel, defaultNegativeListener)
                    .setPositiveButton(ResourceTable.String_butn_ask, new Component.ClickedListener()
                    {
                        @Override
                        public void onClick(Component component)
                        {
                            watcher.onResultReturned(false, true);
                            // No, I am not going to add an if statement since when it is not needed
                            // the main method returns true.
//                            fractionAbility.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                    Manifest.permission.ACCESS_COARSE_LOCATION}, requestId);
                        }
                    })
                    .show();

            return false;
        }

        if (!getConnectionUtils().isLocationServiceEnabled()) {
            new AlertDialog(getConnectionUtils().getContext())
                    .setMessage(ResourceTable.String_mesg_locationDisabledSelfHotspot)
                    .setNegativeButton(ResourceTable.String_butn_cancel, defaultNegativeListener)
                    .setPositiveButton(ResourceTable.String_butn_locationSettings, new Component.ClickedListener()
                    {
                        @Override
                        public void onClick(Component component)
                        {
                            watcher.onResultReturned(false, true);
                            HMUtils.startAbility(fractionAbility, null, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        }
                    })
                    .show();

            return false;
        }

        watcher.onResultReturned(true, false);

        return true;
    }

    public SnackbarSupport getSnackbarSupport()
    {
        return mSnackbarSupport;
    }

    public void makeAcquaintance(final FractionAbility fractionAbility, final UITask task, final Object object, final int accessPin,
                                 final NetworkDeviceLoader.OnDeviceRegisteredListener registerListener)
    {
        WorkerService.RunningTask runningTask = new WorkerService.RunningTask()
        {
            private boolean mConnected = false;
            private String mRemoteAddress;

            @Override
            public void onRun()
            {
                final Component.ClickedListener retryButtonListener = new Component.ClickedListener()
                {
                    @Override
                    public void onClick(Component component)
                    {
                        makeAcquaintance(fractionAbility, task, object, accessPin, registerListener);
                    }
                };

                try {
                    if (object instanceof NetworkDeviceListProvider.HotspotNetwork) {
                        mRemoteAddress = getConnectionUtils().establishHotspotConnection(getInterrupter(),
                                (NetworkDeviceListProvider.HotspotNetwork) object,
                                (delimiter, timePassed) -> timePassed >= 30000);
                    } else if (object instanceof String)
                        mRemoteAddress = (String) object;

                    if (mRemoteAddress != null) {
                        mConnected = setupConnectionNoThread(fractionAbility, mRemoteAddress, accessPin, new NetworkDeviceLoader.OnDeviceRegisteredListener()
                        {
                            @Override
                            public void onDeviceRegistered(final AccessDatabase database, final NetworkDevice device, final NetworkDevice.Connection connection)
                            {
                                // we may be working with direct IP scan
                                new EventHandler(EventRunner.create(true)).postTask(() -> {
                                    if (registerListener != null)
                                        registerListener.onDeviceRegistered(database, device, connection);
                                });
                            }
                        }, retryButtonListener) != null;
                    }

                    if (!mConnected && !getInterrupter().interruptedByUser())
                        new EventHandler(EventRunner.create(true)).postTask(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                AlertDialog alertDialog = new AlertDialog(fractionAbility)
                                        .setMessage(ResourceTable.String_mesg_connectionFailure)
                                        .setNegativeButton(ResourceTable.String_butn_close, null)
                                        .setPositiveButton(ResourceTable.String_butn_retry, retryButtonListener);

                                if (object instanceof NetworkDevice)
                                    alertDialog.setTitle(((NetworkDevice) object).nickname);

                                alertDialog.show();
                            }
                        });
                } catch (Exception e) {

                } finally {
                    new EventHandler(EventRunner.create(true)).postTask(() -> {
                        if (task != null && !fractionAbility.isTerminating())
                            task.updateTaskStopped();
                    });
                }
                // We can't add dialog outside of the else statement as it may close other dialogs as well
            }
        }.setTitle(fractionAbility.getString(ResourceTable.String_mesg_completing))
                .setIconRes(ResourceTable.Graphic_ic_compare_arrows_white_24dp);

//        runningTask.run(fractionAbility);

        if (task != null)
            task.updateTaskStarted(runningTask.getInterrupter());
    }

    public boolean turnOnWiFi(final FractionAbility fractionAbility, final int requestId, final RequestWatcher watcher)
    {
        if (true) { //getConnectionUtils().getWifiManager().setWifiEnabled(true)) {
            getSnackbarSupport().createSnackbar(ResourceTable.String_mesg_turningWiFiOn).show();
            watcher.onResultReturned(true, false);
            return true;
        } else
            new AlertDialog(getConnectionUtils().getContext())
                    .setMessage(ResourceTable.String_mesg_wifiEnableFailed)
                    .setNegativeButton(ResourceTable.String_butn_close, new Component.ClickedListener()
                    {
                        @Override
                        public void onClick(Component component)
                        {
                            watcher.onResultReturned(false, false);
                        }
                    })
                    .setPositiveButton(ResourceTable.String_butn_settings, new Component.ClickedListener()
                    {
                        @Override
                        public void onClick(Component component)
                        {
                            HMUtils.startAbility(fractionAbility, null, Settings.ACTION_WIFI_SETTINGS);
                            watcher.onResultReturned(false, true);
                        }
                    })
                    .show();

        return false;
    }

    public boolean notifyWirelessRequestHandled()
    {
        boolean returnedState = mWirelessEnableRequested;

        mWirelessEnableRequested = false;

        return returnedState;
    }


    public static void sendMsg(Context context, String deviceId, String adapterName, String msg) throws Exception {
        NetworkDevice networkDevice = new NetworkDevice(deviceId);
        NetworkDevice.Connection connection = new NetworkDevice.Connection(deviceId, adapterName);
        AccessDatabase database = AppUtils.getDatabase(context);
        database.reconstruct(networkDevice);
        database.reconstruct(connection);
        context.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(new Runnable() {
            @Override
            public void run() {
                CommunicationBridge.connect(database, true, new CommunicationBridge.Client.ConnectionHandler() {
                    @Override
                    public void onConnect(CommunicationBridge.Client client) {
                        client.setDevice(networkDevice);

                        try {
                            final JSONObject jsonRequest = new JSONObject();
                            final CoolSocket.ActiveConnection activeConnection = client.communicate(networkDevice, connection);

                            jsonRequest.put(Keyword.REQUEST, Keyword.REQUEST_CLIPBOARD);
                            jsonRequest.put(Keyword.TRANSFER_CLIPBOARD_TEXT, msg.toString());

                            activeConnection.reply(jsonRequest.toString());

                            CoolSocket.ActiveConnection.Response response = activeConnection.receive();
                            activeConnection.getSocket().close();

                            JSONObject clientResponse = new JSONObject(response.response);

                            if (clientResponse.has(Keyword.RESULT) && clientResponse.getBoolean(Keyword.RESULT)) {
                                //todo show success
                            } else {
                                //todo show error
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }

    public static void sendFile(Ability context, String deviceId, String adapterName, TransferGroup mGroup) throws Exception {
        NetworkDevice networkDevice = new NetworkDevice(deviceId);
        NetworkDevice.Connection connection = new NetworkDevice.Connection(deviceId, adapterName);
        AccessDatabase database = AppUtils.getDatabase(context);
        database.reconstruct(networkDevice);
        database.reconstruct(connection);
        context.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(new Runnable() {
            @Override
            public void run() {
                CommunicationBridge.connect(database, false, new CommunicationBridge.Client.ConnectionHandler() {
                    @Override
                    public void onConnect(CommunicationBridge.Client client) {
                        client.setDevice(networkDevice);
                        try {
                            boolean doPublish = false;
                            final JSONObject jsonRequest = new JSONObject();
                            final TransferGroup.Assignee assignee = new TransferGroup.Assignee(mGroup, networkDevice, connection);
                            final List<TransferObject> pendingRegistry = new ArrayList<>();

                            final List<TransferObject> existingRegistry =
                                    new ArrayList<>(AppUtils.getDatabase(context).castQuery(
                                            new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER)
                                                    .setWhere(AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND "
                                                                    + AccessDatabase.FIELD_TRANSFER_TYPE + "=?",
                                                            String.valueOf(mGroup.groupId),
                                                            TransferObject.Type.OUTGOING.toString()), TransferObject.class));


                            try {
                                // Checks if the current assignee is already on the list, if so do publish not insert
                                AppUtils.getDatabase(context).reconstruct(
                                        new TransferGroup.Assignee(assignee.groupId,
                                                assignee.deviceId));

                                doPublish = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            jsonRequest.put(Keyword.REQUEST, Keyword.REQUEST_TRANSFER);
                            jsonRequest.put(Keyword.TRANSFER_GROUP_ID, mGroup.groupId);

                            if (existingRegistry.size() == 0)
                                throw new Exception("Empty share holder id: " + mGroup.groupId);

                            JSONArray filesArray = new JSONArray();

                            for (TransferObject transferObject : existingRegistry) {
                                Log.e("publish " + transferObject.friendlyName);

                                TransferObject copyObject = new TransferObject(AccessDatabase.convertValues(transferObject.getValues()));


                                copyObject.deviceId = assignee.deviceId; // We will clone the file index with new deviceId
                                copyObject.flag = TransferObject.Flag.PENDING;
                                copyObject.accessPort = 0;
                                copyObject.skippedBytes = 0;
                                JSONObject thisJson = new JSONObject();

                                try {
                                    thisJson.put(Keyword.INDEX_FILE_NAME, copyObject.friendlyName);
                                    thisJson.put(Keyword.INDEX_FILE_SIZE, copyObject.fileSize);
                                    thisJson.put(Keyword.TRANSFER_REQUEST_ID, copyObject.requestId);
                                    thisJson.put(Keyword.INDEX_FILE_MIME, copyObject.fileMimeType);

                                    if (copyObject.directory != null)
                                        thisJson.put(Keyword.INDEX_DIRECTORY, copyObject.directory);

                                    filesArray.put(thisJson);
                                    pendingRegistry.add(copyObject);
                                } catch (Exception e) {
                                    Log.e("UI", "Sender error on fileUri: " + e.getClass().getName() + " : " + copyObject.friendlyName);
                                }
                            }

                            // so that if the user rejects, it won't be removed from the sender
                            jsonRequest.put(Keyword.FILES_INDEX, filesArray.toString());


                            final CoolSocket.ActiveConnection activeConnection = client.communicate(networkDevice, connection);


                            activeConnection.reply(jsonRequest.toString());

                            CoolSocket.ActiveConnection.Response response = activeConnection.receive();
                            activeConnection.getSocket().close();

                            JSONObject clientResponse = new JSONObject(response.response);

                            if (clientResponse.has(Keyword.RESULT) && clientResponse.getBoolean(Keyword.RESULT)) {
                                Log.e("Organising your files");

                                if (doPublish)
                                    AppUtils.getDatabase(context).publish(assignee);
                                else
                                    AppUtils.getDatabase(context).insert(assignee);

                                if (doPublish)
                                    AppUtils.getDatabase(context).publish(pendingRegistry, null);
                                else
                                    AppUtils.getDatabase(context).insert(pendingRegistry, null);


//                                AccessDatabase accessDatabase = new AccessDatabase(context);
//                                accessDatabase.remove(existingRegistry);
//                                accessDatabase.remove(mGroup);
//                                DBHelper.deleteTransferInDb();


                                Log.e("send ok!");
                            } else {
                                Log.e("send faild!");
                            }

                        } catch (Exception e) {
                            if (!(e instanceof InterruptedException)) {
                                e.printStackTrace();
                            }
                        } finally {
                            Log.e("send over!");
//                            AppUtils.getDatabase(context).remove(mGroup);
//                            if (existingRegistry != null) AppUtils.getDatabase(context).remove(existingRegistry);
                        }
                    }
                });
            }

        });
    }

    public static void showConnectionRejectionInformation(final Context context,
                                                          final NetworkDevice device,
                                                          final org.json.JSONObject clientResponse,
                                                          final Component.ClickedListener retryButtonListener)
    {
        try {
            if (clientResponse.has(Keyword.ERROR)) {
                if (clientResponse.getString(Keyword.ERROR).equals(Keyword.ERROR_NOT_ALLOWED))
                    new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                                new AlertDialog(context)
                                        .setTitle(ResourceTable.String_mesg_notAllowed)
                                        .setMessage(context.getString(ResourceTable.String_text_notAllowedHelp, device.nickname, AppUtils.getLocalDeviceName(context)))
                                        .setNegativeButton(ResourceTable.String_butn_close, null)
                                        .setPositiveButton(ResourceTable.String_butn_retry, retryButtonListener)
                                        .show();
                        }
                    });
            } else
                showUnknownError(context, retryButtonListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void showUnknownError(final Context context, final Component.ClickedListener retryButtonListener)
    {
        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable()
        {
            @Override
            public void run()
            {
                    new AlertDialog(context)
                            .setMessage(ResourceTable.String_mesg_somethingWentWrong)
                            .setNegativeButton(ResourceTable.String_butn_close, null)
                            .setPositiveButton(ResourceTable.String_butn_retry, retryButtonListener)
                            .show();
            }
        });
    }

    public interface RequestWatcher
    {
        void onResultReturned(boolean result, boolean shouldWait);
    }
}
