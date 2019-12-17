<template>
  <v-app>
    <v-content>
      <div id="app">
        <Navbar />
        <router-view />
      </div>
    </v-content>
  </v-app>
</template>
<script>
  import Navbar from './components/Navbar';
  import Cookie from "js-cookie";
  
  export default {
  name: 'App',
  components: {
    Navbar
  },
  mounted(){
    if (Cookie.get("token") !== undefined) {
      this.$store.state.isLoggedin = true
      this.$store.state.cookie = Cookie.get("token");
    }
    if(this.$route.query.redirect) {
      this.$router.push({path: this.$route.query.redirect})
    }
  }
};
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
