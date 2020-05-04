<template>
    <v-container>
        <v-img :src="imageBase64" width="100" height="100"/>
        <v-btn @click="retrieveUserPicture"/>
    </v-container>
</template>

<script>
    import userService from "service/userService";

    export default {
        name: 'Profile',
        data() {
            return {
                image: ''
            }
        },
        methods: {
            async retrieveUserPicture() {
                const userPicture = await userService.getUserPicture(this.$store.state.id)
                const buffer = Buffer.from(userPicture)

                const binary = buffer.reduce((data, b) => data += String.fromCharCode(b), '');
                this.image = "data:image/png;base64," + btoa(binary);
            }
        },
        computed: {
            imageBase64() {
                return this.image;
            }
        }
    }
</script>

<style scoped>

</style>