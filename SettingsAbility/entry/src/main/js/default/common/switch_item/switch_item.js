export default {
    props: {
        itemTitle: {
            default: 'item'
        },
        subTitle: {
            default: 'sub title'
        }
    },
    data: {
        star: false,
    },
    onInit() {

    },
    onClickStar() {
        this.star = !this.star;
    }
}
