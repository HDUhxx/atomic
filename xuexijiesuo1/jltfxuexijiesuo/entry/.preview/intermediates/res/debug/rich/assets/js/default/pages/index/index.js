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
/******/ 	return __webpack_require__(__webpack_require__.s = 7);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */,
/* 1 */,
/* 2 */,
/* 3 */,
/* 4 */,
/* 5 */,
/* 6 */,
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(8)
var $app_style$ = __webpack_require__(9)
var $app_script$ = __webpack_require__(10)

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
/* 8 */
/***/ (function(module, exports) {

module.exports = {
  "type": "div",
  "attr": {},
  "classList": [
    "box"
  ],
  "children": [
    {
      "type": "div",
      "attr": {},
      "classList": [
        "harder"
      ],
      "children": [
        {
          "type": "text",
          "attr": {
            "value": function () {return this.title}
          },
          "classList": [
            "title"
          ]
        }
      ]
    },
    {
      "type": "button",
      "attr": {
        "value": "了解更多"
      },
      "events": {
        "click": "page_two"
      },
      "classList": [
        "liaojie1"
      ]
    }
  ]
}

/***/ }),
/* 9 */
/***/ (function(module, exports) {

module.exports = {
  "@MEDIA": [
    {
      "condition": "screen and (device-type: phone)",
      ".box": {
        "backgroundImage": "/image/page2.jpg",
        "backgroundSize": "720px",
        "display": "flex",
        "justifyContent": "center"
      },
      ".liaojie1": {
        "textColor": "#000000",
        "backgroundColor": "#FFFF00",
        "width": "50%",
        "height": "100px",
        "textAlign": "center",
        "fontSize": "42px",
        "borderRadius": "45px",
        "fontWeight": "700",
        "marginTop": "900px"
      }
    },
    {
      "condition": "screen and (device-type: tv)",
      ".box": {
        "flexDirection": "column",
        "backgroundColor": "#0000FF"
      },
      ".harder": {
        "flexDirection": "column",
        "width": "100%",
        "height": "100px",
        "justifyContent": "center"
      },
      ".title": {
        "textAlign": "center",
        "fontSize": "48px",
        "color": "#FFFFFF",
        "fontWeight": "700"
      },
      ".center": {
        "flexDirection": "column",
        "width": "100%",
        "height": "200px",
        "justifyContent": "center"
      },
      ".block": {
        "width": "100%",
        "height": "100px"
      },
      ".text_box": {
        "flexDirection": "column",
        "width": "100%"
      },
      ".boxtxt": {
        "fontSize": "42px",
        "textAlign": "center",
        "fontWeight": "400",
        "color": "#FFFFFF"
      },
      ".button": {
        "width": "100%",
        "height": "50px",
        "justifyContent": "center"
      },
      ".button_style": {
        "width": "30%",
        "height": "50px",
        "textAlign": "center",
        "fontSize": "32px",
        "borderRadius": "45px",
        "fontWeight": "400",
        "backgroundColor": "#FFFF00",
        "color": "#FF0000"
      }
    }
  ]
}

/***/ }),
/* 10 */
/***/ (function(module, exports) {

module.exports = function(module, exports, $app_require$){"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _system = _interopRequireDefault($app_require$("@app-module/system.router"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

var _default = {
  data: {
    know: "了解更多"
  },
  page_two: function page_two() {
    _system["default"].push({
      uri: 'pages/interest/interest'
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
/******/ ]);
//# sourceMappingURL=index.js.map