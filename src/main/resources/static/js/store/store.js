import Vuex from 'vuex'
import Vue from "vue";
import UserService from "service/userService";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        userAuthToken: ''
    },
    mutations: {
        setUserAuthToken (state, userAuthToken) {
            state.userAuthToken = userAuthToken;
        }
    },
    actions: {
        async userLogin({commit}, payload) {
            console.log(payload);
            commit('setUserAuthToken', await UserService.login(payload.login, payload.password));
        },
        async userLogout({commit}) {
            await UserService.logout();
            commit('setUserAuthToken', '');
        },
        async loadAuthTokenFromStorage({commit}) {
            commit('setUserAuthToken', UserService.loadAuthTokenFromStorage())
        }
    }
})