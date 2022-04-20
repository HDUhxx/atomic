package com.ohos.trebleshot.utils;

import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.notification.NotificationActionButton;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.rpc.RemoteException;

import java.io.InputStream;

/**
 * Created by: veli
 * Date: 4/28/17 2:22 AM
 */

public class DynamicNotification
{
    private final int mNotificationId;
    private final NotificationRequest mNotificationRequest;
    private final Context mContext;
    private final NotificationRequest.NotificationNormalContent mNotificationNormalContent;

    public DynamicNotification(Context context, NotificationSlot slot, int notificationId, String title)
    {
        mContext = context;
        mNotificationNormalContent = new NotificationRequest.NotificationNormalContent();
        mNotificationNormalContent.setTitle(title);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(mNotificationNormalContent);
        mNotificationRequest = new NotificationRequest(notificationId);
        mNotificationRequest.setContent(notificationContent);
        mNotificationRequest.setSlotId(slot.getId());
        mNotificationId = notificationId;
    }

    public DynamicNotification(Context context, NotificationSlot slot, int notificationId, String title, String content)
    {
        mContext = context;
        mNotificationNormalContent = new NotificationRequest.NotificationNormalContent();
        mNotificationNormalContent.setTitle(title).setText(content);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(mNotificationNormalContent);
        mNotificationRequest = new NotificationRequest(notificationId);
        mNotificationRequest.setContent(notificationContent);
        mNotificationRequest.setSlotId(slot.getId());
        mNotificationId = notificationId;
    }


    public DynamicNotification cancel()
    {
        try {
            NotificationHelper.cancelNotification(mNotificationId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public int getNotificationId()
    {
        return mNotificationId;
    }
//
//    public DynamicNotification setNotificationId(int notificationId)
//    {
//        mNotificationId = notificationId;
//        return this;
//    }

    public DynamicNotification show()
    {
        try {
            NotificationHelper.publishNotification(mNotificationRequest);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public DynamicNotification setContentIntent(IntentAgent intentAgent) {
        mNotificationRequest.setIntentAgent(intentAgent);
        return this;
    }

    public DynamicNotification setContentText(String string) {
        mNotificationNormalContent.setText(string);
        return this;
    }

    public DynamicNotification setContentTitle(String title) {
        mNotificationNormalContent.setTitle(title);
        return this;
    }

    public DynamicNotification setSmallIcon(int resId) {
        PixelMap pixelMap = getPixelMap(resId);
        mNotificationRequest.setLittleIcon(pixelMap);
        return this;
    }

    public DynamicNotification setProgress(int max, int progress, boolean indeterminate){

        mNotificationRequest.setProgressBar(progress,max,indeterminate);
        return this;
    }

    public DynamicNotification updateProgress(int max, int percent, boolean indeterminate)  {
        try {
            mNotificationRequest.setProgressBar(percent,max,indeterminate);
            NotificationHelper.publishNotification(mNotificationRequest);
        }catch (Exception e){
            e.printStackTrace();
        }

        return this;
    }

    /**
     * 通过资源ID获取位图对象
     **/
    private PixelMap getPixelMap(int drawableId) {
        InputStream drawableInputStream = null;
        try {
            drawableInputStream = mContext.getResourceManager().getResource(drawableId);
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(drawableInputStream, sourceOptions);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(0, 0);
            decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
            decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
            return imageSource.createPixelmap(decodingOptions);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (drawableInputStream != null){
                    drawableInputStream.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public DynamicNotification addAction(int resId, String title, IntentAgent intent) {
        NotificationActionButton  notificationActionButton = new NotificationActionButton.Builder(
                getPixelMap(resId),
                title,
                intent
        ).build();

        mNotificationRequest.addActionButton(notificationActionButton);
        return this;
    }

    public DynamicNotification setAutoCancel(boolean b) {
        mNotificationRequest.setAutoDeletedTime(5000);
        return this;
    }

    public DynamicNotification setDeleteIntent(IntentAgent negativeIntent) {

        NotificationActionButton  notificationActionButton = new NotificationActionButton.Builder(
                null,
                "删除",
                negativeIntent
        ).build();

        mNotificationRequest.addActionButton(notificationActionButton);
        return this;
    }

    public DynamicNotification setDefaults(int notificationSettings) {
        return this;
    }

    public DynamicNotification setContentInfo(String extraText) {
        mNotificationNormalContent.setAdditionalText(extraText);
        return this;
    }
}
