<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <div class="logo" />
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu class="site-navbar__menu" mode="horizontal">
        <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <div class="sit-navbar-title">标准时间分析管理系统</div>
      <el-menu class="site-navbar__menu site-navbar__menu--right show-right" mode="horizontal">
        <el-menu-item index="2" class="message-menu-item" @click.native="messageView()">
          <template>
            <el-badge
              :value="this.$store.getters.unreadCount === 0 ? null:this.$store.getters.unreadCount"
            >
              <!-- <icon-svg name="pinglun" width="100%" height="100%"></icon-svg> -->
            </el-badge>
            <span>消息</span>
          </template>
        </el-menu-item>
        <el-menu-item class="site-navbar__avatar user-data show-right" index="3">
          <el-dropdown :show-timeout="0" placement="bottom" size="small">
            <span class="el-dropdown-link user-wrp">
              <img src="~@/assets/img/user.jpg" :alt="userName" />
              <span class="user-wrp">
                <span class="user">{{ userName }}</span><span class="department">{{ department }}</span>
                <p class="time">{{date}}</p>
              </span>
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
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
    <message v-if="messageVisible" ref="message"></message>
  </nav>
</template>

<script>
import UpdatePassword from "./main-navbar-update-password";
import { clearLoginInfo } from "@/utils";
import Message from "./common/message";
import { messageList } from "@/api/message";

export default {
  data() {
    return {
      updatePassowrdVisible: false,
      messageVisible: false,
      messageCount: 0,
      bageValue: null,
      sendTimeIntervalt: null,
      date: null
    };
  },
  created() {
    let self = this
    let Y, M , D , H , Min
    self.getMessageList();
    self.sendTime();
    // 时间定时器
    this.timer = setInterval(()=>{
      let date = new Date()
      Y = date.getFullYear() + '年'
      M = date.getMonth() + 1 + '月'
      D = date.getDate() + '日'
      H = String(date.getHours()).padStart(2,'0') + ':'
      Min = String(date.getMinutes()).padStart(2,'0')
      self.date = Y + M + D + H + Min
    }, 1000)
  },
  beforeDestroy() {
    // 在Vue实例销毁前，清除定时器
    if (this.timer) {
      clearInterval(this.timer) 
    }
  },
  components: {
    UpdatePassword,
    Message
  },
  computed: {
    navbarLayoutType: {
      get() {
        return this.$store.state.common.navbarLayoutType;
      }
    },
    sidebarFold: {
      get() {
        return this.$store.state.common.sidebarFold;
      },
      set(val) {
        this.$store.commit("common/UpdateSidebarFold", val);
      }
    },
    mainTabs: {
      get() {
        return this.$store.state.common.mainTabs;
      },
      set(val) {
        this.$store.commit("common/UpdateMainTabs", val);
      }
    },
    userName: {
      get() {
        return this.$store.state.user.name;
      }
    },
    department: {
      get () {
        return this.$store.state.user.department
      }
    }
  },
  destroyed() {
    clearInterval(this.sendTimeIntervalt);
  },
  methods: {
    // 修改密码
    updatePasswordHandle() {
      this.updatePassowrdVisible = true;
      this.$nextTick(() => {
        this.$refs.updatePassowrd.init();
      });
    },
    // 查看消息记录
    getMessageList() {
      messageList({
        page: this.pageIndex,
        limit: this.pageSize,
        type: "unread"
      }).then(({ page }) => {
        if (page.data.length > 0) {
          this.$store.dispatch("user/SetUnreadCount", page.totalCount);
          this.messageCount = page.totalCount;
          if (this.messageCount > 0) {
            this.bageValue = this.messageCount;
            this.messageHandle();
          } else {
            this.bageValue = null;
          }
        } else {
          this.messageCount = 0;
          this.bageValue = null;
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
      });
    },
    // 退出
    logoutHandle() {
      this.$confirm(`确定进行[退出]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          clearLoginInfo();
          this.$router.push({ name: "login" });
        })
        .catch(() => {});
    },
    messageHandle() {
      this.messageVisible = true;
      this.$nextTick(() => {
        this.$refs.message.messageVisible();
      });
    },
    setMessageCount() {
      messageList({
        type: "unread"
      }).then(({ total }) => {
        if (total > 0) {
          this.$store.dispatch("user/SetUnreadCount", total);
        }
      });
    },
    messageView() {
      this.$router.push({ name: "sys-message" });
    },
    sendTime() {
      var self = this;
      this.sendTimeIntervalt = setInterval(sendTimeOut, 60000);
      function sendTimeOut() {
        self.setMessageCount();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.site-navbar{
  min-width: 1100px;
}
.message-menu-item {
  .el-badge {
    font-size: 20px;
  }
  > span {
    color: #606266;
    font-size: 14px;
  }
}
.user-data{
  width: 250px;
}
.user-wrp{
  display: inline-block;
  position: relative;
  margin: 0;
  height: 30px;
}
.user{
  margin-top: -25%;
  padding: 0;
  height: 30%;
}

.department{
  height: 30%;
  margin-top: -25%;
  margin-left: 10%;
}
.time{
  position: absolute;
  margin: 0;
  padding: 0;
  width: auto;
  top: 50%;
}
.sit-navbar-title {
  font-size: 24px;
  line-height: 50px;
  font-weight: lighter;
  margin-left: 25%;
  display: inline-block;
  text-align: center;

}

.el-menu.el-menu--horizontal{
  border: none
}

@media screen and (max-width: 600px){
  .show-right{
    display: none;
  }
}
</style>
