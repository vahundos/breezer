<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Breezer</v-toolbar-title>
            <v-btn v-if="this.$store.state.userAuthToken" absolute right icon @click.prevent="userLogout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <v-container>
                <user-login-form v-if="!this.$store.state.userAuthToken"/>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import UserLoginForm from "components/user/UserLoginForm.vue";
    import UserRegistrationForm from "components/user/UserRegistrationForm.vue";
    import { mapActions } from 'vuex'

    export default {
        name: "App",
        components: {UserLoginForm, UserRegistrationForm},
        methods: {
            ...mapActions(['userLogout'])
        }
    }
</script>

<style scoped>

</style>