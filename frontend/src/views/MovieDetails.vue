<template>
  <v-container>
    <Loading v-if="isLoading" />
    <v-container
      v-else-if="!isLoading && getMovie"
      class="movie_info_container"
    >
      <v-row justify="center">
        <v-col
          cols="12"
          md="4"
          class="movie_poster_container"
          :class="breakpointSmAndDown && 'poster_below_sm'"
        >
          <v-img :src="getPoster" class="movie_poster" alt="Image not found" />
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

export default {
  name: 'MovieDetails',
  components: {
    Loading
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
        : 'assets/not-found.png';
    },
    getRating() {
      return this.getMovie.imdbRating;
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
        this.$router.push({ path: '/' });
      }
    }
  },
  mounted() {
    this.fetchMovie();
  },
  watch: {
    getRoute: function(value, oldValue) {
      if (value !== oldValue) {
        this.fetchMovie();
      }
    }
  }
};
</script>

<style>
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
