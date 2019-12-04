<template>
  <v-card text id="nav">
      <v-btn to="/" color="teal" text value="home">
        <span>Home</span>
      </v-btn>

      <v-btn
        to="/login"
        color="teal"
        text
        value="LoginRegister"
      >
        <span>Login</span>
      </v-btn>

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
