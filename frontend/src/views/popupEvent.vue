<template>
    <div id="app">
        <v-app id="inspire">
            <v-row justify="center">
                <v-btn
                        color="primary"
                        dark
                        @click.stop="dialog = true"
                >
                    Open Dialog
                </v-btn>

                <v-dialog
                        v-model="dialog"
                        max-width="600"
                >
                    <v-card>
                        <v-card-title class="headline">Create Movie nights event</v-card-title>

                        <v-card-text>
                        </v-card-text>

                        <v-card-actions>
                            <v-spacer></v-spacer>

                            <v-btn
                                    color="green darken-1"
                                    text
                                    @click="dialog = false"
                            >
                                Cancel
                            </v-btn>

                            <v-btn
                                    color="green darken-1"
                                    text
                                    @click="dialog = false"
                            >
                                Create
                            </v-btn>
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
        data () {
            return {
                dialog: false,
            }
        },
    };
</script>