<template>
    <div>
        <Loading v-if="isLoading && events.length === 0" />
        <v-row>
            <v-col
                    v-for="(item, i) in events"
                    :key="item.eventId"
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

                            <v-card-text class="text-md-left eventInfo thick">
                                Event creator: {{item.creator}}
                            </v-card-text>

                            <v-card-text class="text-md-left eventInfo thick">
                                Date: {{item.startTime.split('T')[0]}}
                            </v-card-text>

                            <v-card-text class="text-md-left eventInfo thick">
                                Time: {{item.startTime.split('T')[1]}} - {{item.endTime.split('T')[1]}}
                            </v-card-text>

                            <v-row justify="start" class="eventInfo invitedFriendsBlock">
                                <div >
                                    <v-card-text class="thick">Invited friends:</v-card-text>
                                </div>

                                <div >
                                    <v-card-text class="friendsList" v-for="(friend, i) in item.attendees" :key="i">
                                        <v-row justify="start">
                                            <v-icon>account_circle</v-icon>
                                            <p class="attendeeName thick">{{friend}}</p>
                                        </v-row>
                                    </v-card-text>
                                </div>
                            </v-row>

                        </v-col>

                        <v-col cols="12" md="2" class="poster" :class="breakpointSmAndDown
                                    && 'poster_below_sm'">
                            <v-img :src="getPoster(item)" class="movie_poster" alt="Image not found"/>
                        </v-col>

                    </v-row>

                    <v-row>
                        <v-col md="6">
                            <EventEditor v-if = "item.creator === getCurrentUser()" :event="item" :key="i" v-on:childToParent="eventUpdated"/>
                        </v-col>

                        <v-col md="6">
                            <v-btn text v-if = "item.creator === getCurrentUser()" @click="deletePopup(item.eventId)">Delete</v-btn>
                        </v-col>

                    </v-row>

                </v-card>
            </v-col>
            <v-btn v-if="!this.noMore" block color="secondary" @click="getMyEvents" dark>More..</v-btn>
            <div v-else class="flex">
                <h2 class="justify-center">No events</h2>
            </div>
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
                        <v-btn color="primary" text @click="deleteEvent()">YES</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
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
            isLoading: false,
            dialog: false,
            events: [],
            moviesID: [],
            pageCounter: 0,
            noMore: true,
            selectedEventId: ''
        }),
        methods: {
            getPoster(item) {
                return item && item.poster && item.poster !== 'N/A'
                    ? item.poster
                    : 'not-found.jpg';
                },
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
            deletePopup(eventId){
                this.selectedEventId = eventId;
                this.dialog = true;
            },
            async deleteEvent() {
                let eventDeleted = await GCalendarService().deleteEvent(this.selectedEventId);
                if (eventDeleted) {
                    this.events = this.events.filter(event => event.eventId !== this.selectedEventId);
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
                this.isLoading = true;
                let res = await EventsService().getAllEvents(this.pageCounter);
                if (res.length !== 0) {
                    this.noMore = false;
                    this.moviesID = [];
                    await res.forEach(item => {
                        this.events.push(item);
                        this.moviesID.push({imdbID: item.movieId})
                    });
                    this.isLoading = false;
                    await this.getEventsPoster();
                    this.pageCounter++
                }else {
                    this.isLoading = false;
                    this.noMore = true
                }
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
            if (this.$store.state.isLoggedin){
                await this.getMyEvents();
            }
        }
    }
</script>

<style scoped>

    .invitedFriendsBlock{
        margin-left: 0vw;
    }

    .eventInfo{
        margin-top: 2vh;
        margin-bottom: -3vh;
    }
    .friendsList{
        margin-top: 1.7vh;
        padding: 0;
        margin-bottom: 2vh;
    }
    .attendeeName{
        margin-left: 0.3vw;
        margin-bottom: 0;
    }
    .poster{
        margin-top: 2vh;
    }
    .thick{
        font-weight: bold;
    }

</style>
