package com.example.notificationslot_dome.slice;

import com.example.notificationslot_dome.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.utils.Color;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public class MainAbilitySlice extends AbilitySlice {
    private static final Object LABEL = new HiLogLabel(HiLog.LOG_APP,0x0,"蛟龙腾飞-通知");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        NotificationSlot();
    }

    private void NotificationSlot(){
        NotificationSlot slot = new NotificationSlot("slot_001", "slot_default", NotificationSlot.LEVEL_MIN); // 创建notificationSlot对象
        slot.setDescription("NotificationSlotDescription");
        slot.setEnableVibration(true); // 设置振动提醒
        slot.setEnableLight(true); // 设置开启呼吸灯提醒
        slot.setLedLightColor(Color.RED.getValue());// 设置呼吸灯的提醒颜色
        try {
            NotificationHelper.addNotificationSlot(slot);
        } catch (RemoteException ex) {
            HiLog.error((HiLogLabel) LABEL, "Exception occurred during addNotificationSlot invocation.");
        }

        int notificationId = 1;
        NotificationRequest request = new NotificationRequest(notificationId);
        request.setSlotId(slot.getId());

        String title = "消息";
        String text = "哈哈哈哈哈哈哈哈";
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle(title)
                .setText(text);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent); // 设置通知的内容

        try {
            NotificationHelper.publishNotification(request);
        } catch (RemoteException ex) {
            HiLog.error((HiLogLabel) LABEL, "Exception occurred during publishNotification invocation.");
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
