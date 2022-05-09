/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.hml?entry");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.hml?entry":
/*!**********************************************************************************************************!*\
  !*** f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.hml?entry ***!
  \**********************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

__webpack_require__(/*! !../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/loader.js!../../common/component/continuous/ContinuousScrollableImageView.hml?name=continuous-drawable */ "./lib/loader.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.hml?name=continuous-drawable")
var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!./index.hml */ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!./index.css */ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!./index.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.js")

$app_define$('@app-component/index', [], function($app_require$, $app_exports$, $app_module$) {

$app_script$($app_module$, $app_exports$, $app_require$)
if ($app_exports$.__esModule && $app_exports$.default) {
$app_module$.exports = $app_exports$.default
}

$app_module$.exports.template = $app_template$

$app_module$.exports.style = $app_style$

})
$app_bootstrap$('@app-component/index',undefined,undefined)

/***/ }),

/***/ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.css":
/*!***********************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.css ***!
  \***********************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flexDirection": "column"
  },
  ".simpleSize": {
    "objectFit": "none"
  },
  ".simpleAnimation": {
    "animationDuration": "9s",
    "animationDelay": "0ms",
    "animationDirection": "normal",
    "animationTimingFunction": "ease",
    "animationPlayState": "running",
    "animationIterationCount": -1,
    "animationFillMode": "none",
    "animationName": "simpleFrames"
  },
  "@KEYFRAMES": {
    "simpleFrames": [
      {
        "transform": "{\"translateX\":\"0px\"}",
        "time": 0
      },
      {
        "transform": "{\"translateX\":\"900px\"}",
        "time": 100
      }
    ]
  }
}

/***/ }),

/***/ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.css":
/*!*******************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.css ***!
  \*******************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "width": "100%",
    "flexDirection": "column",
    "justifyContent": "center",
    "backgroundColor": "#E7F4F7",
    "height": "100%"
  },
  ".plane": {
    "marginTop": "80%",
    "width": "100%",
    "height": "100%"
  },
  ".cloud": {
    "marginTop": "50%",
    "width": "100%",
    "height": "100%"
  },
  ".mountain": {
    "width": "100%",
    "height": "100%",
    "bottom": "5%"
  },
  ".simpleSize": {
    "backgroundColor": "#0000FF",
    "width": "100px",
    "height": "100px"
  }
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.hml":
/*!**************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.hml ***!
  \**************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "common/component/continuous/ContinuousScrollableImageView:1",
    "ref": "stack_id",
    "id": "refId"
  },
  "type": "stack",
  "id": "refId",
  "children": [
    {
      "attr": {
        "debugLine": "common/component/continuous/ContinuousScrollableImageView:2",
        "id": "img",
        "src": function () {return this.srcRes}
      },
      "type": "image",
      "id": "img",
      "style": {
        "width": function () {return this.widthPx},
        "height": function () {return this.heightPx},
        "transform": function () {return this.translate1},
        "objectFit": function () {return this.scaleMode}
      }
    },
    {
      "attr": {
        "debugLine": "common/component/continuous/ContinuousScrollableImageView:9",
        "id": "img",
        "src": function () {return this.srcRes}
      },
      "type": "image",
      "id": "img",
      "style": {
        "width": function () {return this.widthPx},
        "height": function () {return this.heightPx},
        "transform": function () {return this.translate2},
        "objectFit": function () {return this.scaleMode}
      }
    }
  ]
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.hml":
/*!**********************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.hml ***!
  \**********************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "pages/index/index:2",
    "className": "container"
  },
  "type": "div",
  "classList": [
    "container"
  ],
  "children": [
    {
      "attr": {
        "debugLine": "pages/index/index:3",
        "className": "plane",
        "srcResource": "common/images/plane.png",
        "srcHeight": "100px",
        "srcWidth": "300px",
        "scaleStyle": "contain",
        "scaleDuration": "9000",
        "scaleDirection": "normal",
        "scaleOrientation": "h"
      },
      "type": "continuous-drawable",
      "classList": [
        "plane"
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:13",
        "className": "cloud",
        "srcResource": "common/images/cloud.png",
        "srcHeight": "800px",
        "srcWidth": "800px",
        "scaleStyle": "contain",
        "scaleDuration": "5000",
        "scaleDirection": "reverse"
      },
      "type": "continuous-drawable",
      "classList": [
        "cloud"
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:22",
        "className": "mountain",
        "srcResource": "common/images/mountain.png",
        "srcHeight": "300px",
        "srcWidth": "1080",
        "scaleStyle": "contain",
        "scaleDuration": "6000",
        "scaleDirection": "reverse"
      },
      "type": "continuous-drawable",
      "classList": [
        "mountain"
      ]
    }
  ]
}

/***/ }),

/***/ "./lib/loader.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.hml?name=continuous-drawable":
/*!************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/loader.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.hml?name=continuous-drawable ***!
  \************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!./ContinuousScrollableImageView.hml */ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!./ContinuousScrollableImageView.css */ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader?presets[]=f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!./ContinuousScrollableImageView.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=f:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=f:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.js")

$app_define$('@app-component/continuous-drawable', [], function($app_require$, $app_exports$, $app_module$) {

$app_script$($app_module$, $app_exports$, $app_require$)
if ($app_exports$.__esModule && $app_exports$.default) {
$app_module$.exports = $app_exports$.default
}

$app_module$.exports.template = $app_template$

$app_module$.exports.style = $app_style$

})


/***/ }),

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.js":
/*!*************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/pages/index/index.js ***!
  \*************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){"use strict";

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js");

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _ohos = _interopRequireDefault(requireModule("@ohos.animator"));

var _default = {
  data: {
    translateX: 0,
    animator: null
  },
  computed: {
    translateXPx: function translateXPx() {
      return this.translateX + 'px';
    },
    translateX1Px: function translateX1Px() {
      return this.translateX - 1080 + 'px';
    }
  },
  onInit: function onInit() {
    var options = {
      duration: 5000,
      easing: 'ease-in-out',
      fill: 'forwards',
      iterations: 10000,
      begin: 0,
      end: 1080
    };
    this.animator = _ohos["default"].createAnimator(options);
    var that = this;

    this.animator.onframe = function (value) {
      that.translateX = value;
    };
  },
  toggleState: function toggleState() {
    this.animator.play();
  }
};
exports["default"] = _default;

function requireModule(moduleName) {
  const systemList = ['system.router', 'system.app', 'system.prompt', 'system.configuration',
  'system.image', 'system.device', 'system.mediaquery', 'ohos.animator', 'system.grid', 'system.resource']
  var target = ''
  if (systemList.includes(moduleName.replace('@', ''))) {
    target = $app_require$('@app-module/' + moduleName.substring(1));
    return target;
  }
  var shortName = moduleName.replace(/@[^.]+.([^.]+)/, '$1');
  if (typeof ohosplugin !== 'undefined' && /@ohos/.test(moduleName)) {
    target = ohosplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  if (typeof systemplugin !== 'undefined') {
    target = systemplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  target = requireNapi(shortName);
  return target;
}

var moduleOwn = exports.default || module.exports;
var accessors = ['public', 'protected', 'private'];
if (moduleOwn.data && accessors.some(function (acc) {
    return moduleOwn[acc];
  })) {
  throw new Error('For VM objects, attribute data must not coexist with public, protected, or private. Please replace data with public.');
} else if (!moduleOwn.data) {
  moduleOwn.data = {};
  moduleOwn._descriptor = {};
  accessors.forEach(function(acc) {
    var accType = typeof moduleOwn[acc];
    if (accType === 'object') {
      moduleOwn.data = Object.assign(moduleOwn.data, moduleOwn[acc]);
      for (var name in moduleOwn[acc]) {
        moduleOwn._descriptor[name] = {access : acc};
      }
    } else if (accType === 'function') {
      console.warn('For VM objects, attribute ' + acc + ' value must not be a function. Change the value to an object.');
    }
  });
}}
/* generated by ace-loader */


/***/ }),

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=f:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=f:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.js":
/*!*****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!f:/Dome/git_dome/ContinuousScrollableImageJS/entry/src/main/js/default/common/component/continuous/ContinuousScrollableImageView.js ***!
  \*****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){"use strict";

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js");

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _ohos = _interopRequireDefault(requireModule("@ohos.animator"));

var _default = {
  props: {
    srcResource: {
      "default": "common/images/cloud.png"
    },
    srcWidth: {
      "default": 100
    },
    srcHeight: {
      "default": 100
    },
    scaleStyle: {
      "default": "contain"
    },
    scaleDuration: {
      "default": 3000
    },
    scaleDirection: {
      "default": "normal"
    },
    scaleOrientation: {
      "default": "h"
    }
  },
  data: {
    compWidth: 0,
    compHeight: 0,
    translateFirst: 0,
    translateSecond: 0,
    translateLength: 0,
    scaleMode: "none",
    duration: 3000,
    direction: "normal",
    orientation: "h",
    srcRes: "common/images/cloud.png",
    animator: null,
    test: "translateX("
  },
  computed: {
    widthPx: function widthPx() {
      return this.compWidth + 'px';
    },
    heightPx: function heightPx() {
      return this.compHeight + 'px';
    },
    translate1: function translate1() {
      return this.test + this.translateFirst + 'px' + ')';
    },
    translate2: function translate2() {
      return this.test + this.translateSecond + 'px' + ')';
    }
  },
  onInit: function onInit() {
    console.error("AAA - onInit");
  },
  onAttached: function onAttached() {
    this.srcRes = this.srcResource;
    this.compWidth = parseInt(this.srcWidth.replace('px', ''));
    this.compHeight = parseInt(this.srcHeight.replace('px', ''));
    this.scaleMode = this.scaleStyle;
    this.duration = this.scaleDuration;
    this.direction = this.scaleDirection;
    this.orientation = this.scaleOrientation.toLowerCase();
  },
  onPageShow: function onPageShow() {
    var rect = this.$element("refId").getBoundingClientRect();

    if (this.orientation == 'v') {
      this.test = "translateY(";
      this.translateLength = rect.height;
    } else {
      this.test = "translateX(";
      this.translateLength = rect.width;
    }

    var options = {
      duration: this.duration,
      easing: 'linear',
      iterations: Number.MAX_VALUE,
      begin: 0,
      direction: this.direction,
      end: this.translateLength
    };
    this.animator = _ohos["default"].createAnimator(options);
    var that = this;

    this.animator.onframe = function (value) {
      that.translateFirst = value;
      that.translateSecond = value - that.translateLength;
    };

    this.animator.play();
  }
};
exports["default"] = _default;

function requireModule(moduleName) {
  const systemList = ['system.router', 'system.app', 'system.prompt', 'system.configuration',
  'system.image', 'system.device', 'system.mediaquery', 'ohos.animator', 'system.grid', 'system.resource']
  var target = ''
  if (systemList.includes(moduleName.replace('@', ''))) {
    target = $app_require$('@app-module/' + moduleName.substring(1));
    return target;
  }
  var shortName = moduleName.replace(/@[^.]+.([^.]+)/, '$1');
  if (typeof ohosplugin !== 'undefined' && /@ohos/.test(moduleName)) {
    target = ohosplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  if (typeof systemplugin !== 'undefined') {
    target = systemplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  target = requireNapi(shortName);
  return target;
}

var moduleOwn = exports.default || module.exports;
var accessors = ['public', 'protected', 'private'];
if (moduleOwn.data && accessors.some(function (acc) {
    return moduleOwn[acc];
  })) {
  throw new Error('For VM objects, attribute data must not coexist with public, protected, or private. Please replace data with public.');
} else if (!moduleOwn.data) {
  moduleOwn.data = {};
  moduleOwn._descriptor = {};
  accessors.forEach(function(acc) {
    var accType = typeof moduleOwn[acc];
    if (accType === 'object') {
      moduleOwn.data = Object.assign(moduleOwn.data, moduleOwn[acc]);
      for (var name in moduleOwn[acc]) {
        moduleOwn._descriptor[name] = {access : acc};
      }
    } else if (accType === 'function') {
      console.warn('For VM objects, attribute ' + acc + ' value must not be a function. Change the value to an object.');
    }
  });
}}
/* generated by ace-loader */


/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js":
/*!**********************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/interopRequireDefault.js ***!
  \**********************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    "default": obj
  };
}

module.exports = _interopRequireDefault;

function requireModule(moduleName) {
  const systemList = ['system.router', 'system.app', 'system.prompt', 'system.configuration',
  'system.image', 'system.device', 'system.mediaquery', 'ohos.animator', 'system.grid', 'system.resource']
  var target = ''
  if (systemList.includes(moduleName.replace('@', ''))) {
    target = $app_require$('@app-module/' + moduleName.substring(1));
    return target;
  }
  var shortName = moduleName.replace(/@[^.]+.([^.]+)/, '$1');
  if (typeof ohosplugin !== 'undefined' && /@ohos/.test(moduleName)) {
    target = ohosplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  if (typeof systemplugin !== 'undefined') {
    target = systemplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  target = requireNapi(shortName);
  return target;
}


/***/ })

/******/ });
//# sourceMappingURL=index.js.map