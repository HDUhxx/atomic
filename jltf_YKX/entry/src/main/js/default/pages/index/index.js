import prompt from '@system.prompt';
import router from '@system.router';

let dtInfo = null;
let ggInfo = null;
let a = null;
let b = null;

const ABILITY_TYPE_EXTERNAL = 0;
const ACTION_SYNC = 0;

export default {
    data:{
        a:"",
        b:""
    },

    getText(e){
        console.info(e.value)
        this.a=e.value
        this.plus(1013)
    },

    showText(){
        this.plus(1014)
    },

    getImg(){
       this.plus(1011)
    },
    sendImg(){
        this.plus(1012)
    },

    getData(code){
        var actionData = {};
        switch (code) {
            case 1013:
                actionData.text = this.a
            break;
            default:
                break;
        }
        return actionData;
    },

    showData(ret){
        switch (ret.code) {
            case 1014:
                this.b = ret.text
                break;
            default:
                break;
        }
    },

    plus: async function (code) {

        var actionData = this.getData(code);

        var action = {};
        action.bundleName = 'com.jitf.cardshow';
        action.abilityName = 'com.jltf.jltf_idiom.controller.ServiceAbility';
        action.messageCode = code;
        action.data = actionData;
        action.abilityType = ABILITY_TYPE_EXTERNAL;
        action.syncOption = ACTION_SYNC;

        var result = await FeatureAbility.callAbility(action);
        var ret = JSON.parse(result);

        console.info("code:" + JSON.stringify(ret.code))
        console.info("获取的数据:" + JSON.stringify(ret))
        this.showData(ret)
    }
}
