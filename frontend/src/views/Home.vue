<template>
  <div class="home">
    <v-container>
      {{username}}
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
      this.res = res.status === 200 ? await res.json() : null;
      this.username = this.res.username
  }
      
  }
}
</script>
