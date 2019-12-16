<template>
    <v-container>
        
        <v-text-field
                class="searchField"
                outlined
                label="Search"
                append-icon="search"
                v-model="search"
                v-on:keyup="searchForMovies(500)"
                v-on:focus="focusTextField"
                style="position: absolute; width: 30%; top: 5px; z-index: 2; height: 5.5vh;"
        >
        </v-text-field>
        <transition name="fade">
            <div @click="closeMenu" v-if="movieList.length > 0" class="overlay"
                 style="position:fixed; top:0; left:0; width: 100vw; height: 100vh; background-color: rgba(10,10,10,0.4); z-index: 1"/>
        </transition>
        <transition name="fade">
            <Loading class="listComponent" v-if="isLoading && searching && movieList.length === 0" style="right: 25%;"/>
            <v-list class="listComponent" v-else-if="movieList.length > 0"
                    style="max-height: 60vh; overflow-x:scroll;">
                <SearchResult v-for="search in searches"
                              :key="search.imdbID"
                              :poster="search.Poster"
                              :title="search.Title"
                              :year="search.Year"
                              :id="search.imdbID"
                              v-on:closemenu="closeMenu"/>
                <v-btn @click="loadMoreMovies">Load more</v-btn>
            </v-list>
            <v-container class="listComponent"
                         v-else-if="!isLoading && this.search.length > 0 && this.searchResponse === 0"
                         style="background-color: white">No movies found
            </v-container>
        </transition>
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
        movieList: [],
        loading: true,
        searching: false,
        pageNumber: 1,
        suggest: [],
        suggestNumber: 0
      }
    },
    methods: {
      async suggestMovies(letter, word) {
        this.suggest = [];
        let response = await fetch('api/movies/suggest?l=' + letter + '&s=' + word);
        response = response.status === 200 ? await response.json() : null;
        response.d.forEach(text => this.suggest.push(text.l))
      },

      async searchForMovies(timeout) {
        if (this.search.length > 2) {

          this.suggestMovies(this.search.charAt(0), this.search);
          this.searching = true;
          this.loading = true;
          setTimeout(async () => {
            let response;
            if (this.suggest[this.suggestNumber] !== undefined && this.search.length > 0) {
              response = await fetch('api/movies/search?s=' + this.suggest[this.suggestNumber] + "&p=" + this.pageNumber);
              response = response.status === 200 ? await response.json() : null;
              this.searchResponse = response ? response.Search : [];
            }
            if (this.searchResponse.length < 2) {
              response = await fetch('api/movies/search?s=' + this.search + "&p=" + this.pageNumber);
              response = response.status === 200 ? await response.json() : null;
              this.searchResponse = response ? response.Search : [];
            }
            this.loading = false;
            this.searching = false;
            this.addMoviesToList()
            return response;
          }, timeout);
        }
      },
      closeMenu() {
        this.search = "";
        this.movieList = [];
        this.pageNumber = 1;
      },
      focusTextField() {
        this.movieList = [];
        this.pageNumber = 1;

      },
      addMoviesToList() {
        if (this.loading === false) {
          let tempArray = this.movieList.concat(this.searchResponse);
          tempArray = tempArray.filter((thing, index, self) =>
            index === self.findIndex((t) => (
              t.imdbID === thing.imdbID
            ))
          )
          this.movieList = tempArray
        }
      },
      loadMoreMovies() {
        this.pageNumber++;
        this.searchForMovies(10)
      }
    },
    computed: {
      searches() {
        return this.movieList
      },
      isLoading() {
        return this.loading;
      },
      searchChange() {
        return this.search;
      }
    },
    watch: {
      searchChange: function (value, oldValue) {
        if (value !== oldValue) {
          this.movieList = [];
        }
      },

    }
  }

</script>

<style scoped>
    .listComponent {
        position: absolute;
        top: 6vh;
        width: 30%;
        z-index: 2;
    }
    
    
    .fade-enter-active, .fade-leave-active {
        transition: ease-in-out opacity 0.2s;
    }
    
    .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
    {
        opacity: 0;
    }
    
    @keyframes fadein {
        from {
            opacity: 0;
        }
        to {
            opacity: 1;
        }
    }
</style>

