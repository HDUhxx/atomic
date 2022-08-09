/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!********************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/TransitionAnimtaionETS/entry/src/main/ets/default/pages/share/ShareItem.ets?entry ***!
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
var router = globalThis.requireNativeModule('system.router');
class ShareItem extends View {
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
        Flex.create();
        Flex.width('100%');
        Flex.padding({ left: 16, right: 16 });
        Flex.backgroundColor('#FFECECEC');
        Flex.create({ justifyContent: FlexAlign.Start, alignItems: ItemAlign.Center });
        Flex.height(120);
        Flex.backgroundColor('rgb(181,222,224)');
        Flex.borderRadius(15);
        Flex.margin({ top: 20 });
        Stack.create();
        Stack.height('100%');
        Stack.width('100%');
        Image.create({ "id": 0, "type": 30000, params: ['image2.jpg'] });
        Image.sharedTransition('imageId', { duration: 600, curve: Curve.Smooth, delay: 100 });
        Image.onClick(() => {
            router.push({ uri: 'pages/share/SharePage' });
        });
        Image.objectFit(ImageFit.Cover);
        Image.height('100%');
        Image.width('100%');
        Image.borderRadius(15);
        Stack.pop();
        Text.create('点击查看共享元素转场动效');
        Text.fontSize(20);
        Text.fontColor(Color.Black);
        Text.fontWeight(FontWeight.Regular);
        Text.margin({ left: 10, right: 10 });
        Text.pop();
        Flex.pop();
        Flex.pop();
    }
}
loadDocument(new ShareItem("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=ShareItem.js.map