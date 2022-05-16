package com.lxj.xpopup.impl;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.util.TextUtils;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * Description: 带输入框，确定和取消的对话框
 * Create by dance, at 2018/12/16
 */
public class InputConfirmPopupView extends ConfirmPopupView implements Component.ClickedListener {

    /**
     * @param context      上下文
     * @param bindLayoutId 在Confirm弹窗基础上需要增加一个id为et_input的TextField
     */
    public InputConfirmPopupView(Context context, int bindLayoutId, PopupInfo popupInfo) {
        super(context, bindLayoutId, popupInfo);
    }

    public String inputContent;

    @Override
    protected void onCreate() {
        super.onCreate();
        et_input.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(hint)) {
            et_input.setHint(hint);
        }
        if (!TextUtils.isEmpty(inputContent)) {
            et_input.setText(inputContent);
        }

        XPopupUtils.setCursorDrawableColor(et_input, XPopup.getPrimaryColor());
        et_input.setLayoutRefreshedListener(new LayoutRefreshedListener() {
            @Override
            public void onRefreshed(Component component) {
                PixelMapElement defaultDrawable = XPopupUtils.createBitmapDrawable(et_input.getWidth(), 0x888888FF);
                PixelMapElement focusDrawable = XPopupUtils.createBitmapDrawable(et_input.getWidth(), XPopup.getPrimaryColor());
                et_input.setBasement(XPopupUtils.createSelector(defaultDrawable, focusDrawable));
            }
        });
    }

    public TextField getEditText() {
        return et_input;
    }

    protected void applyLightTheme() {
        super.applyLightTheme();
        et_input.setHintColor(new Color(0xff888888));
        et_input.setTextColor(new Color(0xff333333));
    }

    protected void applyDarkTheme() {
        super.applyDarkTheme();
        et_input.setHintColor(new Color(0xff888888));
        et_input.setTextColor(new Color(0xffdddddd));
    }

    OnCancelListener cancelListener;
    OnInputConfirmListener inputConfirmListener;

    public void setListener(OnInputConfirmListener inputConfirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.inputConfirmListener = inputConfirmListener;
    }

    @Override
    public void onClick(Component component) {
        if (component == tv_cancel) {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        } else if (component == tv_confirm) {
            if (inputConfirmListener != null) {
                inputConfirmListener.onConfirm(et_input.getText().toString().trim());
            }
            if (popupInfo.autoDismiss) {
                dismiss();
            }
        }
    }
}
