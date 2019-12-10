<template>
    <h1>Hell from Events</h1>

</template>

<script>
import EventsService from "../services/EventsService";   export default {
        data: () => ({

        }),

        methods: {

        },
        mounted() {
            let res = EventsService().getAllEvents();
            console.log("events: ",res)

        }

    }
</script>

<style scoped>


</style>
