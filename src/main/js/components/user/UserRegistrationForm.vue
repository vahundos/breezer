<template>
    <v-container>
        <v-form ref="form">
            <v-text-field
                    label="First name"
                    outlined
                    v-model="form.firstName"
                    :error-messages="errors.firstName"
                    @input="fieldErrors('firstName')"
                    @blur="fieldErrors('firstName')"/>


            <v-text-field
                    label="Second name"
                    outlined
                    v-model="form.secondName"
                    :error-messages="errors.secondName"
                    @input="fieldErrors('secondName')"
                    @blur="fieldErrors('secondName')"/>


            <v-text-field
                    label="Nickname"
                    outlined
                    v-model="form.nickname"
                    :error-messages="errors.nickname"
                    @input="fieldErrors('nickname')"
                    @blur="fieldErrors('nickname')"/>


            <v-text-field
                    label="Email"
                    outlined
                    v-model="form.email"
                    :error-messages="errors.email"
                    @input="fieldErrors('email')"
                    @blur="fieldErrors('email')"/>

            <v-row>
                <v-col>
                    <v-text-field
                            label="Password"
                            type="password"
                            outlined
                            v-model="form.password"
                            :error-messages="errors.password"
                            @input="fieldErrors('password')"
                            @blur="fieldErrors('password')"/>
                </v-col>
                <v-col>
                    <v-text-field
                            label="Confirm password"
                            type="password"
                            outlined
                            v-model="form.passwordConfirmation"
                            :error-messages="errors.passwordConfirmation"
                            @input="fieldErrors('passwordConfirmation')"
                            @blur="fieldErrors('passwordConfirmation')"/>
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
    </v-container>
</template>

<script>
    import axios from 'axios'
    import {email, maxLength, minLength, required, sameAs} from "vuelidate/lib/validators";
    import {getErrorMessagesForParam} from 'utils/errorMessageUtils'
    import userService from "service/userService";

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
                snackbar: {
                    isSuccess: false,
                    isShowing: false,
                    message: ''
                }
            }
        },
        validations: {
            form: {
                firstName: {
                    required,
                    minLength: minLength(3),
                    maxLength: maxLength(20)
                },
                secondName: {
                    required,
                    minLength: minLength(3),
                    maxLength: maxLength(20)
                },
                nickname: {
                    required,
                    minLength: minLength(2),
                    maxLength: maxLength(20)
                },
                email: {
                    required,
                    email
                },
                password: {
                    required,
                    minLength: minLength(6),
                    maxLength: maxLength(50)
                },
                passwordConfirmation: {
                    required,
                    sameAsPassword: sameAs('password')
                }
            }
        },
        methods: {
            fieldErrors(paramName) {
                this.$v.form[paramName].$touch();
                this.errors[paramName] = getErrorMessagesForParam(this.$v.form, paramName);
            },
            onSubmit() {
                this.$v.form.$touch();
                if (this.$v.form.$invalid) {
                    console.log('Form is not-well formed');
                    return;
                }

                userService.register(this.form)
                    .then(response => {
                        this.snackbar.isSuccess = true;
                        this.snackbar.message = "Registered successfully";
                        this.snackbar.isShowing = true;
                        console.log(response);

                        this.$router.push('/login');
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