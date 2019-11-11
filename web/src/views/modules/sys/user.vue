<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <el-form-item label="用户名">
          <el-input v-model="listQuery.username" placeholder="用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="listQuery.email" placeholder="邮箱" clearable></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="listQuery.mobile" placeholder="手机号" clearable></el-input>
        </el-form-item>
        <div class="buttons">
          <el-button @click="clearQuery()">清   空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">工位信息</div>
        <div class="buttons">
          <el-button v-if="isAuth('sys:user:create')" @click="">导入</el-button>
          <el-button v-if="isAuth('sys:user:create')" @click="">导出</el-button>
          <el-button v-if="isAuth('sys:user:create')" @click="addOrUpdateHandle()">新增</el-button>
          <el-button v-if="isAuth('sys:user:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50">
        </el-table-column>
        <el-table-column
          prop="username"
          header-align="center"
          align="center"
          label="工位名">
        </el-table-column>
        <el-table-column
          prop="email"
          header-align="center"
          align="center"
          label="备注">
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="150"
          label="操作">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:user:reset')" type="text" size="small" @click="resetHandle(scope.row.id)">详情</el-button>
            <el-button v-if="isAuth('sys:user:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { keyBy } from 'lodash'
import { listUser, userReset, userDelete } from '@/api/user'

const status = [
  { id: 'new', name: '正常' },
  { id: 'locked', name: '禁用', tagType: 'danger' }
]

export default {
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        username: null,
        email: null,
        mobile: null
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      statusMap: keyBy(status, 'id')
    }
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 获取数据列表
    getDataList (pageIndex) {
      if (pageIndex) {
        this.pageIndex = pageIndex
      }
      this.dataListLoading = true
      listUser(Object.assign(
        {
          page: this.pageIndex,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({data, total}) => {
        this.dataList = data
        this.totalPage = total
      }).finally(() => {
        this.dataListLoading = false
      })
    },
    clearQuery () {
      this.listQuery = Object.assign(this.listQuery, {
        username: null,
        email: null,
        mobile: null
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `edit-user/${id}` : 'add-user' })
      })
    },
    // 修改密码
    resetHandle (id) {
      this.$prompt('请输入新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'text',
        inputValidator: (val) => {
          if (val === null || val.length < 6 || val.length > 32) {
            return false
          }
        },
        inputErrorMessage: '密码长度在 6 到 32 个字符'
      }).then(({ value }) => {
        userReset({userId: id, password: value}).then(({data}) => {
          if (data) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    // 删除
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对选择的数据=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userDelete(ids).then(({data}) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getDataList()
            }
          })
        })
      }).catch(() => {})
    }
  }
}
</script>
