import 'babel-polyfill'
import 'classlist-polyfill'
import Vue from 'vue'
import axios from './router/axios'
import VueAxios from 'vue-axios'
import App from './App'
import './permission' // 权限
import './error' // 日志
import router from './router/router'
import store from './store'
import { loadStyle } from './util/util'
import * as urls from '@/config/env'
//引入图标
import './assets/icon/iconfont.css'
import './assets/icon/font/font_567566_qo5lxgtishg.css'
import './assets/icon/font/font_667895_vf6hgm08ubf.css'
import './util/dd.js'


import {
  iconfontUrl,
  iconfontVersion
} from '@/config/env'
import * as filters from './filters' // 全局filter
import './styles/common.scss'
// 引入avue的包
import Avue from '@smallwei/avue/lib/avue.js'
// 引入avue的样式文件
import '@smallwei/avue/lib/index.css'

import basicContainer from './components/basic-container/main'

import { validatenull } from '@/util/validate'

import uploader from 'vue-simple-uploader'//引入上传文件夹组件
import echarts from 'echarts'

Vue.prototype.$echarts = echarts 

Vue.use(uploader)

Vue.prototype.validatenull = validatenull

Vue.use(Avue, { menuType: 'text' })

Vue.use(router)

Vue.use(VueAxios, axios)

// 注册全局容器
Vue.component('basicContainer', basicContainer)
Vue.component('d2-icon', () => import('./components/d2-icon'))
Vue.component('d2-icon-svg', () => import('./components/d2-icon-svg/index.vue'))

// 加载相关url地址
Object.keys(urls).forEach(key => {
  Vue.prototype[key] = urls[key]
})

//加载过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

// 动态加载阿里云字体库
iconfontVersion.forEach(ele => {
  console.log("-----------------------------------------ele", ele);
  loadStyle(iconfontUrl.replace('$key', ele))
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

/* router.beforeEach((to, from, next) => {
  console.log("to",to)
  if (to.matched.some(record => record.meta.title)) {
    console.log("to.matched",to.matched)
    next() 
  } else {
    next() // 确保一定要调用 next()
  }
}) */