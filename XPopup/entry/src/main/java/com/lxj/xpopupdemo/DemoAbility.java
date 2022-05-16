package com.lxj.xpopupdemo;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.ToastUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;

/**
 * Description:
 * Create by lxj, at 2019/2/2
 */
public class DemoAbility extends Ability {

    TextField editText;
    Component content;
    BasePopupView attachPopup;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_demo);
        // 修改状态栏的颜色
        StatusBarUtils.setStatusBarColor(this, ElementUtil.getColor(this, ResourceTable.Color_colorBar));
        editText = (TextField) findComponentById(ResourceTable.Id_et);
        content = findComponentById(ResourceTable.Id_content);
        findComponentById(ResourceTable.Id_text).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showMultiPopup();
            }
        });
        showMultiPopup();
        attachPopup = new XPopup.Builder(DemoAbility.this)
                .atView(editText)
                .isRequestFocus(false) // 要设置这个，否则Ability内的输入框会无法获取焦点
                .hasShadowBg(false)
                .positionByWindowCenter(true)
                .isComponentMode(true, content)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asAttachList(new String[]{"联想到的内容 - 1", "联想到的内容 - 2", "联想到的内容 - 333"}, null, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        ToastUtil.showToast(DemoAbility.this, text);
                    }
                });
        editText.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String phoneNumber, int start, int before, int count) {
                if (phoneNumber.isEmpty()) {
                    attachPopup.dismiss();
                    return;
                }
                if (attachPopup.isDismiss()) {
                    attachPopup.show();
                }
            }
        });
    }

    /**
     * 多弹窗同时展示
     */
    private void showMultiPopup() {
        final BasePopupView loadingPopup = new XPopup.Builder(this).asLoading();
        loadingPopup.show();
        new XPopup.Builder(DemoAbility.this)
                .autoDismiss(false)
                .asBottomList("haha", new String[]{"点我显示弹窗0", "点我显示弹窗1", "点我显示弹窗2", "点我显示弹窗3"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        new XPopup.Builder(DemoAbility.this).asConfirm("测试", "aaaa" + position, new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                loadingPopup.dismiss();
                            }
                        }).show();
                    }
                }).show();
    }
}
