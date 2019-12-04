<template>
  <v-container>
    <Loading v-if="isLoading" />
    <v-container
      v-else-if="!isLoading && getMovie"
      class="movie_info_container"
    >
      <v-row justify-content="center">
        <v-col cols="4" class="movie_poster">
          <v-img :src="getPoster" />
        </v-col>
        <v-col cols="8" class="movie_info">
          <div class="display-3 text-left">
            {{ getMovie.Title }}
          </div>
          <div class="title text-left">{{ getMovie.imdbRating }}/10</div>
          <div class="title text-left">{{ getMovie.Metascore }}/100</div>
          <table>
            <tr>
              <td>Year:</td>
              <td>{{ getMovie.Year }}</td>
            </tr>
            <tr>
              <td>Genre:</td>
              <td>{{ getGenres }}</td>
            </tr>
            <tr>
              <td>Plot:</td>
              <td>{{ getMovie.Plot }}</td>
            </tr>
            <tr>
              <td>Awards:</td>
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
      return this.movie && this.movie.Poster !== 'N/A' ? this.movie.Poster : '';
    },
    getRating() {
      return Math.floor(this.getMovie.imdbRating);
    },
    getGenres() {
      return this.movie.Genre.join(', ');
    },
    isLoading() {
      return this.loading;
    },
    getRoute(){
      return this.$route;
    }
  },
  methods: {
    async fetchMovie() {
      if (this.$route.query.id) {
        this.loading = true;
        this.movie = await movieDetailsService().getMovieDetails(
          this.$route.query.id
        );
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
      if(value !== oldValue) {
        this.fetchMovie();
      }

    }
  }
};
</script>

<style></style>
