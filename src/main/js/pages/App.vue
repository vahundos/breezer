<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title @click="showMain">Breezer</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn v-if="this.$store.state.userAuthToken" icon @click="logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
            <v-btn v-if="!this.$store.state.userAuthToken" text @click="showLogin">
                Login
            </v-btn>
            <v-btn v-if="!this.$store.state.userAuthToken" text @click="showRegistration">
                Registration
            </v-btn>
        </v-app-bar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import { mapActions } from 'vuex'

    export default {
        name: "App",
        methods: {
            ...mapActions(['userLogout']),
            logout() {
                this.$router.push('/');
                this.userLogout();
            },
            showMain() {
                this.$router.push('/')
            },
            showLogin() {
                this.$router.push('/login')
            },
            showRegistration() {
                this.$router.push('/registration');
            }
        }
    }
</script>

<style scoped>

</style>