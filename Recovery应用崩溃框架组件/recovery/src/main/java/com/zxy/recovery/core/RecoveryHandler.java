package com.zxy.recovery.core;

import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.tools.DefaultHandlerUtil;
import com.zxy.recovery.tools.LogUtil;
import com.zxy.recovery.tools.RecoverySharedPrefsUtil;
import com.zxy.recovery.tools.RecoverySilentSharedPrefsUtil;
import com.zxy.recovery.tools.RecoveryUtil;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.os.ProcessManager;
import ohos.rpc.RemoteException;
import ohos.utils.IntentConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * RecoveryHandler
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
final class RecoveryHandler implements Thread.UncaughtExceptionHandler {
    private static final int NUM10 = 10;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private RecoveryCallback mCallback;

    private RecoveryStore.ExceptionData mExceptionData;

    private String mStackTrace;

    private String mCause;

    private RecoveryHandler(Thread.UncaughtExceptionHandler defHandler) {
        mDefaultUncaughtExceptionHandler = defHandler;
    }

    static RecoveryHandler newInstance(Thread.UncaughtExceptionHandler defHandler) {
        return new RecoveryHandler(defHandler);
    }

    @Override
    public synchronized void uncaughtException(Thread t, Throwable e) {
        if (Recovery.getInstance().isRecoverEnabled()) {
            if (Recovery.getInstance().isSilentEnabled()) {
                RecoverySilentSharedPrefsUtil.recordCrashData();
            } else {
                RecoverySharedPrefsUtil.recordCrashData();
            }
        }
        printWrite(e);
    }

    private void printWrite(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        String stackTrace = sw.toString();
        String cause = e.getMessage();
        Throwable rootTr = e;
        while (e.getCause() != null) {
            e = e.getCause();
            if (e.getStackTrace() != null && e.getStackTrace().length > 0) {
                rootTr = e;
            }
            String msg = e.getMessage();
            if (!(msg == null || msg.length() == 0)) {
                cause = msg;
            }
        }
        String exceptionType = rootTr.getClass().getName();
        String throwClassName;
        String throwMethodName;
        int throwLineNumber;
        if (rootTr.getStackTrace().length > 0) {
            StackTraceElement trace = rootTr.getStackTrace()[0];
            throwClassName = trace.getClassName();
            throwMethodName = trace.getMethodName();
            throwLineNumber = trace.getLineNumber();
        } else {
            throwClassName = "unknown";
            throwMethodName = "unknown";
            throwLineNumber = 0;
        }
        mExceptionData = RecoveryStore.ExceptionData.newInstance()
                .type(exceptionType)
                .className(throwClassName)
                .methodName(throwMethodName)
                .lineNumber(throwLineNumber);
        mStackTrace = stackTrace;
        mCause = cause;
        if (mCallback != null) {
            mCallback.stackTrace(stackTrace);
            mCallback.cause(cause);
            mCallback.exception(exceptionType, throwClassName, throwMethodName, throwLineNumber);
            mCallback.throwable(e);
        }

        if (!DefaultHandlerUtil.isSystemDefaultUncaughtExceptionHandler(mDefaultUncaughtExceptionHandler)) {
            if (mDefaultUncaughtExceptionHandler == null) {
                killProcess();
                return;
            }

            recover();
        } else {
            recover();
            killProcess();
        }
    }

    RecoveryHandler setCallback(RecoveryCallback callback) {
        mCallback = callback;
        return this;
    }

    private void recover() {
        if (!Recovery.getInstance().isRecoverEnabled()) {
            return;
        }
        if (RecoveryUtil.isAppInBackground(Recovery.getInstance().getContext())
                && !Recovery.getInstance().isRecoverInBackground()) {
            killProcess();
            return;
        }

        if (Recovery.getInstance().isSilentEnabled()) {
            startRecoverService();
        } else {
            startRecoverAbility();
        }
    }

    private void startRecoverAbility() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(Recovery.getInstance().getContext().getBundleName())
                .withAbilityName(RecoveryAbility.class)
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION)
                .build();
        intent.setOperation(operation);
        if (RecoveryStore.getInstance().getIntent() != null) {
            intent.setParam(RecoveryStore.RECOVERY_INTENT, RecoveryStore.getInstance().getIntent());
        }
        if (!RecoveryStore.getInstance().getIntents().isEmpty()) {
            intent.setSequenceableArrayListParam(RecoveryStore.RECOVERY_INTENTS,
                    RecoveryStore.getInstance().getIntents());
        }
        intent.setParam(RecoveryStore.RECOVERY_STACK, Recovery.getInstance().isRecoverStack());
        intent.setParam(RecoveryStore.IS_DEBUG, Recovery.getInstance().isDebug());
        if (mExceptionData != null) {
            intent.setParam(RecoveryStore.EXCEPTION_DATA, mExceptionData);
        }
        intent.setParam(RecoveryStore.STACK_TRACE, String.valueOf(mStackTrace));
        intent.setParam(RecoveryStore.EXCEPTION_CAUSE, String.valueOf(mCause));
        Recovery.getInstance().getContext().startAbility(intent, 0);
    }

    /**
     * 启动服务
     */
    private void startRecoverService() {
        restart();
        Intent intent = new Intent();
        if (RecoveryStore.getInstance().getIntent() != null) {
            intent.setParam(RecoveryStore.RECOVERY_INTENT, RecoveryStore.getInstance().getIntent());
        }
        if (!RecoveryStore.getInstance().getIntents().isEmpty()) {
            intent.setSequenceableArrayListParam(RecoveryStore.RECOVERY_INTENTS,
                    RecoveryStore.getInstance().getIntents());
        }
        intent.setParam(RecoveryService.RECOVERY_SILENT_MODE_VALUE,
                Recovery.getInstance().getSilentMode().getValue());

        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(Recovery.getInstance().getContext().getBundleName())
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION)
                .withAbilityName(RecoveryService.class)
                .build();
        intent.setOperation(operation);
        RecoveryService.start(Recovery.getInstance().getContext(), intent);
    }

    void register() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void killProcess() {
        ProcessManager.kill(ProcessManager.getPid());
    }

    public void restart() {
        String className = "";
        try {
            BundleInfo bundleInfo = Recovery.getInstance().getContext().getBundleManager()
                    .getBundleInfo(Recovery.getInstance().getContext().getBundleName(),
                            IBundleManager.GET_ABILITY_INFO_WITH_PERMISSION);
            AbilityInfo abilityInfo = Recovery.getInstance().getContext().getBundleManager()
                    .getModuleMainAbility(Recovery.getInstance().getContext().getBundleName(),
                            bundleInfo.getModuleNames().get(0));
            className = abilityInfo.getClassName();
        } catch (RemoteException e) {
            LogUtil.error("RecoveryHandler","RemoteException");
        }

        Intent mIntent = new Intent();
        Set<String> entities = new HashSet<>();
        entities.add(IntentConstants.ENTITY_HOME);
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(Recovery.getInstance().getContext().getBundleName())
                .withAbilityName(className)
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION)
                .withAction(IntentConstants.ACTION_HOME)
                .withEntities(entities)
                .build();
        mIntent.setOperation(operation);
        RecoveryService.start(Recovery.getInstance().getContext(), mIntent);
    }
}
