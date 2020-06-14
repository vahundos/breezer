import axios from 'axios'

const USER_TOKEN = 'user-token'
const X_AUTH_TOKEN_HEADER = 'X-Auth-Token'

const REMOTE_SERVICE_BASE_URL = 'http://localhost:8080/users'

export default class UserService {

    static async login(username, password) {
        const response = await axios.post(REMOTE_SERVICE_BASE_URL + '/login', '', {
            headers: {
                'Authorization': 'Basic ' + btoa(`${username}:${password}`)
            }
        })

        const authToken = response.data.authToken
        localStorage.setItem(USER_TOKEN, authToken)
        axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = authToken
        return response.data
    }

    static async getByAuthorization() {
        if (this.loadAuthTokenFromStorage() === null) {
            return null
        }

        let response = await axios.post(REMOTE_SERVICE_BASE_URL + '/login')
        return response.data.user
    }

    static async getUserPicture(userId) {
        const response = await axios.get(REMOTE_SERVICE_BASE_URL + `/${userId}/picture`, {responseType: 'blob'})
        return response.data
    }

    static async register(form) {
        await axios.post(REMOTE_SERVICE_BASE_URL + '/register', JSON.stringify(form), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
    }

    static async logout() {
        await axios.post(REMOTE_SERVICE_BASE_URL + '/logout', '')
        localStorage.removeItem(USER_TOKEN)
        delete axios.defaults.headers.common[X_AUTH_TOKEN_HEADER]
    }

    static loadAuthTokenFromStorage() {
        let userToken = localStorage.getItem(USER_TOKEN)
        axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = userToken

        return userToken
    }
}