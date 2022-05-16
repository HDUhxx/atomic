package com.lxj.xpopupdemo.custom;


import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.ToastUtil;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentState;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.app.Context;

public class LoginPopup extends CenterPopupView {
    public LoginPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_popup_login;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Button btnCancle = findComponentById(ResourceTable.Id_btn_cancle);
        Button btnLogin = findComponentById(ResourceTable.Id_btn_login);
        TextField tdNumber = findComponentById(ResourceTable.Id_tf_number);
        TextField tdPassword = findComponentById(ResourceTable.Id_tf_password);
        btnCancle.setBackground(getBtnElement());
        btnLogin.setBackground(getBtnElement());

        btnCancle.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                ToastUtil.showToast(getContext(), "取消登录");
                dismiss();
            }
        });

        btnLogin.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (tdNumber.getText().isEmpty()) {
                    ToastUtil.showToast(getContext(), "帐号不能为空");
                } else if (tdPassword.getText().isEmpty()) {
                    ToastUtil.showToast(getContext(), "密码不能为空");
                } else {
                    ToastUtil.showToast(getContext(), "登录成功");
                    dismiss();
                }
            }
        });
    }

    private StateElement getBtnElement() {
        ShapeElement elementButtonOn = new ShapeElement();
        RgbColor rgbColorOn = new RgbColor(0xBDBDBDFF);
        elementButtonOn.setRgbColor(rgbColorOn);
        elementButtonOn.setShape(ShapeElement.RECTANGLE);
        elementButtonOn.setCornerRadius(10.0f);

        ShapeElement elementButtonOff = new ShapeElement();
        RgbColor rgbColorOff = new RgbColor(0xD9D9D9FF);
        elementButtonOff.setRgbColor(rgbColorOff);
        elementButtonOff.setShape(ShapeElement.RECTANGLE);
        elementButtonOff.setCornerRadius(10.0f);

        StateElement stateElement = new StateElement();
        stateElement.addState(new int[]{ComponentState.COMPONENT_STATE_PRESSED}, elementButtonOn);
        stateElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, elementButtonOff);
        return stateElement;
    }
}
