package com.ohos.trebleshot.service;

import com.ohos.trebleshot.config.AppConfig;
import com.ohos.trebleshot.config.Keyword;
import com.ohos.trebleshot.coolsocket.CoolSocket;
import com.ohos.trebleshot.coolsocket.CoolTransfer;
import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.database.SQLiteDatabase;
import com.ohos.trebleshot.database.exception.ReconstructionFailedException;
import com.ohos.trebleshot.exception.AssigneeNotFoundException;
import com.ohos.trebleshot.exception.ConnectionNotFoundException;
import com.ohos.trebleshot.exception.DeviceNotFoundException;
import com.ohos.trebleshot.exception.TransferGroupNotFoundException;
import com.ohos.trebleshot.log.Log;
import com.ohos.trebleshot.object.*;
import com.ohos.trebleshot.utils.*;
import fi.iki.elonen.NanoHTTPD;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.data.preferences.Preferences;
import ohos.data.rdb.ValuesBucket;
import ohos.event.notification.NotificationRequest;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class CommunicationService extends Ability
{
    public static final String TAG = "CommunicationService";
    public static final String ACTION_DEVICE_DISCOVERY = "com.genonbeta.TrebleShot.action.DEVICE_DISCOVERY";
    public static final String ACTION_DEVICE_DISCOVERY_STATUS = "com.genonbeta.TrebleShot.action.DEVICE_DISCOVERY_STATUS";
    public static final String ACTION_FILE_TRANSFER = "com.genonbeta.TrebleShot.action.FILE_TRANSFER";
    public static final String ACTION_CLIPBOARD = "com.genonbeta.TrebleShot.action.CLIPBOARD";
    public static final String ACTION_CANCEL_INDEXING = "com.genonbeta.TrebleShot.action.CANCEL_INDEXING";
    public static final String ACTION_IP = "com.genonbeta.TrebleShot.action.IP";
    public static final String ACTION_END_SESSION = "com.genonbeta.TrebleShot.action.END_SESSION";
    public static final String ACTION_SEAMLESS_RECEIVE = "com.genonbeta.intent.action.SEAMLESS_START";
    public static final String ACTION_CANCEL_JOB = "com.genonbeta.TrebleShot.transaction.action.CANCEL_JOB";
    public static final String ACTION_TOGGLE_SEAMLESS_MODE = "com.genonbeta.TrebleShot.transaction.action.TOGGLE_SEAMLESS_MODE";
    public static final String ACTION_REVOKE_ACCESS_PIN = "com.genonbeta.TrebleShot.transaction.action.REVOKE_ACCESS_PIN";
    public static final String ACTION_TOGGLE_HOTSPOT = "com.genonbeta.TrebleShot.transaction.action.TOGGLE_HOTSPOT";
    public static final String ACTION_REQUEST_HOTSPOT_STATUS = "com.genonbeta.TrebleShot.transaction.action.REQUEST_HOTSPOT_STATUS";
    public static final String ACTION_HOTSPOT_STATUS = "com.genonbeta.TrebleShot.transaction.action.HOTSPOT_STATUS";
    public static final String ACTION_DEVICE_ACQUAINTANCE = "com.genonbeta.TrebleShot.transaction.action.DEVICE_ACQUAINTANCE";
    public static final String ACTION_SERVICE_STATUS = "com.genonbeta.TrebleShot.transaction.action.SERVICE_STATUS";
    public static final String ACTION_SERVICE_CONNECTION_TRANSFER_QUEUE = "com.genonbeta.TrebleShot.transaction.action.SERVICE_CONNECTION_TRANSFER_QUEUE";
    public static final String ACTION_TASK_STATUS_CHANGE = "com.genonbeta.TrebleShot.transaction.action.TASK_STATUS_CHANGE";
    public static final String ACTION_TASK_RUNNING_LIST_CHANGE = "com.genonbeta.TrebleShot.transaction.action.TASK_RUNNNIG_LIST_CHANGE";
    public static final String ACTION_REQUEST_TASK_STATUS_CHANGE = "com.genonbeta.TrebleShot.transaction.action.REQUEST_TASK_STATUS_CHANGE";
    public static final String ACTION_REQUEST_TASK_RUNNING_LIST_CHANGE = "com.genonbeta.TrebleShot.transaction.action.REQUEST_TASK_RUNNING_LIST_CHANGE";
    public static final String ACTION_INCOMING_TRANSFER_READY = "com.genonbeta.TrebleShot.transaction.action.INCOMING_TRANSFER_READY";
    public static final String ACTION_TRUSTZONE_STATUS = "com.genonbeta.TrebleShot.transaction.action.TRUSTZONE_STATUS";
    public static final String ACTION_REQUEST_TRUSTZONE_STATUS = "com.genonbeta.TrebleShot.transaction.action.REQUEST_TRUSTZONE_STATUS";
    public static final String ACTION_TOGGLE_WEBSHARE = "com.genonbeta.TrebleShot.transaction.action.TOGGLE_WEBSHARE";
    public static final String ACTION_WEBSHARE_STATUS = "com.genonbeta.TrebleShot.transaction.action.WEBSHARE_STATUS";
    public static final String ACTION_REQUEST_WEBSHARE_STATUS = "com.genonbeta.TrebleShot.transaction.action.REQUEST_WEBSHARE_STATUS";

    public static final String EXTRA_DEVICE_ID = "extraDeviceId";
    public static final String EXTRA_STATUS_STARTED = "extraStatusStarted";
    public static final String EXTRA_CONNECTION_ADAPTER_NAME = "extraConnectionAdapterName";
    public static final String EXTRA_REQUEST_ID = "extraRequestId";
    public static final String EXTRA_CLIPBOARD_ID = "extraTextId";
    public static final String EXTRA_GROUP_ID = "extraGroupId";
    public static final String EXTRA_IS_ACCEPTED = "extraAccepted";
    public static final String EXTRA_CLIPBOARD_ACCEPTED = "extraClipboardAccepted";
    public static final String EXTRA_HOTSPOT_ENABLED = "extraHotspotEnabled";
    public static final String EXTRA_HOTSPOT_DISABLING = "extraHotspotDisabling";
    public static final String EXTRA_HOTSPOT_NAME = "extraHotspotName";
    public static final String EXTRA_HOTSPOT_KEY_MGMT = "extraHotspotKeyManagement";
    public static final String EXTRA_HOTSPOT_PASSWORD = "extraHotspotPassword";
    public static final String EXTRA_TASK_CHANGE_TYPE = "extraTaskChangeType";
    public static final String EXTRA_TASK_LIST_RUNNING = "extraTaskListRunning";
    public static final String EXTRA_DEVICE_LIST_RUNNING = "extraDeviceListRunning";
    public static final String EXTRA_TOGGLE_WEBSHARE_START_ALWAYS = "extraToggleWebShareStartAlways";

    public static final int TASK_STATUS_ONGOING = 0;
    public static final int TASK_STATUS_STOPPED = 1;

    private final List<ProcessHolder> mActiveProcessList = new ArrayList<>();
    private final CommunicationServer mCommunicationServer = new CommunicationServer();
    private WebShareServer mWebShareServer = null;
    private final SeamlessServer mSeamlessServer = new SeamlessServer();
    private DeviceDiscoveryTask deviceDiscoveryTask;
    private final Map<Long, Interrupter> mOngoingIndexList = new HashMap<>();
    private final Receive mReceive = new Receive();
    private final Send mSend = new Send();
    private final ExecutorService mSelfExecutor = Executors.newFixedThreadPool(10);
    private CommunicationNotificationHelper mNotificationHelper;

    private boolean mSeamlessMode = false;
    private boolean mPinAccess = false;

    public static final int NOTIFICATION_ID = 1005;

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return super.onConnect(intent);
    }


    private AccessDatabase getDatabase(){
        return AppUtils.getDatabase(getContext());
    }

    private Preferences getDefaultPreferences(){
        return AppUtils.getPreferences(getContext());
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        mNotificationHelper = new CommunicationNotificationHelper(NotificationUtils.getNotificationUtilsInst(getContext()));
        mReceive.setNotifyDelay(AppConfig.DEFAULT_NOTIFICATION_DELAY);
        mSend.setNotifyDelay(AppConfig.DEFAULT_NOTIFICATION_DELAY);

        // 创建通知，其中1005为notificationId
        NotificationRequest request = new NotificationRequest(NOTIFICATION_ID);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle(getString(ResourceTable.String_share_web_notification_text));
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);

        // 绑定通知，1005为创建通知时传入的notificationId
        keepBackgroundRunning(NOTIFICATION_ID, request);


        //设置信任
        updateServiceState(getDefaultPreferences().getBoolean("trust_always", true));


        try {
            deviceDiscoveryTask = new DeviceDiscoveryTask(getContext());
            mCommunicationServer.start();
            mSeamlessServer.start();
            mWebShareServer = new WebShareServer(this, AppConfig.SERVER_PORT_WEBSHARE);
            mWebShareServer.setAsyncRunner(new WebShareServer.BoundRunner(
                    Executors.newFixedThreadPool(AppConfig.WEB_SHARE_CONNECTION_MAX)));
        } catch (Throwable t) {
            Log.e(TAG, "Failed to start Web Share Server");
        }

    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId)
    {
        super.onCommand(intent, restart, startId);

        if (intent != null)
            Log.d(TAG, "onStart(Intent intent) : action = " + intent.getAction());

        if (intent != null) {
            if (ACTION_FILE_TRANSFER.equals(intent.getAction())) {
                final String deviceId = intent.getStringParam(EXTRA_DEVICE_ID);
                final long groupId = intent.getLongParam(EXTRA_GROUP_ID, -1);
                final boolean isAccepted = intent.getBooleanParam(EXTRA_IS_ACCEPTED, false);


                try {
                    final TransferInstance transferInstance = new TransferInstance(getDatabase(), groupId, deviceId, true);

                    CommunicationBridge.connect(getDatabase(), new CommunicationBridge.Client.ConnectionHandler()
                    {
                        @Override
                        public void onConnect(CommunicationBridge.Client client)
                        {
                            try {
                                CoolSocket.ActiveConnection activeConnection = client.communicate(transferInstance.getDevice(), transferInstance.getConnection());
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(Keyword.REQUEST, Keyword.REQUEST_RESPONSE);
                                jsonObject.put(Keyword.TRANSFER_GROUP_ID, groupId);
                                jsonObject.put(Keyword.TRANSFER_IS_ACCEPTED, isAccepted);
                                activeConnection.reply(jsonObject.toString());

                                activeConnection.receive();
                                activeConnection.getSocket().close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    if (isAccepted)
                        startFileReceiving(groupId, deviceId);
                    else
                        getDatabase().remove(transferInstance.getGroup());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_IP.equals(intent.getAction())) {
                String deviceId = intent.getStringParam(EXTRA_DEVICE_ID);
                boolean isAccepted = intent.getBooleanParam(EXTRA_IS_ACCEPTED, false);


                NetworkDevice device = new NetworkDevice(deviceId);

                try {
                    getDatabase().reconstruct(device);
                    device.isRestricted = !isAccepted;
                    getDatabase().update(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_CANCEL_INDEXING.equals(intent.getAction())) {
                long groupId = intent.getLongParam(EXTRA_GROUP_ID, -1);
                Interrupter interrupter = getOngoingIndexList().get(groupId);

                if (interrupter != null)
                    interrupter.interrupt();
            } else if (ACTION_CLIPBOARD.equals(intent.getAction()) && intent.hasParameter(EXTRA_CLIPBOARD_ACCEPTED)) {
                //剪切板
            } else if (ACTION_END_SESSION.equals(intent.getAction())) {
                terminateAbility();
            } else if (ACTION_SEAMLESS_RECEIVE.equals(intent.getAction())
                    && intent.hasParameter(EXTRA_GROUP_ID)
                    && intent.hasParameter(EXTRA_DEVICE_ID)) {
                long groupId = intent.getLongParam(EXTRA_GROUP_ID, -1);
                String deviceId = intent.getStringParam(EXTRA_DEVICE_ID);

                try {
                    ProcessHolder process = findProcessById(groupId, deviceId);

                    if (process == null)
                        startFileReceiving(groupId, deviceId);
                    else
                    {
                        ///show dialog
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ACTION_CANCEL_JOB.equals(intent.getAction())
                    && intent.hasParameter(EXTRA_GROUP_ID)
                    && intent.hasParameter(EXTRA_DEVICE_ID)) {

                long groupId = intent.getLongParam(EXTRA_GROUP_ID, -1);
                String deviceId = intent.getStringParam(CommunicationService.EXTRA_DEVICE_ID);

                ProcessHolder processHolder = findProcessById(groupId, deviceId);

                if (processHolder == null) {
                    notifyTaskStatusChange(groupId, deviceId, TASK_STATUS_STOPPED);

                } else {
                    processHolder.notification = getNotificationHelper().notifyStuckThread(processHolder);

                    if (!processHolder.builder.getTransferProgress().isInterrupted()) {
                        processHolder.builder.getTransferProgress().interrupt();
                    } else {
                        try {
                            if (processHolder.builder instanceof CoolTransfer.Receive.Builder) {
                                CoolTransfer.Receive.Builder receiveBuilder = (CoolTransfer.Receive.Builder) processHolder.builder;

                                if (receiveBuilder.getServerSocket() != null)
                                    receiveBuilder.getServerSocket().close();
                            }
                        } catch (Exception e) {
                            // do nothing
                        }

                        try {
                            if (processHolder.activeConnection != null
                                    && processHolder.activeConnection.getSocket() != null)
                                processHolder.activeConnection.getSocket().close();
                        } catch (IOException e) {
                            // do nothing
                        }

                        try {
                            if (processHolder.builder.getSocket() != null)
                                processHolder.builder.getSocket().close();
                        } catch (IOException e) {
                            // do nothing
                        }
                    }
                }
            } else if (ACTION_TOGGLE_SEAMLESS_MODE.equals(intent.getAction())) {
                updateServiceState(!mSeamlessMode);
            } else if (ACTION_TOGGLE_HOTSPOT.equals(intent.getAction())) {
                //停止热点
            } else if (ACTION_REQUEST_HOTSPOT_STATUS.equals(intent.getAction())) {
                //获取热点状态
            } else if (ACTION_SERVICE_STATUS.equals(intent.getAction())
                    && intent.hasParameter(EXTRA_STATUS_STARTED)) {
                //
            } else if (ACTION_REQUEST_TASK_STATUS_CHANGE.equals(intent.getAction())
                    && intent.hasParameter(EXTRA_GROUP_ID)
                    && intent.hasParameter(EXTRA_DEVICE_ID)) {
                long groupId = intent.getLongParam(EXTRA_GROUP_ID, -1);
                String deviceId = intent.getStringParam(EXTRA_DEVICE_ID);

                notifyTaskStatusChange(groupId, deviceId, findProcessById(groupId, deviceId) == null
                        ? TASK_STATUS_STOPPED
                        : TASK_STATUS_ONGOING);
            } else if (ACTION_REQUEST_TASK_RUNNING_LIST_CHANGE.equals(intent.getAction())) {
                notifyTaskRunningListChange();
            } else if (ACTION_REVOKE_ACCESS_PIN.equals(intent.getAction())) {
                revokePinAccess();
                refreshServiceState();
            } else if (ACTION_REQUEST_TRUSTZONE_STATUS.equals(intent.getAction())) {
                sendTrustZoneStatus();
            } else if (ACTION_REQUEST_WEBSHARE_STATUS.equals(intent.getAction())) {
                sendWebShareStatus();
            } else if (ACTION_TOGGLE_WEBSHARE.equals(intent.getAction())) {
                if (intent.hasParameter(EXTRA_TOGGLE_WEBSHARE_START_ALWAYS))
                    setWebShareEnabled(intent.getBooleanParam(EXTRA_TOGGLE_WEBSHARE_START_ALWAYS,
                            false), true);
                else
                    toggleWebShare();
            }else if(ACTION_DEVICE_DISCOVERY.equals(intent.getAction())){
                deviceDiscoveryTask.discovery();
            }else if(ACTION_DEVICE_DISCOVERY_STATUS.equals(intent.getAction())){
                boolean status = deviceDiscoveryTask.isRunning();
                Intent eventIntent = new Intent();
                eventIntent.setParam(DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS,status);
                CommonEventHelper.publish(DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS,eventIntent);
                System.out.println("publish event...."+DeviceDiscoveryTask.EVENT_DISCOVERY_STATUS);
            }
        }

    }

    public CommunicationNotificationHelper getNotificationHelper()
    {
        return mNotificationHelper;
    }

    @Override
    public void onStop() {
        super.onStop();

        mCommunicationServer.stop();
        mSeamlessServer.stop();
        deviceDiscoveryTask.onStop();


        {
            ValuesBucket values = new ValuesBucket();

            values.putBoolean(AccessDatabase.FIELD_TRANSFERGROUP_ISSHAREDONWEB, false);
            getDatabase().update(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP)
                    .setWhere(String.format("%s = ?", AccessDatabase.FIELD_TRANSFERGROUP_ISSHAREDONWEB),
                            String.valueOf(1)), values);
        }

        setWebShareEnabled(false, false);
        sendTrustZoneStatus();



        revokePinAccess();


        synchronized (getOngoingIndexList()) {
            for (Interrupter interrupter : getOngoingIndexList().values()) {
                interrupter.interrupt(false);
                Log.d(TAG, "onDestroy(): Ongoing indexing stopped: " + interrupter.toString());
            }
        }

        synchronized (getActiveProcessList()) {
            for (ProcessHolder processHolder : getActiveProcessList())
                if (processHolder.builder != null) {
                    processHolder.builder.getTransferProgress().interrupt();
                    Log.d(TAG, "onDestroy(): Killing sending process: " + processHolder.builder.toString());
                }
        }
    }

    public synchronized boolean addProcess(ProcessHolder processHolder)
    {
        return getActiveProcessList().add(processHolder);
    }

    public synchronized boolean removeProcess(ProcessHolder processHolder)
    {
        return getActiveProcessList().remove(processHolder);
    }

    public boolean hasOngoingTasks()
    {
        return mCommunicationServer.getConnections().size() > 0
                || getOngoingIndexList().size() > 0
                || getActiveProcessList().size() > 0;
    }

    public ProcessHolder findProcessById(long groupId, String deviceId)
    {
        synchronized (getActiveProcessList()) {
            for (ProcessHolder processHolder : getActiveProcessList())
                if (processHolder.groupId == groupId
                        && deviceId.equals(processHolder.deviceId))
                    return processHolder;
        }

        return null;
    }

    public synchronized List<ProcessHolder> getActiveProcessList()
    {
        return mActiveProcessList;
    }



    public synchronized Map<Long, Interrupter> getOngoingIndexList()
    {
        return mOngoingIndexList;
    }

    public ExecutorService getSelfExecutor()
    {
        return mSelfExecutor;
    }



    public boolean isProcessRunning(int groupId, String deviceId)
    {
        return findProcessById(groupId, deviceId) != null;
    }

    public void notifyTaskStatusChange(long groupId, String deviceId, int state)
    {

    }

    public void notifyTaskRunningListChange()
    {
        List<Long> taskList = new ArrayList<>();
        ArrayList<String> deviceList = new ArrayList<>();

        synchronized (getActiveProcessList()) {
            for (ProcessHolder processHolder : getActiveProcessList()) {
                if (processHolder.groupId != 0
                        && processHolder.deviceId != null) {
                    taskList.add(processHolder.groupId);
                    deviceList.add(processHolder.deviceId);
                }
            }
        }

        long[] taskArray = new long[taskList.size()];

        for (int i = 0; i < taskList.size(); i++)
            taskArray[i] = taskList.get(i);

//        sendBroadcast(new ExtIntent(ACTION_TASK_RUNNING_LIST_CHANGE)
//                .setParam(EXTRA_TASK_LIST_RUNNING, taskArray)
//                .putStringArrayListExtra(EXTRA_DEVICE_LIST_RUNNING, deviceList));
    }

    public void refreshServiceState()
    {
        updateServiceState(mSeamlessMode);
    }

    public void revokePinAccess()
    {
        getDefaultPreferences().putInt(Keyword.NETWORK_PIN, -1);
        getDefaultPreferences().flush();
    }


    public void sendWebShareStatus()
    {
        try {
            Intent intent = new Intent();
            intent.setParam(EXTRA_STATUS_STARTED, mWebShareServer.isAlive());
            HMUtils.sendEvent(intent, ACTION_WEBSHARE_STATUS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendTrustZoneStatus()
    {
        CommonEventHelper.publish(ACTION_TRUSTZONE_STATUS, new Intent()
                .setParam(EXTRA_STATUS_STARTED, mSeamlessMode));
    }

    public void startFileReceiving(long groupId, String deviceId) throws TransferGroupNotFoundException, DeviceNotFoundException, ConnectionNotFoundException, AssigneeNotFoundException
    {
        Log.e("startFileReceiving");
        // it should create its own devices
        startFileReceiving(new TransferInstance(getDatabase(), groupId, deviceId, true));
    }

    public void startFileReceiving(TransferInstance transferInstance)
    {
        CoolSocket.connect(new SeamlessClientHandler(transferInstance));
    }

    public void updateServiceState(boolean seamlessMode)
    {
        boolean broadcastStatus = mSeamlessMode != seamlessMode;
        mSeamlessMode = seamlessMode;
        mPinAccess = getDefaultPreferences().getInt(Keyword.NETWORK_PIN, -1) != -1;

        if (broadcastStatus)
            sendTrustZoneStatus();

        getNotificationHelper().getCommunicationServiceNotification(mSeamlessMode, mPinAccess,
                mWebShareServer != null && mWebShareServer.isAlive());
    }

    public void setWebShareEnabled(boolean enable, boolean updateServiceState)
    {
        boolean canStart = !mWebShareServer.isAlive();

        if (!enable && !canStart)
            mWebShareServer.stop();
        else if (enable && canStart)
            try {
                mWebShareServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (updateServiceState)
            updateServiceState(mSeamlessMode);
        sendWebShareStatus();
    }

    public void toggleWebShare()
    {
        setWebShareEnabled(!mWebShareServer.isAlive(), true);
    }

    public class CommunicationServer extends CoolSocket
    {
        public CommunicationServer()
        {
            super(AppConfig.SERVER_PORT_COMMUNICATION);
            setSocketTimeout(AppConfig.DEFAULT_SOCKET_TIMEOUT_LARGE);
        }

        @Override
        protected void onConnected(final ActiveConnection activeConnection)
        {
            if (getConnectionCountByAddress(activeConnection.getAddress()) > 3)
                return;

            // the problem with the programming is that nobody seems to care what they do even though
            // what they do always concern others. This is the new world order and only thing you can
            // do about is to what until 

            try {
                ActiveConnection.Response clientRequest = activeConnection.receive();
                JSONObject responseJSON = analyzeResponse(clientRequest);
                JSONObject replyJSON = new JSONObject();
                //打印通信数据
                Log.e("1---:"+responseJSON.toString());
                if (responseJSON.has(Keyword.REQUEST)
                        && Keyword.BACK_COMP_REQUEST_SEND_UPDATE.equals(responseJSON.getString(Keyword.REQUEST))) {

                    replyJSON.put(Keyword.RESULT, true);
                    Log.e("M-----"+replyJSON.toString());
                    activeConnection.reply(replyJSON.toString());

                    getSelfExecutor().submit(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try {
                                //UpdateUtils.sendUpdate(getApplicationContext(), activeConnection.getClientAddress());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    return;
                }

                boolean result = false;
                boolean shouldContinue = false;

                final int networkPin = getDefaultPreferences().getInt(Keyword.NETWORK_PIN, -1);
//                final boolean isSecureConnection = networkPin != -1
//                        && responseJSON.has(Keyword.DEVICE_SECURE_KEY)
//                        && responseJSON.getInt(Keyword.DEVICE_SECURE_KEY) == networkPin;
                final boolean isSecureConnection = true;
                String deviceSerial = null;

                AppUtils.applyDeviceToJSON(CommunicationService.this, replyJSON);

                if (responseJSON.has(Keyword.HANDSHAKE_REQUIRED) && responseJSON.getBoolean(Keyword.HANDSHAKE_REQUIRED)) {
                    pushReply(activeConnection, replyJSON, true);

                    if (!responseJSON.has(Keyword.HANDSHAKE_ONLY) || !responseJSON.getBoolean(Keyword.HANDSHAKE_ONLY)) {
                        if (responseJSON.has(Keyword.DEVICE_INFO_SERIAL))
                            deviceSerial = responseJSON.getString(Keyword.DEVICE_INFO_SERIAL);

                        clientRequest = activeConnection.receive();
                        responseJSON = analyzeResponse(clientRequest);
                        Log.e("2---:"+responseJSON.toString());
                    } else {
                        return;
                    }
                }

                if (deviceSerial != null) {
                    NetworkDevice device = new NetworkDevice(deviceSerial);

                    try {
                        getDatabase().reconstruct(device);

                        if (isSecureConnection)
                            device.isRestricted = false;

                        if (!device.isRestricted)
                            shouldContinue = true;
                        Log.e("3---："+device.toString());
                    } catch (Exception e1) {
                        e1.printStackTrace();

                        device = NetworkDeviceLoader.load(true, getDatabase(), activeConnection.getClientAddress(), null);

                        if (device == null)
                            throw new Exception("Could not reach to the opposite server");

                        device.isTrusted = true;

                        if (isSecureConnection)
                            device.isRestricted = false;

                        getDatabase().publish(device);

                        shouldContinue = true;

                        if (device.isRestricted) {
                            getNotificationHelper().notifyConnectionRequest(device);
                        }
                    }

                    final NetworkDevice.Connection connection = NetworkDeviceLoader.processConnection(getDatabase(), device, activeConnection.getClientAddress());
                    final NetworkDevice finalDevice = device;
                    final boolean isSeamlessAvailable = (mSeamlessMode && device.isTrusted)
                            || (isSecureConnection && getDefaultPreferences().getBoolean("qr_trust", false));

                    if (!shouldContinue) {
                        replyJSON.put(Keyword.ERROR, Keyword.ERROR_NOT_ALLOWED);
                        Log.e("4---");
                    }
                    else if (responseJSON.has(Keyword.REQUEST)) {
                        if (isSecureConnection && !mPinAccess)
                            // Probably pin access has just activated, so we should update the service state
                            refreshServiceState();

                        switch (responseJSON.getString(Keyword.REQUEST)) {
                            case (Keyword.REQUEST_TRANSFER):
                                Log.e("5---"+responseJSON.has(Keyword.FILES_INDEX)+"----"+responseJSON.has(Keyword.TRANSFER_GROUP_ID)+"---"+getOngoingIndexList().size());
                                if (responseJSON.has(Keyword.FILES_INDEX) && responseJSON.has(Keyword.TRANSFER_GROUP_ID) && getOngoingIndexList().size() < 1) {
                                    final String jsonIndex = responseJSON.getString(Keyword.FILES_INDEX);
                                    final long groupId = responseJSON.getLong(Keyword.TRANSFER_GROUP_ID);
                                    Log.e("6---");
                                    result = true;

                                    getSelfExecutor().submit(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            final JSONArray jsonArray;
                                            final Interrupter interrupter = new Interrupter();
                                            TransferGroup group = new TransferGroup(groupId);
                                            TransferGroup.Assignee assignee = new TransferGroup.Assignee(group, finalDevice, connection);
                                            final DynamicNotification notification = getNotificationHelper().notifyPrepareFiles(group, finalDevice);
                                            notification.setProgress(0, 0, true);

                                            try {
                                                jsonArray = new JSONArray(jsonIndex);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                notification.cancel();
                                                return;
                                            }

                                            notification.setProgress(0, 0, false);
                                            boolean usePublishing = false;

                                            try {
                                                System.out.println("group-----"+groupId);
                                                getDatabase().reconstruct(group);
                                                usePublishing = true;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            TransferObject transferObject = null;

                                            getDatabase().publish(group);
                                            getDatabase().publish(assignee);

                                            synchronized (getOngoingIndexList()) {
                                                Log.e("----AddSync");
                                                getOngoingIndexList().put(group.groupId, interrupter);
                                            }

                                            long uniqueId = System.currentTimeMillis(); // The uniqueIds
                                            List<TransferObject> pendingRegistry = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                if (interrupter.interrupted())
                                                    break;

                                                try {
                                                    if (!(jsonArray.get(i) instanceof JSONObject))
                                                        continue;

                                                    JSONObject requestIndex = jsonArray.getJSONObject(i);

                                                    if (requestIndex != null && requestIndex.has(Keyword.INDEX_FILE_NAME) && requestIndex.has(Keyword.INDEX_FILE_SIZE) && requestIndex.has(Keyword.INDEX_FILE_MIME) && requestIndex.has(Keyword.TRANSFER_REQUEST_ID)) {
                                                        transferObject = new TransferObject(
                                                                requestIndex.getLong(Keyword.TRANSFER_REQUEST_ID),
                                                                groupId,
                                                                assignee.deviceId,
                                                                requestIndex.getString(Keyword.INDEX_FILE_NAME),
                                                                "." + (uniqueId++) + "." + AppConfig.EXT_FILE_PART,
                                                                requestIndex.getString(Keyword.INDEX_FILE_MIME),
                                                                requestIndex.getLong(Keyword.INDEX_FILE_SIZE),
                                                                TransferObject.Type.INCOMING);

                                                        if (requestIndex.has(Keyword.INDEX_DIRECTORY))
                                                            transferObject.directory = requestIndex.getString(Keyword.INDEX_DIRECTORY);

                                                        pendingRegistry.add(transferObject);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            SQLiteDatabase.ProgressUpdater progressUpdater = new SQLiteDatabase.ProgressUpdater()
                                            {
                                                long lastNotified = System.currentTimeMillis();

                                                @Override
                                                public void onProgressChange(int total, int current)
                                                {
                                                    if ((System.currentTimeMillis() - lastNotified) > 1000) {
                                                        lastNotified = System.currentTimeMillis();
                                                        notification.updateProgress(total, current, false);
                                                    }
                                                }

                                                @Override
                                                public boolean onProgressState()
                                                {
                                                    return !interrupter.interrupted();
                                                }
                                            };
                                            Log.e("6----"+pendingRegistry.size());
                                            if (pendingRegistry.size() > 0) {
                                                try {
                                                    if (usePublishing)
                                                        getDatabase().publish(pendingRegistry, progressUpdater);
                                                    else
                                                        getDatabase().insert(pendingRegistry, progressUpdater);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                            notification.cancel();
                                            Log.e("6----end");

                                            synchronized (getOngoingIndexList()) {
                                                Log.e("----removeSync");
                                                getOngoingIndexList().remove(group.groupId);
                                            }

                                            if (interrupter.interrupted())
                                                getDatabase().remove(group);
                                            else if (transferObject != null && pendingRegistry.size() > 0) {
//                                                sendBroadcast(new ExtIntent(ACTION_INCOMING_TRANSFER_READY)
//                                                        .setParam(EXTRA_GROUP_ID, groupId)
//                                                        .setParam(EXTRA_DEVICE_ID, finalDevice.deviceId));

                                                if (isSeamlessAvailable)
                                                    try {
                                                        startFileReceiving(group.groupId, finalDevice.deviceId);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                else{
                                                    getNotificationHelper().notifyTransferRequest(transferObject, finalDevice, pendingRegistry.size());
                                                }

                                            }
                                        }
                                    });
                                }
                                break;
                            case (Keyword.REQUEST_RESPONSE):
                                Log.e("REQUEST_RESPONSE----"+responseJSON.toString());
                                if (responseJSON.has(Keyword.TRANSFER_GROUP_ID)) {
                                    int groupId = responseJSON.getInt(Keyword.TRANSFER_GROUP_ID);
                                    boolean isAccepted = responseJSON.getBoolean(Keyword.TRANSFER_IS_ACCEPTED);

                                    TransferGroup group = new TransferGroup(groupId);
                                    TransferGroup.Assignee assignee = new TransferGroup.Assignee(group, device);

                                    try {
                                        getDatabase().reconstruct(group);
                                        getDatabase().reconstruct(assignee);

                                        if (!isAccepted)
                                            getDatabase().remove(assignee);

                                        result = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case (Keyword.REQUEST_CLIPBOARD):
                                if (responseJSON.has(Keyword.TRANSFER_CLIPBOARD_TEXT)) {
                                    TextStreamObject textStreamObject = new TextStreamObject(AppUtils.getUniqueNumber(), responseJSON.getString(Keyword.TRANSFER_CLIPBOARD_TEXT), System.currentTimeMillis());

                                    getDatabase().publish(textStreamObject);
                                    getNotificationHelper().notifyClipboardRequest(device, textStreamObject);

                                    result = true;
                                }
                                break;
                            case (Keyword.REQUEST_ACQUAINTANCE):
//                                sendBroadcast(new ExtIntent(ACTION_DEVICE_ACQUAINTANCE)
//                                        .setParam(EXTRA_DEVICE_ID, device.deviceId)
//                                        .setParam(EXTRA_CONNECTION_ADAPTER_NAME, connection.adapterName));

                                result = true;
                                break;
                            case (Keyword.REQUEST_HANDSHAKE):
                                Log.e("REQUEST_HANDSHAKE");
                                result = true;
                                break;
                            case (Keyword.REQUEST_START_TRANSFER):
                                Log.e("REQUEST_START_TRANSFER");
                                if (!device.isTrusted)
                                    replyJSON.put(Keyword.ERROR, Keyword.ERROR_REQUIRE_TRUSTZONE);
                                else if (responseJSON.has(Keyword.TRANSFER_GROUP_ID)) {
                                    int groupId = responseJSON.getInt(Keyword.TRANSFER_GROUP_ID);

                                    try {
                                        TransferGroup group = new TransferGroup(groupId);
                                        getDatabase().reconstruct(group);

                                        ProcessHolder process = findProcessById(groupId, device.deviceId);

                                        if (process == null) {
                                            startFileReceiving(groupId, device.deviceId);
                                            result = true;
                                        } else
                                            responseJSON.put(Keyword.ERROR,
                                                    Keyword.ERROR_NOT_ACCESSIBLE);
                                    } catch (Exception e) {
                                        // do nothing
                                        responseJSON.put(Keyword.ERROR, Keyword.ERROR_NOT_FOUND);
                                    }
                                }
                                break;
                        }
                    }
                }

                pushReply(activeConnection, replyJSON, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public JSONObject analyzeResponse(ActiveConnection.Response response) throws Exception
        {
            return response.totalLength > 0 ? new JSONObject(response.response) : new JSONObject();
        }

        public void pushReply(ActiveConnection activeConnection, JSONObject reply, boolean result) throws Exception, TimeoutException, IOException
        {
            reply.put(Keyword.RESULT, result);
            activeConnection.reply(reply.toString());
        }
    }

    private class SeamlessServer extends CoolSocket
    {
        SeamlessServer()
        {
            super(AppConfig.SERVER_PORT_SEAMLESS);
            setSocketTimeout(AppConfig.DEFAULT_SOCKET_TIMEOUT);
        }

        @Override
        protected void onConnected(ActiveConnection activeConnection)
        {
            ProcessHolder processHolder = new ProcessHolder();

            TransferInstance transferInstance = null;
            processHolder.activeConnection = activeConnection;
            processHolder.type = TransferObject.Type.OUTGOING;
            processHolder.builder = new CoolTransfer.Send.Builder<>();
            processHolder.builder.setExtra(processHolder);

            synchronized (getActiveProcessList()) {
                getActiveProcessList().add(processHolder);
            }

            try {
                ActiveConnection.Response mainRequest = activeConnection.receive();
                Log.d(TAG, "SeamlessServer.onConnected(): receive: " + mainRequest.response);
                JSONObject mainRequestJSON = new JSONObject(mainRequest.response);
                String deviceId = mainRequestJSON.has(Keyword.TRANSFER_DEVICE_ID)
                        ? mainRequestJSON.getString(Keyword.TRANSFER_DEVICE_ID)
                        : null;
                int groupId = mainRequestJSON.getInt(Keyword.TRANSFER_GROUP_ID);

                activeConnection.setId(groupId);

                {
                    JSONObject reply = new JSONObject();
                    reply.put(Keyword.RESULT, false);

                    try {
                        if (deviceId != null) {
                            NetworkDevice otherDevice = new NetworkDevice(deviceId);
                            getDatabase().reconstruct(otherDevice);

                            NetworkDevice.Connection connection = NetworkDeviceLoader.processConnection(getDatabase(), otherDevice, activeConnection.getClientAddress());

                            transferInstance = new TransferInstance.Builder()
                                    .supply(connection)
                                    .supply(otherDevice)
                                    .build(getDatabase(), groupId, deviceId, true);
                        } else
                            transferInstance = new TransferInstance(getDatabase(), groupId, activeConnection.getClientAddress(), false);

                        processHolder.groupId = transferInstance.getGroup().groupId;
                        processHolder.deviceId = transferInstance.getAssignee().deviceId;

                        reply.put(Keyword.RESULT, true);
                    } catch (TransferGroupNotFoundException e) {
                        reply.put(Keyword.ERROR, Keyword.ERROR_NOT_FOUND);
                        e.printStackTrace();
                        return;
                    } catch (DeviceNotFoundException e) {
                        reply.put(Keyword.ERROR, Keyword.ERROR_NOT_ALLOWED);
                        e.printStackTrace();
                        return;
                    } finally {
                        activeConnection.reply(reply.toString());
                        Log.d(TAG, "SeamlessServer.onConnected(): reply: " + reply.toString());
                    }
                }

                {
                    // It is a good practice to update the transfer method
                    // when the connection address is not
                }

                notifyTaskStatusChange(processHolder.groupId, processHolder.deviceId, TASK_STATUS_ONGOING);
                notifyTaskRunningListChange();

                while (activeConnection.getSocket() != null
                        && activeConnection.getSocket().isConnected()) {
                    processHolder.builder.reset();

                    // This will set the previous

                    {
                        Send.Builder<ProcessHolder> sendBuilder = (Send.Builder<ProcessHolder>) processHolder.builder;
                        ActiveConnection.Response response = activeConnection.receive();
                        Log.d(TAG, "SeamlessServer.onConnected(): receive: " + response.response);

                        if (response.response == null || response.totalLength < 1) {
                            Log.d(TAG, "SeamlessServer.onConnected(): NULL response was received exiting loop");
                            return;
                        }

                        JSONObject request = new JSONObject(response.response);
                        JSONObject reply = new JSONObject();

                        try {
                            if (request.has(Keyword.RESULT) && !request.getBoolean(Keyword.RESULT)) {
                                // the assignee for this transfer has received the files. We can remove it
                                if (request.has(Keyword.TRANSFER_JOB_DONE) && request.getBoolean(Keyword.TRANSFER_JOB_DONE))
                                    Log.d(TAG, "SeamlessServer.onConnected(): Receiver notified us that it has received all the pending transfers: " + processHolder.deviceId);
                                else
                                    processHolder.builder.getTransferProgress().interrupt();

                                return;
                            } else if (!processHolder.builder.getTransferProgress().isInterrupted()) {
                                processHolder.transferObject = new TransferObject(
                                        request.getInt(Keyword.TRANSFER_REQUEST_ID),
                                        processHolder.deviceId,
                                        processHolder.type);

                                getDatabase().reconstruct(processHolder.transferObject);

                                processHolder.transferObject.accessPort = request.getInt(Keyword.TRANSFER_SOCKET_PORT);

                                if (request.has(Keyword.SKIPPED_BYTES)) {
                                    processHolder.transferObject.skippedBytes = request.getLong(Keyword.SKIPPED_BYTES);
                                    Log.d(TAG, "SeamlessServes.onConnected(): Has skipped bytes: " + processHolder.transferObject.skippedBytes);
                                }

                                // This changes the state of the object to pending from any other
                                getDatabase().update(processHolder.transferObject);


                                reply.put(Keyword.RESULT, true);

                                getNotificationHelper().notifyFileTransaction(processHolder);
                                Log.d(TAG, "SeamlessServer.onConnected(): Proceeding to send");
                                File sendFile = new File(processHolder.transferObject.file);
                                sendBuilder.setServerIp(activeConnection.getClientAddress())
                                        .setInputStream(new FileInputStream(sendFile))
                                        .setPort(processHolder.transferObject.accessPort)
                                        .setFileSize(sendFile.length())
                                        .setBuffer(new byte[AppConfig.BUFFER_LENGTH_DEFAULT])
                                        .setExtra(processHolder);
                            } else if (processHolder.builder.getTransferProgress().isInterrupted()) {
                                reply.put(Keyword.RESULT, false);
                                reply.put(Keyword.TRANSFER_JOB_DONE, false);
                                return;
                            }
                        } catch (ReconstructionFailedException e) {
                            reply.put(Keyword.RESULT, false);
                            reply.put(Keyword.ERROR, Keyword.ERROR_NOT_FOUND);
                            reply.put(Keyword.FLAG, Keyword.FLAG_GROUP_EXISTS);

                            processHolder.transferObject.flag = TransferObject.Flag.REMOVED;
                        } catch (FileNotFoundException  e) {
                            Log.e(TAG, "SeamlessServer.onConnected(): File is not accessible ? " + processHolder.transferObject.friendlyName);

                            reply.put(Keyword.RESULT, false);
                            reply.put(Keyword.ERROR, Keyword.ERROR_NOT_ACCESSIBLE);
                            reply.put(Keyword.FLAG, Keyword.FLAG_GROUP_EXISTS);

                            processHolder.transferObject.flag = TransferObject.Flag.INTERRUPTED;

                            e.printStackTrace();
                        } catch (Exception e) {
                            Log.e(TAG, "SeamlessServer.onConnected(): Exception is handled: " + e.toString());

                            reply.put(Keyword.RESULT, false);
                            reply.put(Keyword.ERROR, Keyword.ERROR_UNKNOWN);
                            reply.put(Keyword.FLAG, Keyword.FLAG_GROUP_EXISTS);

                            processHolder.transferObject.flag = TransferObject.Flag.INTERRUPTED;

                            e.printStackTrace();
                        } finally {
                            if (reply.length() > 0) {
                                activeConnection.reply(reply.toString());
                                Log.d(TAG, "SeamlessServer.onConnected(): reply: " + reply.toString());
                            }
                        }

                        if (reply.has(Keyword.RESULT) && reply.getBoolean(Keyword.RESULT))
                            mSend.send(sendBuilder, true);
                    }

                    // We are now updating instances always at the end because it will be
                    // changed by the process itself naturally.
                    if (processHolder.transferObject != null) {
                        Log.d(TAG, "SeamlessServer.onConnected(): Updating file instances to " + processHolder.transferObject.flag.toString());
                        getDatabase().update(processHolder.transferObject);
                    }

                    // By rejecting to provide information when there is something wrong other
                    // than on
                    // e of the sides requested to interrupt receiver will try to attempt to
                    // restart the process. This is why interruption is checked. One more loop
                    // will allow us to gather information because the interruption may be requested
                    // while the transfer is going on meaning there was not a chance to gather the proper
                    // information.
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!activeConnection.getSocket().isClosed())
                        activeConnection.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (transferInstance != null
                        && !CoolTransfer.Flag.CONTINUE.equals(processHolder.builder.getFlag())
                        && !processHolder.builder.getTransferProgress().isInterrupted())
                    mNotificationHelper.notifyConnectionError(transferInstance, TransferObject.Type.OUTGOING, null);
                else if (processHolder.notification != null)
                    processHolder.notification.cancel();


                synchronized (getActiveProcessList()) {
                    getActiveProcessList().remove(processHolder);

                    if (processHolder.groupId != 0 && processHolder.deviceId != null)
                        notifyTaskStatusChange(processHolder.groupId, processHolder.deviceId, TASK_STATUS_STOPPED);

                    notifyTaskRunningListChange();
                }
            }
        }
    }

    private class SeamlessClientHandler implements CoolSocket.Client.ConnectionHandler
    {
        private final TransferInstance mTransfer;

        SeamlessClientHandler(TransferInstance transferInstance)
        {
            mTransfer = transferInstance;
        }

        @Override
        public void onConnect(CoolSocket.Client client)
        {
            NetworkDevice thisDevice = AppUtils.getLocalDevice(CommunicationService.this);
            boolean retry = false;

            ProcessHolder processHolder = new ProcessHolder();

            processHolder.type = TransferObject.Type.INCOMING;
            processHolder.groupId = mTransfer.getGroup().groupId;
            processHolder.deviceId = mTransfer.getAssignee().deviceId;
            processHolder.activeConnection = new CoolSocket.ActiveConnection(AppConfig.DEFAULT_SOCKET_TIMEOUT);
            processHolder.builder = new CoolTransfer.Receive.Builder<>();

            processHolder.builder.setExtra(processHolder);

            synchronized (getActiveProcessList()) {
                getActiveProcessList().add(processHolder);
            }

            notifyTaskStatusChange(processHolder.groupId, processHolder.deviceId, TASK_STATUS_ONGOING);
            notifyTaskRunningListChange();
            Log.d(TAG, "SeamlessClientHandler.onConnect()---init");
            try {
                try {
                    // this will first connect to CommunicationService to make sure the connection
                    // is okay
                    CommunicationBridge.Client handshakeClient
                            = new CommunicationBridge.Client(getDatabase());
                    handshakeClient.setDevice(mTransfer.getDevice());
                    CoolSocket.ActiveConnection initialConnection = handshakeClient
                            .communicate(mTransfer.getDevice(), mTransfer.getConnection());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Keyword.REQUEST, Keyword.REQUEST_HANDSHAKE);
                    initialConnection.reply(jsonObject.toString());
                    Log.d(TAG, "SeamlessClientHandler.onConnect(): reply: empty");

                    JSONObject resultObject = new JSONObject(initialConnection.receive().response);
                    Log.d(TAG, "SeamlessClientHandler.onConnect(): Initial connection response: " + resultObject.toString());

                    if (!resultObject.getBoolean(Keyword.RESULT))
                        throw new Exception("Server rejected the request");
                } catch (Exception e) {
                    getNotificationHelper().notifyConnectionError(mTransfer, TransferObject.Type.INCOMING, null);
                    e.printStackTrace();
                    return;
                }

                try {
                    processHolder.activeConnection
                            .connect(new InetSocketAddress(mTransfer.getConnection().ipAddress, AppConfig.SERVER_PORT_SEAMLESS));
                } catch (Exception e) {
                    getNotificationHelper().notifyConnectionError(mTransfer, TransferObject.Type.INCOMING, null);
                    e.printStackTrace();
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Keyword.TRANSFER_GROUP_ID, processHolder.groupId);
                    jsonObject.put(Keyword.TRANSFER_DEVICE_ID, thisDevice.deviceId);
                    processHolder.activeConnection.reply(jsonObject.toString());
                    Log.d(TAG, "SeamlessClientHandler.onConnect(): reply: empty");

                    CoolSocket.ActiveConnection.Response response = processHolder.activeConnection.receive();
                    JSONObject request = new JSONObject(response.response);
                    Log.d(TAG, "SeamlessClientHandler.onConnect(): receive: " + response.response);

                    if (!request.getBoolean(Keyword.RESULT)) {
                        Log.d(TAG, "SeamlessClientHandler.onConnect(): false result, it will exit.");

                        String errorCode = request.has(Keyword.ERROR)
                                ? request.getString(Keyword.ERROR)
                                : null;

                        if (Keyword.ERROR_NOT_FOUND.equals(errorCode)) {
                            ValuesBucket valuesBucket = new ValuesBucket();
                            valuesBucket.putString(AccessDatabase.FIELD_TRANSFER_FLAG, TransferObject.Flag.REMOVED.toString());

                            getDatabase().update(TransferUtils.createTransferSelection(
                                    processHolder.groupId,
                                    processHolder.deviceId,
                                    TransferObject.Flag.DONE,
                                    false), valuesBucket);
                        }

                        getNotificationHelper().notifyConnectionError(mTransfer, TransferObject.Type.INCOMING, errorCode);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                {
                    while (processHolder.activeConnection.getSocket() != null
                            && processHolder.activeConnection.getSocket().isConnected()) {
                        processHolder.builder.reset();

                        // Exiting directly will not be a problem since, after this phase, there will
                        // be a response how things went.
                        if (processHolder.builder.getTransferProgress().isInterrupted())
                            break;

                        try {
                            TransferObject firstAvailableTransfer = TransferUtils
                                    .fetchValidTransfer(getContext(),
                                            processHolder.groupId,
                                            processHolder.deviceId,
                                            processHolder.type);

                            if (firstAvailableTransfer == null) {
                                Log.d(TAG, "SeamlessClientHandler(): Exiting because there is no pending file instance left");
                                break;
                            }

                            processHolder.transferObject = firstAvailableTransfer;
//                            processHolder.currentFile = new File(processHolder.transferObject.file);
                            processHolder.currentFile = FileUtils.getUploadFile(getContext(),processHolder.transferObject.friendlyName);
                            getNotificationHelper().notifyFileTransaction(processHolder);

                            {
                                Receive.Builder<ProcessHolder> receiveBuilder = (Receive.Builder<ProcessHolder>) processHolder.builder;
                                //todo bug
                                receiveBuilder.setOutputStream(new FileOutputStream(processHolder.currentFile))
                                        .setServerSocket(new ServerSocket(0))
                                        .setTimeout(AppConfig.DEFAULT_SOCKET_TIMEOUT)
                                        .setBuffer(new byte[AppConfig.BUFFER_LENGTH_DEFAULT])
                                        .setFileSize(processHolder.transferObject.fileSize)
                                        .setExtra(processHolder);

                                Receive.EventHandler handler = mReceive.prepare(receiveBuilder);
                                long currentSize = processHolder.currentFile.length();
                                processHolder.transferObject.skippedBytes = currentSize;

                                {
                                    JSONObject jsonObject = new JSONObject();

                                    jsonObject.put(Keyword.TRANSFER_REQUEST_ID, processHolder.transferObject.requestId);
                                    jsonObject.put(Keyword.TRANSFER_GROUP_ID, processHolder.transferObject.groupId);
                                    jsonObject.put(Keyword.TRANSFER_SOCKET_PORT, receiveBuilder.getServerSocket().getLocalPort());
                                    jsonObject.put(Keyword.RESULT, true);

                                    if (currentSize > 0)
                                        jsonObject.put(Keyword.SKIPPED_BYTES, currentSize);

                                    handler.getExtra().activeConnection.reply(jsonObject.toString());
                                    Log.d(TAG, "Receive.onTaskPrepareSocket(): reply: " + jsonObject.toString());
                                }

                                {

                                    JSONObject response = new JSONObject(handler.getExtra().activeConnection.receive().response);
                                    Log.d(TAG, "Receive.onTaskPrepareSocket(): receive: " + response.toString());

                                    if (!response.getBoolean(Keyword.RESULT)) {
                                        if (response.has(Keyword.TRANSFER_JOB_DONE)
                                                && !response.getBoolean(Keyword.TRANSFER_JOB_DONE)) {
                                            handler.getTransferProgress().interrupt();
                                            Log.d(TAG, "Receive.onTaskPrepareSocket(): Transfer should be closed, babe!");
                                            break;
                                        } else if (response.has(Keyword.FLAG) && Keyword.FLAG_GROUP_EXISTS.equals(response.getString(Keyword.FLAG))) {
                                            if (response.has(Keyword.ERROR) && response.getString(Keyword.ERROR).equals(Keyword.ERROR_NOT_FOUND)) {
                                                handler.getExtra().transferObject.flag = TransferObject.Flag.REMOVED;
                                                Log.d(TAG, "Receive.onTaskPrepareSocket(): Sender says it does not have the file defined");
                                            } else if (response.has(Keyword.ERROR) && response.getString(Keyword.ERROR).equals(Keyword.ERROR_NOT_ACCESSIBLE)) {
                                                handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;
                                                Log.d(TAG, "Receive.onTaskPrepareSocket(): Sender says it can't open the file");
                                            } else if (response.has(Keyword.ERROR) && response.getString(Keyword.ERROR).equals(Keyword.ERROR_UNKNOWN)) {
                                                handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;
                                                Log.d(TAG, "Receive.onTaskPrepareSocket(): Sender says an unknown error occurred");
                                            }
                                        }
                                    } else {
                                        long sizeChanged = response.has(Keyword.SIZE_CHANGED)
                                                ? response.getLong(Keyword.SIZE_CHANGED)
                                                : -1;

                                        boolean sizeActuallyChanged = sizeChanged > -1 &&
                                                handler.getExtra().transferObject.fileSize != sizeChanged;

                                        boolean canContinue = !sizeActuallyChanged || currentSize < 1;

                                        if (sizeActuallyChanged) {
                                            Log.d(TAG, "Receive.onTaskPrepareSocket(): Sender says the file has a new size");
                                            handler.getExtra().transferObject.fileSize = response.getLong(Keyword.SIZE_CHANGED);
                                        }

                                        if (!canContinue) {
                                            Log.d(TAG, "Receive.onTaskPrepareSocket(): The change may broke the previous file which has a length. Cannot take the risk.");
                                            handler.getExtra().transferObject.flag = TransferObject.Flag.REMOVED;
                                        } else
                                            mReceive.receive(handler, true);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            retry = true;

                            if (!processHolder.recoverInterruptions) {
                                TransferUtils.recoverIncomingInterruptions(CommunicationService.this, processHolder.groupId);
                                processHolder.recoverInterruptions = true;
                            }

                            break;
                        } finally {
                            if (processHolder.transferObject != null) {
                                // We are now updating instances always at the end because it will be
                                // changed by the process itself naturally
                                Log.d(TAG, "SeamlessClientHandler.onConnect(): Updating file instances to " + processHolder.transferObject.flag.toString());
                                getDatabase().update(processHolder.transferObject);
                            }
                        }
                    }
                }

                // Check if all the pending files are flagged with Flag.DONE
                try {


                    boolean isJobDone = CoolTransfer.Flag.CONTINUE.equals(processHolder.builder.getFlag());
                    boolean hasLeftFiles = getDatabase().getFirstFromTable(TransferUtils.createTransferSelection(
                            processHolder.groupId,
                            processHolder.deviceId,
                            TransferObject.Flag.DONE,
                            false)) == null;

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Keyword.RESULT, false);
                    jsonObject.put(Keyword.TRANSFER_JOB_DONE, isJobDone && hasLeftFiles);
                    processHolder.activeConnection.reply(jsonObject.toString());
                    Log.d(TAG, "SeamlessClientHandler.onConnect(): reply: done ?? " + (isJobDone && hasLeftFiles));

                    if (!retry)
                        if (!processHolder.builder.getTransferProgress().isInterrupted()) {
                            // If retry requested, don't show a notification because this method will loop
                            if (isJobDone) {
                                //需要验证
                                getNotificationHelper().notifyFileReceived(processHolder, mTransfer.getDevice(), processHolder.currentFile);
                                Log.d(TAG, "SeamlessClientHandler.onConnect(): Notify user");
                            } else {
                                getNotificationHelper().notifyReceiveError(processHolder);
                                Log.d(TAG, "SeamlessClientHandler.onConnect(): Some files was not received");
                            }
                        } else {
                            // If there was an error it should be handled by showing another error notification
                            // most of which are seemingly potential headache in the future
                            Log.d(TAG, "SeamlessClientHandler.onConnect(): Removing notification an error is already notified");
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (processHolder.activeConnection != null && !processHolder.activeConnection.getSocket().isClosed())
                        processHolder.activeConnection.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                synchronized (getActiveProcessList()) {
                    getActiveProcessList().remove(processHolder);
                }

                notifyTaskStatusChange(mTransfer.getGroup().groupId, mTransfer.getAssignee().deviceId, TASK_STATUS_STOPPED);
                notifyTaskRunningListChange();

                Log.d(TAG, "We have exited");

                if (retry && processHolder.attemptsLeft > 0
                        && !processHolder.builder.getTransferProgress().isInterrupted()) {
                    try {
                        startFileReceiving(processHolder.groupId, processHolder.deviceId);
                        processHolder.attemptsLeft--;
                    } catch (Exception e) {
                        Log.d(TAG, "SeamlessClientHandler.onConnect(): Restart is requested, but transfer instance failed to reconstruct");
                    }
                }
            }
        }
    }

    public class Receive extends CoolTransfer.Receive<ProcessHolder>
    {
        @Override
        public Flag onError(TransferHandler<ProcessHolder> handler, Exception error)
        {
            if (error != null)
                error.printStackTrace();

            handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;
            getNotificationHelper().notifyReceiveError(handler.getExtra().transferObject);

            return Flag.CANCEL_ALL;
        }

        @Override
        public void onDestroy(TransferHandler<ProcessHolder> handler)
        {
            if (handler.getTransferProgress().isInterrupted()
                    && TransferObject.Flag.IN_PROGRESS.equals(handler.getExtra().transferObject.flag))
                handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;
        }

        @Override
        public void onNotify(TransferHandler<ProcessHolder> handler, int percentage)
        {
            // Some bytes have been received, meaning we can handle another file recovery (useful for big files)
            handler.getExtra().recoverInterruptions = false;


            handler.getExtra().transferObject.flag = TransferObject.Flag.IN_PROGRESS;
            handler.getExtra().transferObject.flag.setBytesValue(handler.getTransferProgress().getCurrentTransferredByte());

            getDatabase().update(handler.getExtra().transferObject);

            // We have transferred bytes now, so reset the counter; cuz it works
            handler.getExtra().attemptsLeft = 2;
        }

        @Override
        public void onTaskEnd(TransferHandler<ProcessHolder> handler)
        {
            try {
//                handler.getExtra().currentFile.sync();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (handler.getFileSize() == handler.getExtra().currentFile.length()) {
                handler.getExtra().transferObject.flag = TransferObject.Flag.DONE;
                File currentFile = handler.getExtra().currentFile;

                if (currentFile.getParentFile() != null)
                    try {
                        //todo
                        //handler.getExtra().currentFile = FileUtils.saveReceivedFile(currentFile.getParentFile(), currentFile, handler.getExtra().transferObject);
                        handler.getExtra().transferObject.file = handler.getExtra().currentFile.getName();
                        Log.d(TAG, "Receive.onTransferCompleted(): Saved as: " + handler.getExtra().currentFile.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            } else {
                handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;
                handler.setFlag(Flag.CANCEL_CURRENT);
            }
        }

        @Override
        public Flag onTaskPrepareSocket(TransferHandler<ProcessHolder> handler)
        {
            return Flag.CONTINUE;
        }

        @Override
        public Flag onTaskPrepareSocket(final TransferHandler<ProcessHolder> handler, final ServerSocket serverSocket)
        {
            return Flag.CONTINUE;
        }

        @Override
        public Flag onPrepare(TransferHandler<ProcessHolder> handler)
        {
            if (handler.getTransferProgress().getTotalByte() == 0) {
                TransferGroup.Index indexInstance = new TransferGroup.Index();

                getDatabase().calculateTransactionSize(handler.getExtra().transferObject.groupId, indexInstance);

                handler.getTransferProgress().setTotalByte(indexInstance.incoming - indexInstance.incomingCompleted);
            }

            return Flag.CONTINUE;
        }

        @Override
        public Flag onTaskOrientateStreams(TransferHandler<ProcessHolder> handler, InputStream inputStream, OutputStream outputStream)
        {
            if (handler.getExtra().transferObject.skippedBytes > 0)
                try {
                    handler.skipBytes(handler.getExtra().transferObject.skippedBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Flag.CONTINUE;
                }

            return Flag.CONTINUE;
        }

        @Override
        public void onProcessListChanged(ArrayList<TransferHandler<ProcessHolder>> processList, TransferHandler<ProcessHolder> handler, boolean isAdded)
        {
            super.onProcessListChanged(processList, handler, isAdded);


        }
    }

    public class Send extends CoolTransfer.Send<ProcessHolder>
    {
        @Override
        public Flag onError(TransferHandler<ProcessHolder> handler, Exception error)
        {
            if (error != null)
                error.printStackTrace();

            handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;

            return Flag.CANCEL_ALL;
        }

        @Override
        public void onNotify(TransferHandler<ProcessHolder> handler, int percentage)
        {


            handler.getExtra().transferObject.flag = TransferObject.Flag.IN_PROGRESS;
            handler.getExtra().transferObject.flag.setBytesValue(handler.getTransferProgress().getCurrentTransferredByte());

            getDatabase().update(handler.getExtra().transferObject);
        }

        @Override
        public void onTaskEnd(TransferHandler<ProcessHolder> handler)
        {
            handler.getExtra().transferObject.flag = handler.getTransferProgress().getCurrentTransferredByte() == handler.getFileSize()
                    ? TransferObject.Flag.DONE
                    : TransferObject.Flag.INTERRUPTED;
        }

        @Override
        public void onDestroy(TransferHandler<ProcessHolder> handler)
        {
            if (handler.getTransferProgress().isInterrupted()
                    && TransferObject.Flag.IN_PROGRESS.equals(handler.getExtra().transferObject.flag))
                handler.getExtra().transferObject.flag = TransferObject.Flag.INTERRUPTED;
        }

        @Override
        public Flag onTaskPrepareSocket(TransferHandler<ProcessHolder> handler)
        {
            return Flag.CONTINUE;
        }

        @Override
        public Flag onPrepare(TransferHandler<ProcessHolder> handler)
        {
            if (handler.getTransferProgress().getTotalByte() == 0) {
                TransferGroup.Index indexInstance = new TransferGroup.Index();

                getDatabase().calculateTransactionSize(handler.getExtra().transferObject.groupId, indexInstance);
                handler.getTransferProgress().setTotalByte(indexInstance.outgoing - indexInstance.outgoingCompleted);
            }

            return Flag.CONTINUE;
        }


        @Override
        public Flag onTaskOrientateStreams(TransferHandler<ProcessHolder> handler, InputStream inputStream, OutputStream outputStream)
        {
            super.onTaskOrientateStreams(handler, inputStream, outputStream);

            if (handler.getExtra().transferObject.skippedBytes > 0)
                try {
                    handler.skipBytes(handler.getExtra().transferObject.skippedBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Flag.CONTINUE;
                }

            return Flag.CONTINUE;
        }
    }

    public static class ProcessHolder
    {
        public CoolTransfer.ParentBuilder<ProcessHolder> builder;
        public CoolSocket.ActiveConnection activeConnection;
        public DynamicNotification notification;
        public TransferObject transferObject;
        public File currentFile;
        public TransferObject.Type type;
        public String deviceId;
        public boolean recoverInterruptions = false;
        public long groupId;
        public int attemptsLeft = 2;
    }
}
