<template>
        <v-row>
            <v-col
                    v-for="(item, i) in events"
                    :key="i"
                    cols="12"
            >
                <v-card
                        color="#1F7087"
                        dark
                >
                            <v-card-title
                                    class="headline"
                                    v-text="item.eventName"
                            />

                                <v-row >
                                    <v-col class="friendChip" cols="12" md="2">
                                        <v-card-text>Invited friends:</v-card-text>
                                    </v-col>

                                    <v-col cols="12" sm="12" md="2" class="invitedFriendsList">
                                        <v-card-text
                                            v-for="(friend, i) in item.attendees"
                                                :key="i"
                                            class="invitedFriendsList">
                                            <v-icon>account_circle</v-icon>
                                            {{friend}}
                                        </v-card-text>
                                    </v-col>



                                </v-row>

                            <v-img v-if="isLoading" :src="item.poster" class="movie_poster" alt="Image not found"/>

                            <EventEditor v-if = "item.creator === getCurrentUser()" :event="item" :key="i"/>

                </v-card>
            </v-col>
        </v-row>

</template>

<script>
import EventsService from "../services/EventsService";
import EventEditor from "./EventEditor";
import movieDetailsService from "../services/movieDetailsService";

    export default {
        components: {
            EventEditor
        },
        data: () => ({
            events: [],
            moviesID: [],
            isLoading: false
        }),
        methods: {
            getCurrentUser() {
                return this.$store.state.loggedInUser
            },
            async getMoviePoster(id) {
                let moviePoster = await movieDetailsService().getMovieDetails(id);
                this.isLoading = true;
                //console.log(moviePoster.Poster);
                return moviePoster.Poster;
            }
        },
        async mounted() {
            let res = await EventsService().getAllEvents();
            await res.forEach(item => {
                this.events.push(item);
                this.moviesID.push({imdbID: item.movieId})
            });

            let movies = await movieDetailsService().getAllMoviesDetails(this.moviesID);
            movies.forEach(movie => {
                this.events.forEach(e => {
                    if (e.movieId === movie.imdbID) e.poster = movie.Poster
                })
            });
            this.isLoading = true
            console.log(this.events[0])
        }
    }
</script>

<style scoped>

    .friendChip {
        padding: 0;
    }
    .invitedFriendsList{
        padding: 0;
        margin-left: -3vw;
        margin-top: 0.8vh;
    }
    .poster{
        display: flex;
        justify-content: end;
    }

</style>
