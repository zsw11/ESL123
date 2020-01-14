<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">

        <el-form-item :label="'报表'" prop="reportId" >
          <el-input v-model="listQuery.reportId"  clearable></el-input>
        </el-form-item>

        <el-form-item :label="'空form标准编号'" prop="formCode" >
          <keyword-search style="width: 250px" v-model="listQuery.formCode"  :searchApi="this.listReport" :labelColumn="'fromCode'" :allowEmpty="true"></keyword-search>
        </el-form-item>

        <div style="float: right;margin-right: 4px">
          <el-button @click="getDataList(1)" type="primary" >搜   索</el-button>

          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">报表组报表关系</div>
        <div class="buttons">
          <el-button v-if="isAuth('masterData:reportgroupreportrela:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>

          <el-button v-if="isAuth('masterData:reportgroupreportrela:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          type="selection"
          header-align="left"
          align="left"
          width="50">
        </el-table-column>

        <el-table-column align="center" prop="id" label="ID" >
          <template slot-scope="scope">
            <span>{{scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="reportId" label="报表名称" >
          <template slot-scope="scope">
            <span>{{scope.row.reportId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="formCode" label="空from标准编号" >
          <template slot-scope="scope">
            <span>{{scope.row.formcode }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" min-width="200" >
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>


      <el-table-column align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button type="text" size="small" @click="details(scope.row.id)">详情</el-button>
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
import { listReportGroupReportRela, deleteReportGroupReportRela } from '@/api/reportGroupReportRela'
import { listReport } from '@/api/report'
export default {
  name: 'reportGroupReportRelaList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        reportGroupId: null,
        reportId: null,
        remark: null,
        fromCode: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listReport,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'reportGroupReportRela',
        name: '报表组报表关系',
        children: [
          { code: 'id', name: 'ID', type: 'string', required: true },
          { code: 'reportGroupId', name: '报表组ID', type: 'string', required: true },
          { code: 'reportId', name: '报表ID', type: 'string', required: true },
          { code: 'createBy', name: '创建者ID', type: 'string', required: true },
          { code: 'createAt', name: '创建时间', type: 'string', required: true },
          { code: 'updateBy', name: '更新者ID', type: 'string', required: true },
          { code: 'updateAt', name: '更新时间', type: 'string', required: true },
          { code: 'deleteAt', name: '删除时间', type: 'string', required: true },
          { code: 'createdAt', name: '创建时间', type: 'string', required: true },
          { code: 'updatedAt', name: '修改时间', type: 'string', required: true }
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
      listReportGroupReportRela(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({page}) => {
        this.dataList = page.data
        console.log(page.data)
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
        reportGroupId: null,
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
    // 详情
    details (id) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({path: `/details-reportgroupreportrela/${id}`})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-reportgroupreportrela/${id}` : '/add-reportgroupreportrela' })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row ? row.id : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReportGroupReportRela(ids).then(() => {
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
