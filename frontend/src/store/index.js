import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);
//const LOGIN_URL = "api/login";

export default new Vuex.Store({
  state: {
    islogedin: false
  },
  mutations: {
    setStatus(state, status) {
      state.islogedin = status;
    }
  },
  actions: {
    async login(context, info) {
      const data = { username: info.email, password: info.password };
      let response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });
      response = response.status === 200 ? await response.json() : null;
      console.log(response);
      //let successfulLogin = !response.url.includes("error");
      //if (successfulLogin) {
      //this.commit("setStatus", successfulLogin);
      //router.push({ path: '/' })
      //this.dispatch('getUserInfoFromDb', info.email)
      //}
    }
  },
  modules: {}
});
