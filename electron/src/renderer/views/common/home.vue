<template>
  <div class="home-page">
    <el-row>
      <el-col :span="17" class="functions">
        <div class="title">欢 迎 使 用 STAMS</div>
        <el-row>
          <el-col :span="7" @click.native="go({ path: '/workbook-workbook'})">
            <img src="@/assets/img/home/workbook.jpg" alt="">
            <p>时间分析</p>
          </el-col>
          <el-col :span="7">
            <img src="@/assets/img/home/report.jpg" alt="">
            <p>数据统计</p>
          </el-col>
          <el-col :span="7" @click.native="go({ path: '/masterdata-model'})">
            <img src="@/assets/img/home/master-data.jpg" alt="">
            <p>Master Data</p>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="7" @click.native="go({ path: '/report-approve'})">
            <img src="@/assets/img/home/approve.jpg" alt="">
            <p>报表审批</p>
          </el-col>
          <el-col :span="7" @click.native="go({ path: '/sys-role'})">
            <img src="@/assets/img/home/role.jpg" alt="">
            <p>权限管理</p>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="7">
            <img src="@/assets/img/home/query.jpg" alt="">
            <p>查询</p>
          </el-col>
          <el-col :span="7" @click.native="go({ path: '/sys-config'})">
            <img src="@/assets/img/home/setting.jpg" alt="">
            <p>系统设置</p>
          </el-col>
          <el-col :span="7">
            <img src="@/assets/img/home/manual.jpg" alt="">
            <p>操作指引</p>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="7" class="info">
        <div>
          <img src="@/assets/img/home/message.jpg" alt="">
          <p>我的消息</p>
        </div>
        <div>
          <img src="@/assets/img/home/operation.jpg" alt="">
          <p>最近操作</p>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'home',
  data () {
    return  {

    }
  },
  created () {
    console.log('init')
  },
  methods: {
    go (route) {
      this.$router.push(route)
    }
  }
}
</script>

<style scoped lang="scss">
  .home-page{
    height: 100%;
    width: 100%;
    background-color: #f3f3f3;
    background-size: 100% 100%;
    background-repeat: no-repeat;

    > .el-row {
      height: 100%;
      > .el-col {
        height: 100%;
      }
    }

    .functions {
      position: relative;
      border-right: solid 3px skyblue;
      padding: 3%;
      .title {
        margin: 0;
        font-size: 5vh;
        font-weight: bold;
      }
      .el-row {
        height: 25%;
        margin-top: 10px;
        .el-col {
          cursor: pointer;
          height: 100%;
          text-align: center;
          background-color: #fff;
          margin: 0 5px;
          img {
            height: 30%;
            margin-top: 20%;
          }
          p {
            text-align: center;
            font-size: 2.5vh;
          }
          &:hover {
            color: #999;
            border: 2px solid #eeeeee
          }
        }
      }
    }
    .info {
      div {
        height: 47%;
        margin: 10px;
        background-color: #fff;
        img {
          margin-top: 25%;
          width: 25%;
          margin-left: 37.5%;
          margin-right: 37.5%;
        }
        p {
          text-align: center;
          font-size: 2.5vh;
        }
        &:hover {
          color: #999;
          border: 4px solid #eeeeee
        }
      }
    }
  }
</style>
