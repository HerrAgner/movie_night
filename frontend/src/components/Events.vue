<template>
    <div>
        <Loading v-if="isLoading" />
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
                        <v-img :src="item.poster" class="movie_poster" alt="Image not found"/>
                    </v-col>

                </v-row>

                <v-row>
                    <v-col md="6">
                        <EventEditor v-if = "item.creator === getCurrentUser()" :event="item" :key="i" v-on:childToParent="eventUpdated"/>
                    </v-col>

                    <v-col md="6">
                        <v-btn text v-if = "item.creator === getCurrentUser()" @click="deletePopup">Delete</v-btn>
                    </v-col>

                </v-row>

                <v-dialog
                        v-model="dialog"
                        width="500"
                        :retain-focus="false"
                >
                    <v-card>
                        <v-card-title
                                class="headline grey lighten-2"
                                primary-title
                        >
                            Delete event
                        </v-card-title>

                        <v-card-text>
                            <h3>Are you sure you want to delete this event?</h3>
                        </v-card-text>

                        <v-divider></v-divider>

                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" text @click="dialog = false">NO</v-btn>
                            <v-btn color="primary" text @click="deleteEvent(item.eventId)">YES</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-card>
        </v-col>
        <v-btn v-if="!this.noMore" block color="secondary" @click="getMyEvents" dark>More..</v-btn>
        <div v-else class="flex">
            <h2 class="justify-center"> No More</h2>
        </div>
    </v-row>
    </div>
</template>

<script>
import EventsService from "../services/EventsService";
import EventEditor from "./EventEditor";
import movieDetailsService from "../services/movieDetailsService";
import GCalendarService from "../services/GCalendarService";
import Loading from '@/components/Loading';

    export default {
        components: {
            EventEditor,
            Loading
        },
        data: () => ({
            isLoading: true,
            dialog: false,
            events: [],
            moviesID: [],
            pageCounter: 0,
            noMore: false
        }),
        methods: {
            eventUpdated(eventFromChild){
                this.events.forEach(event => {
                    if(event.eventId === eventFromChild.eventId){
                        event.eventName = eventFromChild.eventName;
                        event.startTime = eventFromChild.startTime;
                        event.endTime = eventFromChild.endTime;
                        event.attendees = eventFromChild.attendees
                    }
                });
            },
            deletePopup(){
                this.dialog = true;
            },
            async deleteEvent(eventId) {
                console.log(eventId);
                let eventDeleted = GCalendarService().deleteEvent(eventId);
                if (eventDeleted) {
                    this.events = this.events.filter(event => event.eventId !== eventId);
                    this.dialog = false;
                }

            },
            breakpointSmAndDown() {
                return this.$vuetify.breakpoint.smAndDown;
            },
            getCurrentUser() {
                return this.$store.state.loggedInUser
            },
            async getMyEvents(){
                let res = await EventsService().getAllEvents(this.pageCounter);
                if (res.length !== 0) {
                    this.moviesID = [];
                    await res.forEach(item => {
                        this.events.push(item);
                        this.moviesID.push({imdbID: item.movieId})
                    });
                    this.isLoading = false
                    await this.getEventsPoster();
                    this.pageCounter++
                }else this.noMore = true
            },
            async getEventsPoster(){
                let movies = await movieDetailsService().getAllMoviesDetails(this.moviesID);
                movies.forEach(movie => {
                    this.events.forEach(e => {
                        if (e.movieId === movie.imdbID) {
                            e.poster = movie.Poster;
                            e.runtime = movie.Runtime
                        }
                    })
                });

                this.events = [...this.events];
            }
        },
        async mounted() {
            await this.getMyEvents();
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
