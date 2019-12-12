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
                    <v-row>
                        <v-col cols="12" md="9" class="movie_info">
                            <v-card-title
                                    class="headline"
                                    v-text="item.eventName"
                            />

                            <v-row >
                                <v-col class="friendChip2" cols="12" md="3">
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

                            <v-row >
                                <v-col class="friendChip" cols="12" md="2">
                                    <v-card-text>Date: </v-card-text>
                                </v-col>
                                <v-col cols="12" sm="12" md="2" class="dateTime">
                                    <v-card-text class="dateTime">
                                            {{item.startTime.split('T')[0]}}
                                    </v-card-text>
                                </v-col>
                            </v-row>


                            <v-row >
                                <v-col class="friendChip" cols="12" md="2">
                                    <v-card-text>Time: </v-card-text>
                                </v-col>
                                <v-col cols="12" sm="12" md="2" class="dateTime">
                                    <v-card-text class="dateTime">
                                        {{item.startTime.split('T')[1]}}
                                    </v-card-text>
                                </v-col>
                            </v-row>

                        </v-col>

                        <v-col cols="12" md="2" class="movie_poster_container" :class="breakpointSmAndDown
                                    && 'poster_below_sm'">
                            <v-img v-if="isLoading" :src="item.poster" class="movie_poster" alt="Image not found"/>
                        </v-col>

                    </v-row>

                            <EventEditor v-if = "item.creator === getCurrentUser()" :event="item" :key="i"/>
                            <v-btn text v-if = "item.creator === getCurrentUser()" @click="deleteEvent(item.eventId)">Delete</v-btn>

                </v-card>
            </v-col>
        </v-row>

</template>

<script>
import EventsService from "../services/EventsService";
import EventEditor from "./EventEditor";
import movieDetailsService from "../services/movieDetailsService";
import GCalendarService from "../services/GCalendarService";

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
            deleteEvent(eventId){
                console.log(eventId, 'events')

                GCalendarService().deleteEvent(eventId)
            },
            breakpointSmAndDown() {
                return this.$vuetify.breakpoint.smAndDown;
            },
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
        }
    }
</script>

<style scoped>

    .friendChip {
        padding: 0;
    }
    .friendChip2{
        padding: 0;
        margin-left: -0.4vw;
    }
    .invitedFriendsList{
        padding: 0;
        margin-left: -3vw;
        margin-top: 0.8vh;
    }

    .dateTime{
        padding: 0;
        margin-left: -2.5vw;
        margin-top: 0.8vh;
    }

</style>
