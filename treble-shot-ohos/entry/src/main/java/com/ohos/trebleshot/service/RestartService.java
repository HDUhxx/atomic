package com.ohos.trebleshot.service;

import com.ohos.trebleshot.utils.DynamicNotification;
import com.ohos.trebleshot.utils.InterruptAwareJob;
import com.ohos.trebleshot.utils.Interrupter;
import com.ohos.trebleshot.utils.NotificationUtils;
import me.panavtec.title.MainAbility;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.rpc.IRemoteObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ohos.trebleshot.utils.NotificationUtils.getNotificationUtilsInst;

/**
 * created by: Veli
 * date: 26.01.2018 14:21
 */

public class RestartService extends Ability {
    public static final String TAG = RestartService.class.getSimpleName();

    private static RestartService self;

    public static RestartService getSelf() {
        return self;
    }

    public static void setSelf(RestartService self) {
        RestartService.self = self;
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return super.onConnect(intent);
    }

    @Override
    public void onDisconnect(Intent intent) {
        super.onDisconnect(intent);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setSelf(this);
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
        super.onCommand(intent, restart, startId);
        try {
            Thread.sleep(2000);
            HMUtils.startAbility(this, MainAbility.class.getName(), "");
            terminateAbility();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}