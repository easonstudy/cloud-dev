// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'

import VueI18n from 'vue-i18n'
import router from './router'
import ElementUI from 'element-ui'
import enLocale from 'element-ui/lib/locale/lang/en'
import zhLocale from 'element-ui/lib/locale/lang/zh-CN'

import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/iconfont.css'
import '@/assets/styles/main.scss'

import customENLocale from '@/common/lang/en'
import customZhLocale from '@/common/lang/zh'

// Create VueI18n instance with options
Vue.use(VueI18n);
const i18n = new VueI18n({
  locale: 'zh',       // set locale
  //this.$i18n.locale // 通过切换locale的值来实现语言切换
  messages: {         // set locale messages
    en: {
      enLocale, // 或者用 Object.assign({ message: 'hello' }, enLocale)
      t: customENLocale
    },
    zh: {
      zhLocale, // 或者用 Object.assign({ message: '你好' }, zhLocale)
      t: customZhLocale
    }
  }
});

Vue.config.productionTip = false;
Vue.use(ElementUI);


/* eslint-disable no-new */
new Vue({
  el: '#app',
  i18n,
  router,
  template: '<App/>',
  components: {App}
});
