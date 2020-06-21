import Vuex from 'vuex'
import Vue from 'vue'
import UserService from 'service/userService'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        authToken: null,
        user: null
    },
    mutations: {
        setAuthToken(state, userAuthToken) {
            state.authToken = userAuthToken
        },
        setUser(state, user) {
            state.user = user
        }
    },
    actions: {
        async userLogin({commit}, payload) {
            const data = await UserService.login(payload.login, payload.password)

            commit('setAuthToken', data.authToken)
            commit('setUser', data.user)
        },
        async userLogout({commit}) {
            await UserService.logout()
            commit('setAuthToken', null)
            commit('setUser', null)
        },
        async refreshStorage({commit}) {
            commit('setAuthToken', UserService.loadAuthTokenFromStorage())
            commit('setUser', await UserService.getByAuthorization())
        }
    }
})