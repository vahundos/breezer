import axios from 'axios'
import {REMOTE_SERVICE_BASE_URL} from 'constants/serviceConstants';

const USER_TOKEN = 'user-token';
const X_AUTH_TOKEN_HEADER = 'X-Auth-Token';

const AUTH_URL = `${REMOTE_SERVICE_BASE_URL}/authentication`

export default class AuthService {

    static async login(username, password) {
        const response = await axios.post(AUTH_URL, '', {
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
        await axios.delete(AUTH_URL, '');
        localStorage.removeItem(USER_TOKEN);
        delete axios.defaults.headers.common[X_AUTH_TOKEN_HEADER];
    }

    static loadAuthTokenFromStorage() {
        let userToken = localStorage.getItem(USER_TOKEN);
        axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = userToken;

        return userToken;
    }
}
