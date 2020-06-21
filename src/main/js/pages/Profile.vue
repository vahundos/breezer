<template>
    <v-container>
        <v-img :src="image" width="200" height="200"/>
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
        created() {
            this.retrieveUserPicture()
        },
        methods: {
            async retrieveUserPicture() {
                const userPicture = await userService.getUserPicture(this.$store.state.user.id)
                const reader = new FileReader()
                reader.onload = () => {
                    this.image = reader.result
                }
                reader.readAsDataURL(userPicture)
            }
        }
    }
</script>

<style scoped>

</style>