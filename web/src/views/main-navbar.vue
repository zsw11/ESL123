<template>
  <nav class="site-navbar"
       :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand"
          @click="$router.push({ name: 'home' })">
        <div class="logo" />
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu class="site-navbar__menu"
               mode="horizontal">
        <el-menu-item class="site-navbar__switch"
                      index="0"
                      @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <el-menu class="site-navbar__menu site-navbar__menu--right"
               mode="horizontal">
        <el-menu-item index="2"
                      class="message-menu-item"
                      @click.native="messageView()">
          <template>
            <el-badge :value="this.$store.getters.unreadCount === 0 ? null:this.$store.getters.unreadCount">
              <icon-svg name="pinglun"
                        width="100%"
                        height="100%"></icon-svg>
            </el-badge>
            <span>消息</span>
          </template>
        </el-menu-item>
        <el-menu-item class="site-navbar__avatar"
                      index="3">
          <el-dropdown :show-timeout="0"
                       placement="bottom"
                       size="small">
            <span class="el-dropdown-link">
              <img src="~@/assets/img/avatar.png"
                   :alt="userName">{{ userName }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePassowrdVisible"
                     ref="updatePassowrd"></update-password>
    <message v-if="messageVisible"
             ref="message"></message>
  </nav>
</template>

<script>
import UpdatePassword from './main-navbar-update-password'
import { clearLoginInfo } from '@/utils'
import Message from './common/message'
import { messageList } from '@/api/message'

export default {
  data () {
    return {
      updatePassowrdVisible: false,
      messageVisible: false,
      messageCount: 0,
      bageValue: null,
      sendTimeIntervalt: null
    }
  },
  created () {
    this.getMessageList()
    this.sendTime()
  },
  components: {
    UpdatePassword,
    Message
  },
  computed: {
    navbarLayoutType: {
      get () { return this.$store.state.common.navbarLayoutType }
    },
    sidebarFold: {
      get () { return this.$store.state.common.sidebarFold },
      set (val) { this.$store.commit('common/UpdateSidebarFold', val) }
    },
    mainTabs: {
      get () { return this.$store.state.common.mainTabs },
      set (val) { this.$store.commit('common/UpdateMainTabs', val) }
    },
    userName: {
      get () { return this.$store.state.user.name }
    }
  },
  destroyed () {
    clearInterval(this.sendTimeIntervalt)
  },
  methods: {
    // 修改密码
    updatePasswordHandle () {
      this.updatePassowrdVisible = true
      this.$nextTick(() => {
        this.$refs.updatePassowrd.init()
      })
    },
    // 查看消息记录
    getMessageList () {
      messageList(
        {
          page: this.pageIndex,
          limit: this.pageSize,
          type: 'unread'
        }).then(({ page }) => {
          if (page.data.length > 0) {
            this.$store.dispatch('user/SetUnreadCount', page.totalCount)
            this.messageCount = page.totalCount
            if (this.messageCount > 0) {
              this.bageValue = this.messageCount
              this.messageHandle()
            } else {
              this.bageValue = null
            }
          } else {
            this.messageCount = 0
            this.bageValue = null
          }
          // if (data && data.code === 0) {
          //   this.$store.dispatch('user/SetUnRead', data.page.totalCount)
          //   this.messageCount = data.page.totalCount
          //   if (this.messageCount > 0) {
          //     this.bageValue = this.messageCount
          //     this.messageHandle()
          //   } else {
          //     this.bageValue = null
          //   }
          // } else {
          //   this.messageCount = 0
          //   this.bageValue = null
          // }
        })
    },
    // 退出
    logoutHandle () {
      this.$confirm(`确定进行[退出]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        clearLoginInfo()
        this.$router.push({ name: 'login' })
      }).catch(() => { })
    },
    messageHandle () {
      this.messageVisible = true
      this.$nextTick(() => {
        this.$refs.message.messageVisible()
      })
    },
    setMessageCount () {
      messageList({
        type: 'unread'
      }).then(({ total }) => {
        if (total > 0) {
          this.$store.dispatch('user/SetUnreadCount', total)
        }
      })
    },
    messageView () {
      this.$router.push({ name: 'sys-message' })
    },
    sendTime () {
      var self = this
      this.sendTimeIntervalt = setInterval(sendTimeOut, 60000)
      function sendTimeOut () {
        self.setMessageCount()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.message-menu-item {
  .el-badge {
    font-size: 20px;
  }
  > span {
    color: #606266;
    font-size: 14px;
  }
}
</style>
