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
    title: ["????????????", "???????????????????????????"],
    arrs: [{
      'id': 'Ability',
      'value': '??????????????????????????????????????????????????????????????????Ability?????????????????????Feature Ability???Particle Ability???'
    }, {
      'id': 'AbilitySlice',
      'value': '??????????????????????????????????????????????????????????????????Feature Ability????????????????????????Feature Ability?????????????????????????????????????????????????????????????????????????????????????????????AbilitySlice???'
    }, {
      'id': 'ANS',
      'value': 'Advanced Notification Service???????????????????????????HarmonyOS???????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'CES',
      'value': 'Common Event Service??????HarmonyOS????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'DV',
      'value': 'Device Virtualization?????????????????????????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'FA',
      'value': 'Feature Ability?????????????????????????????????Ability?????????????????????????????????'
    }, {
      'id': 'HAP',
      'value': 'HarmonyOS Ability Package?????????HAP?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????.hap???'
    }, {
      'id': 'HDF',
      'value': 'Hardware Driver Foundation?????????????????????????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'IDN',
      'value': 'Intelligent Distributed Networking??????HarmonyOS????????????????????????????????????????????????????????????IDN?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'MSDP',
      'value': 'Mobile Sensing Development Platform????????????????????????MSDP???????????????????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'PA',
      'value': 'Particle Ability?????????????????????????????????Ability????????????Feature Ability????????????????????????????????????????????????????????????????????????????????????????????????????????????'
    }, {
      'id': 'Supervirtualdevice',
      'value': '??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????'
    }],
    show_text: '',
    know_study: "????????????"
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