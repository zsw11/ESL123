<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">

        <el-form-item :label="'工场'" prop="factory">
          <el-input v-model="listQuery.factory" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'机种'" prop="modelId">
          <keyword-search
            v-model="listQuery.modelId"
            :allowMultiple="true"
            :searchApi="this.listModel"
            :allowEmpty="true" clearable>

          </keyword-search>
        </el-form-item>

        <el-form-item :label="'ST/LST'" prop="stlst">
          <dict-select
            dictType="ST"
            class="input"
            v-model="listQuery.stlst"
            :allowEmpty="true"
            clearable>
          </dict-select>
        </el-form-item>

        <el-form-item :label="'生产阶段'" prop="phaseId">
          <keyword-search
            style="width: 100%"
            v-model="listQuery.phaseId"
            :allowMultiple="true"
            :searchApi="this.listPhase"
            :valueColumn="'name'"
            :allowEmpty="true">
          </keyword-search>
        </el-form-item>


        <el-form-item :label="'型号'" prop="model_type">
          <el-input v-model="listQuery.model_type" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'仕向'" prop="destinations">
          <el-input v-model="listQuery.destinations" clearable></el-input>
        </el-form-item>

        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">ReportChangeRecord</div>
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



        <el-table-column align="center" prop="factory" label="工场">
          <template slot-scope="scope">
            <span>{{scope.row.factory }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelName" label="机种">
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="phaseName" label="生产阶段">
          <template slot-scope="scope">
            <span>{{scope.row.phaseName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stlst" label="ST/LST">
          <template slot-scope="scope">
            <span>{{scope.row.stlst }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="model_type" label="型号">
          <template slot-scope="scope">
            <span>{{scope.row.model_type }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向">
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
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
            <el-button
              size="mini"
              type="text"
              @click="approve(scope.row.modelId,scope.row.phaseId,scope.row.stlst)"
            >提交审批</el-button>
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
    <el-dialog
      customClass="dialog"
      width="30%"
      title="报表审批"
      :visible.sync="approveShow">
      <el-form :inline="true" :model="approveForm" @keyup.enter.native="getDataList()">
        <el-form-item :label="'选择报表组'">
          <el-radio-group v-model="approveForm.reportGroupId" size="small">
            <el-radio :label="item.id"  v-for="item in reportGroup" :key="item.id">{{item.name}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="'下一审批者'" prop="nextApprove" >
          <el-input  v-model="approveForm.nextApprove" clearable></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="approveShow = false">取 消</el-button>
            <el-button type="primary" @click="approvePut">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listReportChangeRecord,
  deleteReportChangeRecord
} from '@/api/reportChangeRecord'
import { listModel } from '@/api/model'
import { listPhase } from '@/api/phase'
import { fetchReportGroup } from '@/api/report'
import { createReportApprove } from '@/api/reportApprove'

export default {
  name: 'reportChangeRecordList',
  data () {
    return {
      approveShow: false,
      approveForm: {
        reportId: null,
        reportGroupId: null,
        nextApprove: null,
        modelId: null,
        phaseId: null,
        stlst: null
      },
      reportGroup: [],
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        title: null,
        sheetName: null,
        factory: null,
        modelId: null,
        modelName: null,
        phaseName: null,
        phaseId: null,
        stlst: null,
        model_type: null,
        destinations: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listPhase,
      listModel,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'reportChangeRecord',
          name: 'reportChangeRecord',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            {
              code: 'deptId',
              name: '组织机构ID',
              type: 'string',
              required: true
            },
             { code: 'title', name: '标题', type: 'string', required: true },
            {
              code: 'sheetName',
              name: 'Sheet名称',
              required: true
            },
            { code: 'factory', name: '工场', type: 'string', required: true },
            { code: 'modelId', name: '机种ID', type: 'string', required: true },
            {
              code: 'model_type',
              name: '型号',
              type: 'string',
              required: true
            },
            {
              code: 'destinations',
              name: '仕向',
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
      listReportChangeRecord(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      )
        .then((page) => {
          this.dataList = page.data
          this.total = page.totalCount
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
        title: null,
        sheetName: null,
        factory: null,
        modelId: null,
        model_type: null,
        destinations: null,
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
            ? `/edit-reportchangerecord/${id}`
            : '/add-reportchangerecord'
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
        deleteReportChangeRecord(ids).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getDataList()
        })
      })
    },
    // 提交审批
    approve (model, phase, stlst) {
      this.approveForm.modelId = model
      this.approveForm.phaseId = phase
      this.approveForm.stlst = stlst
      let data = {
        name: '履历表'
      }
      fetchReportGroup(data).then((page) => {
        this.reportGroup = page.page
      })
      this.approveShow = true
    },
    // 确定提交
    approvePut () {
      if (this.approveShow) {
        createReportApprove(this.approveForm).then((page) => {
          if (page.status === 200) {
            this.approveShow = false
            this.$notify({
              title: '成功',
              message: '提交审批成功',
              type: 'success',
              duration: 2000
            })
          }
        })
      }
    }
  }
}
</script>
<style lang="scss">
  .dialog{
    .el-radio+.el-radio {
      display: block;
      margin-left: 0;
      margin-top: 10px;
    }
  }
</style>

