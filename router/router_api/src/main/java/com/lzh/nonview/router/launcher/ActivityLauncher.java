/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzh.nonview.router.launcher;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;


/**
 * The base class of <i><b>Activity Launcher</b></i>
 * he default impl is {@link DefaultActivityLauncher}
 *
 * @since 2021-04-06
 */
public abstract class ActivityLauncher extends Launcher{

    /**
     * 调用时将调用此方法
     *
     * @param context
     * @return Intent
     */
    public abstract Intent createIntent(Ability context);

    /**
     * Fraction
     *
     * @param fragment
     * @throws Exception a error occurs
     */
    public abstract void open(Fraction fragment) throws Exception;
}
