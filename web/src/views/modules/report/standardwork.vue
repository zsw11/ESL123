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

<!--        <el-form-item :label="'Sheet名称'" prop="sheetName">-->
<!--          <el-input v-model="listQuery.sheetName" clearable></el-input>-->
<!--        </el-form-item>-->

        <el-form-item :label="'机种'" prop="modelId">
          <keyword-search v-model="listQuery.modelId" :allowMultiple="true" :searchApi="this.listModel"  :allowEmpty="true" clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'型号'" prop="model_type">
          <el-input v-model="listQuery.model_type" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'系数'" prop="coefficient">
          <el-input v-model="listQuery.coefficient" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'生产阶段'" prop="phaseId">
          <keyword-search v-model="listQuery.phaseId" :allowMultiple="true" :searchApi="this.listPhase"  :allowEmpty="true" clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'ST/LST'" prop="stlst">
          <dict-select dictType="ST" class="input" v-model="listQuery.stlst" :allowEmpty="true" clearable></dict-select>
        </el-form-item>

        <el-form-item :label="'技通No'" prop="RevNo">
          <el-input v-model="listQuery.RevNo" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'首个标准工数title'" prop="firstStandardWorkTitle">
          <el-input v-model="listQuery.firstStandardWorkTitle" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'第三个标准工数title'" prop="secondStandardWorkTitle">
          <el-input v-model="listQuery.secondStandardWorkTitle" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'发行日'" prop="monthResult">
          <el-date-picker
            v-model="listQuery.monthResult"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
          ></el-date-picker>
        </el-form-item>
<!--        <el-form-item :label="'确认ID'" prop="comfirmBy">-->
<!--          <el-input-number v-model="listQuery.comfirmBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'承认ID'" prop="inChargeBy">-->
<!--          <el-input-number v-model="listQuery.inChargeBy" clearable></el-input-number>-->
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
        <div class="card-title">ReportStandardWork</div>
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

<!--        <el-table-column align="center" prop="sheetName" label="Sheet名称">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.sheetName }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

        <el-table-column align="center" prop="modelId" label="机种">
          <template slot-scope="scope">
            <span>{{scope.row.modelId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="model_type" label="型号">
          <template slot-scope="scope">
            <span>{{scope.row.model_type }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="coefficient" label="系数">
          <template slot-scope="scope">
            <span>{{scope.row.coefficient }}</span>
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

        <el-table-column align="center" prop="RevNo" label="技通No">
          <template slot-scope="scope">
            <span>{{scope.row.RevNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="monthResult" label="发行日">
          <template slot-scope="scope">
            <span>{{scope.row.monthResult | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="firstStandardWorkTitle" label="首个标准工数title" width="130">
          <template slot-scope="scope">
            <span>{{scope.row.firstStandardWorkTitle }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="secondStandardWorkTitle" label="第三个标准工数title" width="130">
          <template slot-scope="scope">
            <span>{{scope.row.secondStandardWorkTitle }}</span>
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
  listReportStandardWork,
  deleteReportStandardWork
} from '@/api/reportStandardWork'
import { listPhase } from '@/api/phase'
import { listModel } from '@/api/model'
import { fetchReportGroup } from '@/api/report'

export default {
  name: 'reportStandardWorkList',
  data () {
    return {
      approveShow: false,
      approveForm: {
        reportId: null,
        reportGroupId: null,
        nextApprove: null
      },
      reportGroup: [
        {
          name: '报表组1',
          id: 1
        },
        {
          name: '报表组2',
          id: 2
        },
        {
          name: '报表组3',
          id: 3
        },
        {
          name: '报表组4',
          id: 4
        }
      ],
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        title: null,
        sheetName: null,
        modelId: null,
        modelName: null,
        model_type: null,
        coefficient: null,
        phaseId: null,
        phaseName: null,
        stlst: null,
        RevNo: null,
        monthResult: null,
        firstStandardWorkTitle: null,
        secondStandardWorkTitle: null,
        comfirmBy: null,
        inChargeBy: null,
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
          code: 'reportStandardWork',
          name: 'reportStandardWork',
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
            { code: 'modelId', name: '机种ID', type: 'string', required: true },
            {
              code: 'model_type',
              name: '型号',
              type: 'string',
              required: true
            },
            {
              code: 'coefficient',
              name: '系数',
              type: 'string',
              required: true
            },
            {
              code: 'phaseId',
              name: '生产阶段ID',
              type: 'string',
              required: true
            },
            { code: 'RevNo', name: '技通No', type: 'string', required: true },
            {
              code: 'monthResult',
              name: '发行日',
              type: 'string',
              required: true
            },
            {
              code: 'firstStandardWorkTitle',
              name: '首个标准工数title',
              type: 'string',
              required: true
            },
            {
              code: 'secondStandardWorkTitle',
              name: '第三个标准工数title',
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
      listReportStandardWork(
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
        modelId: null,
        model_type: null,
        coefficient: null,
        phaseId: null,
        RevNo: null,
        monthResult: null,
        firstStandardWorkTitle: null,
        secondStandardWorkTitle: null,
        comfirmBy: null,
        inChargeBy: null,
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
            ? `/edit-reportstandardwork/${id}`
            : '/add-reportstandardwork'
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
        deleteReportStandardWork(ids).then(() => {
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
      this.approveShow = true
      let data = {
        model,
        phase,
        stlst,
        name: 'standardtime'
      }
      fetchReportGroup(data).then((page) => {
        console.log(page)
      })
    },
    // 确定提交
    approvePut () {
      this.approveShow = false
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
