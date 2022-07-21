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
/******/ 	return __webpack_require__(__webpack_require__.s = "../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.hml?entry");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.hml?entry":
/*!**************************************************************************************************************!*\
  !*** f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.hml?entry ***!
  \**************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!./detail.hml */ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!./detail.css */ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!./detail.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.js")

$app_define$('@app-component/detail', [], function($app_require$, $app_exports$, $app_module$) {

$app_script$($app_module$, $app_exports$, $app_require$)
if ($app_exports$.__esModule && $app_exports$.default) {
$app_module$.exports = $app_exports$.default
}

$app_module$.exports.template = $app_template$

$app_module$.exports.style = $app_style$

})
$app_bootstrap$('@app-component/detail',undefined,undefined)

/***/ }),

/***/ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.css":
/*!***********************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.css ***!
  \***********************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flexDirection": "column",
    "justifyContent": "center",
    "alignItems": "center",
    "backgroundColor": "#445164",
    "paddingBottom": "70px"
  },
  ".title": {
    "fontSize": "20px",
    "justifyContent": "center"
  },
  ".centerAre": {
    "flexDirection": "row",
    "marginTop": "15px"
  },
  ".centerLeft": {
    "flexDirection": "column",
    "justifyContent": "center",
    "marginLeft": "10px"
  },
  ".movieImg": {
    "height": "200px",
    "width": "260px",
    "marginTop": "10px",
    "marginLeft": "20px",
    "objectFit": "fill",
    "borderBottomLeftRadius": "20px",
    "borderBottomRightRadius": "20px",
    "borderTopLeftRadius": "20px",
    "borderTopRightRadius": "20px"
  },
  ".centerRight": {
    "flexDirection": "column",
    "justifyContent": "center",
    "marginLeft": "15px"
  },
  ".movieName": {
    "width": "140px",
    "marginLeft": "50px",
    "color": "#000000",
    "fontSize": "18px"
  },
  ".rightText": {
    "fontSize": "15px",
    "marginTop": "15px",
    "color": "#FFB4B7B3"
  },
  ".actorText": {
    "fontSize": "15px",
    "color": "#FFB4B7B3"
  },
  ".actor": {
    "marginTop": "5px",
    "flexDirection": "column"
  },
  ".actorsImg": {
    "width": "180px",
    "height": "60px",
    "marginTop": "5px",
    "objectFit": "cover",
    "flexDirection": "row"
  },
  ".actors": {
    "width": "60px",
    "height": "60px"
  },
  ".likeImg": {
    "marginLeft": "4px",
    "marginTop": "45px",
    "width": "30px",
    "height": "30px",
    "objectFit": "fill"
  },
  ".rate": {
    "marginTop": "15px",
    "flexDirection": "row",
    "marginLeft": "20px"
  },
  ".rateImg": {
    "width": "20px",
    "height": "20px"
  },
  ".rating": {
    "width": "200px",
    "fontSize": "20px",
    "marginLeft": "10px",
    "fontWeight": "300",
    "color": "#FF9800"
  },
  ".story": {
    "flexDirection": "column"
  },
  ".storyTitle": {
    "marginLeft": "150px",
    "color": "#000000",
    "fontSize": "16px",
    "marginTop": "25px",
    "justifyContent": "center"
  },
  ".storyContent": {
    "backgroundColor": "#415165",
    "color": "#000000",
    "fontSize": "16px",
    "marginTop": "5px",
    "textOverflow": "ellipsis",
    "marginLeft": "10px"
  },
  ".express": {
    "backgroundColor": "#f9efff",
    "radius": "10px",
    "marginLeft": "280px",
    "marginTop": "5px",
    "fontSize": "16px",
    "paddingTop": "5px",
    "paddingRight": "5px",
    "paddingBottom": "5px",
    "paddingLeft": "5px",
    "textColor": "#000000"
  },
  ".stills": {
    "flexDirection": "column"
  },
  ".stillsTitle": {
    "marginLeft": "160px",
    "fontSize": "16px",
    "marginTop": "25px",
    "color": "#000000"
  },
  ".stillsImg": {
    "marginLeft": "20px",
    "marginRight": "20px",
    "height": "100px",
    "marginTop": "10px",
    "objectFit": "cover",
    "flexDirection": "row"
  },
  ".stillsImgList": {
    "width": "100px",
    "height": "100px",
    "marginTop": "10px",
    "marginLeft": "1px",
    "marginBottom": "10px"
  }
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.hml":
/*!**************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.hml ***!
  \**************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "pages/index/detail:1",
    "className": "container"
  },
  "type": "div",
  "classList": [
    "container"
  ],
  "children": [
    {
      "attr": {
        "debugLine": "pages/index/detail:2",
        "className": "title",
        "value": function () {return this.title}
      },
      "type": "text",
      "classList": [
        "title"
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/detail:3",
        "className": "centerAre"
      },
      "type": "div",
      "classList": [
        "centerAre"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/detail:4",
            "src": function () {return this.imgUrl},
            "className": "movieImg"
          },
          "type": "image",
          "classList": [
            "movieImg"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:5",
            "className": "centerRight"
          },
          "type": "div",
          "classList": [
            "centerRight"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/detail:6",
                "className": "movieName",
                "value": function () {return this.movieName}
              },
              "type": "text",
              "classList": [
                "movieName"
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/detail:7",
                "className": "movieType"
              },
              "type": "div",
              "classList": [
                "movieType"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/detail:8",
                    "className": "rightText",
                    "value": function () {return this.movieType}
                  },
                  "type": "text",
                  "classList": [
                    "rightText"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/detail:9",
                    "className": "rightText",
                    "value": function () {return this.type}
                  },
                  "type": "text",
                  "classList": [
                    "rightText"
                  ]
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/detail:11",
                "className": "actor"
              },
              "type": "div",
              "classList": [
                "actor"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/detail:12",
                    "className": "actorText",
                    "value": function () {return this.actors}
                  },
                  "type": "text",
                  "classList": [
                    "actorText"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/detail:13",
                    "className": "actorsImg"
                  },
                  "type": "list",
                  "classList": [
                    "actorsImg"
                  ],
                  "children": [
                    {
                      "attr": {
                        "debugLine": "pages/index/detail:14"
                      },
                      "type": "list-item",
                      "repeat": function () {return this.actorsImg},
                      "children": [
                        {
                          "attr": {
                            "debugLine": "pages/index/detail:15",
                            "src": function () {return this.$item},
                            "className": "actors"
                          },
                          "type": "image",
                          "classList": [
                            "actors"
                          ]
                        }
                      ]
                    }
                  ]
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/detail:19",
                "src": "/common/images/like.png",
                "className": "likeImg"
              },
              "type": "image",
              "classList": [
                "likeImg"
              ]
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/detail:22",
        "className": "rate"
      },
      "type": "div",
      "classList": [
        "rate"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/detail:23",
            "src": "/common/images/rating.png",
            "className": "rateImg"
          },
          "type": "image",
          "classList": [
            "rateImg"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:24",
            "src": "/common/images/rating.png",
            "className": "rateImg"
          },
          "type": "image",
          "classList": [
            "rateImg"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:25",
            "src": "/common/images/rating.png",
            "className": "rateImg"
          },
          "type": "image",
          "classList": [
            "rateImg"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:26",
            "src": "/common/images/rating.png",
            "className": "rateImg"
          },
          "type": "image",
          "classList": [
            "rateImg"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:27",
            "src": "/common/images/rating1.png",
            "className": "rateImg"
          },
          "type": "image",
          "classList": [
            "rateImg"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:28",
            "className": "rating",
            "value": function () {return this.rating}
          },
          "type": "text",
          "classList": [
            "rating"
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/detail:30",
        "className": "story"
      },
      "type": "div",
      "classList": [
        "story"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/detail:31",
            "className": "storyTitle",
            "value": function () {return this.storyTitle}
          },
          "type": "text",
          "classList": [
            "storyTitle"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:32",
            "className": "storyContent",
            "value": function () {return this.introduction}
          },
          "type": "text",
          "classList": [
            "storyContent"
          ],
          "style": {
            "maxLines": function () {return this.maxLinesL}
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:33",
            "className": "express",
            "show": function () {return this.isShow},
            "value": function () {return this.expressText}
          },
          "type": "button",
          "classList": [
            "express"
          ],
          "onBubbleEvents": {
            "click": "showContent"
          }
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/detail:35",
        "className": "stills"
      },
      "type": "div",
      "classList": [
        "stills"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/detail:36",
            "className": "stillsTitle",
            "value": function () {return this.stillsTitle}
          },
          "type": "text",
          "classList": [
            "stillsTitle"
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/detail:37",
            "className": "stillsImg"
          },
          "type": "list",
          "classList": [
            "stillsImg"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/detail:38"
              },
              "type": "list-item",
              "repeat": function () {return this.actorsImg},
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/detail:39",
                    "src": function () {return this.$item},
                    "className": "stillsImgList"
                  },
                  "type": "image",
                  "classList": [
                    "stillsImgList"
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

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.js":
/*!*****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/detail.js ***!
  \*****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = function(module, exports, $app_require$){"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;
var _default = {
  data: {
    title: '电影',
    imgUrl: '',
    movieName: '',
    movieType: '电影类型：',
    type: '',
    actors: '主演',
    actorsImg: ['common/images/movies_image13.jpg', 'common/images/movies_image12.jpg', 'common/images/movies_image11.jpg', 'common/images/movies_image10.jpg'],
    rating: '',
    storyTitle: '剧情简介',
    introduction: '',
    expressText: '更多',
    flag: true,
    stillsTitle: '剧照',
    isShow: false,
    maxLinesL: 3
  },
  onInit: function onInit() {
    if (this.introduction.length > 60) {
      this.isShow = true;
    }
  },
  showContent: function showContent() {
    if (this.flag) {
      this.maxLinesL = 10;
      this.expressText = '收起';
      this.flag = false;
    } else {
      this.maxLinesL = 3;
      this.expressText = '更多';
      this.flag = true;
    }
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


/***/ })

/******/ });
//# sourceMappingURL=detail.js.map