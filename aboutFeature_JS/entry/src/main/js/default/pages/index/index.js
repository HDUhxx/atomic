import device from '@system.device';
import router from '@system.router';
import mediaquery from '@system.mediaquery';
import app from '@system.app';

const TAG = '[index]';
var mMediaQueryList;
var context;
var wearableMediaListener = function (e) {
    if (e.matches) {
        // do something
        console.log(TAG + "Success Media Listen");
        context.initial_value = 4;
        context.$child('id_fragment_main').initial_index_value = 4;
        console.log(context.initial_value);
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
        initial_value: 0
    },
    onInit() {
        console.log(TAG + 'onInit');
        this.context = this;
        this.mMediaQueryList = mediaquery.matchMedia("screen and (device-type: wearable)");
        this.mMediaQueryList.addListener(wearableMediaListener);
    },
    onReady()
    {
        console.log(TAG + 'onReady');
        console.log(TAG + getDeviceInfo());
    },
    onShow()
    {
        console.log(TAG + 'onShow');
        console.log(TAG + getDeviceInfo());
    },
    onDestroy() {
        console.log(TAG + 'onDestroy');
        mMediaQueryList.removeListener(wearableMediaListener);
    },
    backClick() {
        app.terminate();
    },
//    路由跳转
    refuseService() {
        router.push({
            uri: 'pages/service/service'
        });
    },
    privacyProtocol() {
        router.push({
            uri: 'pages/privacy/privacy'
        });
    },
    userProtocol() {
        router.push({
            uri: 'pages/userProtocol/userProtocol'
        });
    }
}
