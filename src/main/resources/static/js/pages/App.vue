<template>
    <v-app>
        <v-app-bar dark app>
            <v-toolbar-title>Page title</v-toolbar-title>
        </v-app-bar>
        <v-content>
            <v-container>
                <user-registration-form/>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import UserRegistrationForm from "components/user/UserRegistrationForm.vue";

    export default {
        name: "App",
        components: {UserRegistrationForm}
    }
</script>

<style scoped>

</style>