package com.example.myapplication.widget.jltfcard;

import com.example.myapplication.widget.controller.FormController;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * The JLTFCardImpl
 */
public class JLTFCardImpl extends FormController {

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, JLTFCardImpl.class.getName());


    private final Context mContext;

    public JLTFCardImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
        mContext = context;
    }

    @Override
    public ProviderFormInfo bindFormData() {
        return null;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
        ZSONObject zsonObject = ZSONObject.stringToZSON(message);

        // Do something here after receive the message from js card
        ZSONObject result = new ZSONObject();
        switch (zsonObject.getString("mAction")) {
            case "JLTFPrevious":
                result.put("itemTitle", "上一首歌曲");
                result.put("src", "/common/previous.jpg");
                break;
            case "JLTFPlay":
                result.put("itemTitle", "播放歌曲");
                result.put("src", "/common/bg.jpg");
                break;
            case "JLTFNext":
                result.put("itemTitle", "下一首歌曲");
                result.put("src", "/common/next.jpg");
                break;
            default:
                break;
        }

        // Update js card
        try {
            if (mContext instanceof Ability) {
                ((Ability) mContext).updateForm(formId, new FormBindingData(result));
            }
        } catch (FormException e) {
            HiLog.error(TAG, e.getMessage());
        }
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return null;
    }
}
