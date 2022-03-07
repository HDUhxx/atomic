package com.zxy.recovery.core;

import com.zxy.recovery.tools.LogUtil;
import com.zxy.recovery.tools.RecoverySilentSharedPrefsUtil;
import com.zxy.recovery.tools.RecoveryUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityPackage;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.event.notification.NotificationRequest;
import ohos.rpc.RemoteException;
import ohos.utils.IntentConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.zxy.recovery.core.RecoveryAbility.RECOVERY_MODE_ACTIVE;

/**
 * 服务
 *
 * @author:wjt
 * @since 2021-04-06
 */
public class RecoveryService extends Ability {
    static final String RECOVERY_SILENT_MODE_VALUE = "recovery_silent_mode_value";
    private static final int NUM1005 = 1005;
    private static final int NUM10 = 10;
    private static final int NUMF1 = -1;
    /**
     * 构造
     */
    public RecoveryService() {
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        NotificationRequest request = new NotificationRequest(NUM1005);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle("title").setText("Recovery");
        NotificationRequest.NotificationContent notificationContent =
                new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);
        keepBackgroundRunning(NUM1005, request);
    }

    private void stopSelf2() {
        terminateAbility();
    }

    @Override
    protected void onCommand(Intent intent, boolean restart, int startId) {
        super.onCommand(intent, restart, startId);

        if (RecoverySilentSharedPrefsUtil.shouldClearAppNotRestart()) {
            RecoverySilentSharedPrefsUtil.clear();
            terminateAbility();
        }
        Recovery.SilentMode mode = getRecoverySilentMode(intent);
        if (mode == Recovery.SilentMode.RESTART) {
            restart();
        } else if (mode == Recovery.SilentMode.RECOVER_ACTIVITY_STACK) {
            recoverActivityStack(intent);
        } else if (mode == Recovery.SilentMode.RECOVER_TOP_ACTIVITY) {
            recoverTopActivity(intent);
        } else if (mode == Recovery.SilentMode.RESTART_AND_CLEAR) {
            restartAndClear();
        } else {
            stopSelf2();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 停止此程序
     */
    private void killProcess() {
        getAbilityManager().killProcessesByBundleName("com.zxy.recovery.test");
    }

    private Recovery.SilentMode getRecoverySilentMode(Intent intent) {
        int value = intent.getIntParam(RECOVERY_SILENT_MODE_VALUE, NUMF1);
        return Recovery.SilentMode.getMode(value);
    }

    //程序栈
    private void recoverActivityStack(Intent o) {
        ArrayList<Intent> intents = getRecoveryIntents(o);
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
                availableIntents.get(availableIntents.size() - 1).setParam(RECOVERY_MODE_ACTIVE, true);
                for (Intent intent : availableIntents) {
                    intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_INSTALL_WITH_BACKGROUND_MODE);
                    setTransitionAnimation(0, 0);
                    startAbility(intent);
                }
                stopSelf2();
                return;
            }
        }
        restart();
    }

    private ArrayList<Intent> getRecoveryIntents(Intent intent) {
        ArrayList<Intent> arrayList = null;
        boolean hasRecoveryIntents = intent.hasParameter(RecoveryStore.RECOVERY_INTENTS);
        if (hasRecoveryIntents) {
            arrayList = intent.getSequenceableArrayListParam(RecoveryStore.RECOVERY_INTENTS);
        }
        return arrayList;
    }

    /**
     * 恢复最上层的ability
     *
     * @param o
     */
    private void recoverTopActivity(Intent o) {
        Intent intent = getRecoveryIntent(o);
        if (intent != null && RecoveryUtil.isIntentAvailable(this, intent)) {
            IntentParams intentParams = new IntentParams();
            intentParams.setClassLoader(getClassloader());
            intent.setParams(intentParams);
            intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION);
            intent.setParam(RECOVERY_MODE_ACTIVE, true);
            startAbility(intent);
            stopSelf2();
            return;
        }
        restart();
    }

    private Intent getRecoveryIntent(Intent intent) {
        Intent intent1 = null;
        boolean hasRecoverIntent = intent.hasParameter(RecoveryStore.RECOVERY_INTENT);
        if (hasRecoverIntent) {
            intent1 = intent.getSequenceableParam(RecoveryStore.RECOVERY_INTENT);
        }
        return intent1;
    }

    private void restartAndClear() {
        RecoveryUtil.clearApplicationData();
        restart();
    }

    /**
     * 启动ability
     *
     * @param context
     * @param intent
     */
    public static void start(Context context, Intent intent) {
        if (context instanceof AbilityPackage) {
            AbilityPackage ability = (AbilityPackage) context;
            ability.startAbility(intent, 0);
        }
    }

    /**
     * 重启
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
            LogUtil.error("RecoveryService","RemoteException");
        }

        Intent mIntent = new Intent();
        Set<String> entities = new HashSet<>();
        entities.add(IntentConstants.ENTITY_HOME);
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(getBundleName())
                .withAbilityName(className)
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION)
                .withAction(IntentConstants.ACTION_HOME)
                .withEntities(entities)
                .build();
        mIntent.setOperation(operation);
        startAbility(mIntent);
    }
}
