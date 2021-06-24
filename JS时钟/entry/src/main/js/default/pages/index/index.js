export default {
    data: {
        hourHand: "rotate(-90deg)",
        minuteHand: "rotate(-90deg)",
        secondHand: "rotate(-90deg)"
    },
    onInit() {
        const _this = this
        let hour = new Date().getHours()
        let minute = new Date().getMinutes()
        let second = new Date().getSeconds()

        hour %= 12
        _this.hourHand = "rotate(" + (hour * 30 - 90) + "deg)"
        _this.minuteHand = "rotate(" + (minute * 6 - 90) + "deg)"
        _this.secondHand = "rotate(" + (second * 6 - 90) + "deg)"

        setInterval(function () {
            let hour = new Date().getHours()
            let minute = new Date().getMinutes()
            let second = new Date().getSeconds()

            hour %= 12
            _this.hourHand = "rotate(" + (hour * 30 - 90) + "deg)"
            _this.minuteHand = "rotate(" + (minute * 6 - 90) + "deg)"
            _this.secondHand = "rotate(" + (second * 6 - 90) + "deg)"

        }, 1000);
    }
}
