<template>
    <div id="app">
        <h1>Sign Up</h1>
        <v-app id="inspire">
            <v-form
                ref="registerForm"
                v-model="valid"
                lazy-validation
                >
                <v-text-field
                v-model="username"
                :counter="10"
                :rules="usernameRules"
                label="First name"
                required
                ></v-text-field>

                <v-text-field
                v-model="password"
                :rules="passwordRules"
                label="Password"
                required
                ></v-text-field>
            
                <v-btn
                :disabled="!valid"
                color="success"
                @click="validate"
                >
                sign up
                </v-btn> 
                {{messageToClient}}
            </v-form>
      </v-app>
    </div>    
</template>


<script>

export default {
    data: () => ({
    valid: true,
    messageToClient: '',
    password: '',
    passwordRules: [
        v => !!v || 'Password is required'
    ],
    username: '',
    usernameRules: [
        v => !!v || 'First name is required',
        v => (v && v.length <= 10) || 'Name must be less than 10 characters'
    ],
    }),
    methods: {
        async validate() {
            if (this.$refs.registerForm.validate()) {
                //let existUsername = await this.$store.dispatch('checkUsernameInDb', this.username)
                //if (existUsername){
                    //this.messageToClient = 'Username is already taken!'
                //}else {
                    //this.$store.state.userInfo = [];
                    //if (this.$store.state.userInfo.email === undefined){
                        this.messageToClient = '';
                        this.snackbar = true;

                        this.$store.dispatch('addUserToDB',{username:this.username,
                                                            password: this.password,})
                        this.messageToClient = 'Account registered successfully!'
                        this.reset()
                    
                //}
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
