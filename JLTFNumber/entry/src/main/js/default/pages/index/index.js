export default {
    data: {
        result: '将1~9数字分别填入方格中使横竖对角线之和都为15',
        answer: []
    },
    onInit() {
    },
    one(e) {
        this.answer[0] = parseInt(e.value);

    },
    two(e) {
        this.answer[1] = parseInt(e.value);


    },
    three(e) {
        this.answer[2] = parseInt(e.value);


    },
    four(e) {
        this.answer[3] = parseInt(e.value);


    },
    five(e) {
        this.answer[4] = parseInt(e.value);


    },
    six(e) {
        this.answer[5] = parseInt(e.value);


    },
    seven(e) {
        this.answer[6] = parseInt(e.value);


    },
    eight(e) {
        this.answer[7] = parseInt(e.value);


    },
    nine(e) {
        this.answer[8] = parseInt(e.value);


    },
    submit(){
        if (this.answer[0] + this.answer[1] + this.answer[2] == 15
            && this.answer[3] + this.answer[4] + this.answer[5] == 15
            && this.answer[6] + this.answer[7] + this.answer[8] == 15
            && this.answer[0] + this.answer[3] + this.answer[6] == 15
            && this.answer[1] + this.answer[4] + this.answer[7] == 15
            && this.answer[2] + this.answer[5] + this.answer[8] == 15
            && this.answer[0] + this.answer[4] + this.answer[8] == 15
            && this.answer[2] + this.answer[4] + this.answer[6] == 15) {
                this.result = '胜利'

        } else {
            this.result = '失败'
        }
    }
}
