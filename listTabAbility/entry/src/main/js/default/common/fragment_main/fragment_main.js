import prompt from '@system.prompt';
import device from '@system.device';

const TAG = '[fragment_main]';
var context;
var startSearch = 0;

// js call java
// abilityType: 0-Ability; 1-Internal Ability
const ABILITY_TYPE_EXTERNAL = 0;
const ABILITY_TYPE_INTERNAL = 1;
// syncOption(Optional, default sync): 0-Sync; 1-Async
const ACTION_SYNC = 0;
const ACTION_ASYNC = 1;
const ACTION_MESSAGE_CODE = 1001;

var getDeviceInfo = function () {
    var res = '';
    device.getInfo({
        success: function (data) {
            console.log(TAG + 'Device screenShape:' + data.screenShape);
            this.res = data.screenShape;
        },
        fail: function (data, code) {
            console.log(TAG + 'Error code:' + code + '; Error information: ' + data);
        },
    });
    return res;
};

export default {
    data: {
        list_data: [{
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: '/common/images/ic.png',
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: '/common/images/ic.png',
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    },
                    {
                        item_icon: "/common/images/ic.png",
                        item_name: 'double lines',
                        item_description: 'item description',
                        item_right_text: 'item right text',
                        item_right_arrow: '/common/images/right_arrow.png'
                    }
        ],
        file_data: [
            {
                "image_add": "/common/images/profile.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/ic.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/more.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            },
            {
                "image_add": "/common/images/add64.png",
                "img_text": "Login for huawei service"
            }
        ],
        initial_index_value: 0,
        tv_img_add: "/common/images/profile.png",
        tv_img_text: "Login for huawei service0"
    },
    onInit() {
        console.log(TAG + 'onInit');
        console.log(TAG + this.initial_index_value);
        this.context = this;
        this.getSystemColorModeByJava();
        this.tv_img_text = this.$t('strings.service');
        this.list_data.forEach(element => {
            element.item_name = this.$t('strings.item_name');
            element.item_description = this.$t('strings.item_description');
            element.item_right_text = this.$t('strings.item_right_text');
        });
        this.file_data.forEach(element => {
            element.img_text = this.$t('strings.img_text');
        })
    },
    onReady()
    {
        console.log(TAG + 'onReady');
        if (getDeviceInfo() == 'circle') {
            // you can do something here
        }
    },
    onShow()
    {
        console.log(TAG + 'onShow');
    },
    onDestroy() {
        console.log(TAG + 'onDestroy');
    },
    changeList($idx) {
        console.log(TAG + $idx);
        this.tv_img_add = this.file_data[$idx].image_add;
        this.tv_img_text = this.file_data[$idx].img_text + $idx;
        this.$element($idx).focus({
            focus: true
        });
    },
    enterkeyClick(e) {
        prompt.showToast({
            message: "enter key clicked",
            duration: 3000,
        });
    },
    getFocusOfSearch(e) {
        this.startSearch = 1;
    },
    searchChange(e) {
        if (startSearch != 0) {
            prompt.showToast({
                message: "value: " + e.value,
                duration: 3000,
            });
        }
    },
    getSystemColorModeByJava: async function() { // only support async
        var actionData = {};
        actionData.firstNum = 123; //test data
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
            this.list_data.forEach(element => {
                element.item_icon = "/common/images/ic_dark_mode.png";
                element.item_right_arrow = "/common/images/right_arrow_dark_mode.png";
            });
        }
    }
}