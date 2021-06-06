
export default {
    data: {
        hour: new Date().getHours(),
        minute: new Date().getMinutes(),
        day: "",
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        date: new Date().getDate()
    },
    onInit() {
        switch(new Date().getDay()) {
                case 0:
                    this.day = "日";
                    break;
                case 1:
                    this.day = "一";
                    break;
                case 2:
                    this.day = "二";
                    break;
                case 3:
                    this.day = "三";
                    break;
                case 4:
                    this.day = "四";
                    break;
                case 5:
                    this.day = "五";
                    break;
                case 6:
                    this.day = "六";
                    break;
            }
    }
}

