/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!**************************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/TransitionAnimtaionETS/entry/src/main/ets/default/pages/page/BottomTransition.ets?entry ***!
  \**************************************************************************************************************************************************/
/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
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
class BottomTransition extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    render() {
        Stack.create();
        Stack.width('100%');
        Stack.height('100%');
        Image.create({ "id": 0, "type": 30000, params: ['image1.jpg'] });
        Image.objectFit(ImageFit.Cover);
        Image.width('100%');
        Image.height('100%');
        Stack.pop();
    }
    // 页面转场通过全局transition方法进行配置转场参数
    pageTransition() {
        PageTransition.create();
        // 页面入场组件：SlideEffect.Bottom 设置到入场时表示从下边滑入，出场时表示滑出到下边。
        PageTransitionEnter.create({ duration: 600, curve: Curve.Smooth });
        // 页面入场组件：SlideEffect.Bottom 设置到入场时表示从下边滑入，出场时表示滑出到下边。
        PageTransitionEnter.slide(SlideEffect.Bottom);
        // 页面退场组件：SlideEffect.Bottom 设置到入场时表示从下边滑入，出场时表示滑出到下边。
        PageTransitionExit.create({ duration: 600, curve: Curve.Smooth });
        // 页面退场组件：SlideEffect.Bottom 设置到入场时表示从下边滑入，出场时表示滑出到下边。
        PageTransitionExit.slide(SlideEffect.Bottom);
        PageTransition.pop();
    }
}
loadDocument(new BottomTransition("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=BottomTransition.js.map