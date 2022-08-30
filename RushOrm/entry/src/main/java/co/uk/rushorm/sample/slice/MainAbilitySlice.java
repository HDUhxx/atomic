
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

package co.uk.rushorm.sample.slice;

import co.uk.rushorm.sample.ResourceTable;

import co.uk.rushorm.core.LogUtil;
import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;
import co.uk.rushorm.sample.demo.Car;
import co.uk.rushorm.sample.demo.Engine;
import co.uk.rushorm.sample.demo.Wheel;
import com.alibaba.fastjson.JSON;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Button btnSave = (Button) findComponentById(ResourceTable.Id_btn_save);
        Button btnSelect = (Button) findComponentById(ResourceTable.Id_btn_select);
        Button btnDel = (Button) findComponentById(ResourceTable.Id_btn_del);
        Text tvShow = (Text) findComponentById(ResourceTable.Id_text_show);
        //保存
        btnSave.setClickedListener(component -> {
            final long startTime = System.currentTimeMillis();
            final int numberToSave = 100;

            List<Car> cars = new ArrayList<>(numberToSave);
            for (int i = 0; i < numberToSave; i++) {
                Car car = new Car("Red", new Engine());
                car.wheels = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    car.wheels.add(new Wheel("Michelin"));
                }
                cars.add(car);
            }

            RushCore.getInstance().save(cars, () -> {
                long endTime = System.currentTimeMillis();
                final double saveTime = (endTime - startTime) / 1000.0;
                getAbility().getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.error("test", "Rush main complete====");
                        tvShow.setText("保存成功!总耗时:"+String.format("%1$,.2f s", saveTime));
                    }
                });
            });
        });
        //查询
        btnSelect.setClickedListener(component -> {
            LogUtil.error("test", "Rush main select start====");
            new Thread(() -> {
                List<Car> cars = new RushSearch().find(Car.class);
                getAbility().getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.error("test", "Rush main select complete====");
                        tvShow.setText("查询成功:"+JSON.toJSONString(cars));
                    }
                });
            }).start();
        });
        //删除
        btnDel.setClickedListener(component -> new Thread(() -> {
            RushCore.getInstance().deleteAll(Car.class);
            RushCore.getInstance().deleteAll(Engine.class);
            RushCore.getInstance().deleteAll(Wheel.class);
            getAbility().getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                @Override
                public void run() {
                    tvShow.setText("删除成功");
                }
            });
        }).start());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
