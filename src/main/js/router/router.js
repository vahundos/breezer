import Vue from 'vue'
import VueRouter from 'vue-router'
import UserLoginForm from "components/user/UserLoginForm.vue"
import UserRegistrationForm from "components/user/UserRegistrationForm.vue"

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        component: UserLoginForm
    },
    {
        path: '/registration',
        component: UserRegistrationForm
    }
]

export default new VueRouter({
    mode: 'history',
    routes
})