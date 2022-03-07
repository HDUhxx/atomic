package com.zxy.recovery.core;


import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.ElementName;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RecoveryStore
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public final class RecoveryStore {

    /**
     * RECOVERY_INTENT
     */
    public static final String RECOVERY_INTENT = "recovery_intent";

    /**
     * RECOVERY_INTENTS
     */
    public static final String RECOVERY_INTENTS = "recovery_intents";

    /**
     * RECOVERY_STACK
     */
    public static final String RECOVERY_STACK = "recovery_stack";

    /**
     * recovery_is_debug
     */
    public static final String IS_DEBUG = "recovery_is_debug";

    /**
     * recovery_stack_trace
     */
    public static final String STACK_TRACE = "recovery_stack_trace";

    /**
     * recovery_exception_data
     */
    public static final String EXCEPTION_DATA = "recovery_exception_data";

    /**
     * recovery_exception_cause
     */
    public static final String EXCEPTION_CAUSE = "recovery_exception_cause";

    private volatile static RecoveryStore sInstance;

    private static final Object LOCK = new Object();

    private List<WeakReference<Ability>> mRunningActivities;

    private Intent mIntent;

    private RecoveryStore() {
        mRunningActivities = new CopyOnWriteArrayList<>();
    }

    /**
     * 获取单例
     *
     * @return RecoveryStore
     */
    public static RecoveryStore getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new RecoveryStore();
                }
            }
        }
        return sInstance;
    }

    public synchronized Intent getIntent() {
        return mIntent;
    }

    public synchronized void setIntent(Intent intent) {
        mIntent = intent;
    }

    /**
     * verifyAbility
     *
     * @param ability
     * @return boolean
     */
    public boolean verifyAbility(Ability ability) {
        return ability != null
                && !Recovery.getInstance().getSkipActivities().contains(ability.getClass())
                && !RecoveryAbility.class.isInstance(ability);
    }

    public List<WeakReference<Ability>> getRunningActivities() {
        return mRunningActivities;
    }

    /**
     * putAbility
     *
     * @param ability
     */
    public void putAbility(Ability ability) {
        WeakReference<Ability> weakReference = new WeakReference<>(ability);
        mRunningActivities.add(weakReference);
    }

    /**
     * contains
     *
     * @param ability
     * @return boolean
     */
    public boolean contains(Ability ability) {
        if (ability == null) {
            return false;
        }
        int size = mRunningActivities.size();
        for (int i = 0; i < size; i++) {
            WeakReference<Ability> refer = mRunningActivities.get(i);
            if (refer == null) {
                continue;
            }
            Ability tmp = refer.get();
            if (tmp == null) {
                continue;
            }
            if (ability == tmp) {
                return true;
            }

        }
        return false;
    }

    /**
     * 移除Ability
     *
     * @param ability
     */
    public void removeAbility(Ability ability) {
        for (WeakReference<Ability> abilityWeakReference : mRunningActivities) {
            if (abilityWeakReference == null) {
                continue;
            }
            Ability tmpAbility = abilityWeakReference.get();
            if (tmpAbility == null)
                continue;
            if (tmpAbility == ability) {
                mRunningActivities.remove(abilityWeakReference);
                break;
            }
        }
    }

    int getRunningAbilityCount() {
        AtomicInteger count = new AtomicInteger(0);
        for (WeakReference<Ability> abilityWeakReference : mRunningActivities) {
            if (abilityWeakReference == null) {
                continue;
            }
            Ability ability = abilityWeakReference.get();
            if (ability == null) {
                continue;
            }
            count.set(count.incrementAndGet());
        }
        return count.get();
    }

    ElementName getBaseAbility() {
        for (WeakReference<Ability> abilityWeakReference : mRunningActivities) {
            if (abilityWeakReference == null) {
                continue;
            }
            Ability tmpAbility = abilityWeakReference.get();
            if (tmpAbility == null) {
                continue;
            }
            return tmpAbility.getElementName();
        }
        return null;
    }

    ArrayList<Intent> getIntents() {
        ArrayList<Intent> intentList = new ArrayList<>();
        for (WeakReference<Ability> abilityWeakReference : mRunningActivities) {
            if (abilityWeakReference == null)
                continue;
            Ability tmpAbility = abilityWeakReference.get();
            if (tmpAbility == null) {
                continue;
            }
            intentList.add((Intent) tmpAbility.getIntent().clone());
        }
        return intentList;
    }

    /**
     * ExceptionData
     *
     * @since 2021-04-06
     */
    public static final class ExceptionData implements Sequenceable {
        private String type;
        private String className;
        private String methodName;
        private int lineNumber;

        /**
         * ExceptionData
         *
         * @return ExceptionData
         */
        public static ExceptionData newInstance() {
            return new ExceptionData();
        }

        /**
         * ExceptionData
         *
         * @param type
         * @return ExceptionData
         */
        public ExceptionData type(String type) {
            this.type = type;
            return this;
        }

        /**
         * ExceptionData
         *
         * @param className
         * @return ExceptionData
         */
        public ExceptionData className(String className) {
            this.className = className;
            return this;
        }

        /**
         * ExceptionData
         *
         * @param methodName
         * @return ExceptionData
         */
        public ExceptionData methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        /**
         * lineNumber
         *
         * @param lineNumber
         * @return ExceptionData
         */
        public ExceptionData lineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public String getType() {
            return type;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        @Override
        public boolean marshalling(Parcel out) {
            return out.writeString(type)
                    && out.writeString(className)
                    && out.writeString(methodName)
                    && out.writeInt(lineNumber);
        }

        @Override
        public boolean unmarshalling(Parcel in) {
            this.type = in.readString();
            this.className = in.readString();
            this.methodName = in.readString();
            this.lineNumber = in.readInt();
            return true;
        }

        /**
         * Sequenceable
         */
        public static final Sequenceable.Producer PRODUCER = in -> {
            ExceptionData instance = new ExceptionData();
            instance.unmarshalling(in);
            return instance;
        };
    }
}
