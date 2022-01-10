// index.js
import prompt from '@system.prompt';
export default {
    getCol(e) {
        this.$element('mygrid').getColumns(function (result) {
            prompt.showToast({
                message: e.target.id + ' result = ' + result,
                duration: 3000,
            });
        })
    },
    getColWidth(e) {
        this.$element('mygrid').getColumnWidth(function (result) {
            prompt.showToast({
                message: e.target.id + ' result = ' + result,
                duration: 3000,
            });
        })
    }
}