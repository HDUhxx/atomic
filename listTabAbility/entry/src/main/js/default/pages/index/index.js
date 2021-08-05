import prompt from '@system.prompt';
import device from '@system.device';
import mediaquery from '@system.mediaquery';

const TAG = '[index]';
var mMediaQueryList;
var context;

// js call java
// abilityType: 0-Ability; 1-Internal Ability
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
// syncOption(Optional, default sync): 0-Sync; 1-Async
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;
const ACTION_MESSAGE_CODE = 1001;

var wearableMediaListener = function (e) {
    if (e.matches) {
        // do something
        console.log(TAG + "Success Media Listen");
        context.initial_value = 4;
        context.$child('id_fragment_main').initial_index_value = 0;
        context.$child('id_fragment_main').list_data.forEach(element => {
            element.item_icon = context.images_resource_dark_mode.image_icon;
            element.item_right_arrow = context.images_resource_dark_mode.image_right_arrow;
        });
    }
};
var getDeviceInfo = function () {
    var res = '';
    device.getInfo({
        success: function (data) {
            console.log(TAG + 'Success device obtained. screenShape=' + data.screenShape);
            this.res = data.screenShape;
        },
        fail: function (data, code) {
            console.log(TAG + 'Failed to obtain device. Error code=' + code + '; Error information: ' + data);
        },
    });
    return res;
};


export default {
    data: {
        images_resource: {
            "image_icon": "/common/images/ic.png",
            "image_add": "/common/images/add64.png",
            "image_more": "/common/images/more.png",
            "image_right_arrow": "/common/images/right_arrow.png"
        },
        images_resource_dark_mode: {
            "image_icon": "/common/images/ic_dark_mode.png",
            "image_add": "/common/images/add64.svg",
            "image_more": "/common/images/more.svg",
            "image_right_arrow": "/common/images/right_arrow_dark_mode.png"
        },
        initial_value: 0
    },
    onInit() {
        console.log(TAG + 'onInit');
        context = this;
        this.mMediaQueryList = mediaquery.matchMedia("screen and (device-type: wearable)");
        this.mMediaQueryList.addListener(wearableMediaListener);
        console.info(TAG + "java js" + this.getSystemColorModeByJava()); // async call and return null
    },
    onReady()
    {
        console.log(TAG + 'onReady');
        console.log(TAG + getDeviceInfo()); // getDeviceInfo after Init
    },
    onShow()
    {
        console.log(TAG + 'onShow');
    },
    onDestroy() {
        console.log(TAG + 'onDestroy');
        mMediaQueryList.removeListener(wearableMediaListener);
    },
    onClick() {
        prompt.showToast({
            message: "add",
            duration: 3000,
        });
    },
    onClick2() {
        prompt.showToast({
            message: "more",
            duration: 3000,
        });
    },
    getSystemColorModeByJava: async function() {
        var actionData = {};
        actionData.firstNum = 123;
        actionData.secondNum = 465;

        var action = {};
        action.bundleName = 'com.example.listtabability';
        action.abilityName = 'com.example.listtabability.ServiceAbilityForJS';
        action.messageCode = ACTION_MESSAGE_CODE;
        action.data = actionData;
        action.abilityType = ABILITY_TYPE_EXTERNAL;
        action.syncOption = ACTION_SYNC;

        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);
        if (ret.code == 0) {
            console.info('result is:' + JSON.stringify(ret.abilityResult));
        } else {
            console.error('error code:' + JSON.stringify(ret.code));
        }

        if (ret.getColorMode == 0) {
            console.info(TAG + ret.getColorMode);
            // get system color mode and do something
            this.images_resource = this.images_resource_dark_mode;
        }

    }
}