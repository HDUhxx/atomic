package com.jltf.ai_tts.slice;

import com.jltf.ai_tts.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;

import ohos.ai.tts.TtsClient; // TTS接口
import ohos.ai.tts.TtsListener; // TTS回调
import ohos.ai.tts.TtsParams; // TTS参数
import ohos.ai.tts.constants.TtsEvent; // TTS事件
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.PacMap; // TTS依赖

import java.util.UUID;
import java.util.logging.Logger;

public class MainAbilitySlice extends AbilitySlice {

    private final static HiLogLabel TAG  = new HiLogLabel(HiLog.LOG_APP,0x0,"语音播报");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        TtsClient.getInstance().create(MainAbilitySlice.this, ttsListener);

        TtsParams ttsParams = new TtsParams();
        ttsParams.setDeviceId(String.valueOf(UUID.randomUUID()));  //设置设备ID
        ttsParams.setSpeed(6);  //设置语速
        ttsParams.setPitch(6);  //设置音调
        ttsParams.setSpeaker(0);  //设置音色 0 xiaoyi ，1 yoyo
        ttsParams.setVolume(8);   //设置音量

        findComponentById(ResourceTable.Id_bfBtn).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

                TaskDispatcher globalTaskDispatcher = getGlobalTaskDispatcher(TaskPriority.DEFAULT);
                globalTaskDispatcher.syncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        if (TtsClient.getInstance().init(ttsParams)) {
                            TtsClient.getInstance().speakText("宠辱 不惊，闲 看庭 前花开花落；去留 无意，漫随 天外 云卷 云舒！", null);
                        }
                    }
                });

            }
        });

        findComponentById(ResourceTable.Id_tzBtn).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(TtsClient.getInstance().isSpeaking()){
                    TtsClient.getInstance().stopSpeak();
                }
            }
        });
    }

    private static final TtsListener ttsListener = new TtsListener() {
        @Override
        public void onEvent(int eventType, PacMap pacMap) {
             HiLog.info(TAG,"onEvent:" + eventType);
            if (eventType == TtsEvent.CREATE_TTS_CLIENT_SUCCESS) {
                HiLog.info(TAG,"TTS Client create success");
            }
        }

        @Override
        public void onStart(String utteranceId) {
             HiLog.info(TAG,utteranceId + " audio synthesis begins");
        }

        @Override
        public void onProgress(String utteranceId, byte[] audioData, int progress) {
            HiLog.info(TAG,utteranceId + " audio synthesis progress：" + progress);
        }

        @Override
        public void onFinish(String utteranceId) {
            HiLog.info(TAG,utteranceId + " audio synthesis completed");
        }

        @Override
        public void onSpeechStart(String utteranceId) {
            HiLog.info(TAG,utteranceId + " begins to speech");
        }

        @Override
        public void onSpeechProgressChanged(String utteranceId, int progress) {
            HiLog.info(TAG,utteranceId + " speech progress：" + progress);
        }

        @Override
        public void onSpeechFinish(String utteranceId) {
            HiLog.info(TAG,utteranceId + " speech completed");
        }

        @Override
        public void onError(String utteranceId, String errorMessage) {
            HiLog.info(TAG,utteranceId + " errorMessage: " + errorMessage);
        }
    };


    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        TtsClient.getInstance().destroy();
    }
}
