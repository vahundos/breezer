import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Vue from 'vue'
import App from 'pages/App.vue'
import {ButtonPlugin, FormInputPlugin, NavbarPlugin} from 'bootstrap-vue'

Vue.use(NavbarPlugin);
Vue.use(FormInputPlugin);
Vue.use(ButtonPlugin);

new Vue({
    el: '#app',
    render: a => a(App)
});