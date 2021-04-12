import prompt from '@system.prompt'
export default {
    visibilitychange(e) {
        prompt.showToast({
            message: 'visibility change visibility: ' + e.visibility,
            duration: 3000,
        });
    },
    showpopup() {
        this.$element("popup").show();
    },
    hidepopup() {
        this.$element("popup").hide();
    },
}