package com.example.radiostate_dome;

import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.telephony.NetworkState;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.RadioStateObserver;
import ohos.telephony.SignalInformation;

import java.util.List;

public class MainAbility extends AceAbility {

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP,0x0,"网络");
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        // 获取RadioInfoManager对象。
        RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(getContext());

        // 执行回调的runner。
        EventRunner runner = EventRunner.create();

// 创建MyRadioStateObserver的对象。
        MyRadioStateObserver observer = new MyRadioStateObserver(1, runner);

// 添加回调，以NETWORK_STATE和SIGNAL_INFO为例。
        radioInfoManager.addObserver(observer, RadioStateObserver.OBSERVE_MASK_NETWORK_STATE | RadioStateObserver.OBSERVE_MASK_SIGNAL_INFO);
    }

    // 创建继承RadioStateObserver的类MyRadioStateObserver
    class MyRadioStateObserver extends RadioStateObserver {


        // 构造方法，在当前线程的runner中执行回调，slotId需要传入要观察的卡槽ID（0或1）。
        MyRadioStateObserver(int slotId) {
            super(slotId);
        }

        // 构造方法，在执行runner中执行回调。
        MyRadioStateObserver(int slotId, EventRunner runner) {
            super(slotId, runner);
        }

        // 网络注册状态变化的回调方法。
        @Override
        public void onNetworkStateUpdated(NetworkState state) {
            HiLog.info(TAG,"网络注册状态变化："+state.toString());
        }

        // 信号信息变化的回调方法。
        @Override
        public void onSignalInfoUpdated(List<SignalInformation> signalInfos) {
            HiLog.info(TAG,"信号信息变化："+signalInfos.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
