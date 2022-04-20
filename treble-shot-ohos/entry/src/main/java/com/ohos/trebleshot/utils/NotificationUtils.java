package com.ohos.trebleshot.utils;

import com.ohos.trebleshot.database.AccessDatabase;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.data.preferences.Preferences;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.rpc.RemoteException;

/**
 * Created by: veli
 * Date: 4/28/17 2:00 AM
 */

public class NotificationUtils
{
    public static final String TAG = "NotificationUtils";
    public static NotificationSlot mNotificationChannelHigh;
//    public static final String NOTIFICATION_CHANNEL_HIGH = "tsHighPriority";
//    public static final String NOTIFICATION_CHANNEL_LOW = "tsLowPriority";

    public static NotificationSlot mNotificationChannelLow;

    public static final String EXTRA_NOTIFICATION_ID = "notificationId";

    private final Context mContext;
    private final AccessDatabase mDatabase;
    private Preferences mPreferences;

    public static NotificationUtils mNotificationUtilsInst;
    public static NotificationUtils getNotificationUtilsInst(Context context){
        if(mNotificationUtilsInst == null) {
            mNotificationUtilsInst = new NotificationUtils(context);
        }
        return  mNotificationUtilsInst;
    }

    public NotificationUtils(Context context)
    {
        mContext = context;
        mDatabase = AppUtils.getDatabase(context);
        mPreferences =  AppUtils.getPreferences(context);;

        // 创建notificationSlot对象
        mNotificationChannelLow = new NotificationSlot("slot_001", "TShot DEBUG的所有通知", NotificationSlot.LEVEL_MIN);
        mNotificationChannelLow.setDescription("NotificationSlotDescription");
        mNotificationChannelLow.setEnableVibration(true); // 设置振动提醒
        mNotificationChannelLow.setLockscreenVisibleness(NotificationRequest.VISIBLENESS_TYPE_PUBLIC);// 设置锁屏模式
        mNotificationChannelLow.setEnableLight(true); // 设置开启呼吸灯提醒
        mNotificationChannelLow.setLedLightColor(Color.RED.getValue());// 设置呼吸灯的提醒颜色

        mNotificationChannelHigh = mNotificationChannelLow;
        try {
            NotificationHelper.addNotificationSlot(mNotificationChannelLow);
        } catch (RemoteException ex) {
           ex.printStackTrace();
        }

    }

    public DynamicNotification buildDynamicNotification(long notificationId, NotificationSlot slot, String title)
    {
        // Let's hope it will turn out to be less painful
        return new DynamicNotification(mContext, slot,
                (int) (notificationId > Integer.MAX_VALUE ? notificationId / 100000 : notificationId), title);
    }

    public DynamicNotification buildDynamicNotification(long notificationId, NotificationSlot slot, String title, String content)
    {
        // Let's hope it will turn out to be less painful
        return new DynamicNotification(mContext, slot,
                (int) (notificationId > Integer.MAX_VALUE ? notificationId / 100000 : notificationId), title, content);
    }


    public NotificationUtils cancel(int notificationId)
    {
        try {
            NotificationHelper.cancelNotification(notificationId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Context getContext()
    {
        return mContext;
    }

    public AccessDatabase getDatabase()
    {
        return mDatabase;
    }

    public int getNotificationSettings()
    {
        int makeSound = (mPreferences.getBoolean("notification_sound", true)) ? 1 : 0;
        int vibrate = (mPreferences.getBoolean("notification_vibrate", true)) ? 1 : 0;
        int light = (mPreferences.getBoolean("notification_light", false)) ? 1 : 0;

        return makeSound | vibrate | light;
    }

    public Preferences getPreferences()
    {
        return mPreferences;
    }
}
