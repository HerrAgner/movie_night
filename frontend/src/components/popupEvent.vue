<template>
    <div id="app">
        <v-app id="inspire">
            <v-row justify="center">
                <v-btn color="primary" dark @click.stop="dialog = true">Create event</v-btn>

                <v-dialog v-model="dialog" max-width="800">
                    <v-card>
                        <v-card-title class="headline">MOVIE NIGHTS EVENT</v-card-title>

                        <v-card-text>
                            <v-container class="movie_info_container">
                                <v-row justify="center">
                                    <v-col cols="12" md="4" class="movie_poster_container" :class="breakpointSmAndDown
                                    && 'poster_below_sm'">
                                        <v-img :src="movie.Poster" class="movie_poster" alt="Image not found" />
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
                                                            @click="toggle"
                                                        >
                                                            <v-list-item-content>
                                                                <v-list-item-title>{{selectTitle}}</v-list-item-title>
                                                            </v-list-item-content>
                                                        </v-list-item>
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


                                            <v-col cols="12" sm="6" md="5">
                                                <v-menu
                                                    v-model="menu"
                                                    :close-on-content-click="false"
                                                    :nudge-right="40"
                                                    transition="scale-transition"
                                                    offset-y
                                                    min-width="290px"
                                                >
                                                    <template v-slot:activator="{ on }">
                                                        <v-text-field
                                                            v-model="date"
                                                            label="Pick a date"
                                                            prepend-icon="event"
                                                            readonly
                                                            v-on="on"
                                                        />
                                                    </template>
                                                    <v-date-picker v-model="date" @input="menu = false"/>
                                                </v-menu>
                                            </v-col>


                                        </div>
                                    </v-col>
                                </v-row>
                            </v-container>

                        </v-card-text>

                        <v-card-actions>
                            <v-spacer></v-spacer>

                            <v-btn color="green darken-1" text @click="dialog = false">Cancel</v-btn>

                            <v-btn color="green darken-1" text @click="dialog = false">Save</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-row>
        </v-app>
    </div>
</template>
<script>

export default {
    name: 'popupEvent',
    props: ['movie'],
    data: () => ({
        dialog: false,
        eventName: '',
        friends: [],
        date: new Date().toISOString().substr(0, 10),
        menu: false,
        selectedFriends: [],
        selectTitle: 'Select All',
        loading: false,
    }),
    methods: {
        toggle () {
            this.$nextTick(() => {
                if (this.inviteAllFriends) {
                    this.selectedFriends = [];
                    this.selectTitle = 'Select All'
                } else {
                    this.selectedFriends = this.friends.slice();
                    this.selectTitle = 'Select None'
                }
            })
        },
        close () {
            alert('Chip close clicked')
        },
    },
    computed: {
        selections () {
            const selections = []

            for (const selection of this.selectedFriends) {
                selections.push(this.friends[selection])
            }

            return selections
        },
        breakpointSmAndDown() {
            return this.$vuetify.breakpoint.smAndDown;
        },
        inviteAllFriends () {
            return this.selectedFriends.length === this.friends.length
        },
    },
    async mounted(){
        let token = this.$store.state.cookie;
        let res = await fetch('/api/event', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            },
        });
        if (res.status === 200){
            res = await res.json();
            res.forEach(item => {
                //if (this.$store.state.loggedInUser !== item.username) {
                    this.friends.push(item.username);
                    //this.value.push(item.id);
                //}
            });
        }
    }
};
</script>
<style>
    .chipContainer{
        padding: 0.1vw;
        margin-top: -3vh;
    }
    .friendChip {
        padding: 0.1vw;
    }
    .ss{
        padding-left: 3vw;
        padding-right: 1vw;
    }
</style>
