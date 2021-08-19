import router from '@system.router';

export default {
    data: {
        know_interest:"点击学习"
    },
    page_three(){
        router.push({
            uri:'pages/study/study'
        })
    }
}
