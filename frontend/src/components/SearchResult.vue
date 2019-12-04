<template>
    <div style="display: flex; flex-direction: column">
        <v-list-item
                :key="title"
                @click="goToMovie"
        >
            <v-list-item-avatar style="height: min-content" tile>
                <v-img :src="poster"/>
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
      return {}
    },
    props: ['title', 'year', 'poster', 'id'],
    methods: {
      goToMovie() {
        this.$router.push({path: "details?id=" + this.id})
      }
    }
  }
</script>

<style scoped>

</style>