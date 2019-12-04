<template>
    <v-container>
        <v-text-field
                class="searchField"
                outlined
                label="Search"
                append-icon="search"
                v-model="search"
                v-on:keyup.enter="searchForMovies"
                style="position: absolute; width: 30%; top: 5px">
        </v-text-field>
        <v-list style="position: absolute; top: 6vh; width: 30%">
            <SearchResult v-for="search in searches"
                          :key="search.imdbID"
                          :poster="search.Poster"
                          :title="search.Title"
                          :year="search.Year"
                          :id="search.imdbID"/>
        
        </v-list>
    
    </v-container>
</template>

<script>
  import SearchResult from "./SearchResult";

  export default {
    name: "SearchInput",
    components: {SearchResult},
    data() {
      return {
        search: "",
        searchResponse: []
      }
    },
    methods: {
      async searchForMovies() {
        let response = await fetch('api/movies/search?s=' + this.search);
        response = response.status === 200 ? await response.json() : null;
        this.searchResponse = response.Search;
        return response;
      }
    },
    computed: {
      searches() {
        return this.searchResponse;
      }
    }
  }

</script>

<style scoped>

</style>