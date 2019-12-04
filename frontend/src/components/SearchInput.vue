<template>
    <v-text-field
            class="searchField"
            outlined
            label="Search"
            append-icon="search"
            v-model="search"
            v-on:keyup.enter="searchForMovies">
    </v-text-field>
    
</template>

<script>
  export default {
    name: "SearchInput",
    data () {
      return {
        search: ""
      }
    },
    methods: {
      async searchForMovies() {
          let response = await fetch('api/movies/search?s=' + this.search);
          response = response.status === 200 ? response.json() : null;
          return response;
      }
    }
  }

</script>

<style scoped>

</style>