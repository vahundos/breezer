<template>
    <v-form ref="form">
        <v-text-field
                label="Login"
                outlined
                v-model="form.login"
                :rules="[rules.required]"
                :error-messages="errors.login"
                v-on:keydown="errors.login = []"/>

        <v-text-field
                label="Password"
                type="password"
                outlined
                v-model="form.password"
                :rules="[rules.required]"
                :error-messages="errors.password"
                v-on:keydown="errors.password = []"/>

        <v-layout align-center justify-end>
            <v-btn color="primary" @click.prevent="onSubmit">
                Login
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
        name: "UserLoginForm",
        data() {
            return {
                form: {
                    login: '',
                    password: ''
                },
                errors: {
                    login: [],
                    password: []
                },
                rules: {
                    required: value => !!value || 'Required.',
                    email: value => {
                        const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                        return pattern.test(value) || 'Invalid e-mail.'
                    }
                },
                snackbar: {
                    isSuccess: false,
                    isShowing: false,
                    message: ''
                }
            }
        },
        methods: {
            onSubmit() {
                if (!this.$refs.form.validate()) {
                    return;
                }

                axios.post('/users/login', '', {
                    headers: {
                        'Authorization': 'Basic ' + btoa(`${this.form.login}:${this.form.password}`)
                    }
                })
                .then(response => {
                    this.snackbar.isSuccess = true;
                    this.snackbar.message = "Login successfully";
                    this.snackbar.isShowing = true;
                    console.log(response);

                    console.log('Auth token is ' + response.data.authToken);
                })
                .catch(errorResponse => {
                    console.error(errorResponse);
                    if (errorResponse.response.status === 400) {
                        for (const fieldError of errorResponse.response.data) {
                            this.errors[fieldError.fieldName] = fieldError.errors;
                        }
                    } else {
                        this.snackbar.isSuccess = false;
                        this.snackbar.isShowing = true;
                        this.snackbar.message = 'Internal Server Error. Try later';
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>