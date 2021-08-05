// @ts-ignore
import prompt from '@system.prompt';
import app from '@system.app';

export default {
    props: {
        barTitle: {
            default: 'title'
        }
    },
    onClickAdd() {
        prompt.showToast({
            message: 'add'
        });
    },
    onClickMore() {
        prompt.showToast({
            message: 'more'
        });
    },
    onClickBack() {
        app.terminate();
    }
}
