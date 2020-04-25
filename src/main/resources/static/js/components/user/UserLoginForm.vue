<template>
    <v-form ref="form">

        <v-text-field
                label="Login"
                v-model="form.login"
                outlined
                :error-messages="errors.login"
                @input="fieldErrors('login')"
                @blur="fieldErrors('login')"/>

        <v-text-field
                label="Password"
                type="password"
                v-model="form.password"
                outlined
                :error-messages="errors.password"
                @input="fieldErrors('password')"
                @blur="fieldErrors('password')"/>

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
    import {maxLength, minLength, required} from 'vuelidate/lib/validators'
    import {getErrorMessagesForParam} from 'utils/errorMessageUtils'
    import UserService from 'service/userService'

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
                snackbar: {
                    isSuccess: false,
                    isShowing: false,
                    message: ''
                }
            }
        },
        validations: {
            form: {
                login: {
                    required,
                    minLength: minLength(3),
                    maxLength: maxLength(20)
                },
                password: {
                    required,
                    minLength: minLength(6),
                    maxLength: maxLength(50)
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

                UserService.login(this.form.login, this.form.password)
                    .then(() => {
                        this.snackbar.isSuccess = true;
                        this.snackbar.message = "Login successfully";
                        this.snackbar.isShowing = true;
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