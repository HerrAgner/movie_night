<template>
    <div style="display: flex; flex-direction: column">
        <v-list-item
                :key="title"
                @click="goToMovie"
        >
            <v-list-item-avatar style="height: min-content" tile>
                <v-img :src="getPoster" @error="placeholderfunc"/>
            </v-list-item-avatar>
            
            <v-list-item-content>
                <v-list-item-title v-html="title"/>
                <v-list-item-subtitle v-html="year"/>
            </v-list-item-content>
        </v-list-item>
        <v-divider/>
    </div>
</template>

<script>
  export default {
    name: "SearchResult",
    data() {
      return {
        posterError: false
      }
    },
    props: ['title', 'year', 'poster', 'id'],
    methods: {
      goToMovie() {
        this.$emit("closemenu");
        this.$router.push({path: "details?id=" + this.id})
      },
      placeholderfunc() {
        this.posterError = true;
      }
    },
    computed: {
        getPoster() {
          if (this.posterError === false && this.poster !== "N/A") {
            return this.poster;
          } else {
            return "data:image/jpeg;base64,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";
          }
        }
    }
  }
</script>

<style scoped>

</style>