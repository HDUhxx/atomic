import Animator from '@ohos.animator';

export default {
    data: {
        translateX: 0,
        animator : null
    },
    computed: {
        translateXPx() {
            return this.translateX + 'px'
        },
        translateX1Px() {
            return (this.translateX-1080) + 'px'
        },
    },
    onInit(){
        var options = {
            duration: 5000,
            easing: 'ease-in-out',
            fill: 'forwards',
            iterations: 10000,
            begin: 0,
            end: 1080
        };
        this.animator = Animator.createAnimator(options);
        var that = this
        this.animator.onframe = function (value) {
            that.translateX = value;
        };
    },
    toggleState() {
        this.animator.play();
    }
}
