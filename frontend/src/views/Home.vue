<template>
  <div class="home">
    <v-container>
      {{$vuetify.breakpoint.name}}
      </v-container> 
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'home',
  components: {
  },
  mounted(){
    // console.log(this.$vuetify)
  }
}
</script>
