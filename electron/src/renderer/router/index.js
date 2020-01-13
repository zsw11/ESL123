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
    { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页', noSidebar: true } },
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
    { path: '/add-approveopininon', component: _import('modules/masterdata/approveopininon-maintain'), name: 'add-approveopininon', meta: { title: '常用审批意见-新增', isTab: true } },
    { path: '/edit-approveopininon/:id(\\d+)', component: _import('modules/masterdata/approveopininon-maintain'), name: 'edit-approveopininon', meta: { title: '常用审批意见-编辑', isTab: true } },
    { path: '/details-approveopininon/:id(\\d+)', component: _import('modules/masterdata/approveopininon-maintain'), name: 'details-approveopininon', meta: { title: '常用审批意见-详情', isTab: true } },

    // 组织机构关键词关系
    { path: '/add-deptoperationrela', component: _import('modules/masterdata/deptoperationrela-maintain'), name: 'add-deptoperationrela', meta: { title: '新增组织机构关键词关系', isTab: true } },
    { path: '/edit-deptoperationrela/:id(\\d+)', component: _import('modules/masterdata/deptoperationrela-maintain'), name: 'edit-deptoperationrela', meta: { title: '编辑组织机构关键词关系', isTab: true } },
    // 机种
    { path: '/add-model', component: _import('modules/masterdata/model-maintain'), name: 'add-model', meta: { title: '机种-新增', isTab: true } },
    { path: '/edit-model/:id(\\d+)', component: _import('modules/masterdata/model-maintain'), name: 'edit-model', meta: { title: '机种-编辑', isTab: true } },
    { path: '/details-model/:id(\\d+)', component: _import('modules/masterdata/model-maintain'), name: 'details-model', meta: { title: '机种-详情', isTab: true } },
    { path: '/model-part/:id(\\d+)', component: _import('modules/masterdata/partmodelrela'), name: 'model-part', meta: { title: '机种-部品', isTab: true } },
    { path: '/model-tool/:id(\\d+)', component: _import('modules/masterdata/toolmodelrela'), name: 'model-tool', meta: { title: '机种-治工具', isTab: true } },
    { path: '/model-workstation/:id(\\d+)', component: _import('modules/masterdata/workstationmodelrela'), name: 'model-workstation', meta: { title: '机种-工位', isTab: true } },

    // 机种里的部品关系
    { path: '/details-partmodelrela/:id(\\d+)', component: _import('modules/masterdata/partmodelrela-maintain'), name: 'details-partmodelrela', meta: { title: '机种-部品关系详情', isTab: true } },

    // 机种里的治工具关系
    { path: '/details-toolmodelrela/:id(\\d+)', component: _import('modules/masterdata/toolmodelrela-maintain'), name: 'details-toolmodelrela', meta: { title: '机种-治工具关系详情', isTab: true } },

    // 机种里的工位关系
    { path: '/details-workstationmodelrela/:id(\\d+)', component: _import('modules/masterdata/workstationmodelrela-maintain'), name: 'details-workstationmodelrela', meta: { title: '机种-工位关系详情', isTab: true } },

    // 部品里的机种关系
    { path: '/details-modelpartrela/:id(\\d+)', component: _import('modules/masterdata/modelpartrela-maintain'), name: 'details-modelpartrela', meta: { title: '部品-机种关系详情', isTab: true } },

    // 机种系列里的机种关系
    { path: '/details-modelmodelseriesrela/:id(\\d+)', component: _import('modules/masterdata/modelmodelseriesrela-maintain'), name: 'details-modelmodelseriesrela', meta: { title: '机种系列-机种关系详情', isTab: true } },

    // 治工具里的机种关系
    { path: '/details-modeltoolrela/:id(\\d+)', component: _import('modules/masterdata/modeltoolrela-maintain'), name: 'details-modeltoolrela', meta: { title: '治工具-机种关系详情', isTab: true } },

    // 工位里的机种关系
    { path: '/details-modelworkstationrela/:id(\\d+)', component: _import('modules/masterdata/modelworkstationrela-maintain'), name: 'details-modelworkstationrela', meta: { title: '工位-机种关系详情', isTab: true } },

    // 机种系列
    { path: '/add-modelseries', component: _import('modules/masterdata/modelseries-maintain'), name: 'add-modelseries', meta: { title: '机种系列-新增', isTab: true } },
    { path: '/edit-modelseries/:id(\\d+)', component: _import('modules/masterdata/modelseries-maintain'), name: 'edit-modelseries', meta: { title: '机种系列-编辑', isTab: true } },
    { path: '/details-modelseries/:id(\\d+)', component: _import('modules/masterdata/modelseries-maintain'), name: 'details-modelseries', meta: { title: '机种系列-详情', isTab: true } },
    { path: '/modelseries-model/:id(\\d+)', component: _import('modules/masterdata/modelmodelseriesrela'), name: 'modelseries-model', meta: { title: '机种系列-机种', isTab: true } },

    // 关键词
    { path: '/add-action', component: _import('modules/masterdata/action-maintain'), name: 'add-operation', meta: { title: '关键词-新增', isTab: true } },
    { path: '/edit-action/:id(\\d+)', component: _import('modules/masterdata/action-maintain'), name: 'edit-operation', meta: { title: '关键词-编辑', isTab: true } },
    { path: '/details-action/:id(\\d+)', component: _import('modules/masterdata/action-maintain'), name: 'details-operation', meta: { title: '关键词-详情', isTab: true } },

    // 部品
    { path: '/add-part', component: _import('modules/masterdata/part-maintain'), name: 'add-part', meta: { title: '部品-新增', isTab: true } },
    { path: '/edit-part/:id(\\d+)', component: _import('modules/masterdata/part-maintain'), name: 'edit-part', meta: { title: '部品-编辑', isTab: true } },
    { path: '/details-part/:id(\\d+)', component: _import('modules/masterdata/part-maintain'), name: 'details-part', meta: { title: '部品-详情', isTab: true } },
    { path: '/part-model/:id(\\d+)', component: _import('modules/masterdata/modelpartrela'), name: 'part-model', meta: { title: '部品-机种', isTab: true } },

    // 生产阶段
    { path: '/add-phase', component: _import('modules/masterdata/phase-maintain'), name: 'add-phase', meta: { title: '生产阶段-新增', isTab: true } },
    { path: '/edit-phase/:id(\\d+)', component: _import('modules/masterdata/phase-maintain'), name: 'edit-phase', meta: { title: '生产阶段-编辑', isTab: true } },
    { path: '/details-phase/:id(\\d+)', component: _import('modules/masterdata/phase-maintain'), name: 'details-phase', meta: { title: '生产阶段-详情', isTab: true } },

    // 报表
    { path: '/add-report', component: _import('modules/masterdata/report-maintain'), name: 'add-report', meta: { title: '报表-新增', isTab: true } },
    { path: '/edit-report/:id(\\d+)', component: _import('modules/masterdata/report-maintain'), name: 'edit-report', meta: { title: '报表-编辑', isTab: true } },
    { path: '/details-report/:id(\\d+)', component: _import('modules/masterdata/report-maintain'), name: 'details-report', meta: { title: '报表-详情', isTab: true } },

    // 报表组
    { path: '/add-reportgroup', component: _import('modules/masterdata/reportgroup-maintain'), name: 'add-reportgroup', meta: { title: '报表组-新增', isTab: true } },
    { path: '/edit-reportgroup/:id(\\d+)', component: _import('modules/masterdata/reportgroup-maintain'), name: 'edit-reportgroup', meta: { title: '报表组-编辑', isTab: true } },
    { path: '/details-reportgroup/:id(\\d+)', component: _import('modules/masterdata/reportgroup-maintain'), name: 'details-reportgroup', meta: { title: '报表组-详情', isTab: true } },
    { path: '/reportgroup-report/:id(\\d+)', component: _import('modules/masterdata/reportgroupreportrela'), name: 'reportgroup-report', meta: { title: '报表组-报表', isTab: true } },

    // 报表组报表关系
    { path: '/details-reportgroupreportrela/:id(\\d+)', component: _import('modules/masterdata/reportgroupreportrela-maintain'), name: 'details-reportgroupreportrela', meta: { title: '报表组报表关系详情', isTab: true } },
    { path: '/edit-reportgroupreportrela/:id(\\d+)', component: _import('modules/masterdata/reportgroupreportrela-maintain'), name: 'edit-reportgroupreportrela', meta: { title: '编辑报表组报表关系', isTab: true } },

    // 治工具
    { path: '/add-tool', component: _import('modules/masterdata/tool-maintain'), name: 'add-tool', meta: { title: '治工具-新增', isTab: true } },
    { path: '/edit-tool/:id(\\d+)', component: _import('modules/masterdata/tool-maintain'), name: 'edit-tool', meta: { title: '治工具-编辑', isTab: true } },
    { path: '/details-tool/:id(\\d+)', component: _import('modules/masterdata/tool-maintain'), name: 'details-tool', meta: { title: '治工具-详情', isTab: true } },
    { path: '/tool-model/:id(\\d+)', component: _import('modules/masterdata/modeltoolrela'), name: 'tool-model', meta: { title: '治工具-机种', isTab: true } },

    // 工位
    { path: '/add-workstation', component: _import('modules/masterdata/workstation-maintain'), name: 'add-workstation', meta: { title: '工位-新增', isTab: true } },
    { path: '/edit-workstation/:id(\\d+)', component: _import('modules/masterdata/workstation-maintain'), name: 'edit-workstation', meta: { title: '工位-编辑', isTab: true } },
    { path: '/details-workstation/:id(\\d+)', component: _import('modules/masterdata/workstation-maintain'), name: 'details-workstation', meta: { title: '工位-详情', isTab: true } },
    { path: '/workstation-model/:id(\\d+)', component: _import('modules/masterdata/modelworkstationrela'), name: 'workstation-model', meta: { title: '工位-机种', isTab: true } },

    // 工位类型
    { path: '/add-workstationtype', component: _import('modules/masterdata/workstationtype-maintain'), name: 'add-workstationtype', meta: { title: '工位类型-新增', isTab: true } },
    { path: '/edit-workstationtype/:id(\\d+)', component: _import('modules/masterdata/workstationtype-maintain'), name: 'edit-workstationtype', meta: { title: '工位类型-编辑', isTab: true } },
    { path: '/details-workstationtype/:id(\\d+)', component: _import('modules/masterdata/workstationtype-maintain'), name: 'details-workstationtype', meta: { title: '工位类型-详情', isTab: true } },

    // 工位类型节点
    { path: '/add-workstationtypenode', component: _import('modules/masterdata/workstationtypenode-maintain'), name: 'add-workstationtypenode', meta: { title: '新增工位类型节点', isTab: true } },
    { path: '/edit-workstationtypenode/:id(\\d+)', component: _import('modules/masterdata/workstationtypenode-maintain'), name: 'edit-workstationtypenode', meta: { title: '编辑工位类型节点', isTab: true } },

    // 组织机构工位关系
    { path: '/add-deptworkstationrela', component: _import('modules/masterdata/deptworkstationrela-maintain'), name: 'add-deptworkstationrela', meta: { title: '新增组织机构工位关系', isTab: true } },
    { path: '/edit-deptworkstationrela/:id(\\d+)', component: _import('modules/masterdata/deptworkstationrela-maintain'), name: 'edit-deptworkstationrela', meta: { title: '编辑组织机构工位关系', isTab: true } },

    // 常用指标组合
    { path: '/add-measuregroup', component: _import('modules/masterdata/measuregroup-maintain'), name: 'add-measuregroup', meta: { title: '常用指标组合-新增', isTab: true } },
    { path: '/edit-measuregroup/:id(\\d+)', component: _import('modules/masterdata/measuregroup-maintain'), name: 'edit-measuregroup', meta: { title: '常用指标组合-编辑', isTab: true } },
    { path: '/details-measuregroup/:id(\\d+)', component: _import('modules/masterdata/measuregroup-maintain'), name: 'details-measuregroup', meta: { title: '常用指标组合-详情', isTab: true } },

    // 手顺
    { path: '/add-operationgroupoperation', component: _import('modules/masterdata/operationgroupoperation-maintain'), name: 'add-operationgroupoperation', meta: { title: '新增手顺', isTab: true } },
    { path: '/edit-operationgroupoperation/:id(\\d+)', component: _import('modules/masterdata/operationgroupoperation-maintain'), name: 'edit-operationgroupoperation', meta: { title: '编辑手顺', isTab: true } },

    // 手顺组合
    { path: '/add-opertaiongroup', component: _import('modules/masterdata/opertaiongroup-maintain'), name: 'add-opertaiongroup', meta: { title: '手顺组合-新增', isTab: true } },
    { path: '/edit-opertaiongroup/:id(\\d+)', component: _import('modules/masterdata/opertaiongroup-maintain'), name: 'edit-opertaiongroup', meta: { title: '手顺组合-编辑', isTab: true } },
    { path: '/details-opertaiongroup/:id(\\d+)', component: _import('modules/masterdata/opertaiongroup-maintain'), name: 'details-opertaiongroup', meta: { title: '手顺组合-详情', isTab: true } },

    // 分析表
    { path: '/add-workbook', component: _import('modules/workbook/workbook-maintain'), name: 'add-workbook', meta: { title: '新增分析表', isTab: true } },
    { path: '/edit-workbook/:id(\\d+)', component: _import('modules/workbook/workbook-maintain'), name: 'edit-workbook', meta: { title: '编辑分析表', isTab: true } },
    { path: '/copy-workbook/:id(\\d+)', component: _import('modules/workbook/workbook-maintain'), name: 'copy-workbook', meta: { title: '复制分析表', isTab: true } },
    { path: '/alter-workbook/:id(\\d+)', component: _import('modules/workbook/workbook-maintain'), name: 'alter-workbook', meta: { title: '修订分析表', isTab: true } },
    { path: '/workbook-detail/:id(\\d+)', component: _import('modules/workbook/workbook-detail'), name: 'workbook-detail', meta: { title: '分析表录入', isFull: true } },

    // Collection - Compare表
    { path: '/add-compare', component: _import('modules/report/compare-maintain'), name: 'add-compare', meta: { title: '新增Collection - Compare表', isTab: true } },
    { path: '/edit-compare/:id(\\d+)', component: _import('modules/report/compare-maintain'), name: 'edit-compare', meta: { title: '编辑Collection - Compare表', isTab: true } },

    // Revision History 表
    { path: '/add-revisionhistory', component: _import('modules/report/revisionhistory-maintain'), name: 'add-revisionhistory', meta: { title: '新增Revision History 表', isTab: true } },
    { path: '/edit-revisionhistory/:id(\\d+)', component: _import('modules/report/revisionhistory-maintain'), name: 'edit-revisionhistory', meta: { title: '编辑Revision History 表', isTab: true } },

    // 报表审批表
    { path: '/add-approve', component: _import('modules/report/approve-maintain'), name: 'add-approve', meta: { title: '新增报表审批表', isTab: true } },
    { path: '/edit-approve/:id(\\d+)', component: _import('modules/report/approve-maintain'), name: 'edit-approve', meta: { title: '编辑报表审批表', isTab: true } },
    { path: '/details-approve/:id(\\d+)/:reportGroupId', component: _import('modules/report/approve-maintain'), name: 'details-approve', meta: { title: '报表审批表-详情', isTab: true } },

    // 报表审批历史表
    { path: '/add-approvehistory', component: _import('modules/report/approvehistory-maintain'), name: 'add-approvehistory', meta: { title: '新增报表审批历史表', isTab: true } },
    { path: '/edit-apprehistory/:id(\\d+)', component: _import('modules/report/approvehistory-maintain'), name: 'edit-approvehistory', meta: { title: '编辑报表审批历史表', isTab: true } },

    // 履历表
    { path: '/add-changerecord', component: _import('modules/report/changerecord-maintain'), name: 'add-changerecord', meta: { title: '新增履历表', isTab: true } },
    { path: '/edit-changerecord/:id(\\d+)', component: _import('modules/report/changerecord-maintain'), name: 'edit-changerecord', meta: { title: '编辑履历表', isTab: true } },

    // 标准时间表
    { path: '/add-standardtime', component: _import('modules/report/standardtime-maintain'), name: 'add-standardtime', meta: { title: '新增标准时间表', isTab: true } },
    { path: '/edit-standardtime/:id(\\d+)', component: _import('modules/report/standardtime-maintain'), name: 'edit-standardtime', meta: { title: '编辑标准时间表', isTab: true } },

    // 标准工数表
    { path: '/add-standardwork', component: _import('modules/report/standardwork-maintain'), name: 'add-standardwork', meta: { title: '新增标准工数表', isTab: true } },
    { path: '/edit-standardwork/:id(\\d+)', component: _import('modules/report/standardwork-maintain'), name: 'edit-standardwork', meta: { title: '编辑标准工数表', isTab: true } },

    // Report - 时间联络表
    { path: '/add-timecontact', component: _import('modules/report/timecontact-maintain'), name: 'add-timecontact', meta: { title: '新增Report - 时间联络表', isTab: true } },
    { path: '/edit-timecontact/:id(\\d+)', component: _import('modules/report/timecontact-maintain'), name: 'edit-timecontact', meta: { title: '编辑Report - 时间联络表', isTab: true } },
    // Report - Total表
    { path: '/add-total', component: _import('modules/report/total-maintain'), name: 'add-total', meta: { title: '新增Report - Total表', isTab: true } },
    { path: '/edit-total/:id(\\d+)', component: _import('modules/report/total-maintain'), name: 'edit-total', meta: { title: '编辑Report - Total表', isTab: true } },

    // Collection - MOST Value 表
    { path: '/add-mostvalue', component: _import('modules/report/mostvalue-maintain'), name: 'add-mostvalue', meta: { title: '新增Collection - MOST Value 表', isTab: true } },
    { path: '/edit-mostvalue/:id(\\d+)', component: _import('modules/report/mostvalue-maintain'), name: 'edit-mostvalue', meta: { title: '编辑Collection - MOST Value 表', isTab: true } },

    // 工位时间表
    { path: '/add-stationtime', component: _import('modules/report/stationtime-maintain'), name: 'add-stationtime', meta: { title: '新增工位时间表', isTab: true } },
    { path: '/edit-stationtime/:id(\\d+)', component: _import('modules/report/stationtime-maintain'), name: 'edit-stationtime', meta: { title: '编辑工位时间表', isTab: true } },

    // 人机联合表
    { path: '/add-manmachinecombination', component: _import('modules/report/manmachinecombination-maintain'), name: 'add-manmachinecombination', meta: { title: '新增人机联合表', isTab: true } },
    { path: '/edit-manmachinecombination/:id(\\d+)', component: _import('modules/report/manmachinecombination-maintain'), name: 'edit-manmachinecombination', meta: { title: '编辑人机联合表', isTab: true } },

    // 报表批次
    { path: '/createreport-reportbatch', component: _import('modules/report/reportbatch-maintain'), name: 'createreport-reportbatch', meta: { title: '生成报表', isTab: true } },
    { path: '/details-reportbatch/:id(\\d+)', component: _import('modules/report/reportbatch-maintain'), name: 'details-reportbatch', meta: { title: '报表批次详情', isTab: true } }

  ],
  beforeEnter (to, from, next) {
    let token = store.getters.token
    // console.log('token', token, to)
    if (!token || !/\S/.test(token)) {
      clearLoginInfo()
      next({ name: 'login' })
      return
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
