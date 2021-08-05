import prompt from '@system.prompt';

export default {
    props: {
        settingTitle: {
            default: 'Setting'
        },
        numOfTopItem: {
            default: 1
        },
        numOfBotItem: {
            default: 0
        }
    },
    data: {
        next: "",
        border_visible_top: [],
        border_visible_bot: [],
        num_setting_simple_text: 1,
        num_setting_switch: 0,
        setting_simple_text: [],
        setting_switch: [],
        ok: false,
    },
    onInit() {
        this.num_setting_simple_text = this.numOfTopItem;
        this.num_setting_switch = this.numOfBotItem;
        this.next = this.$t('strings.next');
        var str = 'strings.item';
        for (var i = 0; i < this.num_setting_simple_text; i++) {
            this.border_visible_top[i] = i == 0 ? false : true;
            //引用资源 strings.item(0~this.num_setting_simple_text)
            var resource = str + i;
            this.setting_simple_text[i] = this.$t(resource);
        }
        var str_main = 'strings.double_line_main_', str_sub = 'strings.double_line_sub_';
        for (var i = 0; i < this.num_setting_switch; i++) {
            var resource_main = str_main + i;
            var resource_sub = str_sub + i;
            this.setting_switch[i] = {
                double_line_main_content: this.$t(resource_main),
                double_line_sub_content: this.$t(resource_sub)
            };
        }
    },
    onClickNext() {
        prompt.showToast({
            message: 'next'
        });
    },
}
