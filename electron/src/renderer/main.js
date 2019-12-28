import Vue from 'vue'
import App from '@/App'
import Conifg from '@/config.js'
import router from '@/router'                 // api: https://github.com/vuejs/vue-router
import store from '@/store'                   // api: https://github.com/vuejs/vuex
import VueCookie from 'vue-cookie'            // api: https://github.com/alfhen/vue-cookie
import '@/element-ui'                         // api: https://github.com/ElemeFE/element
import '@/icons'                              // api: http://www.iconfont.cn/
import '@/element-ui-theme'
import '@/assets/scss/index.scss'
import httpRequest from '@/utils/httpRequest' // api: https://github.com/axios/axios
import { isAuth } from '@/utils'
import * as filters from '@/filters'
import cloneDeep from 'lodash/cloneDeep'
import '@/components/common'
// vxe-table
import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/index.css'
import VXETablePluginExcel from 'vxe-table-plugin-excel'
import 'vxe-table-plugin-excel/dist/style.css'

if (!process.env.IS_WEB) Vue.use(require('vue-electron'))
Vue.use(VXETable)
VXETable.use(VXETablePluginExcel)

Vue.use(VueCookie)
Vue.config.productionTip = false

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

// 非生产环境, 适配mockjs模拟数据                 // api: https://github.com/nuysoft/Mock
if (process.env.NODE_ENV !== 'production') {
  require('@/mock')
}

// 挂载全局
Vue.prototype.$http = httpRequest // ajax请求方法
Vue.prototype.isAuth = isAuth     // 权限方法
// Vue.prototype.
Vue.prototype.customConfig = Conifg

// 保存整站vuex本地储存初始状态
window.SITE_CONFIG['storeState'] = cloneDeep(store.state)

/* eslint-disable no-new */
const vue = new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})

router.beforeEach((to, from, next) => {
  if (from) {
    const tab = store.getters.mainTabs.find(item => item.name === from.name)
    if (tab && tab.changed) {
      vue.$confirm('离开页面可能导致编辑数据丢失，确认离开?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        next()
      }).catch(() => {
        store.dispatch('common/updateMainTabsActiveName', from.name)
      })
    } else {
      next()
    }
  } else {
    next()
  }
})
