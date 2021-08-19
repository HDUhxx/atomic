// @ts-nocheck
/* xxx.js */

export default {
    data: {
        count: 0
    },
    increase() {
        this.count++;
    },
    decrease() {
        this.count--;
    },
    multiply(multiplier) {
        this.count = multiplier *(this.count);
    },
    sqrty(){
        this.count = Math.sqrt(this.count).toFixed(2);


     }
};