<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Page title</v-toolbar-title>
        </v-app-bar>
        <v-content>
            <v-container>
                <user-login-form/>
                <br/>
                <user-registration-form/>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import UserLoginForm from "components/user/UserLoginForm.vue";
    import UserRegistrationForm from "components/user/UserRegistrationForm.vue";

    export default {
        name: "App",
        components: {UserLoginForm, UserRegistrationForm}
    }
</script>

<style scoped>

</style>