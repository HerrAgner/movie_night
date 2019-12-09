import Vue from 'vue';
import Vuex from 'vuex';
import Cookie from "js-cookie";
import router from '../router/index.js'



Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isLoggedin: false,
    loggedInUser: '',
    cookie: ''
  },
  mutations: {
    setLogin(state, status) {
      state.isLoggedin = status;
    },
    setLoggedInUser(state, user) {
      state.loggedInUser = user;

    },
    setCookie(state, cookie) {
      state.cookie = cookie;
    }
  },
  actions: {
    async login(context, info) {
      const data = { username: info.email, password: info.password };
      await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      }).then(async response => {
        this.response = response.status === 200 ? await response.json() : null;
        if (this.response !== null) {
          Cookie.set("token", this.response.jwt);
          this.commit("setLogin", true);
          this.commit("setCookie", this.response.jwt);
          router.push({ path: '/' })
        }
      })
    },
    async logout() {
      Cookie.remove("token")
      this.commit("setLogin", false);
    },
  },
  modules: {}
});
