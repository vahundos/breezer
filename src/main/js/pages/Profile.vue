<template>
    <v-container>
        <v-img :src="image" width="200" height="200"/>
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
                const reader = new FileReader()
                reader.onload = () => {
                    console.log(reader.result)
                    this.image = reader.result
                }
                reader.readAsDataURL(userPicture)
            }
        }
    }
</script>

<style scoped>

</style>