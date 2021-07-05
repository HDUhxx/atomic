package com.jltf.jltfcardtemplate05.widget.widget;

import com.jltf.jltfcardtemplate05.widget.controller.FormController;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * Form controller implementation.
 */
public class WidgetImpl extends FormController {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WidgetImpl.class.getName());
    private static final int DEFAULT_DIMENSION_2X2 = 2;

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData() {
        HiLog.info(TAG, "bind form data");
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        if (dimension == DEFAULT_DIMENSION_2X2) {
            ZSONObject zsonObject = new ZSONObject();
            zsonObject.put("", "");
            providerFormInfo.setJsBindingData(new FormBindingData(zsonObject));
        }
        return providerFormInfo;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
        HiLog.info(TAG, "update form data: formId" + formId);
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "onTriggerFormEvent.");
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        HiLog.info(TAG, "set route page slice.");
        ZSONObject zsonObject = ZSONObject.stringToZSON(intent.getStringParam("params"));
        if (zsonObject.getString("message").equals("button")) {
            return WidgetSlice.class;
        }
        return null;
    }
}
