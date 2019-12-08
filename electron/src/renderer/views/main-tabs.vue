<template>
  <div class="tabs-view-container">
    <scroll-pane class='tabs-view-wrapper' ref='scrollPane'>
      <router-link ref='tab' class="tabs-view-item" :class="isActive(tab)?'active':''" v-for="tab in mainTabs"
        :to="tab" :key="tab.path" @contextmenu.prevent.native="openMenu(tab,$event)">
        {{tab.displayTitle}}
        <span class='el-icon-close' @click.prevent.stop='closeSelectedTab(tab)'></span>
      </router-link>
    </scroll-pane>
    <ul class='contextmenu' v-show="visible" :style="{left:left+'px',top:top+'px'}">
      <li @click="closeSelectedTab(selectedTab)">关闭</li>
      <li @click="closeOthersTabs">关闭其他</li>
      <li @click="closeAllTabs">关闭所有</li>
    </ul>
  </div>
</template>

<script>
import ScrollPane from '@/components/ScrollPane'
import { isURL } from '@/utils/validate'
import { cloneDeep } from 'lodash'

export default {
  components: { ScrollPane },
  data () {
    return {
      visible: false,
      top: 0,
      left: 0,
      selectedTab: {},
      dynamicMenuRoutes: []
    }
  },
  computed: {
    mainTabs: {
      get () { return cloneDeep(this.$store.state.common.mainTabs) },
      set (val) { this.$store.commit('common/UpdateMainTabs', val) }
    },
    mainTabsActiveName: {
      get () { return this.$store.state.common.mainTabsActiveName },
      set (val) { this.$store.dispatch('common/updateMainTabsActiveName', val) }
    },
    siteContentViewHeight () {
      var height = this.documentClientHeight - 50 - 30 - 2
      if (this.$route.meta.isTab) {
        height -= 40
        return isURL(this.$route.meta.iframeUrl) ? { height: height + 'px' } : { minHeight: height + 'px' }
      }
      return { minHeight: height + 'px' }
    }
  },
  watch: {
    $route () {
      this.addViewTabs()
      this.moveToCurrentTab()
    },
    visible (value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    }
  },
  created () {
    this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
    this.addViewTabs()
  },
  mounted () {
    this.addViewTabs()
  },
  methods: {
    generateRoute () {
      if (this.$route.name) {
        return this.$route
      }
      return false
    },
    isActive (route) {
      return route.name === this.mainTabsActiveName
    },
    addViewTabs () {
      let route = this.generateRoute()
      if (route.meta.isTab) {
        // tab选中, 不存在先添加
        let tab = this.mainTabs.filter(item => item.name === route.name)[0]
        if (tab) {
          this.$store.dispatch('common/updateTabAttrs', {
            name: route.name,
            params: route.params,
            query: route.query
          })
        } else {
          if (route.meta.isDynamic) {
            route = this.dynamicMenuRoutes.filter(item => item.name === route.name)[0]
            if (!route) {
              return console.error('未能找到可用标签页!')
            }
          }
          tab = {
            menuId: route.meta.menuId || route.name,
            name: route.name,
            title: route.meta.title,
            displayTitle: route.meta.title,
            type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
            iframeUrl: route.meta.iframeUrl || '',
            params: route.params,
            query: route.query
          }
          this.mainTabs = this.mainTabs.concat([tab])
        }
        this.mainTabsActiveName = tab.name
      }
    },
    moveToCurrentTab () {
      const tabs = this.$refs.tab
      this.$nextTick(() => {
        if (tabs) {
          for (const tab of tabs) {
            if (tab.to.path === this.$route.path) {
              this.$refs.scrollPane.moveToTarget(tab.$el)
              break
            }
          }
        }
      })
    },
    // tabs, 删除tab
    closeSelectedTab (tab) {
      const tabName = tab.name
      this.mainTabs = this.mainTabs.filter(item => item.name !== tabName)
      if (this.mainTabs.length >= 1) {
        // 当前选中tab被删除
        if (tabName === this.mainTabsActiveName) {
          tab = this.mainTabs[this.mainTabs.length - 1]
          this.$router.push({ name: tab.name, query: tab.query, params: tab.params }, () => {
            this.mainTabsActiveName = this.$route.name
          })
        }
      } else {
        this.menuActiveName = ''
        this.$router.push({ name: 'home' })
      }
    },
    closeOthersTabs () {
      this.mainTabs = this.mainTabs.filter(item => item.name === this.mainTabsActiveName)
    },
    closeAllTabs () {
      this.mainTabs = []
      this.menuActiveName = ''
      this.$router.push({ name: 'home' })
    },
    openMenu (tab, e) {
      this.visible = true
      this.selectedTab = tab
      const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
      this.left = e.clientX - offsetLeft + 15 // 15: margin right
      this.top = e.clientY
    },
    closeMenu () {
      this.visible = false
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.tabs-view-container {
  .tabs-view-wrapper {
    background: #fff;
    height: 34px;
    border-bottom: 1px solid #d8dce5;
    box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
    .tabs-view-item {
      display: inline-block;
      position: relative;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;
      &:first-of-type {
        margin-left: 15px;
      }
      &.active {
        background-color: #42b983;
        color: #fff;
        border-color: #42b983;
        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
  }
  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 100;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, .3);
    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;
      &:hover {
        background: #eee;
      }
    }
  }
}
</style>

<style rel="stylesheet/scss" lang="scss">
//reset element css of el-icon-close
.tabs-view-wrapper {
  .tabs-view-item {
    .el-icon-close {
      width: 16px;
      height: 16px;
      vertical-align: 2px;
      border-radius: 50%;
      text-align: center;
      transition: all .3s cubic-bezier(.645, .045, .355, 1);
      transform-origin: 100% 50%;
      &:before {
        transform: scale(.6);
        display: inline-block;
        vertical-align: -3px;
      }
      &:hover {
        background-color: #b4bccc;
        color: #fff;
      }
    }
  }
}
</style>
