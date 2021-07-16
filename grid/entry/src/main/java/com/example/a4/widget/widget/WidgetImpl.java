package com.example.a4.widget.widget;

import com.example.a4.ResourceTable;
import com.example.a4.widget.controller.FormController;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.HashMap;
import java.util.Map;

public class WidgetImpl extends FormController {
    public static final int DIMENSION_1X2 = 1;
    public static final int DIMENSION_2X4 = 3;
    public static final int DIMENSION_4X4 = 4;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WidgetImpl.class.getName());
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    private static final Map<Integer, Integer> RESOURCE_ID_MAP = new HashMap<>();

    static {
        RESOURCE_ID_MAP.put(DIMENSION_1X2, ResourceTable.Layout_form_grid_pattern_widget_1_2);
        RESOURCE_ID_MAP.put(DEFAULT_DIMENSION_2X2, ResourceTable.Layout_form_grid_pattern_widget_2_2);
        RESOURCE_ID_MAP.put(DIMENSION_2X4, ResourceTable.Layout_form_grid_pattern_widget_2_4);
        RESOURCE_ID_MAP.put(DIMENSION_4X4, ResourceTable.Layout_form_grid_pattern_widget_4_4);
    }

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData() {
        HiLog.info(TAG, "bind form data when create form");
        return new ProviderFormInfo(RESOURCE_ID_MAP.get(dimension), context);
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
        HiLog.info(TAG, "update form data timing, default 30 minutes");
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "handle card click event.");
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        HiLog.info(TAG, "get the default page to route when you click card.");
        return null;
    }
}