package com.example.splash_screen_ability.slice;

import com.example.splash_screen_ability.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.bundle.AbilityInfo;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.global.configuration.Configuration;

public class MainAbilitySlice extends AbilitySlice {
    private static final int EVENT_TIMER = 1;
    private static final int COUNT_DOWN_TIME = 100;
    private static final int COUNT_DOWN_PERIOD = 1000;

    private Text skipCount;
    private DirectionalLayout skipButtonTouchTarget;
    private Intent redirectIntent;
    private CountDownHandler handler;
    private DirectionalLayout bottomBar;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
//        显示广告窗口
        setUIContent(ResourceTable.Layout_splash_screen_layout);
//        获取环境配置：正向，横屏
        int orientation = getResourceManager().getConfiguration().direction;

        skipCount = (Text) findComponentById(ResourceTable.Id_skip_button_text2);
        skipButtonTouchTarget = (DirectionalLayout) findComponentById(ResourceTable.Id_skip_button_touchTarget);
        bottomBar = (DirectionalLayout) findComponentById(ResourceTable.Id_bottom_bar);
        if (orientation == Configuration.DIRECTION_HORIZONTAL) {
            bottomBar.setVisibility(Component.HIDE);
        }
        initRedirectIntent();
        initRedirection();
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        if (displayOrientation == AbilityInfo.DisplayOrientation.LANDSCAPE) {
            bottomBar.setVisibility(Component.HIDE);
        } else {
            bottomBar.setVisibility(Component.VISIBLE);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
        handler.sendEvent(EVENT_TIMER, 0);
    }

    @Override
    protected void onBackground() {
        super.onBackground();
        if (handler != null) {
            handler.removeAllEvent();
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (handler != null) {
            handler.removeAllEvent();
        }
    }

    private void initRedirectIntent() {
        redirectIntent = new Intent();

        // Set redirect destiny ability
        // Put the ability you want to show after the splash screen here
//        Operation公共接口操作,封装与Intent相关的参数和操作。
        Operation operation = new Intent.OperationBuilder().withDeviceId("")
//                输入bundle的Name，通过getBundleName()获取
                .withBundleName(getBundleName())
//                设置此OperationBuilder的abilityName属性。
                .withAbilityName("com.example.splash_screen_ability.SplashScreenAbility")
                .build();
        redirectIntent.setOperation(operation);
    }

    private void initRedirection() {
        // Set skip button click listener
        if (skipButtonTouchTarget != null) {
            skipButtonTouchTarget.setClickedListener(component -> {
                startAbility(redirectIntent);
                terminate();
//                线程如果不为空，要删除
                if (handler != null) {
                    handler.removeAllEvent();
                }
            });
        }
        // Set up count down event handler
        handler = new CountDownHandler(EventRunner.current());
    }

    /**
     * Countdown handler
     */
    private class CountDownHandler extends EventHandler {
        private int countDown = COUNT_DOWN_TIME;

        CountDownHandler(EventRunner runner) {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event.eventId == EVENT_TIMER) {
                skipCount.setText(" " + countDown);
                countDown--;
                if (countDown >= 0) {
                    handler.sendEvent(EVENT_TIMER, COUNT_DOWN_PERIOD);
                } else {
                    startAbility(redirectIntent);
                    terminate();
                }
            }
        }
    }
}
