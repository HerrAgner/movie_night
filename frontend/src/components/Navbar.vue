<template>
  <v-card flat id="nav">
    <v-bottom-nav :value="true" absolute color="white">
      <v-btn to="/" color="teal" flat value="home">
        <span>Home</span>
      </v-btn>

      <v-btn
        to="/login"
        color="teal"
        flat
        value="LoginRegister"
        v-if="!statusT"
      >
        <span>Login</span>
      </v-btn>

    </v-bottom-nav>
  </v-card>
</template>

<script>
export default {
  name: "Navbar",
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
