import axios from 'axios'

const USER_TOKEN = 'user-token';
const X_AUTH_TOKEN_HEADER = 'X-Auth-Token';

const REMOTE_SERVICE_BASE_URL = 'http://localhost:8080/users';

export default class UserService {

    static async login(username, password) {
        const response = await axios.post(REMOTE_SERVICE_BASE_URL + '/login', '', {
            headers: {
                'Authorization': 'Basic ' + btoa(`${username}:${password}`)
            }
        });

        const authToken = response.data.authToken;
        localStorage.setItem(USER_TOKEN, authToken);
        axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = authToken;
        return authToken;
    }

    static async logout() {
        await axios.post(REMOTE_SERVICE_BASE_URL + '/logout', '');
        localStorage.removeItem(USER_TOKEN);
        delete axios.defaults.headers.common[X_AUTH_TOKEN_HEADER];
    }

    static loadAuthTokenFromStorage() {
        let userToken = localStorage.getItem(USER_TOKEN);
        axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = userToken;

        return userToken;
    }
}