<template>
    <v-form>
        <v-text-field
                label="First name"
                outlined
                v-model="form.firstName"
                :rules="[rules.required]"/>


        <v-text-field
                label="Second name"
                outlined
                v-model="form.secondName"
                :rules="[rules.required]"/>


        <v-text-field
                label="Nickname"
                outlined
                v-model="form.nickname"
                :rules="[rules.required]"/>


        <v-text-field
                label="Email"
                outlined
                v-model="form.email"
                :rules="[rules.required, rules.email]"/>

        <v-row>
            <v-col>
                <v-text-field
                        label="Password"
                        type="password"
                        outlined
                        v-model="form.password"
                        :rules="[rules.required]"/>
            </v-col>
            <v-col>
                <v-text-field
                        label="Confirm password"
                        type="password"
                        outlined
                        v-model="form.passwordConfirmation"
                        :rules="[rules.required, computed.passwordMatch]"/>
            </v-col>
        </v-row>

        <v-layout align-center justify-end>
            <v-btn color="primary" @click="onSubmit">
                Register
            </v-btn>
        </v-layout>
    </v-form>
</template>

<script>
    export default {
        name: "UserRegistrationForm",
        data() {
            return {
                form: {
                    firstName: '',
                    secondName: '',
                    nickname: '',
                    email: '',
                    password: '',
                    passwordConfirmation: ''
                },
                rules: {
                    required: value => !!value || 'Required.',
                    email: value => {
                        const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                        return pattern.test(value) || 'Invalid e-mail.'
                    }
                },
                computed: {
                    passwordMatch: () => this.form.password === this.form.passwordConfirmation || 'Passwords are different'
                }
            }
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault();
                alert(JSON.stringify(this.form))
            }
        }
    }
</script>

<style scoped>

</style>