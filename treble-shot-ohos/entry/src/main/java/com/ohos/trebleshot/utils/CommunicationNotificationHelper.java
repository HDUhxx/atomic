package com.ohos.trebleshot.utils;

import com.ohos.trebleshot.config.Keyword;
import com.ohos.trebleshot.coolsocket.CoolTransfer;
import com.ohos.trebleshot.object.*;
import com.ohos.trebleshot.service.CommunicationService;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.fraction.FileExplorerFragment;
import me.panavtec.title.fraction.TransferGroupListFragment;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.hmutils.TextUtils;
import me.panavtec.title.slice.TextEditorAbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.utils.net.Uri;

import java.io.File;

import static me.panavtec.title.hmutils.HMUtils.transferLongToDate;

/**
 * created by: Veli
 * date: 26.01.2018 18:29
 */

public class CommunicationNotificationHelper
{
    public static final int SERVICE_COMMUNICATION_FOREGROUND_NOTIFICATION_ID = 1;

    private final NotificationUtils mNotificationUtils;

    public CommunicationNotificationHelper(NotificationUtils notificationUtils)
    {
        mNotificationUtils = notificationUtils;
    }

    public DynamicNotification getCommunicationServiceNotification(boolean seamlessMode,
                                                                   boolean pinAccess,
                                                                   boolean webShare)
    {
        StringBuilder builder = new StringBuilder();

        if(webShare)
            builder.append(getContext().getString(ResourceTable.String_butn_webShare));

        if (builder.length() > 0)
            builder.append(" - ");

        builder.append(getContext().getString(ResourceTable.String_text_communicationServiceRunning));

        Intent intent1 = new Intent();
        IntentAgent intentAgent1 =
                HMUtils.createIntentAgent(getContext(), intent1, CommunicationService.class.getName(), CommunicationService.ACTION_TOGGLE_SEAMLESS_MODE, IntentAgentConstant.OperationType.START_SERVICE);

        Intent intent = new Intent();
        IntentAgent intentAgent =
                HMUtils.createIntentAgent(getContext(), intent, CommunicationService.class.getName(), CommunicationService.ACTION_REVOKE_ACCESS_PIN, IntentAgentConstant.OperationType.START_SERVICE);

        Intent intent2 = new Intent();
        IntentAgent intentAgent2 =
                HMUtils.createIntentAgent(getContext(), intent2, CommunicationService.class.getName(), CommunicationService.ACTION_END_SESSION, IntentAgentConstant.OperationType.START_SERVICE);

        DynamicNotification notification = getUtils().buildDynamicNotification(SERVICE_COMMUNICATION_FOREGROUND_NOTIFICATION_ID, NotificationUtils.mNotificationChannelLow,
                getContext().getString(ResourceTable.String_text_communicationServiceStop));

        notification.setSmallIcon(ResourceTable.Media_ic_trebleshot_rounded_white_24dp_static)
                .setContentTitle(builder.toString())
//                .setContentText.(getContext().getString(ResourceTable.String_text_communicationServiceStop))
                .setAutoCancel(true)
                .addAction(ResourceTable.Media_ic_compare_arrows_white_24dp_static, getContext().getString(seamlessMode ? ResourceTable.String_butn_turnTrustZoneOff : ResourceTable.String_butn_turnTrustZoneOn),
                        intentAgent1)
                .setContentIntent(intentAgent2);

        if (pinAccess)
            notification.addAction(ResourceTable.Media_ic_autorenew_white_24dp_static, getContext().getString(ResourceTable.String_butn_revokePin), intentAgent);

        return notification.show();
    }

    public Context getContext()
    {
        return getUtils().getContext();
    }

    public NotificationUtils getUtils()
    {
        return mNotificationUtils;
    }

    public DynamicNotification notifyConnectionRequest(NetworkDevice device)
    {
        DynamicNotification notification = getUtils().buildDynamicNotification(AppUtils.getUniqueNumber(), NotificationUtils.mNotificationChannelHigh,
                getContext().getString(ResourceTable.String_text_connectionPermission),
                getContext().getString(ResourceTable.String_ques_allowDeviceToConnect));

        Intent acceptIntent = new Intent();
//        Intent dialogIntent = new Intent(getContext(), DialogEventReceiver.class);

        acceptIntent.setParam(CommunicationService.EXTRA_DEVICE_ID, device.deviceId);
        acceptIntent.setParam(NotificationUtils.EXTRA_NOTIFICATION_ID, notification.getNotificationId());

        Intent rejectIntent = ((Intent) acceptIntent.clone());

        acceptIntent.setParam(CommunicationService.EXTRA_IS_ACCEPTED, true);
        rejectIntent.setParam(CommunicationService.EXTRA_IS_ACCEPTED, false);

 
        IntentAgent positiveIntent =
                HMUtils.createIntentAgent(getContext(), acceptIntent, CommunicationService.class.getName(),CommunicationService.ACTION_IP, IntentAgentConstant.OperationType.START_SERVICE);
        
        IntentAgent negativeIntent =
                HMUtils.createIntentAgent(getContext(), rejectIntent, CommunicationService.class.getName());

        notification.setSmallIcon(ResourceTable.Media_ic_alert_circle_outline_white_24dp_static)
//                .setContentTitle(getContext().getString(ResourceTable.String_text_connectionPermission))
//                .setContentText.(getContext().getString(ResourceTable.String_ques_allowDeviceToConnect))
                .setContentInfo(device.nickname)
//                .setContentIntent(PendingIntent.getBroadcast(getContext(), AppUtils.getUniqueNumber(), dialogIntent, 0))
                .setDefaults(getUtils().getNotificationSettings())
                .setDeleteIntent(negativeIntent)
                .addAction(ResourceTable.Media_ic_check_white_24dp_static, getContext().getString(ResourceTable.String_butn_accept), positiveIntent)
                .addAction(ResourceTable.Media_ic_close_white_24dp_static, getContext().getString(ResourceTable.String_butn_reject), negativeIntent);
//                .setTicker(getContext().getString(ResourceTable.String_text_connectionPermission));

        return notification.show();
    }

    public DynamicNotification notifyTransferRequest(TransferObject transferObject, NetworkDevice device, int numberOfFiles)
    {

        String message = numberOfFiles > 1 ? getContext().getString(ResourceTable.String_ques_receiveMultipleFiles, numberOfFiles, numberOfFiles) : transferObject.friendlyName;

        DynamicNotification notification = getUtils().buildDynamicNotification(
                TransferUtils.createUniqueTransferId(transferObject.groupId, device.deviceId, transferObject.type),
                NotificationUtils.mNotificationChannelHigh, message);

        Intent acceptIntent = new Intent()
                .setParam(CommunicationService.EXTRA_DEVICE_ID, device.deviceId)
                .setParam(CommunicationService.EXTRA_GROUP_ID, transferObject.groupId).setParam(NotificationUtils.EXTRA_NOTIFICATION_ID, notification.getNotificationId());

        Intent rejectIntent = ((Intent) acceptIntent.clone());

        acceptIntent.setParam(CommunicationService.EXTRA_IS_ACCEPTED, true);
        rejectIntent.setParam(CommunicationService.EXTRA_IS_ACCEPTED, false);


        IntentAgent positiveIntent =
                HMUtils.createIntentAgent(getContext(), acceptIntent, CommunicationService.class.getName(),CommunicationService.ACTION_FILE_TRANSFER, IntentAgentConstant.OperationType.START_SERVICE);

        IntentAgent negativeIntent =
                HMUtils.createIntentAgent(getContext(), rejectIntent, CommunicationService.class.getName());


        Intent contentIntent = new Intent()
                .setParam(TransferGroupListFragment.EXTRA_GROUP_ID, transferObject.groupId);

        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), contentIntent, TransferGroupListFragment.class.getName(), TransferGroupListFragment.ACTION_LIST_TRANSFERS, IntentAgentConstant.OperationType.START_ABILITY);

        notification.setSmallIcon(ResourceTable.Media_stat_sys_download_anim0)
                .setContentTitle(getContext().getString(ResourceTable.String_ques_receiveFile))
//                .setContentText.(message)
                .setContentInfo(device.nickname)
                .setContentIntent(contentIntentAgent)
                .setDefaults(getUtils().getNotificationSettings())
                .setDeleteIntent(negativeIntent)
                .addAction(ResourceTable.Media_ic_check_white_24dp_static, getContext().getString(ResourceTable.String_butn_receive), positiveIntent)
                .addAction(ResourceTable.Media_ic_close_white_24dp_static, getContext().getString(ResourceTable.String_butn_reject), negativeIntent);
//                .setTicker(getContext().getString(ResourceTable.String_ques_receiveFile));
//                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return notification.show();
    }

    public DynamicNotification notifyFileTransaction(CommunicationService.ProcessHolder processHolder) throws Exception
    {
        if (processHolder.notification == null) {
            NetworkDevice device = new NetworkDevice(processHolder.deviceId);
            getUtils().getDatabase().reconstruct(device);

            boolean isIncoming = TransferObject.Type.INCOMING.equals(processHolder.transferObject.type);

            processHolder.notification = getUtils().buildDynamicNotification(
                    TransferUtils.createUniqueTransferId(processHolder.groupId, device.deviceId, processHolder.transferObject.type),
                    NotificationUtils.mNotificationChannelLow,
                    getContext().getString(isIncoming ? ResourceTable.String_text_receiving : ResourceTable.String_text_sending));

            Intent cancelIntent = new Intent();
            cancelIntent.setParam(CommunicationService.EXTRA_REQUEST_ID, processHolder.transferObject.requestId);
            cancelIntent.setParam(CommunicationService.EXTRA_GROUP_ID, processHolder.groupId);
            cancelIntent.setParam(CommunicationService.EXTRA_DEVICE_ID, processHolder.deviceId);
            cancelIntent.setParam(NotificationUtils.EXTRA_NOTIFICATION_ID, processHolder.notification.getNotificationId());

            IntentAgent cancelIntentAgent =
                    HMUtils.createIntentAgent(getContext(), cancelIntent, CommunicationService.class.getName(),CommunicationService.ACTION_CANCEL_JOB, IntentAgentConstant.OperationType.START_SERVICE);

            Intent contentIntent = new Intent()
                    .setParam(TransferGroupListFragment.EXTRA_GROUP_ID, processHolder.transferObject.groupId);

            IntentAgent contentIntentAgent =
                    HMUtils.createIntentAgent(getContext(), contentIntent, TransferGroupListFragment.class.getName(), TransferGroupListFragment.ACTION_LIST_TRANSFERS, IntentAgentConstant.OperationType.START_ABILITY);


            processHolder.notification.setSmallIcon(isIncoming ? ResourceTable.Media_stat_sys_download_anim0 : ResourceTable.Media_stat_sys_upload_anim0)
//                    .setContentText.(getContext().getString(isIncoming ? ResourceTable.String_text_receiving : ResourceTable.String_text_sending))
                    .setContentInfo(device.nickname)
                    .setContentIntent(contentIntentAgent)
//                    .setOngoing(true)
                    .addAction(ResourceTable.Media_ic_close_white_24dp_static, getContext().getString(isIncoming ? ResourceTable.String_butn_cancelReceiving : ResourceTable.String_butn_cancelSending), cancelIntentAgent);
        }

        processHolder.notification.setContentTitle(processHolder.transferObject.friendlyName);

        return processHolder.notification;
    }

    public DynamicNotification notifyClipboardRequest(NetworkDevice device, TextStreamObject object)
    {
        DynamicNotification notification = getUtils().buildDynamicNotification(object.id, NotificationUtils.mNotificationChannelHigh,
                getContext().getString(ResourceTable.String_text_textReceived));

        Intent acceptIntent = new Intent()
                .setParam(CommunicationService.EXTRA_CLIPBOARD_ID, object.id)
                .setParam(NotificationUtils.EXTRA_NOTIFICATION_ID, notification.getNotificationId());

        Intent activityIntent = new Intent();
        activityIntent
                .setParam(TextEditorAbilitySlice.EXTRA_CLIPBOARD_ID, object.id);
        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), activityIntent, CommunicationService.class.getName(),TextEditorAbilitySlice.ACTION_EDIT_TEXT, IntentAgentConstant.OperationType.START_SERVICE);

        Intent rejectIntent = ((Intent) acceptIntent.clone());

        acceptIntent.setParam(CommunicationService.EXTRA_CLIPBOARD_ACCEPTED, true);
        rejectIntent.setParam(CommunicationService.EXTRA_CLIPBOARD_ACCEPTED, false);

        IntentAgent positiveIntent =
                HMUtils.createIntentAgent(getContext(), acceptIntent, CommunicationService.class.getName(),CommunicationService.ACTION_CLIPBOARD, IntentAgentConstant.OperationType.START_SERVICE);

        IntentAgent negativeIntent =
                HMUtils.createIntentAgent(getContext(), rejectIntent, CommunicationService.class.getName());


        notification
                .setSmallIcon(ResourceTable.Media_stat_sys_download_anim0)
                .setContentTitle(getContext().getString(ResourceTable.String_ques_copyToClipboard))
//                .setContentText.(getContext().getString(ResourceTable.String_text_textReceived))
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(object.text)
//                        .setBigContentTitle(getContext().getString(ResourceTable.String_ques_copyToClipboard)))
                .setContentInfo(device.nickname)
                .setContentIntent(contentIntentAgent)
                .setDefaults(getUtils().getNotificationSettings())
                .setDeleteIntent(negativeIntent)
                .addAction(ResourceTable.Media_ic_check_white_24dp_static, "拷贝", positiveIntent)
                .addAction(ResourceTable.Media_ic_close_white_24dp_static, "取消", negativeIntent);
//                .setTicker(getContext().getString(ResourceTable.String_text_receivedTextSummary));
//                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return notification.show();
    }

    public DynamicNotification notifyFileReceived(CommunicationService.ProcessHolder processHolder, NetworkDevice device, File savePath)
    {

        CoolTransfer.TransferProgress progress = processHolder.builder.getTransferProgress();

        DynamicNotification notification = getUtils().buildDynamicNotification(
                TransferUtils.createUniqueTransferId(processHolder.groupId, device.deviceId, processHolder.transferObject.type),
                NotificationUtils.mNotificationChannelHigh,
                getContext().getString(ResourceTable.String_text_receivedTransfer,
                        FileUtils.sizeExpression(progress.getTransferredByte(), false),
                        transferLongToDate("E, d MMM yyyy HH:mm:ss 'GMT'",progress.getTimeElapsed())
                ));

        notification
                .setSmallIcon(ResourceTable.Media_stat_sys_download_anim0)
                .setContentInfo(device.nickname)
                .setAutoCancel(true)
                .setDefaults(getUtils().getNotificationSettings());
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentText.(getContext().getString(ResourceTable.String_text_receivedTransfer,
//                        FileUtils.sizeExpression(progress.getTransferredByte(), false),
//                        transferLongToDate("E, d MMM yyyy HH:mm:ss 'GMT'",progress.getTimeElapsed())
//                ));

        Intent contentIntent = new Intent()
                .setParam(FileExplorerFragment.EXTRA_FILE_PATH, Uri.getUriFromFile(savePath));

        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), contentIntent, FileExplorerFragment.class.getName());

        if (progress.getTransferredFileCount() != 1) {
            notification
                    .setContentTitle(getContext().getString(ResourceTable.String_text_fileReceiveCompletedSummary, progress.getTransferredFileCount()))
                    .setContentIntent(contentIntentAgent);
        } else {
            try {
                Intent openIntent = new Intent();
//                openIntent.setUriAndType(Uri.getUriFromFile(processHolder.currentFile), "");
                notification.setContentIntent(HMUtils.createIntentAgentWithUri(getContext(), openIntent, IntentAgentConstant.OperationType.START_ABILITY,Uri.getUriFromFile(processHolder.currentFile)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            notification
                    .setContentTitle(processHolder.transferObject.friendlyName)
                    .addAction(ResourceTable.Media_ic_folder_white_24dp_static, getContext().getString(ResourceTable.String_butn_showFiles),
                            contentIntentAgent);
        }

        return notification.show();
    }

    public DynamicNotification notifyReceiveError(CommunicationService.ProcessHolder processHolder)
    {
        DynamicNotification notification = getUtils().buildDynamicNotification(
                TransferUtils.createUniqueTransferId(processHolder.groupId, processHolder.deviceId, processHolder.transferObject.type),
                NotificationUtils.mNotificationChannelHigh, getContext().getString(ResourceTable.String_mesg_fileReceiveFilesLeftError));


        Intent contentIntent = new Intent()
                .setParam(TransferGroupListFragment.EXTRA_GROUP_ID, processHolder.groupId);

        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), contentIntent, TransferGroupListFragment.class.getName(), TransferGroupListFragment.ACTION_LIST_TRANSFERS, IntentAgentConstant.OperationType.START_ABILITY);

        notification.setSmallIcon(ResourceTable.Media_ic_alert_circle_outline_white_24dp_static)
                .setContentTitle(getContext().getString(ResourceTable.String_text_error))
//                .setContentText.(getContext().getString(ResourceTable.String_mesg_fileReceiveFilesLeftError))
                .setAutoCancel(true)
                .setDefaults(getUtils().getNotificationSettings())
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntentAgent);

        return notification.show();
    }

    public DynamicNotification notifyReceiveError(TransferObject transferObject)
    {
        DynamicNotification notification = getUtils().buildDynamicNotification(transferObject.getId(), NotificationUtils.mNotificationChannelHigh,
                getContext().getString(ResourceTable.String_text_error),
                getContext().getString(ResourceTable.String_mesg_fileReceiveError, transferObject.friendlyName)
        );


        Intent contentIntent = new Intent()
                .setParam(TransferGroupListFragment.EXTRA_GROUP_ID, transferObject.groupId)
                .setParam(TransferGroupListFragment.EXTRA_REQUEST_ID, transferObject.requestId)
                .setParam(TransferGroupListFragment.EXTRA_REQUEST_TYPE, transferObject.type)
                .setParam(TransferGroupListFragment.EXTRA_DEVICE_ID, transferObject.deviceId);

        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), contentIntent, TransferGroupListFragment.class.getName(), TransferGroupListFragment.ACTION_LIST_TRANSFERS, IntentAgentConstant.OperationType.START_ABILITY);

        notification.setSmallIcon(ResourceTable.Media_ic_alert_circle_outline_white_24dp_static)
//                .setContentTitle(getContext().getString(ResourceTable.String_text_error))
//                .setContentText.(getContext().getString(ResourceTable.String_mesg_fileReceiveError, transferObject.friendlyName))
                .setAutoCancel(true)
                .setDefaults(getUtils().getNotificationSettings())
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntentAgent);

        return notification.show();
    }

    public DynamicNotification notifyConnectionError(TransferInstance transferInstance, TransferObject.Type type, String errorKey)
    {

        String errorMsg = getContext().getString(ResourceTable.String_mesg_deviceConnectionError, transferInstance.getDevice().nickname, TextUtils.getAdapterName(getContext(), transferInstance.getConnection()));

        if (errorKey != null)
            switch (errorKey) {
                case Keyword.ERROR_NOT_ALLOWED:
                    errorMsg = getContext().getString(ResourceTable.String_mesg_notAllowed);
                    break;
                case Keyword.ERROR_NOT_FOUND:
                    errorMsg = getContext().getString(ResourceTable.String_mesg_notValidTransfer);
            }

        Intent contentIntent = new Intent()
                .setParam(TransferGroupListFragment.EXTRA_GROUP_ID, transferInstance.getGroup().groupId);

        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), contentIntent, TransferGroupListFragment.class.getName(), TransferGroupListFragment.ACTION_LIST_TRANSFERS, IntentAgentConstant.OperationType.START_ABILITY);

        DynamicNotification notification = getUtils().buildDynamicNotification(
                TransferUtils.createUniqueTransferId(transferInstance.getGroup().groupId, transferInstance.getDevice().deviceId, type),
                NotificationUtils.mNotificationChannelHigh, errorMsg);
        notification.setSmallIcon(ResourceTable.Media_ic_alert_circle_outline_white_24dp_static)
                .setContentTitle(getContext().getString(ResourceTable.String_text_error))
//                .setContentText.(errorMsg)
                .setAutoCancel(true)
                .setDefaults(getUtils().getNotificationSettings())
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntentAgent);

        return notification.show();
    }

    public DynamicNotification notifyPrepareFiles(TransferGroup group, NetworkDevice device)
    {
        DynamicNotification notification = getUtils().buildDynamicNotification(
                TransferUtils.createUniqueTransferId(group.groupId, device.deviceId, TransferObject.Type.INCOMING),
                NotificationUtils.mNotificationChannelLow, getContext().getString(ResourceTable.String_text_preparingFiles),
                getContext().getString(ResourceTable.String_text_savingDetails)
        );

        Intent cancelIntent = new Intent()
                .setParam(NotificationUtils.EXTRA_NOTIFICATION_ID, notification.getNotificationId())
                .setParam(CommunicationService.EXTRA_GROUP_ID, group.groupId);

        IntentAgent negativeIntent =
                HMUtils.createIntentAgent(getContext(), cancelIntent, CommunicationService.class.getName(), CommunicationService.ACTION_CANCEL_INDEXING, IntentAgentConstant.OperationType.START_SERVICE);

        Intent contentIntent = new Intent()
                .setParam(TransferGroupListFragment.EXTRA_GROUP_ID, group.groupId);

        IntentAgent contentIntentAgent =
                HMUtils.createIntentAgent(getContext(), contentIntent, TransferGroupListFragment.class.getName(), TransferGroupListFragment.ACTION_LIST_TRANSFERS, IntentAgentConstant.OperationType.START_ABILITY);


        notification.setSmallIcon(ResourceTable.Media_stat_sys_download_anim4)
//                .setContentTitle(getContext().getString(ResourceTable.String_text_preparingFiles))
//                .setContentText.(getContext().getString(ResourceTable.String_text_savingDetails))
                .setAutoCancel(false)
                .addAction(ResourceTable.Media_ic_close_white_24dp_static, getContext().getString(ResourceTable.String_butn_cancel), negativeIntent)
                .setContentIntent(contentIntentAgent);

        return notification.show();
    }

    public DynamicNotification notifyStuckThread(CommunicationService.ProcessHolder processHolder)
    {
        return notifyStuckThread(processHolder.groupId, processHolder.deviceId, processHolder.type);
    }

    public DynamicNotification notifyStuckThread(long groupId, String deviceId, TransferObject.Type type)
    {
        DynamicNotification notification = getUtils().buildDynamicNotification(
                TransferUtils.createUniqueTransferId(groupId, deviceId, type),
                NotificationUtils.mNotificationChannelLow, getContext().getString(ResourceTable.String_text_stopping),
                getContext().getString(ResourceTable.String_text_cancellingTransfer));

        Intent killIntent = new Intent()
                .setParam(CommunicationService.EXTRA_GROUP_ID, groupId)
                .setParam(CommunicationService.EXTRA_DEVICE_ID, deviceId)
                .setParam(NotificationUtils.EXTRA_NOTIFICATION_ID, notification.getNotificationId());

        IntentAgent killIntentAgent =
                HMUtils.createIntentAgent(getContext(), killIntent, CommunicationService.class.getName(), CommunicationService.ACTION_CANCEL_JOB, IntentAgentConstant.OperationType.START_SERVICE);

        notification.setSmallIcon(ResourceTable.Media_ic_alert_circle_outline_white_24dp_static)
//                .setOngoing(true)
//                .setContentTitle(getContext().getString(ResourceTable.String_text_stopping))
//                .setContentText.(getContext().getString(ResourceTable.String_text_cancellingTransfer))
//                .setProgressValue(0, 0, true)
                .addAction(ResourceTable.Media_ic_close_white_24dp_static, getContext().getString(ResourceTable.String_butn_killNow),
                        killIntentAgent);

        return notification.show();
    }

    public void showToast(String toastText)
    {
        ToastDialog toastDialog  = new ToastDialog(getContext());
        toastDialog.setTitleText(toastText);
        toastDialog.show();
    }

    public void showToast(int toastTextRes)
    {
        ToastDialog toastDialog  = new ToastDialog(getContext());
        toastDialog.setTitleText(getContext().getString(toastTextRes));
        toastDialog.show();
    }

    public void showToast(String text, int length)
    {
        ToastDialog toastDialog  = new ToastDialog(getContext());
        toastDialog.setTitleText(text);
        toastDialog.show();
    }

    public void showToast(int textRes, int length)
    {
        ToastDialog toastDialog  = new ToastDialog(getContext());
        toastDialog.setTitleText(getContext().getString(textRes));
        toastDialog.show();
    }
}
