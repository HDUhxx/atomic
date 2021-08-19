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
/******/ 	return __webpack_require__(__webpack_require__.s = 15);
/******/ })
/************************************************************************/
/******/ ({

/***/ 15:
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(16)
var $app_style$ = __webpack_require__(17)
var $app_script$ = __webpack_require__(18)

$app_define$('@app-component/study', [], function($app_require$, $app_exports$, $app_module$) {

$app_script$($app_module$, $app_exports$, $app_require$)
if ($app_exports$.__esModule && $app_exports$.default) {
$app_module$.exports = $app_exports$.default
}

$app_module$.exports.template = $app_template$

$app_module$.exports.style = $app_style$

})
$app_bootstrap$('@app-component/study',undefined,undefined)

/***/ }),

/***/ 16:
/***/ (function(module, exports) {

module.exports = {
  "type": "div",
  "attr": {},
  "classList": [
    "pagediv"
  ],
  "children": [
    {
      "type": "div",
      "attr": {},
      "classList": [
        "topdiv"
      ],
      "children": [
        {
          "type": "text",
          "attr": {
            "value": function () {return this.$item}
          },
          "classList": [
            "textdiv"
          ],
          "repeat": function () {return this.title}
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "classList": [
        "middlediv"
      ],
      "children": [
        {
          "type": "block",
          "attr": {},
          "repeat": function () {return this.arrs},
          "children": [
            {
              "type": "div",
              "attr": {},
              "classList": [
                "box"
              ],
              "children": [
                {
                  "type": "button",
                  "attr": {
                    "value": function () {return this.$item.id}
                  },
                  "classList": [
                    "button_dj"
                  ],
                  "events": {
                    "click": function (evt) {this.show(this.$item.id,evt)}
                  }
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "type": "div",
      "attr": {
        "clss": "bottomdiv"
      },
      "children": [
        {
          "type": "div",
          "attr": {},
          "classList": [
            "div_dati"
          ],
          "children": [
            {
              "type": "text",
              "attr": {
                "value": function () {return this.show_text}
              },
              "classList": [
                "text_style"
              ]
            }
          ]
        }
      ]
    },
    {
      "type": "div",
      "attr": {},
      "classList": [
        "button_study"
      ],
      "children": [
        {
          "type": "text",
          "attr": {
            "value": function () {return this.know_study}
          },
          "classList": [
            "button_style_study"
          ],
          "events": {
            "click": "page_four"
          }
        }
      ]
    }
  ]
}

/***/ }),

/***/ 17:
/***/ (function(module, exports) {

module.exports = {
  "@MEDIA": [
    {
      "condition": "screen and (device-type: phone)",
      ".pagediv": {
        "width": "100%",
        "height": "1500px",
        "backgroundColor": "#FFF8DC",
        "display": "flex",
        "flexDirection": "column"
      },
      ".topdiv": {
        "width": "100%",
        "height": "10%",
        "backgroundColor": "#fad10a",
        "display": "flex",
        "justifyContent": "center",
        "alignItems": "center",
        "flexDirection": "column"
      },
      ".middlediv": {
        "width": "100%",
        "height": "30%",
        "backgroundColor": "#FAEBD7",
        "display": "flex",
        "justifyContent": "center",
        "alignItems": "center",
        "flexWrap": "wrap"
      },
      ".box": {
        "width": "31%",
        "height": "100px",
        "backgroundColor": "#0373fc",
        "borderTopWidth": "3px",
        "borderRightWidth": "3px",
        "borderBottomWidth": "3px",
        "borderLeftWidth": "3px",
        "borderStyle": "solid",
        "borderTopColor": "#000000",
        "borderRightColor": "#000000",
        "borderBottomColor": "#000000",
        "borderLeftColor": "#000000",
        "borderRadius": "20px",
        "marginTop": "30px",
        "marginRight": "5px",
        "marginBottom": "5px",
        "marginLeft": "5px",
        "display": "flex",
        "justifyContent": "center",
        "alignItems": "center"
      },
      ".textdiv": {
        "fontSize": "50px",
        "color": "#000000",
        "fontWeight": "bold"
      },
      ".boxtxt": {
        "fontSize": "45px",
        "color": "#000000",
        "fontWeight": "bold"
      },
      ".button_dj": {
        "backgroundColor": "#0373fc",
        "textColor": "#FFFFFF",
        "height": "100px",
        "width": "200px"
      },
      ".bottomdiv": {
        "backgroundColor": "#000000",
        "borderTopWidth": "2px",
        "borderRightWidth": "2px",
        "borderBottomWidth": "2px",
        "borderLeftWidth": "2px",
        "borderStyle": "solid",
        "borderTopColor": "#000000",
        "borderRightColor": "#000000",
        "borderBottomColor": "#000000",
        "borderLeftColor": "#000000",
        "width": "100px",
        "height": "400px",
        "justifyContent": "center"
      },
      ".div_dati": {
        "width": "90%",
        "height": "300px",
        "backgroundColor": "#0612f1",
        "marginTop": "150px",
        "marginLeft": "30px",
        "borderTopWidth": "2px",
        "borderRightWidth": "2px",
        "borderBottomWidth": "2px",
        "borderLeftWidth": "2px",
        "borderStyle": "solid",
        "borderTopColor": "#000000",
        "borderRightColor": "#000000",
        "borderBottomColor": "#000000",
        "borderLeftColor": "#000000",
        "opacity": 1,
        "display": "flex",
        "justifyContent": "center"
      },
      ".text_style": {
        "color": "#FFFFFF",
        "fontSize": "36px",
        "opacity": 1,
        "marginLeft": "5px",
        "textAlign": "left"
      },
      ".button_study": {
        "width": "100%",
        "height": "150px",
        "justifyContent": "center",
        "marginTop": "100px"
      },
      ".button_style_study": {
        "width": "45%",
        "height": "100px",
        "textAlign": "center",
        "fontSize": "42px",
        "borderRadius": "45px",
        "backgroundColor": "#FFFF00",
        "color": "#000000",
        "fontWeight": "400"
      }
    },
    {
      "condition": "screen and (device-type: tv)",
      ".pagediv": {
        "width": "100%",
        "height": "1080px",
        "backgroundColor": "#FFF8DC",
        "display": "flex",
        "flexDirection": "column"
      },
      ".topdiv": {
        "width": "100%",
        "height": "6%",
        "backgroundColor": "#AECAFA",
        "display": "flex",
        "justifyContent": "center",
        "alignItems": "center",
        "flexDirection": "column"
      },
      ".middlediv": {
        "width": "100%",
        "height": "17%",
        "backgroundColor": "#FAEBD7",
        "display": "flex",
        "justifyContent": "center",
        "alignItems": "center",
        "flexWrap": "wrap"
      },
      ".box": {
        "width": "20%",
        "height": "50px",
        "backgroundColor": "#008000",
        "borderTopWidth": "3px",
        "borderRightWidth": "3px",
        "borderBottomWidth": "3px",
        "borderLeftWidth": "3px",
        "borderStyle": "solid",
        "borderTopColor": "#000000",
        "borderRightColor": "#000000",
        "borderBottomColor": "#000000",
        "borderLeftColor": "#000000",
        "borderRadius": "20px",
        "marginTop": "5px",
        "marginRight": "5px",
        "marginBottom": "5px",
        "marginLeft": "5px",
        "display": "flex",
        "justifyContent": "center",
        "alignItems": "center"
      },
      ".textdiv": {
        "fontSize": "26px",
        "color": "#000000",
        "fontWeight": "bold"
      },
      ".boxtxt": {
        "fontSize": "45px",
        "color": "#000000",
        "fontWeight": "bold"
      },
      ".button_dj": {
        "backgroundColor": "#008000",
        "height": "100px",
        "width": "100px",
        "fontSize": "18px"
      },
      ".div_dati": {
        "width": "100%",
        "height": "85px",
        "backgroundColor": "#F5F5DC"
      },
      ".text_style": {
        "textColor": "#000000",
        "fontSize": "22px",
        "color": "#FF8C00"
      },
      ".button_study": {
        "width": "100%",
        "height": "50px",
        "justifyContent": "center"
      },
      ".button_style_study": {
        "width": "30%",
        "height": "50px",
        "textAlign": "center",
        "fontSize": "32px",
        "borderRadius": "45px",
        "backgroundColor": "#FFFF00",
        "color": "#FF0000",
        "fontWeight": "400"
      }
    }
  ]
}

/***/ }),

/***/ 18:
/***/ (function(module, exports) {

module.exports = function(module, exports, $app_require$){"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _system = _interopRequireDefault($app_require$("@app-module/system.router"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function _newArrowCheck(innerThis, boundThis) { if (innerThis !== boundThis) { throw new TypeError("Cannot instantiate an arrow function"); } }

var _default = {
  data: {
    title: ["点击按钮", "解锁术语解释与学习"],
    arrs: [{
      'id': 'Ability',
      'value': '应用的重要组成部分，是应用所具备能力的抽象。Ability分为两种类型，Feature Ability和Particle Ability。'
    }, {
      'id': 'AbilitySlice',
      'value': '切片，是单个可视化界面及其交互逻辑的总和，是Feature Ability的组成单元。一个Feature Ability可以包含一组业务关系密切的可视化界面，每一个可视化界面对应一个AbilitySlice。'
    }, {
      'id': 'ANS',
      'value': 'Advanced Notification Service，通知增强服务，是HarmonyOS中负责处理通知的订阅、发布和更新等操作的系统服务。'
    }, {
      'id': 'CES',
      'value': 'Common Event Service，是HarmonyOS中负责处理公共事件的订阅、发布和退订的系统服务。'
    }, {
      'id': 'DV',
      'value': 'Device Virtualization，设备虚拟化，通过虚拟化技术可以实现不同设备的能力和资源融合。'
    }, {
      'id': 'FA',
      'value': 'Feature Ability，元程序，代表有界面的Ability，用于与用户进行交互。'
    }, {
      'id': 'HAP',
      'value': 'HarmonyOS Ability Package，一个HAP文件包含应用的所有内容，由代码、资源、三方库及应用配置文件组成，其文件后缀名为.hap。'
    }, {
      'id': 'HDF',
      'value': 'Hardware Driver Foundation，硬件驱动框架，用于提供统一外设访问能力和驱动开发、管理框架。'
    }, {
      'id': 'IDN',
      'value': 'Intelligent Distributed Networking，是HarmonyOS特有的分布式组网能力单元。开发者可以通过IDN获取分布式网络内的设备列表和设备状态信息，以及注册分布式网络内设备的在网状态变化信息。'
    }, {
      'id': 'MSDP',
      'value': 'Mobile Sensing Development Platform，移动感知平台。MSDP子系统提供两类核心能力：分布式融合感知和分布式设备虚拟化。'
    }, {
      'id': 'PA',
      'value': 'Particle Ability，元服务，代表无界面的Ability，主要为Feature Ability提供支持，例如作为后台服务提供计算能力，或作为数据仓库提供数据访问能力。'
    }, {
      'id': 'Supervirtualdevice',
      'value': '超级虚拟终端亦称超级终端，通过分布式技术将多个终端的能力进行整合，存放在一个虚拟的硬件资源池里，根据业务需要统一管理和调度终端能力，来对外提供服务。'
    }],
    show_text: '',
    know_study: "了解更多"
  },
  show: function show(e) {
    var _this = this;

    console.info(e);
    var list = this;
    this.arrs.forEach(function (element) {
      _newArrowCheck(this, _this);

      console.info(element.id);

      if (element.id == e) {
        list.show_text = element.value;
      }
    }.bind(this));
    console.info(list.show_text);
  },
  page_four: function page_four() {
    _system["default"].push({
      uri: 'pages/harmonyos/harmonyos'
    });
  }
};
exports["default"] = _default;
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


/***/ })

/******/ });
//# sourceMappingURL=study.js.map