<template>
    <div id="app">
        <h1>Register</h1>
        <v-app id="inspire">
            <v-form
                ref="registerForm"
                v-model="valid"
                lazy-validation
                >
                <v-text-field
                        v-model="username"
                        :counter="8"
                        :rules="usernameRules"
                        label="Username"
                        required
                />

                <v-text-field
                        v-model="password"
                        :type="show ? 'text' : 'password'"
                        :rules="passwordRules"
                        label="Password"
                        required
                        :append-icon="show ? 'remove_red_eye' : 'visibility_off'"
                        @click:append="show = !show"
                />
            
                <v-btn
                :disabled="!valid"
                color="success"
                @click="validate"
                >
                submit
                </v-btn> 
            </v-form>
                {{messageToClient}}
      </v-app>
    </div>    
</template>


<script>
  import AddUserService from '../services/AddUserService.js'

  export default {
    data: () => ({
    valid: true,
    show: false,
    messageToClient: '',
    password: '',
    passwordRules: [
        v => !!v || 'Password is required'
    ],
    username: '',
    usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length <= 8) || 'Name must be less than 8 characters'
    ],
    }),
    methods: {
        async validate() {
            if (this.$refs.registerForm.validate()) {
                this.messageToClient = '';
                this.snackbar = true;
                let addedUser = await AddUserService().addUserToDB({username:this.username, password: this.password,})                
                if (addedUser) {
                    this.messageToClient = 'Account registered successfully!'
                    this.reset()
                }else this.messageToClient = 'Username is already taken!'
            }
        },
        reset() {
            this.$refs.registerForm.reset();
        }
    },
}
</script>

<style scoped>
#app{
  width: 40vw;
  margin-top: 3vh;
  margin-left: 2vw;
  margin-bottom: 0;
  height: 70vh;
}
</style>
