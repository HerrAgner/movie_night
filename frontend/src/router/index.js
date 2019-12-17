import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';
import Cookie from 'js-cookie';
import store from '../store/index.js';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/login',
    name: 'LoginRegister',
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/LoginRegister.vue')
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
  const noAccessWhenLoggedIn = ['LoginRegister'].includes(to.name);
  let validAuth;
  try {
    validAuth =
      new Date() <
      new Date(JSON.parse(atob(Cookie.get('token').split('.')[1])).exp * 1000);
  } catch {
    validAuth = false;
  }

  if (!validAuth) {
    store.dispatch('logout');
  } else if (validAuth && noAccessWhenLoggedIn) {
    return next('/');
  }

  window.scrollTo(0, 0);
  next();
});

export default router;
