<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Page title</v-toolbar-title>
            <v-btn v-if="userToken" absolute right icon @click.prevent="logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <v-container>
                <user-login-form/>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import UserLoginForm from "components/user/UserLoginForm.vue";
    import UserRegistrationForm from "components/user/UserRegistrationForm.vue";
    import UserService from "service/userService";

    export default {
        name: "App",
        components: {UserLoginForm, UserRegistrationForm},
        methods: {
            logout: function () {
                UserService.logout();
            }
        },
        computed: {
            userToken: function () {
                return localStorage.getItem('user-token');
            }
        }
    }
</script>

<style scoped>

</style>