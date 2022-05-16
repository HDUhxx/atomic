package com.jjoe64.graphview_os.base;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;

public abstract class BaseAbilitySlice extends AbilitySlice {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(getLayout());

        initView();
        initData();
    }

    protected abstract int getLayout();

    /**
     * 初始化Veiw
     */
    protected abstract void initView();

    /**
     * Data处理
     */
    protected abstract void initData();

    /**
     * 无传参
     *
     * @param action str
     */
    public void startAbilitySlice(String action) {
        Intent secondIntent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withAction(action)
                .build();
        secondIntent.setOperation(operation);
        startAbility(secondIntent);
    }

    /**
     * 有传参
     *
     * @param parameters str
     * @param action str
     */
    public void startAbilitySlice(String action, IntentParams parameters) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withAction(action)
                .build();
        intent.setOperation(operation);
        intent.setParams(parameters);
        startAbility(intent);
    }


    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
