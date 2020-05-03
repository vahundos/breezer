import axios from 'axios'
import nock from 'nock'
import userService from "service/userService"

const BASE_PATH = 'http://localhost:8080'
const DEFAULT_REPLAY_HEADERS = {'Access-Control-Allow-Origin': '*'}
const X_AUTH_TOKEN_HEADER = 'X-Auth-Token'
const AUTH_TOKEN = 'authToken'
const USER_TOKEN = 'user-token'

describe('UserService', () => {

    beforeEach(() => {
        nock(BASE_PATH)
            .options(/.*/)
            .reply(200, {}, {
                'Access-Control-Allow-Headers': 'authorization, x-auth-token',
                'Access-Control-Allow-Methods': 'GET,POST',
                'Access-Control-Allow-Origin': '*'
            })
    })

    test('should login', async () => {
        const username = 'user'
        const password = 'password'

        const requestHeaders = {'Authorization': 'Basic ' + btoa(`${username}:${password}`)}
        nock(BASE_PATH, {requestHeaders})
            .defaultReplyHeaders(DEFAULT_REPLAY_HEADERS)
            .post('/users/login')
            .reply(200, {
                'authToken': AUTH_TOKEN
            })

        const returnedUserToken = await userService.login(username, password)
        expect(returnedUserToken).toBe(AUTH_TOKEN)
        expect(axios.defaults.headers.common[X_AUTH_TOKEN_HEADER]).toBe(AUTH_TOKEN)
        expect(localStorage.getItem(USER_TOKEN)).toBe(AUTH_TOKEN)
    })

    test('should register', async () => {
        const form = {data: 'value'}

        const requestHeaders = {'Content-Type': 'application/json'}
        nock(BASE_PATH, {requestHeaders})
            .defaultReplyHeaders(DEFAULT_REPLAY_HEADERS)
            .post('/users/register', JSON.stringify(form))
            .reply(201)

        await userService.register(form)
    })

    test('should logout', async () => {
        localStorage.setItem(USER_TOKEN, AUTH_TOKEN)
        axios.defaults.headers.common[X_AUTH_TOKEN_HEADER] = AUTH_TOKEN

        const requestHeaders = {}
        requestHeaders[X_AUTH_TOKEN_HEADER] = AUTH_TOKEN
        nock(BASE_PATH, {requestHeaders})
            .defaultReplyHeaders(DEFAULT_REPLAY_HEADERS)
            .post('/users/logout')
            .reply(200)

        await userService.logout()

        expect(localStorage.getItem(USER_TOKEN)).toBeNull()
        expect(axios.defaults.headers.common[X_AUTH_TOKEN_HEADER]).toBeUndefined()
    })

    test('should loadAuthTokenFromStorage', () => {
        localStorage.setItem(USER_TOKEN, AUTH_TOKEN)

        const actualUserToken = userService.loadAuthTokenFromStorage()
        expect(actualUserToken).toBe(AUTH_TOKEN)
        expect(axios.defaults.headers.common[X_AUTH_TOKEN_HEADER]).toBe(AUTH_TOKEN)
    })
})