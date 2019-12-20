/**
 * 全站权限配置
 *
 */
import router from './router/router'
import store from '@/store'
import { getStore } from '@/util/store'
import { validatenull } from '@/util/validate'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
NProgress.configure({ showSpinner: false })
const lockPage = store.getters.website.lockPage // 锁屏页

/**
 * 导航守卫，相关内容可以参考:
 * https://router.vuejs.org/zh/guide/advanced/navigation-guards.html
 */
router.beforeEach((to, from, next) => {
  // 缓冲设置
  if (to.meta.keepAlive === true && store.state.tags.tagList.some(ele => {
    return ele.value === to.fullPath
  })) {
    to.meta.$keepAlive = true
  } else {
    NProgress.start()
    if (to.meta.keepAlive === true && validatenull(to.meta.$keepAlive)) {
      to.meta.$keepAlive = true
    } else {
      to.meta.$keepAlive = false
    }
  }
  //移除硬件的缓存
  if (to.path.indexOf("hardwarelib") !== -1) {
    to.meta.$keepAlive = false
    to.meta.keepAlive = false
  }
  //移除审批的缓存
  if (to.path.indexOf("/library/ce") !== -1) {
    to.meta.$keepAlive = false
    to.meta.keepAlive = false
  }
  const meta = to.meta || {}
  if (store.getters.access_token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      if (store.getters.roles.length === 0) {
        store.dispatch('GetUserInfo').then(() => {
          next({ ...to, replace: true })
        }).catch(() => {
          store.dispatch('FedLogOut').then(() => {
            next({ path: '/login' })
          })
        })
      } else {
        // console.log("to",to)
        const value = to.query.src || to.fullPath
        const label = to.query.proFloName || to.query.name || to.meta.title || to.name
        if (meta.isTab !== false && !validatenull(value) && !validatenull(label)) {
          store.commit('ADD_TAG', {
            label: label,
            value: value,
            params: to.params,
            query: to.query,
            group: router.$avueRouter.group || []
          })
          let tagListTemp = store.state.tags.tagList
          // console.log("store",store.state.tags.tagList)
          for (const i in tagListTemp) {
            if (tagListTemp[i].value.indexOf('hardwareAdd') !== -1 && tagListTemp[i].value.indexOf('?') === -1) {
              // console.log("tagListTemp",tagListTemp[i])
              store.commit("DEL_TAG", tagListTemp[i]);
            }
            if (tagListTemp[i].value.indexOf('hardwareUpdate') !== -1 && tagListTemp[i].value.indexOf('?') === -1) {
              // console.log("tagListTemp",tagListTemp[i])hardwareUpdate
              store.commit("DEL_TAG", tagListTemp[i]);
            }
          }

        }
        next()
      }
    }
  } else {
    if (meta.isAuth === false) {
      next()
    } else {
      next('/login')
    }
  }
})

router.afterEach(() => {
  NProgress.done()
  const title = store.getters.tag.label
  router.$avueRouter.setTitle(title)
})
