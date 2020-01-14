<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form
      :inline="true"
      :model="listQuery"
      @keyup.enter.native="getDataList()"
      class="form-min-width">

        <el-form-item :label="'报表组名称'" prop="name" >
          <el-input v-model="listQuery.name" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'报表'" prop="reportId">
          <keyword-search
            v-model="listQuery.reportId"
            :searchApi="listReport"
            :allowEmpty="true">
          </keyword-search>
        </el-form-item>

        <el-form-item :label="'备注'" prop="remark" >
          <el-input v-model="listQuery.remark" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'作成人'" prop="createBy" >
          <keyword-search
            :searchApi="this.listStaffUser"
            v-model="listQuery.createBy"
            :allowEmpty="true"
            valueColumn="userId"
            clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'修改人'" prop="updateBy" >
          <keyword-search
            :searchApi="this.listStaffUser"
            v-model="listQuery.updateBy"
            :allowEmpty="true"
            valueColumn="userId"
            clearable></keyword-search>
        </el-form-item>
      </el-form>
      <div class="clearfix">
        <div class="right">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
          <el-button @click="clearQuery()">清 空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">报表组</div>
        <div class="buttons">
          <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
          <el-button
            type="danger"
            @click="deleteHandle()"
            :disabled="dataListSelections.length <= 0">
            批量删除
          </el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          fixed="left"
          type="selection"
          header-align="left"
          align="left"
          width="50">
        </el-table-column>

        <el-table-column
          label="序号"
          type="index">
        </el-table-column>

        <el-table-column align="center" prop="name" label="报表组名称" >
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="id" label="报表"  min-width="200" >
          <template slot-scope="scope">
            <span :title="scope.row.allReportName">{{scope.row.allReportName }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" prop="remark" label="备注" min-width="200">
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="createName" label="作成人">
          <template slot-scope="scope">
            <span>{{scope.row.createName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createAt" label="作成时间" width="100">
          <template slot-scope="scope">
            <span :title="scope.row.createAt">{{scope.row.createAt | format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateName" label="修改人">
          <template slot-scope="scope">
            <span>{{scope.row.updateName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateAt" label="修改时间" width="100">
          <template slot-scope="scope">
            <span :title="scope.row.updateAt">{{scope.row.updateAt  | format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" fixed="right" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="text" class="delete" @click="deleteHandle(scope.row)">删除</el-button>
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
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>

    </el-card>
  </div>
</template>

<script>
import { listReportGroup, deleteReportGroup } from '@/api/reportGroup'
import { listReport } from '@/api/report'
import { listStaffUser } from '@/api/staff'

export default {
  name: 'reportGroupList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        name: null,
        remark: null,
        reportId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listStaffUser,
      listReport,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'reportGroup',
        name: '报表组',
        children: [
          { code: 'id', name: '报表', type: 'string', required: true },
          { code: 'name', name: '报表组名称', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: true },
          { code: 'reportId', name: '报表组', type: 'string', required: true }
        ]
      }],
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
      listReportGroup(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({page}) => {
        this.dataList = page.data
        this.total = page.totalCount
      }).catch(() => {
        this.dataList = []
        this.total = 0
      }).finally(() => {
        this.dataListLoading = false
      })
    },
    // 清除查询条件
    clearQuery () {
      this.listQuery = Object.assign(this.listQuery, {
        name: null,
        remark: null,
        reportId: null,
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
    // 报表
    report (id) {
      this.$nextTick(() => {
        this.$router.push({path: `/reportgroup-report/${id}`})
      })
    },
    // 详情
    details (id) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({path: `/details-reportgroup/${id}`})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-reportgroup/${id}` : '/add-reportgroup' })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row ? [row.id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReportGroup(ids).then((data) => {
          if(data.code === 200) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.pageNo = this.dataList.length === 1 ? this.pageNo-1 : this.pageNo
          }else {
            this.$message.error(data.msg)
          }
          this.getDataList()
        })
      })
    }
  }
}
</script>
