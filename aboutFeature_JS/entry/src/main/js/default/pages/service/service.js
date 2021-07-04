import router from '@system.router';

export default {
    data: {
        title: 'Terms of use',
        description: 'If refused, all installation tasks will be suspended, all services will be stopped, and the Huawei application market will be closed.'
    },
    backToMain() {
        router.back();
    }
}
