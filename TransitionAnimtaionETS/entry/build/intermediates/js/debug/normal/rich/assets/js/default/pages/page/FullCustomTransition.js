/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!******************************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/TransitionAnimtaionETS/entry/src/main/ets/default/pages/page/FullCustomTransition.ets?entry ***!
  \******************************************************************************************************************************************************/
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
class FullCustomTransition extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.__myProgress = new ObservedPropertySimple(1, this, "myProgress");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.myProgress !== undefined) {
            this.myProgress = params.myProgress;
        }
    }
    aboutToBeDeleted() {
        this.__myProgress.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get myProgress() {
        return this.__myProgress.get();
    }
    set myProgress(newValue) {
        this.__myProgress.set(newValue);
    }
    render() {
        Stack.create();
        Stack.width('100%');
        Stack.height('100%');
        Stack.opacity(this.myProgress);
        Stack.scale({ x: this.myProgress, y: this.myProgress });
        Stack.rotate({ x: 0, y: 0, z: 1, angle: 360 * this.myProgress });
        Image.create({ "id": 0, "type": 30000, params: ['image1.jpg'] });
        Image.objectFit(ImageFit.Cover);
        Image.width('100%');
        Image.height('100%');
        Stack.pop();
    }
    pageTransition() {
        PageTransition.create();
        // 页面入场组件： 进场过程中会逐帧触发onEnter回调，入参为动效的归一化进度(0% -- 100%)
        PageTransitionEnter.create({ duration: 800, curve: Curve.Smooth });
        // 页面入场组件： 进场过程中会逐帧触发onEnter回调，入参为动效的归一化进度(0% -- 100%)
        PageTransitionEnter.onEnter((type, progress) => {
            this.myProgress = progress; // 页面入场式myProgress从0变化到1
        });
        // 页面退场组件： 进场过程中会逐帧触发onExit回调，入参为动效的归一化进度(0% -- 100%)
        PageTransitionExit.create({ duration: 800, curve: Curve.Smooth });
        // 页面退场组件： 进场过程中会逐帧触发onExit回调，入参为动效的归一化进度(0% -- 100%)
        PageTransitionExit.onExit((type, progress) => {
            this.myProgress = 1 - progress; //页面退场式myProgress从1变化到0
        });
        PageTransition.pop();
    }
}
loadDocument(new FullCustomTransition("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=FullCustomTransition.js.map