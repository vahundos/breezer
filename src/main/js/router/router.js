import Vue from 'vue'
import VueRouter from 'vue-router'
import UserLoginForm from "components/user/UserLoginForm.vue"
import UserRegistrationForm from "components/user/UserRegistrationForm.vue"
import Profile from 'pages/Profile.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        component: UserLoginForm
    },
    {
        path: '/profile',
        component: Profile
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