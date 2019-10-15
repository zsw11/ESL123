<template>
  <el-dialog :title="'未读消息信息'"
             :append-to-body="true"
             :visible.sync="visible">
    <el-form :model="dataForm"
             ref="dataForm"
             label-width="100px">
      <el-table :data="dataList"
                style="width: 100%;">
        <el-table-column prop="title"
                         header-align="center"
                         align="center"
                         label="标题">
          <template slot-scope="scope">
            {{scope.row.title}}
          </template>
        </el-table-column>
        <el-table-column prop="content"
                         header-align="center"
                         align="center"
                         label="消息内容">
        </el-table-column>
        <el-table-column prop="createTime"
                         header-align="center"
                         align="center"
                         label="时间">
          <template slot-scope="scope">
            {{ scope.row.createdAt ? day(scope.row.createdAt).format('YYYY-MM-DD') : ''}}
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button type="primary"
                 @click="messageReadHadle()">设为已读</el-button>
      <el-button type="primary"
                 @click="messageView()">消息中心</el-button>
      <el-button @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { messageList, messageRead } from '@/api/message'
import day from 'dayjs'

export default {
  data () {
    return {
      visible: false,
      dataList: [],
      day,
      dataForm: {
        id: null
      }
    }
  },
  methods: {
    messageVisible () {
      messageList({
        page: 1,
        limit: 5,
        type: 'unread'
      }).then(({ data }) => {
        this.visible = true
        this.dataList = data
      })
    },
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
            this.setUnReadList()
            this.$store.dispatch('user/SetUnreadCount', 0)
          }
        })
      } else {
        this.visible = false
      }
    },
    // 获取数据列表
    setUnReadList () {
      messageList(
        {
          page: this.pageIndex,
          limit: this.pageSize,
          type: 'unread'
        }).then(({ status, data, total }) => {
          if (status === 404) {
            this.visible = false
          }
        })
    },
    // 消息跳转到指定详情
    // messageDetail (sourceId, sourceType) {
    //   this.$nextTick(() => {
    //   })
    // },
    messageView () {
      this.visible = false
      this.$router.push({ name: 'sys-message' })
    }
  }
}
</script>
