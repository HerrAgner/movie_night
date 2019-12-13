<template>
            <v-row justify="center">
                <v-btn text @click.stop="dialog = true">Edit</v-btn>

                <v-dialog v-model="dialog" max-width="800" @click:outside="resetPopup">
                    <v-card>
                        <v-card-title class="headline">Edit event</v-card-title>

                        <v-card-text>
                            <v-container class="movie_info_container">
                                <v-row justify="center">

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


                                                <v-container>
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


                                            <v-col cols="12" >
                                                <SuggestedEventTimes :attendees=selectedFriends :runtime=event.runtime @handleTimeUpdate="handleTimeUpdate" />
                                            </v-col>


                                        </div>
                                    </v-col>
                                </v-row>
                            </v-container>

                        </v-card-text>

                        <v-card-actions>
                            <v-spacer></v-spacer>

                            <v-btn color="green darken-1"
                                   text
                                   @click="resetPopup"
                                   :disabled="saving">

                                Cancel
                            </v-btn>

                            <v-btn color="green darken-1"
                                   text
                                   @click="update"
                                   :disabled="saving">
                                Save</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-row>

</template>
<script>

    import GCalendarService from "../services/GCalendarService";
    import SuggestedEventTimes from "./SuggestedEventTimes";

    export default {
        name: 'popupEvent',
        props: {
            event: Object,
        },
        components: {
            SuggestedEventTimes
        },
        data: () => ({
            saving: false,
            dialog: false,
            eventName: '',
            friends: [],
            date: new Date().toISOString().substr(0, 10),
            menu: false,
            selectedFriends: []
        }),
        methods: {
            handleTimeUpdate(data){
                this.event.startTime = data.split(' - ')[0].replace(' ', 'T');
                this.event.endTime = data.split(' - ')[1].replace(' ', 'T');

                console.log("start time"+this.event.startTime)
                console.log("end time"+this.event.endTime)

            },
            toggle() {
                this.$nextTick(() => {
                    this.inviteAllFriends ? this.selectedFriends = [] : this.selectedFriends = this.friends.slice();
                })
            },
            async update() {
                this.saving = true;
                const data = {
                    eventId: this.event.eventId,
                    movieId: this.event.movieId,
                    eventName: this.eventName,
                    creator: this.$store.state.loggedInUser,
                    startTime: new Date(this.date).toLocaleString().replace(" ", "T"),
                    endTime: new Date(this.date).toLocaleString().replace(" ", "T"),
                    timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone,
                    attendees: this.selectedFriends
                };
                let res = await GCalendarService().updateGoogleCalendarEvent(data);
                if (res) {
                    await this.$emit('childToParent', data);
                    this.dialog = false;
                    this.saving = false;
                }
            },
            resetPopup(){
                this.dialog = false;
                this.selectedFriends = [...this.event.attendees];
                this.eventName = this.event.eventName
            }
        },
        computed: {
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
        },
        async created() {
            this.selectedFriends = [...this.event.attendees]
            this.eventName = this.event.eventName
        },
        async mounted() {
            let token = this.$store.state.cookie;
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
    };
</script>
<style>

</style>
