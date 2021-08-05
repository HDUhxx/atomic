const TAG = '[fragment_main]';

export default {
    data: {
        title: "",
        array: [[1, 0], [1, 1], [1, 2], [2, 2], [2, 2], [2, 3], [3, 3], [3, 5]],
        list: [],
        tv_img_add: "common/profile.png",
        tv_img_text: "",
        file_data: [
            {
                "image_add": "common/profile.png",
            },
            {
                "image_add": "common/ic.png",
            },
            {
                "image_add": "common/more.png",
            },
            {
                "image_add": "common/add64.png",
            },
            {
                "image_add": "common/add64.png",
            },
            {
                "image_add": "common/add64.png",
            },
            {
                "image_add": "common/add64.png",
            },
            {
                "image_add": "common/add64.png",
            }
        ]
    },
    onInit() {
        this.title = this.$t('strings.title');
        this.list = [];
        this.tv_img_text = this.$t('strings.img_text_0');
        var str = 'strings.Setting';
        for (var i = 0; i < this.array.length; i++) {
            var resource = str + i;
            var dataItem = {
                value: this.$t(resource)
            };
            this.list.push(dataItem);
        }
    },
    changeList($idx) {
        console.log(TAG + $idx);
        this.tv_img_add = this.file_data[$idx].image_add;
        this.tv_img_text = this.$t('strings.img_text_' + $idx);
        this.$element($idx).focus({
            focus: true
        });
    }
}
