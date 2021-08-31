export default {
    swipeTo() {
        this.$element('swiper').swipeTo({index: 0});/*跳转到首页的方法*/

    },
    showNext() {
        this.$element('swiper').showNext();/*下一页的方法*/
    },
    showPrevious() {
        this.$element('swiper').showPrevious();/*上一页的方法*/
    }
}