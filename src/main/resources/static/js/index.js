import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import Vue from 'vue'
import App from 'pages/App.vue'
import {
    ButtonPlugin,
    CardPlugin,
    FormCheckboxPlugin,
    FormGroupPlugin,
    FormInputPlugin,
    FormPlugin,
    FormSelectPlugin,
    NavbarPlugin
} from 'bootstrap-vue'

Vue.use(NavbarPlugin);
Vue.use(FormInputPlugin);
Vue.use(ButtonPlugin);
Vue.use(FormPlugin);
Vue.use(CardPlugin);
Vue.use(FormGroupPlugin);
Vue.use(FormSelectPlugin);
Vue.use(FormCheckboxPlugin);

new Vue({
    el: '#app',
    render: a => a(App)
});