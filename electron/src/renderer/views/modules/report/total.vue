<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
<!--        <el-form-item :label="'ID'" prop="id">-->
<!--          <el-input-number v-model="listQuery.id" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'组织机构ID'" prop="deptId">-->
<!--          <el-input-number v-model="listQuery.deptId" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'标题'" prop="title">-->
<!--          <el-input v-model="listQuery.title" clearable></el-input>-->
<!--        </el-form-item>-->


        <div class="form-min-width">

          <el-form-item :label="'Sheet名称'" prop="sheetName">
            <el-input v-model="listQuery.sheetName" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              v-model="listQuery.modelId"
              :searchApi="this.listModel"
              :allowEmpty="true"
              clearable>
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'生产阶段'" prop="phaseId">
          <keyword-search
            style="width: 100%"
            v-model="listQuery.phaseId"
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
              clearable>
            </dict-select>
          </el-form-item>

          <el-form-item :label="'仕向'" prop="destinations">
            <el-input v-model="listQuery.destinations" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'类别'" prop="cotegory">
            <el-input v-model="listQuery.cotegory" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'mecha'" prop="mecha">
            <el-input v-model="listQuery.mecha" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'R_code'" prop="rcode">
            <el-input v-model="listQuery.rcode" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'津贴'" prop="allowanceRate">
            <el-input v-model="listQuery.allowanceRate" clearable></el-input>
          </el-form-item>


        <el-form-item :label="'ST版本号'" prop="stRev">
          <el-input v-model="listQuery.stRev" clearable></el-input>
        </el-form-item>

          <el-form-item :label="'LST版本号'" prop="lstRev">
            <el-input v-model="listQuery.lstRev" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'发行日'" prop="issueDate">
            <el-date-picker
              v-model="issueDate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              clearable>
            </el-date-picker>
          </el-form-item>
        </div>



<!--        <el-form-item :label="'确认ID'" prop="comfirmBy">-->
<!--          <el-input-number v-model="listQuery.comfirmBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'承认ID'" prop="inChargeBy">-->
<!--          <el-input-number v-model="listQuery.inChargeBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'拖机或不拖机'" prop="type">-->
<!--          <el-input v-model="listQuery.type" clearable></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'创建者ID'" prop="createBy">-->
<!--          <el-input-number v-model="listQuery.createBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'创建时间'" prop="createAt">-->
<!--          <el-date-picker-->
<!--            v-model="listQuery.createAt"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            clearable-->
<!--          ></el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'更新者ID'" prop="updateBy">-->
<!--          <el-input-number v-model="listQuery.updateBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'更新时间'" prop="updateAt">-->
<!--          <el-date-picker-->
<!--            v-model="listQuery.updateAt"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            clearable-->
<!--          ></el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'删除时间'" prop="deleteAt">-->
<!--          <el-date-picker-->
<!--            v-model="listQuery.deleteAt"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            clearable-->
<!--          ></el-date-picker>-->
<!--        </el-form-item>-->

        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">ReportTotal</div>
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

        <el-table-column label="序号" type="index"></el-table-column>

<!--        <el-table-column align="center" prop="id" label="ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.id }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="deptId" label="组织机构ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.deptId }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="title" label="标题">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.title }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

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

        <el-table-column align="center" prop="phaseName" label="生产阶段">
          <template slot-scope="scope">
            <span>{{scope.row.phaseName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stlst" label="ST/LST" >
          <template slot-scope="scope">
            <span v-if="scope.row.stlst">{{ dictItemSTLST[scope.row.stlst].name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="monthResult" label="发行日" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.monthResult | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向">
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="cotegory" label="类别">
          <template slot-scope="scope">
            <span>{{scope.row.cotegory }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="mecha" label="mecha">
          <template slot-scope="scope">
            <span>{{scope.row.mecha }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="rcode" label="R_code">
          <template slot-scope="scope">
            <span>{{scope.row.rcode }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stRev" label="ST版本号">
          <template slot-scope="scope">
            <span>{{scope.row.stRev }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="lstRev" label="LST版本号">
          <template slot-scope="scope">
            <span>{{scope.row.lstRev }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="allowanceRate" label="津贴">
          <template slot-scope="scope">
            <span>{{scope.row.allowanceRate }}</span>
          </template>
        </el-table-column>

<!--        <el-table-column align="center" prop="comfirmBy" label="确认ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.comfirmBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="inChargeBy" label="承认ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.inChargeBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="type" label="拖机或不拖机">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.type }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="createBy" label="创建者ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.createBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="createAt" label="创建时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.createAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="updateBy" label="更新者ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.updateBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="updateAt" label="更新时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.updateAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="deleteAt" label="删除时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.deleteAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" label="创建时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.createdAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" label="修改时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.updatedAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

        <el-table-column
          fixed="right"
          align="center"
          :label="'操作'"
          width="200"
          class-name="small-padding fixed-width"
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
              @click="down(scope.row)"
            >下载</el-button>
            <el-button
              size="mini"
              type="text"
              @click="approve(scope.row.modelId,scope.row.phaseId,scope.row.stlst,scope.row.destinations,scope.row.versionNumber)"
              v-if="scope.row.exist"
            >提交审批</el-button>
            <el-button
              size="mini"
              type="text"
              v-if="!scope.row.exist"
            >已提交审批</el-button>
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
        <div>
          <el-form-item :label="'下一审批者'" prop="nextApproverId" >
             <keyword-search
              v-model="approveForm.nextApproverId"
              :searchApi="this.listStaffUser"
              :allowEmpty="true"
              valueColumn="userId"
            clearable>
          </keyword-search>
            </el-input>
          </el-form-item>
        </div>

      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="approveShow = false">取 消</el-button>
            <el-button type="primary" @click="approvePut">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>

<script>
import { listReportTotal, deleteReportTotal } from '@/api/reportTotal'
import { listModel } from '@/api/model'
import { listPhase } from '@/api/phase'
import { fetchReportGroup } from '@/api/report'
import { keyBy } from 'lodash'
import { listDict, listDictItem } from '@/api/dict'
import { createReportApprove, downloadReportApprove } from '@/api/reportApprove'
import { listStaffUser } from '@/api/staff'

export default {
  name: 'reportTotalList',
  data () {
    return {
      approveShow: false,
      approveForm: {
        reportGroupId: null,
        nextApproverId: null,
        modelId: null,
        phaseId: null,
        stlst: null,
        destinations: null,
        versionNumber: null,
      },
      reportGroup: [],
      dataButton: 'list',
      issueDate: null,
      listQuery: {
        id: null,
        deptId: null,
        title: null,
        sheetName: null,
        modelId: null,
        modelName: null,
        phaseName: null,
        phaseId: null,
        stlst: null,
        monthResult: null,
        destinations: null,
        cotegory: null,
        mecha: null,
        rcode: null,
        stRev: null,
        lstRev: null,
        allowanceRate: null,
        comfirmBy: null,
        inChargeBy: null,
        type: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listStaffUser,
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
          code: 'reportTotal',
          name: 'reportTotal',
          // eslint-disable-next-line no-sparse-arrays
          children: [
            {
              code: 'id',
              name: 'ID',
              type: 'string',
              required: true
            },
            {
              code: 'deptId',
              name: '组织机构ID',
              type: 'string',
              required: true
            },
            {
              code: 'title',
              name: '标题',
              type: 'string',
              required: true
            },
            {
              code: 'sheetName',
              name: 'Sheet名称',
              required: true
            },
            {
              code: 'modelId',
              name: '机种ID',
              type: 'string',
              required: true
            },
            {
              code: 'monthResult',
              name: '发行日',
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
              code: 'cotegory',
              name: '类别',
              type: 'string',
              required: true
            },
            {
              code: 'mecha',
              name: 'mecha',
              type: 'string',
              required: true
            },
            {
              code: 'rcode',
              name: 'R_code',
              type: 'string',
              required: true
            },
            {
              code: 'stRev',
              name: 'ST版本号',
              type: 'string',
              required: true
            },
            {
              code: 'lstRev',
              name: 'LST版本号',
              type: 'string',
              required: true
            },
            {
              code: 'allowanceRate',
              name: '津贴',
              type: 'string',
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
            {
              code: 'type',
              name: '拖机或不拖机',
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
    self.issueDate = null
    self.listQuery.monthResult = null
    self.getDictByType()
    self.getDataList()
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if(this.issueDate){
        let result = {
          monthResultStart: this.issueDate[0],
          monthResultStop: this.issueDate[1]
        }
        this.listQuery.monthResult = result
      }
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listReportTotal(
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
        phaseId: null,
        stlst: null,
        deptId: null,
        title: null,
        sheetName: null,
        modelId: null,
        modelName: null,
        monthResult: null,
        destinations: null,
        cotegory: null,
        mecha: null,
        rcode: null,
        stRev: null,
        lstRev: null,
        allowanceRate: null,
        comfirmBy: null,
        inChargeBy: null,
        type: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      })
      this.issueDate = null
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
          path: id ? `/edit-total/${id}` : '/add-reporttotal'
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
        deleteReportTotal(ids).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success'
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
      this.approveForm.destinations = destinations
      this.approveForm.versionNumber = versionNumber
      let data = {
        name: 'Report-Total表',
        modelId,
        phaseId,
        stlst,
        destinations,
        versionNumber
      }
      fetchReportGroup(data).then((page) => {
        this.reportGroup = page
      })
      this.approveShow = true
    },
    // 确定提交
    approvePut () {
      if (this.approveShow) {
        createReportApprove(this.approveForm).then((page) => {
          if (!page) {
            this.approveShow = false
            this.$notify({
              title: '成功',
              message: '提交审批成功',
              type: 'success',
              duration: 2000
            })
          }else {
            let name = ''
            page.forEach((item)=>{
              name += (item.name + '   ')
            })
            this.$message({
              message: name+'未生成',
              type: 'warning',
              duration: 3000,
              onClose: () => {
                this.getDataList()
              }
            })
            this.approveShow = false
          }
          this.getDataList()
        })
      }
    },
    // 字典表
    getDictByType () {
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
    },
    down(row){
      let data ={
        modelId: row.modelId,
        phaseId: row.phaseId,
        stlst: row.stlst,
        destinations: row.destinations,
        versionNumber: row.versionNumber,
        reportId: 7
      }
      downloadReportApprove(data).then(response => {
        
      });
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
