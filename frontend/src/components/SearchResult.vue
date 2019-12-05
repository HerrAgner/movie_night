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
            return "/assets/not-found.jpg";
          }
        }
    }
  }
</script>

<style scoped>

</style>