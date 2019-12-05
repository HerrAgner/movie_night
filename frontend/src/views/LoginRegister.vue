<template>
  <div class="form">
    <Login />
    <Register />
  </div>
</template>

<script>
import Login from "@/components/Login.vue"
import Register from "@/components/Register.vue"

export default {
  components:{
    Login,
    Register
  }
}
</script>

<style scoped>
.form{
  overflow: hidden;
  display: flex;   
  height: 70vh;
  margin-left: 6vw;
}
</style>
