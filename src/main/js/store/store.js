import Vuex from 'vuex'
import Vue from 'vue'
import UserService from 'service/userService'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        authToken: null,
        id: null
    },
    mutations: {
        setAuthToken(state, userAuthToken) {
            state.authToken = userAuthToken
        },
        setId(state, id) {
            state.id = id
        }
    },
    actions: {
        async userLogin({commit}, payload) {
            console.log(payload)
            const data = await UserService.login(payload.login, payload.password)

            commit('setAuthToken', data.authToken)
            commit('setId', data.user.id)
        },
        async userLogout({commit}) {
            await UserService.logout()
            commit('setAuthToken', null)
            commit('setId', null)
        },
        async refreshStorage({commit}) {
            commit('setAuthToken', UserService.loadAuthTokenFromStorage())
            commit('setId', await UserService.retrieveIdByAuthorization())
        }
    }
})