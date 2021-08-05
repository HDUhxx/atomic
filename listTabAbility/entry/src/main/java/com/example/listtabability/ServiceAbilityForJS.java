package com.example.listtabability;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteObject;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ServiceAbilityForJS
 */
public class ServiceAbilityForJS extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    // return a handler to communication
    private final MyRemote remote = new MyRemote();

    /**
     * get system color mode
     *
     * @param context context
     * @return system color mode
     */
    public static int getColorMode(Context context) {
        return context.getResourceManager().getConfiguration().getSystemColorMode();
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "ServiceAbilityForJS::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "ServiceAbilityForJS::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "ServiceAbilityForJS::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean isRestart, int startId) {
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    /**
     * MyRemote
     */
    class MyRemote extends RemoteObject implements IRemoteBroker {
        private static final int SUCCESS = 0;
        private static final int MESSAGE_CODE = 1001;

        MyRemote() {
            super("MyService_MyRemote");
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            switch (code) {
                case MESSAGE_CODE: {
                    String zsonStr = data.readString();
                    RequestParam param = new RequestParam();
                    try {
                        param = ZSONObject.stringToClass(zsonStr, RequestParam.class);
                    } catch (NullPointerException e) {
                        HiLog.error(LABEL_LOG, "convert failed.");
                    }

                    // only support serializable data object
                    Map<String, Object> zsonResult = new HashMap<String, Object>();
                    zsonResult.put("code", SUCCESS);
                    zsonResult.put("abilityResult", param.getFirstNum() + param.getSecondNum());
                    zsonResult.put("getColorMode", getColorMode(ServiceAbilityForJS.this));
                    reply.writeString(ZSONObject.toZSONString(zsonResult));
                    break;
                }
                default: {
                    Map<String, Object> zsonResult = new HashMap<String, Object>();
                    zsonResult.put("abilityError", "ERROR");
                    reply.writeString(ZSONObject.toZSONString(zsonResult));
                    return false;
                }
            }
            return true;
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
}

/**
 * RequestParam
 */
class RequestParam {
    private int firstNum;
    private int secondNum;

    public int getFirstNum() {
        return firstNum;
    }

    public int getSecondNum() {
        return secondNum;
    }

}