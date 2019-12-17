<template>
    <v-card id="nav" style="display:flex; align-items: center">
        <v-container style="display: flex; justify-content: space-between; align-items: center; height: 5vh;">
            <v-btn to="/" color="teal" text value="home">
                <span>Home</span>
            </v-btn>
            <v-col cols="6">
            <Search id="search"/>
            </v-col>
            <v-btn
                    to="/about"
                    color="teal"
                    text>
                <span>About stuff</span>
            </v-btn>
            <v-btn
                    to="/login"
                    color="teal"
                    text
                    value="login"
                    v-if="!isLoggedin"
            >
                <span>Login</span>
            </v-btn>

            <v-btn
                    color="teal"
                    text
                    value="log out"
                    v-if="isLoggedin"
                    @click="changeStatus"
            >
                <span>Log out </span>
            </v-btn>
            <v-btn
                    color="teal"
                    text
                    value="Google Account"
                    v-if="isLoggedin"
                    @click="googleLogin"
            >
                <span>Google Account</span>
            </v-btn>
        </v-container>

      

  </v-card>
</template>

<script>
  import Search from "@/components/SearchInput.vue"
import Cookie from "js-cookie";

  export default {
    components: {
      Search
    },
name: "Navbar",
      data(){
        return {
            isLoggedin: this.$store.state.isLoggedin
        }
    },
    methods: {
      changeStatus() {
        this.$store.dispatch('logout')

      },
        async signInCallback(authResult){

            console.log('authResult', authResult);

            if (authResult['code']) {
                // Hide the sign-in button now that the user is authorized
                //$('#signinButton').hide();

                // Send the code to the server
                let token = Cookie.get("token");
                let result = await fetch('api/gauth', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/octet-stream; charset=utf-8',
                        'X-Requested-With': 'XMLHttpRequest',
                        'Authorization': `Bearer ${token}`
                    },
                    body: authResult['code']
                });
                console.log(result)
            } else {
                console.log("error")
            }

        },
        googleLogin(){
            window.auth2.grantOfflineAccess().then(this.signInCallback);
        }
    },
    watch: {
      status(newValue) {
        if (newValue === true) {
          this.isLoggedin = true;
        } else {
          this.isLoggedin = false;
        }
      }
    },
    computed: {
      status() {
        return this.$store.state.isLoggedin
      }
    }
};
</script>

<style scoped>
    #nav {
        min-height: 66px;
        z-index: 999;
        padding: 5px;
    }

    #search {
        margin-top: 5px;
    }

    #alert {
        color: red;
    }
    
</style>
