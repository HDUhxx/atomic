package com.jltf.jltf_idiom.controller;

import com.huawei.caas.messageservice.HwShareUtils;
import com.huawei.caas.messageservice.WebPageShareMsg;
import com.huawei.hms.accountsdk.exception.ApiException;
import com.huawei.hms.accountsdk.support.account.AccountAuthManager;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParams;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.huawei.hms.accountsdk.support.account.service.AccountAuthService;
import com.huawei.hms.accountsdk.support.account.tasks.OnFailureListener;
import com.huawei.hms.accountsdk.support.account.tasks.OnSuccessListener;
import com.huawei.hms.accountsdk.support.account.tasks.Task;
import com.jltf.jltf_idiom.MainAbility;
import com.jltf.jltf_idiom.ResourceTable;

import com.jltf.jltf_idiom.utils.PasteboardUtils;

import ohos.aafwk.ability.*;
import ohos.aafwk.content.Intent;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.global.resource.NotExistException;
import ohos.interwork.utils.PacMapEx;
import ohos.location.*;
import ohos.media.image.PixelMap;
import ohos.rpc.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.io.*;
import java.time.Instant;
import java.util.*;

public class ServiceAbility extends Ability {
    private static final HiLogLabel TAG = new HiLogLabel(3, 0xD001100, "蛟龙腾飞-ServiceAbility");

    MyRemote remote=new MyRemote();


    @Override
    public void onStart(Intent intent) {
        HiLog.error(TAG, "ServiceAbility::onStart");
        super.onStart(intent);



    }


    // FA在请求PA服务时会调用Ability.connectAbility连接PA，连接成功后，需要在onConnect返回一个remote对象，供FA向PA发送消息
    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }


    class MyRemote extends RemoteObject implements IRemoteBroker {
        private static final int SUCCESS = 0;
        private static final int COPYTEXT = 1013; // 复制
        private static final int PASTETEXT = 1014; // 粘贴


        MyRemote() {
            super("MyService_MyRemote");
        }

        @Override
            public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {

                String dataStr = data.readString();
                ZSONObject parms = ZSONObject.stringToZSON(dataStr);

                List idioms = new ArrayList();

                Map<String, Object> result = new HashMap<String, Object>();
                HiLog.info(TAG,"请求方："+code);
                HiLog.info(TAG,"请求方数据："+dataStr);
                switch (code) {
                    case COPYTEXT:
                        new PasteboardUtils(getContext()).copyText(parms.getString("text"));
                        break;
                    case PASTETEXT:
                        result.put("text",new PasteboardUtils(getContext()).pasteText() );
                        break;
                    default: {
                        result.put("abilityError", 0);
                        reply.writeString(ZSONObject.toZSONString(result));
                        return false;
                    }
                }
                result.put("code",code);
                HiLog.info(TAG,"后台数据："+result.toString());
                reply.writeString(ZSONObject.toZSONString(result));
                return true;
            }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }

}