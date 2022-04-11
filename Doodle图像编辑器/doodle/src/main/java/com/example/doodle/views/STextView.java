//
///*
// * Copyright (C) 2021 Huawei Device Co., Ltd.
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//
//package com.example.doodlelib.views;
//
//import ohos.agp.components.AttrSet;
//import ohos.agp.components.ComponentState;
//import ohos.agp.utils.Color;
//import ohos.app.Context;
//
///**
// * STextView
// *
// * @since 2021-04-29
// */
//public class STextView extends PaddingTextView {
//    /**
//     * STextView
//     *
//     * @param context
//     */
//    public STextView(Context context) {
//        this(context, (AttrSet) null);
//    }
//
//    /**
//     * STextView
//     *
//     * @param context
//     * @param attrSet
//     */
//    public STextView(Context context, AttrSet attrSet) {
//        this(context, attrSet, null);
//    }
//
//    /**
//     * STextView
//     *
//     * @param context
//     * @param attrSet
//     * @param styleName
//     */
//    public STextView(Context context, AttrSet attrSet, String styleName) {
//        super(context, attrSet, styleName);
//        init(attrSet);
//    }
//
//    private void init(AttrSet attrs) {
//        // int defaultColor = this.getTextColors().getDefaultColor();
//        // 获取TV的默认颜色，无API暂用此方法
//        Color defaultColor = this.getTextColor();
//        Color textColorSelected = null;
//        Color textColorPressed = null;
//        Color textColorDisable = null;
//        if (attrs.getAttr("stv_text_color_selected").isPresent()) {
//            textColorSelected = attrs.getAttr("stv_text_color_selected").get().getColorValue();
//        }
//        if (attrs.getAttr("stv_text_color_pressed").isPresent()) {
//            textColorPressed = attrs.getAttr("stv_text_color_pressed").get().getColorValue();
//        }
//        if (attrs.getAttr("stv_text_color_disable").isPresent()) {
//            textColorDisable = attrs.getAttr("stv_text_color_disable").get().getColorValue();
//        }
//
//        // 状态选择器的状态
//        int[][] state = {{ComponentState.COMPONENT_STATE_EMPTY, ComponentState.COMPONENT_STATE_PRESSED},
//            {ComponentState.COMPONENT_STATE_EMPTY, ComponentState.COMPONENT_STATE_SELECTED},
//            {-ComponentState.COMPONENT_STATE_EMPTY},
//            {},
//        };
//
//        // 状态选择器对应的colors
//        int[] colors = {
//            textColorPressed.getValue(),
//            textColorSelected.getValue(),
//            textColorDisable.getValue(),
//            defaultColor.getValue(),
//        };
//        // 阻塞：如何获取View的element和设置以上的状态选择器值
//        // element.setStateColorList(state, colors);
//
//    }
//}