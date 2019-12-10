<template>
    <v-container>
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
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title
                                    class="headline"
                                    v-text="item.eventName"
                            />
                            <v-card-text>Invited friends:</v-card-text>

                            <v-col v-for="(friend, i) in item.attendees"
                                        :key="i"> {{friend}}
                            </v-col>

                            <v-btn text>EDIT</v-btn>

                        </div>


                        <v-avatar
                                class="ma-3"
                                size="125"
                                tile
                        >
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>
        </v-row>
    </v-container>

</template>

<script>
import EventsService from "../services/EventsService";
export default {
        data: () => ({
            events: []
        }),

        methods: {

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


</style>
