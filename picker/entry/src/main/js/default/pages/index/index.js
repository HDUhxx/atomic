// xxx.js
import router from '@system.router';
import prompt from '@system.prompt';
export default {
    data: {
        rangetext:['技术部', "销售部", "运营部"],
        multitext:[["上午", "下午", "晚上"], ["写作业", "打游戏", "看电视"], ["2小时", "1小时"], ["k", "l", "m"]],
        textvalue:'普通选择器',
        datevalue:'日期选择器',
        timevalue:'时间选择器',
        datetimevalue:'日期时间选择器',
        multitextvalue:'多列文本选择器',
        containsecond:true,
        multitextselect:[1,2,0],
        datetimeselect:'2012-5-6-11-25',
        timeselect:'11:22:30',
        dateselect:'2021-3-2',
        textselect:'2'
    },
    textonchange(e) {
        this.textvalue = e.newValue;
        prompt.showToast({ message:"text:"+e.newValue+",newSelected:"+e.newSelected })
    },
    textoncancel(e) {
        prompt.showToast({ message:"text: textoncancel" })
    },
    dateonchange(e) {
        this.datevalue = e.year + "-" + (e.month+1) + "-" + e.day;
        prompt.showToast({ message:"date:"+e.year+"-"+(e.month+1)+"-"+e.day })
    },
    dateoncancel() {
        prompt.showToast({ message:"date: dateoncancel" })
    },
    timeonchange(e) {
        if(this.containsecond){
            this.timevalue=e.hour+":"+e.minute+":"+e.second;
            prompt.showToast({ message:"时间:" + e.hour + ":" + e.minute + ":" + e.second })
        } else {
            this.timevalue=e.hour+":"+e.minute;
            prompt.showToast({ message:"时间:" + e.hour + ":" + e.minute })
        }},
    timeoncancel() {
        prompt.showToast({ message:"timeoncancel" })
    },
    datetimeonchange(e) {
        this.datetimevalue=e.year+"-"+ (e.month+1)+"-"+e.day+" "+e.hour+":"+e.minute;
        prompt.showToast({ message:"时间:"+ (e.month+1)+"-"+e.day+" "+e.hour+":"+e.minute })
    },
    datetimeoncancel() {
        prompt.showToast({ message:"datetimeoncancel" })
    },
    multitextonchange(e) {
        this.multitextvalue=e.newValue;
        prompt.showToast({ message:"多列文本更改" + e.newValue })
    },
    multitextoncancel() {
        prompt.showToast({ message:"multitextoncancel" })
    },
    popup_picker() {
        this.$element("picker_text").show();
    },
}