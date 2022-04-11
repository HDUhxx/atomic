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

package com.lzh.nonview.router.demo.slice;

import com.lzh.nonview.router.demo.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Text;

/**
 * 由于依赖的是外部库，所以直接用自己的方法实现
 *
 * @since 2021-03-20
 */
public class ArgsAbilitySlice extends AbilitySlice {
    private boolean isBoo;
    private int mInt;
    private byte mByte;
    private short mShort;
    private char mChar;
    private float mFloat;
    private long mLong;
    private double mDouble;
    private String mString = null;
    private String mUser = null;
    private String mUrl = null;
    private IntentParams intentParams = null;
    private IntentParams intentParams2 = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        intentParams = intent.getParams();
        intentParams2 = intent.getParam("IntentParams");
        super.setUIContent(ResourceTable.Layout_ability_args);
        final Text showText = (Text) findComponentById(ResourceTable.Id_show_text);
        final StringBuilder sb = new StringBuilder();
        isBoo = (boolean) intentParams2.getParam("mBoolean");
        mInt = (int) intentParams2.getParam("mInt");
        mByte = (byte) intentParams2.getParam("mByte");
        mShort = (short) intentParams2.getParam("mShort");
        mChar = (char) intentParams2.getParam("mChar");
        mFloat = (float) intentParams2.getParam("mFloat");
        mLong = (long) intentParams2.getParam("mLong");
        mDouble = (double) intentParams2.getParam("mDouble");
        mString = (String) intentParams2.getParam("mString");
        mUser = (String) intentParams2.getParam("mUser");
        mUrl = (String) intentParams2.getParam("mUrl");
        sb.append(
                "isBoo =" + isBoo + System.lineSeparator()
                + "mInt=" + mInt
                + System.lineSeparator() + "mByte=" + mByte + System.lineSeparator()
                + "mShort=" + mShort + System.lineSeparator() + "mChar="
                + mChar + System.lineSeparator() + "mFloat=" + mFloat + System.lineSeparator()
                + "mLong=" + mLong + System.lineSeparator()
                + "mDouble=" + mDouble + System.lineSeparator()
                + "mString=" + mString + System.lineSeparator()
                + "mUser=" + mUser + System.lineSeparator() + "mUrl=" + mUrl);
        showText.setText(sb.toString());
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
