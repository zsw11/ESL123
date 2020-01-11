<template>
  <aside class="site-sidebar" :class="'site-sidebar--' + sidebarLayoutSkin">
    <div class="site-sidebar__inner">
      <el-menu
        :default-active="menuActiveName || 'home'"
        :collapse="sidebarFold"
        :collapseTransition="false"
        class="site-sidebar__menu">
        <el-menu-item index="home" @click="$router.push({ name: 'home' })">
          <icon-svg name="shouye" class="site-sidebar__menu-icon"></icon-svg>
          <span slot="title" class="menu-font">首页</span>
        </el-menu-item>
        <sub-menu
          v-for="menu in menuList"
          :key="menu.id"
          :menu="menu"
          :dynamicMenuRoutes="dynamicMenuRoutes"
        ></sub-menu>
      </el-menu>
    </div>
  </aside>
</template>

<script>
import SubMenu from "./main-sidebar-sub-menu";
export default {
  data() {
    return {
      dynamicMenuRoutes: []
    };
  },
  components: {
    SubMenu
  },
  computed: {
    sidebarLayoutSkin: {
      get() {
        return this.$store.state.common.sidebarLayoutSkin;
      }
    },
    sidebarFold: {
      get() {
        return this.$store.state.common.sidebarFold;
      }
    },
    menuList: {
      get() {
        return this.$store.state.common.menuList;
      },
      set(val) {
        this.$store.commit("common/UpdateMenuList", val);
      }
    },
    menuActiveName: {
      get() {
        return this.$store.state.common.menuActiveName;
      },
      set(val) {
        this.$store.commit("common/UpdateMenuActiveName", val);
      }
    }
  },
  created() {
    this.menuList = JSON.parse(sessionStorage.getItem("menuList") || "[]");
    this.dynamicMenuRoutes = JSON.parse(
      sessionStorage.getItem("dynamicMenuRoutes") || "[]"
    );
  }
};
</script>
<style lang="scss" scoped>
.menu-font{
  font-size: 16px;
  font-weight: 600;
}
</style>
