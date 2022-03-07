/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxy.recovery.core.slice;

import com.zxy.recovery.ResourceTable;
import com.zxy.recovery.core.DebugAbility;
import com.zxy.recovery.core.RecoveryAbility;
import com.zxy.recovery.core.RecoveryStore;
import com.zxy.recovery.tools.LogUtil;
import com.zxy.recovery.tools.RecoverySharedPrefsUtil;
import com.zxy.recovery.tools.RecoveryUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.os.ProcessManager;
import ohos.rpc.RemoteException;
import ohos.utils.IntentConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 恢复页面
 *
 * @author:wjt
 * @since 2021-04-06
 */
public class RecoveryAbilitySlice extends AbilitySlice {
    private static final int NUM10 = 10;
    private RecoveryStore.ExceptionData mExceptionData;
    private String mStackTrace;
    private String mCause;

    private Text textDebug;
    private Text mCrashTipsTv;
    private Button mRecoverBtn;
    private Button mRestartBtn;
    private Button mRestartClearBtn;

    private Intent mIntent;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setStatusBarColor(Color.getIntColor("#212121"));
        super.setUIContent(ResourceTable.Layout_recovery_activity_recover);
        mIntent = intent;

        initView();
        initData();
        setupEvent();
    }

    private void initView() {
        mCrashTipsTv = (Text) findComponentById(ResourceTable.Id_text_crash_tips);
        mRecoverBtn = (Button) findComponentById(ResourceTable.Id_btn_recover);
        mRestartBtn = (Button) findComponentById(ResourceTable.Id_btn_restart);
        mRestartClearBtn = (Button) findComponentById(ResourceTable.Id_btn_restart_clear);

        Image imageBack = (Image) findComponentById(ResourceTable.Id_image_title_bar_back);
        imageBack.setVisibility(Component.HIDE);
        textDebug = (Text) findComponentById(ResourceTable.Id_text_title_bar_right);
        imageBack.setClickedListener(component -> terminateAbility());
        textDebug.setClickedListener(component -> {
            Intent mIntent2 = new Intent();
            Operation operation2 = new Intent.OperationBuilder()
                    .withBundleName(getBundleName())
                    .withAbilityName(DebugAbility.class)
                    .build();
            mIntent2.setParam(RecoveryStore.EXCEPTION_DATA, mExceptionData);
            mIntent2.setParam(RecoveryStore.EXCEPTION_CAUSE, mCause);
            mIntent2.setParam(RecoveryStore.STACK_TRACE, mStackTrace);
            mIntent2.setOperation(operation2);
            startAbility(mIntent2);
        });
    }

    private void initData() {
        mCrashTipsTv.setText(String.format(getString(ResourceTable.String_recovery_crash_tips_msg),
                getString(ResourceTable.String_app_name)));
        boolean isDebugMode = isDebugMode();
        textDebug.setVisibility(isDebugMode ? Component.VISIBLE : Component.HIDE);
        mExceptionData = getExceptionData();
        mCause = getCause();
        mStackTrace = getStackTrace();
    }

    private void setupEvent() {
        mRecoverBtn.setClickedListener(component -> {
            boolean isRestart = RecoverySharedPrefsUtil.shouldRestartApp();
            if (isRestart) {
                RecoverySharedPrefsUtil.clear();
                restart();
                return;
            }
            if (isRecoverStack()) {
                recoverActivityStack();
            } else {
                recoverTopActivity();
            }
        });

        mRestartBtn.setClickedListener(component -> {
            boolean isRestart = RecoverySharedPrefsUtil.shouldRestartApp();
            if (isRestart) {
                RecoverySharedPrefsUtil.clear();
            }
            restart();
        });

        mRestartClearBtn.setClickedListener(component -> {
            CommonDialog commonDialog = new CommonDialog(this);
            commonDialog.setTransparent(true);
            Component dialogLayout = LayoutScatter.getInstance(this).
                    parse(ResourceTable.Layout_dialog_layout, null, false);
            Button btnCancel = (Button) dialogLayout.findComponentById(ResourceTable.Id_btn_dialog_layout_cancel);
            Button btnConfirm = (Button) dialogLayout.findComponentById(ResourceTable.Id_btn_dialog_layout_confirm);
            btnCancel.setClickedListener(component1 -> {
                commonDialog.destroy();
            });
            btnConfirm.setClickedListener(component1 -> {
                RecoveryUtil.clearApplicationData();
                restart();
            });

            commonDialog.setContentCustomComponent(dialogLayout);
            commonDialog.show();
        });
    }

    private boolean isDebugMode() {
        return mIntent.getBooleanParam(RecoveryStore.IS_DEBUG, false);
    }

    private RecoveryStore.ExceptionData getExceptionData() {
        return mIntent.getSequenceableParam(RecoveryStore.EXCEPTION_DATA);
    }

    private String getCause() {
        return mIntent.getStringParam(RecoveryStore.EXCEPTION_CAUSE);
    }

    private String getStackTrace() {
        return mIntent.getStringParam(RecoveryStore.STACK_TRACE);
    }

    /**
     * 重启应用
     */
    public void restart() {
        String className = "";
        try {
            BundleInfo bundleInfo = getBundleManager().getBundleInfo(this.getBundleName(),
                    IBundleManager.GET_ABILITY_INFO_WITH_PERMISSION);
            AbilityInfo abilityInfo = getBundleManager().getModuleMainAbility(getBundleName(),
                    bundleInfo.getModuleNames().get(0));
            className = abilityInfo.getClassName();
        } catch (RemoteException e) {
            LogUtil.error("RecoveryAbilitySlice","RemoteException");
        }

        Intent mainIntent = new Intent();
        Set<String> entities = new HashSet<>();
        entities.add(IntentConstants.ENTITY_HOME);
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(getBundleName())
                .withAbilityName(className)
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION)
                .withAction(IntentConstants.ACTION_HOME)
                .withEntities(entities)
                .build();
        mainIntent.setOperation(operation);
        startAbility(mainIntent);
        terminateAbility();
    }

    private void recoverTopActivity() {
        Intent intent = getRecoveryIntent();
        if (intent != null && RecoveryUtil.isIntentAvailable(this, intent)) {
            IntentParams intentParams = new IntentParams();
            intentParams.setClassLoader(getClassloader());
            intent.setParams(intentParams);
            intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION);
            intent.setParam(RecoveryAbility.RECOVERY_MODE_ACTIVE, true);
            startAbility(intent);
            setTransitionAnimation(0, 0);
            terminateAbility();
            return;
        }
        restart();
    }

    private boolean isRecoverStack() {
        boolean hasRecoverStack = mIntent.hasParameter(RecoveryStore.RECOVERY_STACK);
        return !hasRecoverStack || mIntent.getBooleanParam(RecoveryStore.RECOVERY_STACK, true);
    }

    private void recoverActivityStack() {
        ArrayList<Intent> intents = getRecoveryIntents();
        if (intents != null && !intents.isEmpty()) {
            ArrayList<Intent> availableIntents = new ArrayList<>();
            for (Intent tmp : intents) {
                if (tmp != null && RecoveryUtil.isIntentAvailable(this, tmp)) {
                    IntentParams intentParams = new IntentParams();
                    intentParams.setClassLoader(getClassloader());
                    tmp.setParams(intentParams);
                    availableIntents.add(tmp);
                }
            }
            if (!availableIntents.isEmpty()) {
                availableIntents.get(0).addFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION);
                availableIntents.get(availableIntents.size() - 1).setParam(RecoveryAbility.RECOVERY_MODE_ACTIVE, true);
                startAbilities(availableIntents.toArray(new Intent[availableIntents.size()]));
                setTransitionAnimation(0, 0);
                terminateAbility();
                return;
            }
        }
        restart();
    }

    private Intent getRecoveryIntent() {
        Intent recoveryIntent = null;
        boolean isHasRecoverIntent = mIntent.hasParameter(RecoveryStore.RECOVERY_INTENT);
        if (isHasRecoverIntent) {
            recoveryIntent = mIntent.getSequenceableParam(RecoveryStore.RECOVERY_INTENT);
        }
        return recoveryIntent;
    }

    private ArrayList<Intent> getRecoveryIntents() {
        ArrayList<Intent> arrayList = null;
        boolean isHasRecoveryIntents = mIntent.hasParameter(RecoveryStore.RECOVERY_INTENTS);
        if (isHasRecoveryIntents) {
            arrayList = mIntent.getSequenceableArrayListParam(RecoveryStore.RECOVERY_INTENTS);
        }
        return arrayList;
    }

    private void killProcess() {
        ProcessManager.kill(ProcessManager.getPid());
    }

    @Override
    protected void onStop() {
        super.onStop();
        terminateAbility();
    }

    @Override
    public void terminateAbility() {
        super.terminateAbility();
        killProcess();
    }
}
