<template>
  <div class="gen-list-page">
    <el-card class="with-title">
      <div slot="header"
           class="clearfix">
        <div class="card-title">消息记录</div>
      </div>
      <el-tabs v-model="activeName"
               @tab-click="handleClick">
        <el-tab-pane label="全部消息"
                     name="all"></el-tab-pane>
        <el-tab-pane label="未读消息"
                     name="unread"></el-tab-pane>
      </el-tabs>
      <el-table :data="dataList"
                v-loading="dataListLoading"
                style="width: 100%;">
        <el-table-column prop="title"
                         header-align="center"
                         align="center"
                         label="消息标题">
          <template slot-scope="scope">
            <!-- <el-button type="text" v-if="scope.row.sourceType === 'stocktransfer' || scope.row.sourceType === 'sparepartreturn'" size="small" @click="nodeHandle(scope.row.processId)">流程图</el-button> -->
            <!-- <el-button type="text"
                       v-if="scope.row.status==='release' && (scope.row.sourceType === 'stocktransfer' || scope.row.sourceType === 'sparepartreturn')"
                       size="small"
                       @click="approval(scope.row.sourceId,scope.row.sourceType)">{{scope.row.title}}</el-button>
            <el-button type="text"
                       v-else-if="scope.row.status==='fixed' && (scope.row.sourceType === 'stocktransfer' || scope.row.sourceType === 'sparepartreturn')"
                       size="small"
                       @click="approvalDetail(scope.row.sourceId,scope.row.sourceType)">{{scope.row.title}}</el-button>
            <el-button type="text"
                       size="small"
                       @click="messageDetail(scope.row.sourceId,scope.row.sourceType,scope.row.status)">{{scope.row.title}}</el-button>  -->
                       {{scope.row.title}}
          </template>
        </el-table-column>
        <el-table-column prop="content"
                         header-align="center"
                         align="center"
                         label="消息内容"></el-table-column>
        <el-table-column prop="createTime"
                         header-align="center"
                         align="center"
                         label="发送时间">
          <template slot-scope="scope">{{ scope.row.createdAt ? day(scope.row.createdAt).format('YYYY-MM-DD') : ''}}</template>
        </el-table-column>
        <!-- <el-table-column header-align="center"
                         align="center"
                         label="操作">
          <template slot-scope="scope">
            <el-button type="text"
                       v-if="scope.row.sourceType === 'stocktransfer' || scope.row.sourceType === 'sparepartreturn'"
                       size="small"
                       @click="nodeHandle(scope.row.processId)">流程图</el-button>
            <el-button type="text"
                       v-if="scope.row.status==='release' && (scope.row.sourceType === 'stocktransfer' || scope.row.sourceType === 'sparepartreturn')"
                       size="small"
                       @click="approval(scope.row.sourceId,scope.row.sourceType)">审批</el-button>
            <el-button type="text"
                       v-if="scope.row.status==='fixed' && (scope.row.sourceType === 'stocktransfer' || scope.row.sourceType === 'sparepartreturn')"
                       size="small"
                       @click="approvalDetail(scope.row.sourceId,scope.row.sourceType)">详情</el-button>
            <el-button type="text"
                       v-else
                       size="small"
                       @click="messageDetail(scope.row.sourceId,scope.row.sourceType)">详情</el-button>
          </template>
        </el-table-column> -->
      </el-table>
      <el-pagination @size-change="sizeChangeHandle"
                     @current-change="currentChangeHandle"
                     :current-page="pageIndex"
                     :page-sizes="[10, 20, 50, 100]"
                     :page-size="pageSize"
                     :total="totalPage"
                     layout="total, sizes, prev, pager, next, jumper"></el-pagination>
    </el-card>
  </div>
</template>

<script>
import { messageList, messageRead } from '@/api/message'
import day from 'dayjs'
// import { clearTimeout, clearInterval } from 'timers'

export default {
  data () {
    return {
      dataList: [],
      totalPage: 0,
      pageIndex: 1,
      pageSize: 10,
      activeName: 'unread',
      dataListLoading: false,
      day,
      timeout: null,
      interval: null
    }
  },
  activated () {
    this.getDataList(this.activeName)
    if (this.activeName === 'unread') {
      this.sendTime()
    }
  },
  deactivated () {
    clearTimeout(this.timeout)
  },
  methods: {
    // 获取数据列表
    getDataList (type, pageIndex) {
      if (pageIndex) {
        this.pageIndex = pageIndex
      }
      this.dataListLoading = true
      messageList({
        page: this.pageIndex,
        limit: this.pageSize,
        type: type
      }).then(({ page }) => {
        const data = page.data
        if (data.length > 0) {
          this.dataList = data
          this.totalPage = page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    handleClick (tab, event) {
      var activatedName = tab.name
      this.activeName = tab.name
      this.getDataList(activatedName, 1)
      if (this.activeName === 'unread') {
        this.sendTime()
      }
    },
    // approval (sourceId, sourceType) {
    //   this.$nextTick(() => {
    //     if (sourceType === 'sparepartreturn') {
    //       this.$router.push({ path: `stock-sparepartreturn` })
    //       // this.$router.push({ path: `sparepartreturn-approval/${sourceId}` })
    //     } else {
    //       this.$router.push({ path: `stock-stocktransfer` })
    //       // this.$router.push({ path: `stocktransfer-approval/${sourceId}` })
    //     }
    //   })
    // },
    // approvalDetail (sourceId, sourceType) {
    //   this.$nextTick(() => {
    //     if (sourceType === 'sparepartreturn') {
    //       this.$router.push({ path: `stock-sparepartreturn` })
    //       // this.$router.push({ path: `sparepartreturn-appdetail/${sourceId}` })
    //     } else {
    //       this.$router.push({ path: `stock-stocktransfer` })
    //       // this.$router.push({ path: `stocktransfer-appdetail/${sourceId}` })
    //     }
    //   })
    // },
    // 消息跳转到指定详情
    // messageDetail (sourceId, sourceType) {
    //   this.$router.push({ path: `stock-sparepartreturn` })
    //   this.$nextTick(() => { })
    // },
    // nodeHandle (processId) {
    //   this.$nextTick(() => {
    //     this.$router.push({ path: 'node-img/' + processId })
    //   })
    // },
    messageReadHadle () {
      var ids = []
      for (var i = 0; i < this.dataList.length; i++) {
        if (!this.dataList[i].ifRead) {
          ids.push(this.dataList[i].id)
        }
      }
      if (ids.length > 0) {
        messageRead(ids).then(({ status }) => {
          if (status === 200) {
            this.getDataList(this.activeName)
            this.setUnReadList()
          }
        })
      }
    },
    // 获取数据列表
    setUnReadList () {
      messageList({
        page: this.pageIndex,
        limit: this.pageSize,
        type: 'unread'
      }).then(({ data, status }) => {
        if (data && status === 200) {
          this.$store.dispatch('user/SetUnreadCount', data.length)
        } else {
          this.$store.dispatch('user/SetUnreadCount', 0)
        }
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList(this.activeName)
      if (this.activeName === 'unread') {
        this.sendTime()
      }
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList(this.activeName)
      if (this.activeName === 'unread') {
        this.sendTime()
      }
    },

    // 超过时间转为已读
    sendTime () {
      const self = this
      this.timeout = setTimeout(sendTimeOut, 10000)
      function sendTimeOut () {
        self.messageReadHadle()
      }
    }

    // 设置轮询时间
    // setIntervalTime () {
    //   const self = this
    //   this.interval = setInterval(intervaTime, 6000)
    //   function intervaTime () {
    //     self.getDataList(self.activeName)
    //   }
    // }
  }
}
</script>
