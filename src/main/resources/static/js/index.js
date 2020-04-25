import Vue from 'vue'
import App from 'pages/App.vue'
import vuetify from 'plugins/vuetify'
import store from 'store/store'
import 'vuetify/dist/vuetify.min.css'

new Vue({
    el: '#app',
    vuetify,
    store,
    render: a => a(App),
    created: function () {
        this.$store.dispatch('loadAuthTokenFromStorage');
    }
});