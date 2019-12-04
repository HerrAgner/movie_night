<template>
  <div class="form">
    <Login />
  </div>
</template>

<script>
import Login from "@/components/Login.vue"

export default {
  components:{
    Login
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
