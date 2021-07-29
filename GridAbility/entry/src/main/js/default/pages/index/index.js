import app from '@system.app';

var context;

export default {
    data: {
        images_resource: {
            "image_add": "common/images/back.png",
            "image_ic": "common/images/ic.png"
        },
        list_data: [
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },

        ],
        dec_data: [
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
            {
                item_icon: 'common/images/jltf.jpg',
                item_name: 'Text',
            },
        ]
    },
    onInit() {
        context = this;
        context.list_data.forEach(element => {
            element.item_name = context.$t('strings.item_name_t');
        });
        context.dec_data.forEach(element => {
            element.item_name = context.$t('strings.item_name_b');
        });
    },
    listFocus($idx) {
        this.$element($idx).focus({
            focus: true
        });
        console.log($idx);
    },
    backHome() {
        app.terminate();
    }
}
