<template>
  <div class="home">
    <v-container>
      <div>
        Hi {{getUsername}}!

        <CalendarDay /> 
      </div>
      </v-container> 
  </div>
</template>

<script>
import Cookie from "js-cookie";
import CalendarDay from '@/components/CalendarDay';

// @ is an alias to /src

export default {
  name: 'home',
  data: () => ({
    // username: ''
  }),
  components: {
    CalendarDay
  },
  computed: {
    getUsername() {
      return this.$store.state.loggedInUser;
    }
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
        // this.username = res.username
        this.$store.commit('setLoggedInUser', res.username);
      }else {
        await this.$router.push({path: '/login'})
        this.$store.state.isLoggedin = false

      }
  }

  }
}
</script>
