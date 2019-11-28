<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <el-form-item label="用户名">
          <el-input v-model="listQuery.username" placeholder="用户名／用户操作" clearable></el-input>
        </el-form-item>
        <div class="buttons">
          <el-button @click="clearQuery()">清   空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">系统日志信息</div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        style="width: 100%">
        <el-table-column
          prop="username"
          header-align="center"
          align="center"
          label="用户名">
        </el-table-column>
        <el-table-column
          prop="operation"
          header-align="center"
          align="center"
          label="用户操作">
        </el-table-column>
        <el-table-column
          prop="url"
          header-align="center"
          align="center"
          width="150"
          :show-overflow-tooltip="true"
          label="url">
        </el-table-column>
        <el-table-column
          prop="params"
          header-align="center"
          align="center"
          width="150"
          :show-overflow-tooltip="true"
          label="请求参数">
        </el-table-column>
        <el-table-column
          prop="time"
          header-align="center"
          align="center"
          label="执行时长(毫秒)">
        </el-table-column>
        <el-table-column
          prop="ip"
          header-align="center"
          align="center"
          width="150"
          label="IP地址">
        </el-table-column>
        <el-table-column
          prop="createDate"
          header-align="center"
          align="center"
          width="180"
          label="创建时间">
          <template slot-scope="scope">
            <span>{{scope.row.createdAt | format('YYYY-MM-DD HH:mm:ss')}}</span>
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
import { listLog } from '@/api/log'

export default {
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        username: null
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      selectionDataList: []
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
      listLog(Object.assign(
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
        username: null
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
    }
  }
}
</script>
