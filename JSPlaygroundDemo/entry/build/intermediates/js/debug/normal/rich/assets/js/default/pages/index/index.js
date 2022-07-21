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
/******/ 	return __webpack_require__(__webpack_require__.s = "../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.hml?entry");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.hml?entry":
/*!******************************************************************************************************************!*\
  !*** f:/Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.hml?entry ***!
  \******************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!./index.hml */ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!./index.css */ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!./index.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.js")

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

/***/ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.css":
/*!***************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!f:/Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.css ***!
  \***************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flexDirection": "column",
    "backgroundColor": "#000000"
  },
  ".title": {
    "fontWeight": "600",
    "color": "#cccccc",
    "backgroundColor": "#000000"
  },
  ".tag-list": {
    "width": "100%"
  },
  ".todo-list-item": {
    "width": "100%"
  },
  ".todo-item": {
    "height": "120px",
    "width": "100%",
    "borderBottomLeftRadius": "5px",
    "borderBottomRightRadius": "5px",
    "borderTopLeftRadius": "5px",
    "borderTopRightRadius": "5px",
    "alignItems": "center"
  },
  ".flex-row": {
    "flexDirection": "row",
    "alignItems": "center"
  },
  ".todo-name-mark": {
    "width": "100%",
    "height": "100%",
    "alignItems": "center"
  },
  ".todo-name": {
    "fontSize": "32px",
    "color": "#FFFFFF",
    "height": "100px",
    "marginRight": "5px",
    "maxLines": 1,
    "textOverflow": "ellipsis"
  },
  ".text-default": {
    "color": "#FFFFFF"
  },
  ".text-gray": {
    "color": "#808080"
  },
  ".todo-mark": {
    "width": "18px",
    "height": "30%",
    "marginLeft": "16px",
    "borderBottomLeftRadius": "50px",
    "borderBottomRightRadius": "50px",
    "borderTopLeftRadius": "50px",
    "borderTopRightRadius": "50px",
    "backgroundColor": "#778899"
  },
  ".todo-time": {
    "fontSize": "32px",
    "width": "100%",
    "height": "100%",
    "textAlign": "left",
    "color": "#808080",
    "marginTop": "2px"
  },
  ".urgent": {
    "backgroundColor": "#B22222"
  },
  ".senior": {
    "backgroundColor": "#FFD700"
  },
  ".middle": {
    "backgroundColor": "#66CDAA"
  },
  ".low": {
    "backgroundColor": "#0D9FFB"
  },
  ".hide": {
    "display": "none"
  },
  ".show": {
    "display": "flex"
  },
  ".todo-image": {
    "width": "50px",
    "height": "50%",
    "objectFit": "contain",
    "marginLeft": "10px",
    "marginTop": "3px"
  },
  ".todo-text-wrapper": {
    "height": "100%",
    "flexGrow": 1,
    "marginTop": "0px",
    "marginRight": "32px",
    "marginBottom": "0px",
    "marginLeft": "32px",
    "flexDirection": "column"
  },
  "@MEDIA": [
    {
      "condition": "(device-type: tv)",
      ".todo-item": {
        "height": "50px"
      },
      ".todo-name": {
        "fontSize": "18px"
      },
      ".todo-time": {
        "fontSize": "16px"
      },
      ".todo-mark": {
        "width": "10px",
        "height": "10px"
      }
    },
    {
      "condition": "(device-type: phone)",
      ".title": {
        "fontSize": "42px",
        "marginTop": "36px",
        "marginBottom": "36px",
        "marginLeft": "24px"
      }
    },
    {
      "condition": "(device-type: wearable)",
      ".title": {
        "fontSize": "52px",
        "height": "20%",
        "textAlign": "center"
      },
      ".todo-list-item": {
        "paddingLeft": "100px",
        "paddingRight": "50px"
      }
    }
  ]
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.hml":
/*!******************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!f:/Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.hml ***!
  \******************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "pages/index/index:14",
    "className": "container"
  },
  "type": "div",
  "classList": [
    "container"
  ],
  "children": [
    {
      "attr": {
        "debugLine": "pages/index/index:15",
        "className": "tag-list",
        "initialindex": function () {return this.initialIndex}
      },
      "type": "list",
      "classList": [
        "tag-list"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:16",
            "className": "todo-list-item",
            "focusable": "false"
          },
          "type": "list-item",
          "repeat": function () {return this.taskList},
          "classList": [
            "todo-list-item"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:17",
                "className": "todo-item flex-row"
              },
              "type": "div",
              "classList": [
                "todo-item",
                "flex-row"
              ],
              "onBubbleEvents": {
                "click": function (evt) {this.completeEvent(this.$item.id,evt)}
              },
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:18",
                    "className": "todo-image",
                    "src": function () {return this.$item.checkBtn}
                  },
                  "type": "image",
                  "classList": [
                    "todo-image"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:19",
                    "className": "todo-text-wrapper"
                  },
                  "type": "div",
                  "classList": [
                    "todo-text-wrapper"
                  ],
                  "children": [
                    {
                      "attr": {
                        "debugLine": "pages/index/index:20",
                        "className": "todo-name-mark"
                      },
                      "type": "div",
                      "classList": [
                        "todo-name-mark"
                      ],
                      "children": [
                        {
                          "attr": {
                            "debugLine": "pages/index/index:21",
                            "className": "todo-name {{$item.color}}",
                            "focusable": "false",
                            "value": function () {return this.$item.event}
                          },
                          "type": "text",
                          "classList": function () {return ['todo-name', this.$item.color]}
                        },
                        {
                          "attr": {
                            "debugLine": "pages/index/index:22",
                            "className": "todo-mark {{$item.tag}} {{$item.showTag}}"
                          },
                          "type": "text",
                          "classList": function () {return ['todo-mark', this.$item.tag, this.$item.showTag]}
                        }
                      ]
                    },
                    {
                      "attr": {
                        "debugLine": "pages/index/index:24",
                        "className": "todo-time",
                        "value": function () {return this.$item.time}
                      },
                      "type": "text",
                      "classList": [
                        "todo-time"
                      ]
                    }
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}

/***/ }),

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.js":
/*!*********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!f:/Dome/git_dome/harmonyos_codelabs/JSPlaygroundDemo/entry/src/main/js/default/pages/index/index.js ***!
  \*********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){"use strict";

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js");

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _system = _interopRequireDefault(requireModule("@system.device"));

function _createForOfIteratorHelper(o) { if (typeof Symbol === "undefined" || o[Symbol.iterator] == null) { if (Array.isArray(o) || (o = _unsupportedIterableToArray(o))) { var i = 0; var F = function F() {}; return { s: F, n: function n() { if (i >= o.length) return { done: true }; return { done: false, value: o[i++] }; }, e: function e(_e) { throw _e; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); } var it, normalCompletion = true, didErr = false, err; return { s: function s() { it = o[Symbol.iterator](); }, n: function n() { var step = it.next(); normalCompletion = step.done; return step; }, e: function e(_e2) { didErr = true; err = _e2; }, f: function f() { try { if (!normalCompletion && it["return"] != null) it["return"](); } finally { if (didErr) throw err; } } }; }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

var BUTTON_STATE_IMAGE = ['/common/checkbutton.png', '/common/done.png'];
var TAG_STATE = ['show', 'hide'];
var TEXT_COLOR = ['text-default', 'text-gray'];
var EVENT_LEVEL = ['urgent', 'senior', 'middle', 'low'];
var _default = {
  data: {
    title: "",
    eventList: ["拿快递", "购买礼物", "约会/安排", "回复邮件", "运动健身", "足球比赛", "看书学习"],
    initialIndex: 0,
    taskList: []
  },
  onInit: function onInit() {
    this.$set('taskList', []);
  },
  onShow: function onShow() {
    for (var index = 0; index < this.eventList.length; index++) {
      var element = {};
      element.id = 'id-' + index;
      element.event = this.eventList[index];
      element.time = this.getRandomTime();
      var completeState = this.getRandom(100) % 2;
      element.checkBtn = BUTTON_STATE_IMAGE[completeState];
      element.color = TEXT_COLOR[completeState];
      element.showTag = TAG_STATE[completeState];
      element.tag = EVENT_LEVEL[this.getRandom(EVENT_LEVEL.length)];
      this.taskList.push(element);
    }

    var _this = this;

    _system["default"].getInfo({
      success: function success(data) {
        if (data.deviceType && data.deviceType === 'wearable') {
          _this.initialIndex = 2;
        }
      }
    });
  },
  completeEvent: function completeEvent(e) {
    var _iterator = _createForOfIteratorHelper(this.taskList),
        _step;

    try {
      for (_iterator.s(); !(_step = _iterator.n()).done;) {
        var i = _step.value;

        if (i.id == e) {
          if (i.checkBtn == "/common/done.png") {
            i.checkBtn = "/common/checkbutton.png";
            i.showTag = 'show';
            i.color = 'text-default';
            i.completeState = false;
          } else {
            i.checkBtn = "/common/done.png";
            i.showTag = 'hide';
            i.color = 'text-gray';
            i.completeState = true;
          }

          return;
        }
      }
    } catch (err) {
      _iterator.e(err);
    } finally {
      _iterator.f();
    }
  },
  getRandomTime: function getRandomTime() {
    var hour = this.getRandom(24);
    var minute = this.getRandom(60);

    if (minute < 10) {
      minute = '0' + minute;
    }

    return hour + ':' + minute;
  },
  getRandom: function getRandom(range) {
    var num = Math.random();
    num = num * range;
    num = Math.floor(num);
    return num;
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