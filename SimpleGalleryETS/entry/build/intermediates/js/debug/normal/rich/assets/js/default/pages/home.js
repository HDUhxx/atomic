/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/bottomTabs.ets":
/*!****************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/bottomTabs.ets ***!
  \****************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.BottomTabs = void 0;
/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
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
var prompt = isSystemplugin('prompt', 'system') ? globalThis.systemplugin.prompt : globalThis.requireNapi('prompt');
function getTabSrc(tabIndex, index) {
    let imgSrc = { "id": 16777259, "type": 20000, params: [] };
    if (tabIndex === index) {
        imgSrc = { "id": 16777258, "type": 20000, params: [] };
    }
    return imgSrc;
}
function getTabTextColor(tabIndex, index) {
    let color = '#000000';
    if (tabIndex === index) {
        color = '#0A59F7';
    }
    return color;
}
class BottomTabs extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.tabSrc = [0, 1, 2, 3];
        this.backgroundColor = '#F1F3F5';
        this.controller = new TabsController();
        this.tips = '这是测试功能，暂时未实现';
        this.__bottomTabIndex = new SynchedPropertySimpleTwoWay(params.bottomTabIndex, this, "bottomTabIndex");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.tabSrc !== undefined) {
            this.tabSrc = params.tabSrc;
        }
        if (params.backgroundColor !== undefined) {
            this.backgroundColor = params.backgroundColor;
        }
        if (params.controller !== undefined) {
            this.controller = params.controller;
        }
        if (params.tips !== undefined) {
            this.tips = params.tips;
        }
    }
    aboutToBeDeleted() {
        this.__bottomTabIndex.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get bottomTabIndex() {
        return this.__bottomTabIndex.get();
    }
    set bottomTabIndex(newValue) {
        this.__bottomTabIndex.set(newValue);
    }
    render() {
        Flex.create({ direction: FlexDirection.Row, alignItems: ItemAlign.Center, justifyContent: FlexAlign.SpaceEvenly });
        Flex.width('100%');
        Flex.height('8%');
        Flex.backgroundColor(this.backgroundColor);
        ForEach.create("1", this, ObservedObject.GetRawObject(this.tabSrc), item => {
            Column.create();
            Column.onClick(() => {
                if (item === this.bottomTabIndex) {
                    this.controller.changeIndex(this.bottomTabIndex);
                }
                else {
                    prompt.showToast({
                        message: this.tips
                    });
                }
            });
            Image.create(getTabSrc(this.bottomTabIndex, item));
            Image.objectFit(ImageFit.Contain);
            Image.width('60%');
            Image.height('60%');
            Text.create({ "id": 16777234, "type": 10003, params: [] });
            Text.fontSize(14);
            Text.fontColor(getTabTextColor(this.bottomTabIndex, item));
            Text.pop();
            Column.pop();
        }, item => item.toString());
        ForEach.pop();
        Flex.pop();
    }
}
exports.BottomTabs = BottomTabs;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/homeListItem.ets":
/*!******************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/homeListItem.ets ***!
  \******************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {


/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.HomeListItem = void 0;
var router = globalThis.requireNativeModule('system.router');
const homeListDataModel_1 = __webpack_require__(/*! ../model/homeListDataModel */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/homeListDataModel.ets");
class HomeListItem extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.recommends = [{ "id": 16777236, "type": 10003, params: [] }, { "id": 16777223, "type": 10003, params: [] }, { "id": 16777232, "type": 10003, params: [] }, { "id": 16777217, "type": 10003, params: [] }];
        this.listItems = homeListDataModel_1.initializeImageData();
        this.isToday = true;
        this.titleIndex = 0;
        this.imageWidth = '120';
        this.ratio = 1;
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.recommends !== undefined) {
            this.recommends = params.recommends;
        }
        if (params.listItems !== undefined) {
            this.listItems = params.listItems;
        }
        if (params.isToday !== undefined) {
            this.isToday = params.isToday;
        }
        if (params.titleIndex !== undefined) {
            this.titleIndex = params.titleIndex;
        }
        if (params.imageWidth !== undefined) {
            this.imageWidth = params.imageWidth;
        }
        if (params.ratio !== undefined) {
            this.ratio = params.ratio;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    aboutToAppear() {
        if (this.titleIndex === 0) {
            this.listItems = homeListDataModel_1.initializeTodayData();
            this.isToday = true;
            this.imageWidth = '92';
            this.ratio = 1;
        }
        else {
            this.listItems = homeListDataModel_1.initializeImageData();
            this.isToday = false;
            this.imageWidth = '145';
            this.ratio = 0.65;
        }
    }
    render() {
        Column.create();
        Row.create();
        Row.width('100%');
        Text.create(this.recommends[this.titleIndex]);
        Text.textAlign(TextAlign.Start);
        Text.fontSize(16);
        Text.fontWeight(FontWeight.Bold);
        Text.layoutWeight(1);
        Text.borderColor(Color.Blue);
        Text.pop();
        Text.create({ "id": 16777227, "type": 10003, params: [] });
        Text.fontSize(16);
        Text.fontColor(Color.Gray);
        Text.pop();
        Image.create({ "id": 16777248, "type": 20000, params: [] });
        Image.width('10');
        Image.height('5%');
        Image.alignSelf(ItemAlign.End);
        Image.objectFit(ImageFit.Contain);
        Row.pop();
        List.create({ initialIndex: 0 });
        List.listDirection(Axis.Horizontal);
        List.margin({ top: '2%', bottom: '2%' });
        ForEach.create("1", this, ObservedObject.GetRawObject(this.listItems), item => {
            ListItem.create();
            Image.create(item.smallImg);
            Image.width(this.imageWidth);
            Image.aspectRatio(this.ratio);
            Image.borderRadius(10);
            Image.margin({ right: 15 });
            Image.sharedTransition(this.titleIndex + item.id, { duration: 500, curve: Curve.Linear });
            Image.onClick(() => {
                let shareIdStr = this.titleIndex + item.id;
                console.info('Item onClick' + shareIdStr);
                router.push({
                    uri: 'pages/image',
                    params: { isToday: this.isToday, shareId: shareIdStr, index: item.id, ratio: this.ratio }
                });
            });
            ListItem.pop();
        }, item => item.name);
        ForEach.pop();
        List.pop();
        Column.pop();
    }
}
exports.HomeListItem = HomeListItem;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/homeTabContent.ets":
/*!********************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/homeTabContent.ets ***!
  \********************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {


/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.HomeTabComponent = void 0;
const topTabs_1 = __webpack_require__(/*! ../common/topTabs */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/topTabs.ets");
const subscribe_1 = __webpack_require__(/*! ../common/subscribe */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/subscribe.ets");
const homeListItem_1 = __webpack_require__(/*! ../common/homeListItem */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/homeListItem.ets");
class HomeTabComponent extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.recommends = [{ "id": 16777236, "type": 10003, params: [] }, { "id": 16777223, "type": 10003, params: [] }, { "id": 16777232, "type": 10003, params: [] }, { "id": 16777217, "type": 10003, params: [] }];
        this.__showSettings = new SynchedPropertySimpleTwoWay(params.showSettings, this, "showSettings");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.recommends !== undefined) {
            this.recommends = params.recommends;
        }
    }
    aboutToBeDeleted() {
        this.__showSettings.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get showSettings() {
        return this.__showSettings.get();
    }
    set showSettings(newValue) {
        this.__showSettings.set(newValue);
    }
    render() {
        Column.create();
        let earlierCreatedChild_1 = this.findChildById("1");
        if (earlierCreatedChild_1 == undefined) {
            View.create(new topTabs_1.TopTabs("1", this, { showSettings: this.__showSettings }));
        }
        else {
            earlierCreatedChild_1.updateWithValueParams({});
            View.create(earlierCreatedChild_1);
        }
        List.create();
        List.listDirection(Axis.Vertical);
        List.width('100%');
        List.layoutWeight(1);
        ListItem.create();
        let earlierCreatedChild_2 = this.findChildById("2");
        if (earlierCreatedChild_2 == undefined) {
            View.create(new subscribe_1.SubscribeSwiper("2", this, {}));
        }
        else {
            earlierCreatedChild_2.updateWithValueParams({});
            if (!earlierCreatedChild_2.needsUpdate()) {
                earlierCreatedChild_2.markStatic();
            }
            View.create(earlierCreatedChild_2);
        }
        ListItem.pop();
        ForEach.create("4", this, ObservedObject.GetRawObject(this.recommends), item => {
            ListItem.create();
            let earlierCreatedChild_3 = this.findChildById("3");
            if (earlierCreatedChild_3 == undefined) {
                View.create(new homeListItem_1.HomeListItem("3", this, { titleIndex: this.recommends.indexOf(item) }));
            }
            else {
                earlierCreatedChild_3.updateWithValueParams({
                    titleIndex: this.recommends.indexOf(item)
                });
                if (!earlierCreatedChild_3.needsUpdate()) {
                    earlierCreatedChild_3.markStatic();
                }
                View.create(earlierCreatedChild_3);
            }
            ListItem.pop();
        }, item => this.recommends.indexOf(item).toString());
        ForEach.pop();
        List.pop();
        Column.pop();
    }
}
exports.HomeTabComponent = HomeTabComponent;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/notificationSettings.ets":
/*!**************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/notificationSettings.ets ***!
  \**************************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {


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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.NotificationSettings = void 0;
const subscribeDataModels_1 = __webpack_require__(/*! ../model/subscribeDataModels */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/subscribeDataModels.ets");
var reminderAgent = globalThis.requireNapi('reminderAgent') || (isSystemplugin('reminderAgent', 'ohos') ? globalThis.ohosplugin.reminderAgent : isSystemplugin('reminderAgent', 'system') ? globalThis.systemplugin.reminderAgent : undefined);
let subscribeItems = subscribeDataModels_1.getSubscribeData();
let reminderId = 0;
class NotificationSettings extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.__subscribes = new ObservedPropertyObject(subscribeItems, this, "subscribes");
        this.__isToggleOn = new ObservedPropertySimple(subscribeDataModels_1.getSwitchState(), this, "isToggleOn");
        this.__showSettings = new SynchedPropertySimpleTwoWay(params.showSettings, this, "showSettings");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.subscribes !== undefined) {
            this.subscribes = params.subscribes;
        }
        if (params.isToggleOn !== undefined) {
            this.isToggleOn = params.isToggleOn;
        }
    }
    aboutToBeDeleted() {
        this.__subscribes.aboutToBeDeleted();
        this.__isToggleOn.aboutToBeDeleted();
        this.__showSettings.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get subscribes() {
        return this.__subscribes.get();
    }
    set subscribes(newValue) {
        this.__subscribes.set(newValue);
    }
    get isToggleOn() {
        return this.__isToggleOn.get();
    }
    set isToggleOn(newValue) {
        this.__isToggleOn.set(newValue);
    }
    get showSettings() {
        return this.__showSettings.get();
    }
    set showSettings(newValue) {
        this.__showSettings.set(newValue);
    }
    render() {
        Scroll.create();
        Scroll.height('70%');
        Scroll.width('100%');
        Scroll.borderRadius(30);
        Scroll.backgroundColor('#E8E8E8');
        Scroll.transition({ type: TransitionType.Insert, translate: { y: '100%' } });
        Scroll.transition({ type: TransitionType.Delete, translate: { y: '100%' } });
        Column.create();
        Column.backgroundColor('#E8E8E8');
        Column.constraintSize({ minHeight: '100%' });
        Column.borderRadius(30);
        Text.create();
        Text.width('20%');
        Text.height('1%');
        Text.backgroundColor(Color.Gray);
        Text.borderRadius(20);
        Text.margin({ top: 5 });
        Text.pop();
        Row.create();
        Row.width('100%');
        Row.padding({ top: '2%', left: '5%', right: '5%' });
        Text.create({ "id": 16777229, "type": 10003, params: [] });
        Text.fontSize(18);
        Text.fontColor(Color.Black);
        Text.fontWeight(FontWeight.Bold);
        Text.layoutWeight(1);
        Text.textAlign(TextAlign.Start);
        Text.pop();
        Image.create({ "id": 16777240, "type": 20000, params: [] });
        Image.objectFit(ImageFit.Cover);
        Image.width(30);
        Image.height(30);
        Image.onClick(() => {
            Context.animateTo({ duration: 500, curve: Curve.Ease }, () => {
                this.showSettings = !this.showSettings;
            });
        });
        Row.pop();
        Text.create({ "id": 16777224, "type": 10003, params: [] });
        Text.width('100%');
        Text.fontColor(Color.Grey);
        Text.fontSize(14);
        Text.textAlign(TextAlign.Start);
        Text.padding({ top: '3%', left: '5%' });
        Text.pop();
        Column.create();
        Flex.create({ direction: FlexDirection.Row, alignItems: ItemAlign.Center, justifyContent: FlexAlign.End });
        Flex.width('100%');
        Flex.margin({ top: '3%' });
        Flex.padding({ left: '5%', right: '5%' });
        Flex.backgroundColor(Color.White);
        Text.create({ "id": 16777218, "type": 10003, params: [] });
        Text.fontSize(16);
        Text.fontColor(Color.Black);
        Text.fontWeight(FontWeight.Bold);
        Text.width('100%');
        Text.textAlign(TextAlign.Start);
        Text.pop();
        Toggle.create({ type: ToggleType.Switch, isOn: this.isToggleOn });
        Toggle.width(70);
        Toggle.height(30);
        Toggle.pop();
        Flex.pop();
        Text.create({ "id": 16777231, "type": 10003, params: [] });
        Text.fontColor(Color.Grey);
        Text.fontSize(12);
        Text.margin({ top: '3%' });
        Text.pop();
        List.create();
        List.backgroundColor(Color.White);
        List.margin({ top: '5%' });
        List.padding({ left: '5%', right: '5%' });
        List.divider({ strokeWidth: 1, color: '#E5E5E6' });
        ForEach.create("1", this, ObservedObject.GetRawObject(this.subscribes), item => {
            If.create();
            if (item.isSubscribe) {
                If.branchId(0);
                ListItem.create();
                Flex.create({ direction: FlexDirection.Row, alignItems: ItemAlign.Center });
                Flex.width('100%');
                Flex.height('10%');
                Text.create(item.title);
                Text.fontSize(16);
                Text.fontColor(Color.Black);
                Text.fontWeight(FontWeight.Bold);
                Text.width('100%');
                Text.textAlign(TextAlign.Start);
                Text.layoutWeight(1);
                Text.pop();
                Image.create({ "id": 16777241, "type": 20000, params: [] });
                Image.objectFit(ImageFit.Contain);
                Image.width(25);
                Image.onClick(() => {
                    reminderId = item.reminderId;
                    subscribeItems[item.id].isSubscribe = false;
                    subscribeItems[item.id].reminderId = 0;
                    this.subscribes = subscribeItems;
                    this.isToggleOn = subscribeDataModels_1.getSwitchState();
                    reminderAgent.cancelReminder(reminderId);
                });
                Flex.pop();
                ListItem.pop();
            }
            If.pop();
        }, item => item.title.toString());
        ForEach.pop();
        List.pop();
        Column.pop();
        Column.pop();
        Scroll.pop();
    }
}
exports.NotificationSettings = NotificationSettings;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/subscribe.ets":
/*!***************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/subscribe.ets ***!
  \***************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {


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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.SubscribeSwiper = void 0;
var reminderAgent = globalThis.requireNapi('reminderAgent') || (isSystemplugin('reminderAgent', 'ohos') ? globalThis.ohosplugin.reminderAgent : isSystemplugin('reminderAgent', 'system') ? globalThis.systemplugin.reminderAgent : undefined);
const subscribeDataModels_1 = __webpack_require__(/*! ../model/subscribeDataModels */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/subscribeDataModels.ets");
let calendar = null;
let subscribeItems = subscribeDataModels_1.getSubscribeData();
let subscribeTimes = subscribeDataModels_1.initializeSubscribeTimes();
let num = 0;
class SubscribeDialog extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.controller = undefined;
        this.action = undefined;
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.controller !== undefined) {
            this.controller = params.controller;
        }
        if (params.action !== undefined) {
            this.action = params.action;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    setController(ctr) {
        this.controller = ctr;
    }
    render() {
        Column.create();
        Column.padding('3%');
        Text.create({ "id": 16777233, "type": 10003, params: [] });
        Text.fontSize(17);
        Text.fontColor(Color.Black);
        Text.fontWeight(FontWeight.Bold);
        Text.pop();
        Text.create({ "id": 16777216, "type": 10003, params: [] });
        Text.fontSize(17);
        Text.fontColor(Color.Black);
        Text.fontWeight(FontWeight.Medium);
        Text.margin({ top: '3%' });
        Text.pop();
        Row.create();
        Row.width('100%');
        Row.margin({ top: '3%' });
        Button.createWithLabel({ "id": 16777238, "type": 10003, params: [] });
        Button.layoutWeight(7);
        Button.fontColor(Color.Blue);
        Button.backgroundColor(Color.White);
        Button.margin(5);
        Button.onClick(() => {
            this.controller.close();
            this.action();
        });
        Button.pop();
        Text.create();
        Text.width(1);
        Text.height(35);
        Text.backgroundColor('#8F8F8F');
        Text.pop();
        Button.createWithLabel({ "id": 16777228, "type": 10003, params: [] });
        Button.layoutWeight(7);
        Button.fontColor(Color.Red);
        Button.backgroundColor(Color.White);
        Button.margin(5);
        Button.onClick(() => {
            this.controller.close();
            this.action();
        });
        Button.pop();
        Row.pop();
        Column.pop();
    }
}
class SwiperItem extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.index = undefined;
        this.isSubscribe = undefined;
        this.dialogController = new CustomDialogController({
            builder: () => {
                let jsDialog = new SubscribeDialog("1", this, { action: this.onAccept });
                jsDialog.setController(this.dialogController);
                View.create(jsDialog);
            },
            cancel: this.cancelDialog,
            autoCancel: true
        }, this);
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.index !== undefined) {
            this.index = params.index;
        }
        if (params.isSubscribe !== undefined) {
            this.isSubscribe = params.isSubscribe;
        }
        if (params.dialogController !== undefined) {
            this.dialogController = params.dialogController;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    onAccept() {
        console.log("onAccept");
        if (calendar == null) {
            console.log("the calendar reminder is null");
            return;
        }
        reminderAgent.publishReminder(calendar, (err, reminderId) => {
            console.info('num=' + num + ', reminderId:' + reminderId);
            subscribeItems[num].isSubscribe = true;
            subscribeItems[num].reminderId = reminderId;
        });
    }
    cancelDialog() {
        console.log("Cancel dialog");
    }
    render() {
        Stack.create({ alignContent: Alignment.BottomStart });
        Stack.height('100%');
        Stack.width(330);
        Stack.onClick(() => {
            num = this.index;
            if (this.index >= subscribeItems.length || subscribeItems[this.index].isSubscribe) {
                calendar = null;
                return;
            }
            else {
                calendar = {
                    reminderType: 1,
                    dateTime: {
                        year: subscribeTimes[this.index].year,
                        month: subscribeTimes[this.index].month,
                        day: subscribeTimes[this.index].day,
                        hour: subscribeTimes[this.index].hour,
                        minute: subscribeTimes[this.index].minute,
                        second: subscribeTimes[this.index].second
                    },
                    repeatMonths: [0],
                    repeatDays: [0],
                    actionButton: [
                        {
                            title: 'close',
                            type: 0
                        },
                        {
                            title: 'snooze',
                            type: 1
                        },
                    ],
                    wantAgent: {
                        pkgName: 'ohos.samples.simplegallery',
                        abilityName: 'ohos.samples.simplegallery.MainAbility'
                    },
                    maxScreenWantAgent: {
                        pkgName: 'ohos.samples.simplegallery',
                        abilityName: 'ohos.samples.simplegallery.MainAbility'
                    },
                    ringDuration: 5,
                    snoozeTimes: 2,
                    timeInterval: 5,
                    title: 'SimpleGallery',
                    content: subscribeItems[this.index].title,
                    expiredContent: '',
                    snoozeContent: "remind later",
                    notificationId: 100,
                    slotType: 3
                };
            }
            this.dialogController.open();
        });
        Image.create(subscribeItems[this.index].image);
        Image.objectFit(ImageFit.Cover);
        Image.height('100%');
        Image.width('100%');
        Image.borderRadius(10);
        Text.create(subscribeItems[this.index].title);
        Text.fontSize(15);
        Text.fontColor(Color.Black);
        Text.fontWeight(FontWeight.Bold);
        Text.margin({ left: '5%', bottom: '10%' });
        Text.pop();
        Stack.pop();
    }
}
class SubscribeSwiper extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.index = 0;
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.index !== undefined) {
            this.index = params.index;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    render() {
        Swiper.create();
        Swiper.width('100%');
        Swiper.height('30%');
        Swiper.indicator(false);
        Swiper.index(this.index);
        Swiper.autoPlay(true);
        Swiper.itemSpace(15);
        Swiper.displayMode(SwiperDisplayMode.AutoLinear);
        Swiper.margin({ bottom: '4%' });
        ForEach.create("3", this, ObservedObject.GetRawObject(subscribeItems), item => {
            let earlierCreatedChild_2 = this.findChildById("2");
            if (earlierCreatedChild_2 == undefined) {
                View.create(new SwiperItem("2", this, { index: item.id }));
            }
            else {
                earlierCreatedChild_2.updateWithValueParams({
                    index: item.id
                });
                if (!earlierCreatedChild_2.needsUpdate()) {
                    earlierCreatedChild_2.markStatic();
                }
                View.create(earlierCreatedChild_2);
            }
        }, item => item.title);
        ForEach.pop();
        Swiper.pop();
    }
}
exports.SubscribeSwiper = SubscribeSwiper;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/topTabs.ets":
/*!*************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/topTabs.ets ***!
  \*************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.TopTabs = void 0;
var prompt = isSystemplugin('prompt', 'system') ? globalThis.systemplugin.prompt : globalThis.requireNapi('prompt');
var resourceManager = globalThis.requireNapi('resourceManager') || (isSystemplugin('resourceManager', 'ohos') ? globalThis.ohosplugin.resourceManager : isSystemplugin('resourceManager', 'system') ? globalThis.systemplugin.resourceManager : undefined);
class TopTabs extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.tabs = [{ "id": 16777222, "type": 10003, params: [] }, { "id": 16777220, "type": 10003, params: [] }, { "id": 16777237, "type": 10003, params: [] }, { "id": 16777225, "type": 10003, params: [] }, { "id": 16777230, "type": 10003, params: [] }];
        this.tips = '这是测试功能，暂时未实现';
        this.__showSettings = new SynchedPropertySimpleTwoWay(params.showSettings, this, "showSettings");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.tabs !== undefined) {
            this.tabs = params.tabs;
        }
        if (params.tips !== undefined) {
            this.tips = params.tips;
        }
    }
    aboutToBeDeleted() {
        this.__showSettings.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get showSettings() {
        return this.__showSettings.get();
    }
    set showSettings(newValue) {
        this.__showSettings.set(newValue);
    }
    render() {
        Flex.create({ direction: FlexDirection.Row, alignItems: ItemAlign.Center });
        Flex.height('8%');
        Flex.width('100%');
        Flex.alignSelf(ItemAlign.Start);
        Row.create();
        Row.layoutWeight(1);
        Row.height('100%');
        ForEach.create("1", this, ObservedObject.GetRawObject(this.tabs), item => {
            If.create();
            if (this.tabs.indexOf(item) == 0) {
                If.branchId(0);
                Text.create(item);
                Text.fontSize(18);
                Text.fontWeight(FontWeight.Bold);
                Text.pop();
            }
            else {
                If.branchId(1);
                Text.create(item);
                Text.fontSize(17);
                Text.fontWeight(FontWeight.Medium);
                Text.margin({ left: '5%' });
                Text.pop();
            }
            If.pop();
        }, item => this.tabs.indexOf(item).toString());
        ForEach.pop();
        Row.pop();
        Image.create({ "id": 16777256, "type": 20000, params: [] });
        Image.width('7%');
        Image.height('80%');
        Image.constraintSize({ maxWidth: 25 });
        Image.objectFit(ImageFit.Contain);
        Image.onClick(() => {
            prompt.showToast({
                message: this.tips
            });
        });
        Image.create({ "id": 16777257, "type": 20000, params: [] });
        Image.width('7%');
        Image.height('80%');
        Image.constraintSize({ maxWidth: 25 });
        Image.objectFit(ImageFit.Contain);
        Image.margin({ left: 5 });
        Image.onClick(() => {
            Context.animateTo({ duration: 500, curve: Curve.Ease }, () => {
                this.showSettings = !this.showSettings;
            });
        });
        Flex.pop();
    }
}
exports.TopTabs = TopTabs;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/homeListDataModel.ets":
/*!**********************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/homeListDataModel.ets ***!
  \**********************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.initializeTodayData = exports.initializeImageData = exports.ImageData = void 0;
class ImageData {
    constructor(id, smallImg, bigImg, name) {
        this.id = id;
        this.smallImg = smallImg;
        this.bigImg = bigImg;
        this.name = name;
    }
}
exports.ImageData = ImageData;
function initializeImageData() {
    let imageDataArray = [
        { "id": "0", "smallImg": { "id": 16777249, "type": 20000, params: [] }, "bigImg": { "id": 16777249, "type": 20000, params: [] }, "name": '推荐1' },
        { "id": "1", "smallImg": { "id": 16777250, "type": 20000, params: [] }, "bigImg": { "id": 16777250, "type": 20000, params: [] }, "name": '推荐2' },
        { "id": "2", "smallImg": { "id": 16777251, "type": 20000, params: [] }, "bigImg": { "id": 16777251, "type": 20000, params: [] }, "name": '推荐3' },
        { "id": "3", "smallImg": { "id": 16777252, "type": 20000, params: [] }, "bigImg": { "id": 16777252, "type": 20000, params: [] }, "name": '推荐4' },
        { "id": "4", "smallImg": { "id": 16777253, "type": 20000, params: [] }, "bigImg": { "id": 16777253, "type": 20000, params: [] }, "name": '推荐5' },
        { "id": "5", "smallImg": { "id": 16777254, "type": 20000, params: [] }, "bigImg": { "id": 16777254, "type": 20000, params: [] }, "name": '推荐6' },
        { "id": "6", "smallImg": { "id": 16777255, "type": 20000, params: [] }, "bigImg": { "id": 16777255, "type": 20000, params: [] }, "name": '推荐7' },
    ];
    return imageDataArray;
}
exports.initializeImageData = initializeImageData;
function initializeTodayData() {
    let todyDataArray = [
        { "id": "0", "smallImg": { "id": 16777260, "type": 20000, params: [] }, "bigImg": { "id": 16777260, "type": 20000, params: [] }, "name": '今日推荐1' },
        { "id": "1", "smallImg": { "id": 16777263, "type": 20000, params: [] }, "bigImg": { "id": 16777263, "type": 20000, params: [] }, "name": '今日推荐2' },
        { "id": "2", "smallImg": { "id": 16777264, "type": 20000, params: [] }, "bigImg": { "id": 16777264, "type": 20000, params: [] }, "name": '今日推荐3' },
        { "id": "3", "smallImg": { "id": 16777265, "type": 20000, params: [] }, "bigImg": { "id": 16777265, "type": 20000, params: [] }, "name": '今日推荐4' },
        { "id": "4", "smallImg": { "id": 16777266, "type": 20000, params: [] }, "bigImg": { "id": 16777266, "type": 20000, params: [] }, "name": '今日推荐5' },
        { "id": "5", "smallImg": { "id": 16777267, "type": 20000, params: [] }, "bigImg": { "id": 16777267, "type": 20000, params: [] }, "name": '今日推荐6' },
        { "id": "6", "smallImg": { "id": 16777268, "type": 20000, params: [] }, "bigImg": { "id": 16777268, "type": 20000, params: [] }, "name": '今日推荐7' },
        { "id": "7", "smallImg": { "id": 16777269, "type": 20000, params: [] }, "bigImg": { "id": 16777269, "type": 20000, params: [] }, "name": '今日推荐8' },
        { "id": "8", "smallImg": { "id": 16777270, "type": 20000, params: [] }, "bigImg": { "id": 16777270, "type": 20000, params: [] }, "name": '今日推荐9' },
        { "id": "9", "smallImg": { "id": 16777261, "type": 20000, params: [] }, "bigImg": { "id": 16777261, "type": 20000, params: [] }, "name": '今日推荐10' },
        { "id": "10", "smallImg": { "id": 16777262, "type": 20000, params: [] }, "bigImg": { "id": 16777262, "type": 20000, params: [] }, "name": '今日推荐11' }
    ];
    return todyDataArray;
}
exports.initializeTodayData = initializeTodayData;


/***/ }),

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/subscribeDataModels.ets":
/*!************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/subscribeDataModels.ets ***!
  \************************************************************************************************************************************/
/***/ ((__unused_webpack_module, exports) => {


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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.getSwitchState = exports.getSubscribeData = exports.SubscribeData = exports.initializeSubscribeTimes = exports.SubscribeTime = void 0;
class SubscribeTime {
    constructor(year, month, day, hour, minute, second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
}
exports.SubscribeTime = SubscribeTime;
function initializeSubscribeTimes() {
    let subscribeTimeArray = [
        { "year": 2021, "month": 9, "day": 17, "hour": 20, "minute": 0, "second": 0 },
        { "year": 2021, "month": 9, "day": 18, "hour": 20, "minute": 30, "second": 0 },
        { "year": 2021, "month": 9, "day": 19, "hour": 20, "minute": 0, "second": 0 },
        { "year": 2021, "month": 9, "day": 19, "hour": 20, "minute": 0, "second": 0 },
    ];
    return subscribeTimeArray;
}
exports.initializeSubscribeTimes = initializeSubscribeTimes;
class SubscribeData {
    constructor(id, title, image, isSubscribe, reminderId) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.isSubscribe = isSubscribe;
        this.reminderId = reminderId;
    }
}
exports.SubscribeData = SubscribeData;
let subscribeDataArray = null;
function getSubscribeData() {
    if (subscribeDataArray === null) {
        subscribeDataArray = [
            { "id": 0, "title": '精致女人穿搭技巧', "image": { "id": 16777244, "type": 20000, params: [] }, "isSubscribe": false, "reminderId": 0 },
            { "id": 1, "title": '猫咪的欢乐日常', "image": { "id": 16777245, "type": 20000, params: [] }, "isSubscribe": false, "reminderId": 0 },
            { "id": 2, "title": '开心养鱼', "image": { "id": 16777246, "type": 20000, params: [] }, "isSubscribe": false, "reminderId": 0 },
            { "id": 3, "title": '炎炎养猫记', "image": { "id": 16777247, "type": 20000, params: [] }, "isSubscribe": false, "reminderId": 0 }
        ];
    }
    return subscribeDataArray;
}
exports.getSubscribeData = getSubscribeData;
let settingsSwitchState = false;
function getSwitchState() {
    settingsSwitchState = false;
    subscribeDataArray.forEach(item => {
        if (item.isSubscribe) {
            settingsSwitchState = true;
        }
    });
    return settingsSwitchState;
}
exports.getSwitchState = getSwitchState;


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// This entry need to be wrapped in an IIFE because it need to be isolated against other modules in the chunk.
(() => {
var exports = __webpack_exports__;
/*!***************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/pages/home.ets?entry ***!
  \***************************************************************************************************************************/

Object.defineProperty(exports, "__esModule", ({ value: true }));
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
const notificationSettings_1 = __webpack_require__(/*! ../common/notificationSettings */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/notificationSettings.ets");
const homeTabContent_1 = __webpack_require__(/*! ../common/homeTabContent */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/homeTabContent.ets");
const bottomTabs_1 = __webpack_require__(/*! ../common/bottomTabs */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/common/bottomTabs.ets");
class HomeComponent extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.controller = new TabsController();
        this.backgroundColor = '#F1F3F5';
        this.__bottomTabIndex = new ObservedPropertySimple(1, this, "bottomTabIndex");
        this.__showSettings = new ObservedPropertySimple(false, this, "showSettings");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.controller !== undefined) {
            this.controller = params.controller;
        }
        if (params.backgroundColor !== undefined) {
            this.backgroundColor = params.backgroundColor;
        }
        if (params.bottomTabIndex !== undefined) {
            this.bottomTabIndex = params.bottomTabIndex;
        }
        if (params.showSettings !== undefined) {
            this.showSettings = params.showSettings;
        }
    }
    aboutToBeDeleted() {
        this.__bottomTabIndex.aboutToBeDeleted();
        this.__showSettings.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get bottomTabIndex() {
        return this.__bottomTabIndex.get();
    }
    set bottomTabIndex(newValue) {
        this.__bottomTabIndex.set(newValue);
    }
    get showSettings() {
        return this.__showSettings.get();
    }
    set showSettings(newValue) {
        this.__showSettings.set(newValue);
    }
    render() {
        Stack.create({ alignContent: Alignment.BottomStart });
        Stack.width('100%');
        Stack.height('100%');
        Flex.create({ direction: FlexDirection.Column, alignItems: ItemAlign.End, justifyContent: FlexAlign.End });
        Flex.width('100%');
        Flex.layoutWeight(1);
        Flex.backgroundColor(this.backgroundColor);
        Tabs.create({ barPosition: BarPosition.End, index: 1, controller: this.controller });
        Tabs.onChange((index) => {
            this.bottomTabIndex = index;
        });
        Tabs.vertical(false);
        Tabs.barHeight(0);
        Tabs.width('100%');
        Tabs.scrollable(false);
        TabContent.create();
        TabContent.pop();
        TabContent.create();
        TabContent.padding({ left: 15, right: 15 });
        let earlierCreatedChild_2 = this.findChildById("2");
        if (earlierCreatedChild_2 == undefined) {
            View.create(new homeTabContent_1.HomeTabComponent("2", this, { showSettings: this.__showSettings }));
        }
        else {
            earlierCreatedChild_2.updateWithValueParams({});
            View.create(earlierCreatedChild_2);
        }
        TabContent.pop();
        TabContent.create();
        TabContent.pop();
        TabContent.create();
        TabContent.pop();
        Tabs.pop();
        let earlierCreatedChild_3 = this.findChildById("3");
        if (earlierCreatedChild_3 == undefined) {
            View.create(new bottomTabs_1.BottomTabs("3", this, { controller: this.controller, bottomTabIndex: this.__bottomTabIndex }));
        }
        else {
            earlierCreatedChild_3.updateWithValueParams({
                controller: this.controller
            });
            View.create(earlierCreatedChild_3);
        }
        Flex.pop();
        If.create();
        if (this.showSettings) {
            If.branchId(0);
            let earlierCreatedChild_4 = this.findChildById("4");
            if (earlierCreatedChild_4 == undefined) {
                View.create(new notificationSettings_1.NotificationSettings("4", this, { showSettings: this.__showSettings }));
            }
            else {
                earlierCreatedChild_4.updateWithValueParams({});
                View.create(earlierCreatedChild_4);
            }
        }
        If.pop();
        Stack.pop();
    }
}
loadDocument(new HomeComponent("1", undefined, {}));

})();

/******/ })()
;
//# sourceMappingURL=home.js.map