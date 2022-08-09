/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!**********************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/TransitionAnimtaionETS/entry/src/main/ets/default/pages/Index.ets?entry ***!
  \**********************************************************************************************************************************/
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
class Item extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.text = undefined;
        this.uri = undefined;
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.text !== undefined) {
            this.text = params.text;
        }
        if (params.uri !== undefined) {
            this.uri = params.uri;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    render() {
        Stack.create({ alignContent: Alignment.Center });
        Stack.onClick(() => {
            router.push({ uri: this.uri });
        });
        Stack.height(100);
        Stack.borderRadius(15);
        Stack.width('80%');
        Stack.margin({ bottom: 20 });
        Image.create({ "id": 0, "type": 30000, params: ['image3.png'] });
        Image.objectFit(ImageFit.Cover);
        Image.width('100%');
        Image.height(100);
        Image.borderRadius(15);
        Text.create(this.text);
        Text.fontSize(20);
        Text.fontWeight(FontWeight.Bold);
        Text.fontColor(Color.Black);
        Text.pop();
        Stack.pop();
    }
}
class Index extends View {
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
        Flex.create({ direction: FlexDirection.Column, alignItems: ItemAlign.Center, justifyContent: FlexAlign.Center });
        Flex.width('100%');
        Flex.height('100%');
        Flex.backgroundColor('#FFECECEC');
        let earlierCreatedChild_2 = this.findChildById("2");
        if (earlierCreatedChild_2 == undefined) {
            View.create(new Item("2", this, { text: '页面间转场：底部滑入', uri: 'pages/page/BottomTransition' }));
        }
        else {
            earlierCreatedChild_2.updateWithValueParams({
                text: '页面间转场：底部滑入', uri: 'pages/page/BottomTransition'
            });
            if (!earlierCreatedChild_2.needsUpdate()) {
                earlierCreatedChild_2.markStatic();
            }
            View.create(earlierCreatedChild_2);
        }
        let earlierCreatedChild_3 = this.findChildById("3");
        if (earlierCreatedChild_3 == undefined) {
            View.create(new Item("3", this, { text: '页面间转场：自定义1', uri: 'pages/page/CustomTransition' }));
        }
        else {
            earlierCreatedChild_3.updateWithValueParams({
                text: '页面间转场：自定义1', uri: 'pages/page/CustomTransition'
            });
            if (!earlierCreatedChild_3.needsUpdate()) {
                earlierCreatedChild_3.markStatic();
            }
            View.create(earlierCreatedChild_3);
        }
        let earlierCreatedChild_4 = this.findChildById("4");
        if (earlierCreatedChild_4 == undefined) {
            View.create(new Item("4", this, { text: '页面间转场：自定义2', uri: 'pages/page/FullCustomTransition' }));
        }
        else {
            earlierCreatedChild_4.updateWithValueParams({
                text: '页面间转场：自定义2', uri: 'pages/page/FullCustomTransition'
            });
            if (!earlierCreatedChild_4.needsUpdate()) {
                earlierCreatedChild_4.markStatic();
            }
            View.create(earlierCreatedChild_4);
        }
        let earlierCreatedChild_5 = this.findChildById("5");
        if (earlierCreatedChild_5 == undefined) {
            View.create(new Item("5", this, { text: '组件内转场', uri: 'pages/ComponentTransition' }));
        }
        else {
            earlierCreatedChild_5.updateWithValueParams({
                text: '组件内转场', uri: 'pages/ComponentTransition'
            });
            if (!earlierCreatedChild_5.needsUpdate()) {
                earlierCreatedChild_5.markStatic();
            }
            View.create(earlierCreatedChild_5);
        }
        let earlierCreatedChild_6 = this.findChildById("6");
        if (earlierCreatedChild_6 == undefined) {
            View.create(new Item("6", this, { text: '共享元素转场', uri: 'pages/share/ShareItem' }));
        }
        else {
            earlierCreatedChild_6.updateWithValueParams({
                text: '共享元素转场', uri: 'pages/share/ShareItem'
            });
            if (!earlierCreatedChild_6.needsUpdate()) {
                earlierCreatedChild_6.markStatic();
            }
            View.create(earlierCreatedChild_6);
        }
        Flex.pop();
    }
}
loadDocument(new Index("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=Index.js.map