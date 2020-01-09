<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <div class="form-min-width">


          <el-form-item :label="'报表组'" prop="reportGroupId">
            <keyword-search
              v-model="listQuery.reportGroupId"
              
              :searchApi="this.listReportGroup"
              :allowEmpty="true" clearable>
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              v-model="listQuery.modelId"
              
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
              
              :searchApi="this.listPhase"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'下一审批者'" prop="nextApproverId">
            <el-input v-model="listQuery.nextApproverId" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'审批意见'" prop="opinion">
            <el-input v-model="listQuery.opinion" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'结果'" prop="result">
            <dict-select dictType="Result" v-model="listQuery.result" :allowEmpty="true" clearable></dict-select>
          </el-form-item>
        </div>


        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" type="primary">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">ReportApproveHistory</div>
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
        style="width: 100%;">
        <el-table-column type="selection" header-align="left" align="left" width="50"></el-table-column>

        <el-table-column label="序号" type="index"></el-table-column>


        <el-table-column align="center" prop="reportGroupName" label="报表组">
          <template slot-scope="scope">
            <span>{{scope.row.reportGroupName }}</span>
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

        <el-table-column align="center" prop="nextApproverName" label="下一审批者">
          <template slot-scope="scope">
            <span>{{scope.row.nextApproverName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="result" label="结果">
          <template slot-scope="scope">
            <span>{{ dictItemApproveResult[scope.row.result].name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="opinion" label="审批意见">
          <template slot-scope="scope">
            <span>{{scope.row.opinion }}</span>
          </template>
        </el-table-column>


        <el-table-column
          fixed="right"
          align="center"
          :label="'操作'"
          class-name="small-padding fixed-width"
          width="200">
          <template slot-scope="scope">
<!--             <el-button-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              @click="addOrUpdateHandle(scope.row.id)"-->
<!--            >编辑</el-button>-->
<!--            <el-button-->
<!--              size="mini"-->
<!--              type="text"-->
<!--            >下载</el-button>-->
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
import { listReportApproveHistory, deleteReportApproveHistory } from '@/api/reportApproveHistory'
import { listReportGroup } from '@/api/reportGroup'
import { listDict, listDictItem } from '@/api/dict'
import { listModel } from '@/api/model'
import { listPhase } from '@/api/phase'
import { keyBy } from 'lodash'

export default {
  name: 'reportApproveHistoryList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        reportApproveId: null,
        modelId: null,
        phaseId: null,
        stlst: null,
        result: null,
        opinion: null,
        reportGroupId: null,
        nextApproverId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listModel,
      listPhase,
      listDict,
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
            {
              code: 'id',
              name: 'ID',
              type: 'string',
              required: true
            },
            {
              code: 'deptId',
              name: '所属部门',
              type: 'string',
              required: true
            },
            {
              code: 'opinion',
              name: '审批意见',
              type: 'string',
              required: true
            },
            {
              code: 'result',
              name: '结果',
              type: 'string',
              required: true
            },
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
      complexFilters: [],
      dictItemApproveResult: [],
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
      listReportApproveHistory(
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
        reportApproveId: null,
        result: null,
        opinion: null,
        modelId: null,
        phaseId: null,
        stlst: null,
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
            ? `/edit-approvehistory/${id}`
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
    },
    // 字典表
    getDictByType () {
      listDictItem({ type: 'Result' }).then(({data}) => {
        this.dictItemApproveResult = keyBy(data, 'code')
      })
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
    }
  }
}
</script>

