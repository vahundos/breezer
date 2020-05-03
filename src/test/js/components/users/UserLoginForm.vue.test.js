import Vue from 'vue';
import Vuetify from 'vuetify';
import { mount } from '@vue/test-utils'
import UserLoginForm from 'components/user/UserLoginForm'

Vue.use(Vuetify);

describe('UserLoginForm', () => {

    test('something', () => {
        const wrapper = mount(UserLoginForm)
        const vm = wrapper.vm

        console.log(vm)
    })
})