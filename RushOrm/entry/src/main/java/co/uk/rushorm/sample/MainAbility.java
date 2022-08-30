
/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.uk.rushorm.sample;

import co.uk.rushorm.ohos.OhosInitializeConfig;
import co.uk.rushorm.ohos.RushOhos;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.sample.demo.Car;
import co.uk.rushorm.sample.demo.Engine;
import co.uk.rushorm.sample.demo.Wheel;
import co.uk.rushorm.sample.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(Car.class);
        classes.add(Engine.class);
        classes.add(Wheel.class);
        OhosInitializeConfig ohosInitializeConfig = new OhosInitializeConfig(getApplicationContext(), classes);
        RushOhos.initialize(ohosInitializeConfig);
    }
}
