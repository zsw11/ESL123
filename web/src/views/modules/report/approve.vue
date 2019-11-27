<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">


        <el-form-item :label="'所属部门'" prop="deptId">
          <keyword-search v-model="listQuery.deptId" :allowMultiple="true" :searchApi="this.listDept"  :allowEmpty="true" clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'报表组'" prop="report_group_id">
          <keyword-search v-model="listQuery.reportGroupId" :allowMultiple="true" :searchApi="this.listReportGroup"  :allowEmpty="true" clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'下一审批者ID'" prop="nextApproverId">
          <el-input v-model="listQuery.nextApproverId" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'状态'" prop="status">
          <el-input v-model="listQuery.status" clearable></el-input>
        </el-form-item>


        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">reportApprove</div>
        <div class="buttons">
          <el-button
            v-if="isAuth('report:reportapprove:create')"
            type="primary"
            @click="addOrUpdateHandle()"
          >新增</el-button>

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


        <el-table-column align="center" prop="deptId" label="所属部门">
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="report_group_id" label="报表组">
          <template slot-scope="scope">
            <span>{{scope.row.report_group_id }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="nextApproverId" label="下一审批者">
          <template slot-scope="scope">
            <span>{{scope.row.nextApproverId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="status" label="状态">
          <template slot-scope="scope">
            <span>{{scope.row.status }}</span>
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
import { listReportApprove, deleteReportApprove } from '@/api/reportApprove'
import { listDept } from '@/api/dept'
import { listReportGroup } from '@/api/reportGroup'

export default {
  name: 'reportApproveList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        report_group_id: null,
        nextApproverId: null,
        status: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listDept,
      listReportGroup,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'reportApprove',
          name: 'reportApprove',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            {
              code: 'deptId',
              name: '所属部门',
              type: 'string',
              required: true
            },
            {
              code: 'report_group_id',
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
            { code: 'status', name: '状态', type: 'string', required: true },
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
      listReportApprove(
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
        report_group_id: null,
        nextApproverId: null,
        status: null,
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
          path: id ? `/edit-reportapprove/${id}` : '/add-reportapprove'
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
        deleteReportApprove(ids).then(() => {
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
