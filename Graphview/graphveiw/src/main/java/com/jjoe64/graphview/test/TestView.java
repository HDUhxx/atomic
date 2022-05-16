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
package com.jjoe64.graphview.test;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

public class TestView extends Component implements Component.DrawTask , Component.TouchEventListener{
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public TestView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    public TestView(Context context, AttrSet attrSet, int resId) {
        super(context, attrSet, resId);

    }

    @Override
    public void onDraw(Component component, Canvas canvas) {

    }


    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        return false;
    }
}
