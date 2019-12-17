<template>
    <div id="app">
        <v-container id="inspire">
            <v-row justify="center">
                <v-btn v-if="this.$store.state.isLoggedin" color="primary" dark @click.stop="dialog = true">Create event</v-btn>
                <h4 v-else>You have to login to create an event</h4>

                <v-dialog v-model="dialog" max-width="800">
                    <v-card>
                        <v-card-title class="headline">MOVIE NIGHTS EVENT</v-card-title>

                        <v-card-text>
                            <v-container class="movie_info_container">
                                <v-row justify="center">
                                    <v-col cols="12" md="4" class="movie_poster_container" :class="breakpointSmAndDown
                                    && 'poster_below_sm'">
                                        <v-img :src="movie.Poster" class="movie_poster" alt="Image not found"/>
                                    </v-col>

                                    <v-col cols="12" md="8" class="movie_info">
                                        <div>
                                            <v-col cols="12" sm="6" md="5">
                                                <v-text-field
                                                        v-model="eventName"
                                                        required
                                                        label="Event name"
                                                />
                                            </v-col>

                                            <v-col cols="12" sm="6" md="10">
                                                <v-select cols="12" sm="6" md="4"
                                                          v-model="selectedFriends"
                                                          :items="friends"
                                                          label="Invite friends"
                                                          multiple
                                                >
                                                    <template v-slot:prepend-item>
                                                        <v-list-item
                                                                ripple
                                                                @click="toggle"
                                                        >
                                                            <v-list-item-action>
                                                                <v-icon color="indigo darken-4">{{ icon }}</v-icon>
                                                            </v-list-item-action>
                                                            <v-list-item-content>
                                                                <v-list-item-title>Select All</v-list-item-title>
                                                            </v-list-item-content>
                                                        </v-list-item>
                                                        <v-divider class="mt-2"/>
                                                    </template>
                                                </v-select>


                                                <v-container pa-0>
                                                    <v-row justify="start" class="chipContainer">
                                                        <v-col cols="12" sm="12" md="4"
                                                               v-for="(friend, i) in selectedFriends"
                                                               :key="friend"
                                                               class="friendChip"
                                                        >
                                                            <v-chip cols="12" sm="12" md="12"
                                                                    close
                                                                    color="primary"
                                                            >
                                                                <v-avatar left>
                                                                    <v-icon>account_circle</v-icon>
                                                                </v-avatar>
                                                                {{ friend }}
                                                                <v-avatar left @click="selectedFriends.splice(i, 1)">
                                                                    <v-icon class="ss">cancel</v-icon>
                                                                </v-avatar>

                                                            </v-chip>
                                                        </v-col>
                                                    </v-row>
                                                </v-container>


                                            </v-col>


                                            <v-col cols="12">
                                                <SuggestedEventTimes :attendees=selectedFriends :runtime=movie.Runtime @handleTimeUpdate="handleTimeUpdate" />
                                            </v-col>


                                        </div>
                                    </v-col>
                                </v-row>
                            </v-container>

                        </v-card-text>

                        <v-card-actions>
                            <v-spacer></v-spacer>

                            <v-btn color="green darken-1" :disabled="canceling" text @click="dialog = false">Cancel</v-btn>

                            <v-btn color="green darken-1" :disabled="!saving" text @click="createEvent">Save</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-row>
        </v-container>
    </div>
</template>
<script>
import SuggestedEventTimes from '@/components/SuggestedEventTimes';
  import GCalendarService from "../services/GCalendarService";

  export default {
    name: 'popupEvent',
    props: ['movie'],
    components: {
        SuggestedEventTimes
    },
    data: () => ({
    saving: false,
      dialog: false,
      eventName: '',
      friends: [],
      startTime: null,
      endTime: null,
      menu: false,
      selectedFriends: [],
        canceling: false
    }),
    methods: {
        handleTimeUpdate(data){
            this.startTime = data.split(' - ')[0].replace(' ', 'T');
            this.endTime = data.split(' - ')[1].replace(' ', 'T');
        },
      toggle() {
        this.$nextTick(() => {
          this.inviteAllFriends ? this.selectedFriends = [] : this.selectedFriends = this.friends.slice();
        })
      },
      async createEvent() {
          this.saving = true;
          this.canceling = true;
        const data = {
          movieId: this.movie.imdbID,
          eventName: this.eventName,
          creator: this.$store.state.loggedInUser,
          startTime: this.startTime,
          endTime: this.endTime,
          timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone,
          attendees: this.selectedFriends
        };
        await GCalendarService().createGoogleCalendarEvent(data);
        this.dialog = false;
          this.saving = false;
          this.canceling = false;
      }
    },
    computed: {
      selections() {
        const selections = []

        for (const selection of this.selectedFriends) {
          selections.push(this.friends[selection])
        }

        return selections
      },
      breakpointSmAndDown() {
        return this.$vuetify.breakpoint.smAndDown;
      },
      inviteAllFriends() {
        return this.selectedFriends.length === this.friends.length
      },
        inviteSomeFriends () {
            return this.selectedFriends.length > 0 && !this.inviteAllFriends
        },
        icon () {
            if (this.inviteAllFriends) return 'check_circle';
            if (this.inviteSomeFriends) return 'minimize';
            return 'check_circle_outline'
        },
      getEventName(){
          return  this.eventName
      },
      getSelectedFriends(){
          return  this.selectedFriends
      },
      getDate(){
          return  this.startTime
      }
    },
    async mounted() {
        let token = this.$store.state.cookie;
        if (token !== ''){
            let res = await fetch('/api/event', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
            });
        if (res.status === 200) {
            res = await res.json();
            res.forEach(item => {
                if (this.$store.state.loggedInUser !== item.username && item.googleToken !== null) {
                    this.friends.push(item.username);
                }
            });
        }
    }
    },
      watch: {
          getEventName(value) {
              this.saving = (value !== '' && this.selectedFriends.length !== 0 && this.startTime !== null);
          },
          getSelectedFriends(value) {
              this.saving = (value.length !== 0 && this.eventName !== '' && this.startTime !== null);
          },
          getDate(value) {
              this.saving = !(value === null && this.selectedFriends.length === 0 && this.eventName === '');
          }
      }
  };
</script>
<style>
    .chipContainer {
        padding: 0;
        /* margin-top: -3vh; */
        height: 50px;
    }

    .friendChip {
        padding: 0.1vw;
    }

    .ss {
        padding-left: 3vw;
        padding-right: 1vw;
    }
</style>
