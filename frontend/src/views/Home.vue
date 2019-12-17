<template>
  <div class="home">
    <v-container>
      <div>
        <h2 v-if="this.$store.state.isLoggedin">Hi {{getUsername}}!</h2>
        <h2 v-else> Hi you!</h2>
        <Events v-if="this.$store.state.isLoggedin" />
      </div>
      </v-container>
  </div>
</template>

<script>
import Events from '@/components/Events';
import GetUserService from "../services/GetUserService";

export default {
  name: 'home',
  components: {
    Events
  },
  computed: {
    getUsername() {
      return this.$store.state.loggedInUser;
    }
  },
  mounted() {
    GetUserService().getUser();
  }
}
</script>
