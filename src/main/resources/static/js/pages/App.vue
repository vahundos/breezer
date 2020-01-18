<template>
    <v-app>
        <v-app-bar dark app>
            <v-toolbar-title>Page title</v-toolbar-title>
        </v-app-bar>
        <v-content>
            Hello Vue
        </v-content>
    </v-app>
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