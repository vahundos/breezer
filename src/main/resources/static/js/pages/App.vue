<template>
    <div class="d-flex justify-content-center align-items-center" style="height: 100vh">
        <b-form style="width: 30%" @submit="onSubmit">

            <b-form-group
                    id="input-group-1"
                    label="First name:"
                    label-for="input-1">
                <b-form-input
                        id="input-1"
                        v-model="form.firstName"
                        required
                        placeholder="Enter first name"
                />
            </b-form-group>

            <b-form-group id="input-group-2" label="Second name:" label-for="input-2">
                <b-form-input
                        id="input-2"
                        v-model="form.secondName"
                        required
                        placeholder="Enter second name"
                />
            </b-form-group>

            <b-form-group id="input-group-3" label="Nickname:" label-for="input-3">
                <b-form-input
                        id="input-3"
                        v-model="form.nickname"
                        required
                        placeholder="Enter nickname"
                />
            </b-form-group>

            <b-form-group id="input-group-4" label="Enter email:" label-for="input-4"
                          description="We'll never share your email with anyone else.">
                <b-form-input
                        id="input-4"
                        v-model="form.email"
                        required
                        placeholder="Enter email"
                        type="email"
                />
            </b-form-group>

            <b-form-group id="input-group-5" label="Enter password:" label-for="input-5">
                <b-form-input
                        id="input-5"
                        v-model="form.password"
                        required
                        placeholder="Enter password"
                        type="password"
                />
            </b-form-group>

            <b-form-group id="input-group-6" label="Confirm password:" label-for="input-6">
                <b-form-input
                        id="input-6"
                        v-model="form.passwordConfirmation"
                        required
                        placeholder="Confirm password"
                        type="password"
                />
            </b-form-group>

            <div class="d-flex justify-content-end">
                <b-button type="submit" variant="primary">Submit</b-button>
            </div>
        </b-form>
    </div>
</template>

<script>
    export default {
        name: "App",
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
                show: true
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