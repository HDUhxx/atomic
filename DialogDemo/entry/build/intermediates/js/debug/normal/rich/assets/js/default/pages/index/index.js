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
/******/ 	return __webpack_require__(__webpack_require__.s = "../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.hml?entry");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.hml?entry":
/*!************************************************************************************************************!*\
  !*** f:/Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.hml?entry ***!
  \************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!./index.hml */ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!./index.css */ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!./index.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.js")

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

/***/ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.css":
/*!*********************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!f:/Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.css ***!
  \*********************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".doc-page": {
    "height": "100%",
    "flexDirection": "column",
    "backgroundColor": "#E3f8F9",
    "justifyContent": "center",
    "alignItems": "center"
  },
  ".btn-div": {
    "width": "100%",
    "flexDirection": "column",
    "alignItems": "center",
    "justifyContent": "center"
  },
  ".btn": {
    "backgroundColor": "#17A98E",
    "marginTop": "15px",
    "width": "80%",
    "fontWeight": "bold"
  },
  ".btn-text": {
    "color": "#000000",
    "fontWeight": "bold",
    "fontSize": "39px"
  },
  ".dialog-main": {
    "width": "80%",
    "marginBottom": "40%"
  },
  ".dialog-div": {
    "flexDirection": "column",
    "alignItems": "center"
  },
  ".inner-txt": {
    "height": "120px",
    "flexDirection": "column",
    "alignItems": "center",
    "justifyContent": "space-around"
  },
  ".inner-btn": {
    "height": "80px",
    "justifyContent": "center",
    "alignItems": "center"
  },
  ".alert-inner-txt": {
    "height": "120px",
    "flexDirection": "column",
    "alignItems": "center",
    "justifyContent": "space-around"
  },
  ".alert-inner-btn": {
    "height": "80px",
    "justifyContent": "space-around",
    "alignItems": "center"
  },
  ".alert-dialog": {
    "width": "80%",
    "marginBottom": "40%"
  }
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.hml":
/*!************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!f:/Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.hml ***!
  \************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "pages/index/index:14",
    "className": "doc-page"
  },
  "type": "div",
  "classList": [
    "doc-page"
  ],
  "children": [
    {
      "attr": {
        "debugLine": "pages/index/index:15",
        "className": "btn-div"
      },
      "type": "div",
      "classList": [
        "btn-div"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:16",
            "type": "capsule",
            "value": "AlertDialog",
            "className": "btn"
          },
          "type": "button",
          "classList": [
            "btn"
          ],
          "onBubbleEvents": {
            "click": "showAlert"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:17",
            "type": "capsule",
            "value": "ConfirmDialog",
            "className": "btn"
          },
          "type": "button",
          "classList": [
            "btn"
          ],
          "onBubbleEvents": {
            "click": "showConfirm"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:18",
            "type": "capsule",
            "value": "LoadingDialog",
            "className": "btn"
          },
          "type": "button",
          "classList": [
            "btn"
          ],
          "onBubbleEvents": {
            "click": "showLoading"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:19",
            "type": "capsule",
            "value": "PromptDialog",
            "className": "btn"
          },
          "type": "button",
          "classList": [
            "btn"
          ],
          "onBubbleEvents": {
            "click": "showPrompt"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:20",
            "type": "capsule",
            "value": "ProgressDialog",
            "className": "btn"
          },
          "type": "button",
          "classList": [
            "btn"
          ],
          "onBubbleEvents": {
            "click": "showProgress"
          }
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:23",
        "id": "alertDialog",
        "className": "alert-dialog"
      },
      "type": "dialog",
      "id": "alertDialog",
      "classList": [
        "alert-dialog"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:24",
            "className": "dialog-div"
          },
          "type": "div",
          "classList": [
            "dialog-div"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:25",
                "className": "alert-inner-txt"
              },
              "type": "div",
              "classList": [
                "alert-inner-txt"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:26",
                    "className": "txt",
                    "value": "AlertDialog"
                  },
                  "type": "text",
                  "classList": [
                    "txt"
                  ]
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/index:28",
                "className": "alert-inner-btn"
              },
              "type": "div",
              "classList": [
                "alert-inner-btn"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:29",
                    "type": "capsule",
                    "value": "Confirm"
                  },
                  "type": "button",
                  "onBubbleEvents": {
                    "click": function (evt) {this.confirmClick('alertDialog',evt)}
                  },
                  "style": {
                    "width": "80%"
                  }
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:35",
        "id": "confirmDialog",
        "className": "dialog-main"
      },
      "type": "dialog",
      "id": "confirmDialog",
      "classList": [
        "dialog-main"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:36",
            "className": "dialog-div"
          },
          "type": "div",
          "classList": [
            "dialog-div"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:37",
                "className": "inner-txt"
              },
              "type": "div",
              "classList": [
                "inner-txt"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:38",
                    "className": "txt",
                    "value": "ConfirmDialog"
                  },
                  "type": "text",
                  "classList": [
                    "txt"
                  ]
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/index:40",
                "className": "inner-btn"
              },
              "type": "div",
              "classList": [
                "inner-btn"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:41",
                    "type": "capsule",
                    "value": "Cancel",
                    "className": "btn-txt"
                  },
                  "type": "button",
                  "style": {
                    "width": "40%",
                    "marginRight": "10px"
                  },
                  "classList": [
                    "btn-txt"
                  ],
                  "onBubbleEvents": {
                    "click": function (evt) {this.cancelClick('confirmDialog',evt)}
                  }
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:44",
                    "type": "capsule",
                    "value": "Confirm",
                    "className": "btn-txt"
                  },
                  "type": "button",
                  "style": {
                    "width": "40%"
                  },
                  "classList": [
                    "btn-txt"
                  ],
                  "onBubbleEvents": {
                    "click": function (evt) {this.confirmClick('confirmDialog',evt)}
                  }
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:51",
        "id": "loadingDialog"
      },
      "type": "dialog",
      "id": "loadingDialog",
      "style": {
        "width": "120px",
        "height": "120px",
        "marginBottom": "40%"
      },
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:52",
            "className": "dialog-div"
          },
          "type": "div",
          "classList": [
            "dialog-div"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:53",
                "className": "loading-img img-rotate",
                "id": "loading-img",
                "src": "/common/images/loading.svg"
              },
              "type": "image",
              "classList": [
                "loading-img",
                "img-rotate"
              ],
              "id": "loading-img",
              "style": {
                "height": "60px",
                "width": "60px"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:55",
                "value": "loading..."
              },
              "type": "text",
              "style": {
                "fontSize": "16px",
                "color": "#999999"
              }
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:59",
        "id": "promptDialog",
        "className": "dialog-main"
      },
      "type": "dialog",
      "id": "promptDialog",
      "classList": [
        "dialog-main"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:60",
            "className": "dialog-div"
          },
          "type": "div",
          "classList": [
            "dialog-div"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:61",
                "className": "inner-txt"
              },
              "type": "div",
              "classList": [
                "inner-txt"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:62",
                    "className": "txt",
                    "value": "PromptDialog"
                  },
                  "type": "text",
                  "classList": [
                    "txt"
                  ]
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/index:64",
                "type": "password",
                "placeholder": "please enter password"
              },
              "type": "input",
              "style": {
                "marginLeft": "20px",
                "marginRight": "20px"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:66",
                "className": "inner-btn"
              },
              "type": "div",
              "classList": [
                "inner-btn"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:67",
                    "type": "capsule",
                    "value": "Cancel",
                    "className": "btn-txt"
                  },
                  "type": "button",
                  "style": {
                    "width": "40%",
                    "marginRight": "10px"
                  },
                  "classList": [
                    "btn-txt"
                  ],
                  "onBubbleEvents": {
                    "click": function (evt) {this.cancelClick('promptDialog',evt)}
                  }
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:70",
                    "type": "capsule",
                    "value": "Confirm",
                    "className": "btn-txt"
                  },
                  "type": "button",
                  "style": {
                    "width": "40%"
                  },
                  "classList": [
                    "btn-txt"
                  ],
                  "onBubbleEvents": {
                    "click": function (evt) {this.confirmClick('promptDialog',evt)}
                  }
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:77",
        "id": "progressDialog"
      },
      "type": "dialog",
      "id": "progressDialog",
      "style": {
        "width": "80%",
        "height": "200px",
        "marginBottom": "40%"
      },
      "events": {
        "cancel": "onCancel"
      },
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:78"
          },
          "type": "div",
          "style": {
            "flexDirection": "column",
            "alignItems": "center"
          },
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:79",
                "value": "Downloading..."
              },
              "type": "text",
              "style": {
                "fontSize": "25px",
                "color": "#000000",
                "fontWeight": "bold"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:80"
              },
              "type": "div",
              "style": {
                "width": "80%",
                "marginTop": "40px",
                "marginBottom": "10px"
              },
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:81",
                    "className": "min-progress",
                    "type": "horizontal",
                    "percent": function () {return this.percent},
                    "secondarypercent": "50"
                  },
                  "type": "progress",
                  "classList": [
                    "min-progress"
                  ],
                  "style": {
                    "height": "10px"
                  }
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/index:84",
                "value": function () {return decodeURI('Image%20') + (this.percent/10) + decodeURI('%20of%2010')}
              },
              "type": "text",
              "style": {
                "fontSize": "18px",
                "color": "#666666"
              }
            }
          ]
        }
      ]
    }
  ]
}

/***/ }),

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.js":
/*!***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!f:/Dome/git_dome/harmonyos_codelabs/DialogDemo/entry/src/main/js/default/pages/index/index.js ***!
  \***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){"use strict";

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js");

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _system = _interopRequireDefault(requireModule("@system.prompt"));

var _default = {
  data: {
    percent: 0,
    interval: ''
  },
  showAlert: function showAlert() {
    this.$element('alertDialog').show();
  },
  showConfirm: function showConfirm() {
    this.$element('confirmDialog').show();
  },
  showLoading: function showLoading() {
    var options = {
      duration: 800,
      easing: 'linear',
      iterations: 'Infinity'
    };
    var frames = [{
      transform: {
        rotate: '0deg'
      }
    }, {
      transform: {
        rotate: '360deg'
      }
    }];
    this.animation = this.$element('loading-img').animate(frames, options);
    this.$element('loadingDialog').show();
    this.animation.play();
  },
  showPrompt: function showPrompt() {
    this.$element('promptDialog').show();
  },
  showProgress: function showProgress() {
    var that = this;
    that.percent = 0;
    this.$element('progressDialog').show();
    this.interval = setInterval(function () {
      that.percent += 10;

      if (that.percent >= 100) {
        clearInterval(that.interval);
      }
    }, 500);
  },
  confirmClick: function confirmClick(id) {
    this.$element(id).close();

    _system["default"].showToast({
      message: 'confirm clicked'
    });
  },
  cancelClick: function cancelClick(id) {
    this.$element(id).close();

    _system["default"].showToast({
      message: 'cancel clicked'
    });
  },
  onCancel: function onCancel() {
    clearInterval(this.interval);
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