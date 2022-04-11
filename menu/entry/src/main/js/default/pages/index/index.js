// xxx.js
import prompt from '@system.prompt';
export default {
    onMenuSelected(e) {
        prompt.showToast({
            message: e.value
        })
    },
    onTextClick() {
        this.$element("apiMenu").show({x:280,y:400});
    }
}