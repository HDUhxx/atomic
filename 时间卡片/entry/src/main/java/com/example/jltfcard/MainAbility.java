package com.example.jltfcard;

import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;
import ohos.utils.zson.ZSONObject;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.ability.AbilitySlice;

public class MainAbility extends AceAbility {
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    private static final int DIMENSION_1X2 = 1;
    private static final int DIMENSION_4X4 = 4;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        ZSONObject zsonObject = new ZSONObject();
        String formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        int dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
        if (formName.equals("form_card")) {
            if (dimension == DIMENSION_1X2) {
                zsonObject.put("mini", true);
            } else if (dimension == DIMENSION_4X4) {
                zsonObject.put("imagePaddingTop", "12px");
            }
        }
        ProviderFormInfo formInfo = new ProviderFormInfo();
        formInfo.setJsBindingData(new FormBindingData(zsonObject));
        return formInfo;
    }

    @Override
    protected void onUpdateForm(long formId) {
        super.onUpdateForm(formId);
    }

    @Override
    protected void onDeleteForm(long formId) {
        super.onDeleteForm(formId);
    }

    @Override
    protected void onTriggerFormEvent(long formId, String message) {
        super.onTriggerFormEvent(formId, message);
    }
}
