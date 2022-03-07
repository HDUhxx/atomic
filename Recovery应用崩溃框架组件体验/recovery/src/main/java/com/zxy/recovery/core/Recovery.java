package com.zxy.recovery.core;

import com.zxy.recovery.callback.RecoveryActivityLifecycleCallback;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.exception.RecoveryException;
import com.zxy.recovery.tools.RecoveryUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityPackage;
import ohos.app.Context;
import ohos.app.ElementsCallback;
import ohos.global.configuration.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Recovery
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public class Recovery {
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;
    private static final int NUM4 = 4;
    private volatile static Recovery sInstance;

    private static final Object LOCK = new Object();

    private Context mContext;

    private boolean isDebug = false;

    /**
     * The default to restore the stack.
     */
    private boolean isRecoverStack = true;

    /**
     * The default is not restore the background process.
     */
    private boolean isRecoverInBackground = false;

    private Class<? extends Ability> mMainPageClass;

    private RecoveryCallback mCallback;

    private boolean isSilentEnabled = false;

    private SilentMode mSilentMode = SilentMode.RECOVER_ACTIVITY_STACK;

    private List<Class<? extends Ability>> mSkipActivities = new ArrayList<>();

    /**
     * Whether to enter recovery mode.
     */
    private boolean isRecoverEnabled = true;

    private Recovery() {
    }

    /**
     * 获取单例
     *
     * @return Recovery
     */
    public static Recovery getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new Recovery();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param context
     * @throws RecoveryException
     */
    public void init(Context context) {
        if (context == null) {
            throw new RecoveryException("Context can not be null!");
        }
        if (!(context instanceof AbilityPackage)) {
            context = context.getApplicationContext();
        }
        mContext = context;
        if (!RecoveryUtil.isMainProcess(context)) {
            return;
        }
        registerRecoveryHandler();
        registerRecoveryLifecycleCallback();
    }

    /**
     * 是否开启debug
     *
     * @param isDebug2
     * @return Recovery
     */
    public Recovery debug(boolean isDebug2) {
        this.isDebug = isDebug2;
        return this;
    }

    /**
     * 是否恢复栈
     *
     * @param isRecoverStack2
     * @return Recovery
     */
    public Recovery recoverStack(boolean isRecoverStack2) {
        this.isRecoverStack = isRecoverStack2;
        return this;
    }

    /**
     * 是否后台崩溃后恢复
     *
     * @param isRecoverInBackground2
     * @return Recovery
     */
    public Recovery recoverInBackground(boolean isRecoverInBackground2) {
        this.isRecoverInBackground = isRecoverInBackground2;
        return this;
    }

    /**
     * 指定后台页面
     *
     * @param clazz
     * @return Recovery
     */
    public Recovery mainPage(Class<? extends Ability> clazz) {
        this.mMainPageClass = clazz;
        return this;
    }

    /**
     * 回调
     *
     * @param callback
     * @return Recovery
     */
    public Recovery callback(RecoveryCallback callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * 静默恢复
     *
     * @param isEnabled
     * @param mode
     * @return Recovery
     */
    public Recovery silent(boolean isEnabled, SilentMode mode) {
        this.isSilentEnabled = isEnabled;
        this.mSilentMode = mode == null ? SilentMode.RECOVER_ACTIVITY_STACK : mode;
        return this;
    }

    /**
     * 跳过
     *
     * @param activities
     * @return Recovery
     */
    @SafeVarargs
    public final Recovery skip(Class<? extends Ability>... activities) {
        if (activities == null) {
            return this;
        }
        mSkipActivities.addAll(Arrays.asList(activities));
        return this;
    }

    /**
     * 恢复开关
     *
     * @param isEnabled
     * @return Recovery
     */
    public Recovery recoverEnabled(boolean isEnabled) {
        this.isRecoverEnabled = isEnabled;
        return this;
    }

    private void registerRecoveryHandler() {
        RecoveryHandler.newInstance(Thread.getDefaultUncaughtExceptionHandler()).setCallback(mCallback).register();
    }

    private void registerRecoveryLifecycleCallback() {
        ((AbilityPackage) getContext()).registerCallbacks(new RecoveryActivityLifecycleCallback(),
                new ElementsCallback() {
                    @Override
                    public void onMemoryLevel(int i) {
                    }

                    @Override
                    public void onConfigurationUpdated(Configuration configuration) {
                    }
                });
    }

    public Context getContext() {
        return RecoveryUtil.checkNotNull(mContext, "The context is not initialized");
    }

    public boolean isDebug() {
        return isDebug;
    }

    boolean isRecoverInBackground() {
        return isRecoverInBackground;
    }

    boolean isRecoverStack() {
        return isRecoverStack;
    }

    boolean isRecoverEnabled() {
        return isRecoverEnabled;
    }

    Class<? extends Ability> getMainPageClass() {
        return mMainPageClass;
    }

    boolean isSilentEnabled() {
        return isSilentEnabled;
    }

    SilentMode getSilentMode() {
        return mSilentMode;
    }

    public List<Class<? extends Ability>> getSkipActivities() {
        return mSkipActivities;
    }

    /**
     * SilentMode
     *
     * @since 2021-04-06
     */

    public enum SilentMode {
        /**
         * RESTART
         */
        RESTART(1),
        /**
         * RECOVER_ACTIVITY_STACK
         */
        RECOVER_ACTIVITY_STACK(2),
        /**
         * RECOVER_TOP_ACTIVITY
         */
        RECOVER_TOP_ACTIVITY(3),
        /**
         * RESTART_AND_CLEAR
         */
        RESTART_AND_CLEAR(4);

        int value;

        SilentMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        /**
         * SilentMode
         *
         * @param value
         * @return SilentMode
         */
        public static SilentMode getMode(int value) {
            switch (value) {
                case 1:
                    return RESTART;
                case NUM2:
                    return RECOVER_ACTIVITY_STACK;
                case NUM3:
                    return RECOVER_TOP_ACTIVITY;
                case NUM4:
                    return RESTART_AND_CLEAR;
                default:
                    return RECOVER_ACTIVITY_STACK;
            }
        }
    }
}
