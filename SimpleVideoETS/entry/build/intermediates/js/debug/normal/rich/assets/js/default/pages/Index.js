/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleVideoETS/entry/src/main/ets/default/common/data/VideoData.ets":
/*!******************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleVideoETS/entry/src/main/ets/default/common/data/VideoData.ets ***!
  \******************************************************************************************************************************/
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
exports.horizontalVideos = exports.swiperVideos = void 0;
const localSource = "/common/video/video1.mp4";
const webSource = "https://ss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/cae-legoup-video-target/93be3d88-9fc2-4fbd-bd14-833bca731ca7.mp4";
exports.swiperVideos = [
    {
        "image": '/common/image/video_ad0.jpg',
        "source": localSource
    },
    {
        "image": '/common/image/video_ad1.jpg',
        "source": webSource
    },
    {
        "image": '/common/image/video_ad2.jpg',
        "source": localSource
    }
];
exports.horizontalVideos = [
    {
        "image": '/common/image/video_list0.jpg',
        "source": webSource
    },
    {
        "image": '/common/image/video_list1.jpg',
        "source": localSource
    },
    {
        "image": '/common/image/video_list2.jpg',
        "source": webSource
    }
];


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
/*!**************************************************************************************************************************!*\
  !*** ../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleVideoETS/entry/src/main/ets/default/pages/Index.ets?entry ***!
  \**************************************************************************************************************************/

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
const VideoData_ets_1 = __webpack_require__(/*! ../common/data/VideoData.ets */ "../../../../../../Dome/git_dome/harmonyos_codelabs/SimpleVideoETS/entry/src/main/ets/default/common/data/VideoData.ets");
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
        Column.create();
        Column.backgroundColor("#EEEEEE");
        Column.padding({ left: 15, top: 15, right: 15, bottom: 15 });
        // 视频轮播组件
        Swiper.create();
        // 视频轮播组件
        Swiper.autoPlay(true);
        // 视频轮播组件
        Swiper.height(180);
        // 视频轮播组件
        Swiper.itemSpace(15);
        ForEach.create("3", this, ObservedObject.GetRawObject(VideoData_ets_1.swiperVideos), item => {
            let earlierCreatedChild_2 = this.findChildById("2");
            if (earlierCreatedChild_2 == undefined) {
                View.create(new SwiperItem("2", this, { imageSrc: item.image, source: item.source }));
            }
            else {
                earlierCreatedChild_2.updateWithValueParams({
                    imageSrc: item.image, source: item.source
                });
                if (!earlierCreatedChild_2.needsUpdate()) {
                    earlierCreatedChild_2.markStatic();
                }
                View.create(earlierCreatedChild_2);
            }
        }, item => item.image.toString());
        ForEach.pop();
        // 视频轮播组件
        Swiper.pop();
        // 视频列表上方的文本信息
        Flex.create({ direction: FlexDirection.Row });
        // 视频列表上方的文本信息
        Flex.margin({ top: 20, bottom: 15 });
        Text.create('Coming soon');
        Text.fontSize(20);
        Text.fontWeight(FontWeight.Bold);
        Text.margin({ left: 10 });
        Text.pop();
        Image.create('/common/image/next.png');
        Image.height(8);
        Image.width(16);
        // 视频列表上方的文本信息
        Flex.pop();
        // 视频列表组件
        List.create({ space: 15 });
        // 视频列表组件
        List.listDirection(Axis.Horizontal);
        ForEach.create("5", this, ObservedObject.GetRawObject(VideoData_ets_1.horizontalVideos), item => {
            ListItem.create();
            let earlierCreatedChild_4 = this.findChildById("4");
            if (earlierCreatedChild_4 == undefined) {
                View.create(new HorizontalItem("4", this, { imageSrc: item.image, source: item.source }));
            }
            else {
                earlierCreatedChild_4.updateWithValueParams({
                    imageSrc: item.image, source: item.source
                });
                if (!earlierCreatedChild_4.needsUpdate()) {
                    earlierCreatedChild_4.markStatic();
                }
                View.create(earlierCreatedChild_4);
            }
            ListItem.pop();
        }, item => item.image.toString());
        ForEach.pop();
        // 视频列表组件
        List.pop();
        Column.pop();
    }
}
class SwiperItem extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.imageSrc = undefined;
        this.source = undefined;
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.imageSrc !== undefined) {
            this.imageSrc = params.imageSrc;
        }
        if (params.source !== undefined) {
            this.source = params.source;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    render() {
        // 跳转一：使用Navigator组件跳转到视频播放界面
        Navigator.create({ target: 'pages/Play', type: NavigationType.Push });
        // 跳转一：使用Navigator组件跳转到视频播放界面
        Navigator.params({ source: this.source });
        Image.create(this.imageSrc);
        Image.objectFit(ImageFit.Cover);
        // 跳转一：使用Navigator组件跳转到视频播放界面
        Navigator.pop();
    }
}
class HorizontalItem extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.imageSrc = undefined;
        this.source = undefined;
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.imageSrc !== undefined) {
            this.imageSrc = params.imageSrc;
        }
        if (params.source !== undefined) {
            this.source = params.source;
        }
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id());
    }
    render() {
        // 跳转二：使用route跳转到视频播放界面
        Image.create(this.imageSrc);
        // 跳转二：使用route跳转到视频播放界面
        Image.width('80%');
        // 跳转二：使用route跳转到视频播放界面
        Image.height('25%');
        // 跳转二：使用route跳转到视频播放界面
        Image.onClick(() => {
            router.push({
                uri: 'pages/Play',
                params: { source: this.source }
            });
        });
    }
}
loadDocument(new Index("1", undefined, {}));

})();

/******/ })()
;
//# sourceMappingURL=Index.js.map