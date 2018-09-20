import Vue from 'vue'
import Router from 'vue-router'

import Env from '@/api/env'
import {getUser, removeToken, removeUser} from "@/utils/session"

import NotFound from '@/components/404'
import Login from '@/components/Login'
import Self from '@/components/points/Self'
import Score from '@/components/points/Score'
import Help from '@/components/points/Help'
import Record from '@/components/points/Record'
import TicketImg from '@/components/points/TicketImg'

Vue.use(Router);

let basePathPre = Env.basePathPre;

const baseRouter = [
  {
    path: '/',
    redirect: basePathPre +'/login',
  },
  {
    path: basePathPre + '/login',
    name: 'Login',
    component: Login
  },
  {
    path: basePathPre + '/self',
    name: 'Self',
    component: Self
  },
  {
    path: basePathPre + '/score',
    name: 'Score',
    component: Score
  },
  {
    path: basePathPre + '/help',
    name: 'Help',
    component: Help
  },
  {
    path: basePathPre + '/record',
    name: 'Record',
    component: Record
  },
  {
    path: basePathPre + '/ticketImg',
    name: 'TicketImg',
    component: TicketImg
  },
  {
    path: basePathPre + '/404',
    name: 'NotFound',
    component: NotFound
  }
];

let router = new Router({
  //mode: 'history',
  routes: baseRouter
});

router.beforeEach((to, from, next) => {
  console.log('app router to:' + to.path)

  let ua = window.navigator.userAgent.toLowerCase();
  //微信客户端
  //if (ua.indexOf('micromessenger') > 1) {


  //非微信客户端
  //} else {
  //removeUser();
  //removeToken();

  if (to.path.startsWith("/cert")) {
    alert("params=>" + this.$route.query.code + ":" + this.$route.query.params);
    alert("href=>" + this.location.href);

    next({path: basePathPre + '/login'})
    return;
  }

  if (to.path.startsWith(basePathPre + '/login')) {
    if (getUser()) {
      next({path: basePathPre + '/self'})
    } else {
      next()
    }
  } else {
    //检查用户是否存在
    if (!getUser()) {
      next({path: basePathPre + '/login'})
    } else if (to.path) {
      next()
    } else {
      next({path: '/404'})
    }
  }

});

export default router
