// xxx.js
import router from '@system.router';
export default {
    data:{
        time: "2022.03.27",
        address:"广东省深圳市*****",
        personal:"*****",
    },
    click(){
        router.push({
            uri:"pages/touter/touter"
        })
    }
}