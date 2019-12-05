<template>
  <div class="home">
    <v-container>
      <div>
        Hi {{username}}!
      </div>
      </v-container> 
  </div>
</template>

<script>
import Cookie from "js-cookie";

// @ is an alias to /src

export default {
  name: 'home',
  data: () => ({
    username: ''
  }),
  components: {
  },
  async mounted(){   
    if (Cookie.get("token") !== undefined){
      let token = Cookie.get("token");
      let res = await fetch('/api/login', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        },
      });
      if (res.status === 200){
        res = await res.json();
        this.username = res.username
      }else {
        await this.$router.push({path: '/login'})
        this.$store.state.isLoggedin = false

      }
  }
      
  }
}
</script>
