import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Dashboard from '@/components/Dashboard'

import {getUser, getRouters, removeUser, removeToken, removeRouters, removeRoutersLoaded, removePermissions} from "@/utils/session"
import lazyLoading from '@/utils/lazyLoading'
import MenuUtils from '@/utils/MenuUtils'

import UserChangePwd from '@/components/user/changepwd'
import UserProfile from '@/components/user/profile'

import NotFound from '@/components/404'

// 懒加载方式，当路由被访问的时候才加载对应组件
const Login = lazyLoading('Login')

Vue.use(Router)

//公共路由
const baseRouter = [
  {
    path: '/login',
    name: '登录',
    component: Login
  },
  {
    path: '/',
    name: '主页',
    component: Home,
    redirect: '/dashboard',
    leaf: true, // 只有一个节点
    menuShow: true,
    iconCls: 'iconfont icon-home', // 图标样式class
    children: [
      {path: '/dashboard', component: Dashboard, name: '首页', menuShow: true, "leaf": true}
    ]
  },
  {
    path: '/',
    component: Home,
    name: '个人资料',
    menuShow: false,
    iconCls: 'el-icon-tickets',
    leaf: false, // 只有一个节点
    children: [
      {path: '/user/profile', component: UserProfile, name: '个人信息', menuShow: true, "leaf": true},
      {path: '/user/changepwd', component: UserChangePwd, name: '修改密码', menuShow: true, "leaf": true}
    ]
  },
  {
    path: '/',
    component: Home,
    redirect: '/404',
    menuShow: false,
    leaf: true, // 只有一个节点
    children: [
      {path: '/404', component: NotFound, name: '', menuShow: false, "leaf": true}
    ]
  }
]

let router = new Router({
//  mode: 'history',
  routes: baseRouter
})

//白名单
const whiteList = ['/login']

let data = getRouters();
if (getUser() && data != null && data != undefined && data != 'undefined') {
  //这里是防止用户手动刷新页面，整个app要重新加载,动态新增的路由，会消失，所以我们重新add一次
  let routes = [];
  MenuUtils(routes, JSON.parse(data));
  router.addRoutes(routes);
  removeRoutersLoaded();
}

let _self = this;
router.beforeEach((to, from, next) => {
  console.log('router to:' + to.path)
  if (to.path.startsWith('/login')) {
    removeUser();
    removeToken();
    removePermissions();
    removeRouters();
    removeRoutersLoaded();
    next()
  } else {
    // 在免登录白名单，直接进入
    let b = 0;
    whiteList.forEach((item) => {
      if (to.path.includes(item)) {
        b = !0;
      }
    });
    if (b) {
      next();
      return;
    }

    if (!getUser()) {      //权限判断
      next({path: '/login'})
    } else {
      if (to.path) {
        next()
      } else {
        next({ path: '/404' })
      }
    }



  }
})


export default router
