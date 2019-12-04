import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
//const LOGIN_URL = "api/login";

export default new Vuex.Store({
  state: {
    islogedin: false,

  },
  mutations: {

    setStatus(state, status) {
      state.islogedin = status;
    }
  },
  actions: {
    async login(info) {
      const data = { "username": info.email, "password": info.password };
      await fetch("http://localhost:8081/api/login", {
        method: "POST",
        mode: 'no-cors',
        headers: {'Accept': 'application/json',
                   "Content-Type":"application/json",
                   },
        body: JSON.stringify(data),
      }).then(response => {
        console.log(response);
        
        //let successfulLogin = !response.url.includes("error");
        //if (successfulLogin) {          
          //this.commit("setStatus", successfulLogin);
          //router.push({ path: '/' })
          //this.dispatch('getUserInfoFromDb', info.email)
        //}
      });
    }
  },
  modules: {
  }
})
