import axios from 'axios'
import nock from 'nock'
import userService from "service/userService"

describe('UserService', () => {

    beforeEach(() => {
        nock('http://localhost:8080')
            .options('/users/login')
            .reply(200, {}, {
                'Access-Control-Allow-Headers': 'authorization, x-auth-token',
                'Access-Control-Allow-Methods': 'GET,POST',
                'Access-Control-Allow-Origin': '*'
            })
    });

    test('should login', async () => {
        const authToken = 'authToken';
        const username = 'user';
        const password = 'password';

        nock('http://localhost:8080', {
            reqheaders: {
                'Authorization': 'Basic ' + btoa(`${username}:${password}`)
            }
        })
            .defaultReplyHeaders({'Access-Control-Allow-Origin': '*'})
            .post('/users/login')
            .reply(200, {
                authToken
            })

        const returnedUserToken = await userService.login(username, password)
        expect(returnedUserToken).toBe(authToken)
        expect(axios.defaults.headers.common['X-Auth-Token']).toBe(authToken)
        expect(localStorage.getItem('user-token')).toBe(authToken);
    })
})