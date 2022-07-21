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
/******/ 	return __webpack_require__(__webpack_require__.s = "../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.hml?entry");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.hml?entry":
/*!*************************************************************************************************************!*\
  !*** f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.hml?entry ***!
  \*************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!./index.hml */ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!./index.css */ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../../HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!./index.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.js")

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

/***/ "./lib/json.js!./lib/style.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.css":
/*!**********************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/style.js!f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.css ***!
  \**********************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flexDirection": "column",
    "justifyContent": "flex-start",
    "alignItems": "center"
  },
  ".top": {
    "width": "100%",
    "height": "30%",
    "backgroundImage": "/common/images/bg.jpg",
    "backgroundSize": "cover",
    "flexDirection": "column",
    "backgroundRepeat": "no-repeat",
    "justifyContent": "flex-start",
    "alignItems": "center"
  },
  ".title": {
    "marginTop": "10px",
    "fontSize": "25px",
    "color": "#FF7E7A7A"
  },
  ".titleDes": {
    "marginTop": "10px",
    "fontSize": "15px",
    "color": "#17A98E"
  },
  ".bottom": {
    "width": "100%",
    "height": "70%",
    "flexDirection": "column",
    "justifyContent": "flex-start",
    "alignItems": "center"
  },
  ".listItem": {
    "width": "100%",
    "height": "110px",
    "flexDirection": "column",
    "marginTop": "10px"
  },
  ".items": {
    "flexDirection": "row"
  },
  ".movieIndex": {
    "color": "#FF0000",
    "width": "24px",
    "height": "100%",
    "marginLeft": "15px",
    "marginRight": "15px",
    "fontSize": "20px"
  },
  ".movieImg": {
    "width": "150px",
    "height": "100px",
    "objectFit": "fill"
  },
  ".movieInfo": {
    "width": "150px",
    "height": "100%",
    "flexDirection": "column"
  },
  ".movieName": {
    "fontSize": "18px",
    "marginLeft": "10px"
  },
  ".rate": {
    "marginTop": "5px",
    "marginLeft": "10px",
    "flexDirection": "row"
  },
  ".rateImage": {
    "width": "30px",
    "height": "30px",
    "objectFit": "contain"
  },
  ".rateText": {
    "width": "200px",
    "marginLeft": "10px",
    "fontSize": "12px",
    "color": "#FF9800",
    "fontWeight": "normal"
  },
  ".genre": {
    "marginLeft": "10px",
    "color": "#FF81807E",
    "fontSize": "12px",
    "fontWeight": "lighter"
  },
  ".like": {
    "width": "50px",
    "marginLeft": "5px",
    "flexDirection": "column"
  },
  ".likeImg": {
    "width": "30px",
    "height": "30px"
  },
  ".likeText": {
    "width": "30px",
    "height": "30px",
    "fontSize": "13px",
    "color": "#FFF88B06"
  }
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.hml":
/*!*************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/json.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/template.js!f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.hml ***!
  \*************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "pages/index/index:1",
    "className": "container"
  },
  "type": "div",
  "classList": [
    "container"
  ],
  "children": [
    {
      "attr": {
        "debugLine": "pages/index/index:2",
        "className": "top"
      },
      "type": "div",
      "classList": [
        "top"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:3",
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
            "debugLine": "pages/index/index:4",
            "className": "titleDes",
            "value": function () {return this.titleDesc}
          },
          "type": "text",
          "classList": [
            "titleDes"
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:6",
        "className": "bottom"
      },
      "type": "list",
      "classList": [
        "bottom"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:7",
            "className": "listItem"
          },
          "type": "list-item",
          "classList": [
            "listItem"
          ],
          "repeat": {
            "exp": function () {return this.movieList},
            "value": "movie"
          },
          "onBubbleEvents": {
            "click": function (evt) {this.jump2Detail(this.movie.sort,evt)}
          },
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:8",
                "className": "items"
              },
              "type": "div",
              "classList": [
                "items"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:9",
                    "className": "movieIndex",
                    "value": function () {return this.movie.sort}
                  },
                  "type": "text",
                  "classList": [
                    "movieIndex"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:10",
                    "src": function () {return this.movie.imgUrl},
                    "className": "movieImg"
                  },
                  "type": "image",
                  "classList": [
                    "movieImg"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:11",
                    "className": "movieInfo"
                  },
                  "type": "div",
                  "classList": [
                    "movieInfo"
                  ],
                  "children": [
                    {
                      "attr": {
                        "debugLine": "pages/index/index:12",
                        "className": "movieName",
                        "value": function () {return this.movie.title}
                      },
                      "type": "text",
                      "classList": [
                        "movieName"
                      ]
                    },
                    {
                      "attr": {
                        "debugLine": "pages/index/index:13",
                        "className": "rate"
                      },
                      "type": "div",
                      "classList": [
                        "rate"
                      ],
                      "children": [
                        {
                          "attr": {
                            "debugLine": "pages/index/index:14",
                            "src": "/common/images/rating.png",
                            "className": "rateImage"
                          },
                          "type": "image",
                          "classList": [
                            "rateImage"
                          ]
                        },
                        {
                          "attr": {
                            "debugLine": "pages/index/index:15",
                            "src": "/common/images/rating.png",
                            "className": "rateImage"
                          },
                          "type": "image",
                          "classList": [
                            "rateImage"
                          ]
                        },
                        {
                          "attr": {
                            "debugLine": "pages/index/index:16",
                            "src": "/common/images/rating.png",
                            "className": "rateImage"
                          },
                          "type": "image",
                          "classList": [
                            "rateImage"
                          ]
                        },
                        {
                          "attr": {
                            "debugLine": "pages/index/index:17",
                            "src": "/common/images/rating.png",
                            "className": "rateImage"
                          },
                          "type": "image",
                          "classList": [
                            "rateImage"
                          ]
                        },
                        {
                          "attr": {
                            "debugLine": "pages/index/index:18",
                            "src": "/common/images/rating1.png",
                            "className": "rateImage"
                          },
                          "type": "image",
                          "classList": [
                            "rateImage"
                          ]
                        },
                        {
                          "attr": {
                            "debugLine": "pages/index/index:19",
                            "className": "rateText",
                            "value": function () {return this.movie.rating}
                          },
                          "type": "text",
                          "classList": [
                            "rateText"
                          ]
                        }
                      ]
                    },
                    {
                      "attr": {
                        "debugLine": "pages/index/index:21",
                        "className": "genre",
                        "value": function () {return this.movie.type}
                      },
                      "type": "text",
                      "classList": [
                        "genre"
                      ]
                    }
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:23",
                    "className": "like"
                  },
                  "type": "div",
                  "classList": [
                    "like"
                  ],
                  "children": [
                    {
                      "attr": {
                        "debugLine": "pages/index/index:24",
                        "src": "/common/images/like.png",
                        "className": "likeImg"
                      },
                      "type": "image",
                      "classList": [
                        "likeImg"
                      ]
                    },
                    {
                      "attr": {
                        "debugLine": "pages/index/index:25",
                        "className": "likeText",
                        "value": function () {return this.likeText}
                      },
                      "type": "text",
                      "classList": [
                        "likeText"
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

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=F:\\HarmonyOS\\HarmonyOs SDK\\js\\2.2.0.3\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.js":
/*!****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/script.js!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=F:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!f:/HarmonyOS/HarmonyOs SDK/js/2.2.0.3/build-tools/ace-loader/lib/resource-reference-script.js!f:/Dome/git_dome/harmonyos_codelabs/JSMovieCard/entry/src/main/js/default/pages/index/index.js ***!
  \****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){"use strict";

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js");

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _system = _interopRequireDefault(requireModule("@system.router"));

var _default = {
  data: {
    title: '热门电影排行榜',
    titleDesc: '根据电影实时热度得出的综合排名',
    movieList: [{
      'sort': '1',
      'title': '电影1',
      'type': '剧情',
      'imgUrl': '/common/images/movies_image3.jpg',
      'commentCount': '2319996',
      'rating': '9.7',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '2',
      'title': '电影2',
      'type': '剧情/爱情',
      'imgUrl': '/common/images/movies_image5.jpg',
      'commentCount': '1723017',
      'rating': '9.6',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '3',
      'title': '电影3',
      'type': '剧情',
      'imgUrl': '/common/images/movies_image6.jpg',
      'commentCount': '358932',
      'rating': '9.6',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '4',
      'title': '电影4',
      'type': '剧情/爱情',
      'imgUrl': '/common/images/movies_image7.jpg',
      'commentCount': '1745644',
      'rating': '9.6',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '5',
      'title': '电影5',
      'type': '剧情/喜剧',
      'imgUrl': '/common/images/movies_image8.jpg',
      'commentCount': '1076203',
      'rating': '9.5',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '6',
      'title': '电影6',
      'type': '剧情/喜剧',
      'imgUrl': '/common/images/movies_image9.jpg',
      'commentCount': '10762011',
      'rating': '9.5',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '7',
      'title': '电影7',
      'type': '剧情/喜剧',
      'imgUrl': '/common/images/movies_image10.jpg',
      'commentCount': '1076277',
      'rating': '9.5',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }, {
      'sort': '8',
      'title': '电影8',
      'type': '剧情/喜剧',
      'imgUrl': '/common/images/movies_image11.jpg',
      'commentCount': '1076203',
      'rating': '9.5',
      'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' + '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
    }],
    'likeText': '想看'
  },
  onInit: function onInit() {},
  jump2Detail: function jump2Detail(e) {
    var movieInfo = this.movieList[e - 1];

    _system["default"].push({
      uri: 'pages/index/detail',
      params: {
        'sort': movieInfo.sort,
        'movieName': movieInfo.title,
        'type': movieInfo.type,
        'imgUrl': movieInfo.imgUrl,
        'rating': movieInfo.rating,
        'introduction': movieInfo.introduction
      }
    });
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