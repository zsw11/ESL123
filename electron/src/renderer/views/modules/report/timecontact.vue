<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <div class="form-min-width">

          <el-form-item :label="'Sheet名称'" prop="sheetName">
            <el-input v-model="listQuery.sheetName" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              v-model="listQuery.modelId"
              :allowMultiple="true"
              :searchApi="this.listModel"
              :allowEmpty="true"
              clearable>
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'生产阶段'" prop="phaseId">
            <keyword-search
              style="width: 100%"
              v-model="listQuery.phaseId"
              :allowMultiple="true"
              :searchApi="this.listPhase"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'ST/LST'" prop="stlst">
            <dict-select
              dictType="ST"
              class="input"
              v-model="listQuery.stlst"
              :allowEmpty="true"
              clearable></dict-select>
          </el-form-item>

          <el-form-item :label="'ES/AMP/MP'" prop="stage">
            <el-input class="input" v-model="listQuery.stage" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'发行类别：新规制定/修订'" prop="publishType">
            <el-input class="input" v-model="listQuery.publishType" clearable></el-input>
          </el-form-item>


          <el-form-item :label="'版本号'" prop="revNo">
            <el-input class="input" v-model="listQuery.revNo" clearable></el-input>
          </el-form-item>



<!--          <el-form-item :label="'拖机上一版本印字'" prop="towingLastVersionPrinting">-->
<!--            <el-input class="input" v-model="listQuery.towingLastVersionPrinting" clearable></el-input>-->
<!--          </el-form-item>-->


          <el-form-item :label="'opertaionNO'" prop="operationStandardNo">
            <el-input class="input" v-model="listQuery.operationStandardNo" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'INstruction'" prop="operationInstruction">
            <el-input class="input" v-model="listQuery.operationInstruction" clearable></el-input>
          </el-form-item>

        </div>

        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">ReportTimeContact</div>
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


        <el-table-column align="center" prop="sheetName" label="Sheet名称">
          <template slot-scope="scope">
            <span>{{scope.row.sheetName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelName" label="机种">
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stage" label="ES/AMP/MP" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.stage }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="publishType" label="发行类别：新规制定/修订"  width="160">
          <template slot-scope="scope">
            <span>{{scope.row.publishType }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" prop="stlst" label="ST/LST" >
          <template slot-scope="scope">
            <span v-if="scope.row.stlst">{{ dictItemSTLST[scope.row.stlst].name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="phaseName" label="生产阶段">
          <template slot-scope="scope">
            <span>{{scope.row.phaseName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="revNo" label="版本号">
          <template slot-scope="scope">
            <span>{{scope.row.revNo }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" prop="operationStandardNo" label="opertaionNO" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.operationStandardNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="operationInstruction" label="INstruction" width="90">
          <template slot-scope="scope">
            <span>{{scope.row.operationInstruction }}</span>
          </template>
        </el-table-column>

        <el-table-column
          fixed="right"
          align="center"
          :label="'操作'"
          class-name="small-padding fixed-width"
          width="200"
        >
          <template slot-scope="scope">
             <el-button
              size="mini"
              type="text"
              @click="addOrUpdateHandle(scope.row.id)"
            >编辑</el-button>
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
  listReportTimeContact,
  deleteReportTimeContact
} from '@/api/reportTimeContact'
import { listModel } from '@/api/model'
import { fetchReportGroup } from '@/api/report'
import { listPhase } from '@/api/phase'
import { keyBy } from 'lodash'
import { listDict, listDictItem } from '@/api/dict'

export default {
  name: 'reportTimeContactList',
  data () {
    return {
      approveShow: false,
      dataButton: 'list',
      approveForm: {
        reportId: null,
        reportGroupId: null,
        nextApprove: null,
        modelId: null,
        phaseId: null,
        stlst: null
      },
      reportGroup: [],
      listQuery: {
        id: null,
        deptId: null,
        title: null,
        sheetName: null,
        comfirmBy: null,
        inChargeBy: null,
        modelId: null,
        stage: null,
        publishType: null,
        reviseReason: null,
        stlst: null,
        phaseName: null,
        phaseId: null,
        revNo: null,
        allCountSub: null,
        allCountMain: null,
        allCountPrinting: null,
        allCountExternalInspection: null,
        allCountPacking: null,
        towingLastVersionSub: null,
        towingLastVersionMain: null,
        towingLastVersionPrinting: null,
        towingLastVersionExternalInspection: null,
        towingLastVersionPacking: null,
        operationStandardNo: null,
        operationInstruction: null,
        exceptionOperation: null,
        remarkVersionCopmare: null,
        remarkSub: null,
        remarkMain: null,
        remarkPrinting: null,
        remarkExternalInspection: null,
        remarkPacking: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listDict,
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
          code: 'reportTimeContact',
          name: 'reportTimeContact',
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
            {
              code: 'comfirmBy',
              name: '确认ID',
              type: 'string',
              required: true
            },
            {
              code: 'inChargeBy',
              name: '承认ID',
              type: 'string',
              required: true
            },
            { code: 'modelId', name: '机种ID', type: 'string', required: true },
            {
              code: 'stage',
              name: 'ES/AMP/MP',
              type: 'string',
              required: true
            },
            {
              code: 'publishType',
              name: '发行类别：新规制定/修订',
              type: 'string',
              required: true
            },
            {
              code: 'reviseReason',
              name: '修订理由',
              type: 'string',
              required: true
            },
            { code: 'stlst', name: 'ST/LST', type: 'string', required: true },
            { code: 'revNo', name: '版本号', type: 'string', required: true },
            {
              code: 'allCountSub',
              name: '全数sub工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountMain',
              name: '全数main工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountPrinting',
              name: '全数印字/检查/调整工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountExternalInspection',
              name: '全数外装工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountPacking',
              name: '全数捆包工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionSub',
              name: '拖机上一版本sub工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionMain',
              name: '拖机上一版本main工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionPrinting',
              name: '拖机上一版本印字',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionExternalInspection',
              name: '拖机上一版本外装工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionPacking',
              name: '拖机上一版本捆包工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'operationStandardNo',
              name: 'opertaionNO',
              type: 'string',
              required: true
            },
            {
              code: 'operationInstruction',
              name: 'INstruction',
              type: 'string',
              required: true
            },
            {
              code: 'exceptionOperation',
              name: 'EXceprtionOperation',
              type: 'string',
              required: true
            },
            {
              code: 'remarkVersionCopmare',
              name: 'exceptionoperation',
              type: 'string',
              required: true
            },
            {
              code: 'remarkSub',
              name: 'sub差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'remarkMain',
              name: '印字/检查/调整差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'remarkExternalInspection',
              name: '外装差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'remarkPacking',
              name: '捆包差异备注',
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
      complexFilters: [],
      dictItemSTLST: []
    }
  },
  activated () {
    const self = this
    self.getDictByType()
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
      listReportTimeContact(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      )
        .then((page, status) => {
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
        comfirmBy: null,
        inChargeBy: null,
        modelId: null,
        phaseId: null,
        stage: null,
        publishType: null,
        reviseReason: null,
        stlst: null,
        revNo: null,
        allCountSub: null,
        allCountMain: null,
        allCountPrinting: null,
        allCountExternalInspection: null,
        allCountPacking: null,
        towingLastVersionSub: null,
        towingLastVersionMain: null,
        towingLastVersionPrinting: null,
        towingLastVersionExternalInspection: null,
        towingLastVersionPacking: null,
        operationStandardNo: null,
        operationInstruction: null,
        exceptionOperation: null,
        remarkVersionCopmare: null,
        remarkSub: null,
        remarkMain: null,
        remarkPrinting: null,
        remarkExternalInspection: null,
        remarkPacking: null,
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
          path: id ? `/edit-timecontact/${id}` : '/add-timecontact'
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
        deleteReportTimeContact(ids).then(() => {
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
    approve (modelId, phaseId, stlst) {
      this.approveForm.modelId = modelId
      this.approveForm.phaseId = phaseId
      this.approveForm.stlst = stlst
      let data = {
        name: 'Report-时间联络表',
        modelId,
        phaseId,
        stlst
      }
      fetchReportGroup(data).then((page) => {
        this.reportGroup = page
      })
      this.approveShow = true
    },
    // 确定提交
    approvePut () {
      this.approveShow = false
    },
    // 字典表
    getDictByType () {
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
    }
  }
}
</script>
<style lang="scss">
  .input{
    width: 150px;
  }
  .min-width{
    min-width: 1100px;
  }
  .dialog{
    .el-radio+.el-radio {
      display: block;
      margin-left: 0;
      margin-top: 10px;
    }
  }
</style>
