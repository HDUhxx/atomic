/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!************************************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/TransitionAnimtaionETS/entry/src/main/ets/default/pages/ComponentTransition.ets?entry ***!
  \************************************************************************************************************************************************/
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
class ComponentItem extends View {
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
        Stack.create({ alignContent: Alignment.Center });
        Stack.height(120);
        Stack.borderRadius(15);
        Stack.width('80%');
        Stack.padding({ top: 20 });
        Stack.transition({ type: TransitionType.Insert, scale: { x: 0.5, y: 0.5 }, opacity: 0 });
        Stack.transition({ type: TransitionType.Delete, rotate: { x: 0, y: 1, z: 0, angle: 360 }, scale: { x: 0, y: 0 } });
        Image.create({ "id": 0, "type": 30000, params: ['image3.png'] });
        Image.objectFit(ImageFit.Cover);
        Image.width('100%');
        Image.height(120);
        Image.borderRadius(15);
        Stack.pop();
    }
}
class ComponentTransition extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.__isShow = new ObservedPropertySimple(false, this, "isShow");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.isShow !== undefined) {
            this.isShow = params.isShow;
        }
    }
    aboutToBeDeleted() {
        this.__isShow.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get isShow() {
        return this.__isShow.get();
    }
    set isShow(newValue) {
        this.__isShow.set(newValue);
    }
    render() {
        Column.create();
        Column.padding({ left: 20, right: 20 });
        Column.backgroundColor('#FFECECEC');
        Column.height('100%');
        Column.width('100%');
        If.create();
        if (this.isShow) {
            If.branchId(0);
            let earlierCreatedChild_2 = this.findChildById("2");
            if (earlierCreatedChild_2 == undefined) {
                View.create(new ComponentItem("2", this, {}));
            }
            else {
                earlierCreatedChild_2.updateWithValueParams({});
                if (!earlierCreatedChild_2.needsUpdate()) {
                    earlierCreatedChild_2.markStatic();
                }
                View.create(earlierCreatedChild_2);
            }
        }
        If.pop();
        let earlierCreatedChild_3 = this.findChildById("3");
        if (earlierCreatedChild_3 == undefined) {
            View.create(new ComponentItem("3", this, {}));
        }
        else {
            earlierCreatedChild_3.updateWithValueParams({});
            if (!earlierCreatedChild_3.needsUpdate()) {
                earlierCreatedChild_3.markStatic();
            }
            View.create(earlierCreatedChild_3);
        }
        Button.createWithLabel("Toggle");
        Button.onClick(() => {
            //执行动效，动效时长600ms
            Context.animateTo({ duration: 600 }, () => {
                this.isShow = !this.isShow;
            });
        });
        Button.height(45);
        Button.width(200);
        Button.fontColor(Color.Black);
        Button.backgroundColor('rgb(181,222,224)');
        Button.margin({ top: 20 });
        Button.pop();
        Column.pop();
    }
}
loadDocument(new ComponentTransition("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=ComponentTransition.js.map