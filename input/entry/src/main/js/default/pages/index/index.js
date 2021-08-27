import prompt from '@system.prompt'
export default {
    /*输入完后所展示的信息*/
    change(e){
        prompt.showToast({
            message: e.value,
           /* 弹出来的小框展示的时间*/
            duration: 3000,
        });
    },
    enterkeyClick(e){
        prompt.showToast({
            message: "enterkey clicked",
            duration: 3000,
        });
    },
    /*想设定点击后框样式的变化，但是没成功*/
    touchstart(e){
        var inputf=this.$element("inputf");
        inputf.width="500px";
        console.log(inputf);

    },
    buttonClick(e){
        prompt.showToast({
            message: "登录成功",
            duration: 5000,
        });
    },
}