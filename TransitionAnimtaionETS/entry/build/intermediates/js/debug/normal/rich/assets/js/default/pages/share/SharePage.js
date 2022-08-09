/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!********************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/TransitionAnimtaionETS/entry/src/main/ets/default/pages/share/SharePage.ets?entry ***!
  \********************************************************************************************************************************************/
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
class SharePage extends View {
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
        Image.create({ "id": 0, "type": 30000, params: ['image2.jpg'] });
        Image.sharedTransition('imageId', { duration: 1000, curve: Curve.Smooth, delay: 100 });
        Image.objectFit(ImageFit.Cover);
        Image.width('100%');
        Image.height('100%');
        Stack.pop();
    }
}
loadDocument(new SharePage("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=SharePage.js.map