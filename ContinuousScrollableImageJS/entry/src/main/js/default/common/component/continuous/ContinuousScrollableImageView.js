/**
 * Copyright (c) 2021 shanghaozhi
   ContinuousScrollableImageJS is licensed under Mulan PSL v2.
   You can use this software according to the terms and conditions of the Mulan PSL v2.
   You may obtain a copy of Mulan PSL v2 at:
            http://license.coscl.org.cn/MulanPSL2
   THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
   See the Mulan PSL v2 for more details.
 */
import Animator from '@ohos.animator';

export default {
    props: {
        srcResource: {
            default: "common/images/cloud.png"
        },
        srcWidth: {
            default: 100
        },
        srcHeight: {
            default: 100
        },
        scaleStyle: {
            default: "contain"
        },
        scaleDuration: {
            default: 3000
        },
        scaleDirection: {
            default: "normal"
        },
        scaleOrientation: {
            default: "h"
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
        widthPx() {
            return this.compWidth + 'px'
        },
        heightPx() {
            return this.compHeight + 'px'
        },
        translate1() {
            return this.test + this.translateFirst + 'px' + ')'
        },
        translate2() {
            return this.test + this.translateSecond + 'px' + ')'
        },
    },
    onInit() {
        console.error("AAA - onInit")
    },
    onAttached() {
        this.srcRes = this.srcResource;
        this.compWidth = parseInt(this.srcWidth.replace('px', ''));
        this.compHeight = parseInt(this.srcHeight.replace('px', ''));
        this.scaleMode = this.scaleStyle;
        this.duration = this.scaleDuration;
        this.direction = this.scaleDirection;
        this.orientation = this.scaleOrientation.toLowerCase();
    },
    onPageShow() {
        var rect = this.$element("refId").getBoundingClientRect()
        if (this.orientation == 'v') {
            this.test = "translateY("
            this.translateLength = rect.height;
        } else {
            this.test = "translateX("
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
        this.animator = Animator.createAnimator(options);
        var that = this
        this.animator.onframe = function (value) {
            that.translateFirst = value;
            that.translateSecond = value - that.translateLength;
        };
        this.animator.play();
    },
}