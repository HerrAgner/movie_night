<template>
  <v-container>
    <Loading v-if="isLoading" />
    <v-container
      v-else-if="!isLoading && getMovie"
      class="movie_info_container"
    >
      <v-row justify="center">
        <v-col
          cols="6"
          md="4"
          class="movie_poster_container"
          :class="breakpointSmAndDown && 'poster_below_sm'"
        >
          <v-img @click="lurig" :src="getPoster" class="movie_poster" alt="Image not found" />
          <div v-if="breakpointSmAndDown">
            <div class="text-center display-1">
              {{ getMovie.Title }}
            </div>
            <v-rating
              v-if="breakpointSmAndDown"

              dense
              :value="getMovie.imdbRating"
              empty-icon="star_border"
              half-icon="star_half"
              full-icon="star"
              length="10"
              half-increments
              color="orange"
              background-color="orange"
              readonly
            ></v-rating>
          </div>
        </v-col>
        <v-col cols="12" md="8" class="movie_info">
          <div v-if="!breakpointSmAndDown">
            <div class="text-left display-2">
              {{ getMovie.Title }}
            </div>
            <div class="movie_rating">
              <v-rating
                v-if="!breakpointSmAndDown"
                :value="getRating"
                empty-icon="star_border"
                half-icon="star_half"
                full-icon="star"
                length="10"
                half-increments
                color="orange"
                background-color="orange"
                readonly
              ></v-rating>
            </div>
          </div>
          <table class="movie_info_table">
            <tr>
              <td class="movie_info_prop">Year:</td>
              <td>{{ getMovie.Year }}</td>
            </tr>
            <tr>
              <td class="movie_info_prop">Genre:</td>
              <td>{{ getGenres }}</td>
            </tr>
            <tr>
              <td class="movie_info_prop">Plot:</td>
              <td>{{ getMovie.Plot }}</td>
            </tr>
            <tr>
              <td class="movie_info_prop">Awards:</td>
              <td>{{ getMovie.Awards }}</td>
            </tr>
            <popupEvent :movie="movie" v-on:childToParent="eventUpdated"/>
          </table>
        </v-col>
      </v-row>
    </v-container>
    <v-container v-else>
      Could not find movie..
    </v-container>

    <v-snackbar
            v-model="snackbar"
            :timeout="timeout"
            :top="true"
            :color="color"
    >
      {{ text }}
      <v-btn
              color="black"
              text
              @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>

  </v-container>
    <v-container>
        <Loading v-if="isLoading"/>
        <v-container
                v-else-if="!isLoading && getMovie"
                class="movie_info_container"
        >
            <v-row justify="center">
                <v-col
                        cols="6"
                        md="4"
                        class="movie_poster_container"
                        :class="breakpointSmAndDown && 'poster_below_sm'"
                >
                    <v-img @click="lurig" :src="getPoster" class="movie_poster" alt="Image not found"/>
                    <div v-if="breakpointSmAndDown">
                        <div class="text-center display-1">
                            {{ getMovie.Title }}
                        </div>
                        <v-rating
                                v-if="breakpointSmAndDown"
                                
                                dense
                                :value="getMovie.imdbRating"
                                empty-icon="star_border"
                                half-icon="star_half"
                                full-icon="star"
                                length="10"
                                half-increments
                                color="orange"
                                background-color="orange"
                                readonly
                        ></v-rating>
                    </div>
                </v-col>
                <v-col cols="12" md="8" class="movie_info">
                    <div v-if="!breakpointSmAndDown">
                        <div class="text-left display-2">
                            {{ getMovie.Title }}
                        </div>
                        <div class="movie_rating">
                            <v-rating
                                    v-if="!breakpointSmAndDown"
                                    :value="getRating"
                                    empty-icon="star_border"
                                    half-icon="star_half"
                                    full-icon="star"
                                    length="10"
                                    half-increments
                                    color="orange"
                                    background-color="orange"
                                    readonly
                            ></v-rating>
                        </div>
                    </div>
                    <table class="movie_info_table">
                        <tr>
                            <td class="movie_info_prop">Year:</td>
                            <td>{{ getMovie.Year }}</td>
                        </tr>
                        <tr>
                            <td class="movie_info_prop">Genre:</td>
                            <td>{{ getGenres }}</td>
                        </tr>
                        <tr>
                            <td class="movie_info_prop">Plot:</td>
                            <td>{{ getMovie.Plot }}</td>
                        </tr>
                        <tr>
                            <td class="movie_info_prop">Awards:</td>
                            <td>{{ getMovie.Awards }}</td>
                        </tr>
                        <popupEvent :movie="movie"/>
                    </table>
                </v-col>
            </v-row>
        </v-container>
        <v-container v-else>
            Could not find movie..
        </v-container>
    </v-container>
</template>

<script>
  import movieDetailsService from '@/services/movieDetailsService';
  import Loading from '@/components/Loading';
  import popupEvent from '@/components/popupEvent';


  export default {
    name: 'MovieDetails',
    components: {
      Loading,
      popupEvent
    },
    data: () => ({
      movie: null,
      loading: true
    }),
    computed: {
      getMovie() {
        return this.movie;
      },
      getPoster() {
        return this.movie && this.movie.Poster && this.movie.Poster !== 'N/A'
          ? this.movie.Poster
          : 'not-found.jpg';
      },
      getRating() {
        return this.getMovie.imdbRating && this.getMovie.imdbRating !== 'N/A' ? parseInt(this.getMovie.imdbRating) : 0;
      },
      getGenres() {
        return this.movie.Genre.join(', ');
      },
      isLoading() {
        return this.loading;
      },
      getRoute() {
        return this.$route;
      },
      breakpointSmAndDown() {
        return this.$vuetify.breakpoint.smAndDown;
      }
    },
    methods: {
      async fetchMovie() {
        if (this.$route.query.id) {
          this.loading = true;
          try {
            this.movie = await movieDetailsService().getMovieDetails(
              this.$route.query.id
            );
          } catch (err) {
            console.log(err);
          }
          this.loading = false;
        } else {
          this.$router.push({path: '/'});
        }
      },
      lurig() {
        if (this.movie.Title === "Stranger Things") {
          document.body.style.transition = "transform 3s ease-in-out"
            document.body.style.transform = "rotate(180deg)";
          document.title = "ʇɥƃᴉN ǝᴉʌoW";
          this.$vuetify.theme.dark = true
        }
      }
    },
    mounted() {
      this.fetchMovie();
  name: 'MovieDetails',
  components: {
    Loading,
    popupEvent
  },
  data: () => ({
    movie: null,
    loading: true,
    snackbar: false,
    text: 'Movie event was added.',
    timeout: 5000,
    color: 'green'
  }),
  computed: {
    getMovie() {
      return this.movie;
    },
    getPoster() {
      return this.movie && this.movie.Poster && this.movie.Poster !== 'N/A'
        ? this.movie.Poster
        : 'not-found.jpg';
    },
    getRating() {
      return this.getMovie.imdbRating && this.getMovie.imdbRating !== 'N/A' ? parseInt(this.getMovie.imdbRating) : 0;
    },
    getGenres() {
      return this.movie.Genre.join(', ');
    },
    isLoading() {
      return this.loading;
    },
    getRoute() {
      return this.$route;
    },
    breakpointSmAndDown() {
      return this.$vuetify.breakpoint.smAndDown;
    }
  },
  methods: {
    eventUpdated(data){
      if (data !== true) {
        this.color = 'red';
        this.text = 'Something went wrong'
      }
      this.snackbar = true;
    },
    async fetchMovie() {
      if (this.$route.query.id) {
        this.loading = true;
        try {
          this.movie = await movieDetailsService().getMovieDetails(
            this.$route.query.id
          );
        } catch (err) {
          console.log(err);
        }
        this.loading = false;
      } else {
        this.$router.push({ path: '/' });
      }
    },
    lurig(){
      if (this.movie.Title === "Stranger Things") {
        document.body.style.transform = "rotate(180deg)";
      }
    }
  },
  mounted() {
    this.fetchMovie();

    },
    watch: {
      getRoute: function (value, oldValue) {
        if (value !== oldValue) {
          this.fetchMovie();
        }
      }
    }
  };
</script>

<style scoped>
    .movie_info_table {
        text-align: left;
    }
    
    .movie_info_table td {
        padding: 5px;
    }
    
    .movie_poster {
        width: 100%;
        /* max-width: 300px; */
        /* min-width: 300px; */
    }
    
    .poster_below_sm {
        max-width: 400px;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }
    
    .movie_info_prop {
        font-weight: bold;
        vertical-align: text-top;
    }
    
    .movie_rating {
        display: flex;
        justify-content: flex-start;
    }
</style>
