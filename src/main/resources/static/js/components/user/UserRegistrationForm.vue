<template>
    <v-form ref="form">
        <v-text-field
                label="First name"
                outlined
                v-model="form.firstName"
                :rules="[rules.required]"
                :error-messages="errors.firstName"/>


        <v-text-field
                label="Second name"
                outlined
                v-model="form.secondName"
                :rules="[rules.required]"
                :error-messages="errors.secondName"/>


        <v-text-field
                label="Nickname"
                outlined
                v-model="form.nickname"
                :rules="[rules.required]"
                :error-messages="errors.nickname"/>


        <v-text-field
                label="Email"
                outlined
                v-model="form.email"
                :rules="[rules.required]"
                :error-messages="errors.email"/>

        <v-row>
            <v-col>
                <v-text-field
                        label="Password"
                        type="password"
                        outlined
                        v-model="form.password"
                        :rules="[rules.required]"
                        :error-messages="errors.password"/>
            </v-col>
            <v-col>
                <v-text-field
                        label="Confirm password"
                        type="password"
                        outlined
                        v-model="form.passwordConfirmation"
                        :rules="[rules.required, computed.passwordMatch]"
                        :error-messages="errors.passwordConfirmation"/>
            </v-col>
        </v-row>

        <v-layout align-center justify-end>
            <v-btn color="primary" @click.prevent="onSubmit">
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
                errors: {
                    firstName: [],
                    secondName: [],
                    nickname: [],
                    email: [],
                    password: [],
                    passwordConfirmation: []
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
            onSubmit() {
                if (!this.$refs.form.validate()) {
                    return;
                }

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
                    if (errorResponse.response.status === 400) {
                        console.log(errorResponse);

                        for (const fieldError of errorResponse.response.data) {
                            this.errors[fieldError.fieldName] = fieldError.errors;
                        }
                    } else {
                        this.snackbar.isSuccess = false;
                        this.snackbar.isShowing = true;
                        this.snackbar.message = 'Internal Server Error. Try later';
                        console.log('Server error');
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>