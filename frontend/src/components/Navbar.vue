<template>
    <v-card id="nav">
        <v-container style="display: flex; justify-content: space-around; align-items: center; height: 5vh">
            <v-btn to="/" color="teal" text value="home">
                <span>Home</span>
            </v-btn>
            <v-col cols="6">
            <Search id="search"/>
            </v-col>
            <v-btn
                    to="/login"
                    color="teal"
                    text
                    value="login"
                    v-if="!isLoggedin"
            >
                <span>Login</span>
            </v-btn>
            
            <v-btn
                    color="teal"
                    text
                    value="log out"
                    v-if="isLoggedin"
                    @click="changeStatus"
            >
                <span>Log out </span>
            </v-btn>
        </v-container>
    </v-card>
</template>

<script>
  import Search from "@/components/SearchInput.vue"

  export default {
    components: {
      Search
    },
    name: "Navbar",
    data() {
      return {
        isLoggedin: this.$store.state.isLoggedin
      }
    },
    methods: {
      changeStatus() {
        this.$store.dispatch('logout')

      }
    },
    watch: {
      status(newValue) {
        if (newValue === true) {
          this.isLoggedin = true;
        } else {
          this.isLoggedin = false;
        }
      }
    },
    computed: {
      status() {
        return this.$store.state.isLoggedin
      }
    }
  };
</script>

<style scoped>
    #nav {
        height: 6vh;
        z-index: 999;
        padding: 5px;
    }
    
    #search {
        margin-top: 3vh;
    }
    
    #alert {
        color: red;
    }
</style>
