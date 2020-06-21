<template>
    <v-container class="container">
        <v-card>
            <v-card-title>Your profile</v-card-title>
            <v-card-text>Some information can be visible for other users</v-card-text>
        </v-card>
        <v-card>
            <v-row class="align-center">
                <v-col class="text-center">
                    <v-card-text>
                        Photo
                    </v-card-text>
                </v-col>
                <v-col class="text-center">Photo to help recognize you</v-col>
                <v-col class="text-center">
                    <input ref="uploader" class="d-none" type="file" accept="image/png" @change="onFileChange">
                    <v-avatar size="64">
                        <v-img :src="image" @click="onImageClick"/>
                    </v-avatar>
                </v-col>
            </v-row>
        </v-card>
        <v-card>
            <v-row class="align-center">
                <v-col class="text-center">
                    <v-card-text>
                        Name
                    </v-card-text>
                </v-col>
                <v-col class="text-center">{{this.$store.state.user.firstName + ' ' +
                    this.$store.state.user.secondName}}
                </v-col>
                <v-col class="text-center">
                    <v-btn v-if="this.$store.state.authToken" icon>
                        <v-icon>keyboard_arrow_right</v-icon>
                    </v-btn>
                </v-col>
            </v-row>
        </v-card>
        <v-card>
            <v-row class="align-center">
                <v-col class="text-center">
                    <v-card-text>
                        Email
                    </v-card-text>
                </v-col>
                <v-col class="text-center">{{this.$store.state.user.email}}</v-col>
                <v-col class="text-center">
                    <v-btn v-if="this.$store.state.authToken" icon>
                        <v-icon>keyboard_arrow_right</v-icon>
                    </v-btn>
                </v-col>
            </v-row>
        </v-card>
        <v-card>
            <v-row class="align-center">
                <v-col class="text-center">
                    <v-card-text>
                        Password
                    </v-card-text>
                </v-col>
                <v-col class="text-center">*******</v-col>
                <v-col class="text-center">
                    <v-btn v-if="this.$store.state.authToken" icon>
                        <v-icon>keyboard_arrow_right</v-icon>
                    </v-btn>
                </v-col>
            </v-row>
        </v-card>
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
            },
            onImageClick() {
                this.$refs.uploader.click()
            },
            onFileChange(e) {
                const formData = new FormData()
                formData.append("picture", e.target.files[0], e.target.files[0].name)

                userService.saveUserPicture(this.$store.state.user.id, formData)
                    .then(value => {
                        window.location.reload()
                    })
            }
        }
    }
</script>

<style scoped>
    .container {
        min-width: 300px;
        max-width: 1200px;
    }
</style>