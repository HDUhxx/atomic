/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

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
/*!****************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/pages/image.ets?entry ***!
  \****************************************************************************************************************************/

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
var router = globalThis.requireNativeModule('system.router');
const homeListDataModel_1 = __webpack_require__(/*! ../model/homeListDataModel */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleGalleryETS/entry/src/main/ets/default/model/homeListDataModel.ets");
class ImageComponent extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.imageSrc = homeListDataModel_1.initializeImageData();
        this.imageIndex = router.getParams().index;
        this.shareId = router.getParams().shareId;
        this.isToday = router.getParams().isToday;
        this.__ratio = new ObservedPropertySimple(router.getParams().ratio, this, "ratio");
        this.__title = new ObservedPropertySimple('', this, "title");
        this.__imageMargin = new ObservedPropertySimple(56, this, "imageMargin");
        this.__visibility = new ObservedPropertySimple(Visibility.Visible, this, "visibility");
        this.__scale = new ObservedPropertySimple(1, this, "scale");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.imageSrc !== undefined) {
            this.imageSrc = params.imageSrc;
        }
        if (params.imageIndex !== undefined) {
            this.imageIndex = params.imageIndex;
        }
        if (params.shareId !== undefined) {
            this.shareId = params.shareId;
        }
        if (params.isToday !== undefined) {
            this.isToday = params.isToday;
        }
        if (params.ratio !== undefined) {
            this.ratio = params.ratio;
        }
        if (params.title !== undefined) {
            this.title = params.title;
        }
        if (params.imageMargin !== undefined) {
            this.imageMargin = params.imageMargin;
        }
        if (params.visibility !== undefined) {
            this.visibility = params.visibility;
        }
        if (params.scale !== undefined) {
            this.scale = params.scale;
        }
    }
    aboutToBeDeleted() {
        this.__ratio.aboutToBeDeleted();
        this.__title.aboutToBeDeleted();
        this.__imageMargin.aboutToBeDeleted();
        this.__visibility.aboutToBeDeleted();
        this.__scale.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get ratio() {
        return this.__ratio.get();
    }
    set ratio(newValue) {
        this.__ratio.set(newValue);
    }
    get title() {
        return this.__title.get();
    }
    set title(newValue) {
        this.__title.set(newValue);
    }
    get imageMargin() {
        return this.__imageMargin.get();
    }
    set imageMargin(newValue) {
        this.__imageMargin.set(newValue);
    }
    get visibility() {
        return this.__visibility.get();
    }
    set visibility(newValue) {
        this.__visibility.set(newValue);
    }
    get scale() {
        return this.__scale.get();
    }
    set scale(newValue) {
        this.__scale.set(newValue);
    }
    aboutToAppear() {
        if (this.isToday) {
            this.imageSrc = homeListDataModel_1.initializeTodayData();
        }
        else {
            this.imageSrc = homeListDataModel_1.initializeImageData();
        }
        if (this.imageIndex < this.imageSrc.length) {
            this.title = this.imageSrc[this.imageIndex].name;
        }
        setTimeout(() => {
            this.ratio = 0;
        }, 500);
    }
    render() {
        Flex.create({ direction: FlexDirection.Column, alignItems: ItemAlign.End, justifyContent: FlexAlign.End });
        Flex.width('100%');
        Flex.height('100%');
        Row.create();
        Row.width('100%');
        Row.height(this.imageMargin);
        Row.backgroundColor('#F1F3F5');
        Row.visibility(this.visibility);
        Image.create({ "id": 16777239, "type": 20000, params: [] });
        Image.width('8%');
        Image.height('50%');
        Image.objectFit(ImageFit.Contain);
        Image.margin({ left: 10 });
        Image.onClick(() => {
            router.back();
        });
        Text.create(this.title);
        Text.fontSize(20);
        Text.fontColor(Color.Black);
        Text.margin({ left: 10 });
        Text.pop();
        Row.pop();
        Flex.create({ direction: FlexDirection.Column, alignItems: ItemAlign.Center, justifyContent: FlexAlign.Center });
        Flex.width('100%');
        Flex.height('100%');
        Swiper.create();
        Swiper.width('100%');
        Swiper.height('100%');
        Swiper.aspectRatio(this.ratio);
        Swiper.scale({ x: this.scale, y: this.scale });
        Swiper.index(this.imageIndex);
        Swiper.indicator(false);
        Swiper.loop(false);
        Swiper.sharedTransition(this.shareId, { duration: 500, curve: Curve.Linear });
        Swiper.onChange((index) => {
            this.scale = 1.0;
            this.imageIndex = index;
            if (this.imageIndex < this.imageSrc.length) {
                this.title = this.imageSrc[this.imageIndex].name;
            }
        });
        ForEach.create("2", this, ObservedObject.GetRawObject(this.imageSrc), item => {
            Image.create(item.bigImg);
            Image.height('100%');
            Image.width('100%');
            Image.objectFit(ImageFit.Contain);
            Image.onClick(() => {
                console.info('Image Click');
                this.scale = 1;
                if (this.visibility == Visibility.Hidden) {
                    this.imageMargin = 56;
                    this.visibility = Visibility.Visible;
                }
                else {
                    this.imageMargin = 0;
                    this.visibility = Visibility.Hidden;
                }
            });
            Gesture.create(GesturePriority.Low);
            GestureGroup.create(GestureMode.Parallel);
            PinchGesture.create();
            PinchGesture.onActionStart(() => {
                console.log('pinch start');
            });
            PinchGesture.onActionUpdate((event) => {
                if (this.visibility == Visibility.Hidden && event.scale <= 2.0) {
                    this.scale = event.scale;
                }
            });
            PinchGesture.onActionEnd(() => {
                if (this.scale < 1.0) {
                    Context.animateTo({ duration: 500, curve: Curve.Ease }, () => {
                        this.scale = 1.0;
                    });
                }
            });
            PinchGesture.pop();
            TapGesture.create({ count: 2, fingers: 1 });
            TapGesture.onAction(() => {
                if (this.visibility == Visibility.Hidden) {
                    Context.animateTo({ duration: 500, curve: Curve.Ease }, () => {
                        this.scale = this.scale < 2.0 ? 2.0 : 1.0;
                    });
                }
            });
            TapGesture.pop();
            GestureGroup.pop();
            Gesture.pop();
        }, item => item.name);
        ForEach.pop();
        Swiper.pop();
        Flex.pop();
        Column.create();
        Column.width('100%');
        Column.height(this.imageMargin);
        Column.backgroundColor('#F1F3F5');
        Column.visibility(this.visibility);
        Image.create({ "id": 16777242, "type": 20000, params: [] });
        Image.objectFit(ImageFit.Contain);
        Image.width(25);
        Image.height(25);
        Image.margin({ top: 5 });
        Text.create({ "id": 16777219, "type": 10003, params: [] });
        Text.fontSize(14);
        Text.fontColor(Color.Black);
        Text.textAlign(TextAlign.Start);
        Text.pop();
        Column.pop();
        Flex.pop();
    }
}
loadDocument(new ImageComponent("1", undefined, {}));

})();

/******/ })()
;
//# sourceMappingURL=image.js.map