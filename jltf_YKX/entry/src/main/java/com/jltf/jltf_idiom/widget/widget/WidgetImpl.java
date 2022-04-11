package com.jltf.jltf_idiom.widget.widget;

import com.jltf.jltf_idiom.widget.controller.FormController;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.nfc.tag.TagInfo;
import ohos.utils.zson.ZSONObject;

public class WidgetImpl extends FormController {

    private static final HiLogLabel TAG = new HiLogLabel(3, 0xD001100, "蛟龙腾飞-WidgetImpl");

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData(long formId) {
        return null;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
        ZSONObject zsonObject = ZSONObject.stringToZSON(message);
        HiLog.info(TAG,"卡片item："+zsonObject.getString("mAction"));
        Item(zsonObject.getString("mAction"));

    }

    //    使用统一service刷新卡片
    private void Item(String item){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName("com.jltf.idiom")
                .withAbilityName("com.jltf.jltf_idiom.service.ServiceAbility")
                .build();
        intent.setOperation(operation);
        intent.setParam("item",item);
        // 启动Service
        context.startAbility(intent,0);
    }



    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return null;
    }
}
