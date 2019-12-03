import Vue from 'vue'
import vuetify from '@/plugins/vuetify' // path to vuetify export

import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false
// Vue.use(Vuetify);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
