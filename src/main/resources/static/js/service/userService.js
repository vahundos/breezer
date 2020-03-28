import axios from 'axios'

const USER_TOKEN = 'user-token';
const X_AUTH_TOKEN_HEADER = 'X-Auth-Token';

const REMOTE_SERVICE_BASE_PATH = '/users';

export default class UserService {

    static login(username, password) {
        return axios.post(REMOTE_SERVICE_BASE_PATH + '/login', '', {
            headers: {
                'Authorization': 'Basic ' + btoa(`${username}:${password}`)
            }
        })
            .then(response => {
                const authToken = response.data.authToken;
                localStorage.setItem(USER_TOKEN, authToken);
                axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = authToken
            })
    }

    static logout() {
        return axios.post(REMOTE_SERVICE_BASE_PATH + '/logout', '')
            .then(() => {
                localStorage.removeItem(USER_TOKEN);
                delete axios.defaults.headers.common[X_AUTH_TOKEN_HEADER];
            });
    }
}