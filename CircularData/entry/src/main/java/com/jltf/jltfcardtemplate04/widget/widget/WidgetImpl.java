package com.jltf.jltfcardtemplate04.widget.widget;

import com.jltf.jltfcardtemplate04.widget.controller.FormController;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

/**
 * Form controller implementation.
 */
public class WidgetImpl extends FormController {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WidgetImpl.class.getName());

    private static final String TITLE_CONTENT = "titleContent";

    private static final String TEXT_CONTENT = "textContent";

    private static final String SEGMENTS = "segments";

    private static final int HUNDRED_PERCENT = 100;

    private static final int TOTAL_STORAGE_CAPACITY = 256;
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    private int storageUsedPercent = 100;

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData() {
        HiLog.info(TAG, "bind form data for a new service widget.");
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        if (dimension == DEFAULT_DIMENSION_2X2) {
            ZSONObject formData = calculateCurrentUsedStorage(storageUsedPercent);
            providerFormInfo.setJsBindingData(new FormBindingData(formData));
        }
        return providerFormInfo;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
        HiLog.info(TAG, "update form data: formId" + formId);
    }


    @Override
    public void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "handle trigger form event.");
        if (!(context instanceof Ability)) {
            return;
        }
        Ability ability = (Ability) context;
        HiLog.info(TAG, "onTriggerFormEvent() ability:" + ability + ", formId: " + formId + ", message: " + message);
        // Do something specific to this kind of form.
        storageUsedPercent = (--storageUsedPercent) < 0 ? HUNDRED_PERCENT : storageUsedPercent;
        ZSONObject formData = calculateCurrentUsedStorage(storageUsedPercent);
        try {
            ability.updateForm(formId, new FormBindingData(formData));
        } catch (FormException e) {
            HiLog.error(TAG, e.getMessage());
        }
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return null;
    }

    private ZSONObject calculateCurrentUsedStorage(int usedPercent) {
        int currentUsedStorage = TOTAL_STORAGE_CAPACITY * usedPercent / HUNDRED_PERCENT;
        ZSONArray zsonArray = new ZSONArray();
        ZSONObject zsonObject = new ZSONObject();
        zsonObject.put("name", "segment");
        zsonObject.put("value", usedPercent);
        zsonArray.add(zsonObject);

        ZSONObject newFormData = new ZSONObject();
        newFormData.put(TITLE_CONTENT, usedPercent + "%");
        newFormData.put(TEXT_CONTENT, currentUsedStorage + "GB" + "/" + TOTAL_STORAGE_CAPACITY + "GB");
        newFormData.put(SEGMENTS, zsonArray);

        return newFormData;
    }
}
