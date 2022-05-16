package com.lxj.xpopup.impl;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.LogUtil;
import com.lxj.xpopup.util.TextUtils;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * Description: 确定和取消的对话框
 * Create by dance, at 2018/12/16
 */
public class ConfirmPopupView extends CenterPopupView implements Component.ClickedListener {

    private static final String TAG = ConfirmPopupView.class.getName();
    OnCancelListener cancelListener;
    OnConfirmListener confirmListener;
    Text tv_title;
    Text tv_content;
    Text tv_cancel;
    Text tv_confirm;
    String title;
    String content;
    String hint;
    String cancelText;
    String confirmText;
    TextField et_input;
    Component divider1;
    Component divider2;
    public boolean isHideCancel = false;

    /**
     * @param context      上下文
     * @param bindLayoutId layoutId 要求布局中必须包含的Text以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     */
    public ConfirmPopupView(Context context, int bindLayoutId, PopupInfo popupInfo) {
        super(context, popupInfo);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : ResourceTable.Layout__xpopup_center_impl_confirm;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tv_title = (Text) findComponentById(ResourceTable.Id_tv_title);
        tv_content = (Text) findComponentById(ResourceTable.Id_tv_content);
        tv_cancel = (Text) findComponentById(ResourceTable.Id_tv_cancel);
        tv_confirm = (Text) findComponentById(ResourceTable.Id_tv_confirm);
        tv_content.setScrollable(true);
        et_input = (TextField) findComponentById(ResourceTable.Id_et_input);
        divider1 = findComponentById(ResourceTable.Id_xpopup_divider1);
        divider2 = findComponentById(ResourceTable.Id_xpopup_divider2);

        tv_cancel.setClickedListener(this);
        tv_confirm.setClickedListener(this);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(HIDE);
        }

        if (!TextUtils.isEmpty(content)) {
            tv_content.setText(content);
        } else {
            tv_content.setVisibility(HIDE);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel.setText(cancelText);
        }
        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm.setText(confirmText);
        }
        if (isHideCancel) {
            tv_cancel.setVisibility(HIDE);
            if (divider2 != null) {
                divider2.setVisibility(HIDE);
            }
        }
        applyTheme();
    }

    protected void applyLightTheme() {
        super.applyLightTheme();
        try {
            tv_title.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_content_color)));
            tv_content.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_content_color)));
            tv_cancel.setTextColor(new Color(0xff666666));
            tv_confirm.setTextColor(new Color(XPopup.getPrimaryColor()));
            if (divider1 != null) {
                divider1.setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_divider)));
            }
            if (divider2 != null) {
                divider2.setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_divider)));
            }
        } catch (Exception e) {
            LogUtil.error(TAG, e.getMessage());
        }
    }

    public Text getTitleTextView() {
        return (Text) findComponentById(ResourceTable.Id_tv_title);
    }

    public Text getContentTextView() {
        return (Text) findComponentById(ResourceTable.Id_tv_content);
    }

    public Text getCancelTextView() {
        return (Text) findComponentById(ResourceTable.Id_tv_cancel);
    }

    public Text getConfirmTextView() {
        return (Text) findComponentById(ResourceTable.Id_tv_confirm);
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        try {
            tv_title.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
            tv_content.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
            tv_cancel.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
            tv_confirm.setTextColor(new Color(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
            if (divider1 != null) {
                divider1.setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_dark_divider)));
            }
            if (divider2 != null) {
                divider2.setBackground(ElementUtil.getShapeElement(ElementUtil.getColor(getContext(), ResourceTable.Color__xpopup_list_dark_divider)));
            }
        } catch (Exception e) {
            LogUtil.error(TAG, e.getMessage());
        }
    }

    public ConfirmPopupView setListener(OnConfirmListener confirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
        return this;
    }

    public ConfirmPopupView setTitleContent(String title, String content, String hint) {
        this.title = title;
        this.content = content;
        this.hint = hint;
        return this;
    }

    public ConfirmPopupView setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public ConfirmPopupView setConfirmText(String confirmText) {
        this.confirmText = confirmText;
        return this;
    }

    @Override
    public void onClick(Component component) {
        if (component == tv_cancel) {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        } else if (component == tv_confirm) {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            if (popupInfo.autoDismiss) {
                dismiss();
            }
        }
    }
}
