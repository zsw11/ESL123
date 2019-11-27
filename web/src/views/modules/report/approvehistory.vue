<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">

        <el-form-item :label="'报表意见'" prop="reportApproveId">
          <el-input v-model="listQuery.reportApproveId" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'结果'" prop="result">
          <el-input v-model="listQuery.result" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'报表组'" prop="reportGroupId">
          <keyword-search v-model="listQuery.reportGroupId" :allowMultiple="true" :searchApi="this.listReportGroup"  :allowEmpty="true" clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'下一审批者'" prop="nextApproverId">
          <el-input v-model="listQuery.nextApproverId" clearable></el-input>
        </el-form-item>


        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">reportApproveHistory</div>
        <div class="buttons">
          <el-button
            :disabled="dataListSelections.length <= 0"
          >批量下载</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;"
      >
        <el-table-column type="selection" header-align="left" align="left" width="50"></el-table-column>

        <el-table-column align="center" prop="reportApproveId" label="报表意见">
          <template slot-scope="scope">
            <span>{{scope.row.reportApproveId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="result" label="结果">
          <template slot-scope="scope">
            <span>{{scope.row.result }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="reportGroupId" label="报表组">
          <template slot-scope="scope">
            <span>{{scope.row.reportGroupId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="nextApproverId" label="下一审批者">
          <template slot-scope="scope">
            <span>{{scope.row.nextApproverId }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          :label="'操作'"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
            >下载</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageNo"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import {
  listReportApproveHistory,
  deleteReportApproveHistory
} from '@/api/reportApproveHistory'
import { listReportGroup } from '@/api/reportGroup'

export default {
  name: 'reportApproveHistoryList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        reportApproveId: null,
        result: null,
        reportGroupId: null,
        nextApproverId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listReportGroup,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'reportApproveHistory',
          name: 'reportApproveHistory',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            {
              code: 'deptId',
              name: '所属部门',
              type: 'string',
              required: true
            },
            {
              code: 'reportApproveId',
              name: '报表意见ID',
              type: 'string',
              required: true
            },
            { code: 'result', name: '结果', type: 'string', required: true },
            {
              code: 'reportGroupId',
              name: '报表组ID',
              type: 'string',
              required: true
            },
            {
              code: 'nextApproverId',
              name: '下一审批者ID',
              type: 'string',
              required: true
            },
            {
              code: 'createBy',
              name: '创建者ID',
              type: 'string',
              required: true
            },
            {
              code: 'createAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updateBy',
              name: '更新者ID',
              type: 'string',
              required: true
            },
            {
              code: 'updateAt',
              name: '更新时间',
              type: 'string',
              required: true
            },
            {
              code: 'deleteAt',
              name: '删除时间',
              type: 'string',
              required: true
            },
            {
              code: 'createdAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updatedAt',
              name: '修改时间',
              type: 'string',
              required: true
            }
          ]
        }
      ],
      complexFilters: []
    }
  },
  activated () {
    const self = this
    self.getDataList()
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listReportApproveHistory(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      )
        .then(({ data, total }) => {
          this.dataList = data
          this.total = total
        })
        .catch(() => {
          this.dataList = []
          this.total = 0
        })
        .finally(() => {
          this.dataListLoading = false
        })
    },
    // 清除查询条件
    clearQuery () {
      this.listQuery = Object.assign(this.listQuery, {
        deptId: null,
        reportApproveId: null,
        result: null,
        reportGroupId: null,
        nextApproverId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageNo = 1
      this.doDataSearch()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageNo = val
      this.doDataSearch()
    },
    // 查询数据
    doDataSearch () {
      if (this.dataButton === 'complex') {
        this.doComplexSearch()
      } else {
        this.getDataList()
      }
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({
          path: id
            ? `/edit-reportapprovehistory/${id}`
            : '/add-reportapprovehistory'
        })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row
        ? row.id
        : this.dataListSelections.map(item => {
          return item.id
        })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReportApproveHistory(ids).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getDataList()
        })
      })
    }
  }
}
</script>
