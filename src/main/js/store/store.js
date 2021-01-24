import Vuex from 'vuex'
import Vue from "vue";
import AuthService from "service/AuthService";

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
            commit('setUserAuthToken', await AuthService.login(payload.login, payload.password));
        },
        async userLogout({commit}) {
            await AuthService.logout();
            commit('setUserAuthToken', '');
        },
        async loadAuthTokenFromStorage({commit}) {
            commit('setUserAuthToken', AuthService.loadAuthTokenFromStorage())
        }
    }
})