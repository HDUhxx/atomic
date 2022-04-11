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

package com.zxy.recovery.test;

import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;
import com.zxy.recovery.tools.RecoveryLog;
import ohos.aafwk.ability.AbilityPackage;

/**
 * MyApplication
 *
 * @author:wjt
 * @since 2021-04-06
 */
public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(true)
                .recoverStack(true)
                .mainPage(MainAbility.class)
                .recoverEnabled(true)
                .callback(new MyCrashCallback())
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .skip(TestAbility.class)
                .init(this);
    }

    /**
     * MyCrashCallback
     *
     * @author:wjt
     * @since 2021-04-06
     */
    static final class MyCrashCallback implements RecoveryCallback {
        @Override
        public void stackTrace(String exceptionMessage) {
            RecoveryLog.e("exceptionMessage:" + exceptionMessage);
        }

        @Override
        public void cause(String cause) {
            RecoveryLog.e("cause:" + cause);
        }

        @Override
        public void exception(String exceptionType, String throwClassName,
                              String throwMethodName, int throwLineNumber) {
            RecoveryLog.e("exceptionType:" + exceptionType);
            RecoveryLog.e("throwClassName:" + throwClassName);
            RecoveryLog.e("throwMethodName:" + throwMethodName);
            RecoveryLog.e("throwLineNumber:" + throwLineNumber);
        }

        @Override
        public void throwable(Throwable throwable) {
        }
    }
}
