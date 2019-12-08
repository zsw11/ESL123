import { cloneDeep } from 'lodash'

export default {
  namespaced: true,
  state: {
    // 页面文档可视高度(随窗口改变大小)
    documentClientHeight: 0,
    // 导航条, 布局风格, defalut(默认) / inverse(反向)
    navbarLayoutType: 'default',
    // 侧边栏, 布局皮肤, light(浅色) / dark(黑色)
    sidebarLayoutSkin: 'dark',
    // 侧边栏, 折叠状态
    sidebarFold: false,
    // 侧边栏, 菜单
    menuList: [],
    menuActiveName: '',
    // 主入口标签页
    mainTabs: [],
    mainTabsActiveName: ''
  },
  mutations: {
    UpdateDocumentClientHeight (state, height) {
      state.documentClientHeight = height
    },
    UpdateNavbarLayoutType (state, type) {
      state.navbarLayoutType = type
    },
    UpdateSidebarLayoutSkin (state, skin) {
      state.sidebarLayoutSkin = skin
    },
    UpdateSidebarFold (state, fold) {
      state.sidebarFold = fold
    },
    UpdateMenuList (state, list) {
      state.menuList = list
    },
    UpdateMenuActiveName (state, name) {
      state.menuActiveName = name
    },
    UpdateMainTabs (state, tabs) {
      state.mainTabs = tabs
    },
    CloseActiveTab (state) {
      state.mainTabs = state.mainTabs.filter(item => item.name !== state.mainTabsActiveName)
    },
    UpdateMainTabsActiveName (state, name) {
      state.mainTabsActiveName = name
    },
    UpdateTabAttrs (state, payload) {
      const tabs = cloneDeep(state.mainTabs)
      const tab = tabs.find(item => item.name === payload.name)
      if (tab) {
        Object.assign(tab, payload)
        tab.displayTitle = tab.title + (tab.changed ? '*' : '')
      }
      state.mainTabs = tabs
    }
  },
  actions: {
    closeActiveTab ({ commit }, payload) {
      commit('CloseActiveTab', payload)
    },
    updateTabAttrs ({ commit }, payload) {
      commit('UpdateTabAttrs', payload)
    },
    updateMainTabsActiveName ({ commit }, name) {
      commit('UpdateMainTabsActiveName', name)
    }
  }
}
