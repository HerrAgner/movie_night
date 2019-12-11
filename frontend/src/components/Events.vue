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

                        <v-avatar
                                cols="12" md="4"
                                size="125"
                                tile
                                class="poster"
                        >
                        </v-avatar>
                            <EventEditor v-if = "item.creator === getCurrentUser()" :event="item" :key="i"/>

                </v-card>
            </v-col>
        </v-row>

</template>

<script>
import EventsService from "../services/EventsService";
import EventEditor from "./EventEditor";
    export default {
        components: {
            EventEditor
        },
        data: () => ({
            events: [],
        }),
        methods: {
            getCurrentUser() {
                return this.$store.state.loggedInUser
            }
        },
        async mounted() {
            let res = await EventsService().getAllEvents();
            res.forEach(item => {
                this.events.push(item);

            });
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
