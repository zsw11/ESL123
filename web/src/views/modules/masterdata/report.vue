<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">

        <el-form-item :label="'报表名称'" prop="name" >
          <el-input  v-model="listQuery.name" clearable></el-input>
        </el-form-item>

        <el-form-item  :label="'空Form标准编号'" prop="formCode" >
          <el-input v-model="listQuery.formCode" clearable></el-input>
        </el-form-item>

        <div class="search-box">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
        <el-dialog
          class="dialog"
          title="报表审批"
          :visible.sync="approveShow"
          width="406px">
          <el-form :inline="true" :model="approveForm" @keyup.enter.native="getDataList()">

            <el-form-item :label="'选择报表组'" prop="name" >
              <el-input  v-model="approveForm.name" clearable></el-input>
            </el-form-item>

          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="approveShow = false">取 消</el-button>
            <el-button type="primary" @click="approvePut">确 定</el-button>
          </span>
        </el-dialog>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">报表信息</div>
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


        <el-table-column align="center" prop="name" label="名称" >
          <template slot-scope="scope">
           <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="formCode" label="空Form标准编号" >
          <template slot-scope="scope">
           <span>{{scope.row.formCode }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" >
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" fixed="right" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
<!--            <el-button  type="text" size="small" @click="approve(scope.row.id)">审批</el-button>-->
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
import { listReport, deleteReport, fetchReportGroup } from '@/api/report'
export default {
  name: 'reportList',
  data () {
    return {
      approveShow: false,
      dataButton: 'list',
      approveForm: {
        name: null,
        opininon: null
      },
      listQuery: {
        name: null,
        formCode: null,
        remark: null
      },
      listReport,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'report',
        name: '报表信息',
        children: [
          { code: 'name', name: '名称', type: 'string', required: true },
          { code: 'formCode', name: '空Form标准编号', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: true }
        ]
      }],
      complexFilters: []
    }
  },
  activated () {
    const self = this
    self.getDataList()
  },
  created () {

  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listReport(Object.assign(
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
        formCode: null,
        remark: null
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
        this.$router.push({path: `/details-report/${id}`, query: {noShow: true}})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-report/${id}` : '/add-report' })
      })
    },
    // 审批
    approve (id) {
      this.approveShow = true
      console.log(id, 222222222222)
      fetchReportGroup(id).then((page) => {
        console.log(page, 11111111111111111111)
      })
    },
    // 提交审批
    approvePut () {
      this.approveShow = false
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
        deleteReport(ids).then(() => {
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
<style lang="scss">
  .dialog {
    .el-form-item {
      label {
        display: inline-block;
        width: 100px;
      }
    }
  }
</style>

