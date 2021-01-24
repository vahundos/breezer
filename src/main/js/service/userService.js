import axios from 'axios'
import {REMOTE_SERVICE_BASE_URL} from 'constants/serviceConstants';

const USERS_URL = `${REMOTE_SERVICE_BASE_URL}/users`

export default class UserService {

    static async register(form) {
        await axios.post(USERS_URL, JSON.stringify(form), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
    }
}
