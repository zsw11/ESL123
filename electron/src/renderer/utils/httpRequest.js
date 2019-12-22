import axios from 'axios'
import router from '@/router'
import store from '@/store'
import qs from 'qs'
import merge from 'lodash/merge'
import { clearLoginInfo } from '@/utils'
import { Message } from 'element-ui'

const http = axios.create({
  timeout: 1000 * 30,
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

// request interceptor
http.interceptors.request.use(config => {
  // Do something before request is sent
  if (config.params) config.params['admin'] = 1
  else config.params = { admin: 1 }
  if (store.getters.token) {
    // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
    config.headers['token'] = store.getters.token
  }
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
  if (response.data.code) {
    if (response.data.code === 401) {
      clearLoginInfo()
      router.push({ name: 'login' })
      Message({
        message: response.data.msg,
        type: 'error',
        duration: 5 * 1000
      })
    }
  }
  if (response.headers['content-type'] === 'application/vnd.ms-excel;charset=utf-8') {
    return response
  }
  return response.data
}, error => {
  switch (error.response.status) {
    case 401: {
      clearLoginInfo()
      router.push({ name: 'login' })
      Message({
        message: '认证失败：' + error.response.data.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(error.response)
    }
    default: {
      Message({
        message: '请求异常，请联系系统管理员，错误信息：' + error.response.data.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(error.response)
    }
  }
})

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi/' : window.SITE_CONFIG.baseUrl) + actionName
}

/**
 * get请求参数处理
 * @param {*} params 参数对象
 * @param {*} openDefultParams 是否开启默认参数?
 */
http.adornParams = (params = {}, openDefultParams = true) => {
  var defaults = {
    't': new Date().getTime()
  }
  return openDefultParams ? merge(defaults, params) : params
}

/**
 * post请求数据处理
 * @param {*} data 数据对象
 * @param {*} openDefultdata 是否开启默认数据?
 * @param {*} contentType 数据格式
 *  json: 'application/json; charset=utf-8'
 *  form: 'application/x-www-form-urlencoded; charset=utf-8'
 */
http.adornData = (data = {}, openDefultdata = true, contentType = 'json') => {
  var defaults = {
    't': new Date().getTime()
  }
  data = openDefultdata ? merge(defaults, data) : data
  return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
}

export default http
