/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import { navTree } from '@/api/menu'
import { isURL } from '@/utils/validate'
import { clearLoginInfo } from '@/utils'
Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } }
  // 内页分析表
  // eslint-disable-next-line standard/object-curly-even-spacing

]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口整体布局' },
  children: [
    // 通过meta对象设置路由展示方式
    // 1. isTab: 是否通过tab展示内容, true: 是, false: 否
    // 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
    // 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
    // eslint-disable-next-line standard/object-curly-even-spacing
    { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页' } },
    { path: '/theme', component: _import('common/theme'), name: 'theme', meta: { title: '主题' } },
    // eslint-disable-next-line standard/object-curly-even-spacing
    // 系统
    { path: '/add-user', component: _import('modules/sys/user-add-or-update'), name: 'add-user', meta: { title: '新增用户', isTab: true } },
    { path: '/edit-user/:id(\\d+)', component: _import('modules/sys/user-add-or-update'), name: 'edit-user', meta: { title: '编辑用户信息', isTab: true } },
    // -
    { path: '/add-role', component: _import('modules/sys/role-add-or-update'), name: 'add-role', meta: { title: '新增角色', isTab: true } },
    { path: '/edit-role/:id(\\d+)', component: _import('modules/sys/role-add-or-update'), name: 'edit-role', meta: { title: '编辑角色信息', isTab: true } },
    // -
    { path: '/add-menu', component: _import('modules/sys/menu-add-or-update'), name: 'add-menu', meta: { title: '新增菜单', isTab: true } },
    { path: '/edit-menu/:id(\\d+)', component: _import('modules/sys/menu-add-or-update'), name: 'edit-menu', meta: { title: '编辑菜单信息', isTab: true } },
    // -
    { path: '/add-config', component: _import('modules/sys/config-add-or-update'), name: 'add-config', meta: { title: '新增参数', isTab: true } },
    { path: '/edit-config/:id(\\d+)', component: _import('modules/sys/config-add-or-update'), name: 'edit-config', meta: { title: '编辑参数信息', isTab: true } },
    // -
    { path: '/add-dept', component: _import('modules/basic/dept-add-or-update'), name: 'add-dept', meta: { title: '新增部门', isTab: true } },
    { path: '/edit-dept/:id(\\d+)', component: _import('modules/basic/dept-add-or-update'), name: 'edit-dept', meta: { title: '编辑部门信息', isTab: true } },
    // -
    { path: '/add-staff', component: _import('modules/basic/staff-maintain'), name: 'add-staff', meta: { title: '新增人员', isTab: true } },
    { path: '/edit-staff/:id(\\d+)', component: _import('modules/basic/staff-maintain'), name: 'edit-staff', meta: { title: '编辑人员信息', isTab: true } },
    // -
    { path: '/add-dict', component: _import('modules/sys/dict-maintain'), name: 'add-dict', meta: { title: '新增字典类型', isTab: true } },
    { path: '/edit-dict/:id(\\d+)', component: _import('modules/sys/dict-maintain'), name: 'edit-dict', meta: { title: '编辑字典类型', isTab: true } },
    { path: '/sys-dict-item/:id(\\d+)', component: _import('modules/sys/dictitem'), name: 'sys-dict-item', meta: { title: '字典项', isTab: true } },
    { path: '/add-dict-item/:dictId(\\d+)', component: _import('modules/sys/dictitem-maintain'), name: 'add-dict-item', meta: { title: '新增字典项', isTab: true } },
    { path: '/edit-dict-item/:id(\\d+)', component: _import('modules/sys/dictitem-maintain'), name: 'edit-dict-item', meta: { title: '编辑字典项', isTab: true } },
    // -
    { path: '/example-parent-list', component: _import('modules/basic/exampleparent'), name: 'example-parent-list', meta: { title: '父表管理', isTab: true } },
    { path: '/add-example-parent', component: _import('modules/basic/exampleparent-maintain'), name: 'add-example-parent', meta: { title: '新增父表', isTab: true } },
    { path: '/edit-example-parent/:id(\\d+)', component: _import('modules/basic/exampleparent-maintain'), name: 'edit-example-parent', meta: { title: '编辑父表', isTab: true } },
    // 系统配置
    { path: '/add-operationconfig', component: _import('modules/sys/operationconfig-add-or-update'), name: 'add-operation-config', meta: { title: '新增系统配置', isTab: true } },
    { path: '/edit-operationconfig/:id(\\d+)', component: _import('modules/sys/operationconfig-add-or-update'), name: 'edit-operation-config', meta: { title: '编辑系统配置', isTab: true } },
    // -
    { path: '/sys-message', component: _import('modules/sys/message'), name: 'sys-message', meta: { title: '消息记录', isTab: true } },

    // 编码规则
    { path: '/coderule-list', component: _import('modules/sys/coderule'), name: 'coderule-list', meta: { title: '编码规则管理', isTab: true } },
    { path: '/add-coderule', component: _import('modules/sys/coderule-maintain'), name: 'add-coderule', meta: { title: '新增编码规则', isTab: true } },
    { path: '/edit-coderule/:id(\\d+)', component: _import('modules/sys/coderule-maintain'), name: 'edit-coderule', meta: { title: '编辑编码规则', isTab: true } },
    // -
    { path: '/log', component: _import('modules/sys/log'), name: 'log', meta: { title: '系统日志记录', isTab: true } },

    // 引用表
    { path: '/reference-list', component: _import('modules/sys/reference'), name: 'reference-list', meta: { title: '引用表管理', isTab: true } },
    { path: '/add-reference', component: _import('modules/sys/reference-maintain'), name: 'add-reference', meta: { title: '新增引用表', isTab: true } },
    { path: '/edit-reference/:id(\\d+)', component: _import('modules/sys/reference-maintain'), name: 'edit-reference', meta: { title: '编辑引用表', isTab: true } },

    // 常用审批意见
    { path: '/add-approveopininon', component: _import('modules/masterData/approveopininon-maintain'), name: 'add-approveopininon', meta: { title: '新增常用审批意见', isTab: true } },
    { path: '/edit-approveopininon/:id(\\d+)', component: _import('modules/masterData/approveopininon-maintain'), name: 'edit-approveopininon', meta: { title: '编辑常用审批意见', isTab: true } },
    // 组织机构关键词关系
    { path: '/add-deptoperationrela', component: _import('modules/masterData/deptoperationrela-maintain'), name: 'add-deptoperationrela', meta: { title: '新增组织机构关键词关系', isTab: true } },
    { path: '/edit-deptoperationrela/:id(\\d+)', component: _import('modules/masterData/deptoperationrela-maintain'), name: 'edit-deptoperationrela', meta: { title: '编辑组织机构关键词关系', isTab: true } },
    // 机种
    { path: '/add-model', component: _import('modules/masterData/model-maintain'), name: 'add-model', meta: { title: '新增机种', isTab: true } },
    { path: '/edit-model/:id(\\d+)', component: _import('modules/masterData/model-maintain'), name: 'edit-model', meta: { title: '编辑机种', isTab: true } },

    // 机种部品关系
    { path: '/add-modelpartrela', component: _import('modules/masterData/modelpartrela-maintain'), name: 'add-modelpartrela', meta: { title: '新增机种部品关系', isTab: true } },
    { path: '/edit-modelpartrela/:id(\\d+)', component: _import('modules/masterData/modelpartrela-maintain'), name: 'edit-modelpartrela', meta: { title: '编辑机种部品关系', isTab: true } },

    // 机种系列
    { path: '/add-modelseries', component: _import('modules/masterData/modelseries-maintain'), name: 'add-modelseries', meta: { title: '新增机种系列', isTab: true } },
    { path: '/edit-modelseries/:id(\\d+)', component: _import('modules/masterData/modelseries-maintain'), name: 'edit-modelseries', meta: { title: '编辑机种系列', isTab: true } },

    // 机种治工具关系
    { path: '/add-modeltoolrela', component: _import('modules/masterData/modeltoolrela-maintain'), name: 'add-modeltoolrela', meta: { title: '新增机种治工具关系', isTab: true } },
    { path: '/edit-modeltoolrela/:id(\\d+)', component: _import('modules/masterData/modeltoolrela-maintain'), name: 'edit-modeltoolrela', meta: { title: '编辑机种治工具关系', isTab: true } },

    // 关键词
    { path: '/add-operation', component: _import('modules/masterData/operation-maintain'), name: 'add-operation', meta: { title: '新增关键词', isTab: true } },
    { path: '/edit-operation/:id(\\d+)', component: _import('modules/masterData/operation-maintain'), name: 'edit-operation', meta: { title: '编辑关键词', isTab: true } },

    // 部品
    { path: '/add-part', component: _import('modules/masterData/part-maintain'), name: 'add-part', meta: { title: '新增部品', isTab: true } },
    { path: '/edit-part/:id(\\d+)', component: _import('modules/masterData/part-maintain'), name: 'edit-part', meta: { title: '编辑部品', isTab: true } },

    // 生产阶段
    { path: '/add-phase', component: _import('modules/masterData/phase-maintain'), name: 'add-phase', meta: { title: '新增生产阶段', isTab: true } },
    { path: '/edit-phase/:id(\\d+)', component: _import('modules/masterData/phase-maintain'), name: 'edit-phase', meta: { title: '编辑生产阶段', isTab: true } },

    // 报表
    { path: '/add-report', component: _import('modules/masterData/report-maintain'), name: 'add-report', meta: { title: '新增报表', isTab: true } },
    { path: '/edit-report/:id(\\d+)', component: _import('modules/masterData/report-maintain'), name: 'edit-report', meta: { title: '编辑报表', isTab: true } },
    // 报表组
    { path: '/add-reportgroup', component: _import('modules/masterData/reportgroup-maintain'), name: 'add-reportgroup', meta: { title: '新增报表组', isTab: true } },
    { path: '/edit-reportgroup/:id(\\d+)', component: _import('modules/masterData/reportgroup-maintain'), name: 'edit-reportgroup', meta: { title: '编辑报表组', isTab: true } },

    // 报表组报表关系
    { path: '/add-reportgroupreportrela', component: _import('modules/masterData/reportgroupreportrela-maintain'), name: 'add-reportgroupreportrela', meta: { title: '新增报表组报表关系', isTab: true } },
    { path: '/edit-reportgroupreportrela/:id(\\d+)', component: _import('modules/masterData/reportgroupreportrela-maintain'), name: 'edit-reportgroupreportrela', meta: { title: '编辑报表组报表关系', isTab: true } },

    // 治工具
    { path: '/add-tool', component: _import('modules/masterData/tool-maintain'), name: 'add-tool', meta: { title: '新增治工具', isTab: true } },
    { path: '/edit-tool/:id(\\d+)', component: _import('modules/masterData/tool-maintain'), name: 'edit-tool', meta: { title: '编辑治工具', isTab: true } },

    // 工位
    { path: '/add-workstation', component: _import('modules/masterData/workstation-maintain'), name: 'add-workstation', meta: { title: '新增工位', isTab: true } },
    { path: '/edit-workstation/:id(\\d+)', component: _import('modules/masterData/workstation-maintain'), name: 'edit-workstation', meta: { title: '编辑工位', isTab: true } },

    // 工位类型
    { path: '/add-workstationtype', component: _import('modules/masterData/workstationtype-maintain'), name: 'add-workstationtype', meta: { title: '新增工位类型', isTab: true } },
    { path: '/edit-workstationtype/:id(\\d+)', component: _import('modules/masterData/workstationtype-maintain'), name: 'edit-workstationtype', meta: { title: '编辑工位类型', isTab: true } },

    // 工位类型节点
    { path: '/add-workstationtypenode', component: _import('modules/masterData/workstationtypenode-maintain'), name: 'add-workstationtypenode', meta: { title: '新增工位类型节点', isTab: true } },
    { path: '/edit-workstationtypenode/:id(\\d+)', component: _import('modules/masterData/workstationtypenode-maintain'), name: 'edit-workstationtypenode', meta: { title: '编辑工位类型节点', isTab: true } },

    // 组织机构工位关系
    { path: '/add-deptworkstationrela', component: _import('modules/masterData/deptworkstationrela-maintain'), name: 'add-deptworkstationrela', meta: { title: '新增组织机构工位关系', isTab: true } },
    { path: '/edit-deptworkstationrela/:id(\\d+)', component: _import('modules/masterData/deptworkstationrela-maintain'), name: 'edit-deptworkstationrela', meta: { title: '编辑组织机构工位关系', isTab: true } },

    // 常用指标组合
    { path: '/add-measuregroup', component: _import('modules/masterData/measuregroup-maintain'), name: 'add-measuregroup', meta: { title: '新增常用指标组合', isTab: true } },
    { path: '/edit-measuregroup/:id(\\d+)', component: _import('modules/masterData/measuregroup-maintain'), name: 'edit-measuregroup', meta: { title: '编辑常用指标组合', isTab: true } },

    // 手顺
    { path: '/add-operationgroupoperation', component: _import('modules/masterData/operationgroupoperation-maintain'), name: 'add-operationgroupoperation', meta: { title: '新增手顺', isTab: true } },
    { path: '/edit-operationgroupoperation/:id(\\d+)', component: _import('modules/masterData/operationgroupoperation-maintain'), name: 'edit-operationgroupoperation', meta: { title: '编辑手顺', isTab: true } },

    // 手顺组合
    { path: '/add-opertaiongroup', component: _import('modules/masterData/opertaiongroup-maintain'), name: 'add-opertaiongroup', meta: { title: '新增手顺组合', isTab: true } },
    { path: '/edit-opertaiongroup/:id(\\d+)', component: _import('modules/masterData/opertaiongroup-maintain'), name: 'edit-opertaiongroup', meta: { title: '编辑手顺组合', isTab: true } },

    // 分析表
    { path: '/add-workbook', component: _import('modules/workbook/workbook-maintain'), name: 'add-workbook', meta: { title: '新增分析表', isTab: true } },
    { path: '/edit-workbook/:id(\\d+)', component: _import('modules/workbook/workbook-maintain'), name: 'edit-workbook', meta: { title: '编辑分析表', isTab: true } },
    { path: '/workbook-detail', component: _import('modules/workbook/workbook-detail'), name: 'workbook-detail', meta: { title: '分析表录入', isTab: false } }
  ],
  beforeEnter (to, from, next) {
    let token = store.getters.token
    if (!token || !/\S/.test(token)) {
      clearLoginInfo()
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
  mode: 'hash',
  // mode: 'history',
  // base: '/apj-security/',
  scrollBehavior: () => ({ y: 0 }),
  isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
  routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
  // 添加动态(菜单)路由
  // 1. 已经添加 or 全局路由, 直接访问
  // 2. 获取菜单列表, 添加并保存本地存储
  if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
    next()
  } else {
    navTree().then(({ data, permissions }) => {
      fnAddDynamicMenuRoutes(data)
      router.options.isAddDynamicMenuRoutes = true
      sessionStorage.setItem('menuList', JSON.stringify(data || '[]'))
      sessionStorage.setItem('permissions', JSON.stringify(permissions || '[]'))
      next({ ...to, replace: true })
    }).catch((e) => {
      console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
      router.push({ name: 'login' })
    })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
  var temp = []
  for (var i = 0; i < globalRoutes.length; i++) {
    if (route.path === globalRoutes[i].path) {
      return 'global'
    } else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
      temp = temp.concat(globalRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].subMenus && menuList[i].subMenus.length >= 1) {
      temp = temp.concat(menuList[i].subMenus)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      var route = {
        path: menuList[i].url.replace('/', '-'),
        component: null,
        name: menuList[i].url.replace('/', '-'),
        meta: {
          menuId: menuList[i].id,
          title: menuList[i].name,
          isDynamic: true,
          isTab: true,
          iframeUrl: ''
        }
      }
      // url以http[s]://开头, 通过iframe展示
      if (isURL(menuList[i].url)) {
        route['path'] = `i-${menuList[i].id}`
        route['name'] = `i-${menuList[i].id}`
        route['meta']['iframeUrl'] = menuList[i].url
      } else {
        try {
          route['component'] = _import(`modules/${menuList[i].url}`) || null
        } catch (e) { }
      }
      routes.push(route)
    }
  }
  if (temp.length >= 1) {
    fnAddDynamicMenuRoutes(temp, routes)
  } else {
    mainRoutes.name = 'main-dynamic'
    mainRoutes.children = routes
    router.addRoutes([
      mainRoutes,
      { path: '*', redirect: { name: '404' } }
    ])
    sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
    console.log('\n')
    console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
    console.log(mainRoutes.children)
    console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
  }
}

export default router
