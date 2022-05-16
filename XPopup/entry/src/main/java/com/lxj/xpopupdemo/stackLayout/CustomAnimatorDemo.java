package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.app.Context;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class CustomAnimatorDemo extends BaseStackLayout implements Component.ClickedListener {

    public CustomAnimatorDemo(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return ResourceTable.Layout_stacklayout_custom_animator_demo;
    }

    @Override
    public void init(Component component) {
        component.findComponentById(ResourceTable.Id_btn_show).setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        new XPopup.Builder(getContext())
                .customAnimator(new RotateAnimator())
                .asConfirm("演示自定义动画", "当前的动画是一个自定义的旋转动画，无论是自定义弹窗还是自定义动画，已经被设计得非常简单；这个动画代码只有6行即可完成！", null)
                .show();
    }

    static class RotateAnimator extends PopupAnimator {
        @Override
        public void initAnimator() {
            targetView.setScaleX(0.0f);
            targetView.setScaleY(0.0f);
            targetView.setAlpha(0.0f);
            targetView.setRotation(360.0f);
        }

        @Override
        public void animateShow() {
            targetView.createAnimatorProperty().rotate(0.0f).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(340)
                    .start();
        }

        @Override
        public void animateDismiss() {
            targetView.createAnimatorProperty().rotate(720.0f).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(340)
                    .start();
        }
    }

}
