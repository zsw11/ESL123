import Vue from 'vue'
import router from '@/router'
import store from '@/store'
import { concat } from 'lodash'

/**
 * 获取uuid
 */
export function getUUID () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
  })
}

/**
 * 是否有权限
 * @param {*} key
 */
export function isAuth (key) {
  return JSON.parse(sessionStorage.getItem('permissions') || '[]').indexOf(key) !== -1 || false
}

// 获取原来展开的id
function getExpandedIds (data, ids = new Set(), id = 'id') {
  (data || []).forEach(d => {
    if (d._expanded) ids.add(d[id])
    if (d.children && d.children.length) {
      getExpandedIds(d.children, ids, id)
    }
  })
  return Array.from(ids)
}
// 展开树
function expandTreeData (data, res = [], id = 'id', options = {}) {
  const {
    expandLevel = 0,
    expandedIds
  } = options
  if (expandedIds && expandedIds.length) {
    data.forEach(d => {
      res.push(d)
      if (expandedIds.includes(d[id]) && d.children) {
        d._expanded = true
        expandTreeData(d.children, res, id, options)
      }
    })
  } else {
    data.forEach(d => {
      res.push(d)
      if (d._level < expandLevel && d.children && d.children.length) {
        d._expanded = true
        expandTreeData(d.children, res, id, options)
      }
    })
  }
  return res
}
/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function treeDataTranslate (data, id = 'id', options = {}) {
  var res = []
  var temp = {}
  const {
    pid = 'parentId',
    lastData
  } = options
  for (var i = 0; i < data.length; i++) {
    temp[data[i][id]] = data[i]
  }
  for (var k = 0; k < data.length; k++) {
    if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
      if (!temp[data[k][pid]]['children']) {
        temp[data[k][pid]]['children'] = []
      }
      if (!temp[data[k][pid]]['_level']) {
        temp[data[k][pid]]['_level'] = 1
      }
      data[k]['_level'] = temp[data[k][pid]]._level + 1
      temp[data[k][pid]]['children'].push(data[k])
    } else {
      res.push(data[k])
    }
  }
  return expandTreeData(res, undefined, id, Object.assign(
    options,
    {
      expandedIds: getExpandedIds(lastData, undefined, id)
    }
  ))
}

/**
 * 省/市数据装换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function complexFiltersArea (data) {
  var res = []
  for (var i = 0; i < data.length; i++) {
    if (data[i].type === 'area') {
      var province = {
        name: '省',
        table: data[i].table,
        field: data[i].column[0],
        operation: 'eq',
        value: data[i].value[0]
      }
      var city = {
        name: '市',
        table: data[i].table,
        field: data[i].column[1],
        operation: 'eq',
        value: data[i].value[1]
      }
      res.push(province)
      res.push(city)
    } else {
      res.push(data[i])
    }
  }
  return res
}

export function setDisplayAttributes (displayAttributes) {
  var res = []
  for (var i = 0; i < displayAttributes.length; i++) {
    if (displayAttributes[i].display) {
      res.push(displayAttributes[i].code)
    }
  }
  return res
}

/**
 * 清除登录信息
 */
export function clearLoginInfo () {
  Vue.cookie.delete('token')
  store.commit('resetStore')
  router.options.isAddDynamicMenuRoutes = false
}

export function filterAttributes (attributeConfig, options = {}) {
  if (typeof options === 'string') {
    options = { filterKey: options }
  }
  const {
    filterKey,
    isDefault = true,
    plain = false,
    attributes
  } = options
  if (attributes) {
    if (plain) {
      return attributes.map(a => {
        const [ tc, ac ] = a.split('.')
        const foundA = attributeConfig.find(t => t.code === tc).children.find(ta => ta.code === ac)
        return Object.assign({}, foundA, { code: a })
      })
    } else {
      return attributeConfig.map(t => {
        return {
          code: t.code,
          name: t.name,
          children: t.children.filter(a => {
            return attributes.includes(`${t.code}.${a.code}`)
          })
        }
      })
    }
  } else {
    const filterFunction = isDefault ? t => !(t[filterKey] === false) : t => t[filterKey]
    const tables = attributeConfig.filter(filterFunction)
    if (plain) return concat.apply(tables.map(t => t.children.filter(filterFunction)))
    else {
      return tables.map(t => {
        return {
          code: t.code,
          name: t.name,
          children: t.children.filter(filterFunction)
        }
      })
    }
  }
}

export function isCtrlKeys (e) {
  return ['Backspace', 'Delete', 'Shift', 'Alt', 'Meta', 'Control'].includes(e.key)
}

export function download (data) {
  if (!data) {
    return
  }
  let url = window.URL.createObjectURL(new Blob([data]))
  let link = document.createElement('a')
  link.style.display = 'none'
  link.href = url
  link.setAttribute('download', 'excel.xls')

  document.body.appendChild(link)
  link.click()
}
