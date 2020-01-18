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

        <v-snackbar :timeout="0" v-model="this.snackbar.isShowing">
            <v-icon color="success" v-if="this.snackbar.isSuccess">done</v-icon>
            <v-icon color="error" v-else>warning</v-icon>

            <span>{{this.snackbar.message}}</span>
            <v-btn @click="snackbar.isShowing = false">
                Close
            </v-btn>
        </v-snackbar>
    </v-form>
</template>

<script>
    import axios from 'axios'

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
                },
                snackbar: {
                    isSuccess: false,
                    isShowing: false,
                    message: ''
                }
            }
        },
        methods: {
            onSubmit(evt) {
                evt.preventDefault();

                axios.post('/users/register', JSON.stringify(this.form), {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    this.snackbar.isSuccess = true;
                    this.snackbar.message = "Registered successfully";
                    this.snackbar.isShowing = true;
                    console.log(response);
                })
                .catch(errorResponse => {
                    this.snackbar.isSuccess = false;
                    if (errorResponse.response.status === 400) {
                        this.snackbar.message = errorResponse.response.data.message;
                        console.log(errorResponse);
                    } else {
                        this.snackbar.message = 'Internal Server Error. Try later';
                        console.log('Server error');
                    }
                    this.snackbar.isShowing = true;
                });
            }
        }
    }
</script>

<style scoped>

</style>