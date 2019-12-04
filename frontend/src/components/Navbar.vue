<template>
  <v-card text id="nav">
      <v-btn to="/" color="teal" text value="home">
        <span>Home</span>
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

  </v-card>
</template>

<script>
export default {
  name: "Navbar",
      data(){
        return {
            isLoggedin: this.$store.state.isLoggedin
        }
    },
    methods:{
      changeStatus(){
          this.$store.dispatch('logout', this.$store.state.userInfo.email)
          
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
  height: 56px;
  z-index: 999;
}

#alert {
  color: red;
}
</style>
