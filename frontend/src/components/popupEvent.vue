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
                                            <v-col cols="12" sm="6" md="3">
                                                <v-text-field v-model="eventName" required
                                                              label="Event name"
                                                />
                                            </v-col>
                                            <v-col cols="12" sm="6">
                                                <v-select
                                                        v-model="value"
                                                        :items="items"
                                                        attach
                                                        chips
                                                        label="Invite friends"
                                                        multiple
                                                />
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
        items: [],
        value: [],
    }),
    methods: {

    },
    computed: {
        breakpointSmAndDown() {
            return this.$vuetify.breakpoint.smAndDown;
        }
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
                if (this.$store.state.loggedInUser !== item.username) {
                    this.items.push(item.username);
                    this.value.push(item.id);
                }
            });
        }
    }
};
</script>