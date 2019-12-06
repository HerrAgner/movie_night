<template>
        <v-content>
            <div>
                <v-btn :disabled="valid" color="success" >create event</v-btn>
            </div>
        </v-content>
</template>
<script>

    export default {
        name: 'popupEvent',
    };
</script>