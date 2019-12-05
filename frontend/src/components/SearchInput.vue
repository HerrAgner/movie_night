<template>
    <v-container>
        <v-text-field
                class="searchField"
                outlined
                label="Search"
                append-icon="search"
                v-model="search"
                v-on:keyup="searchForMovies"
                style="position: absolute; width: 30%; top: 5px"
                v-on:blur="timedCloseMenu"
        >
        </v-text-field>
        <Loading v-if="isLoading && searching"/>
        <v-list v-else-if="searchResponse.length > 0 && !isLoading" style="position: absolute; top: 6vh; width: 30%">
            <SearchResult v-for="search in searches"
                          :key="search.imdbID"
                          :poster="search.Poster"
                          :title="search.Title"
                          :year="search.Year"
                          :id="search.imdbID"
                          v-on:closemenu="closeMenu"/>
        
        </v-list>
        <v-container v-else-if="!isLoading && this.search.length > 0">No movies found</v-container>
    </v-container>
</template>

<script>
  import SearchResult from "./SearchResult";
  import Loading from '@/components/Loading';


  export default {
    name: "SearchInput",
    components: {Loading, SearchResult},
    data() {
      return {
        search: "",
        searchResponse: [],
        loading: true,
        searching: false
      }
    },
    methods: {
      async searchForMovies() {
        this.searching = true;
        this.loading = true;
        setTimeout(async () => {
          let response = await fetch('api/movies/search?s=' + this.search);
          response = response.status === 200 ? await response.json() : null;
          this.searchResponse = response ? response.Search : [];
          this.loading = false;
          this.searching = false;
          return response;
        }, 1000);
      },
      closeMenu() {
        this.search = "";
        this.searchResponse = [];
      },
      timedCloseMenu() {
        setTimeout(this.closeMenu, 100)
      }
    },
    computed: {
      searches() {
        return this.searchResponse;
      },
      isLoading() {
        return this.loading;
      }
    }
  }

</script>

<style scoped>

</style>