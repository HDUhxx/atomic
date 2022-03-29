package com.jltf.jltf_idiom;

import com.jltf.jltf_idiom.controller.ServiceAbility;
import com.jltf.jltf_idiom.widget.controller.*;
import ohos.aafwk.ability.*;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;
import com.jltf.jltf_idiom.widget.controller.FormController;
import com.jltf.jltf_idiom.widget.controller.FormControllerManager;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

public class MainAbility extends AceAbility {
    public static final int DEFAULT_DIMENSION_2X2 = 2;
    public static final int DIMENSION_1X2 = 1;
    public static final int DIMENSION_2X4 = 3;
    public static final int DIMENSION_4X4 = 4;
    private static final int INVALID_FORM_ID = -1;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, MainAbility.class.getName()+"蛟龙腾飞");
    private String topWidgetSlice;

    FormBindingData formBindingData;
    private final Uri uri = Uri.parse("dataability:///com.jltf.jltf_idiom.data.FormDataAbility");

    DataAbilityHelper helper= DataAbilityHelper.creator(this);

    @Override
    public void onStart(Intent intent) {

        //  判断是否是已经同意隐私协议 ，是 直接进入主页面 ， 否 进入隐私页面
        setInstanceName("default");

        setPageParams("pages/index/index",null);

        super.onStart(intent);
    }
    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        HiLog.info(TAG, "onCreateForm");
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        String formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        int dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
        HiLog.info(TAG, "onCreateForm: formId=" + formId + ",formName=" + formName);

        helper = DataAbilityHelper.creator(this);

        // 构造插入数据
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putLong("formId", formId);
        valuesBucket.putString("formName",formName);
        try {
            helper.insert(uri, valuesBucket);
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }

        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController = (formController == null) ? formControllerManager.createFormController(formId,
                formName, dimension) : formController;
        if (formController == null) {
            HiLog.error(TAG, "Get null controller. formId: " + formId + ", formName: " + formName);
            return null;
        }

        ZSONObject zsonObject = new ZSONObject();

        // 显示内容
//        ZSONObject zsonObject = getDataByFormName(formId,formName);
//        zsonObject.put("comment","加载中...");
//        zsonObject.put("comment1","加载中...");
//        zsonObject.put("comment2","加载中...");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                UpdataCommect(formId,zsonObject.getString("idiom"));
//            }
//        });


        ProviderFormInfo formInfo = new ProviderFormInfo();
        formInfo.setJsBindingData(new FormBindingData(zsonObject));

        return formInfo;

    }

    @Override
    protected void onUpdateForm(long formId) {
        HiLog.info(TAG, "onUpdateForm");
        super.onUpdateForm(formId);
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);

        FormController formController = formControllerManager.getController(formId);

        formController.onUpdateFormData(formId);
    }

    @Override
    protected void onDeleteForm(long formId) {
        HiLog.info(TAG, "onDeleteForm: formId=" + formId);
        super.onDeleteForm(formId);

        try {
            helper.delete(uri, new DataAbilityPredicates().equalTo("formId", formId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController.onDeleteForm(formId);
        formControllerManager.deleteFormController(formId);

    }

    @Override
    protected void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "onTriggerFormEvent: " + message);


        super.onTriggerFormEvent(formId, message);
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController.onTriggerFormEvent(formId, message);
    }

    @Override
    public void onNewIntent(Intent intent) {
        // Only response to it when starting from a service widget.
        if (intentFromWidget(intent)) {
            String newWidgetSlice = getRoutePageSlice(intent);
            if (topWidgetSlice == null || !topWidgetSlice.equals(newWidgetSlice)) {
                topWidgetSlice = newWidgetSlice;
                restart();
            }
        } else {
            if (topWidgetSlice != null) {
                topWidgetSlice = null;
                restart();
            }
        }
        if (intentFromWidget(intent)) {
            String newWidgetSlice = getRoutePageSlice(intent);
            if (topWidgetSlice == null || !topWidgetSlice.equals(newWidgetSlice)) {
                topWidgetSlice = newWidgetSlice;
                restart();
            }
        }
    }

    private boolean intentFromWidget(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        return formId != INVALID_FORM_ID;
    }

    private String getRoutePageSlice(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        if (formId == INVALID_FORM_ID) {
            return null;
        }
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        if (formController == null) {
            return null;
        }
        Class<? extends AbilitySlice> clazz = formController.getRoutePageSlice(intent);
        if (clazz == null) {
            return null;
        }
        return clazz.getName();
    }
}
