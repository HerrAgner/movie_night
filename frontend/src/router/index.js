import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/login',
    name: 'login',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/details',
    name: 'MovieDetails',
    component: () =>
      import(/* webpackChunkName: "details" */ '../views/MovieDetails.vue')
  },
  {
    component: Home,
    path: '/*'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch(err => err);
};

router.beforeEach((to, from, next) => {
  // const publicPages = [
  //   'Home',
  //   'about',
  //   'MovieDetails'
  // ];

  // const noAccessWhenLoggedIn = ['login'].includes(to.name);
  // const authRequired = !publicPages.includes(to.name);
  // const loggedInUser = localStorage.myToken;
  // let validAuth = false;
  // if (loggedInUser) {
  //   validAuth =
  //     new Date() <
  //     new Date(JSON.parse(atob(localStorage.myToken.split('.')[1])).exp * 1000);
  // }

  // if (authRequired && !validAuth) {
  //   delete localStorage.myToken;
  //   Store.dispatch('authUser');
  //   return next('/login');
  // } else if (validAuth && noAccessWhenLoggedIn) {
  //   return next('/');
  // }

    window.scrollTo(0, 0);
    next();

  

});

export default router;
