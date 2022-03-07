package com.zxy.recovery.core;


import com.zxy.recovery.callback.RecoveryCallback;
import ohos.aafwk.ability.Ability;
import ohos.app.Context;

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

    private Recovery() {
    }

    /**
     * getInstance
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
     * init
     *
     * @param context
     */
    public void init(Context context) {

    }

    /**
     * debug
     *
     * @param isDebug
     * @return Recovery
     */
    public Recovery debug(boolean isDebug) {
        return this;
    }

    /**
     * recoverStack
     *
     * @param isRecoverStack
     * @return Recovery
     */
    public Recovery recoverStack(boolean isRecoverStack) {
        return this;
    }

    /**
     * recoverInBackground
     *
     * @param isRecoverInBackground
     * @return Recovery
     */
    public Recovery recoverInBackground(boolean isRecoverInBackground) {
        return this;
    }

    /**
     * mainPage
     *
     * @param clazz
     * @return Recovery
     */
    public Recovery mainPage(Class<? extends Ability> clazz) {
        return this;
    }

    /**
     * callback
     *
     * @param callback
     * @return Recovery
     */
    public Recovery callback(RecoveryCallback callback) {
        return this;
    }

    /**
     * silent
     *
     * @param enabled
     * @param mode
     * @return Recovery
     */
    public Recovery silent(boolean enabled, SilentMode mode) {
        return this;
    }

    /**
     * skip
     *
     * @param activities
     * @return Recovery
     */
    @SafeVarargs
    public final Recovery skip(Class<? extends Ability>... activities) {
        return this;
    }

    /**
     * recoverEnabled
     *
     * @param enabled
     * @return Recovery
     */
    public Recovery recoverEnabled(boolean enabled) {
        return this;
    }

    /**
     * SilentMode
     *
     * @author:zhengxiaoyong
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
         * getMode
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
