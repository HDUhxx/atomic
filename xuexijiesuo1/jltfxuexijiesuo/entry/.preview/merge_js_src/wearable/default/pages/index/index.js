import router from '@system.router';
export default {
    data: {
        know:"了解更多"
    },
    page_two(){
        router.push({
            uri:'pages/interest/interest'
        })
    }
}
