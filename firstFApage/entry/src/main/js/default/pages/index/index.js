// index.js
export default {
    data: {
        cartText: 'Add To Cart',
        cartStyle: 'cart-text',
        isCartEmpty: true,
        descriptionFirstParagraph: 'This is a food page containing fresh fruits, snacks and etc. You can pick whatever you like and add it to your cart. Your order will arrive within 48 hours. We guarantee that our food is organic and healthy. Feel free to access our 24h online service for more information about our platform and products.',
        imageList: ['/common/images/food_000.PNG', '/common/images/food_001.PNG', '/common/images/food_002.PNG', '/common/images/food_003.PNG'],
    },

    swipeToIndex(index) {
        this.$element('swiperImage').swipeTo({index: index});
    },

    addCart() {
        if (this.isCartEmpty) {
            this.cartText = 'Cart + 1';
            this.cartStyle = 'add-cart-text';
            this.isCartEmpty = false;
        }
    },

    getFocus() {
        if (this.isCartEmpty) {
            this.cartStyle = 'cart-text-focus';
        }
    },

    lostFocus() {
        if (this.isCartEmpty) {
            this.cartStyle = 'cart-text';
        }
    },
}