//xxx.js
import router from '@system.router';
export default {
    data:{
        time:"",
        address:"",
        personal:"",
    },
    getChange(e){
        let idName = e.target.id
        if (idName === "1") {
            this.time = e.value
        }else if (idName === "2") {
            this.address = e.value
        }else if (idName === "3") {
            this.personal = e.value
        }
    },
    btnClick(){
        router.push({
            uri:"pages/index/index",
            params:{
                time:this.time,
                address:this.address,
                personal:this.personal,
            }
        })
    }
}