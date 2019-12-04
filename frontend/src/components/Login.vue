<template>
    <div id="app">
        <h1>Login</h1>
        <v-app id="inspire">
            <v-form
                ref="loginForm"
                v-model="valid"
                lazy-validation
                >
                <v-text-field
                v-model="email"
                :rules="emailRules"
                label="E-mail"
                required
                ></v-text-field>

                <v-text-field
                v-model="password"
                :rules="passwordRules"
                label="Password"
                :type="'password'"
                required
                ></v-text-field>

                <v-btn
                :disabled="!valid"
                color="success"
                @click="validate"
                >
                Log in
                </v-btn>
                {{messageToClient}}

            </v-form>
      </v-app>
    </div>    
</template>

<script>
export default {
    data: () => ({
    messageToClient: '',
    valid: true,
    password: '',
    passwordRules: [
      v => !!v || 'Password is required'
    ],
    email: '',
    emailRules: [
      v => !!v || 'E-mail is required'
    ]
  }),

  methods: {    
    async validate() {
      if(this.$refs.loginForm.validate()) {
        await this.$store.dispatch('login', 
                      {email: this.email, password: this.password});                       
        if (!this.$store.state.status){
          this.messageToClient = 'E-mail and/or password is incorrect';
        }
                                                                             
      }
    }
  }

}
</script>

<style scoped>
#app{
  width: 40vw;
  margin-top: 3vh;
  margin-left: 2vw;
  margin-bottom: 0;
  height: 60vh;
}

</style>
